package data_structure;

import java.awt.List;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections.map.MultiKeyMap;

import utils.TextUtil;

public class Template {
	
	private ArrayList<String> _originalTexts;
	private ArrayList<String> _patternTexts;
	private String fullpatternTxt ="";
	private int customizedHashCode = -1;
	private boolean ifHashed = false;
	
	
	public Template(ArrayList<String> originalTexts){
		_originalTexts = originalTexts;
		set_patternTexts(new ArrayList<String>());
		for(String tmp: originalTexts){
			if(tmp.equals("#ATTRIBUTE#") ||tmp.equals("#VALUE#")){
				get_patternTexts().add(tmp);
			}else{
				get_patternTexts().add(Pattern.quote(tmp));
			}
		}
		fullpatternTxt = TextUtil.joinStringArrayList(get_patternTexts(), "");
	}
	
	public String getValueExtraction(String sent, String attribute){
		return TextUtil.ValueExtraction(this, new Attribute(attribute), sent);
	}
	
	public String getAttrExtraction(String sent, String value){
		return TextUtil.attributeExtraction(this, new Value(value), sent);
	}

	public boolean preQualify(String sent){
		for(String str : _originalTexts){
			if(str.equals("#ATTRIBUTE#") || str.equals("#VALUE#"))
				continue;
			if(!sent.contains(str))
				return false;
		}
		return true;
	}
	
	public String toTemplateString(){
		return fullpatternTxt;
	}
	
	public String toValueTemplateString(Attribute attr){
		return fullpatternTxt.replace("#ATTRIBUTE#", attr.get_txt());
	}
	
	public String toAttrTemplateString(Value val){
		//return TextUtil.HighPerformanceStringReplace(fullpaternTxt, "#VALUE#", val.get_txt());
		return fullpatternTxt.replace("#VALUE#", val.get_txt());
	}

	
	public int hashCode()
	{
		if(ifHashed){
			return customizedHashCode;
		}
		else{
			customizedHashCode = fullpatternTxt.hashCode();
			return customizedHashCode;
		}
	}
	
	public boolean equals(Object obj){
		if(obj == null) return false;
		if(!this.getClass().equals(obj.getClass())) return false;
		Template target = (Template) obj;
		if(target.get_patternTexts().size() == this.get_patternTexts().size()){
			for(int i = 0; i< target.get_patternTexts().size(); i++){
				if(!target.get_patternTexts().get(i).equals(this.get_patternTexts().get(i)))
					return false;
			}
		}
		return true;
	}

	public String getFullpaternTxt() {
		return fullpatternTxt;
	}

	public void setFullpaternTxt(String fullpaternTxt) {
		this.fullpatternTxt = fullpaternTxt;
	}

	public ArrayList<String> get_patternTexts() {
		return _patternTexts;
	}

	public void set_patternTexts(ArrayList<String> _patternTexts) {
		this._patternTexts = _patternTexts;
	}
}
