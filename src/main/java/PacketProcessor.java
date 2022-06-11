import objects.Message;
import objects.Packet;
import utils.PacketUtils;

import javax.crypto.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class PacketProcessor {

    private static SecretKey key;

    static {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(128);
            key = generator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static byte[] encode(Packet packet){
        byte[] messageBytes = encodeMessage(packet.getBMsg());

        byte[] bPktId = ByteBuffer.allocate(Long.BYTES).putLong(packet.getBPktId()).order(ByteOrder.BIG_ENDIAN).array();
        byte[] wLen = ByteBuffer.allocate(Integer.BYTES).putInt(messageBytes.length).order(ByteOrder.BIG_ENDIAN).array();

        byte[] header = ByteBuffer.allocate(Packet.BytesSize.allExceptMessage)
                .put(packet.getBMagic())
                .put(packet.getBSrc())
                .put(bPktId)
                .put(wLen)
                .array();

        byte[] wCrc16 = ByteBuffer.allocate(Short.BYTES).putShort(PacketUtils.crc16(header))
                .order(ByteOrder.BIG_ENDIAN).array();
        byte[] w2Crc16 = ByteBuffer.allocate(Short.BYTES).putShort(PacketUtils.crc16(messageBytes))
                .order(ByteOrder.BIG_ENDIAN).array();

        return ByteBuffer.allocate(header.length + Packet.BytesSize.wCrc16 +
                messageBytes.length + Packet.BytesSize.w2Crc16)
                .put(header)
                .put(wCrc16)
                .put(messageBytes)
                .put(w2Crc16)
                .array();
    }

    private static byte[] encodeMessage(Message message){
        try {
            byte[] cTypeEndian = ByteBuffer.allocate(Integer.BYTES).putInt(message.getCType().code).order(ByteOrder.BIG_ENDIAN).array();
            byte[] bUserIdEndian = ByteBuffer.allocate(Integer.BYTES).putInt(message.getBUserId()).order(ByteOrder.BIG_ENDIAN).array();

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,key);
            byte[] messageEncrypted = cipher.doFinal(message.getMessage());
            byte[] messageEndian = ByteBuffer.allocate(messageEncrypted.length).put(messageEncrypted).order(ByteOrder.BIG_ENDIAN).array();

            return ByteBuffer.allocate(Message.BytesSize.allExceptMessage + messageEndian.length)
                    .put(cTypeEndian)
                    .put(bUserIdEndian)
                    .put(messageEndian)
                    .array();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException();
        }
    }

}
