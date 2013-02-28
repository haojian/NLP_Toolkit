package data_structure;

import utils.TextUtil;

public class Extraction {
	private Value val;
	private Attribute attr;
	private int numberofoccurances;
	
	
	public Extraction(){
		
	}
	
	public Extraction(String value, String attribute){
		this.setVal(new Value(value));
		this.setAttr(new Attribute(attribute));
		this.setNumberofoccurances(1);
	}
	
	public Extraction(String value, String attribute, int occurance){
		this.setVal(new Value(value));
		this.setAttr(new Attribute(attribute));
		this.setNumberofoccurances(occurance);
	}
	
	public Value getVal() {
		return val;
	}
	
	public void setVal(Value val) {
		this.val = val;
	}
	
	public Attribute getAttr() {
		return attr;
	}
	
	public void setAttr(Attribute attr) {
		this.attr = attr;
	}
	
	public int getNumberofoccurances() {
		return numberofoccurances;
	}
	
	public void setNumberofoccurances(int numberofoccurances) {
		this.numberofoccurances = numberofoccurances;
	}
	
	public int hashCode()
	{
		String tmp = val.get_txt() + "|" + attr.get_txt();
		return tmp.hashCode();
	}
	
	public boolean equals(Object obj){
		if(obj == null) return false;
		if(!this.getClass().equals(obj.getClass())) return false;
		Extraction target = (Extraction) obj;
		if(target.getAttr().get_txt().equals(this.getAttr().get_txt()) && target.getVal().get_txt().equals(this.getVal().get_txt()))
			return true;
		return false;
	}
}
