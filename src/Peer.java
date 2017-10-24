/**
 * Created by chase on 10/19/2017.
 *
 * Represents a connected peer.
 */
public class Peer {
    private int id;
    private String hostname;
    private int port;
    private boolean hasFile;

    public Peer(int id, String hostname, int port, boolean hasFile) {
        this.id = id;
        this.hostname = hostname;
        this.port = port;
        this.hasFile = hasFile;
    }
}
