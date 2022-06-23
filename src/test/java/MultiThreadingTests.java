import enums.Command;
import model.Storage;
import objects.Message;
import objects.Packet;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import packetProcessorUnits.implementations.ReceiverImpl;
import packetProcessorUnits.implementations.SenderImpl;
import packetProcessorUnits.interfaces.ReceiverInterface;
import utils.Values;

import javax.print.attribute.standard.MediaSize;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MultiThreadingTests {

    @AfterEach
    void initStorage() {
        Storage.getInstance().initData();
    }

    @Test
    void testNumberOfResponses(){
        ReceiverInterface receiver = ReceiverImpl.getInstance();
        try {
            receiver.startReceiving();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(Values.PACKET_GENERATE_QUANTITY, SenderImpl.getCountResponsesSent());
    }


    @Test
    void addProductGroup(){
        Storage storage = Storage.getInstance();
        int initialGroupNumber = storage.getGroupMap().size();

        JSONObject jsonObject = new JSONObject();
        String groupName = "Group228";
        jsonObject.put(Values.GROUP_NAME_COMMAND_PARAM, groupName);
        byte[] messageBytes = jsonObject.toString().getBytes();
        Message message = new Message(Command.ADD_PRODUCT_GROUP, 1, messageBytes);
        Packet packet = new Packet((byte) 1,1L, message);

        List<Packet> packetList = new ArrayList<>();
        packetList.add(packet);
        packet.setBPktId(3);
        packetList.add(packet);

        ReceiverInterface receiver = ReceiverImpl.getInstance();
        try {
            receiver.startReceiving(packetList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int currentGroupNumber = storage.getGroupMap().size();
        Assertions.assertEquals(initialGroupNumber + 1, currentGroupNumber);
    }

//    @Test
//    void addProductToGroup(){
//        Storage storage = Storage.getInstance();
//        int initialProductNumber = storage.getProductMap().size();
//        int initialProductNumberInGroup = storage.getGroupMap().size();
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put(Values.GROUP_NAME_COMMAND_PARAM, "Group228");
//        byte[] messageBytes = jsonObject.toString().getBytes();
//        Message message = new Message(Command.ADD_PRODUCT_GROUP, 1, messageBytes);
//        Packet packet = new Packet((byte) 1,1L, message);
//
//        List<Packet> packetList = new ArrayList<>();
//        packetList.add(packet);
//        packet.setBPktId(3);
//        packetList.add(packet);
//
//        ReceiverInterface receiver = ReceiverImpl.getInstance();
//        try {
//            receiver.startReceiving(packetList);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        int currentGroupNumber = storage.getGroupMap().size();
//        Assertions.assertEquals(initialGroupNumber + 1, currentGroupNumber);
//    }
}
