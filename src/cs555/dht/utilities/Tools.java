package cs555.dht.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

// Functions used all over the program. Handy location
public class Tools {

	// ================================================================================
	// Message functions
	// ================================================================================
	// Get Message type
	public static int getMessageType(byte[] bytes) {
		byte[] copy = bytes.clone();
		ByteBuffer bbuff = ByteBuffer.wrap(copy);

		// Size
		bbuff.getInt();

		// Return type
		return bbuff.getInt();
	}

	// Generate random number
	public static int generateRandomNumber() {

		int Min = 0;
		int Max = 65535;

		int random = Min + (int) (Math.random() * ((Max - Min) + 1));

		random -= 32768;

		return random;
	}


	//================================================================================
	// Link Functions 
	//================================================================================
	// Create input stream
	public static InputStream createInput(Socket s){
		InputStream sin;

		try {
			sin = s.getInputStream();
			return sin;
		} catch (IOException e){
			printStackTrace(e);
			return null;
		}
	}

	// Create output stream
	public static OutputStream createOutputStream(Socket s){
		OutputStream sout;

		try {
			sout = s.getOutputStream();
			return sout;
		} catch (IOException e){
			printStackTrace(e);
			return null;
		}
	}


	// ================================================================================
	// Host Functions
	// ================================================================================
	public static String getLocalHostname() {
		return getHostname("localhost");
	}

	public static String getHostname(String hname) {
		if (hname.equalsIgnoreCase("localhost") || hname.equalsIgnoreCase("127.0.0.1")){

			try {
				String host = Inet4Address.getLocalHost().getHostName();

				return host;

			} catch (UnknownHostException e) {
				printStackTrace(e);
			}
		}

		return hname;
	}

	// Get hostname without domain
	public static String getShortHostname(String hname){
		String host = getHostname(hname);
		String[] hostParts = host.split("\\.");

		if (hostParts.length > 1){
			return hostParts[0];
		}

		return null;
	}

	public static void sleep(int time){
		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {
			printStackTrace(e);
		}
	}
	//================================================================================
	// Byte Manipulations
	//================================================================================
	// convert string to byte array
	public static byte[] convertToBytes(String s){
		return s.getBytes();
	}

	// Convert Int to byte array
	public static byte[] convertToBytes(int i){
		return convertToBytes(Integer.toString(i));
	}

	//================================================================================
	// Hash functions
	//================================================================================

	public static String convertBytesToHex(byte[] buf) {
		StringBuffer strBuf = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			int byteValue = (int) buf[i] & 0xff;
			if (byteValue <= 15) {
				strBuf.append("0");
			}
			strBuf.append(Integer.toString(byteValue, 16));
		}
		return strBuf.toString();
	}

	public static byte[] convertHexToBytes(String hexString) {
		int size = hexString.length();
		byte[] buf = new byte[size / 2];
		int j = 0;
		for (int i = 0; i < size; i++) {
			String a = hexString.substring(i, i + 2);
			int valA = Integer.parseInt(a, 16);
			i++;
			buf[j] = (byte) valA;
			j++;
		}
		return buf;
	}

	// Generates hash
	public static String generateHash() {
		return "AAAAA";
	}
	
	// Generate hash of file
	public static String generateHash(String fname) {
		//int space = Constants.Id_Space;
		return "BBBBBB";
	}
	
	//================================================================================
	// Error Handling
	//================================================================================
	public static void printStackTrace(Exception e){
		e.printStackTrace();
		System.exit(1);

	}
}
