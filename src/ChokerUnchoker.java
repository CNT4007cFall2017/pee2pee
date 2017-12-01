import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.Remote;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class ChokerUnchoker{
    private static PeerInfo peerInfo;
  public ChokerUnchoker(PeerInfo _peerInfo) {
     peerInfo = _peerInfo;
   }

    public static void main(String[] args)
    {

            Timer timer = new Timer();
            TimerTask task = new TaskHelper(peerInfo);
            timer.schedule(task, 2000, 5000);


    }
    //keeping it just in case
    public void registerRemotePeer(Integer pID, ObjectOutputStream output, ObjectInputStream input){

    }
}

class TaskHelper extends TimerTask {
    private PeerInfo localPeerInfo;
    private HashMap localRemotePeers;
    private RemotePeerInfo[] bytesRecieved;
  public TaskHelper (PeerInfo peerInfo) {
        super();
      localPeerInfo = peerInfo;
      localRemotePeers  = localPeerInfo.remotePeers;
      //initiliaze bytes recieved array;
      bytesRecieved = new RemotePeerInfo[localRemotePeers.size()];
      Integer i = 0;
      //iterate over the localremotePeers hashmap and create and entry in bytesrecieved for each;
      for(Object pID : localRemotePeers.keySet()){
          bytesRecieved[i] = (RemotePeerInfo)localRemotePeers.get(pID);
          i++;
      }
      //
  }
  //function for sorting the bytes recieved array every execute cycle.
  public void sortBytesRecieved(RemotePeerInfo[] _bytesRecieved){
      int maxBytes = 0;

  }

    public static int i = 0;
    public void run() {
        System.out.println("Timer ran " + ++i);
    }
}

