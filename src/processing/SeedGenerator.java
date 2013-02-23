package processing;

import java.util.ArrayList;

import data_structure.Extraction;

public class SeedGenerator {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static SeedGenerator singleton;
	public static SeedGenerator getInstance(){
		if (singleton == null)
			singleton = new SeedGenerator();
		return singleton;
	} 
	
	public ArrayList<Extraction> seedsList;

	public SeedGenerator(){
		seedsList = new ArrayList<Extraction>();
	}
}
