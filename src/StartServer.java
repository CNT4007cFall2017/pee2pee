// File Name GreetingServer.java
import java.net.*;
import java.io.*;

public class StartServer extends Thread {
    private ServerSocket serverSocket;

    public StartServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
    }

    public void run() {
        while(true) {
            try {
                System.out.println("Waiting for client on port " +
                        serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();

                System.out.println("Just connected to " + server.getRemoteSocketAddress());
                ObjectInputStream in = new ObjectInputStream(server.getInputStream());

                System.out.println((String)in.readObject());
                ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
                out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress()
                        + "\nGoodbye!");
                server.close();

            }catch(SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            }catch(IOException e) {
                e.printStackTrace();
                break;
            }catch(ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String [] args) {
        int port = 9000;
        try {
            Thread t = new StartServer(port);
            t.start();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}