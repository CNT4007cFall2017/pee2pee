import java.rmi.Remote;
import java.util.*;

public class PeerInfo {
    public int port;
    public String hostname;
    public int peerId;
    //we may no longer need this validPeerIds
    public Set<Integer> validPeerIds;
    public boolean hasFile;
    public BitSet myBitfield;
    public HashMap<String, Integer> CommonConfig;
    public HashMap<Integer, RemotePeerInfo> interestedPeers;
    public HashMap<Integer, RemotePeerInfo> remotePeers;
    public HashSet<RemotePeerInfo> preferredNeighbors;
    public HashSet<RemotePeerInfo> unpreferredNeighbors;
    //Constants for config file.
    public static final String NUM_PREFERRED= "NumberOfPreferredNeighbors";
    public static final String UNCHOKING_INTERVAL= "UnchokingInterval";
    public static final String OP_UNCHOKING_INTERVAL= "OptimisticUnchokingInterval";
    public static final String FILE_NAME = "FileName";
    public static final String FILE_SIZE= "FileSize";
    public static final String PIECE_SIZE= "PieceSize";



    public static final int BITFIELD_SIZE = 16;

    public PeerInfo(int peerId) {
        this.peerId = peerId;
        remotePeers = new HashMap<>();
        myBitfield = new BitSet(16);
        interestedPeers = new HashMap<>();
        preferredNeighbors = new HashSet<>();
        CommonConfig = new HashMap<>();
        unpreferredNeighbors = new HashSet<>();
    }

    public PeerInfo(int peerId, String hostname, int port) {
        this.port = port;
        this.hostname = hostname;
        this.peerId = peerId;
        hasFile = false;
        remotePeers = new HashMap<>();
        myBitfield = new BitSet(16);
        interestedPeers = new HashMap<>();
        preferredNeighbors = new HashSet<>();
        CommonConfig = new HashMap<>();
        unpreferredNeighbors = new HashSet<>();
    }

    public PeerInfo(PeerInfo peerInfo) {
        synchronized (this) {
            this.remotePeers = peerInfo.remotePeers;
            this.myBitfield = peerInfo.myBitfield;
            this.interestedPeers = peerInfo.interestedPeers;
            this.preferredNeighbors = peerInfo.preferredNeighbors;
            this.unpreferredNeighbors = peerInfo.unpreferredNeighbors;
            this.CommonConfig = peerInfo.CommonConfig;
        }
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

    public void setRemoteChoke(int remoteId, boolean isChoked) {
        this.remotePeers.get(remoteId).choked = isChoked;
    }

    public void resetBytesReceived() {
        for (int remoteId : remotePeers.keySet()) {
            RemotePeerInfo currRemotePeer = remotePeers.get(remoteId);
            currRemotePeer.bytesReceived = 0;
        }
    }

    public void reset(PeerInfo peerInfo) {
        synchronized (this) {
            this.remotePeers = peerInfo.remotePeers;
            this.preferredNeighbors = peerInfo.preferredNeighbors;
            this.unpreferredNeighbors = peerInfo.unpreferredNeighbors;
        }
    }
}
