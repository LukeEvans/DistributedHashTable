package cs555.dht.data;

import cs555.dht.utilities.Constants;
import cs555.dht.utilities.Tools;

public class DataItem {

	public String filename;
	public String filehash;
	public String basepath;
	
	//================================================================================
	// Constructor
	//================================================================================
	public DataItem(String fname, String fhash) {
		filename = fname;
		filehash = fhash;
		basepath = Constants.base_path;
		
		if (filehash.equalsIgnoreCase("")) {
			filehash = Tools.generateHash(filename);
		}
	}
	
	
	//================================================================================
	// House keeping
	//================================================================================
	public String toString() {
		String s = "";
		
		s += basepath + filename + " : " + filehash;
		
		return s;
	}
	
	// Override equals method
	public boolean equals(DataItem other) {
		if (this.filename.equalsIgnoreCase(other.filename)) {
			if (this.filehash.equalsIgnoreCase(other.filehash)) {
				if (this.basepath.equals(other.basepath)) {
					return true;
				}
			}
		}
		
		return false;
	}
}
