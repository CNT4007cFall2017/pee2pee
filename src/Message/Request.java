package Message;

public class Request extends Message {
    Request (byte[] pieceIndF) {
        super (4,Type.REQUEST, pieceIndF);
    }
}
