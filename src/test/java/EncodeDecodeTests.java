import enums.Command;
import objects.Message;
import objects.Packet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import packetProcessorUnits.implementations.Decoder;
import packetProcessorUnits.implementations.Encoder;

import java.util.Arrays;

public class EncodeDecodeTests {

    @Test
    void testDecodeSuccess() {
        byte[] text = "Success message".getBytes();

        Message message = new Message(Command.ADD_PRODUCT_GROUP,1, text);
        Packet initialPacket = new Packet((byte) 1,1L, message);

        try {
            byte[] encodedPacket = Encoder.getInstance().encode(initialPacket);
            Packet decodedPacket = Decoder.getInstance().decode(encodedPacket);
            System.out.println("Initial packet ------ \n" + initialPacket + "\n");
            System.out.println("Encoded packet : " + new String(encodedPacket) + "\n");
            System.out.println("Decoded packet ------ \n" + decodedPacket + "\n");
            Assertions.assertEquals(initialPacket, decodedPacket);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void sameEncodingOfTwoSamePacketsSuccess(){
        byte[] text = "some text".getBytes();

        Message message1 = new Message(Command.ADD_PRODUCT_GROUP,1, text);
        Packet packet1 = new Packet((byte) 1,1L, message1);

        Message message2 = new Message(Command.ADD_PRODUCT_GROUP,1, text);
        Packet packet2 = new Packet((byte) 1,1L, message2);

        try {
            byte[] encoded1 = Encoder.getInstance().encode(packet1);
            byte[] encoded2 = Encoder.getInstance().encode(packet2);
            Assertions.assertTrue(Arrays.equals(encoded1,encoded2));
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void testDecodeHeaderChecksumFailure() {
        byte[] text = "header checksum failure".getBytes();

        Message message = new Message(Command.ADD_PRODUCT_GROUP,1, text);
        Packet initialPacket = new Packet((byte) 1,1L, message);

        try {
            byte[] encodedPacket = Encoder.getInstance().encode(initialPacket);
            encodedPacket[14] = 0x00;
            encodedPacket[15] = 0x00;
            Decoder.getInstance().decode(encodedPacket);
            // incorrect crc
            Assertions.fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.assertTrue(true);
        }
    }

    @Test
    void testDecodeMessageChecksumFailure() {
        byte[] text = "message checksum failure".getBytes();

        Message message = new Message(Command.DECREASE_PRODUCT_QUANTITY,5, text);
        Packet initialPacket = new Packet((byte) 5,5L, message);

        try {
            byte[] encodedPacket = Encoder.getInstance().encode(initialPacket);
            encodedPacket[encodedPacket.length-2] = 0x00;
            encodedPacket[encodedPacket.length-1] = 0x00;
            Decoder.getInstance().decode(encodedPacket);
            // incorrect crc
            Assertions.fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.assertTrue(true);
        }
    }

}
