package processing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import utils.IOOperator;
import utils.ParameterSetting;
import utils.TextUtil;

import data_structure.Attribute;
import data_structure.Extraction;
import data_structure.Value;

public class SeedGenerator {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		SeedGenerator.getInstance();
	}
	
	//hashmap to ensure the performance
	private Map<String, Map<String, Integer>> raw_nounbased_seedsdict;
	//treemap to do the sorting stuffs.
	private TreeMap nounbased_seedsdict;

	
	public static SeedGenerator singleton;
	public static SeedGenerator getInstance(){
		if (singleton == null)
			singleton = new SeedGenerator();
		return singleton;
	} 
	

	public SeedGenerator(){
		IOOperator.getInstance().clearOldLogFiles();
		setNounbased_seedsdict(new TreeMap<String, Map<String, Integer>>());
		raw_nounbased_seedsdict = new HashMap<String, Map<String, Integer>>();
		System.err.println("Loading data files.....");
		File dir = new File(ParameterSetting.PATHTOPOSTAGGEDDATA);
		for(File tmp : dir.listFiles()){
			if(tmp.getName().split("_").length != 3)
				continue;
			LoadRevTxtProccessedData(tmp);
		}
		System.err.println("Processing loaded files.....");
		SortSeedsDict();
		System.err.println("Output seedsdict.....");
		OutputSeedsDict(false);
		System.err.println("Seed generation done!");

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
					if(parsed != null && parsed.length == 3 && parsed[0].equals("amod")){
						System.out.println(parsed[2] + "\t" + parsed[1]);
						
						if(TextUtil.IfHighQualitySpelling(parsed[1]) && TextUtil.IfHighQualitySpelling(parsed[2])){
							if(!StopwordsFilter.getInstance().isStopWords(parsed[1]) && !StopwordsFilter.getInstance().isStopWords(parsed[2])){
								SeedsDictExtraction(parsed[2], parsed[1]);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private void SeedsDictExtraction(String adj, String noun){
		if(raw_nounbased_seedsdict.containsKey(noun)){
			if(raw_nounbased_seedsdict.get(noun).containsKey(adj)){
				raw_nounbased_seedsdict.get(noun).put(adj, raw_nounbased_seedsdict.get(noun).get(adj) + 1);
			}
			else{
				raw_nounbased_seedsdict.get(noun).put(adj, 1);
			}
		}
		else{
			raw_nounbased_seedsdict.put(noun, new HashMap<String, Integer>());
			raw_nounbased_seedsdict.get(noun).put(adj, 1);
		}
	}
	
	private void OutputSeedsDict(boolean ifFullOutput){
		if(ifFullOutput){
	        for(Map.Entry<String, Map<String, Integer>> entry : ((Map<String, Map<String, Integer>>)getNounbased_seedsdict()).entrySet()) {
	        	IOOperator.getInstance().writeToFile(ParameterSetting.PATHTOSEEDFILE, entry.getKey() + "\t" + entry.getValue() + "\n", true);
	        }
		}else{
			int nounCounter = 0;
			
			for(Map.Entry<String, Map<String, Integer>> entry : ((Map<String, Map<String, Integer>>)getNounbased_seedsdict()).entrySet()) {
	        	if(nounCounter >= ParameterSetting.MAXSEEDSNOUN)
	        		break;
	        	IOOperator.getInstance().writeToFile(ParameterSetting.PATHTOSEEDFILE, "\n" + entry.getKey(), true);
	        	nounCounter++;
	        	
	        	int adjCounter =0;
	        	for(Map.Entry<String, Integer> adjEntry : entry.getValue().entrySet()){
	        		if(adjCounter >= ParameterSetting.MAXSEEDSADJ)
	        			break;
	        		IOOperator.getInstance().writeToFile(ParameterSetting.PATHTOSEEDFILE, "\t" + adjEntry.getKey(), true);
	        		adjCounter++;
	        	}
	        }
		}
	}
	
	private void SortSeedsDict(){
        NounCounterComparator nouncomp = new NounCounterComparator(raw_nounbased_seedsdict);
		setNounbased_seedsdict(new TreeMap(nouncomp));

        for(Map.Entry<String, Map<String, Integer>> entry : raw_nounbased_seedsdict.entrySet()) {
            Map<String, Integer> value = entry.getValue();
    		AdjCounterComparator adjcomp = new AdjCounterComparator(value);
    		TreeMap adjtreeMap = new TreeMap(adjcomp);
    		adjtreeMap.putAll(value);
    		getNounbased_seedsdict().put(entry.getKey(), adjtreeMap);
        }
	}


	public TreeMap getNounbased_seedsdict() {
		return nounbased_seedsdict;
	}


	public void setNounbased_seedsdict(TreeMap nounbased_seedsdict) {
		this.nounbased_seedsdict = nounbased_seedsdict;
	}
}

class NounCounterComparator implements Comparator<Object> {
    Map theMapToSort;
    public NounCounterComparator(Map theMapToSort) {
        this.theMapToSort = theMapToSort;
    }

    public int compare(Object key1, Object key2) {
    	Map<String, Integer> val1 = (Map<String, Integer>)theMapToSort.get(key1);
    	Map<String, Integer> val2 = (Map<String, Integer>) theMapToSort.get(key2);
    	int sumA = 0;
    	for(Integer i: val1.values())
    		sumA += i;
    	int sumB = 0;
    	for(Integer i:val2.values())
    		sumB += i;
    	
        if (sumA < sumB) {
            return 1;
        } else {
            return -1;
        }
    }
}

class AdjCounterComparator implements Comparator<Object> {
    Map theMapToSort;
    public AdjCounterComparator(Map theMapToSort) {
        this.theMapToSort = theMapToSort;
    }

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
