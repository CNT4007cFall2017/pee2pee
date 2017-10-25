import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionWorker implements Runnable {
    private String hostName;
    private int port;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket clientSoc;

    public ConnectionWorker(Peer p) {
        hostName = p.getHostname();
        port = p.getPort();

        try {
            clientSoc = new Socket(hostName, port);
            output = new ObjectOutputStream(clientSoc.getOutputStream());
            input = new ObjectInputStream(clientSoc.getInputStream());
            System.out.println("Connected to: " + hostName + " on " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }
}
