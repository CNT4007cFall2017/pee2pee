import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    private byte[] bitfield;

    public Map<Integer, byte[]> remoteBitfields;

    public Peer(int id, String hostname, int port, boolean hasFile, int index) {
        this.id = id;
        this.hostname = hostname;
        this.port = port;
        this.hasFile = hasFile;
        this.index = index;
        inputConnLimit = 0;
        bitfield = new byte[16];
        remoteBitfields = new HashMap<>();

        if (hasFile) {
           Arrays.fill(bitfield, (byte)1);
        } else {
            Arrays.fill(bitfield, (byte)0);
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

    public byte[] getBitfield() {
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
            System.out.println(pair.getKey() + " : " + pair.getValue());
            it.remove();
        }
    }
}
