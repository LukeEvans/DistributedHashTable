package cs555.dht.node;

import cs555.dht.communications.Link;
import cs555.dht.peer.PeerList;
import cs555.dht.utilities.Constants;
import cs555.dht.utilities.Tools;

public class DiscoveryNode extends Node{
	
	PeerList peerList;
	int maxDepth;
	
	//================================================================================
	// Constructor
	//================================================================================
	public DiscoveryNode(PeerList list, int port){
		super(port);
		
		peerList = list;
	}
	
	
	//================================================================================
	// Send
	//================================================================================
	

	
	//================================================================================
	// Receive
	//================================================================================
	// Receieve data
	public synchronized void receive(byte[] bytes, Link l){
		int messageType = Tools.getMessageType(bytes);
		
		switch (messageType) {
		
		case Constants.Registration_Request:

			// If peer's id collides, return a failure message
			
			// Get a random peer out of the peer list
			
			// Send peer back through link
			
			break;

		default:
			
			System.out.println("Unrecognized Message");
			break;
		}
	}
	
	
	//================================================================================
	//================================================================================
	// Main
	//================================================================================
	//================================================================================
	public static void main(String[] args){

		int port = 0;
		
		if (args.length == 1) {
			port = Integer.parseInt(args[0]);
			
		}
		
		else {
			System.out.println("Usage: java cs555.dht.node.Discovery PORT");
			System.exit(1);
		}

		// Create peer list
		PeerList peerList = new PeerList();

		// Create node
		DiscoveryNode manager = new  DiscoveryNode(peerList, port);
		manager.initServer();
		
	}
}
