package Message;

import java.util.BitSet;

public class Request extends Message {
    Request (BitSet pieceIndF) {
        super (4,Type.REQUEST, pieceIndF);
    }
}
