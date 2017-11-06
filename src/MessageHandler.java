import Message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MessageHandler {
    private Socket connSock;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public MessageHandler(Socket _socket){
        connSock = _socket;

        try {
            output = new ObjectOutputStream(connSock.getOutputStream());
            input = new ObjectInputStream(connSock.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Message handle(Message message) {
        if (message.getType() == 5) {
            return message;
        } else {
            return null; //Not sure what to actually return
        }
    }


    public Message recv() throws IOException {
        //clientSoc = new Socket(p.getHostname(), p.getPort());
        Message recMessage = null;
        try {
            recMessage = (Message)input.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return handle(recMessage);

    }
}
