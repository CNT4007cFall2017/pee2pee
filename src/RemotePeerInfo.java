import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.BitSet;
import java.util.Comparator;

public class RemotePeerInfo {
    public int pID;
    public int bytesReceived;
    public boolean choked;
    public BitSet bitfield;
    public ObjectInputStream input;
    public ObjectOutputStream output;

    public RemotePeerInfo(int pid) {
        //this is redundant, but useful for the bytes recieved array in Unchoker
        pID = pid;
        bytesReceived = 0;
        choked = true;
        bitfield = new BitSet(16);
    }

    public void resetBytes() {
        bytesReceived = 0;
    }
}

public class SortByBytes implements Comparator<RemotePeerInfo> {

    @Override
    public int compare(RemotePeerInfo o1, RemotePeerInfo o2) {
        return o2.bytesReceived - o1.bytesReceived;
    }
}
