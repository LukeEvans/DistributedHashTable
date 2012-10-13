package cs555.dht.node;

import cs555.dht.communications.Link;
import cs555.dht.peer.Peer;
import cs555.dht.state.RefreshThread;
import cs555.dht.state.State;
import cs555.dht.utilities.*;
import cs555.dht.wireformats.LookupRequest;
import cs555.dht.wireformats.LookupResponse;
import cs555.dht.wireformats.Payload;
import cs555.dht.wireformats.PredessesorRequest;
import cs555.dht.wireformats.RegisterRequest;
import cs555.dht.wireformats.RegisterResponse;

public class PeerNode extends Node{

	
	Link managerLink;
	int refreshTime;
	String nickname;
	
	public String hostname;
	public int port;
	public int id;

	State state;

	RefreshThread refreshThread;

	//================================================================================
	// Constructor
	//================================================================================
	public PeerNode(int p, int i, int r){
		super(p);

		port = p;
		id = i;
		refreshTime = r;

		if (id == -1) {
			id = Tools.generateHash();
		}

		managerLink = null;

		state = new State(id, this);
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
		RegisterRequest regiserReq = new RegisterRequest(hostname, port, id);
		managerLink.sendData(regiserReq.marshall());

		// Keep sending until we are able to enter
		while (managerLink.waitForIntReply() == Constants.Failure) {
			id = Tools.generateHash();
			regiserReq = new RegisterRequest(hostname, port, id);
			managerLink.sendData(regiserReq.marshall());
		}

		// Wait for data from Discovery
		byte[] randomNodeData = managerLink.waitForData();
		int messageType = Tools.getMessageType(randomNodeData);

		switch (messageType) {
		case Constants.Registration_Reply: 

			RegisterResponse accessPoint = new RegisterResponse();
			accessPoint.unmarshall(randomNodeData);

			LookupRequest lookupReq = new LookupRequest(hostname, port, id, id, 0);
			Peer poc = new Peer(accessPoint.hostName, accessPoint.port, accessPoint.id);
			Link accessLink = connect(poc);
			accessLink.sendData(lookupReq.marshall());
			
			break;

		case Constants.Payload:
			Payload response = new Payload();
			response.unmarshall(randomNodeData);

			// If we heard back that we're the first node, modify state accordingly
			if (response.number == Constants.Null_Peer) {				
				// Add ourselves as all entries in FT
				state.firstToArrive();
			}

			break;

		default:
			System.out.println("Could not get access point from Discovery");
			break;
		}	
		
	}

	//================================================================================
	// DHT maintence
	//================================================================================
	public void updateFT() {
		// Ensure accuracy of Finger Table

	}

	//================================================================================
	// Send
	//================================================================================
	public void sendLookup(Peer p, LookupRequest l) {
		Link lookupPeer = connect(p);
		lookupPeer.sendData(l.marshall());
	}
	
	public void sendPredessessorRequest(Peer p, PredessesorRequest r) {
		Link sucessorLink = connect(p);
		sucessorLink.sendData(r.marshall());
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
			int resolveID = lookup.resolveID;
			String requesterHost = lookup.hostName;
			int requesterPort = lookup.port;
			int requesterID = lookup.id;
			int entry = lookup.ftEntry;

			// If we are the target, handle it
			if (state.itemIsMine(resolveID)) {
				LookupResponse response = new LookupResponse(hostname, port, id, resolveID, entry);
				Peer requester = new Peer(requesterHost, requesterPort, requesterID);
				Link requesterLink = connect(requester);

				requesterLink.sendData(response.marshall());
			}

			// Else, pass it along
			else {
				Link nextHop = connect(state.getNexClosestPeer(resolveID));
				lookup.hopCount++;
				nextHop.sendData(lookup.marshall());
			}

			break;

		case Constants.lookup_reply:

			LookupResponse reply = new LookupResponse();
			reply.unmarshall(bytes);

			System.out.println("Received Reply : " + reply);

			// Heard back for FingerTable entry, update state
			state.parseState(reply);
			
			break;

		case Constants.Predesessor_Request:
			
			PredessesorRequest predReq = new PredessesorRequest();
			predReq.unmarshall(bytes);
			
			System.out.println("Recieved predessor request : " + predReq);
			
			// Add this node as our predessesor
			Peer pred = new Peer(predReq.hostName, predReq.port, predReq.id);
			state.addPredecessor(pred);
			
			break;
			
		default:
			System.out.println("Unrecognized Message");
			break;
		}
	}


	//================================================================================
	// Diagnostics
	//================================================================================
	public void printDiagnostics() {
		System.out.println(state);
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
		int id = -1;
		int refreshTime = 30;

		if (args.length >= 3) {
			discoveryHost = args[0];
			discoveryPort = Integer.parseInt(args[1]);
			localPort = Integer.parseInt(args[2]);

			if (args.length >= 4) {
				id = Integer.parseInt(args[3]);

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
		PeerNode peer = new PeerNode(localPort, id, refreshTime);

		// Enter DHT
		peer.enterDHT(discoveryHost, discoveryPort);
		peer.initServer();

	}
}
