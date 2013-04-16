package data_structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import processing.StopwordsFilter;

import advance.DBLoader;

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
			phrases.add(entry.getKey());
			i++;
			if(i>=n)
				break;
		}
		return phrases;
	}
	
	public Map<String, ArrayList<String>> getStructuredPhrasebyTF(int n){
		if(phraseMap.getSortedMap().size() == 0)
			loadphraseMap();
		Map<String, ArrayList<String>> phrases = new HashMap<String, ArrayList<String>>();
		phrases.put("food", new ArrayList<String>());
		phrases.put("decor", new ArrayList<String>());
		phrases.put("overall", new ArrayList<String>());
		phrases.put("service", new ArrayList<String>());
		phrases.put("price", new ArrayList<String>());
		for(Map.Entry<String, Integer> entry:phraseMap.getSortedMap().entrySet()){
			String[] pair = entry.getKey().split("\t");
			if(pair.length != 2)
				continue;
			String categ = getAttributeCategory(pair[1]);
			if(!categ.equals("unkown")){
				if(phrases.get(categ).size() <5 && !StopwordsFilter.getInstance().ifBannedAttr(pair[1]) 
						&& !StopwordsFilter.getInstance().ifBannedVal(pair[0])){
					phrases.get(categ).add(entry.getKey());
				}
			}
			if((phrases.get("food").size() + phrases.get("decor").size()+
					phrases.get("overall").size() + phrases.get("service").size() + 
					phrases.get("price").size())>25){
				break;
			}
		}
		return phrases;
	}
	
	
	public String getAttributeCategory(String attr){
		Map<String, ArrayList<String>> map = DBLoader.getInstance().getClusterAttribute();
		if(map.get("food").contains(attr))
			return "food";
		else if(map.get("decor").contains(attr))
			return "decor";
		else if(map.get("overall").contains(attr))
			return "overall";
		else if(map.get("price").contains(attr))
			return "price";
		else if(map.get("service").contains(attr))
			return "service";
		return "unkown";
	}
}
