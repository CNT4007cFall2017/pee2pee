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
    private FileHandler fileHandler;
    private int peerId;
    private Peer self;

    public Process(int peerId) throws IOException {
        this.peerId = peerId;

        try {
            fileHandler = new FileHandler("config/PeerInfo.cfg");
            fileHandler.gatherRemotePeers();
            fileHandler.setPeerInputLimit();
            self = fileHandler.findSelf(peerId);
            serverSoc = new ServerSocket(self.getPort());
            ConnectionHandler connectionHandler = new ConnectionHandler(fileHandler.getPeersToConnectTo());



            int inputCount = 0;  // track the number of peers connected to this peer

            while(inputCount < self.getInputConnLimit()) {  // wait for connection from peers below
                System.out.println("Listening on port " + self.getPort());
                Socket clientSoc = serverSoc.accept();
                System.out.println("Getting connection from " + clientSoc.getRemoteSocketAddress());
                new Thread(new PeerWorker(clientSoc)).start();
                inputCount++;
            }

            connectionHandler.connectToPeers();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverSoc.close();
        }

    }
}
