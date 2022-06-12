package objects;

import enums.Command;

import java.util.Arrays;
import java.util.Objects;

public class Message {

    private Command cType;
    private int bUserId;
    private byte[] message;

    public static class BytesSize{
        public static final int C_TYPE = 4;
        public static final int B_USER_ID = 4;
        public static final int ALL_EXCEPT_MESSAGE = 8;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return bUserId == message1.bUserId && cType == message1.cType && Arrays.equals(message, message1.message);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(cType, bUserId);
        result = 31 * result + Arrays.hashCode(message);
        return result;
    }

    @Override
    public String toString() {
        return "Message: \n Command = " + cType + "\n bUserId = " + bUserId + "\n message text = " + new String(message);
    }
}
