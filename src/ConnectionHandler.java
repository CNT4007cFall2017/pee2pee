import com.sun.security.ntlm.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ConnectionHandler {
    private Collection<Peer> peersToConnectTo;
    private Peer myPeer;
    private Set<Integer> validPeerIds;
    private ServerSocket serverSocket;

    public ConnectionHandler(Collection<Peer> peersToConnectTo, Peer myPeer, Set<Integer> validPeerIds) {
        this.peersToConnectTo = peersToConnectTo;
        this.validPeerIds = validPeerIds;
        this.myPeer = myPeer;
        try {
            this.serverSocket = new ServerSocket(myPeer.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
        establishPeerNetwork();
    }

    private void connectToPeers() {
        for(Peer p : peersToConnectTo) {
            try {
                Socket connectionSock = new Socket(p.getHostname(), p.getPort());
                new Thread(new ConnectionWorker(this.validPeerIds, p, connectionSock,true)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void establishPeerNetwork() {
        int connectionCount = 0;

        while (connectionCount < myPeer.getInputConnLimit()) {
            try {
                Socket clientSock = serverSocket.accept();
                new Thread(new ConnectionWorker(this.validPeerIds, myPeer, clientSock, false)).start();  // start a server connection worker
                connectionCount++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        connectToPeers();
    }
}
