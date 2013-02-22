package data_structure;

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
}
