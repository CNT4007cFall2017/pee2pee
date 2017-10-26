package Message;

public class Piece extends Message{
    Piece (byte[] pieceIndF) {
        super (4,Type.PIECE, pieceIndF);
    }
}
