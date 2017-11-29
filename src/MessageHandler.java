import Logger.Logger;
import Message.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.BitSet;
import java.util.Set;

public class MessageHandler {
    private Socket connSock;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Set<Integer> validPeerIds;
    private PeerInfo myPeer;
    private int remotePeerId;

    public MessageHandler(Socket _socket, PeerInfo _myPeer){
        connSock = _socket;
        validPeerIds = _myPeer.remotePeers;
        myPeer = _myPeer;

        try {
            output = new ObjectOutputStream(connSock.getOutputStream());
            input = new ObjectInputStream(connSock.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void teardown() {
        try {
            connSock.close();
            input.close();
            output.close();
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
        Message recMessage;
        try {
            recMessage = (Message)input.readObject();
            handle(recMessage);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void handle(Message message) {
        Integer temp;
        switch (message.getType()) {
            case Type.HANDSHAKE:
                int id = ((Handshake) message).getIdField();

                if (validateHandshake(id)) {
                    remotePeerId = id;
                    try {
                        Logger.logTCPConnection(remotePeerId, myPeer.getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case Type.INTERESTED:
                temp = searchPeers(remotePeerId);
                myPeer.interestedPeers.add(temp);

                try {
                    Logger.logInterestedMsg(myPeer.getId(), remotePeerId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Type.NOTINTERESTED:
                temp = searchPeers(remotePeerId);
                myPeer.interestedPeers.remove(temp);
                try {
                    Logger.logNotInterestedMsg(myPeer.getId(), remotePeerId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            default:
                teardown();
        }
    }
    private void amIInterested(BitSet bitField) {
        BitSet temp = (BitSet) myPeer.getBitfield().clone();

        temp.or(bitField);
        if(temp.cardinality() > myPeer.getBitfield().cardinality()){
            send(new Interested());
        }

    }
    private Integer searchPeers(int peerID){
        for (Integer p : myPeer.remotePeers){
            if(p == peerID){
                return p;
            }
        }
        return null;

    }

    private boolean validateHandshake(int peerId) {
        return validPeerIds.contains(peerId);
    }
}
