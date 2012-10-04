package cs555.dht.node;

import cs555.dht.communications.Link;
import cs555.dht.peer.PeerList;
import cs555.dht.utilities.Constants;
import cs555.dht.utilities.Tools;
import cs555.dht.wireformats.ElectionMessage;
import cs555.dht.wireformats.FetchResponse;

public class DiscoveryNode extends Node{
	
	PeerList peerList;
	
	String linkFile;
	String slaveFile;
	int maxDepth;
	
	//================================================================================
	// Constructor
	//================================================================================
	public DiscoveryNode(PeerList list, int port,String lf, String sf, int md){
		super(port);
		
		peerList = list;
		linkFile = lf;
		slaveFile = sf;
		maxDepth = md;
		
	}
	
	
	//================================================================================
	// Send
	//================================================================================
	public void broadcastElection(){
		ElectionMessage electionMsg = new ElectionMessage(serverPort, Tools.getLocalHostname());
		broadcastMessage(peerList.getAllPeers(), electionMsg.marshall(),Constants.Election_Message);
	}

	
	//================================================================================
	// Receive
	//================================================================================
	// Receieve data
	public synchronized void receive(byte[] bytes, Link l){
		int messageType = Tools.getMessageType(bytes);
		
		switch (messageType) {
		case Constants.Fetch_Response:
			
			FetchResponse response = new FetchResponse();
			response.unmarshall(bytes);
			
			System.out.println("Got: " + response);
			
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
		int maxDepth = 5;
		String linkFile = "";
		String slaveFile = "";

		if (args.length == 3) {
			port = Integer.parseInt(args[0]);
			linkFile = args[1];
			slaveFile = args[2];
		}

		else if (args.length == 4){
			port = Integer.parseInt(args[0]);
			linkFile = args[1];
			slaveFile = args[2];
			maxDepth = Integer.parseInt(args[3]);
		}

		else {
			System.out.println("Usage: java node.NodeManager PORT LINK-FILE SLAVE-FILE <DEPTH>");
			System.exit(1);
		}

		// Create peer list
		PeerList peerList = new PeerList(slaveFile, port);

		// Create node
		DiscoveryNode manager = new  DiscoveryNode(peerList, port, linkFile, slaveFile, maxDepth);
		manager.initServer();
		
	}
}
