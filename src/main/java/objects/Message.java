package objects;

import enums.Command;

public class Message {

    private Command cType;
    private int bUserId;
    private byte[] message;

    public Message(Command cType, int bUserId, byte[] message) {
        this.cType = cType;
        this.bUserId = bUserId;
        this.message = message;
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
