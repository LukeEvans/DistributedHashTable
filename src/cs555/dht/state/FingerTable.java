package cs555.dht.state;

import cs555.dht.peer.Peer;
import cs555.dht.utilities.Constants;

public class FingerTable {
	
	Peer table[];
	int id;
	
	//================================================================================
	// Constructor
	//================================================================================
	public FingerTable(int i) {
		table = new Peer[Constants.Id_Space];
		id = i;
	}
	
	
	//================================================================================
	// Resolve
	//================================================================================
	public Peer getNextClosest(int h) {
		
		// If we know that our successor should hold the ID, send it there
		if ((h <= table[0].id) && (h > id)) {
			return table[0];
		}
		
		// If the id is passed our purview, send it as far as we can
		if (h > table[Constants.Id_Space - 1].id) {
			return table[Constants.Id_Space - 1];
		}
		
		// Else, pass it the next best choice
		Peer bestChoice = table[0];
		
		for (Peer p : table) {
			if ( (p.id > bestChoice.id) && (p.id < h)) {
				bestChoice = p;
			}
		}
		
		return bestChoice;
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
