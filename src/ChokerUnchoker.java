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
      remotePeers = new ArrayList<>();
  }

    public void run() {
        PeerInfo temp = new PeerInfo(localPeerInfo);

        if (temp.remotePeers.size() > 0) {
            remotePeers.clear();
            for (int pID : temp.remotePeers.keySet()) {
                remotePeers.add(temp.remotePeers.get(pID));
            }
            Collections.sort(remotePeers, new SortByBytes());
            temp.resetBytesReceived();
            int numPreferred = temp.CommonConfig.get(PeerInfo.NUM_PREFERRED);

            for (int i = 0; i < remotePeers.size(); i++) {
                RemotePeerInfo currRemotePeer = remotePeers.get(i);
                if (i < numPreferred) {
                    temp.preferredNeighbors.add(currRemotePeer);
                    temp.unpreferredNeighbors.remove(currRemotePeer);
                } else {
                    temp.preferredNeighbors.remove(currRemotePeer);
                    temp.unpreferredNeighbors.add(currRemotePeer);
                }
            }

            localPeerInfo.reset(temp);

            for (RemotePeerInfo rpi : temp.preferredNeighbors) {
                rpi.messageHandler.send(new Unchoke());
            }
            for (RemotePeerInfo rpi : temp.unpreferredNeighbors) {
                rpi.messageHandler.send(new Choke());
            }
        }
    }
}

