package packetProcessorUnits.implementations;

import objects.Packet;
import packetProcessorUnits.interfaces.SenderInterface;

public class SenderImpl implements SenderInterface {

    private static SenderImpl instance;

    public static SenderImpl getInstance() {
        if (instance == null)
            instance = new SenderImpl();
        return instance;
    }

    private SenderImpl() {}

    @Override
    public void send(byte[] encodedResponse) throws Exception {
        Packet decodedPacket = Decoder.getInstance().decode(encodedResponse);
        System.out.println(decodedPacket);
        System.out.println("--------------------------");
    }
}
