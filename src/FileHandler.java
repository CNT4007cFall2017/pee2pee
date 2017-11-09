import java.io.*;
import java.util.*;

public class FileHandler {
    private final Collection<Peer> remotePeers;
    private final Collection<Peer> peersToConnectTo;
    private final Set<Integer> allowedPeerConnections;
    private final String configFileName;

    public int peerCount;  // total number of peers

    public FileHandler(String configFileName) {
        remotePeers = new ArrayList<>();
        allowedPeerConnections = new HashSet<>();
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
                return p;
            }
        }
        return null;
    }

    public void initPeerLists(Peer myPeer) {
        boolean listFlag = false;
        for(Peer p : remotePeers) {
            if (p.getId() == myPeer.getId()) {
                listFlag = true;
                continue;
            }

            if (!listFlag) {
                peersToConnectTo.add(p);
            }
            allowedPeerConnections.add(p.getId());
            myPeer.remoteBitfields.put(p.getId(), new BitSet(16));
        }
    }

    public void setPeerInputLimit() {
        for (Peer p : remotePeers) {
            p.setInputConnLimit(peerCount - p.getIndex());
        }
    }

    public Collection<Peer> getPeersToConnectTo() {
        return peersToConnectTo;
    }

    public Set<Integer> getAllowedPeerConnections() {
        return allowedPeerConnections;
    }

    public Collection<Peer> getRemotePeers() {
        return remotePeers;
    }

}
