import java.net.Socket;
import java.util.Set;

public class ConnectionWorker implements Runnable {

    private Socket socket;
    private boolean receivedHandshake;


    public ConnectionWorker(Socket socket) {
        receivedHandshake = false;
        this.socket = socket;
    }

    @Override
    public void run() {
        //while true
            //listen for message
    }

}
