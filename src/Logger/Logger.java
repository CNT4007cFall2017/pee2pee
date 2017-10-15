package Logger;

import java.io.*;
import java.time.LocalDateTime;

/**
 * Created by chase on 10/8/2017.
 */
public class Logger {

    private static File logFile;
    private static BufferedWriter bw;

    public Logger() {

    }

    public static void makeLogFile(String fileName) throws IOException {
        logFile = new File(fileName);
        logFile.createNewFile();
        clearLogFile();
    }

    public static void logTCPConnection(String pid1, String pid2) throws IOException {
        try {
            bw = new BufferedWriter(new FileWriter(logFile, true));
            String message = String.format("%s: Peer %s makes a connection to Peer %s", LocalDateTime.now(), pid1, pid2);
            bw.write(message);
            bw.newLine();
            bw.flush();
        } finally {
            if (bw != null) {
                bw.close();
            }
        }
    }

    private static void clearLogFile() throws IOException {
        FileWriter fw = new FileWriter(logFile);
        fw.write("");
        fw.close();
    }
}
