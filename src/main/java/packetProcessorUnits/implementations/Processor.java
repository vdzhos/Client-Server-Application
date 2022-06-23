package packetProcessorUnits.implementations;

import enums.Command;
import model.Storage;
import objects.Message;
import objects.Packet;
import utils.Values;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Processor {

    private static Processor instance;

    private ExecutorService executor;
    private Encoder encoder;
    private Storage storage;

    public static Processor getInstance() {
        if (instance == null)
            instance = new Processor();
        return instance;
    }

    private Processor() {
        executor = Executors.newFixedThreadPool(Values.NUMBER_OF_THREADS);
        storage = Storage.getInstance();
        encoder = Encoder.getInstance();
    }

    public void submitProcessTask(Packet packet) {
        executor.submit(() -> {
            Message responseMessage = executeCommand(packet.getBMsg());
            packet.setBSrc(Values.B_SRC);
            packet.setBPktId(packet.getBPktId()+1);
            packet.setBMsg(responseMessage);
            encoder.submitEncodeTask(packet);
        });
    }

    public void shutdown() throws InterruptedException {
        executor.shutdown();
        while (!executor.awaitTermination(60L, TimeUnit.SECONDS)) {}
    }

    private Message executeCommand(Message message) {

//        System.out.println(Thread.currentThread());

        message.setCType(Command.RESPONSE_OK);
        message.setMessage(new byte[]{});
        return message;
    }
}
