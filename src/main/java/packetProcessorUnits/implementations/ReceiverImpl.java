package packetProcessorUnits.implementations;

import objects.Packet;
import packetProcessorUnits.interfaces.ReceiverInterface;
import utils.PacketUtils;

import java.util.Random;

public class ReceiverImpl implements ReceiverInterface {

    private static final int PACKET_QUANTITY = 10;

    private static ReceiverImpl instance;

    private Encoder encoder;
    private Decoder decoder;
    private Processor processor;

    private Thread packetReceiver;

    public static ReceiverImpl getInstance() {
        if (instance == null)
            instance = new ReceiverImpl();
        return instance;
    }

    private ReceiverImpl() {
        this.encoder = Encoder.getInstance();
        this.decoder = Decoder.getInstance();
        this.processor = Processor.getInstance();
        packetReceiver = new Thread(() -> {
            receivePacket();
            try {
                decoder.shutdown();
                processor.shutdown();
                encoder.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        packetReceiver.setDaemon(true);
    }

    @Override
    public void receivePacket() {
        for (int i = 0; i < PACKET_QUANTITY; i++) {
            try {
                long delayTime = new Random().nextInt(750);
//                Thread.sleep(delayTime);
                Thread.sleep(0);
                Packet packet = PacketUtils.generatePacket(i);
                byte[] encodedPacket = encoder.encode(packet);
                decoder.submitDecodeTask(encodedPacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void startReceiving() throws InterruptedException {
        packetReceiver.start();
        packetReceiver.join();
    }

}