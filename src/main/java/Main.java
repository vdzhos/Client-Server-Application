import enums.Command;
import objects.Message;
import objects.Packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        byte[] text = "test".getBytes();
        Message message = new Message(Command.CREATE,5,text);
        Packet packet = new Packet((byte) 1,2L,message);
        byte[] res = PacketProcessor.encode(packet);
        System.out.println(new String(res));
    }

}
