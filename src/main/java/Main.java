import model.Storage;
import packetProcessorUnits.implementations.ReceiverImpl;
import packetProcessorUnits.interfaces.ReceiverInterface;
import utils.PacketUtils;

import javax.sound.midi.Receiver;

public class Main {

    public static void main(String[] args) {

//        for (int i = 0; i < 3; i++) {
//            System.out.println(PacketUtils.generatePacket(i));
//            System.out.println("\n----------------------------------------------------------\n");
//        }

        ReceiverInterface receiver = ReceiverImpl.getInstance();
        try {
            receiver.startReceiving();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("End of main...");
    }

}
