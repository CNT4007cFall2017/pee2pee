package Message;

import java.util.BitSet;

public class Bitfield extends Message{
    public Bitfield (byte[] pieceIndF) {
        super(16, Type.BITFIELD, pieceIndF);
    }

    public BitSet getBitSet() {
        return BitSet.valueOf(payload);
    }
}
