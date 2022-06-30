package packetProcessorUnits.implementations;

import enums.Protocol;
import objects.InetTarget;
import objects.Packet;
import packetProcessorUnits.interfaces.SenderInterface;

import java.io.IOException;
import java.net.DatagramPacket;

public class SenderImpl implements SenderInterface {

    private static SenderImpl instance;
    private static boolean unreachablePacket = false;

    public static SenderImpl getInstance() {
        if (instance == null)
            instance = new SenderImpl();
        return instance;
    }

    private SenderImpl() {}

    @Override
    public void send(byte[] encodedResponse, InetTarget target){
        try{
            while (unreachablePacket){}
            if(target.getProtocol()== Protocol.UDP){
                sendByUDP(encodedResponse, target);
            }else {
                sendByTCP(encodedResponse, target);
            }
        }catch (IOException ignored){}
    }

    private void sendByUDP(byte[] encodedResponse, InetTarget target) throws IOException {
        DatagramPacket packet = new DatagramPacket(encodedResponse, encodedResponse.length,
                target.getAddress(), target.getPort());
        target.getUdpSocket().send(packet);

    }

    private void sendByTCP(byte[] encodedResponse, InetTarget target) throws IOException {
        target.getTcpSocket().getOutputStream().write(encodedResponse);
    }

    public static boolean isUnreachablePacket() {
        return unreachablePacket;
    }

    public static void setUnreachablePacket(boolean unreachablePacket) {
        SenderImpl.unreachablePacket = unreachablePacket;
    }
}
