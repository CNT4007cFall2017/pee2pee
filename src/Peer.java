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

    public Peer(int id, String hostname, int port, boolean hasFile, int index) {
        this.id = id;
        this.hostname = hostname;
        this.port = port;
        this.hasFile = hasFile;
        this.index = index;
        inputConnLimit = 0;
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

    public void setInputConnLimit(int limit) {
        inputConnLimit = limit;
    }
}
