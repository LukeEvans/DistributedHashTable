package cs555.dht.state;

import cs555.dht.peer.Peer;
import cs555.dht.utilities.Constants;

public class FingerTable {
	
	Peer table[];
	
	//================================================================================
	// Constructor
	//================================================================================
	public FingerTable() {
		table = new Peer[Constants.Id_Space];
	}
	
	
	//================================================================================
	// Resolve
	//================================================================================
	public Peer getNextClosest(int h) {
		return null;
	}
	
	//================================================================================
	// State manipulation
	//================================================================================
	// Add myself as every entry
	public void firstToArrive(Peer myself) {
		for (int i=0; i<table.length; i++) {
			table[i] = myself;
		}
	}
	
	public void addEntry(int location, Peer p) {
		table[location] = p;
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
