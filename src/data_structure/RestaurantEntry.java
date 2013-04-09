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
		attributeMap = new SortingHashMap();
	}
	
	private SortingHashMap phraseMap;
	private SortingHashMap attributeMap;
	
	public SortingHashMap getphrasemap(){
		if(phraseMap.getSortedMap().size() == 0)
			loadphraseMap();
		return phraseMap;
	}
	
	private void loadphraseMap(){
		for(Map.Entry<Integer, ReviewEntry> entry: ReviewMap.entrySet()){
			for(Extraction tmp: entry.getValue().getExtract()){
				phraseMap.updateMap(tmp.toString(), 1);
			}
		}
	}
	
	private void loadattributeMap(){
		for(Map.Entry<Integer, ReviewEntry> entry: ReviewMap.entrySet()){
			for(Extraction tmp: entry.getValue().getExtract()){
				attributeMap.updateMap(tmp.getAttr().get_txt(), 1);
			}
		}
	}
	
	public ArrayList<String> getTopNounsbyTF(int n){
		if(attributeMap.getSortedMap().size() == 0)
			loadattributeMap();
		ArrayList<String> nouns = new ArrayList<String>();
		int i =0;
		for(Map.Entry<String, Integer> entry:attributeMap.getSortedMap().entrySet()){
			//System.out.println(entry.getKey() + "\t" + entry.getValue());
			nouns.add(entry.getKey());
			i++;
			if(i>=n)
				break;
		}
		return nouns;
	}
	
	public ArrayList<String> getTopNounsbyTFIDF(int n){
		if(attributeMap.getSortedMap().size() == 0)
			loadattributeMap();
		ArrayList<String> nouns = new ArrayList<String>();
		
		return nouns;
	}
	
	public ArrayList<String> getTopKeyPhrasebyTFIDF(int n){
		if(phraseMap.getSortedMap().size() == 0)
			loadphraseMap();
		ArrayList<String> phrases = new ArrayList<String>();
		
		return phrases;
	}
	
	public ArrayList<String> getTopKeyPhrasebyTF(int n){
		if(phraseMap.getSortedMap().size() == 0)
			loadphraseMap();
		ArrayList<String> phrases = new ArrayList<String>();
		int i =0;
		for(Map.Entry<String, Integer> entry:phraseMap.getSortedMap().entrySet()){
			//System.out.println(entry.getKey() + "\t" + entry.getValue());
			phrases.add(entry.getKey());
			i++;
			if(i>=n)
				break;
		}
		return phrases;
	}
}
