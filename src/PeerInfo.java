import java.util.Set;

public class PeerInfo {
    public int port;
    public String hostname;
    public final int peerId;
    public Set<Integer> validPeerIds;
    public boolean hasFile;

    public PeerInfo(int peerId) {
        this.peerId = peerId;
    }

    public PeerInfo(int peerId, String hostname, int port) {
        this.port = port;
        this.hostname = hostname;
        this.peerId = peerId;
        hasFile = false;
    }

    public PeerInfo(int peerId, String hostname, int port, Set<Integer> validPeerIds) {
        this.port = port;
        this.hostname = hostname;
        this.peerId = peerId;
        this.validPeerIds = validPeerIds;
        hasFile = false;
    }
}
