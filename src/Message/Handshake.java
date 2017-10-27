package Message;

public class Handshake {
    private String header;
    private byte[] zero;
    private byte[] peerID;

    public Handshake(byte[] peerID) {
        this.header = "P2PFILESHARINGPROJ";
        this.zero = new byte[8];
        this.peerID = peerID;
    }
}
