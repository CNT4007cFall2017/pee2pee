import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionWorker implements Runnable {
    private Peer connectedPeer;
    private Peer myPeer;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket clientSoc;

    public ConnectionWorker(Peer p, Peer myself) {
        connectedPeer = p;
        myPeer = myself;
        try {
            clientSoc = new Socket(p.getHostname(), p.getPort());
            output = new ObjectOutputStream(clientSoc.getOutputStream());
            input = new ObjectInputStream(clientSoc.getInputStream());
            System.out.println("Connected to: " + p.getHostname() + " on " + p.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // this will send handshake message
        sendHandShake(myPeer.getId());
        boolean validated = listenForHandshake();

        if (validated) {
            System.out.println("Validated: " + connectedPeer.getId());
        } else {
            System.out.println("NOT AUTHORIZED");
        }
    }

    private void sendHandShake(int pid) {
        try {
            output.writeObject(pid);  //send the handshake
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean listenForHandshake() {
        boolean valid = false;

        try {
            int id = (Integer)input.readObject();
            valid = validate(id);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return valid;
    }

    private boolean validate(int id) {
        return id == connectedPeer.getId();
    }
}
