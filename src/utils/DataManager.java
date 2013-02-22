package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import processing.Extraction_bootstrapping;
import processing.SentenceSplitter;

public class DataManager {

	public static DataManager singleton;
	public static DataManager getInstance() {
		if (singleton == null)
			singleton = new DataManager();
		return singleton;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(File input : DataManager.getInstance().getFilesUnderFolder(ParameterSetting.PATHTOCRAWLEDDATA)){
			long startTime = System.currentTimeMillis();
			int size = DataManager.getInstance().getSentencesInFile(input).size();
			long ellapse = System.currentTimeMillis() - startTime;
			System.out.println(input.getName() + " \t " + size + " Execution time: " + ellapse);
		}
	}
	
	public File[] getFilesUnderFolder(String pathToDir){
		File dir = new File(pathToDir);
		if(!dir.isDirectory()){
			System.out.println("input path need to be a path to directory!");
			return null;
		}
		return dir.listFiles();
	}
	
	public ArrayList<String> getSentencesInFile(File inputFile){
		if(!inputFile.exists())
			return null;
		try{
			ArrayList<String> res = new ArrayList<String>();
			BufferedReader buReader = new BufferedReader(new FileReader(inputFile));
			String tmp="";
			int i = 0;
			for(String s = buReader.readLine(); s!= null; s= buReader.readLine()){
				if(s.startsWith("###")){
					if(tmp.length() == 0){
						i++;
						//System.out.println(String.valueOf(i)+ "th paragraph");
						//IOOperator.getInstance().writeToFile(output_path, s + "\n", true);
						continue;
					}
					ArrayList<String> sentences = SentenceSplitter.getInstance().sentence_split(tmp);
					res.addAll(sentences);
					/*
					for(String sent : sentences){
							//ArrayList<String> noun_results =stanfordparser.getInstance().parse_noun(sent);
							//IOOperator.getInstance().writeToFile(output_path, TextUtil.joinStringArrayList(noun_results) + "\n", true);
					}*/
					tmp = "";
					i++;
					//System.out.println(String.valueOf(i)+ "th paragraph");

				}
				else{
					tmp += s;
				}
				
				// flush the last result to output file.
				if(!tmp.isEmpty()){
					ArrayList<String> sentences = SentenceSplitter.getInstance().sentence_split(tmp);
					res.addAll(sentences);
					/*
					for(String sent : sentences){
						//ArrayList<String> noun_results =stanfordparser.getInstance().parse_noun(sent);
						//IOOperator.getInstance().writeToFile(output_path, TextUtil.joinStringArrayList(noun_results) + "\n", true);
					}
					*/
				}
			}
			return res;
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
