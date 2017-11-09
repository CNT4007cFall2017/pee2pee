import Logger.Logger;
import Message.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;

public class MessageHandler {
    private Socket connSock;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Set<Integer> validPeerIds;
    private Peer myPeer;
    private int remotePeerId;

    public MessageHandler(Socket _socket, Set<Integer> _validPeerIds, Peer _myPeer){
        connSock = _socket;
        validPeerIds = _validPeerIds;
        myPeer = _myPeer;

        try {
            output = new ObjectOutputStream(connSock.getOutputStream());
            input = new ObjectInputStream(connSock.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Message message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void recv() throws IOException {
        Message recMessage = null;
        try {
            recMessage = (Message)input.readObject();
            handle(recMessage);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void handle(Message message) {
        switch (message.getType()) {
            case Type.HANDSHAKE:
                int id = ((Handshake) message).getIdField();
                System.out.println("got handshake message from " + id);

                if (validateHandshake(id)) {
                    remotePeerId = id;
                    try {
                        Logger.logTCPConnection(remotePeerId, myPeer.getId());
                        send(new Handshake(myPeer.getId()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    private boolean validateHandshake(int peerId) {
        return validPeerIds.contains(peerId);
    }
}
