import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chase on 10/19/2017.
 */
public class PeerWorker implements Runnable {

    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private boolean connected;
    private Set<Integer> allowedPeerIds;
    private Peer myPeer;

    public PeerWorker(Socket clientSoc, Collection<Peer> allowedPeerConnections, Peer p) {
        connection = clientSoc;
        connected = true;
        myPeer = p;
        allowedPeerIds = new HashSet<>();

        for (Peer currP : allowedPeerConnections) {
            allowedPeerIds.add(currP.getId());
        }

        try {
            input = new ObjectInputStream(connection.getInputStream());
            output = new ObjectOutputStream(connection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shutdown() {
        System.out.println("shutting down...");
        connected = false;
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(String msg) {
        try {
            output.writeObject(msg);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendHandshake(int pid) {
        try {
            output.writeObject(pid);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected) {

            //verify handshake
            boolean valid = listenForHandshake();

            if (valid) {
                //respond with our own handshake
                sendHandshake(myPeer.getId());
            } else {
                System.out.println("PW NOT VALID");
            }

        }
    }

    private boolean listenForHandshake() {
        boolean valid = false;
        try {
            int pid = (Integer)input.readObject(); //2
            valid =  validate(pid);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return valid;
    }

    private boolean validate(int pid) {
        return allowedPeerIds.contains(pid);
    }
}
