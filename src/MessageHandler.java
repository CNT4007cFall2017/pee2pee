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
    private PeerInfo myPeer;
    private int remotePeerId;

    public MessageHandler(ObjectInputStream input, ObjectOutputStream output, PeerInfo _myPeer, int remotePeerId){
        this.input = input;
        this.output = output;
        myPeer = _myPeer;
        this.remotePeerId = remotePeerId;
    }
    public void teardown() {
        try {
            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void send(Message message) {
        new Thread(new Sender(output, message)).start();
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

    private void handle(Message message) throws IOException {
        RemotePeerInfo temp;
        switch (message.getType()) {
            case Type.INTERESTED:
                temp = myPeer.remotePeers.get(remotePeerId);
                myPeer.writeInterestedPeers(remotePeerId, temp, true);

                try {
                    Logger.logInterestedMsg(myPeer.getId(), remotePeerId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Type.NOTINTERESTED:
                myPeer.interestedPeers.remove(remotePeerId);
                try {
                    Logger.logNotInterestedMsg(myPeer.getId(), remotePeerId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case Type.BITFIELD:
                Bitfield incomingBitField = (Bitfield)message;
                myPeer.remotePeers.get(remotePeerId).bitfield = incomingBitField.getBitSet();
                amIInterested(remotePeerId);
                break;

            case Type.CHOKE:
                if(!myPeer.remotePeers.get(remotePeerId).choked) {
                    myPeer.setRemoteChoke(remotePeerId, true);
                    Logger.logChoke(myPeer.peerId, remotePeerId);
                }
                break;

            case Type.UNCHOKE:
                if(myPeer.remotePeers.get(remotePeerId).choked) {
                    myPeer.setRemoteChoke(remotePeerId, false);
                    Logger.logUnchoke(myPeer.peerId, remotePeerId);

                    byte[] nextPiece = myPeer.getNeededPieceIndex(remotePeerId);
                    send(new Request(nextPiece));
                }
                break;

            case Type.REQUEST:
                Request incomingRequest = (Request)message;
                int pieceIndex = incomingRequest.getPieceIndex();

            default:
//                teardown();
        }
    }
    private void amIInterested(int remoteId) {
        BitSet temp = (BitSet) myPeer.myBitfield.clone();
        BitSet remoteBitfield = myPeer.remotePeers.get(remoteId).bitfield;
        temp.or(remoteBitfield);

        if(temp.cardinality() > myPeer.myBitfield.cardinality()){
            send(new Interested());
        }

    }

}
