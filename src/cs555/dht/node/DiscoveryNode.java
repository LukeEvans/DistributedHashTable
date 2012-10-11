package cs555.dht.node;

import cs555.dht.communications.Link;
import cs555.dht.peer.Peer;
import cs555.dht.peer.PeerList;
import cs555.dht.utilities.Constants;
import cs555.dht.utilities.Tools;
import cs555.dht.wireformats.Payload;
import cs555.dht.wireformats.RegisterRequest;
import cs555.dht.wireformats.RegisterResponse;
import cs555.dht.wireformats.Verification;

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

			RegisterRequest rreq = new RegisterRequest();
			rreq.unmarshall(bytes);
	
			// If peer's id collides, return a failure message
			if (peerList.hashUnique(rreq.nickName)) {
				Verification failure = new Verification(Constants.Failure);
				l.sendData(failure.marshall());
				break;
			}
			
			// Return a random peer
			Peer returnPeer = peerList.getNextPeer();
			
			// If return peer is null, the requesting node is the first to join. Return null
			if (returnPeer == null) {
				Payload nullPeer = new Payload(Constants.Null_Peer);
				l.sendData(nullPeer.marshall());
				break;
			}
			
			// Else, we have a valid peer to return
			RegisterResponse rresp = new RegisterResponse(returnPeer.hostname, returnPeer.port, returnPeer.nickname);
			l.sendData(rresp.marshall());
			
			// Add peer to list
			Peer addPeer = new Peer(rreq.hostName, rreq.port, rreq.nickName);
			peerList.addPeer(addPeer);
			
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
			System.out.println("Usage: java cs555.dht.node.DiscoveryNode PORT");
			System.exit(1);
		}

		// Create peer list
		PeerList peerList = new PeerList();

		// Create node
		DiscoveryNode manager = new  DiscoveryNode(peerList, port);
		manager.initServer();
		
	}
}
