package servers.implementations;

import objects.InetTarget;
import objects.Packet;
import packetProcessorUnits.implementations.Decoder;
import packetProcessorUnits.implementations.Encoder;
import packetProcessorUnits.implementations.Processor;
import servers.interfaces.ServerInterface;
import utils.Values;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class StoreServerUDP extends Thread implements ServerInterface {

    public static final int BUFFER_SIZE = Packet.PACKET_MAX_SIZE;

    private DatagramSocket datagramSocket;
    private boolean running;
    private byte[] buf = new byte[BUFFER_SIZE];

    private Encoder encoder;
    private Decoder decoder;
    private Processor processor;

    public StoreServerUDP() throws SocketException {
        this.encoder = Encoder.getInstance();
        this.decoder = Decoder.getInstance();
        this.processor = Processor.getInstance();
        this.setDaemon(true);
        datagramSocket = new DatagramSocket(Values.SERVER_PORT);
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {

                byte[] encodedPacket = getNextPacket(packet);
                if(encodedPacket==null) continue;

                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                InetTarget inetTarget = new InetTarget(datagramSocket,address,port);

                if(!datagramSocket.isClosed()){
                    decoder.submitDecodeTask(encodedPacket,inetTarget);
                }

//                if (received.equals("end")) {
//                    running = false;
//                    continue;
//                }
            }catch (IOException e){
                e.printStackTrace();
                shutdown();
            }
        }
        releaseResources();
    }

    private byte[] getNextPacket(DatagramPacket packet) throws IOException {
        try {
            datagramSocket.receive(packet);
        }catch (IOException ignored){
            return null;
        }

        if(buf[0] != Packet.B_MAGIC){
            return null;
        }

        ByteBuffer buffer = ByteBuffer.wrap(buf);
        int wLen = buffer.getInt(Packet.BytesConstants.W_LEN_OFFSET);
        int packetLength = Packet.BytesConstants.ALL_EXCEPT_MESSAGE + wLen;

        if(packetLength>BUFFER_SIZE){
            return null;
        }

        byte[] packetBytes = new byte[packetLength];

        buffer.get(packetBytes,0,packetLength);

        return packetBytes;
    }

    public void shutdown(){
        running = false;
        datagramSocket.close();
    }

    private void releaseResources(){
        try {
            decoder.shutdown();
            processor.shutdown();
            encoder.shutdown();
            datagramSocket.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}