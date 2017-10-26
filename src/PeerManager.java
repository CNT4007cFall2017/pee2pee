//Sorry for not showing up last evening Chase, CEN took a bit longer than I really wanted, but it's done for now.

//Concept: Class should provide peers with a manager that chokes and unchokes peers based on project specification

//Possibly pass in the already constructed PeerInfoList

//will need to discuss what we'd like this to do.

//import wherever RemotePeerInfo ends up.

public class PeerManager implements Runnable{
	private final List<RemotePeerInfo> allPeers = new ArrayList<>();
	private final int numPrefNeighbors;
	private final int unchokeInterval;
	private final List<RemotePeerInfo> preferedPeers = new HashSet<>();
	private final int bitmapsize;
	//note that we still need to handle reading from the config file, the dalton project uses the Properties java utility to read the general config file into a properties object
	PeerManager(int selfPID, Collection<RemotePeerInfo> peerlist, Properties config){
		allPeers.addAll(peerlist);

	}
	//There is an unchoke interval and an optmistic unchoke interval, so I imagine optimistic unchoking will need to be it's own thread.



}