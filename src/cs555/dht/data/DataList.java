package cs555.dht.data;

import java.util.ArrayList;

import cs555.dht.utilities.Constants;
import cs555.dht.utilities.Tools;

public class DataList {

	ArrayList<DataItem> dataList;

	//================================================================================
	// Constructor
	//================================================================================
	public DataList() {
		dataList = new ArrayList<DataItem>();
	}


	//================================================================================
	// Data list manipulation
	//================================================================================
	public void addData(DataItem d) {
		if (!contains(d)) {
			dataList.add(d);
		}
	}

	public void removeData(DataItem d) {
		for (int i=0; i<dataList.size(); i++) {
			if (dataList.get(i).equals(d)) {
				Tools.removeFile(Constants.base_path + dataList.get(i).filename);
				dataList.remove(i);
			}
		}
	}
	
	//================================================================================
	// Accessor methods
	//================================================================================
	// Returns a list of data items >= to id
	public ArrayList<DataItem> subsetToMove(int id) {
		return null;
	}
	
	public ArrayList<DataItem> getAllData() {
		return dataList;
	}
	
	//================================================================================
	// House Keeping
	//================================================================================
	public String toString() {
		String s = "";
		
		s += "Data: \n";
		
		for (DataItem d : dataList){
			s += d.toString();
		}

		return s;
	}

	// Override contains method
	public boolean contains(DataItem d) {
		for (DataItem item : dataList) {
			if (item.equals(d)) {
				return true;
			}
		}

		return false;
	}
}
