import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

public class FileHandler {
    private final Collection<Peer> remotePeers;
    private final String configFileName;

    public FileHandler(String configFileName) {
        remotePeers = new ArrayList<>();
        this.configFileName = configFileName;
    }

    public void gatherRemotePeers() {
        File configFile = new File(configFileName);

        try {
            FileReader fileReader = new FileReader(configFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] toks = line.split(" ");
                Peer peer = new Peer(Integer.parseInt(toks[0]), toks[1], Integer.parseInt(toks[2]), toks[3].equals("1"));
                remotePeers.add(peer);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
