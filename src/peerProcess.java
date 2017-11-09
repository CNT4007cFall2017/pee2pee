import Logger.Logger;

import java.io.IOException;

/**
 * Created by chase on 10/19/2017.
 */
public class peerProcess {
    public static void main(String[] args) {
        try {
            int myPeerId = Integer.parseInt(args[0]);
            Logger.makeLogFile(String.format("log%d.txt", myPeerId));
            Process process = new Process(myPeerId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
