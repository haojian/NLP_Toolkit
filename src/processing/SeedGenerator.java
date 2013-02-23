package processing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import utils.ParameterSetting;
import utils.TextUtil;

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
		try {
			BufferedReader csv = new BufferedReader(new FileReader(input));
			String line = "";
			while((line = csv.readLine()) != null){
				if(line.length() == 0)
					continue;
				if(line.startsWith("###Bag of words###")){
					//obsolete data file format. do nothing.
				}else if(line.startsWith("###")){
					//obsolete data file format. do nothing.
				}else if(line.trim().equals("------------")){
					//do nothing.
				}else{
					String[] parsed = TextUtil.ExtractDependentPair(line);
					if(parsed[3].equals("amod")){
						
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
