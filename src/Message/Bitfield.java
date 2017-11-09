package Message;

public class Bitfield extends Message{
    public Bitfield (byte[] pieceIndF) {
        super(16, Type.BITFIELD, pieceIndF);
    }
}
