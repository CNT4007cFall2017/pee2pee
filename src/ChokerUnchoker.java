import Message.Choke;
import Message.Unchoke;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.Remote;
import java.util.*;


public class ChokerUnchoker extends TimerTask {
    private PeerInfo localPeerInfo;
    private ArrayList<RemotePeerInfo> remotePeers;
  public ChokerUnchoker (PeerInfo peerInfo) {
      super();
      localPeerInfo = peerInfo;
      remotePeers = new ArrayList<RemotePeerInfo>();
      //initiliaze bytes recieved array;
      //iterate over the localremotePeers hashmap and create and entry in bytesrecieved for each;

  }
  //function for sorting the bytes recieved array every execute cycle.

    public void run() {
        PeerInfo temp = new PeerInfo(localPeerInfo);
        remotePeers.clear();
        for(int pID : temp.remotePeers.keySet()){
            remotePeers.add(temp.remotePeers.get(pID));
        }
        Collections.sort(remotePeers, new SortByBytes());
        int numPreferred = temp.CommonConfig.get(PeerInfo.NUM_PREFERRED);

        for (int i = 0; i < remotePeers.size(); i++) {
            if (i < numPreferred) {
                temp.preferredNeighbors.add(remotePeers.get(i));
                temp.unpreferredNeighbors.remove(remotePeers.get(i));
            } else {
                temp.preferredNeighbors.remove(remotePeers.get(i));
                temp.unpreferredNeighbors.add(remotePeers.get(i));
            }
        }

        for (RemotePeerInfo rpi : temp.preferredNeighbors) {
            rpi.messageHandler.send(new Unchoke());
        }
        for(RemotePeerInfo rpi : temp.unpreferredNeighbors){
            rpi.messageHandler.send(new Choke());
        }
    }
}

