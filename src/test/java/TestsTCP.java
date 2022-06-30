import clients.implementations.StoreClientTCP;
import enums.Command;
import lombok.Value;
import model.Storage;
import objects.Packet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import servers.implementations.StoreServerTCP;
import servers.implementations.StoreServerUDP;
import utils.Values;

import java.io.IOException;
import java.net.SocketException;

public class TestsTCP {

    private static StoreServerTCP server;

    @BeforeAll
    static void beforeAll() throws IOException {
        server = new StoreServerTCP();
        server.start();
    }

    @AfterAll
    static void afterAll(){
        server.shutdown();
    }

    @Test
    void testTCPPacketTooLarge() throws InterruptedException {
        String groupName = "Group123123"
                + "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "Group12312311111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111"
                + "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "Group12312311111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111"
                + "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "Group12312311111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111"
                + "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "Group12312311111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111"
                + "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "Group12312311111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111"
                + "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "Group12312311111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111"
                + "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "Group12312311111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
        Thread client1Thread = new Thread(() -> {
            try {
                StoreClientTCP client1 = new StoreClientTCP();
                client1.startConnection(Values.SERVER_IP, Values.SERVER_PORT);
                Packet response = client1.sendCommandAndReceive(Command.ADD_PRODUCT_GROUP,groupName);
                System.out.println(response);
                client1.close();
            } catch (Exception e) {
                Assertions.assertTrue(true);
                e.printStackTrace();
            }
        });
        client1Thread.setDaemon(true);
        client1Thread.start();
        client1Thread.join();
        Storage storage = Storage.getInstance();
        Assertions.assertFalse(storage.getGroupMap().containsKey(groupName));
    }

    @Test
    void testTCPPacketCommandExecutedSuccessfully() throws InterruptedException {
        String groupName = "Group123123";
        Thread client1Thread = new Thread(() -> {
            try {
                StoreClientTCP client1 = new StoreClientTCP();
                client1.startConnection(Values.SERVER_IP, Values.SERVER_PORT);
                Packet response = client1.sendCommandAndReceive(Command.ADD_PRODUCT_GROUP,groupName);
                System.out.println(response);
                client1.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        client1Thread.setDaemon(true);
        client1Thread.start();
        client1Thread.join();
        Storage storage = Storage.getInstance();
        Assertions.assertTrue(storage.getGroupMap().containsKey(groupName));
    }

    @Test
    void testTCPPacketSeveralClientsCommandsExecutedSuccessfully() throws InterruptedException {
        String groupName = "Group123123555";
        String productName = "Product0";
        String product1Name = "Product1";
        int quantity = 50;
        double price = 123123123.55;
        Storage storage = Storage.getInstance();
        int productQInit = storage.getProductMap().get(product1Name).getQuantity();

        Thread client1Thread = new Thread(() -> {
            try {
                StoreClientTCP client1 = new StoreClientTCP();
                client1.startConnection(Values.SERVER_IP, Values.SERVER_PORT);
                Packet response = client1.sendCommandAndReceive(Command.INCREASE_PRODUCT_QUANTITY,product1Name,quantity);
                System.out.println(response);
                Packet response2 = client1.sendCommandAndReceive(Command.SET_PRODUCT_PRICE,productName,price);
                System.out.println(response2);
                client1.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Thread client2Thread = new Thread(() -> {
            try {
                StoreClientTCP client2 = new StoreClientTCP();
                client2.startConnection(Values.SERVER_IP, Values.SERVER_PORT);
                Packet response = client2.sendCommandAndReceive(Command.INCREASE_PRODUCT_QUANTITY,product1Name,quantity);
                System.out.println(response);
                Packet response2 = client2.sendCommandAndReceive(Command.ADD_PRODUCT_GROUP,groupName);
                System.out.println(response2);
                client2.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        client1Thread.setDaemon(true);
        client1Thread.start();
        client2Thread.setDaemon(true);
        client2Thread.start();

        client1Thread.join();
        client2Thread.join();

        int productQCurrent = storage.getProductMap().get(product1Name).getQuantity();
        double productPCurrent = storage.getProductMap().get(productName).getPrice();
        Assertions.assertEquals(productQInit+2*quantity,productQCurrent);
        Assertions.assertTrue(storage.getGroupMap().containsKey(groupName));
        Assertions.assertEquals(price,productPCurrent);
    }

    @Test
    void testTCPClientRestoresConnection() throws InterruptedException {
        String groupName = "Group123123";
        Thread client1Thread = new Thread(() -> {
            try {
                StoreClientTCP client1 = new StoreClientTCP();
                client1.startConnection(Values.SERVER_IP, Values.SERVER_PORT);
                client1.close();
                Packet response = client1.sendCommandAndReceive(Command.ADD_PRODUCT_GROUP,groupName);
                System.out.println(response);
                client1.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        client1Thread.setDaemon(true);
        client1Thread.start();
        client1Thread.join();
        Storage storage = Storage.getInstance();
        Assertions.assertTrue(storage.getGroupMap().containsKey(groupName));
    }

}
