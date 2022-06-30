package clients.implementations;

import clients.interfaces.AbstractClient;
import enums.Command;
import objects.Message;
import objects.Packet;
import org.json.JSONObject;
import packetProcessorUnits.implementations.Decoder;
import packetProcessorUnits.implementations.Encoder;
import utils.Values;

import java.io.IOException;
import java.net.*;

public class StoreClientUDP extends AbstractClient {

    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;

    public StoreClientUDP() throws SocketException {

        socket = new DatagramSocket();
        socket.setSoTimeout(Values.SO_TIMEOUT);

        buf = new byte[Packet.PACKET_MAX_SIZE];
        try {
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Packet sendByProtocol(byte[] encodedPacket) throws Exception {
        DatagramPacket packet = new DatagramPacket(encodedPacket, encodedPacket.length, address, Values.SERVER_PORT);
        DatagramPacket response = new DatagramPacket(buf, buf.length);
        int counterOfSendTries = 0;
        boolean received = false;
        while(counterOfSendTries<Values.NUMBER_OF_SEND_TRIES){
            try {
                socket.send(packet);
                socket.receive(response);
                received = true;
                break;
            }catch (SocketTimeoutException e){
                counterOfSendTries++;
            }
        }
        if(!received){
            throw new Exception("Server is unavailable! Number of send packet tries exceeded the maximum limit!");
        }
        return Decoder.getInstance().decode(response.getData());
    }

    public void close() {
        socket.close();
    }

}