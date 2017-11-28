package Message;

import java.util.BitSet;

public class Have extends Message{
    Have (BitSet pieceIndF) {
        super (4,Type.HAVE, pieceIndF);
    }
}
