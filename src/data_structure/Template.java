package data_structure;

import java.awt.List;
import java.util.ArrayList;

import utils.TextUtil;

public class Template {
	private ArrayList<String> _patternTxts;
	private ArrayList<String> _fulllpatternTxts;
	
	public Template(){
		_patternTxts = new ArrayList<String>();
	}
	
	public Template(ArrayList<String> patternTxt){
		_patternTxts = patternTxt;
	}
	
	public String toString(){
		return TextUtil.joinStringArrayList(_patternTxts, "");
	}
	
	public String toValueTemplateString(Attribute attr){
		return TextUtil.joinStringArrayList(_patternTxts, "").replace("#ATTRIBUTE#", attr.get_txt());
	}
	
	public String toAttrTemplateString(Value val){
		//return TextUtil.joinStringArrayList(_patternTxts, "");
		return TextUtil.joinStringArrayList(_patternTxts, "").replace("#VALUE#", val.get_txt());

	}

	public ArrayList<String> get_patternTxts() {
		return _patternTxts;
	}

	public void set_patternTxts(ArrayList<String> _patternTxts) {
		this._patternTxts = _patternTxts;
	}
	
	public int hashCode()
	{
		return TextUtil.joinStringArrayList(_patternTxts, "|").hashCode();
	}
	
	public boolean equals(Object obj){
		if(obj == null) return false;
		if(!this.getClass().equals(obj.getClass())) return false;
		Template target = (Template) obj;
		if(target.get_patternTxts().size() == this.get_patternTxts().size()){
			for(int i = 0; i< target.get_patternTxts().size(); i++){
				if(!target.get_patternTxts().get(i).equals(this.get_patternTxts().get(i)))
					return false;
			}
		}
		return true;
	}

	public ArrayList<String> get_fulllpatternTxts() {
		return _fulllpatternTxts;
	}

	public void set_fulllpatternTxts(ArrayList<String> _fulllpatternTxts) {
		this._fulllpatternTxts = _fulllpatternTxts;
	}
}
