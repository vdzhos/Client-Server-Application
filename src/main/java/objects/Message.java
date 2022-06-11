package objects;

import enums.Command;

import java.nio.ByteBuffer;

public class Message {

    private Command cType;
    private int bUserId;
    private byte[] message;

    public static class BytesSize{
        public static int cType = 4;
        public static int bUserId = 4;
        public static int allExceptMessage = 8;
    }

    public Message(Command cType, int bUserId, byte[] message) {
        this.cType = cType;
        this.bUserId = bUserId;
        this.message = message;
    }

    public byte[] toByteArray(){
        return ByteBuffer.allocate(BytesSize.allExceptMessage + message.length)
                .putInt(cType.code)
                .putInt(bUserId)
                .put(message)
                .array();
    }

    public Command getCType() {
        return cType;
    }

    public void setCType(Command cType) {
        this.cType = cType;
    }

    public int getBUserId() {
        return bUserId;
    }

    public void setBUserId(int bUserId) {
        this.bUserId = bUserId;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }
}
