import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

public class FileHandler {
    private final Collection<Peer> remotePeers;
    private final Collection<Peer> peersToConnectTo;
    private final String configFileName;

    public int peerCount;  // total number of peers

    public FileHandler(String configFileName) {
        remotePeers = new ArrayList<>();
        peersToConnectTo = new ArrayList<>();
        this.configFileName = configFileName;
        peerCount = 0;
    }

    public void gatherRemotePeers() {
        File configFile = new File(configFileName);

        try {
            FileReader fileReader = new FileReader(configFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            peerCount = 0;

            while ((line = bufferedReader.readLine()) != null) {
                peerCount++;
                String[] toks = line.split(" ");
                Peer peer = new Peer(Integer.parseInt(toks[0]), toks[1], Integer.parseInt(toks[2]), toks[3].equals("1"), peerCount);
                remotePeers.add(peer);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Peer findSelf(int peerId) {
        for(Peer p : remotePeers) {
            if (p.getId() == peerId) {
                remotePeers.remove(p);
                return p;
            } else {
                peersToConnectTo.add(p);
            }
        }
        return null;
    }

    public void setPeerInputLimit() {
        for (Peer p : remotePeers) {
            p.setInputConnLimit(peerCount - p.getIndex());
        }
    }

    public Collection<Peer> getPeersToConnectTo() {
        return peersToConnectTo;
    }
}
