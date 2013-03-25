package data_structure;

import java.util.ArrayList;

public class ReviewEntry {


	private ArrayList<Extraction> extractions; 
	private int[] sentIndexRange;
	private int revIndex;
	private double revRating;
	
	public ReviewEntry(){
		extractions = new ArrayList<Extraction>();
		sentIndexRange = new int[2];
	}
	
	public void addExtraction(Extraction tmp){
		extractions.add(tmp);
	}

	public ArrayList<Extraction> getExtract() {
		return extractions;
	}
	
	public void setStartSentIndex(int index){
		sentIndexRange[0] = index;
	}
	
	public void setEndSentIndex(int index){
		sentIndexRange[1] = index;
	}
	
	public int getStartSentIndex(){
		return sentIndexRange[0];
	}
	
	public int getEndSentIndex(){
		return sentIndexRange[1];
	}
	
	public void setRating(double rat){
		revRating = rat;
	}
	
	public double getRating(){
		return revRating;
	}
}
