import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class ChokerUnchoker{
//    private PeerInfo peerInfo;
//    public ChokerUnchoker(PeerInfo peerInfo) {
//        this.peerInfo = peerInfo;
//    }

    public static void main(String[] args)
    {

        Timer timer = new Timer();
        TimerTask task = new TaskHelper();
        timer.schedule(task, 2000, 5000);

    }
}

class TaskHelper extends TimerTask {
//    public TaskHelper (PeerInfo peerInfo) {
//        super();
//        PeerInfo peerInfo;
//    }
//    HashMap localRemotePeer = peerInfo.remotePeers;
    public static int i = 0;
    public void run() {
        System.out.println("Timer ran " + ++i);
    }
}

