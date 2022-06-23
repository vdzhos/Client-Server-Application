package packetProcessorUnits.interfaces;

public interface ReceiverInterface {

    void receivePacket();

    void startReceiving() throws InterruptedException;
}
