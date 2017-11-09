import Message.Type;

import java.io.IOException;
import java.net.Socket;
import java.util.Set;

import Message.*;

public class ConnectionWorker implements Runnable {
    private Peer myPeer;
    private MessageHandler messageHandler;
    private Set<Integer> validPeerIds;
    private boolean doHandshake;

    public ConnectionWorker(Set<Integer> validPeerIds, Peer myPeer, Socket connSock, boolean doHandshake) {
        this.myPeer = myPeer;
        this.doHandshake = doHandshake;
        this.validPeerIds = validPeerIds;
        messageHandler = new MessageHandler(connSock, validPeerIds);
    }

    @Override
    public void run() {
        if (doHandshake) {
            // tell message handler to send handshake message
            System.out.println("sending handshake");
            messageHandler.send(new Handshake(myPeer.getId()));
            try {
                boolean res = (boolean)messageHandler.recv();

                if (res) {
                    // log successful handshake
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // wait for handshake message and validate it
            System.out.println("listening for handshake");
            try {
                boolean res = (boolean)messageHandler.recv();

                if (res) {
                    messageHandler.send(new Handshake(myPeer.getId()));
                    // log successful handhshake
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


}
