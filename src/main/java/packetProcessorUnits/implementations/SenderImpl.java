package packetProcessorUnits.implementations;

import objects.Packet;
import packetProcessorUnits.interfaces.SenderInterface;

public class SenderImpl implements SenderInterface {

    private static SenderImpl instance;
    private static long countResponsesSent = 0;

    public static SenderImpl getInstance() {
        if (instance == null)
            instance = new SenderImpl();
        return instance;
    }

    private SenderImpl() {}

    @Override
    public void send(byte[] encodedResponse) throws Exception {
        countResponsesSent++;
        Packet decodedPacket = Decoder.getInstance().decode(encodedResponse);
        System.out.println(decodedPacket);
        System.out.println("--------------------------");
    }

    public static long getCountResponsesSent() {
        return countResponsesSent;
    }
}
