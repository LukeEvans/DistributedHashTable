package cs555.dht.wireformats;

import cs555.dht.utilities.Constants;

public class RegisterResponse extends RegisterRequest{

	//================================================================================
	// Overridden Constructors
	//================================================================================
	public RegisterResponse(String h, int p, String n){
		super.init(h, p, n);
		type = Constants.Registration_Reply;
		
	}
	
	
	public RegisterResponse(){
		super.init("", 0, "");
		type = Constants.Registration_Reply;
	}
}
