package Logger;

import Message.Bitfield;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

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

    public static void logTCPConnection(int pid1, int pid2) throws IOException {
        String message = String.format("%s: Peer %d makes a connection to Peer %d", LocalDateTime.now(), pid1, pid2);
        log(message);
    }

    public static void logBitfieldRecieved(Bitfield bi) throws IOException {
        String s;
        s = Arrays.toString(bi.getPayload());

        String message = String.format("%s: Peer recieves a bitfield: %s", LocalDateTime.now(), s);
        log(message);
    }

    public static void logPrefNeighborsChange(String pid, List<String> neighbors) throws IOException {
        String neighborsStr = listToString(neighbors);
        String message = String.format("%s: Peer %s has the preferred neighbors %s", LocalDateTime.now(), pid, neighborsStr);
        log(message);
    }

    public static void logOptUnchNeighborChange(String pid1, String optUnchNeighbor) throws IOException {
        String message = String.format("%s: Peer %s makes a connection to Peer %s", LocalDateTime.now(), pid1, optUnchNeighbor);
        log(message);
    }

    public static void logUnchoke(int pid1, int pid2) throws IOException {
        String message = String.format("%s: Peer %s has unchoked peer %s", LocalDateTime.now(), pid2, pid1);
        log(message);
    }

    public static void logChoke(int pid1, int pid2) throws IOException {
        String message = String.format("%s: Peer %s is choked by %s", LocalDateTime.now(), pid1, pid2);
        log(message);
    }

    public static void logHaveMsg(String pid1, String pid2, int index) throws IOException {
        String message = String.format("%s: Peer %s received the 'have' message from %s for the piece %d", LocalDateTime.now(), pid1, pid2, index);
        log(message);
    }

    public static void logInterestedMsg(int pid1, int pid2) throws IOException {
        String message = String.format("%s: Peer %d received the 'interested' message from %d", LocalDateTime.now(), pid1, pid2);
        log(message);
    }

    public static void logNotInterestedMsg(int pid1, int pid2) throws IOException {
        String message = String.format("%s: Peer %d received the 'not interested' message from %d", LocalDateTime.now(), pid1, pid2);
        log(message);
    }

    public static void logDownloading(int pid1, int pid2, int index, int numPieces) throws IOException {
        String message = String.format("%s: Peer %d has downloaded the piece %d from %d.\r\nNow the number of pieces it has is %d", LocalDateTime.now(), pid1, index, pid2, numPieces);
        log(message);
    }

    public static void logCompleteDownload(String pid) throws IOException {
        String message = String.format("%s: Peer %s has downloaded the complete file.", LocalDateTime.now(), pid);
        log(message);
    }

    public static void debug(String msg) throws IOException {
        String message = String.format("%s: %s", LocalDateTime.now(), msg);
        log(message);
    }

    private static synchronized void log(String msg) throws IOException {
        try {
            bw = new BufferedWriter(new FileWriter(logFile, true));
            bw.write(msg);
            bw.newLine();
            bw.flush();
            System.out.println(msg);
        } finally {
            if (bw != null) {
                bw.close();
            }
        }
    }

    private static String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();

        for (String s : list) {
            sb.append(s);
            sb.append("\t");
        }

        return sb.toString();
    }

    private static void clearLogFile() throws IOException {
        FileWriter fw = new FileWriter(logFile);
        fw.write("");
        fw.close();
    }
}
