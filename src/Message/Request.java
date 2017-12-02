package Message;

import java.nio.ByteBuffer;

public class Request extends Message {
    public Request (byte[] pieceIndF) {
        super (4,Type.REQUEST, pieceIndF);
    }

    public int getPieceIndex() {
        ByteBuffer bb = ByteBuffer.wrap(this.payload);
        return bb.getInt();
    }
}
