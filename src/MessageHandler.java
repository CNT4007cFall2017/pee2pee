import Message.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;

public class MessageHandler {
    private Socket connSock;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Set<Integer> validPeerIds;

    public MessageHandler(Socket _socket, Set<Integer> _validPeerIds){
        connSock = _socket;
        validPeerIds = _validPeerIds;

        try {
            output = new ObjectOutputStream(connSock.getOutputStream());
            input = new ObjectInputStream(connSock.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Message message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public Object recv() throws IOException {
        Message recMessage = null;
        try {
            recMessage = (Message)input.readObject();
            return handle(recMessage);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Object handle(Message message) {
        switch (message.getType()) {
            case Type.HANDSHAKE:
                int id = ((Handshake) message).getIdField();
                System.out.println("got handshake message from " + id);
                return validateHandshake(id);
            default:
                return null;
        }
    }

    private boolean validateHandshake(int peerId) {
        return validPeerIds.contains(peerId);
    }
}
