package Message;

import java.util.BitSet;

public class Bitfield extends Message{
    public Bitfield (BitSet payload) {
        super(16, Type.BITFIELD, payload);
    }

    public BitSet getBitSet() {
        return payload;
    }

}
