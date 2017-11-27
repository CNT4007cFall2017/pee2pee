import java.io.*;
import java.net.Socket;
import java.util.*;

public class FileHandler {
//    private final Collection<Peer> remotePeers;
    private Collection<PeerInfo> peersToConnectTo;
    private PeerInfo myPeer;
    private Set<Integer> validPeerIds;
//    private final String configFileName;
//
//    public int peerCount;  // total number of peers

    public FileHandler(String configFileName, PeerInfo myPeer) {
//        remotePeers = new ArrayList<>();
        validPeerIds = new HashSet<>();
        peersToConnectTo = new ArrayList<>();
        this.myPeer = myPeer;
//        this.configFileName = configFileName;
//        peerCount = 0;

        readConfigFile(configFileName);
        connectToPeers();
    }

    private void readConfigFile(String configFileName) {
        File configFile = new File(configFileName);

        try {
            FileReader fileReader = new FileReader(configFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            boolean foundSelf = false;

            while ((line = bufferedReader.readLine()) != null) { // loop through all peers in config file
                String[] tokens = line.split(" ");

                int currId = Integer.parseInt(tokens[0]);
                String hostname = tokens[1];
                int port = Integer.parseInt(tokens[2]);
                boolean hasFile = tokens[3].equals("1");

                if (myPeer.peerId == currId) { // found self
                    foundSelf = true;
                    myPeer.hostname = hostname;
                    myPeer.port = port;
                    myPeer.hasFile = hasFile;
                    new Thread(new ServerProcess(myPeer)).start();
                } else if (!foundSelf) { // looking at peer above self in the list
                    PeerInfo currPeer = new PeerInfo(currId, hostname, port);
                    peersToConnectTo.add(currPeer);
                }

                validPeerIds.add(currId);
            }

            myPeer.validPeerIds = validPeerIds;
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void connectToPeers() {
        for (PeerInfo p : peersToConnectTo) {
            // spawn threads for each connection
            try {
                new Thread(new ConnectionWorker(new Socket(p.hostname, p.port), myPeer)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public void gatherRemotePeers() {
//        File configFile = new File(configFileName);
//
//        try {
//            FileReader fileReader = new FileReader(configFile);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            String line;
//            peerCount = 0;
//        String[] toks = line.split(" ");
//
//                while ((line = bufferedReader.readLine()) != null) {
//                    peerCount++;
//                    Peer peer = new Peer(Integer.parseInt(toks[0]), toks[1], Integer.parseInt(toks[2]), toks[3].equals("1"), peerCount);
//                remotePeers.add(peer);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public Peer findSelf(int peerId) {
//        for(Peer p : remotePeers) {
//            if (p.getId() == peerId) {
//                return p;
//            }
//        }
//        return null;
//    }
//
//    public void initPeerLists(Peer myPeer) {
//        boolean listFlag = false;
//        for(Peer p : remotePeers) {
//            if (p.getId() == myPeer.getId()) {
//                listFlag = true;
//                continue;
//            }
//
//            if (!listFlag) {
//                peersToConnectTo.add(p);
//            }
//            allowedPeerConnections.add(p.getId());
//            myPeer.remoteBitfields.put(p.getId(), new BitSet(16));
//        }
//    }
//
//    public void setPeerInputLimit() {
//        for (Peer p : remotePeers) {
//            p.setInputConnLimit(peerCount - p.getIndex());
//        }
//    }
//
//    public Collection<Peer> getPeersToConnectTo() {
//        return peersToConnectTo;
//    }
//
//    public Set<Integer> getAllowedPeerConnections() {
//        return allowedPeerConnections;
//    }
//
//    public Collection<Peer> getRemotePeers() {
//        return remotePeers;
//    }

}
