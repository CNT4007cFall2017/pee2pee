import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.BitSet;
import java.util.Comparator;

public class RemotePeerInfo {
    public int pID;
    public int bytesReceived;
    public boolean choked;
    public BitSet bitfield;
    public MessageHandler messageHandler;

    public RemotePeerInfo(int pid) {
        //this is redundant, but useful for the bytes recieved array in Unchoker
        pID = pid;
        bytesReceived = 0;
        choked = true;
        bitfield = new BitSet(PeerInfo.BITFIELD_SIZE);
    }

    public void resetBytes() {
        bytesReceived = 0;
    }
}

class SortByBytes implements Comparator<RemotePeerInfo> {

    @Override
    public int compare(RemotePeerInfo o1, RemotePeerInfo o2) {
        return o2.bytesReceived - o1.bytesReceived;
    }
}
