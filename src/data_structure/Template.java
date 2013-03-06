package data_structure;

import java.awt.List;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.MultiKeyMap;

import utils.TextUtil;

public class Template {
	private ArrayList<String> _patternTxts;
	private ArrayList<String> _fulllpatternTxts;
	private String fullpaternTxt = "";
	private int customizedHashCode = -1;
	private boolean ifHashed = false;
	
	private Map<String, String> attributeExtractionHisotry;
	private Map<String, String> valueExtractionHistory;
	
	public Template(){
		_patternTxts = new ArrayList<String>();
		attributeExtractionHisotry = new HashMap<String, String>(10000000);
		valueExtractionHistory = new HashMap<String, String>(10000000);
	}
	
	public String getValueExtraction(String sent, String attribute){
		String key = sent + "-" + attribute;
		if(valueExtractionHistory.containsKey(key)){
			return valueExtractionHistory.get(key);
		}else{
			String value = TextUtil.ValueExtraction(this, new Attribute(attribute), sent);
			valueExtractionHistory.put(key, value);
			return value;
		}
	}
	
	public String getAttrExtraction(String sent, String value){
		//return TextUtil.attributeExtraction(this, new Value(value), sent);
		
		String key = sent + "-" + value;
		
		if(attributeExtractionHisotry.containsKey(key)){
			return attributeExtractionHisotry.get(key);
		}else{
			String attr = TextUtil.attributeExtraction(this, new Value(value), sent);
			attributeExtractionHisotry.put(key, attr);
			return attr;
		}
		
	}
	
	public Template(ArrayList<String> patternTxt){
		_patternTxts = patternTxt;
		fullpaternTxt = TextUtil.joinStringArrayList(_patternTxts, "");
		attributeExtractionHisotry = new HashMap<String, String>();
		valueExtractionHistory = new HashMap<String, String>();
	}
	
	public String toTemplateString(){
		return fullpaternTxt;
	}
	
	public String toValueTemplateString(Attribute attr){
		return fullpaternTxt.replace("#ATTRIBUTE#", attr.get_txt());
	}
	
	public String toAttrTemplateString(Value val){
		return TextUtil.HighPerformanceStringReplace(fullpaternTxt, "#VALUE#", val.get_txt());
		//return fullpaternTxt.replace("#VALUE#", val.get_txt());
	}

	public ArrayList<String> get_patternTxts() {
		return _patternTxts;
	}

	public void set_patternTxts(ArrayList<String> _patternTxts) {
		this._patternTxts = _patternTxts;
	}
	
	public int hashCode()
	{
		if(ifHashed){
			return customizedHashCode;
		}
		else{
			customizedHashCode = fullpaternTxt.hashCode();
			return customizedHashCode;
		}
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

	public String getFullpaternTxt() {
		return fullpaternTxt;
	}

	public void setFullpaternTxt(String fullpaternTxt) {
		this.fullpaternTxt = fullpaternTxt;
	}
}
