package cs555.dht.node;

import cs555.dht.communications.Link;
import cs555.dht.utilities.Constants;
import cs555.dht.utilities.Tools;
import cs555.dht.wireformats.ElectionMessage;
import cs555.dht.wireformats.FetchRequest;
import cs555.dht.wireformats.Verification;
import cs555.dht.peer.Peer;

public class PeerNode extends Node{

	Peer nodeManager;
	Link managerLink;

	//================================================================================
	// Constructor
	//================================================================================
	public PeerNode(int port,int threads){
		super(port);
		nodeManager = null;
		managerLink = null;
	}


	public void initServer(){
		super.initServer();
	}
	
	//================================================================================
	// Receive
	//================================================================================
	// Receieve data
	public synchronized void receive(byte[] bytes, Link l){
		int messageType = Tools.getMessageType(bytes);

		switch (messageType) {
		case Constants.Election_Message:
			ElectionMessage election = new ElectionMessage();
			election.unmarshall(bytes);
			
			Verification electionReply = new Verification(election.type);
			l.sendData(electionReply.marshall());
			
			nodeManager = new Peer(election.host, election.port);
			managerLink = connect(nodeManager);
			
			System.out.println("Elected Official: " + election);
			
			break;

		case Constants.Fetch_Request:
			FetchRequest request = new FetchRequest();
			request.unmarshall(bytes);
			
			System.out.println("Got: \n" + request);
			
			
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
		int threads = 5;
		
		if (args.length == 1) {
			port = Integer.parseInt(args[0]);
		}

		else if (args.length == 2){
			port = Integer.parseInt(args[0]);
			threads = Integer.parseInt(args[1]);
		}

		else {
			System.out.println("Usage: java node.Worker PORT <THREADS>");
			System.exit(1);
		}


		// Create node
		PeerNode peer = new PeerNode(port,threads);
		peer.initServer();

	}
}
