import objects.Packet;
import utils.PacketUtils;

import java.nio.ByteBuffer;

public class PacketEncoder {

    public static byte[] encode(Packet packet){
        byte[] messageBytes = packet.getBMsg().toByteArray();

        byte[] header = ByteBuffer.allocate(Packet.BytesSize.allExceptMessage)
                .put(packet.getBMagic())
                .put(packet.getBSrc())
                .putLong(packet.getBPktId())
                .putInt(messageBytes.length)
                .array();

        return ByteBuffer.allocate(header.length + Packet.BytesSize.wCrc16 +
                messageBytes.length + Packet.BytesSize.w2Crc16)
                .put(header)
                .putShort(PacketUtils.crc16(header))
                .put(messageBytes)
                .putShort(PacketUtils.crc16(messageBytes))
                .array();
    }

}
