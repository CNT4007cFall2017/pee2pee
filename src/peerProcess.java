import Logger.Logger;

import java.io.IOException;

/**
 * Created by chase on 10/19/2017.
 */
public class peerProcess {
    public static void main(String[] args) {
        try {
            Logger.makeLogFile("log.txt");
            Process process = new Process(Integer.parseInt(args[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
