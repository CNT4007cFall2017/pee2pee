import java.io.*;
import java.net.*;

/**
 * Created by chase on 10/18/2017.
 */
public class PeerServer {
    private static final int port = 9000;

    private ServerSocket listener;

    public PeerServer() throws IOException {
        try {
            listener = new ServerSocket(port);
            int clientNum = 1;

            while(true) {
                new Handler(listener.accept(), clientNum).start();
                clientNum++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            listener.close();
        }
    }

    private static class Handler extends Thread {
        private String msgRecv;
        private String msgSend;
        private Socket connection;
        private ObjectInputStream input;
        private ObjectOutputStream output;
        private int clientNumber;

        public Handler(Socket connection, int num) {
            System.out.println(connection);
            this.connection = connection;
            this.clientNumber = num;
        }

        public void run() {
            try {
                input = new ObjectInputStream(connection.getInputStream());
                output = new ObjectOutputStream(connection.getOutputStream());
                output.flush();

                try {
                    while(true) {
                        msgRecv = (String)input.readObject();
                        System.out.println(msgRecv);
                        msgSend = "Thank you";

                        sendMessage(msgSend);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        input.close();
                        output.close();
                        connection.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String msg) {
            try {
                output.writeObject(msg);
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
