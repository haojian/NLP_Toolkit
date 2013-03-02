package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import data_structure.Attribute;
import data_structure.Extraction;
import data_structure.Template;
import data_structure.Value;

public class TextUtil {
	
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
		StringBuilder sb = new StringBuilder();
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
		"here #ATTRIBUTE# is bad",
		"#ATTRIBUTE# is bad"};
		for(String tmp: originalpattern){
			String pattern = tmp.replaceAll("#ATTRIBUTE#", ParameterSetting.REGXWORDPATTERN_V2);
			System.out.println(pattern);
			//String pattern = "here (.*) is bad.";
			String data = "here parking is bad.";
			RegexExtraction(pattern, data);
		}
	}
	
	public static String RegexExtraction(String patternStr, String data){
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(data);
		if (matcher.find()) {   
			if(patternStr.startsWith("(.*)")){
				String[] array = matcher.group(1).split(" ");
				//System.out.println(array[array.length-1].trim().replace(",", ""));
				return array[array.length-1].trim().replace(",", "");
			}else{
				String[] array = matcher.group(1).split(" ");
				//System.out.println(array[0].trim().replace(",", ""));
				return array[0].trim().replace(",", "");
			}
		}
		return null;
	}
	
	public static String ValueExtraction(Template temp, Attribute attr, String sent){
		if(sent == null || sent.length() == 0){
			return null;
		}
		String pattern = temp.toValueTemplateString(attr).replace("#VALUE#", ParameterSetting.REGXWORDPATTERN_V2);
		String res = RegexExtraction(pattern, sent);
		return res;
	}
	
	public static String attributeExtraction(Template temp, Value val, String sent){
		if(sent == null || sent.length() == 0){
			return null;
		}
		
		String pattern = temp.toAttrTemplateString(val).replace("#ATTRIBUTE#", ParameterSetting.REGXWORDPATTERN_V2);
		String res = RegexExtraction(pattern, sent);
		return res;
	}
	
	public static Template patternExtraction(Value val, Attribute attr, String sent){
		ArrayList<String> res = new ArrayList<String>();
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
				}
			}
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
}
