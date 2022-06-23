import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import packetProcessorUnits.implementations.ReceiverImpl;
import packetProcessorUnits.implementations.SenderImpl;
import packetProcessorUnits.interfaces.ReceiverInterface;
import utils.Values;

public class MultiThreadingTests {

    @Test
    void testNumberOfResponses(){
        ReceiverInterface receiver = ReceiverImpl.getInstance();
        try {
            receiver.startReceiving();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(Values.PACKET_GENERATE_QUANTITY,SenderImpl.getCountResponsesSent());
    }

}
