package Message;

public class Bitfield extends Message{
    /* Not sure about length!!! */
    Bitfield (byte[] pieceIndF) {
        super(16, Type.BITFIELD, pieceIndF);
    }
}
