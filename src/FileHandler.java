import java.io.*;
import java.net.Socket;
import java.util.*;

public class FileHandler {
    private Collection<PeerInfo> peersToConnectTo;
    private PeerInfo myPeer;
    private Set<Integer> validPeerIds;
    private String fileName;

    public FileHandler(String peerInfoConfig, String CommonConfig, PeerInfo myPeer) {
        validPeerIds = new HashSet<>();
        peersToConnectTo = new ArrayList<>();
        this.myPeer = myPeer;

        readPeerInfoConfig(peerInfoConfig);
        readCommonConfig(CommonConfig);
        connectToPeers();
    }

    private void readPeerInfoConfig(String configFileName) {
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



                if (myPeer.peerId != currId) {
                    validPeerIds.add(currId);
//                    myPeer.remotePeers.put(currId, new RemotePeerInfo(currId));
                }

                if (myPeer.peerId == currId) { // found self
                    foundSelf = true;
                    myPeer.hostname = hostname;
                    myPeer.port = port;
                    myPeer.hasFile = hasFile;

                    if (hasFile) {
                        myPeer.setAllBits();
                    }

                    new Thread(new ServerProcess(myPeer)).start();
                } else if (!foundSelf) { // looking at peer above self in the list
                    PeerInfo currPeer = new PeerInfo(currId, hostname, port);
                    peersToConnectTo.add(currPeer);
                }
            }

            myPeer.validPeerIds = validPeerIds;
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    private void readCommonConfig(String commonConfig){
        File configFile = new File(commonConfig);
        try{
            FileReader fileReader = new FileReader(configFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(" ");
                String attribute = tokens[0];
                if(!tokens[1].contains(".")) {
                    int value = Integer.parseInt(tokens[1]);
                    myPeer.CommonConfig.put(attribute, value);
                }
                else{
                    fileName = tokens[1];
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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

}
