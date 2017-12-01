import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;



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

    public static int i = 0;
    public void run() {
        System.out.println("Timer ran " + ++i);
        remotePeers.clear();
        for(int pID : localPeerInfo.remotePeers.keySet()){
            remotePeers.add(localPeerInfo.remotePeers.get(pID));
        }
        //
    }
}

