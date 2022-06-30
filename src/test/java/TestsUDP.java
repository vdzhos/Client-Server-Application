import clients.implementations.StoreClientUDP;
import enums.Command;
import model.Storage;
import objects.Packet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import packetProcessorUnits.implementations.SenderImpl;
import servers.implementations.StoreServerUDP;

import java.net.SocketException;

public class TestsUDP {

    private static StoreServerUDP server;

    @BeforeAll
    static void beforeAll() throws SocketException {
        server = new StoreServerUDP();
        server.start();
    }

    @AfterAll
    static void afterAll(){
        server.shutdown();
    }

    @Test
    void testUDPPacketTooLarge() throws InterruptedException {
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
                StoreClientUDP client1 = new StoreClientUDP();
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
    void testUDPPacketCommandExecutedSuccessfully() throws InterruptedException {
        String groupName = "Group123123";
        Thread client1Thread = new Thread(() -> {
            try {
                StoreClientUDP client1 = new StoreClientUDP();
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
    void testUDPPacketSeveralClientsCommandsExecutedSuccessfully() throws InterruptedException {
        String groupName = "Group123123555";
        String productName = "Product0";
        String product1Name = "Product1";
        int quantity = 50;
        double price = 123123123.55;
        Storage storage = Storage.getInstance();
        int productQInit = storage.getProductMap().get(product1Name).getQuantity();

        Thread client1Thread = new Thread(() -> {
            try {
                StoreClientUDP client1 = new StoreClientUDP();
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
                StoreClientUDP client1 = new StoreClientUDP();
                Packet response = client1.sendCommandAndReceive(Command.INCREASE_PRODUCT_QUANTITY,product1Name,quantity);
                System.out.println(response);
                Packet response2 = client1.sendCommandAndReceive(Command.ADD_PRODUCT_GROUP,groupName);
                System.out.println(response2);
                client1.close();
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
    void testUDPRepeatSendingOfPacket() throws InterruptedException {
        String product1Name = "Product1";
        Storage storage = Storage.getInstance();
        int quantity = 50;
        int productQInit = storage.getProductMap().get(product1Name).getQuantity();
        Thread client1Thread = new Thread(() -> {
            try {
                StoreClientUDP client1 = new StoreClientUDP();
                SenderImpl.setUnreachablePacket(true);
                new Thread(() -> {
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    SenderImpl.setUnreachablePacket(false);
                }).start();
                Packet response = client1.sendCommandAndReceive(Command.INCREASE_PRODUCT_QUANTITY,product1Name,quantity);
                System.out.println(response);
                client1.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        client1Thread.setDaemon(true);
        client1Thread.start();
        client1Thread.join();
        int productQCurrent = storage.getProductMap().get(product1Name).getQuantity();
        Assertions.assertEquals(productQInit+quantity,productQCurrent);
    }

}
