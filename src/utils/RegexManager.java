package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegexManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private static RegexManager singleton;
	
	public static RegexManager getInstance() {
		if (singleton == null)
			singleton = new RegexManager();
		return singleton;
	}
	
	//use field directly for performance
	public Map<String, Pattern> patternList;
	
	public RegexManager(){
		patternList = new HashMap<String, Pattern>();
	}
}
