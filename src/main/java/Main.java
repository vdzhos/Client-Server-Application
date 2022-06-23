import enums.Command;
import objects.Message;
import objects.Packet;
import packetProcessorUnits.Decoder;
import packetProcessorUnits.Encoder;

public class Main {

    public static void main(String[] args) {
        byte[] text = "test_message".getBytes();
        Message message = new Message(Command.INCREASE_PRODUCT_QUANTITY,5, text);
        Packet packet = new Packet((byte) 1,2L, message);
        try {
            byte[] encodedPacket = Encoder.encode(packet);
            Packet decodedPacket = Decoder.decode(encodedPacket);
            System.out.println("Initial packet ------ \n" + packet + "\n");
            System.out.println("Encoded packet : " + new String(encodedPacket) + "\n");
            System.out.println("Decoded packet ------ \n" + decodedPacket + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
