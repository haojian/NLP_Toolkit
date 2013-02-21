package data_structure;

import java.util.ArrayList;

import utils.TextUtil;

public class Template {
	public ArrayList<String> patternTxts;
	
	public Template(){
		patternTxts = new ArrayList<String>();
	}
	
	public String toString(){
		return TextUtil.joinStringArrayList(patternTxts, " ");
	}
}
