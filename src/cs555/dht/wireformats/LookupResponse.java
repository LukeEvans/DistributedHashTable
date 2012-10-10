package cs555.dht.wireformats;

import cs555.dht.utilities.Constants;

public class LookupResponse extends LookupRequest{

	//================================================================================
	// Overridden Constructors
	//================================================================================
	public LookupResponse(String h, int p, String n, String r){
		super.init(h, p, n, r);
		type = Constants.lookup_reply;
		
	}
	
	
	public LookupResponse(){
		super.init("", 0, "","");
		type = Constants.lookup_reply;
	}
	
	//================================================================================
	// Verification
	//================================================================================
	public boolean responseResolves(String n) {
		return n.equalsIgnoreCase(resolveString);
	}
}
