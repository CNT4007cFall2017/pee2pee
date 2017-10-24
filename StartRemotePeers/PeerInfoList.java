
import java.io.BufferedReader;
import java.io.IOexception;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.io.Reader;
import java.text.ParseException;
import java.util.LinkedList;
import RemotePeerInfo;





public class PeerInfoList{
	private final Collections<RemotePeerInfo> peerInfoList = new LinkedList<>();

	public void readIn (Reader reader) throws FileNotFoundException, IOexception, ParseException {
		BufferedReader input = new BufferedReader(reader);
		int j = 0;
		for (String line; (line = input.readLine()) != NULL;){
			//remove the dreaded whitespace
			line = line.trim();
			if(line.length<=0){
				continue;
			}
			//split string into tokens based on whitespace
			String[] toks = line.split("\\s+");
			//get the third line (number representing if it has the file) and convert to boolean
			final boolean _hasFile = (toks[3].trim().compareTo("1") == 0);
			peerInfoList.add (new RemotePeerInfo(tokens[0].trim(), tokens[1].trim(), tokens[2].trim(), _hasFile));

			i++;
		}
	}

	public Collections<RemotePeerInfo> getPeerInfoList(){
		return  new LinkedList<>(peerInfoList);

	}
}