package objects;

import java.util.Objects;

public class Packet {

    public static final byte B_MAGIC = 0x13;

    private byte bSrc;
    private long bPktId;
    private int wLen;
    private short wCrc16;
    private Message bMsg;
    private short w2Crc16;

    public static class BytesSize{

        public static final int B_MAGIC = 1;
        public static final int B_SRC = 1;
        public static final int B_PKT_ID = 8;
        public static final int W_LEN = 4;

        public static final int HEADER_SIZE = B_MAGIC + B_SRC + B_PKT_ID + W_LEN;

        public static final int W_CRC_16 = 2;
        public static final int W_2_CRC_16 = 2;
    }

    public Packet(byte bSrc, long bPktId, Message bMsg) {
        this.bSrc = bSrc;
        this.bPktId = bPktId;
        this.bMsg = bMsg;
    }

    public byte getBSrc() {
        return bSrc;
    }

    public void setBSrc(byte bSrc) {
        this.bSrc = bSrc;
    }

    public long getBPktId() {
        return bPktId;
    }

    public void setBPktId(long bPktId) {
        this.bPktId = bPktId;
    }

    public int getWLen() {
        return wLen;
    }

    public void setWLen(int wLen) {
        this.wLen = wLen;
    }

    public short getWCrc16() {
        return wCrc16;
    }

    public void setWCrc16(short wCrc16) {
        this.wCrc16 = wCrc16;
    }

    public Message getBMsg() {
        return bMsg;
    }

    public void setBMsg(Message bMsg) {
        this.bMsg = bMsg;
    }

    public short getW2Crc16() {
        return w2Crc16;
    }

    public void setW2Crc16(short w2Crc16) {
        this.w2Crc16 = w2Crc16;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Packet packet = (Packet) o;
        return bSrc == packet.bSrc && bPktId == packet.bPktId && wLen == packet.wLen && wCrc16 == packet.wCrc16 && w2Crc16 == packet.w2Crc16 && Objects.equals(bMsg, packet.bMsg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bSrc, bPktId, wLen, wCrc16, bMsg, w2Crc16);
    }

    @Override
    public String toString() {
        return "Packet: \nbSrc = " + bSrc + "\nbPktId = " + bPktId + "\n"+ bMsg;
    }
}
