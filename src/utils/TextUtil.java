package utils;

import java.util.ArrayList;

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
}
