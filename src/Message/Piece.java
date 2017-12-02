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
        byte[] index = new byte[4];

        for (int i = 0; i < 4; i++) {
            index[i] = payload[i];
        }

        ByteBuffer bb = ByteBuffer.wrap(index);
        return bb.getInt();
    }

    public byte[] getSubsetOfPayload(int start, int end){
        byte[] subset = new byte[end - start+1];
        for (int i = start; i <= end; i++){
            subset[i] = payload[i];
        }
        return subset;
    }
}
