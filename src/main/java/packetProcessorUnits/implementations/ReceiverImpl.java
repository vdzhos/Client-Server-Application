package packetProcessorUnits.implementations;

import objects.Packet;
import packetProcessorUnits.interfaces.ReceiverInterface;
import utils.PacketUtils;
import utils.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ReceiverImpl implements ReceiverInterface {

    private static ReceiverImpl instance;

    private Encoder encoder;
    private Decoder decoder;
    private Processor processor;

    private boolean started = false;
    private List<Packet> packets = null;

    private ExecutorService executor;
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
        executor = Executors.newFixedThreadPool(Values.NUMBER_OF_THREADS);
    }

    private void initializeReceivingThread() {
        packetReceiver = new Thread(() -> {
            receivePacket();
            try {
                this.shutdown();
                decoder.shutdown();
                processor.shutdown();
                encoder.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        packetReceiver.setDaemon(true);

        executor = Executors.newFixedThreadPool(Values.NUMBER_OF_THREADS);
        encoder.initializeExecutor();
        decoder.initializeExecutor();
        processor.initializeExecutor();
    }

    @Override
    public void receivePacket() {
        if(packets==null){
            sendGeneratedPacketsFromManyThreads();
        }else{
            sendReadyPacketsFromManyThreads();
        }
    }

    private void shutdown() throws InterruptedException {
        executor.shutdown();
        while (!executor.awaitTermination(60L, TimeUnit.SECONDS)) {}
    }

    private void sendGeneratedPacketsFromManyThreads(){
        List<Runnable> runnables = new ArrayList<>();
        for (int i = 0; i < Values.PACKET_GENERATE_QUANTITY; i++) {
            int finalI = i;
            runnables.add(() -> {
                try {
                    Packet packet = PacketUtils.generatePacket(finalI);
                    byte[] encodedPacket = encoder.encode(packet);
                    decoder.submitDecodeTask(encodedPacket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        for (Runnable runnable : runnables) {
            executor.submit(runnable);
        }
    }

    private void sendReadyPacketsFromManyThreads(){
        List<Runnable> runnables = new ArrayList<>();
        for (Packet packet : packets) {
            runnables.add(() -> {
                try {
                    byte[] encodedPacket = encoder.encode(packet);
                    decoder.submitDecodeTask(encodedPacket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        for (Runnable runnable : runnables) {
            executor.submit(runnable);
        }
    }

    @Override
    public void startReceiving() throws InterruptedException {
        initializeReceivingThread();
        packetReceiver.start();
        packetReceiver.join();
        started = true;
    }

    @Override
    public void startReceiving(List<Packet> packets) throws InterruptedException {
        initializeReceivingThread();
        this.packets = packets;
        packetReceiver.start();
        packetReceiver.join();
    }

}