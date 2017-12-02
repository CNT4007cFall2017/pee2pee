package Message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Piece extends Message{
    public Piece (byte[] payload) {
        super (payload.length,Type.PIECE, payload);
    }

    public int getIndex() {
        ByteBuffer bb = ByteBuffer.wrap(getSubsetOfPayload(0, 3));
        return bb.getInt();
    }

    public byte[] getPieceData() {
        return getSubsetOfPayload(4, (payload.length - 1));
    }

    public byte[] getSubsetOfPayload(int start, int end){
        byte[] subset = new byte[(end - start) + 1];

        for (int i = start; i <= end; i++){
            subset[i - start] = payload[i];
        }
        return subset;
    }
}
