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
	private ArrayList<String> attrList;
	private ArrayList<String> valList;
	
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
		attrList = new ArrayList<String>();
		valList = new ArrayList<String>();
		InitSeedExtraction();
	}
	
	//Init an Extraction Map which would be used in the first Template Induction.
	//Init the ValueList & AttrList would be used in the first Value Induction & Attribute Induction.
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
					if(!valList.contains(res[i]))
						valList.add(res[i]);
				}
				if(!attrList.contains(res[0]))
					attrList.add(res[0]);
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
			System.out.println(i + "th iteration......" );
			lastIterationSize = extractionMap.size();
			
			TemplateInduction();
			//AttributeInduction();
			ValueInduction();
			UpdateToNewIteration();
			i++;
			break;
		}
		return;
	}
	
	public void ValueInduction(){
		int i = 0;
		int presize = extractionMap.size();
		Map<Extraction, Integer> cacheMap = new HashMap<Extraction, Integer>();
		for(String sent: corpus){
			ArrayList<Extraction> extractionsInSent = new ArrayList<Extraction>();
			for(Template pattern: templateMap.keySet()){
				for(String attr : attrList){
					Attribute _attrObj = new Attribute(attr);
					String value = TextUtil.ValueExtraction(pattern, _attrObj, sent);
					if(value != null){
						Extraction curExtraction = new Extraction(value, attr, 0);
						if(!extractionsInSent.contains(curExtraction)){
							extractionsInSent.add(curExtraction);
						}
					}
				}
			}
			for(Extraction extraction : extractionsInSent){
				i++;
				if(cacheMap.containsKey(extraction))
					cacheMap.put(extraction, cacheMap.get(extraction) + 1);
				else
					cacheMap.put(extraction, 1);
			}
		}
		
		for(Iterator<Map.Entry<Extraction,Integer>> it = cacheMap.entrySet().iterator(); it.hasNext();){
			Map.Entry<Extraction,Integer> e = it.next();
			if(e.getValue() < bootstrapping_cutoff)
				it.remove();
		}
		
		for(Map.Entry<Extraction, Integer> entry : cacheMap.entrySet()){
			if(extractionMap.containsKey(entry.getKey()))
				extractionMap.put(entry.getKey(), entry.getValue() + extractionMap.get(entry.getKey()));
			else
				extractionMap.put(entry.getKey(), entry.getValue());
		}
		System.out.println( i+ " value based exttractions were found. " + (extractionMap.size() - presize) + " new exttractions were added. " + extractionMap.size() + " extractions in total. ");
		return;
	}
	
	
	public void AttributeInduction(){
		int i = 0;
		int presize = extractionMap.size();
		Map<Extraction, Integer> cacheMap = new HashMap<Extraction, Integer>();
		for(String sent: corpus){
			ArrayList<Extraction> extractionsInSent = new ArrayList<Extraction>();
			for(Template pattern: templateMap.keySet()){
				for(String val : valList){
					Value _valObj = new Value(val);
					String attribute = TextUtil.attributeExtraction(pattern, _valObj, sent);
					if(attribute != null){
						Extraction curExtraction = new Extraction(val, attribute, 0);
						if(!extractionsInSent.contains(curExtraction)){
							extractionsInSent.add(curExtraction);
						}
					}
				}
			}
			for(Extraction extraction : extractionsInSent){
				i++;
				if(cacheMap.containsKey(extraction))
					cacheMap.put(extraction, cacheMap.get(extraction) + 1);
				else
					cacheMap.put(extraction, 1);
			}
		}
		for(Iterator<Map.Entry<Extraction,Integer>> it = cacheMap.entrySet().iterator(); it.hasNext();){
			Map.Entry<Extraction,Integer> e = it.next();
			if(e.getValue() < bootstrapping_cutoff)
				it.remove();
		}
		
		for(Map.Entry<Extraction, Integer> entry : cacheMap.entrySet()){
			if(extractionMap.containsKey(entry.getKey()))
				extractionMap.put(entry.getKey(), entry.getValue() + extractionMap.get(entry.getKey()));
			else
				extractionMap.put(entry.getKey(), entry.getValue());
		}
		
		System.out.println( i+ " attribute based exttractions were found. " + (extractionMap.size() - presize) + " new exttractions were added. " + extractionMap.size() + " extractions in total. ");
		return;
	}
	
	//Extract the template based on the extraction already have.
	public void TemplateInduction(){
		int i = 0;
		int presize = templateMap.size();
		Map<Template, Integer> cacheMap = new HashMap<Template, Integer>();
		 /* sortingMap is used to debug the template extractions.
		 HashMapValueComparator sortingcomp = new HashMapValueComparator(templateMap);
		 TreeMap sortingMap = new TreeMap(sortingcomp);
		*/
		for(String sent: corpus){
			for(Extraction curExtract: extractionMap.keySet()){
				Template pattern = TextUtil.patternExtraction(curExtract.getVal(), curExtract.getAttr(), sent);
				if(pattern != null){
					if(cacheMap.containsKey(pattern)){
						cacheMap.put(pattern, cacheMap.get(pattern) + 1);
					}else{
						cacheMap.put(pattern, 1);
					}
					i++;
				}
			}
		}
		for(Iterator<Map.Entry<Template,Integer>> it = cacheMap.entrySet().iterator(); it.hasNext();){
			Map.Entry<Template,Integer> e = it.next();
			if(e.getValue() < bootstrapping_cutoff)
				it.remove();
		}
		for(Map.Entry<Template, Integer> entry : cacheMap.entrySet()){
			if(templateMap.containsKey(entry.getKey()))
				templateMap.put(entry.getKey(), entry.getValue() + templateMap.get(entry.getKey()));
			else
				templateMap.put(entry.getKey(), entry.getValue());
		}
		//sortingMap.putAll(templateMap);
		System.out.println( i+ " templates were found. " + (templateMap.size()-presize) + " templates added. " + templateMap.size() + " templates in total. ");
		return;
	}
	
	//update Attribute List & Value List.
	//update bootstrapping_cutoff
	public void UpdateToNewIteration(){
		attrList.clear();
		valList.clear();
		for(Iterator<Map.Entry<Extraction, Integer>> it = extractionMap.entrySet().iterator(); it.hasNext();){
			Map.Entry<Extraction, Integer> e = it.next();
			if(e.getValue()<bootstrapping_cutoff){
				it.remove();
				continue;
			}
			if(e.getKey() != null){
				if(!attrList.contains(e.getKey().getAttr().get_txt()))
					attrList.add(e.getKey().getAttr().get_txt());
				if(!valList.contains(e.getKey().getVal().get_txt()))
					valList.add(e.getKey().getAttr().get_txt());
			}
		}
		bootstrapping_cutoff += ParameterSetting.BOOTSTRAPPINGTHRESHOLD;
		
		System.out.println( attrList.size() + "attributes in total. " + valList.size() + "values in total. "  + extractionMap.size() + " extractions in total. ");

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
