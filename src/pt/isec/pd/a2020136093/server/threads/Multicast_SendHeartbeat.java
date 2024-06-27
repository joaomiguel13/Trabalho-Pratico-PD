package pt.isec.pd.a2020136093.server.threads;

import pt.isec.pd.a2020136093.server.model.data.Heartbeat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

import static pt.isec.pd.a2020136093.server.model.data.CONSTANTS.*;

public class Multicast_SendHeartbeat extends Thread {
    private final Heartbeat serverData;
    private DatagramSocket datagramSocket;

    public Multicast_SendHeartbeat(Heartbeat serverData) {
        this.serverData = serverData;

        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);

                oos.writeObject(serverData);

                //Converte o serverData para um array de bytes
                byte[] msgBytes = baos.toByteArray();

                InetAddress ipMulticast = InetAddress.getByName(MULTICAST_IP);

                DatagramPacket dpSend = new DatagramPacket(
                        msgBytes,
                        msgBytes.length,
                        ipMulticast,
                        MULTICAST_PORT
                );

                datagramSocket.send(dpSend);

                Thread.sleep(TIMEOUT_HEARTBEAT);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
