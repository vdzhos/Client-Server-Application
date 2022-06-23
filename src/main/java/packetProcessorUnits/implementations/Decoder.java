package packetProcessorUnits.implementations;

import enums.Command;
import objects.Message;
import objects.Packet;
import utils.CBCUtils;
import utils.PacketUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Decoder {

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

            byte[] messageDecoded = decryptMessage(messageEncrypted);

            return new Message(Command.valueOf(cType), bUserId, messageDecoded);
        }  catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | IllegalBlockSizeException | BadPaddingException
                | InvalidAlgorithmParameterException e) {
            throw new Exception(e.getMessage());
        }
    }

    private static byte[] decryptMessage(byte[] messageEncrypted) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, CBCUtils.key, CBCUtils.ivSpec);
        return cipher.doFinal(messageEncrypted);
    }

}
