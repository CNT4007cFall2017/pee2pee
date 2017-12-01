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
            msgHandler =  new MessageHandler(input, output, peerInfo, remoteId);
            peerInfo.remotePeers.get(remoteId).messageHandler = msgHandler;

            if(peerInfo.validPeerIds.contains(remoteId)){
                receivedHandshake = true;
                Logger.logTCPConnection(peerInfo.peerId, remoteId);
            }
            else{
                //TODO: close connection and log rejection if the handshake fails.
            }

            if(receivedHandshake){
                if(peerInfo.hasPieces()) {
                    output.writeObject(new Bitfield(peerInfo.myBitfield.toByteArray()));
                    output.flush();
                } else {
                    msgHandler.recv();
                }

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
