package data_structure;

import java.util.ArrayList;

import utils.TextUtil;

public class Template {
	private ArrayList<String> _patternTxts;
	
	public Template(){
		_patternTxts = new ArrayList<String>();
	}
	
	public Template(ArrayList<String> patternTxt){
		_patternTxts = patternTxt;
	}
	
	public String toString(){
		return TextUtil.joinStringArrayList(_patternTxts, " ");
	}

	public ArrayList<String> get_patternTxts() {
		return _patternTxts;
	}

	public void set_patternTxts(ArrayList<String> _patternTxts) {
		this._patternTxts = _patternTxts;
	}
}
