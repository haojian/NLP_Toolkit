package data_structure;

public class Extraction {
	private Value val;
	private Attribute attr;
	private int numberofoccurances;
	
	
	public Extraction(){
		
	}
	
	public Extraction(String value, String attribute, int occurance){
		
	}
	
	private Value getVal() {
		return val;
	}
	private void setVal(Value val) {
		this.val = val;
	}
	private Attribute getAttr() {
		return attr;
	}
	private void setAttr(Attribute attr) {
		this.attr = attr;
	}
	private int getNumberofoccurances() {
		return numberofoccurances;
	}
	private void setNumberofoccurances(int numberofoccurances) {
		this.numberofoccurances = numberofoccurances;
	}
}
