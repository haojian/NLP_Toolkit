package processing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

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
	
	
	private ArrayList<Template> curTemplates;
	private ArrayList<Extraction> curExtractions;
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
		curTemplates = new ArrayList<Template>();
		curExtractions = new ArrayList<Extraction>();
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
					curExtractions.add(new Extraction(res[i], res[0], 0));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("Init seed dict.....  Size : " + curExtractions.size());
	}
	
	public void UpdateCorpus(ArrayList<String> sents){
		corpus.addAll(sents);
		System.out.println("Corpus updated at size: "  + corpus.size());
	}
	
	public void StartProcess(){
		//if((ArrayList<String> res = TextUtil.patternExtraction(val, attr, sent))
		int lastIterationSize = -1;
		int i = 0;
		while(curExtractions.size() != lastIterationSize){
			System.err.println(i + "th iteration......" );
			lastIterationSize = curExtractions.size();
			TemplateInduction();
			AttributeInduction();
			ValueInduction();
			bootstrapping_cutoff += ParameterSetting.BOOTSTRAPPINGTHRESHOLD;
			i++;
			break;
		}
		return;
	}
	
	public void ValueInduction(){
		
		return;
	}
	
	public void AttributeInduction(){
		return;
	}
	
	public void TemplateInduction(){
		int i = 0 ;
		for(String sent : corpus){
			for(Extraction tmpExtract: curExtractions){
				Template pattern = TextUtil.patternExtraction(tmpExtract.getVal(), tmpExtract.getAttr(), sent);
				if(pattern != null){
					System.out.println(pattern.toString());
					i++;
				}
			}
		}
		System.out.println(i + " templates were found.");
		return;
	}
	
	public void UpdateProcess(){
		return;
	}

	
	
	private void OutputProcessingRes(){
		
	}
}
