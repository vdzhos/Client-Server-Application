import enums.Command;
import objects.Message;
import objects.Packet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class EncodeDecodeTests {

    @Test
    void testDecodeSuccess() {
        byte[] text = "Success message".getBytes();

        Message message = new Message(Command.UPDATE,1, text);
        Packet initialPacket = new Packet((byte) 1,1L, message);

        try {
            byte[] encodedPacket = PacketProcessor.encode(initialPacket);
            Packet decodedPacket = PacketProcessor.decode(encodedPacket);
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

        Message message1 = new Message(Command.UPDATE,1, text);
        Packet packet1 = new Packet((byte) 1,1L, message1);

        Message message2 = new Message(Command.UPDATE,1, text);
        Packet packet2 = new Packet((byte) 1,1L, message2);

        try {
            byte[] encoded1 = PacketProcessor.encode(packet1);
            byte[] encoded2 = PacketProcessor.encode(packet2);
            Assertions.assertTrue(Arrays.equals(encoded1,encoded2));
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void testDecodeHeaderChecksumFailure() {
        byte[] text = "header checksum failure".getBytes();

        Message message = new Message(Command.UPDATE,1, text);
        Packet initialPacket = new Packet((byte) 1,1L, message);

        try {
            byte[] encodedPacket = PacketProcessor.encode(initialPacket);
            encodedPacket[14] = 0x00;
            encodedPacket[15] = 0x00;
            PacketProcessor.decode(encodedPacket);
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

        Message message = new Message(Command.DELETE,5, text);
        Packet initialPacket = new Packet((byte) 5,5L, message);

        try {
            byte[] encodedPacket = PacketProcessor.encode(initialPacket);
            encodedPacket[encodedPacket.length-2] = 0x00;
            encodedPacket[encodedPacket.length-1] = 0x00;
            PacketProcessor.decode(encodedPacket);
            // incorrect crc
            Assertions.fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assertions.assertTrue(true);
        }
    }

}
