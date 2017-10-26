package Message;

public class Have extends Message{
    Have (byte[] pieceIndF) {
        super (4,Type.HAVE, pieceIndF);
    }
}
