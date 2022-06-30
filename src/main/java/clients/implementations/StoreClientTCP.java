package clients.implementations;

import clients.interfaces.AbstractClient;
import objects.Packet;
import packetProcessorUnits.implementations.Decoder;
import utils.Values;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class StoreClientTCP extends AbstractClient {

    private Socket clientSocket;
    private OutputStream out;
    private InputStream in;

    private byte[] buf;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = clientSocket.getOutputStream();
        in = clientSocket.getInputStream();
        buf = new byte[Packet.PACKET_MAX_SIZE];
    }

    public void close() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    @Override
    protected Packet sendByProtocol(byte[] encodedPacket) throws Exception {
        boolean retry = true;

        while (retry) {
            try {
                out.write(encodedPacket);
                retry = false;
            } catch (IOException e) {
                reconnectToServer(clientSocket);
            }
        }

        int readBytes = -1;

        try {
            readBytes = in.read(buf);
        } catch (IOException e) {
            reconnectToServer(clientSocket);
        }

        if (readBytes == -1)
            throw new Exception("Response was expected but got end of stream.");


        return Decoder.getInstance().decode(buf);
    }

    private void reconnectToServer(Socket clientSocket) throws Exception {
        int counterOfSendTries = 0;
        while (clientSocket.isClosed() && counterOfSendTries < Values.NUMBER_OF_SEND_TRIES) {
            clientSocket = new Socket(clientSocket.getInetAddress(), clientSocket.getPort());
            in = clientSocket.getInputStream();
            out = clientSocket.getOutputStream();
            Thread.sleep(Values.SO_TIMEOUT);
            counterOfSendTries++;
        }
        if (clientSocket.isClosed()) {
            throw new Exception("Server is dead inside (ghoul).");
        }
    }
}
