import java.util.Collection;

public class ConnectionHandler {
    private Collection<Peer> peersToConnectTo;
    private Peer myself;

    public ConnectionHandler(Collection<Peer> peers, Peer myself) {
        peersToConnectTo = peers;
        this.myself = myself;
    }

    public void connectToPeers() {
        for(Peer p : peersToConnectTo) {
            new Thread(new ConnectionWorker(p, myself)).start();
        }
    }
}
