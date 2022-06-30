package servers.implementations;

import objects.InetTarget;
import objects.Packet;
import packetProcessorUnits.implementations.Decoder;
import packetProcessorUnits.implementations.Encoder;
import packetProcessorUnits.implementations.Processor;
import servers.interfaces.ServerInterface;
import utils.Values;

import java.io.*;
import java.net.*;

public class StoreServerTCP extends Thread implements ServerInterface {

    private ServerSocket serverSocket;

    private Encoder encoder;
    private Decoder decoder;
    private Processor processor;
    private boolean running;

    public StoreServerTCP() throws IOException {
        serverSocket = new ServerSocket(Values.SERVER_PORT);
        this.encoder = Encoder.getInstance();
        this.decoder = Decoder.getInstance();
        this.processor = Processor.getInstance();
        encoder.initializeExecutor();
        decoder.initializeExecutor();
        processor.initializeExecutor();

    }

    @Override
    public void run() {
        running = true;

        while (running) {
            try {
                new ClientHandlerTCP(serverSocket.accept(), this).start();
            } catch (IOException ignored) {}
        }
    }

    public void shutdown() {
        try {
            running = false;
            serverSocket.close();
            decoder.shutdown();
            processor.shutdown();
            encoder.shutdown();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }





    private static class ClientHandlerTCP extends Thread {
        private Socket clientSocket;
        private InputStream in;

        private StoreServerTCP storeServerTCP;
        private Encoder encoder;
        private Decoder decoder;
        private Processor processor;

        private boolean running;
        private byte[] buf;

        public ClientHandlerTCP(Socket socket, StoreServerTCP storeServerTCP) {
            this.storeServerTCP = storeServerTCP;
            this.clientSocket = socket;
            this.encoder = storeServerTCP.encoder;
            this.processor = storeServerTCP.processor;
            this.decoder = storeServerTCP.decoder;
            buf = new byte[Packet.PACKET_MAX_SIZE];
        }

        public void run() {
            running = true;

            try {
                in = clientSocket.getInputStream();

                while (running && storeServerTCP.running) {
                    int readBytes = in.read(buf);

                    if (clientSocket.isClosed() || readBytes == -1) {
                        running = false;
                    } else {
                        InetTarget inetTarget = new InetTarget(clientSocket);
                        decoder.submitDecodeTask(buf, inetTarget);
                    }
                }

                in.close();
                clientSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
