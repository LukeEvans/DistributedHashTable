package cs555.dht.state;

import cs555.dht.node.PeerNode;
import cs555.dht.peer.Peer;
import cs555.dht.utilities.Constants;
import cs555.dht.wireformats.LookupRequest;

public class FingerTable {

	Peer table[];
	int id;
	PeerNode node;
	int size;

	//================================================================================
	// Constructor
	//================================================================================
	public FingerTable(int i, PeerNode p) {
		table = new Peer[Constants.Id_Space];
		id = i;
		node = p;
		size = 0;
	}

	//================================================================================
	// Build Finger Table
	//================================================================================
	public void buildFingerTable() {
		// Set size back to zero
		size = 0;

		for (int i=0; i<table.length; i++) {
			int resolve =  (id + (int) Math.pow(2, (i)));
			if (resolve > (int) Math.pow(2, Constants.Id_Space) -1 ) {
				resolve = resolve % ((int) Math.pow(2, Constants.Id_Space));
			}

			LookupRequest req = new LookupRequest(node.hostname, node.port, node.id, resolve, i);
			node.sendLookup(table[0], req);
		}
	}

	//================================================================================
	// Resolve
	//================================================================================
	public Peer getNextClosest(int h) {

		// If we know that our successor should hold the ID, send it there
		if ((h <= table[0].id) && (h > id)) {
			return table[0];
		}

		if (table[0].id < id) {
			if ((h > id) || (h < table[0].id)){
				return table[0];
			}
		}

		// Else, pass it the next best choice
		Peer bestChoice = table[0];

		for (Peer p : table) {
			if ( (p.id > bestChoice.id) && (p.id <= h)) {

				if (p.id != id){
					bestChoice = p;
				}
			}
		}

		return bestChoice;
	}

	//================================================================================
	// State manipulation
	//================================================================================
	// Add myself as every entry
	public void fillTableWith(Peer peer) {
		for (int i=0; i<table.length; i++) {
			table[i] = peer;
		}
	}

	public void addEntry(int location, Peer p) {
		table[location] = p;
		size++;

		if (size == Constants.Id_Space) {
			node.printDiagnostics();
		}
	}

	//================================================================================
	// House keeping
	//================================================================================
	public String toString() {
		String s = "";

		for (int i=0; i<table.length; i++) {
			Peer p = table[i];

			int resolving = ((id) + (int) Math.pow(2, i));
			if (resolving > Constants.Id_Space - 1) {
				resolving = resolving % (int) Math.pow(2, Constants.Id_Space);
			}

			s += i+1 + ": succ(" + Integer.toHexString(resolving) + ")	= " + Integer.toHexString(p.id) + " \n"; 
		}

		return s;
	}
}
