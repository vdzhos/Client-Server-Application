package packetProcessorUnits;

import objects.Message;
import objects.Packet;
import utils.CBCUtils;
import utils.PacketUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Encoder {

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

            byte[] messageEncrypted = encryptMessage(message.getMessage());
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

    private static byte[] encryptMessage(byte[] message) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, CBCUtils.key, CBCUtils.ivSpec);
        return cipher.doFinal(message);
    }

}
