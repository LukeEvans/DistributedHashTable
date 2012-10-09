package cs555.dht.node;

import cs555.dht.communications.Link;
import cs555.dht.peer.Peer;
import cs555.dht.state.State;
import cs555.dht.utilities.*;
import cs555.dht.wireformats.RegisterRequest;
import cs555.dht.wireformats.RegisterResponse;

public class PeerNode extends Node{

	int port;
	int hashSpace;
	Link managerLink;
	Link accessPoint;
	int refreshTime;
	String nickname;
	String hostname;
	
	State state;

	//================================================================================
	// Constructor
	//================================================================================
	public PeerNode(int p, String n, int r, int s){
		super(p);

		port = p;
		nickname = n;
		refreshTime = r;

		if (nickname.equalsIgnoreCase("")) {
			nickname = Tools.generateHash(s);
		}

		managerLink = null;
		accessPoint = null;
		
		state = new State(s);
		hashSpace = s;
		hostname = Tools.getLocalHostname();
	}

	//================================================================================
	// Init
	//================================================================================
	public void initServer(){
		super.initServer();

		// Start thread for refreshing hash
		// refreshThread(r, this);
		// refreshThread.start();
	}

	public void enterDHT(String dHost, int dPort) {
		managerLink = connect(new Peer(dHost, dPort));
		RegisterRequest regiserReq = new RegisterRequest(hostname, port, nickname);
		managerLink.sendData(regiserReq.marshall());
		
		// Keep sending until we are able to enter
		while (managerLink.waitForIntReply() == Constants.Failure) {
			nickname = Tools.generateHash(hashSpace);
			regiserReq = new RegisterRequest(hostname, port, nickname);
		}
		
		RegisterResponse accessPoint = new RegisterResponse();
		accessPoint.unmarshall(managerLink.waitForData());
		
		// If we hear back from Discovery node with our info, we are the first to arrive
		if (regiserReq.equals(accessPoint)) {
			Peer self = new Peer(hostname, port, nickname);
			state.addPredecessor(self);
			state.addSucessor(self);
		}
		
		// Otherwise send lookup request to our access point
		else {
			
		}
	}
	
	//================================================================================
	// Receive
	//================================================================================
	// Receieve data
	public synchronized void receive(byte[] bytes, Link l){
		int messageType = Tools.getMessageType(bytes);

		switch (messageType) {
		case Constants.lookup_request:

			System.out.println("Lookup Request");

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

		String discoveryHost = "";
		int discoveryPort = 0;
		int localPort = 0;
		String nickname = "";
		int refreshTime = 30;
		int idSpace = 16;

		if (args.length >= 3) {
			discoveryHost = args[0];
			discoveryPort = Integer.parseInt(args[1]);
			localPort = Integer.parseInt(args[2]);

			if (args.length >= 4) {
				nickname = args[3];

				if (args.length >= 5) {
					refreshTime = Integer.parseInt(args[4]);
					
					if (args.length >= 6) {
						idSpace = Integer.parseInt(args[5]);
					}
				}
			}
		}

		else {
			System.out.println("Usage: java cs555.dht.node.PeerNode DISCOVERY-NODE DISCOVERY-PORT LOCAL-PORT <HASH> <REFRESH-TIME> <ID-SPACE>");
			System.exit(1);
		}


		// Create node
		PeerNode peer = new PeerNode(localPort, nickname, refreshTime, idSpace);
		
		// Enter DHT
		peer.enterDHT(discoveryHost, discoveryPort);
		peer.initServer();

	}
}
