import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by chase on 10/19/2017.
 *
 * This class is responsible for creating a listening server socket, and creating connection sockets for each
 * peer that connects to this peer.  For each connecting peer, a new thread is spawned.  This thread waits for
 * a message and then handles the message.
 */
public class Process {

    private ServerSocket serverSoc;
    private FileHandler fileHandler;
    private int peerId;
    private Peer myPeer;

    public Process(int peerId) throws IOException {
        this.peerId = peerId;

        fileHandler = new FileHandler("config/PeerInfo.cfg");
        fileHandler.gatherRemotePeers();
        fileHandler.setPeerInputLimit();
        myPeer = fileHandler.findSelf(peerId);
        fileHandler.initPeerLists(myPeer);
        ConnectionHandler connectionHandler = new ConnectionHandler(fileHandler.getPeersToConnectTo(), myPeer, fileHandler.getAllowedPeerConnections());
    }
}
