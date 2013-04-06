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
		map = new HashMap<String, Integer>();
	}
	private Map<String, Integer> map;

	public TreeMap sort(){
		HashMapValueComparator sortingcomp = new HashMapValueComparator(map);
		TreeMap sortingMap = new TreeMap(sortingcomp);
		sortingMap.putAll(map);
		return sortingMap;
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
