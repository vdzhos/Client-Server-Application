package packetProcessorUnits.interfaces;

import objects.InetTarget;

public interface SenderInterface {

    void send(byte[] encodedResponse, InetTarget target) throws Exception;

}
