import java.util.Set;

public class PeerInfo {
    public final int port;
    public final String hostname;
    public final int peerId;
    public final Set<Integer> validPeerIds;
    private boolean hasFile;

    public PeerInfo(int port, String hostname, int peerId, Set<Integer> validPeerIds) {
        this.port = port;
        this.hostname = hostname;
        this.peerId = peerId;
        this.validPeerIds = validPeerIds;
        hasFile = false;
    }
}
