import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.BitSet;

import Logger.Logger;
import Message.*;

public class ConnectionWorker implements Runnable {

    private Socket socket;
    private boolean receivedHandshake;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private int remoteId;
    private PeerInfo peerInfo;
    public MessageHandler msgHandler;


    public ConnectionWorker(Socket socket, PeerInfo peerInfo) {
        receivedHandshake = false;
        this.socket = socket;
        this.peerInfo = peerInfo;
        try {
            output = new ObjectOutputStream(this.socket.getOutputStream());
            input = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //handshake
        try {
            output.writeObject(new Handshake(peerInfo.peerId));  // send the handshake to remote peer
            output.flush();
            Handshake incomingHandshake = (Handshake)input.readObject();  //wait for remote to
            remoteId = incomingHandshake.getIdField();
            if(peerInfo.validPeerIds.contains(remoteId)){
                receivedHandshake = true;
                Logger.logTCPConnection(peerInfo.peerId, remoteId);
            }
            //TODO: close connection and log rejection if the handshake fails.
            if(receivedHandshake){
                if(peerInfo.hasPieces()) {
                    output.writeObject(new Bitfield(peerInfo.getMyBitfield().toByteArray()));
                    output.flush();
                } else {
                    Bitfield incomingBitField = (Bitfield)input.readObject();  //wait for remote to respond
                    peerInfo.setBitField(remoteId, incomingBitField.getBitSet());
                }
                //add the remote peer to the list of remoteID's
                peerInfo.remotePeers.add(remoteId);
                //create a msgHandler, is there an issue keeping the
                //remotePeer/Interested peers updated across threads?
               msgHandler =  new MessageHandler(socket, peerInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        while(true){
            try{
                msgHandler.recv();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
