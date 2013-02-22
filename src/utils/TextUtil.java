package utils;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import data_structure.Attribute;
import data_structure.Value;

public class TextUtil {
	public static String joinStringArrayList(ArrayList<String> list){
		StringBuilder sb = new StringBuilder();
		for (String s : list)
		{
		    sb.append(s);
		    sb.append("\t");
		}

		System.out.println(sb.toString());
		return sb.toString();
	}
	
	public static String joinStringArrayList(ArrayList<String> list, String jointStr){
		StringBuilder sb = new StringBuilder();
		for (String s : list)
		{
		    sb.append(s);
		    sb.append(jointStr);
		}

		System.out.println(sb.toString());
		return sb.toString();
	}
	
	public static ArrayList<String> patternExtraction(Value val, Attribute attr, String sent){
		ArrayList<String> res = new ArrayList<String>();
		if(sent.contains(val.get_txt()) && sent.contains(attr.get_txt())){
			sent.replace(" " + val.get_txt() + " ", "|||[VALUE]|||");
			sent.replace(" " + attr.get_txt() + " ", "|||[ATTRIBUTE]|||");
			String[] tmp = sent.split("|||");
			res.addAll(Arrays.asList(tmp));
		}
		return res;
	}
	
	public static void OutputStats(DescriptiveStatistics stats, String desc){
		System.out.println(desc + stats.toString());
	}
}
