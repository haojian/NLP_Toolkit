package processing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import utils.ParameterSetting;

import data_structure.Extraction;

public class SeedGenerator {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private Map<Extraction, Integer> extraction_dict;
	public static SeedGenerator singleton;
	public static SeedGenerator getInstance(){
		if (singleton == null)
			singleton = new SeedGenerator();
		return singleton;
	} 
	
	public ArrayList<Extraction> seedsList;

	public SeedGenerator(){
		seedsList = new ArrayList<Extraction>();
		extraction_dict = new HashMap<Extraction, Integer>();
		File dir = new File(ParameterSetting.PATHTOPOSTAGGEDDATA);
		for(File tmp : dir.listFiles()){
			LoadRevTxtProccessedData(tmp);
			return;
		}
	}
	
	private void LoadRevTxtProccessedData(File input){
		
	}
}
