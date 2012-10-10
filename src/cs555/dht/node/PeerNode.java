package cs555.dht.node;

import cs555.dht.communications.Link;
import cs555.dht.peer.Peer;
import cs555.dht.state.RefreshThread;
import cs555.dht.state.State;
import cs555.dht.utilities.*;
import cs555.dht.wireformats.LookupRequest;
import cs555.dht.wireformats.LookupResponse;
import cs555.dht.wireformats.RegisterRequest;
import cs555.dht.wireformats.RegisterResponse;

public class PeerNode extends Node{

	int port;
	Link managerLink;
	int refreshTime;
	String nickname;
	String hostname;

	State state;
	
	RefreshThread refreshThread;

	//================================================================================
	// Constructor
	//================================================================================
	public PeerNode(int p, String n, int r){
		super(p);

		port = p;
		nickname = n;
		refreshTime = r;

		if (nickname.equalsIgnoreCase("")) {
			nickname = Tools.generateHash();
		}

		managerLink = null;

		state = new State(nickname);
		hostname = Tools.getLocalHostname();
		
		refreshThread = new RefreshThread(this, refreshTime);
	}

	//================================================================================
	// Init
	//================================================================================
	public void initServer(){
		// Start server listening on specified port
		super.initServer();

		// Start thread for refreshing hash
		refreshThread.start();
	}

	public void enterDHT(String dHost, int dPort) {
		managerLink = connect(new Peer(dHost, dPort));
		RegisterRequest regiserReq = new RegisterRequest(hostname, port, nickname);
		managerLink.sendData(regiserReq.marshall());

		// Keep sending until we are able to enter
		while (managerLink.waitForIntReply() == Constants.Failure) {
			nickname = Tools.generateHash();
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
			LookupRequest lookupReq = new LookupRequest(hostname, port, nickname, nickname);
			Peer poc = new Peer(accessPoint.hostName, accessPoint.port, accessPoint.nickName);
			Link accessLink = connect(poc);
			accessLink.sendData(lookupReq.marshall());
		}
	}

	//================================================================================
	// DHT maintence
	//================================================================================
	public void updateFT() {
		// Ensure accuracy of Finger Table

	}

	//================================================================================
	// Receive
	//================================================================================
	// Receieve data
	public synchronized void receive(byte[] bytes, Link l){
		int messageType = Tools.getMessageType(bytes);

		switch (messageType) {
		case Constants.lookup_request:

			LookupRequest lookup = new LookupRequest();
			lookup.unmarshall(bytes);
			
			System.out.println("Recieved Request : " + lookup);
			
			// Info about the lookup
			String resolveString = lookup.resolveString;
			String requesterHost = lookup.hostName;
			int requesterPort = lookup.port;
			String requesterHash = lookup.nickName;
			
			// If we are the target, handle it
			if (state.itemIsMine(resolveString)) {
				LookupResponse response = new LookupResponse(hostname, port, nickname, resolveString);
				Peer requester = new Peer(requesterHost, requesterPort, requesterHash);
				Link requesterLink = connect(requester);
				
				requesterLink.sendData(response.marshall());
			}
			
			// Else, pass it along
			else {
				Link nextHop = connect(state.getNexClosestPeer(resolveString));
				lookup.hopCount++;
				nextHop.sendData(lookup.marshall());
			}

			break;

		case Constants.lookup_reply:

			LookupResponse reply = new LookupResponse();
			reply.unmarshall(bytes);
			
			System.out.println("Received Reply : " + reply);
			
			// Set the replier to our new successor
			
			// Tell the process who replied that we are now they're new predecessor
			
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

		if (args.length >= 3) {
			discoveryHost = args[0];
			discoveryPort = Integer.parseInt(args[1]);
			localPort = Integer.parseInt(args[2]);

			if (args.length >= 4) {
				nickname = args[3];

				if (args.length >= 5) {
					refreshTime = Integer.parseInt(args[4]);

				}
			}
		}

		else {
			System.out.println("Usage: java cs555.dht.node.PeerNode DISCOVERY-NODE DISCOVERY-PORT LOCAL-PORT <HASH> <REFRESH-TIME>");
			System.exit(1);
		}

		// Create node
		PeerNode peer = new PeerNode(localPort, nickname, refreshTime);

		// Enter DHT
		peer.enterDHT(discoveryHost, discoveryPort);
		peer.initServer();

	}
}
