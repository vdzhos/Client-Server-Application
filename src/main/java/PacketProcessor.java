import enums.Command;
import objects.Message;
import objects.Packet;
import utils.PacketUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class PacketProcessor {

    private static SecretKey key;
    private static final byte[] IV = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private static IvParameterSpec ivSpec;

    static {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(128);
            key = generator.generateKey();
            ivSpec = new IvParameterSpec(IV);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static byte[] encode(Packet packet) throws Exception {
        byte[] messageBytes = encodeMessage(packet.getBMsg());

        byte[] bPktId = ByteBuffer.allocate(Long.BYTES).putLong(packet.getBPktId()).order(ByteOrder.BIG_ENDIAN).array();
        byte[] wLen = ByteBuffer.allocate(Integer.BYTES).putInt(messageBytes.length).order(ByteOrder.BIG_ENDIAN).array();

        byte[] header = ByteBuffer.allocate(Packet.BytesSize.HEADER_SIZE)
                .put(Packet.B_MAGIC)
                .put(packet.getBSrc())
                .put(bPktId)
                .put(wLen)
                .array();

        byte[] wCrc16 = ByteBuffer.allocate(Short.BYTES).putShort(PacketUtils.crc16(header))
                .order(ByteOrder.BIG_ENDIAN).array();
        byte[] w2Crc16 = ByteBuffer.allocate(Short.BYTES).putShort(PacketUtils.crc16(messageBytes))
                .order(ByteOrder.BIG_ENDIAN).array();

        return ByteBuffer.allocate(Packet.BytesSize.HEADER_SIZE + Packet.BytesSize.W_CRC_16 +
                messageBytes.length + Packet.BytesSize.W_2_CRC_16)
                .put(header)
                .put(wCrc16)
                .put(messageBytes)
                .put(w2Crc16)
                .array();
    }

    private static byte[] encodeMessage(Message message) throws Exception {
        try {
            byte[] cTypeEndian = ByteBuffer.allocate(Integer.BYTES).putInt(message.getCType().code).order(ByteOrder.BIG_ENDIAN).array();
            byte[] bUserIdEndian = ByteBuffer.allocate(Integer.BYTES).putInt(message.getBUserId()).order(ByteOrder.BIG_ENDIAN).array();

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            byte[] messageEncrypted = cipher.doFinal(message.getMessage());
            byte[] messageEndian = ByteBuffer.allocate(messageEncrypted.length).put(messageEncrypted).order(ByteOrder.BIG_ENDIAN).array();

            return ByteBuffer.allocate(Message.BytesSize.ALL_EXCEPT_MESSAGE + messageEndian.length)
                    .put(cTypeEndian)
                    .put(bUserIdEndian)
                    .put(messageEndian)
                    .array();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | IllegalBlockSizeException | BadPaddingException
                | InvalidAlgorithmParameterException e) {
            throw new Exception(e.getMessage());
        }
    }

    public static Packet decode(byte[] bytes) throws Exception {
        ByteBuffer wrapBytes = ByteBuffer.wrap(bytes);

        byte bMagic = wrapBytes.get();
        if(bMagic != Packet.B_MAGIC)
            throw new Exception("B_MAGIC is incorrect.");
        // search start of the packet by bMagic needs to be added
        // multiple packet decoding needs to be added

        byte bSrc = wrapBytes.get();
        long bPktId = wrapBytes.getLong();
        int wLen = wrapBytes.getInt();

        byte[] header = new byte[Packet.BytesSize.HEADER_SIZE];
        System.arraycopy(bytes, 0, header, 0, header.length);

        short computedWCrc16 = PacketUtils.crc16(header);
        short decodedWCrc16 = wrapBytes.getShort();
        if(computedWCrc16 != decodedWCrc16)
            throw new Exception("Header checksum doesn't match.");

        byte[] messageBytes = new byte[wLen];
        System.arraycopy(bytes, Packet.BytesSize.HEADER_SIZE + Packet.BytesSize.W_CRC_16, messageBytes, 0, wLen);


        short computedW2Crc16 = PacketUtils.crc16(messageBytes);
        short decodedW2Crc16 = wrapBytes.getShort(bytes.length - Packet.BytesSize.W_2_CRC_16);
        if(computedW2Crc16 != decodedW2Crc16)
            throw new Exception("Message checksum doesn't match.");

        Message message = decodeMessage(messageBytes);

        return new Packet(bSrc, bPktId, message);
    }

    private static Message decodeMessage(byte[] bytes) throws Exception {
        try {
            ByteBuffer wrapBytes = ByteBuffer.wrap(bytes);

            int cType = wrapBytes.getInt();
            int bUserId = wrapBytes.getInt();

            int messageOffset = Message.BytesSize.C_TYPE + Message.BytesSize.B_USER_ID;
            int messageLength = bytes.length - messageOffset;
            byte[] messageEncrypted = new byte[messageLength];
            System.arraycopy(bytes, messageOffset, messageEncrypted, 0, messageLength);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            byte[] messageDecoded = cipher.doFinal(messageEncrypted);

            return new Message(Command.valueOf(cType), bUserId, messageDecoded);
        }  catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | IllegalBlockSizeException | BadPaddingException
                | InvalidAlgorithmParameterException e) {
            throw new Exception(e.getMessage());
        }
    }
}
