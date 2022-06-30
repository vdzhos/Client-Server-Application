package objects;

import enums.Protocol;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class InetTarget {

    private DatagramSocket udpSocket;
    private Socket tcpSocket;
    private Protocol protocol;
    private InetAddress address;
    private int port;

    public InetTarget(DatagramSocket udpSocket, InetAddress address, int port) {
        this.udpSocket = udpSocket;
        this.address = address;
        this.port = port;
        this.protocol = Protocol.UDP;
    }

    public InetTarget(Socket tcpSocket) {
        this.tcpSocket = tcpSocket;
        this.protocol = Protocol.TCP;
    }

    public DatagramSocket getUdpSocket() {
        return udpSocket;
    }

    public Socket getTcpSocket() {
        return tcpSocket;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public void setPort(int port) {
        this.port = port;
    }


}
