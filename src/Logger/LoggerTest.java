package Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chase on 10/15/2017.
 */
public class LoggerTest
{
    public static void main(String args[]) throws IOException {
        List<String> testList = new ArrayList<>();
        testList.add("hello");
        testList.add("there");

        Logger.makeLogFile("test.txt");
        Logger.logTCPConnection("123", "456");
        Logger.logPrefNeighborsChange("123", testList);
        Logger.logOptUnchNeighborChange("123", "456");
        Logger.logUnchoke("123", "456");
        Logger.logChoke("123", "456");
        Logger.logHaveMsg("123", "456", 1);
        Logger.logInterestedMsg("123", "456");
        Logger.logNotInterestedMsg("123", "456");
        Logger.logDownloading("123", "456", 1, 2);
        Logger.logCompleteDownload("123");
    }
}
