package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import data_structure.SentenceEntry;

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
		/*
		for(File input : DataManager.getInstance().getFilesUnderFolder(ParameterSetting.PATHTOCRAWLEDDATA3)){
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
		//System.out.println(Extraction_bootstrapping.getInstance().corpus.get(229554).UniqueID+"\t" + Extraction_bootstrapping.getInstance().corpus.get(229554).get_senttxt());
		Extraction_bootstrapping.getInstance().StartProcess();
		Extraction_bootstrapping.getInstance().WriteResulttoFile();
		System.out.println("long wait.....it's finished!....");
		*/
		FileToSql(ParameterSetting.PATHTOTMPDIR1, ParameterSetting.PATHTOTMPDIR2);
	}
	
	public static File[] getFilesUnderFolder(String pathToDir){
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
	
	static int counter = 0;
	static DBUtil db1;
	public static void FileToSql(String extractionpath, String sentpath){
		System.setProperty("file.encoding","UTF-8");
		
		db1 = new DBUtil();
		db1.execute("DROP TABLE extractions;");
		String sql = "CREATE TABLE extractions ( value text, attr text, sentindex bigint);";
		db1.execute(sql);// create the table
		File[] extractionlists = getFilesUnderFolder(extractionpath);
		for(File input : extractionlists){
			ExtractionsToSQL(input);
		}
		db1.execute("DROP TABLE sentences;");
		db1.execute("CREATE TABLE sentences ( sent_index bigint, sent_txt text, revindex bigint, revrating, double);");// create the table
		File[] sentlists = getFilesUnderFolder(extractionpath);
		for(File input : sentlists){
			SentsToSQL(input);
		}
		db1.rundown();
	}
	
	static int sentsCounter = 0;
	public static void SentsToSQL(File input){
		if(!input.exists())
			return;
		ArrayList<SentenceEntry> res = new ArrayList<SentenceEntry>();
		try{
			
			BufferedReader buReader = new BufferedReader(new FileReader(input));
			String tmp="";
			int i = -1;
			double currating = -1;
			for(String s = buReader.readLine(); s!= null; s= buReader.readLine()){
				if(s.startsWith("###")){
					if(tmp.length() == 0){
						i++;
						currating = Double.valueOf(tmp.split("\t")[3]);
						continue;
					}
					ArrayList<String> sentences = SentenceSplitter.getInstance().sentence_split(tmp);
					for(String sent: sentences){
						SentenceEntry curSent = new SentenceEntry(sent, sentsCounter, input.getName());
						curSent.RevIndex = i;
						curSent.RevRating = currating;
						sentsCounter++;
						res.add(curSent);
					}
					
					tmp = "";
					i++;
					currating = Double.valueOf(tmp.split("\t")[3]);
				}
				else{
					tmp += s;
				}
			}
			// flush the last result to output file.
			if(!tmp.isEmpty()){
				ArrayList<String> sentences = SentenceSplitter.getInstance().sentence_split(tmp);
				for(String sent: sentences){
					SentenceEntry curSent = new SentenceEntry(sent, sentsCounter, input.getName());
					curSent.RevIndex = i;
					curSent.RevRating = currating;
					sentsCounter++;
					res.add(curSent);
				}
				tmp = "";
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//store the res data in to sql.
		for(SentenceEntry sentEntry: res){
			String sql = "INSERT INTO sentences ( sent_index , sent_txt , revindex , revrating )" + " VALUES ( " + sentEntry.UniqueID + ", '" + sentEntry.get_senttxt() + "', "+ sentEntry.RevIndex+ " , " + sentEntry.RevRating + ");";
			db1.executeUpdateSQL(sql);
		}
		System.err.println("% end..." + counter);
		
	}
	public static void ExtractionsToSQL(File input){
		try {
			BufferedReader br = new BufferedReader(new FileReader(input));
			String line = "";
			while((line = br.readLine()) != null){
				String infos[] = line.split("\t");
				if(infos.length == 3){
					String sql = "INSERT INTO extractions ( value, attr, sentindex )" + " VALUES ( '" + infos[0] + "', '" + infos[1] + "', "+ infos[2] + ");";
					db1.executeUpdateSQL(sql);
					counter++;
				}
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("% end..." + counter);
	}
}
