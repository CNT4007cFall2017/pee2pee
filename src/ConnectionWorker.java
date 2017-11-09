import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;

public class ConnectionWorker implements Runnable {
    private Peer connectedPeer;
    private Peer myPeer;
    private MessageHandler messageHandler;
    private Set<Integer> validPeerIds;
    private boolean doHandshake;

    public ConnectionWorker(Set<Integer> validPeerIds, Peer myPeer, Socket connSock, boolean doHandshake) {
        this.connectedPeer = connectedPeer;
        this.myPeer = myPeer;
        this.doHandshake = doHandshake;
        this.validPeerIds = validPeerIds;
        messageHandler = new MessageHandler(connSock);
    }

    @Override
    public void run() {
        if (doHandshake) {
            // tell message handler to send handshake message
            System.out.println("sending handshake");
        } else {
            // wait for handshake message and validate it
            System.out.println("listening for handshake");
        }


    }


}
