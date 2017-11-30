import java.util.*;

public class PeerInfo {
    public int port;
    public String hostname;
    public final int peerId;
    //we may no longer need this validPeerIds
    public Set<Integer> validPeerIds;
    public boolean hasFile;
    public BitSet myBitfield;

    public HashMap<Integer, RemotePeerInfo> interestedPeers;
//    public Set<Integer> remotePeers;
    public HashMap<Integer, RemotePeerInfo> remotePeers;


    public static final int BITFIELD_SIZE = 16;

    public PeerInfo(int peerId) {
        this.peerId = peerId;
        remotePeers = new HashMap<>();
        myBitfield = new BitSet(16);
        interestedPeers = new HashMap<>();
    }

    public PeerInfo(int peerId, String hostname, int port) {
        this.port = port;
        this.hostname = hostname;
        this.peerId = peerId;
        hasFile = false;
        remotePeers = new HashMap<>();
        myBitfield = new BitSet(16);
        interestedPeers = new HashMap<>();
    }

    public PeerInfo(int peerId, String hostname, int port, Set<Integer> validPeerIds) {
        this.port = port;
        this.hostname = hostname;
        this.peerId = peerId;
        this.validPeerIds = validPeerIds;
        hasFile = false;
    }

    public void setAllBits() {
        myBitfield.set(0, BITFIELD_SIZE);
    }
    public byte[] toByteArray(BitSet bits) {
        byte[] bytes = new byte[bits.length()/8];
        for (int i=0; i<bits.length(); i++) {
            if (bits.get(i)) {
                bytes[bytes.length-i/8-1] |= 1<<(i%8);
            }
        }
        return bytes;
    }

    public void setRemoteBitField(int key, Integer pieceIndex) {
        remotePeers.get(key).bitfield.set(pieceIndex);
    }
    public void setMyBitField(Integer pieceIndex) {
        myBitfield.set(pieceIndex);
    }

    public boolean hasPieces() {
        return myBitfield.cardinality() > 0;
    }
    public int getId(){
        return peerId;
    }
}
