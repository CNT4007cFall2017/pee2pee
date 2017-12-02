import java.util.Timer;

/**
 * Created by chase on 10/19/2017.
 *
 * This class is responsible for creating a listening server socket, and creating connection sockets for each
 * peer that connects to this peer.  For each connecting peer, a new thread is spawned.  This thread waits for
 * a message and then handles the message.
 */
public class Process {

    private FileHandler fileHandler;

    private PeerInfo myPeer;

    public Process(int peerId) {

        myPeer = new PeerInfo(peerId);
        fileHandler = new FileHandler("config/PeerInfo.cfg", "config/Common.cfg", myPeer);
        Timer chokerUnchokerTimer = new Timer();
        chokerUnchokerTimer.schedule(new ChokerUnchoker(myPeer), 0, (myPeer.CommonConfig.get(PeerInfo.UNCHOKING_INTERVAL) * 1000));
    }
}
