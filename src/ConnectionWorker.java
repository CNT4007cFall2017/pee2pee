import Helper.Helper;
import Logger.Logger;
import Message.Type;

import java.io.IOException;
import java.net.Socket;
import java.util.Set;

import Message.*;

public class ConnectionWorker implements Runnable {
    private Peer myPeer;
    private MessageHandler messageHandler;

    public ConnectionWorker(Set<Integer> validPeerIds, Peer myPeer, Socket connSock) {
        this.myPeer = myPeer;
        messageHandler = new MessageHandler(connSock, validPeerIds, myPeer);
    }

    @Override
    public void run() {
        doHandshake();
        doBitfield();
        //messageHandler.teardown();
    }

    private void doHandshake() {
        messageHandler.send(new Handshake(myPeer.getId()));
        try {
            messageHandler.recv();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doBitfield() {
        if (myPeer.hasFile()) {
            messageHandler.send(new Bitfield(myPeer.getBitfield().toByteArray()));
        } else {
            try {
                messageHandler.recv();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
