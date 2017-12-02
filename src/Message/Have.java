package Message;

import java.nio.ByteBuffer;

public class Have extends Message{
    public Have (byte[] pieceIndF) {
        super (4,Type.HAVE, pieceIndF);
    }

    public int getIndex(){
        byte[]index = payload;
        ByteBuffer bb = ByteBuffer.wrap(index);
        return bb.getInt();
    }
}
