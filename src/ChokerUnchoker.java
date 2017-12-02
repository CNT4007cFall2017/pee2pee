import Message.Choke;
import Message.Unchoke;

import java.util.*;


public class ChokerUnchoker extends TimerTask {
    private PeerInfo localPeerInfo;
    private ArrayList<RemotePeerInfo> interestedPeers;
  public ChokerUnchoker (PeerInfo peerInfo) {
      super();
      localPeerInfo = peerInfo;
      interestedPeers = new ArrayList<>();
  }

    public void run() {
        PeerInfo temp = new PeerInfo(localPeerInfo);

        if (temp.interestedPeers.size() > 0) {
            interestedPeers.clear();
            for (int pID : temp.interestedPeers.keySet()) {
                interestedPeers.add(temp.interestedPeers.get(pID));
            }
            Collections.sort(interestedPeers, new SortByBytes());
            temp.resetBytesReceived();
            int numPreferred = temp.CommonConfig.get(PeerInfo.NUM_PREFERRED);

            for (int i = 0; i < interestedPeers.size(); i++) {
                RemotePeerInfo currRemotePeer = interestedPeers.get(i);
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

