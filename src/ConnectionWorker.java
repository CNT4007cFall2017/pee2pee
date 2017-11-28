import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import Message.*;

public class ConnectionWorker implements Runnable {

    private Socket socket;
    private boolean receivedHandshake;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private int remoteId;
    private PeerInfo peerInfo;


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
                System.out.println("connected");
            }
            //TODO: close connection and log rejection if the handshake fails.
            //TODO: send bitfield if peer has something ot send, not if has file
            if(receivedHandshake){
                if(peerInfo.hasFile) {
                    //decide where we're storing the bitfields of the local peer and remote peers
                    //output.writeObject(new Bitfield());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
