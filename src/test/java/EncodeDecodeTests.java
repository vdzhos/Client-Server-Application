import enums.Command;
import objects.Message;
import objects.Packet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
    void testDecodeFailure() {
        byte[] text = "Failure message".getBytes();

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

}
