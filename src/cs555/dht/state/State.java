package cs555.dht.state;

import cs555.dht.peer.*;

public class State {
	Peer successor;
	Peer predecessor;
	int idSpace;
	
	FingerTable fingerTable;
	
	//================================================================================
	// Constructors
	//================================================================================
	public State(int s){
		successor = null;
		predecessor = null;
		idSpace = s;
		
		fingerTable = new FingerTable(idSpace);
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
