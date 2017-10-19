/**
 * Created by chase on 10/18/2017.
 */
// File Name GreetingClient.java
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class StartClient {

    public static void main(String [] args) {
        String serverName = "192.168.1.108";
        int port = 9000;
        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            System.out.println("dicks");
            Socket client = new Socket(serverName, port);
            System.out.println("???");
            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outToServer);
            System.out.println("Output stream ready");
            InputStream inFromServer = client.getInputStream();
            ObjectInputStream in = new ObjectInputStream(inFromServer);
            System.out.println("input stream ready");

            String msg;
            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.println("ready for input");
                msg = sc.next();

                out.writeObject(msg);

                if (msg.equals("q")) {
                    break;
                }

                //System.out.println("Server says " + (String)in.readObject());
            }
            out.close();
            in.close();
            inFromServer.close();
            outToServer.close();
            client.close();

        }catch(IOException e) {
            e.printStackTrace();
        }
        
    }
}