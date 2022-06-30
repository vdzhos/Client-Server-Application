package clients.interfaces;

import enums.Command;
import objects.Message;
import objects.Packet;
import org.json.JSONObject;
import packetProcessorUnits.implementations.Encoder;
import utils.Values;

public abstract class AbstractClient {

    private static byte bSrcCounter = 0;
    private byte bSrc;

    public AbstractClient() {
        bSrc = bSrcCounter;
        bSrcCounter = (byte) (bSrcCounter + 1);
    }

    protected byte[] createPacketFromCommand(Command command, Object...args) throws Exception {
        int bUserId = 1;
        JSONObject json = new JSONObject();
        String name;
        int quantity;
        double price;
        try {
            switch (command) {
                case GET_PRODUCT_QUANTITY:
                    name = (String) args[0];
                    json.put(Values.PRODUCT_NAME_COMMAND_PARAM, name);
                    break;
                case DECREASE_PRODUCT_QUANTITY:
                case INCREASE_PRODUCT_QUANTITY:
                    name = (String) args[0];
                    quantity = (int) args[1];
                    json.put(Values.PRODUCT_NAME_COMMAND_PARAM, name);
                    json.put(Values.QUANTITY_COMMAND_PARAM, quantity);
                    break;
                case ADD_PRODUCT_GROUP:
                    name = (String) args[0];
                    json.put(Values.GROUP_NAME_COMMAND_PARAM,name);
                    break;
                case ADD_PRODUCT_TO_GROUP:
                    name = (String) args[0];
                    json.put(Values.PRODUCT_NAME_COMMAND_PARAM,name);
                    name = (String) args[1];
                    json.put(Values.GROUP_NAME_COMMAND_PARAM,name);
                    price = (double) args[2];
                    json.put(Values.PRICE_COMMAND_PARAM,price);
                    quantity = (int) args[3];
                    json.put(Values.QUANTITY_COMMAND_PARAM,quantity);
                    break;
                case SET_PRODUCT_PRICE:
                    name = (String) args[0];
                    json.put(Values.PRODUCT_NAME_COMMAND_PARAM,name);
                    price = (double) args[1];
                    json.put(Values.PRICE_COMMAND_PARAM,price);
                    break;
            }
        } catch (ClassCastException | IndexOutOfBoundsException e) {
            throw new Exception("Incorrect arguments for command: " + command + "!");
        }

        byte[] messageBytes = json.toString().getBytes();
        Message message = new Message(command,bUserId,messageBytes);
        Packet packetToEncode = new Packet(bSrc,message);
        byte[] encodedPacket = Encoder.getInstance().encode(packetToEncode);

        if(encodedPacket.length>Packet.PACKET_MAX_SIZE){
            throw new Exception("Packet is too large!");
        }
        return encodedPacket;
    }

    public Packet sendCommandAndReceive(Command command, Object...args) throws Exception {
        byte[] encodedPacket = createPacketFromCommand(command, args);
        return sendByProtocol(encodedPacket);
    }

    abstract protected Packet sendByProtocol(byte[] encodedPacket) throws Exception;
}
