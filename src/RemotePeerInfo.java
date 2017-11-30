import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.BitSet;

public class RemotePeerInfo {

    public int bytesReceived;
    public boolean choked;
    public BitSet bitfield;
    public ObjectInputStream input;
    public ObjectOutputStream output;

    public RemotePeerInfo() {
        bytesReceived = 0;
        choked = true;
        bitfield = new BitSet(16);
    }

    public void resetBytes() {
        bytesReceived = 0;
    }
}
