package Message;

public class Message {
    private int length;
    private int type; //0-7
    private byte[] payload;

    public Message(int length, int type, byte[] payload) { //4-7
        this.length = length;
        this.type = type;
        this.payload = payload;
    }
    public Message(int type) { //0-3
        this.type = type;
    }
}
