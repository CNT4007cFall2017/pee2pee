import Logger.Logger;
import Message.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
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
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(new Sender(output, message)).start();
    }


    public void recv() throws IOException {
        synchronized (this) {
            Message recMessage;
            try {
                recMessage = (Message) input.readObject();
                handle(recMessage);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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

                    ByteBuffer bb = ByteBuffer.wrap(nextPiece);

                    myPeer.writeRequestedPieces(bb.getInt(), true);
                }
                break;

            case Type.REQUEST:
                Request incomingRequest = (Request)message;
                int pieceIndex = incomingRequest.getPieceIndex();
                byte[] piece = myPeer.Pieces.get(pieceIndex);
                byte[] payload = constructPiecePayload(incomingRequest.getPayload(), piece);
                send(new Piece(payload));
//                myPeer.writeRequestedPieces(pieceIndex, false );
                break;

            case Type.PIECE:
                Piece incomingPiece = (Piece)message;
                int index = incomingPiece.getIndex();
                byte[] pieceData = incomingPiece.getPieceData();
                myPeer.newPieces.put(index, pieceData); // TODO: sync this
                myPeer.myBitfield.set(index);
                myPeer.writeRequestedPieces(index, false);
                Logger.logDownloading(myPeer.peerId, remotePeerId, index, myPeer.myBitfield.cardinality());
                notifyPeersOfPiece(incomingPiece.getSubsetOfPayload(0,3));

                if (myPeer.downloadComplete()) {
                    myPeer.writeFile();
                    Logger.logCompleteDownload(myPeer.peerId);
                } else if (!myPeer.remotePeers.get(remotePeerId).choked && !myPeer.requestedPieces.contains(index)) {
                    byte[] nextPiece = myPeer.getNeededPieceIndex(remotePeerId);
                    ByteBuffer bb = ByteBuffer.wrap(nextPiece);
                    send(new Request(nextPiece));
                    myPeer.writeRequestedPieces(bb.getInt(), true);
                }

                break;

            case Type.HAVE:
                Have incomingHave = (Have)message;
                int remotePiece = incomingHave.getIndex();
                myPeer.remotePeers.get(remotePeerId).bitfield.set(remotePiece);
                amIInterested(remotePeerId);
                Logger.logHaveMsg(myPeer.peerId, remotePeerId, remotePiece);
                break;
            default:
//                teardown();
        }
    }
    //send have messages to every remote peer
    private void notifyPeersOfPiece(byte[] pieceIndex){
        Have have = new Have(pieceIndex);
        for(Integer key : myPeer.remotePeers.keySet()){
            myPeer.remotePeers.get(key).messageHandler.send(have);
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

    private byte[] constructPiecePayload(byte[] index, byte[] data) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(index);
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }

}
