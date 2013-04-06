package data_structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import utils.SortingHashMap;

public class RestaurantEntry {
	public Map<Integer, ReviewEntry> ReviewMap;
	public int StartSentIndex = -1;
	public int EndSentIndex = -1;
	
	public RestaurantEntry(){
		ReviewMap = new HashMap<Integer, ReviewEntry>();
		phraseMap = new SortingHashMap();
	}
	
	public SortingHashMap phraseMap;
	
	public ArrayList<String> getTopNouns(int n){
		for(Map.Entry<Integer, ReviewEntry> entry: ReviewMap.entrySet()){
			
		}
		return null;
	}
}
