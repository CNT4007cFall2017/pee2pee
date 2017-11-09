import Logger.Logger;
import Message.Type;

import java.io.IOException;
import java.net.Socket;
import java.util.Set;

import Message.*;

public class ConnectionWorker implements Runnable {
    private Peer myPeer;
    private MessageHandler messageHandler;
    private boolean doHandshake;

    public ConnectionWorker(Set<Integer> validPeerIds, Peer myPeer, Socket connSock, boolean doHandshake) {
        this.myPeer = myPeer;
        this.doHandshake = doHandshake;
        messageHandler = new MessageHandler(connSock, validPeerIds, myPeer);
    }

    @Override
    public void run() {
        doHandshake();
    }

    private void doHandshake() {
        if (doHandshake) {
            // tell message handler to send handshake message
            messageHandler.send(new Handshake(myPeer.getId()));
        }
        try {
            messageHandler.recv();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
