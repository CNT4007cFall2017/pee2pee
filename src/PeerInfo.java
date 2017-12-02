import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import Message.Message.*;

public class PeerInfo {
    public int port;
    public String hostname;
    public int peerId;
    public ArrayList<byte[]> Pieces;
    //we may no longer need this validPeerIds
    public Set<Integer> validPeerIds;
    public boolean hasFile;
    public BitSet myBitfield;
    public HashMap<String, Integer> CommonConfig;
    public HashMap<Integer, RemotePeerInfo> interestedPeers;
    public HashMap<Integer, RemotePeerInfo> remotePeers;
    public HashMap<Integer, byte[]> newPieces;
    public HashSet<RemotePeerInfo> preferredNeighbors;
    public HashSet<RemotePeerInfo> unpreferredNeighbors;
    //Constants for config file.
    public static final String NUM_PREFERRED= "NumberOfPreferredNeighbors";
    public static final String UNCHOKING_INTERVAL= "UnchokingInterval";
    public static final String OP_UNCHOKING_INTERVAL= "OptimisticUnchokingInterval";
    public static final String FILE_NAME = "FileName";
    public static final String FILE_SIZE= "FileSize";
    public static final String PIECE_SIZE= "PieceSize";
    public static final int PIECE_INDEX_SIZE = 4;
    public static int BITFIELD_SIZE;

    public PeerInfo(int peerId) {
        this.peerId = peerId;
        remotePeers = new HashMap<>();
        myBitfield = new BitSet(BITFIELD_SIZE);
        interestedPeers = new HashMap<>();
        preferredNeighbors = new HashSet<>();
        CommonConfig = new HashMap<>();
        unpreferredNeighbors = new HashSet<>();
        Pieces = new ArrayList<>();
        newPieces = new HashMap<>();
    }

    public PeerInfo(int peerId, String hostname, int port) {
        this.port = port;
        this.hostname = hostname;
        this.peerId = peerId;
        hasFile = false;
        remotePeers = new HashMap<>();
        myBitfield = new BitSet(BITFIELD_SIZE);
        interestedPeers = new HashMap<>();
        preferredNeighbors = new HashSet<>();
        CommonConfig = new HashMap<>();
        unpreferredNeighbors = new HashSet<>();
        Pieces = new ArrayList<>();
        newPieces = new HashMap<>();
    }

    public PeerInfo(PeerInfo peerInfo) { //Copy Constructor
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
        Pieces = new ArrayList<>();
    }

    public void writeInterestedPeers(int pid, RemotePeerInfo rpi, Boolean add){
        synchronized (this){
            if(add){
                interestedPeers.put(pid, rpi);
            }
            else{
                interestedPeers.remove(pid);
            }

        }
    }

    public void writeRemotePeers(int pid, RemotePeerInfo rpi, Boolean add) {
        synchronized (this) {
            if (add) {
                remotePeers.put(pid, rpi);
            } else {
                remotePeers.remove(pid);
            }
        }
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

    public byte[] getNeededPieceIndex(int remoteId) {
        BitSet remoteBitfield;
        remoteBitfield = (BitSet) remotePeers.get(remoteId).bitfield.clone();
        remoteBitfield.xor(myBitfield);

        ByteBuffer bb = ByteBuffer.allocate(PIECE_INDEX_SIZE);
        bb.putInt(remoteBitfield.nextSetBit(0));

        return bb.array();
    }

    public boolean downloadComplete() {
        return myBitfield.cardinality() == BITFIELD_SIZE;
    }

    public void writeFile() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        for (int i = 0; i < newPieces.size(); i++) {
            try {
                outputStream.write(newPieces.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileHandler.mergeFile(outputStream.toByteArray());
    }
}
