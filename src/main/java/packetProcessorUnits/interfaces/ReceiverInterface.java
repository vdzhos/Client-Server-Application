package packetProcessorUnits.interfaces;

import objects.Packet;

import java.util.List;

public interface ReceiverInterface {

    void receivePacket();

    void startReceiving() throws InterruptedException;

    void startReceiving(List<Packet> packets) throws InterruptedException;
}
