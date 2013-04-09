package utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import data_structure.Template;

public class SortingHashMap {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public SortingHashMap(){
		updatemap = new HashMap<String, Integer>();
		HashMapValueComparator sortingcomp = new HashMapValueComparator(updatemap);
		sortedmap = new TreeMap<String, Integer>(sortingcomp);
		lastQuerySize = -1;
	}
	private Map<String, Integer> updatemap;
	private Map<String, Integer> sortedmap;
	private int lastQuerySize;

	public void updateMap(String key, int value){
		if(updatemap.containsKey(key))
			updatemap.put(key, updatemap.get(key)+1);
		else
			updatemap.put(key, value);
	}
	
	
	public Map<String, Integer> getSortedMap(){
		if(sortedmap.size() == lastQuerySize && sortedmap.size() != 0){
			return sortedmap;
		}
		sortedmap.clear();
		sortedmap.putAll(updatemap);
		lastQuerySize = sortedmap.size();
		return sortedmap;
	}
}

class HashMapValueComparator implements Comparator<Object> {
    Map theMapToSort;
    public HashMapValueComparator(Map theMapToSort) {
        this.theMapToSort = theMapToSort;
    }

    @Override
	public int compare(Object key1, Object key2) {
    	Integer val1 = (Integer)theMapToSort.get(key1);
    	Integer val2 = (Integer) theMapToSort.get(key2);
    	
        if (val1 < val2) {
            return 1;
        } else {
            return -1;
        }
    }
}
