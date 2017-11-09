package Message;

import java.io.Serializable;
import java.util.BitSet;

public class Message implements Serializable {
    private int length;
    private int type; //0-7
    protected byte[] payload;

    public Message(int length, int type, byte[] payload) { //4-7
        this.length = length;
        this.type = type;
        this.payload = payload;
    }
    public Message(int type) { //0-3
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public byte[] getPayload() {
        return payload;
    }

    public int getPayloadLength() {
        return payload.length;
    }

    public BitSet getPayloadBitset() {
        BitSet res = new BitSet(16);
        for (int i = 0; i < res.size(); i++) {
            boolean isSet = payload[i] > 0;
            res.set(i, isSet);
        }

        return res;
    }
}
