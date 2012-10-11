package cs555.dht.state;

import cs555.dht.node.PeerNode;
import cs555.dht.peer.*;

public class State {
	Peer successor;
	Peer predecessor;
	int thisID;
	PeerNode myself;
	
	FingerTable fingerTable;
	
	//================================================================================
	// Constructors
	//================================================================================
	public State(int h, PeerNode p){
		successor = null;
		predecessor = null;
		thisID = h;
		myself = p;
		
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
	
	// Set all values to self
	public void firstToArrive() {
		Peer thisPeer = new Peer(myself.hostname, myself.port, myself.id);
		addPredecessor(thisPeer);
		addSucessor(thisPeer);
		
		// Add thisPeer as all values in FT
		
	}
	
	//================================================================================
	// Update State
	//================================================================================
	public void update() {
		
	}
	
	//================================================================================
	// Resolving
	//================================================================================
	public boolean itemIsMine(int h) {
		return true;
	}
	
	// Get the next closest peer to this id from finger table
	public Peer getNexClosestPeer(int h) {
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
