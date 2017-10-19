import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Created by chase on 10/19/2017.
 *
 * This class is responsible for creating a listening server socket, and creating connection sockets for each
 * peer that connects to this peer.  For each connecting peer, a new thread is spawned.  This thread waits for
 * a message and then handles the message.
 */
public class Process {

    private ServerSocket serverSoc;
    private int port;

    public Process() throws IOException {
        port = 9000;

        try {
            serverSoc = new ServerSocket(port);
            System.out.println("Listening on port " + port);
            while(true) {
                Socket clientSoc = serverSoc.accept();
                System.out.println("Getting connection from " + clientSoc.getRemoteSocketAddress());
                new Thread(new PeerWorker(clientSoc)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverSoc.close();
        }

    }
}
