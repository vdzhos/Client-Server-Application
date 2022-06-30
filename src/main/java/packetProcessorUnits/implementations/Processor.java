package packetProcessorUnits.implementations;

import enums.Command;
import model.Storage;
import objects.InetTarget;
import objects.Message;
import objects.Packet;
import org.json.JSONObject;
import utils.Values;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Processor {

    private static Processor instance;

    private ExecutorService executor;
    private Encoder encoder;
    private Storage storage;

    public static Processor getInstance() {
        if (instance == null)
            instance = new Processor();
        return instance;
    }

    private Processor() {
        executor = Executors.newFixedThreadPool(Values.NUMBER_OF_THREADS);
        storage = Storage.getInstance();
        encoder = Encoder.getInstance();
    }

    public void submitProcessTask(Packet packet, InetTarget target) {
        executor.submit(() -> {
            Message responseMessage = executeCommand(packet.getBMsg());
            packet.setBSrc(Values.B_SRC);
            packet.setBPktId(packet.getBPktId()+1);
            packet.setBMsg(responseMessage);
            encoder.submitEncodeTask(packet, target);
        });
    }

    public void shutdown() throws InterruptedException {
        executor.shutdown();
        while (!executor.awaitTermination(60L, TimeUnit.SECONDS)) {}
    }

    private Message executeCommand(Message message) {
//        System.out.println(Thread.currentThread());
        String jsonAsString = new String(message.getMessage());
        JSONObject json = new JSONObject(jsonAsString);

        String productName;
        String groupName;
        int quantity;
        double price;

        JSONObject jsonResponse = new JSONObject(new String(message.getMessage()));

        try{
            switch (message.getCType()){
                case GET_PRODUCT_QUANTITY:
                    productName = (String) json.get(Values.PRODUCT_NAME_COMMAND_PARAM);
                    int result;
                    result = storage.getProductQuantity(productName);
                    jsonResponse.put(Values.QUANTITY_COMMAND_PARAM,result);
                    break;
                case DECREASE_PRODUCT_QUANTITY:
                    productName = (String) json.get(Values.PRODUCT_NAME_COMMAND_PARAM);
                    quantity = (int) json.get(Values.QUANTITY_COMMAND_PARAM);
                    storage.decreaseProductQuantity(productName,quantity);
                    break;
                case INCREASE_PRODUCT_QUANTITY:
                    productName = (String) json.get(Values.PRODUCT_NAME_COMMAND_PARAM);
                    quantity = (int) json.get(Values.QUANTITY_COMMAND_PARAM);
                    storage.increaseProductQuantity(productName,quantity);
                    break;
                case ADD_PRODUCT_GROUP:
                    groupName = (String) json.get(Values.GROUP_NAME_COMMAND_PARAM);
                    storage.addProductGroup(groupName);
                    break;
                case ADD_PRODUCT_TO_GROUP:
                    productName = (String) json.get(Values.PRODUCT_NAME_COMMAND_PARAM);
                    groupName = (String) json.get(Values.GROUP_NAME_COMMAND_PARAM);
                    quantity = (int) json.get(Values.QUANTITY_COMMAND_PARAM);
                    price = ((BigDecimal) json.get(Values.PRICE_COMMAND_PARAM)).doubleValue();
                    storage.addProductToGroup(productName,groupName,quantity,price);
                    break;
                case SET_PRODUCT_PRICE:
                    productName = (String) json.get(Values.PRODUCT_NAME_COMMAND_PARAM);
                    price = ((BigDecimal) json.get(Values.PRICE_COMMAND_PARAM)).doubleValue();
                    storage.setProductPrice(productName,price);
                    break;
            }
            jsonResponse.put(Values.RESPONSE_STATUS_COMMAND_PARAM,Values.RESPONSE_STATUS_OK);
        }catch (Exception e){
            jsonResponse.put(Values.RESPONSE_STATUS_COMMAND_PARAM,Values.RESPONSE_STATUS_ERROR);
            jsonResponse.put(Values.ERROR_MESSAGE_COMMAND_PARAM,e.getMessage());
        }

        message.setMessage(jsonResponse.toString().getBytes());

        return message;
    }

    public void initializeExecutor() {
        executor = Executors.newFixedThreadPool(Values.NUMBER_OF_THREADS);
    }
}
