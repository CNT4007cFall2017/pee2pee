package Message;

public class Handshake extends Message {
    private final String header = "P2PFILESHARINGPROJ0000000000";

    public Handshake(int peerID) {
        super(30, Type.HANDSHAKE, ("P2PFILESHARINGPROJ0000000000" + peerID).getBytes());
    }
}
