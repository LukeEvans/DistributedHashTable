package cs555.dht.utilities;

// Constants for message types
public class Constants {

	// Message types
	public static final int Payload = 1;
	public static final int Verification = 2;
	public static final int Node_Results = 3;
	public static final int Node_Complete = 4;
	public static final int Election_Message = 5;
	public static final int Fetch_Request = 6;
	public static final int Fetch_Response = 7;
	public static final int Registration_Request = 8;
	public static final int Registration_Reply = 9;
	public static final int lookup_request = 10;
	public static final int lookup_reply = 11;
	public static final int store_request = 12;
	
	public static final int Failure = 99;
	public static final int Success = 100;
	
	// Message sizes
	public static final int LEN_BYTES = 3072;
	
	// ID space of DHT
	public static final int Id_Space = 16;
	
	// Base path for data to be stored
	public static final String base_path = "/tmp/evansl/dhtData/";
}
