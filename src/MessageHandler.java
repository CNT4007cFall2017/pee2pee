import Logger.Logger;
import Message.*;
import com.sun.imageio.plugins.common.BitFile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.BitSet;
import java.util.Set;

public class MessageHandler {
    private Socket connSock;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Set<Integer> validPeerIds;
    private Peer myPeer;
    private int remotePeerId;

    public MessageHandler(Socket _socket, Set<Integer> _validPeerIds, Peer _myPeer){
        connSock = _socket;
        validPeerIds = _validPeerIds;
        myPeer = _myPeer;

        try {
            output = new ObjectOutputStream(connSock.getOutputStream());
            input = new ObjectInputStream(connSock.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void teardown() {
        try {
            connSock.close();
            input.close();
            output.close();
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


    public void recv() throws IOException {
        Message recMessage;
        try {
            recMessage = (Message)input.readObject();
            handle(recMessage);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void handle(Message message) {
        switch (message.getType()) {
            case Type.HANDSHAKE:
                int id = ((Handshake) message).getIdField();

                if (validateHandshake(id)) {
                    remotePeerId = id;
                    try {
                        Logger.logTCPConnection(remotePeerId, myPeer.getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case Type.BITFIELD:
              // store remote bitfield
              BitSet bitfield = ((Bitfield) message).getBitSet();
              myPeer.remoteBitfields.put(remotePeerId, bitfield);
              if (myPeer.hasFile()) {
                  send(new Bitfield(myPeer.getBitfield().toByteArray()));
              }
              myPeer.printBitfields();
              break;

            default:
                teardown();
        }
    }

    private boolean validateHandshake(int peerId) {
        return validPeerIds.contains(peerId);
    }
}
