/**
 * Created by chase on 10/18/2017.
 */
// File Name GreetingClient.java
import java.net.*;
import java.io.*;

public class StartClient {

    public static void main(String [] args) {
        String serverName = "192.168.1.138";
        int port = 9000;
        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF("Hello from " + client.getLocalSocketAddress());
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);

            System.out.println("Server says " + in.readUTF());
            client.close();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}