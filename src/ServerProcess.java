/*

 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerProcess implements Runnable {

    private ServerSocket serverSocket;  // used for listening and accepting connections from remote peers
    private PeerInfo peerInfo;  // holds global information to be shared amoung threads

    public ServerProcess(PeerInfo peerInfo) {
        this.peerInfo = peerInfo;
        try {
            serverSocket = new ServerSocket(peerInfo.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // TODO: make all files downloaded global
        while(true) {
            try {
                Socket newConnection = serverSocket.accept();
                new Thread(new ConnectionWorker(newConnection, peerInfo));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
