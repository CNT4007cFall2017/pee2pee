package Logger;

import java.io.IOException;

/**
 * Created by chase on 10/15/2017.
 */
public class LoggerTest
{
    public static void main(String args[]) throws IOException {
        Logger.makeLogFile("test.txt");
        Logger.logTCPConnection("123", "456");
        Logger.logTCPConnection("123", "456");
        Logger.logTCPConnection("12", "45");
    }
}
