package cs555.dht.state;

import cs555.dht.peer.Peer;

public class FingerTable {
	
	int idSpace;
	Peer table[];
	
	//================================================================================
	// Constructor
	//================================================================================
	public FingerTable(int s) {
		idSpace = s;
		table = new Peer[idSpace];
	}
	
	
	
	//================================================================================
	// House keeping
	//================================================================================
	public String toString() {
		String s = "";
		
		for (int i=0; i<table.length; i++) {
			Peer p = table[i];
			
			s += i+1 + ": " + p.toString() + "\n"; 
		}
		
		return s;
	}
}
