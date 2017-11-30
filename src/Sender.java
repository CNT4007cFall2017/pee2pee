import java.io.IOException;
import java.io.ObjectOutputStream;
import Message.*;

public class Sender implements Runnable {

    private ObjectOutputStream output;
    private Message message;

    public Sender(ObjectOutputStream output, Message message) {
        this.output = output;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
