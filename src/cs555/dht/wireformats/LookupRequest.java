package cs555.dht.wireformats;

import java.nio.ByteBuffer;

import cs555.dht.utilities.Constants;
import cs555.dht.utilities.Tools;

public class LookupRequest{

	public int size;
	
	public int type; // 4
	
	public int hostLength; // 4 
	public String hostName; // hostLength
	
	public int port; // 4
	
	public int nickNameLength; // 4
	public String nickName; // nickNameLength
	
	public int resolveLength; // 4
	public String resolveString; // resolveLength
	
	
	//================================================================================
	// Constructors
	//================================================================================
	public LookupRequest(String h, int p, String n, String r){
		init(h, p, n, r);
	}
	
	public LookupRequest(){
		init("",0,"","");
	}
	
	public void init(String h, int p, String n, String r){
		type = Constants.lookup_request;
		hostLength = h.length();
		hostName = h;
		
		port = p;
		
		nickNameLength = n.length();
		nickName = n;
		
		resolveLength = r.length();
		resolveString = r;
		
		size = 4 + 4 + hostLength + 4 + 4 + nickNameLength + 4 + resolveLength;
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
		
		// Host length and hostname
		bbuff.putInt(hostLength);
		bbuff.put(Tools.convertToBytes(hostName));
		
		// Port 
		bbuff.putInt(port);
		
		// nickname length and nickname
		bbuff.putInt(nickNameLength);
		bbuff.put(Tools.convertToBytes(nickName));
		
		// Resolve length and resolve
		bbuff.putInt(resolveLength);
		bbuff.put(Tools.convertToBytes(resolveString));
		
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
		
		// Host length and hostname
		hostLength = bbuff.getInt();
		byte[] hostBytes = new byte[hostLength];
		bbuff.get(hostBytes);
		hostName = new String(hostBytes,0,hostLength);
		
		// Port
		port = bbuff.getInt();
		
		// Nickname length and nickname
		nickNameLength = bbuff.getInt();
		byte[] nickNameBytes = new byte[nickNameLength];
		bbuff.get(nickNameBytes);
		nickName = new String(nickNameBytes,0,nickNameLength);
		
		// Resolve length and resolve
		resolveLength = bbuff.getInt();
		byte[] resolveBytes = new byte[resolveLength];
		bbuff.get(resolveBytes);
		resolveString = new String(resolveBytes,0,resolveLength);
	}
	
	//================================================================================
	// House Keeping
	//================================================================================
	// Override the toString method
	public String toString(){
		String s = "";
		
		s += "Peer: " + hostName + ":" + port + ", " + nickName + " resolving :" + resolveString + "\n";
		
		return s;
	}
	
	// Override the equals method
	public boolean equals(LookupRequest other) {
		if (this.resolveString.equalsIgnoreCase(other.resolveString)){
			return true;
		}
		
		return false;
	}
}
