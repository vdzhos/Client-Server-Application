package packetProcessorUnits.implementations;

import enums.Command;
import objects.Message;
import objects.Packet;
import utils.CBCUtils;
import utils.PacketUtils;
import utils.Values;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Decoder {

    private static Decoder instance;

    private ExecutorService executor;
    private Processor processor;

    public static Decoder getInstance() {
        if (instance == null)
            instance = new Decoder();
        return instance;
    }

    private Decoder() {
        executor = Executors.newFixedThreadPool(Values.NUMBER_OF_THREADS);
        processor = Processor.getInstance();
    }

    public void submitDecodeTask(byte[] packetBytes) {
        executor.submit(() -> {
            try {
                Packet packet = decode(packetBytes);
                processor.submitProcessTask(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void shutdown() throws InterruptedException {
        executor.shutdown();
        while (!executor.awaitTermination(60L, TimeUnit.SECONDS)) {}
    }

    public Packet decode(byte[] bytes) throws Exception {
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

//        System.out.println(Thread.currentThread());
        return new Packet(bSrc, bPktId, message);
    }

    private Message decodeMessage(byte[] bytes) throws Exception {
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

    private byte[] decryptMessage(byte[] messageEncrypted) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, CBCUtils.key, CBCUtils.ivSpec);
        return cipher.doFinal(messageEncrypted);
    }

    public void initializeExecutor() {
        executor = Executors.newFixedThreadPool(Values.NUMBER_OF_THREADS);
    }
}
