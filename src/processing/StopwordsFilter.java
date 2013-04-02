package processing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class StopwordsFilter {

	private static StopwordsFilter singleton;
	public static StopwordsFilter getInstance(){
		if (singleton == null)
			singleton = new StopwordsFilter();
		return singleton;
	} 
	
	public ArrayList<String> Stopwordlist;
	public ArrayList<String> bannedAttrlist;
	public ArrayList<String> bannedVallist;


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
	
	public StopwordsFilter(){
		Stopwordlist = new ArrayList<String>();
		try {
			BufferedReader csv =  new BufferedReader(new FileReader(utils.ParameterSetting.PATHTOSTOPWORDS));
			String line = "";
			while((line = csv.readLine()) != null){
				if(line.length() != 0){
					Stopwordlist.add(line);
				}
			}
			
			csv =  new BufferedReader(new FileReader(utils.ParameterSetting.PATHTOSTOPWORDS));
			line = "";
			while((line = csv.readLine()) != null){
				if(line.length() != 0){
					bannedAttrlist.add(line);
				}
			}
			
			csv =  new BufferedReader(new FileReader(utils.ParameterSetting.PATHTOSTOPWORDS));
			line = "";
			while((line = csv.readLine()) != null){
				if(line.length() != 0){
					bannedVallist.add(line);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isStopWords(String input){
		if(input == null || input.length() == 0)
			return true;
		String tmp = input.toLowerCase();
        if (Stopwordlist.contains(tmp))
            return true;
        return false;
	}
	
	public boolean filterExtraction(String input){
		String[] tmp = input.split(" ");
		if(tmp.length == 2){
			if(Stopwordlist.contains(tmp[0]) || Stopwordlist.contains(tmp[1]))
				return true;
			if(bannedAttrlist.contains(tmp[1]))
				return true;
			if(bannedVallist.contains(tmp[0]))
				return true;
			return false;
		}
		return true;
	}

}
