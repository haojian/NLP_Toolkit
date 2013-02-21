package processing;

import java.util.ArrayList;

import utils.ParameterSetting;

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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
	
	public Extraction_bootstrapping(){
		curTemplates = new ArrayList<Template>();
		curExtractions = new ArrayList<Extraction>();
	}
	
	public void InitSeedExtraction(){
		curExtractions.add(new Extraction("tasty", "food", 0));
		curExtractions.add(new Extraction("delicious", "food", 0));
		curExtractions.add(new Extraction("disgusting", "food", 0));
		curExtractions.add(new Extraction("disgusting", "food", 0));
	}
	
	public void Process(){
		return;
	}
	
	public void ValueInduction(){
		return;
	}
	
	public void AttributeInduction(){
		return;
	}
	
	public void TemplateInduction(){
		return;
	}
	
	public void UpdateProcess(){
		return;
	}

}
