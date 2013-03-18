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
		for(File input : DataManager.getInstance().getFilesUnderFolder(ParameterSetting.PATHTOCRAWLEDDATA2)){
			if(input.getName().split("_").length != 3)
				continue;
			long startTime = System.currentTimeMillis();
			ArrayList<String> tmp =  DataManager.getInstance().getSentencesInFile(input);
			int size = tmp.size();
			Extraction_bootstrapping.getInstance().UpdateCorpus(tmp, input.getName());
			long ellapse = System.currentTimeMillis() - startTime;
			System.out.println(input.getName() + " \t " + size + " Execution time: " + ellapse);

			//break;
		}
		Extraction_bootstrapping.getInstance().StartProcess();
		Extraction_bootstrapping.getInstance().WriteResulttoFile();
		System.out.println("long wait.....it's finished!....");
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
						continue;
					}
					ArrayList<String> sentences = SentenceSplitter.getInstance().sentence_split(tmp);
					res.addAll(sentences);
					tmp = "";
					i++;
				}
				else{
					tmp += s;
				}
			}
			// flush the last result to output file.
			if(!tmp.isEmpty()){
				ArrayList<String> sentences = SentenceSplitter.getInstance().sentence_split(tmp);
				res.addAll(sentences);
				tmp = "";
			}
			return res;
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
