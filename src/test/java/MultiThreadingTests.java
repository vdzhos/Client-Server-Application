import enums.Command;
import model.Product;
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
//        Assertions.assertEquals(Values.PACKET_GENERATE_QUANTITY, SenderImpl.getCountResponsesSent());
    }

    @Test
    void testDataIntegrity1(){
        Storage storage = Storage.getInstance();
        Product product = storage.getProductMap().get("Product0");
        int initialQuantity = product.getQuantity();

        JSONObject jsonObject = new JSONObject();
        String productName = "Product0";
        jsonObject.put(Values.PRODUCT_NAME_COMMAND_PARAM, productName);
        jsonObject.put(Values.QUANTITY_COMMAND_PARAM, 10);

        byte[] messageBytes = jsonObject.toString().getBytes();
        Message message = new Message(Command.INCREASE_PRODUCT_QUANTITY, 1, messageBytes);
        Packet packet = new Packet((byte) 1, message);

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
        int currentQuantity = product.getQuantity();
        Assertions.assertEquals(initialQuantity + 20, currentQuantity);
    }

    @Test
    void testDataIntegrity2(){
        Storage storage = Storage.getInstance();
        Product product = storage.getProductMap().get("Product0");
        product.setQuantity(10);
        int initialQuantity = product.getQuantity();

        JSONObject jsonObject = new JSONObject();
        String productName = "Product0";
        jsonObject.put(Values.PRODUCT_NAME_COMMAND_PARAM, productName);
        jsonObject.put(Values.QUANTITY_COMMAND_PARAM, 10);

        byte[] messageBytes1 = jsonObject.toString().getBytes();
        Message message1 = new Message(Command.INCREASE_PRODUCT_QUANTITY, 1, messageBytes1);
        Packet packet1 = new Packet((byte) 1, message1);

        jsonObject.put(Values.QUANTITY_COMMAND_PARAM, 5);
        byte[] messageBytes2 = jsonObject.toString().getBytes();
        Message message2 = new Message(Command.DECREASE_PRODUCT_QUANTITY, 1, messageBytes2);
        Packet packet2 = new Packet((byte) 1, message2);

        jsonObject.put(Values.QUANTITY_COMMAND_PARAM, 7);
        byte[] messageBytes3 = jsonObject.toString().getBytes();
        Message message3 = new Message(Command.INCREASE_PRODUCT_QUANTITY, 1, messageBytes3);
        Packet packet3 = new Packet((byte) 1, message3);

        List<Packet> packetList = new ArrayList<>();
        packetList.add(packet1);
        packetList.add(packet2);
        packetList.add(packet3);

        ReceiverInterface receiver = ReceiverImpl.getInstance();
        try {
            receiver.startReceiving(packetList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int currentQuantity = product.getQuantity();
        Assertions.assertEquals(initialQuantity + 12, currentQuantity);
    }

    @Test
    void testDataIntegrity3(){
        Storage storage = Storage.getInstance();
        int initialGroupNumber = storage.getGroupMap().size();

        JSONObject jsonObject = new JSONObject();
        String groupName = "Group228";
        jsonObject.put(Values.GROUP_NAME_COMMAND_PARAM, groupName);
        byte[] messageBytes = jsonObject.toString().getBytes();
        Message message = new Message(Command.ADD_PRODUCT_GROUP, 1, messageBytes);
        Packet packet = new Packet((byte) 1, message);

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
}
