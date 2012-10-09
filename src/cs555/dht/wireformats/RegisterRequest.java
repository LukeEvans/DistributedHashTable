package cs555.dht.wireformats;

import java.nio.ByteBuffer;

import cs555.dht.utilities.Constants;
import cs555.dht.utilities.Tools;

public class RegisterRequest{

	public int size;
	
	public int type; // 4
	
	public int hostLength; // 4 
	public String hostName; // hostLength
	
	public int port; // 4
	
	public int nickNameLength; // 4
	public String nickName; // nickNameLength
	
	
	//================================================================================
	// Constructors
	//================================================================================
	public RegisterRequest(String h, int p, String n){
		init(h, p, n);
	}
	
	public RegisterRequest(){
		init("",0,"");
	}
	
	public void init(String h, int p, String n){
		type = Constants.Registration_Request;
		hostLength = h.length();
		hostName = h;
		
		port = p;
		
		nickNameLength = n.length();
		nickName = n;
		
		size = 4 + 4 + hostLength + 4 + 4 + nickNameLength;
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
		
		// Url length and url
		nickNameLength = bbuff.getInt();
		byte[] nickNameBytes = new byte[nickNameLength];
		bbuff.get(nickNameBytes);
		nickName = new String(nickNameBytes,0,nickNameLength);
		
	}
	
	//================================================================================
	// House Keeping
	//================================================================================
	// Override the toString method
	public String toString(){
		String s = "";
		
		s += "Peer: " + hostName + ":" + port + ", " + nickName + "\n";
		
		return s;
	}
	
	// Override the equals method
	public boolean equals(RegisterRequest other) {
		if (this.hostName.equalsIgnoreCase(other.hostName)) {
			if (this.port == other.port) {
				if (this.nickName.equalsIgnoreCase(other.nickName)) {
					return true;
				}
			}
		}
		
		return false;
	}
}
