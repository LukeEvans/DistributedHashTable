package cs555.dht.state;

import cs555.dht.peer.*;

public class State {
	Peer successor;
	Peer predecessor;
	String hash;
	
	FingerTable fingerTable;
	
	//================================================================================
	// Constructors
	//================================================================================
	public State(String h){
		successor = null;
		predecessor = null;
		hash = h;
		
		fingerTable = new FingerTable();
	}
	
	
	//================================================================================
	// State manipulation
	//================================================================================
	public void addSucessor(Peer p) {
		successor = p;
	}
	
	public void addPredecessor(Peer p) {
		predecessor = p;
	}
	
	//================================================================================
	// Update State
	//================================================================================
	public void update() {
		
	}
	
	//================================================================================
	// Resolving
	//================================================================================
	public boolean itemIsMine(String h) {
		return true;
	}
	
	// Get the next closest peer to this id from finger table
	public Peer getNexClosestPeer(String h) {
		return fingerTable.getNextClosest(h);
	}
	
	//================================================================================
	// House keeping
	//================================================================================
	public String toString(){
		String s = "";
		
		s += "Predesessor 	: " + predecessor.toString();
		s += "Sucessor		: " + successor.toString();
		s += "FT			: " + fingerTable.toString();
		
		return s;
	}
}
