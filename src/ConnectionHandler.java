import java.util.Collection;

public class ConnectionHandler {
    private Collection<Peer> peersToConnectTo;

    public ConnectionHandler(Collection<Peer> peers) {
        peersToConnectTo = peers;
    }

    public void connectToPeers() {
        for(Peer p : peersToConnectTo) {
            new Thread(new ConnectionWorker(p)).start();
        }
    }
}
