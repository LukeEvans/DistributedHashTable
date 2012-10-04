package cs555.dht.wireformats;

import java.nio.ByteBuffer;

import cs555.dht.utilities.Constants;
import cs555.dht.utilities.Tools;

public class ElectionMessage {

	public int size;
	public int type; // 4
	public int port; // 4
	public int hostLength; // 4
	public String host; // hostLength
	
	//================================================================================
	// Overridden Constructors
	//================================================================================
	public ElectionMessage(int p, String h){
		hostLength = h.length();
		host = h;
		port = p;
		size = 4 + 4 + 4 + hostLength;
		type = Constants.Election_Message;
	}
	
	public ElectionMessage(){
		hostLength = 0;
		host = "";
		port = 0;
		size = 0;
		type = Constants.Election_Message;
	}
	
	//================================================================================
	// Marshall
	//================================================================================
	public byte[] marshall(){
		byte[] bytes = new byte[size + 4];
		ByteBuffer bbuff = ByteBuffer.wrap(bytes);
		
		// Size
		bbuff.putInt(size);
		
		// type
		bbuff.putInt(type);
		
		// Port 
		bbuff.putInt(port);
		
		// Host length and host
		bbuff.putInt(hostLength);
		bbuff.put(Tools.convertToBytes(host));
		
		return bytes;
	}
	
	
	//================================================================================
	// Unmarshall
	//================================================================================
	public void unmarshall(byte[] bytes){
		ByteBuffer bbuff = ByteBuffer.wrap(bytes);
		
		// Size
		size = bbuff.getInt();
		
		// type
		type = bbuff.getInt();
		
		// port
		port = bbuff.getInt();
		
		// Domain length and domain
		hostLength = bbuff.getInt();
		byte[] hostBytes = new byte[hostLength];
		bbuff.get(hostBytes);
		host = new String(hostBytes,0,hostLength);
		
	}
	
	//================================================================================
	// House Keeping
	//================================================================================
	public String toString(){
		String s = "";
		
		s += "NodeManager: " + host + ":" + port + "\n";
		
		return s;
	}
	
}
