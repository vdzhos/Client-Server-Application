package packetProcessorUnits.implementations;

import model.Storage;
import objects.Packet;
import packetProcessorUnits.interfaces.ReceiverInterface;
import utils.PacketUtils;

import java.util.List;
import java.util.Random;

public class ReceiverImpl implements ReceiverInterface {

    private Thread packetReceiver;
    private int packetQuantity;

    private Storage storage;

    public ReceiverImpl(int packetQuantity) {
        this.packetQuantity = packetQuantity;
        this.storage = Storage.getInstance();
        packetReceiver = new Thread(new Runnable() {
            @Override
            public void run() {
                receivePacket();
            }
        });
        packetReceiver.setDaemon(true);
    }

    @Override
    public void receivePacket() {
        for (int i = 0; i < packetQuantity; i++) {
            try {
                long delayTime = new Random().nextInt(2000);
                Thread.sleep(delayTime);
                Packet packet = PacketUtils.generatePacket(i);
                System.out.println(packet);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}