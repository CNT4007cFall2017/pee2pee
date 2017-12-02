package Message;

public class Have extends Message{
    public Have (byte[] pieceIndF) {
        super (4,Type.HAVE, pieceIndF);
    }
}
