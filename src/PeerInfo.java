import java.util.*;

public class PeerInfo {
    public int port;
    public String hostname;
    public final int peerId;
    public Set<Integer> validPeerIds;
    public boolean hasFile;
    public HashMap<Integer, BitSet> bitfields;

    public PeerInfo(int peerId) {
        this.peerId = peerId;
        bitfields = new HashMap<>();
    }

    public PeerInfo(int peerId, String hostname, int port) {
        this.port = port;
        this.hostname = hostname;
        this.peerId = peerId;
        hasFile = false;
        bitfields = new HashMap<>();
    }

    public PeerInfo(int peerId, String hostname, int port, Set<Integer> validPeerIds) {
        this.port = port;
        this.hostname = hostname;
        this.peerId = peerId;
        this.validPeerIds = validPeerIds;
        hasFile = false;
    }

    public void setAllBits() {
        BitSet temp = bitfields.get(peerId);
        temp.set(0, temp.size()-1);
    }

    public boolean hasPieces() {
        BitSet temp = bitfields.get(peerId);
        return temp.cardinality() > 0;
    }
}
