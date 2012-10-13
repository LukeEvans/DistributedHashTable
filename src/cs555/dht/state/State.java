package cs555.dht.state;

import cs555.dht.node.PeerNode;
import cs555.dht.peer.*;
import cs555.dht.utilities.Constants;
import cs555.dht.wireformats.LookupResponse;
import cs555.dht.wireformats.PredessesorRequest;

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
		
		fingerTable = new FingerTable(thisID,myself);
	}
	
	
	//================================================================================
	// State manipulation
	//================================================================================
	// Check successor candidate
	public boolean shouldAddNewSuccessor(Peer p) {
		if ((successor.id == thisID) || (successor == null)) {
			return true;
		}
		
		// Same side of ring
		if ((p.id < successor.id) && (p.id > thisID)) {
			return true;
		}
		
		// left side of gap
		if ((p.id > thisID) && (p.id < (Math.pow(2, Constants.Id_Space)))) {
			return true;
		}
		
		// Right side of gap
		if ((p.id >= 0) && (p.id < successor.id)) {
			return true;
		}
		
		return false;
	}
	
	public void addSucessor(Peer p) {
		if (!shouldAddNewSuccessor(p)) {
			System.out.println("New Successor is not closer than old : " + p.id);
			return;
		}
		successor = p;
		
		// Tell our new successor that we're it's predecessor
		PredessesorRequest req = new PredessesorRequest(myself.hostname, myself.port, myself.id);
		myself.sendPredessessorRequest(successor, req);
		
		// Our successor changed, update finger table
		fingerTable.addEntry(0, successor);
		fingerTable.buildFingerTable();
		
	}
	
	// check predeccessor candidate
	public boolean shouldAddNewPredecessor(Peer p) {
		if ((predecessor.id == thisID) || (predecessor == null)) {
			return true;
		}
		
		if (itemIsMine(p.id)) {
			return true;
		}
		
		return false;
	}
	
	public void addPredecessor(Peer p) {
		
		if (!shouldAddNewPredecessor(p)) {
			System.out.println("New Predessor is not closer than old : " + p.id);
			return;
		}
		
		predecessor = p;
		
		// If our successor is ourself, and p as our successor as well
		if (successor.id == thisID) {
			addSucessor(p);
		}
	}
	
	// Set all values to self
	public void firstToArrive() {
		Peer thisPeer = new Peer(myself.hostname, myself.port, myself.id);
		addPredecessor(thisPeer);
		addSucessor(thisPeer);
		
		// Add thisPeer as all values in FT
		fingerTable.firstToArrive(thisPeer);
	}
	
	//================================================================================
	// Update State
	//================================================================================
	public void update() {
		
	}
	
	// Decide where to put this peer in Finger Table
	public void parseState(LookupResponse reply) {
		Peer peer = new Peer(reply.hostName, reply.port, reply.id);
		
		// If it's our first entry getting back to us, add it as our sucessor
		if (reply.ftEntry == 0) {
			addSucessor(peer);
		}
		
		fingerTable.addEntry(reply.ftEntry, peer);
		
	}
	
	//================================================================================
	// Resolving
	//================================================================================
	public boolean itemIsMine(int h) {
		// Same side of ring
		if ((h > predecessor.id) && h <= thisID) {
			return true;
		}
		
		// left side of gap
		if ((h > predecessor.id) && (h < (Math.pow(2, Constants.Id_Space)))) {
			return true;
		}
		
		// right side of gap
		if ((h >= 0) && (h <= thisID)) {
			return true;
		}
		
		return false;
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
