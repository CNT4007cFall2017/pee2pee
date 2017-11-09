import java.util.*;

/**
 * Created by chase on 10/19/2017.
 *
 * Represents a connected peer.
 */
public class Peer {
    private final int id;
    private final String hostname;
    private final int port;
    private final boolean hasFile;
    private final int index;
    private int inputConnLimit;
    private BitSet bitfield;

    public Map<Integer, BitSet> remoteBitfields;

    public Peer(int id, String hostname, int port, boolean hasFile, int index) {
        this.id = id;
        this.hostname = hostname;
        this.port = port;
        this.hasFile = hasFile;
        this.index = index;
        inputConnLimit = 0;
        bitfield = new BitSet(16);
        remoteBitfields = new HashMap<>();

        if (hasFile) {
            bitfield.set(0, bitfield.size()-1);
        }
    }

    public int getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public int getInputConnLimit() {
        return inputConnLimit;
    }

    public BitSet getBitfield() {
        return bitfield;
    }

    public void setInputConnLimit(int limit) {
        inputConnLimit = limit;
    }

    public boolean hasFile() {
        return hasFile;
    }

    public void printBitfields() {
        Iterator it = remoteBitfields.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " : " + Helper.Helper.bitSetToString((BitSet)pair.getValue()));
            it.remove();
        }
    }
}
