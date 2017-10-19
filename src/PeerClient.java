import java.io.*;
import java.net.Socket;

/**
 * Created by chase on 10/18/2017.
 */
public class PeerClient {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String msg_send;
    private String msg_recv;

    public PeerClient() {

    }

    public void run() {
        try {
            //create a socket to connect to the server
            socket = new Socket("192.168.1.138", 9000);
            System.out.println("Connected to server");

            //init io streams
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("here");
            //get input from std input
            //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            while(true) {
                //read a sentence from the std in
                //msg_send = br.readLine();

                System.out.println("sending message");
                //send something to the server
                sendMessage("hello");
                System.out.println("sent");
                //get something from the server
                msg_recv = (String)input.readObject();
                System.out.println(msg_recv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            //close connections
            try {
                input.close();
                output.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String msg) {
        try {
            //stream write the message
            output.writeObject(msg);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMsg_send(String s) {
        msg_send = s;
    }

    public String getMsg_recv() {
        return msg_recv;
    }

}
