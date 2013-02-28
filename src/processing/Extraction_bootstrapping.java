package processing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import utils.ParameterSetting;
import utils.TextUtil;

import data_structure.Attribute;
import data_structure.Extraction;
import data_structure.Template;
import data_structure.Value;

public class Extraction_bootstrapping {
	public static Extraction_bootstrapping singleton;
	public static Extraction_bootstrapping getInstance() {
		if (singleton == null)
			singleton = new Extraction_bootstrapping();
		return singleton;
	}
	
	private Map<Template, Integer> templateMap;
	private Map<Extraction, Integer> extractionMap;
	
	private int bootstrapping_cutoff = ParameterSetting.BOOTSTRAPPINGTHRESHOLD;
	private ArrayList<String> corpus;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Extraction_bootstrapping.getInstance().StartProcess();
	}
	
	public Extraction_bootstrapping(){
		extractionMap = new HashMap<Extraction, Integer>();
		templateMap = new HashMap<Template, Integer>();
		corpus = new ArrayList<String>();
		InitSeedExtraction();
	}
	
	public void InitSeedExtraction(){
		try {
			BufferedReader seedReader = new BufferedReader(new FileReader(ParameterSetting.PATHTOSEEDFILE));
			String line = "";
			while((line = seedReader.readLine()) != null){
				String[] res = line.split("\t");
				if(res.length != (ParameterSetting.MAXSEEDSADJ + 1))
					continue;
				for(int i=1; i<ParameterSetting.MAXSEEDSADJ; i++){
					extractionMap.put(new Extraction(res[i], res[0], 0), 0);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("Init seed dict.....  Size : " + extractionMap.size());
	}
	
	public void UpdateCorpus(ArrayList<String> sents){
		for(String sent: sents){
			String tmp = TextUtil.TextPreProcessing(sent);
			corpus.add(tmp);
		}
		System.out.println("Corpus updated at size: "  + corpus.size());
	}
	
	public void StartProcess(){
		//if((ArrayList<String> res = TextUtil.patternExtraction(val, attr, sent))
		int lastIterationSize = -1;
		int i = 0;
		while(extractionMap.size() != lastIterationSize){
			System.err.println(i + "th iteration......" );
			lastIterationSize = extractionMap.size();
			TemplateInduction();
			AttributeInduction();
			//ValueInduction();
			bootstrapping_cutoff += ParameterSetting.BOOTSTRAPPINGTHRESHOLD;
			i++;
			break;
		}
		return;
	}
	
	public void ValueInduction(){
		for(String sent: corpus){
			//add a map to make sure each extraction be extracted for once.
			for(Template pattern: templateMap.keySet()){
				for(Extraction extrac : extractionMap.keySet()){
					String value = TextUtil.ValueExtraction(pattern, extrac.getAttr(), sent);
					if(value!= null && !value.equals(extrac.getAttr()))
						System.out.println( value + " " + extrac.getAttr().get_txt());
				}
			}
		}
		return;
	}
	
	public void AttributeInduction(){
		int i =0;
		int presize = extractionMap.size();
		for(String sent: corpus){
			ArrayList<Extraction> extractionsInSent = new ArrayList<Extraction>();
			for(Template pattern: templateMap.keySet()){
				
				
				for(Iterator<Map.Entry<Extraction,Integer>> it = extractionMap.entrySet().iterator(); it.hasNext();){
					Map.Entry<Extraction,Integer> e = it.next();
					Extraction extrac = e.getKey();
					String attribute = TextUtil.attributeExtraction(pattern, extrac.getVal(), sent);
					if(attribute!= null && !attribute.equals(extrac.getAttr())){
						Extraction newExtraction = new Extraction(extrac.getVal().get_txt(), attribute, 1);
						if(!extractionsInSent.contains(newExtraction)){
							i++;
							extractionsInSent.add(newExtraction);
							//update in the gloabl map
							/*
							if(extractionMap.containsKey(newExtraction))
								extractionMap.put(newExtraction, extractionMap.get(newExtraction) + 1);
							else
								extractionMap.put(newExtraction, 1);
								*/
						}
					}
					
				}
			}
		}
		
		for(Iterator<Map.Entry<Extraction,Integer>> it = extractionMap.entrySet().iterator(); it.hasNext();){
			Map.Entry<Extraction,Integer> e = it.next();
			if(e.getValue() < bootstrapping_cutoff){
				it.remove();
			}else{
				extractionMap.put(e.getKey(), e.getValue()+ParameterSetting.BOOTSTRAPPINGTHRESHOLD);
			}
		}
		System.out.println( i+ " attribute based exttractions were found. " + (extractionMap.size() - presize) + " exttractions were kept");

		return;
	}
	
	public void TemplateInduction(){
		int i = 0;
		 /* sortingMap is used to debug the template extractions.
		 HashMapValueComparator sortingcomp = new HashMapValueComparator(templateMap);
		 TreeMap sortingMap = new TreeMap(sortingcomp);
		*/
		for(String sent : corpus){
			for(Extraction tmpExtract: extractionMap.keySet()){
				Template pattern = TextUtil.patternExtraction(tmpExtract.getVal(), tmpExtract.getAttr(), sent);
				if(pattern != null){
					//System.out.println(pattern.toString());
					if(templateMap.containsKey(pattern))
						templateMap.put(pattern, templateMap.get(pattern) + 1);
					else
						templateMap.put(pattern, 1);
					i++;
				}
			}
		}
		//sortingMap.putAll(templateMap);
		for (Iterator<Map.Entry<Template,Integer>> it = templateMap.entrySet().iterator(); it.hasNext();) {
			 Map.Entry<Template,Integer> e = it.next();
			 if (e.getValue() < bootstrapping_cutoff) {
				 it.remove();
			 }else{
				 templateMap.put(e.getKey(), e.getValue()+ParameterSetting.BOOTSTRAPPINGTHRESHOLD);
			 }
			}
		System.out.println( i+ " templates were found. " + templateMap.size() + " templates were kept");
		return;
	}
	
	public void UpdateProcess(){
		return;
	}

	
	
	private void OutputProcessingRes(){
		
	}
}

class HashMapValueComparator implements Comparator<Object> {
    Map theMapToSort;
    public HashMapValueComparator(Map theMapToSort) {
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
