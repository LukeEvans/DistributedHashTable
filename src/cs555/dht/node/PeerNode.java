package cs555.dht.node;

import cs555.dht.communications.Link;
import cs555.dht.utilities.*;
import cs555.dht.wireformats.*;

public class PeerNode extends Node{

	Link managerLink;
	Link accessPoint;
	int refreshTime;
	String nickname;

	//================================================================================
	// Constructor
	//================================================================================
	public PeerNode(int port, String n, int r){
		super(port);

		nickname = n;
		refreshTime = r;

		if (nickname.equalsIgnoreCase("")) {
			nickname = Tools.generateHash();
		}

		managerLink = null;
		accessPoint = null;
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
		peer.initServer();

	}
}
