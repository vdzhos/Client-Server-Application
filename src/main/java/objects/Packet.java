package objects;

public class Packet {

    private final byte bMagic = 0x13;
    private byte bSrc;
    private long bPktId;
    private int wLen;
    private short wCrc16;
    private Message bMsg;
    private short w2Crc16;

    public Packet(byte bSrc, long bPktId, int wLen,
                  short wCrc16, Message bMsg, short w2Crc16) {
        this.bSrc = bSrc;
        this.bPktId = bPktId;
        this.wLen = wLen;
        this.wCrc16 = wCrc16;
        this.bMsg = bMsg;
        this.w2Crc16 = w2Crc16;
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
}
