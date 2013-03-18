package data_structure;

import java.util.ArrayList;

import utils.TextUtil;

public class Extraction {
	private Value val;
	private Attribute attr;
	private int numberofoccurances;
	private int customizedHashCode = -1;
	private boolean ifHashed = false;
	
	
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
	
	@Override 
	public String toString(){
		return this.val.get_txt() + "\t" + this.attr.get_txt();
	}
	
	public int hashCode()
	{
		if(ifHashed){
			return customizedHashCode;
		}
		else{
			String tmp = val.get_txt() + "|" + attr.get_txt();
			customizedHashCode = tmp.hashCode();
			return customizedHashCode;
		}		
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
