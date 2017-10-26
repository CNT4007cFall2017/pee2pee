//Sorry for not showing up last evening Chase, CEN took a bit longer than I really wanted, but it's done for now.

//Concept: Class should provide peers with a manager that chokes and unchokes peers based on project specification

//Possibly pass in the already constructed PeerInfoList

//will need to discuss what we'd like this to do.

//import wherever RemotePeerInfo ends up.

public class PeerManager implements Runnable{
	private final List<Peer> allPeers = new ArrayList<>();
	private final int numPrefNeighbors;
	private final int unchokeInterval;
	private final int optUnchokeInterval;
	private final List<Peer> preferedPeers = new HashSet<>();
	//private final int bitmapsize;
	//note that we still need to handle reading from the config file, the dalton project uses the Properties java utility to read the general config file into a properties object
	PeerManager(int selfPID, Collection<Peer> peerlist, String gen_config){
		//assumes that a list of peers has already been built in calling process.
		allPeers.addAll(peerlist);
		// reads passed in config file
		File config = new File(gen_config);
		try{
			FileReader file = new FileReader(config);
			BufferedReader bufferedReader =  new BufferedReader(file);
			String line;
			Int[] attributes;
			//the common config file is a set length, only the first 3 are of interest to peerManager.
			for(int i = 0; i < 2; i++){
				line = bufferedReader.readLine();
				String[] toks = line.split(" ");
				attributes[i] = toks[1];
			}
			//may want to convert these to milleseconds for thread.sleep
			numPrefNeighbors = attributes[0];
			unchokeInterval = attributes[1];
			optUnchokeInterval = attributes[2];
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		//I typed those catches out by hand, are you proud Chase?

	}
	//There is an unchoke interval and an optmistic unchoke interval, so I imagine optimistic unchoking will need to be it's own thread.



}