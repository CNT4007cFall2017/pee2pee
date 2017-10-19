import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by chase on 10/19/2017.
 */
public class PeerWorker implements Runnable {

    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private boolean connected;

    public PeerWorker(Socket clientSoc) {
        connection = clientSoc;
        connected = true;

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

    @Override
    public void run() {
        while(connected) {
            try {
                String recv = (String)input.readObject();
            } catch (IOException e) {
                e.printStackTrace();
                shutdown();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                shutdown();
            }
        }
    }
}
