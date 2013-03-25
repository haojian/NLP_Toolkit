package data_structure;

import java.util.HashMap;
import java.util.Map;

public class RestaurantEntry {
	public Map<Integer, ReviewEntry> ReviewMap;
	public int StartSentIndex = -1;
	public int EndSentIndex = -1;
	
	public RestaurantEntry(){
		ReviewMap = new HashMap<Integer, ReviewEntry>();
	}
}
