package Message;

import java.util.BitSet;

public class Piece extends Message{
    Piece (BitSet pieceIndF) {
        super (4,Type.PIECE, pieceIndF);
    }
}
