package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import processing.StopwordsFilter;

import data_structure.Attribute;
import data_structure.Extraction;
import data_structure.Template;
import data_structure.Value;

public class TextUtil {
	
	private TextUtil(){
		
	}
	
	public static void main(String[] args){
		regexTest();
	}
	
	public static String joinStringArrayList(ArrayList<String> list){
		StringBuilder sb = new StringBuilder();
		for (String s : list)
		{
		    sb.append(s);
		    sb.append("\t");
		}
		return sb.toString();
	}
	
	public static String joinStringArrayList(ArrayList<String> list, String jointStr){
		if(list == null)
			return "";
		StringBuffer sb = new StringBuffer(1000);
		for (String s : list)
		{
		    sb.append(s);
		    sb.append(jointStr);
		}
		return sb.toString();
	}
	
	public static void regexTest(){
		//For three possible case, the target could be in middle, start, end of the string.
		String[] originalpattern = {
		"right #ATTRIBUTE#."};
		for(String tmp: originalpattern){
			String pattern = tmp.replaceAll("#ATTRIBUTE#", ParameterSetting.REGXWORDPATTERN_V2).replace(".", "/.");
			System.out.println(pattern);
			//String pattern = "here (.*) is bad.";
			String data = "we got there right when it opened and there was already a long list of people ahead of us.";
			RegexExtraction(pattern, data);
		}
	}
	
	//also remove the stopwords in the result.
	public static String RegexExtraction(String patternStr, String data){
		Pattern pattern;
		if(patternStr == null || patternStr.length() == 0 || patternStr.isEmpty())
			return null;
		if(RegexManager.getInstance().patternList.containsKey(patternStr)){
			pattern = RegexManager.getInstance().patternList.get(patternStr);
		}else{
			pattern = Pattern.compile(patternStr);
			RegexManager.getInstance().patternList.put(patternStr, pattern);
		}
		
		Matcher matcher = pattern.matcher(data);
		String res = "";
		if (matcher.find()) {
			if(patternStr.startsWith(ParameterSetting.REGXWORDPATTERN_V2)){
				String[] array = matcher.group(1).split(" ");
				res = array[array.length-1].replace(',', ' ').trim();
			}else{
				String[] array = matcher.group(1).split(" ");
 				res = array[0].replace(',', ' ').trim();
			}
		}

		if(StopwordsFilter.getInstance().isStopWords(res))
			return null;
		else
			return res;
	}
	
	public static String ValueExtraction(Template temp, Attribute attr, String sent){
		if(sent == null || sent.length() == 0){
			return null;
		}
		if(!sent.contains(attr.get_txt()))
			return null;
		//String pattern = temp.toValueTemplateString(attr).replace("#VALUE#", ParameterSetting.REGXWORDPATTERN_V2);
		String pattern = TextUtil.HighPerformanceStringReplace(temp.toValueTemplateString(attr), "#VALUE#", ParameterSetting.REGXWORDPATTERN_V2);
		String res = RegexExtraction(pattern, sent);
		return res;
	}
	
	public static String attributeExtraction(Template temp, Value val, String sent){
		if(sent == null || sent.length() == 0){
			return null;
		}
		if(!sent.contains(val.get_txt()))
			return null;
		//String pattern = temp.toAttrTemplateString(val).replace("#ATTRIBUTE#", ParameterSetting.REGXWORDPATTERN_V2);
		String pattern = TextUtil.HighPerformanceStringReplace(temp.toAttrTemplateString(val), "#ATTRIBUTE#", ParameterSetting.REGXWORDPATTERN_V2);
		String res = RegexExtraction(pattern, sent);
		return res;
	}
	
	public static Template patternExtraction(Value val, Attribute attr, String sent){
		if(sent.contains(val.get_txt()) && sent.contains(attr.get_txt())){
			sent = sent.replace(val.get_txt(), "-~-#VALUE#-~-");
			sent = sent.replace(attr.get_txt(), "-~-#ATTRIBUTE#-~-");
			String[] tmp = sent.split("-~-");
			int minDistIndex = -1;
			
			for(int i= 0 ; i<tmp.length; i++){
				if(tmp[i].equals("#VALUE#")){
					for(int j = (i+1); j<tmp.length; j++){
						if(tmp[j].equals("#ATTRIBUTE#")){
							if(j == (i+2)){
								if(minDistIndex == -1 || tmp[minDistIndex].split(" ").length > tmp[i+1].split(" ").length)
									minDistIndex = i+1;
							}
						}
					}
				}else if(tmp[i].equals("#ATTRIBUTE#")){
					for(int j = (i+1); j<tmp.length; j++){
						if(tmp[j].equals("#VALUE#")){
							if(j == (i+2))
								if(minDistIndex == -1 ||  tmp[minDistIndex].split(" ").length > tmp[i+1].split(" ").length)
									minDistIndex = i+1;
						}
					}
				}else{
					//tmp[i] = Pattern.quote(tmp[i]);
				}
			}
			ArrayList<String> res = new ArrayList<String>();
			if(minDistIndex != -1 && tmp[minDistIndex].split(" ").length > ParameterSetting.EXTRACTIONDISTANCETHRESHOLD){
				res.add(tmp[minDistIndex-1]);
				res.add(tmp[minDistIndex]);
				res.add(tmp[minDistIndex+1]);
			}
			else
				res.addAll(Arrays.asList(tmp));

			return new Template(res);
		}
		else 
			return null;
		
	}
	
	public static void OutputStats(DescriptiveStatistics stats, String desc){
		System.out.println(desc + stats.toString());
	}
	
	// Extract the relationship from data format: amod(iron-3, flat-2)
	// no index check done in this function. should be added in the further.
	public static String[] ExtractDependentPair(String input){
		if(input.contains("(") && input.contains(")") && input.contains(",")){
			String[] res = new String[3];
			int leftBracketIndex = input.indexOf("(");
			res[0] =  TextPreProcessing(input.substring(0, leftBracketIndex));
			int firsthyphenIndex = input.indexOf('-', leftBracketIndex);
			res[1] = TextPreProcessing(input.substring(leftBracketIndex+1 , firsthyphenIndex));
			
			int commaIndex = input.indexOf(",");
			int righthyphenIndex = input.indexOf('-', commaIndex);
			res[2] = TextPreProcessing(input.substring(commaIndex+1, righthyphenIndex));
			return res;
		} 
		return null;
	}
	
	
	public static boolean IfHighQualitySpelling(String input){
		if(!input.matches("[a-zA-Z]+"))
			return false;
		return true;
	}
	
	public static String TextPreProcessing(String input){
		return input.toLowerCase().trim();
	}
	
	
	///300% performance of the original replace operation.
	public static String HighPerformanceStringReplace(String str, String target, String replacement){
		int index = str.indexOf(target);
		if(index != -1){
			String res = str.substring(0, index) + replacement + str.substring(index + target.length());
			//System.out.println(res);
			return res;
		}
		return null;
	}
}
