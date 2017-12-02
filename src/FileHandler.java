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
        readCommonConfig(CommonConfig);
        splitFile();
        readPeerInfoConfig(peerInfoConfig);
        connectToPeers();
    }


    private void readPeerInfoConfig(String configFileName) { //Take in PeerInfo.cfg and read the file
        File configFile = new File(configFileName);

        try {
            FileReader fileReader = new FileReader(configFile); //Set file reader to take PeerInfo.cfg
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            boolean foundSelf = false; //bool for self peer

            while ((line = bufferedReader.readLine()) != null) { // loop through all peers in config file
                String[] tokens = line.split(" "); //Split config by spaces

                int currId = Integer.parseInt(tokens[0]); //Set current ID (local host)
                String hostname = tokens[1];
                int port = Integer.parseInt(tokens[2]); //Set port number
                boolean hasFile = tokens[3].equals("1");    //Check if it has the file

                //My peer should have all info from config File\

                if (myPeer.peerId != currId) {
                    validPeerIds.add(currId);
//                    myPeer.remotePeers.put(currId, new RemotePeerInfo(currId));
                }

                if (myPeer.peerId == currId) { // found self, set myPeer values to values from files
                    foundSelf = true;
                    myPeer.hostname = hostname;
                    myPeer.port = port;
                    myPeer.hasFile = hasFile;

                    if (hasFile) {  //If self hasfile, set the bits for bitfield
                        myPeer.setAllBits();
                    }

                    new Thread(new ServerProcess(myPeer)).start(); //Create a new thread
                } else if (!foundSelf) { // looking at peer above self in the list
                    PeerInfo currPeer = new PeerInfo(currId, hostname, port); //Create new PeerInfo
                    peersToConnectTo.add(currPeer);
                }
            }

            myPeer.validPeerIds = validPeerIds; //Set specific peer's valid peer Ids
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    private void readCommonConfig(String commonConfig){ //Take in Common.cfg and read the file
        File configFile = new File(commonConfig);   //Read in common.cfg
        try{
            FileReader fileReader = new FileReader(configFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {    //Will line exists
                String[] tokens = line.split(" "); //Split line by spaces
                if(!tokens[1].contains(".")) {
                    String attribute = tokens[0];   //
                    double value = Integer.parseInt(tokens[1]);
                    myPeer.CommonConfig.put(attribute, value); //Makes a set using attribute and value
                }else{
                    fileName = tokens[1];
                }

            }
<<<<<<< HEAD
            PeerInfo.BITFIELD_SIZE = (int) Math.ceil(myPeer.CommonConfig.get(PeerInfo.FILE_SIZE).longValue()/myPeer.CommonConfig.get(PeerInfo.PIECE_SIZE).longValue());
=======
            PeerInfo.BITFIELD_SIZE =(int)Math.ceil(myPeer.CommonConfig.get(PeerInfo.FILE_SIZE)/myPeer.CommonConfig.get(PeerInfo.PIECE_SIZE));
>>>>>>> Dynamic bitfield size on init based on config file and piece size
            myPeer.myBitfield = new BitSet(PeerInfo.BITFIELD_SIZE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void splitFile() {
        File f = new File("config/" + fileName);

        int sizeOfPiece = myPeer.CommonConfig.get(PeerInfo.PIECE_SIZE); //sets an int for size of individual Piece
        byte[] buffer = new byte[sizeOfPiece];

        try (FileInputStream fis = new FileInputStream(f);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            while (bis.read(buffer) > 0) { //Read buffer to Pieces
                myPeer.Pieces.add(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void connectToPeers() {
        for (PeerInfo p : peersToConnectTo) {
            // spawn threads for each connection
            try {
                new Thread(new ConnectionWorker(new Socket(p.hostname, p.port), myPeer)).start(); //Start a thread for each peer to connect to
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
