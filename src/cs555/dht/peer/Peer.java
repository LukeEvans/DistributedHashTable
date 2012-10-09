package cs555.dht.peer;

// Class to abstract the peer
public class Peer {

	public String hostname;
	public int port;
	public String nickname;

	//================================================================================
	// Constructors
	//================================================================================
	public Peer(String host, int p, String n) {
		hostname = host;
		port = p;
		nickname = n;
	}
	
	public Peer(String host, int p) {
		hostname = host;
		port = p;
		nickname = "";
	}
	
	//================================================================================
	// House Keeping
	//================================================================================

	// Override .equals method
	public boolean equals(Peer other) {
		if (other.hostname.equalsIgnoreCase(this.hostname)) {
			if (other.port == this.port) {
				if (other.nickname.equalsIgnoreCase(this.nickname)){
					return true;
				}
			}
		}

		return false;
	}

	// Override .toString method
	public String toString() {
		String s = "";

		s += "[" + hostname + ", " + port + ", " + nickname + "]";

		return s;
	}

}
