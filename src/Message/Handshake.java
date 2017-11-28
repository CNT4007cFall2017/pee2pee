package Message;

import java.util.BitSet;

public class Handshake extends Message {
    private final String header = "P2PFILESHARINGPROJ0000000000";

    public Handshake(int peerID) {
        super(30, Type.HANDSHAKE, BitSet.valueOf(("P2PFILESHARINGPROJ0000000000" + peerID).getBytes()));
    }

    public int getIdField() {
        return this.getPayload()[this.getPayloadLength()-1] - 48; // ascii value offset
    }
}
