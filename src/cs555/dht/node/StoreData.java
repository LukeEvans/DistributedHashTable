package cs555.dht.node;

import cs555.dht.communications.Link;
import cs555.dht.peer.Peer;
import cs555.dht.utilities.Constants;
import cs555.dht.utilities.Tools;
import cs555.dht.wireformats.*;

public class StoreData extends Node {

	String hostName;
	int port;
	String filename;
	String filehash;
	int idSpace;

	public StoreData(int p, String fname, String fhash, int s) {
		super(p);
		hostName = Tools.getLocalHostname();
		port = p;
		filename = fname;
		filehash = fhash;
		idSpace = s;

		if (filehash.equalsIgnoreCase("")) {
			filehash = Tools.generateHash(filename);
		}
	}

	//================================================================================
	// Init
	//================================================================================
	public void initServer(){
		super.initServer();
	}

	public void initLookup(String dHost, int dPort) {
		Link managerLink = connect(new Peer(dHost, dPort));
		LookupRequest req = new LookupRequest(hostName, port, filename, filehash);
		managerLink.sendData(req.marshall());
	}

	//================================================================================
	// Receive
	//================================================================================
	// Receieve data
	public synchronized void receive(byte[] bytes, Link l){
		int messageType = Tools.getMessageType(bytes);

		switch (messageType) {
		case Constants.lookup_reply:

			System.out.println("Lookup Reply");
			LookupResponse response = new LookupResponse();
			response.unmarshall(bytes);
			
			Peer candidate = new Peer(response.hostName, response.port);
			Link candidateLink = connect(candidate);
			
			Payload sendReq = new Payload(Constants.store_request);
			candidateLink.sendData(sendReq.marshall());
			
			if (candidateLink.waitForIntReply() == Constants.Success) {
				// Send data item to candidate
			}
			
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
		String fileName = "";
		String fileHash = "";
		int idSpace = 16;

		if (args.length >= 4) {
			discoveryHost = args[0];
			discoveryPort = Integer.parseInt(args[1]);
			localPort = Integer.parseInt(args[2]);
			fileName = args[3];

			if (args.length >= 5) {
				fileHash = args[4];

				if (args.length >= 6) {
					idSpace = Integer.parseInt(args[5]);
				}
			}

		}

		else {
			System.out.println("Usage: java cs555.dht.node.StoreData DISCOVERY-NODE DISCOVERY-PORT LOCAL-PORT FILE-NAME <CUSTOM-HASH> <ID-Space>");
			System.exit(1);
		}


		// Create node
		StoreData storeHandler = new StoreData(localPort, fileName, fileHash, idSpace);

		// Start
		storeHandler.initServer();
		storeHandler.initLookup(discoveryHost, discoveryPort);
	}
}
