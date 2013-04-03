package processing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import data_structure.Extraction;
import data_structure.RestaurantEntry;
import data_structure.ReviewEntry;

import utils.DBUtil;
import utils.IOOperator;
import utils.Logger;
import utils.ParameterSetting;
import utils.TextUtil;

public class OutputResultFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//LoadReviewsFromSQL();
		//OutputExtractions();
		clusteNouns();
	}

	
	private static Map<String, RestaurantEntry> dataHash = new HashMap<String, RestaurantEntry>();
	
	public static void LoadReviewsFromSQL(){
		DBUtil db1 = new DBUtil();
		Logger.getInstance().getElapseTime(true);
		String sql = "select * from sentences";
		ResultSet rs1 = db1.executeQuerySQL(sql);
		try{
			while(rs1.next()){
				String filename = TextUtil.decode(rs1.getString("filename"));
				int curSentIndex = rs1.getInt("sent_index");
				if(!dataHash.containsKey(filename)){
					RestaurantEntry tmp = new RestaurantEntry();
					tmp.StartSentIndex = curSentIndex;
					dataHash.put(filename, tmp);
				}else{
					dataHash.get(filename).EndSentIndex = curSentIndex;
				}
				int revIndex = rs1.getInt("revindex");

				double rating = rs1.getDouble("revrating");
				if(dataHash.get(filename).ReviewMap.get(revIndex) == null){
					ReviewEntry tmp = new ReviewEntry();
					tmp.setStartSentIndex(curSentIndex);
					tmp.setRating(rating);
					dataHash.get(filename).ReviewMap.put(revIndex, tmp);
				}else{
					dataHash.get(filename).ReviewMap.get(revIndex).setEndSentIndex(curSentIndex);
				}
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Logger.getInstance().getElapseTime(true);
		sql = "select * from extractions";
		rs1 = db1.executeQuerySQL(sql);
		try{
			while(rs1.next()){
				int sentIndex = rs1.getInt("sentindex");
				String attr = rs1.getString("attr");
				String value = rs1.getString("value");
				Extraction tmp = new Extraction(value, attr, 0);
				tmp.setSentIndex(sentIndex);
				insertIntoHash(tmp, sentIndex);
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Logger.getInstance().getElapseTime(true);
		db1.rundown();
	}
	
	public static void insertIntoHash(Extraction extract, int sentIndex){
		for(Entry<String, RestaurantEntry> tmp :  dataHash.entrySet()){
			if(sentIndex <= tmp.getValue().EndSentIndex && sentIndex >= tmp.getValue().StartSentIndex){
				
			for(Entry<Integer, ReviewEntry> subTmp:tmp.getValue().ReviewMap.entrySet()){
				if(sentIndex >= subTmp.getValue().getStartSentIndex() && sentIndex <= subTmp.getValue().getEndSentIndex()){
					dataHash.get(tmp.getKey()).ReviewMap.get(subTmp.getKey()).addExtraction(extract);
					//subTmp.getValue().addExtraction(extract);
					//Logger.getInstance().IncreaseCounter();
					return;
				}
			}
			}
		}
	}
	
	public static void OutputExtractions(){
		for(Entry<String, RestaurantEntry> restaurant: dataHash.entrySet()){
			System.out.println(restaurant.getKey() + "\t" + restaurant.getValue().ReviewMap.keySet().size());
			IOOperator.getInstance().writeToFileUTF8(ParameterSetting.PATHTOEXTRACTIONWITHRATING + "/" + restaurant.getKey(), "", false);
			for(Entry<Integer, ReviewEntry> review : restaurant.getValue().ReviewMap.entrySet()){
				IOOperator.getInstance().writeToFileUTF8(ParameterSetting.PATHTOEXTRACTIONWITHRATING + "/" + restaurant.getKey(), "####" + review.getKey().toString() + "\t" + review.getValue().getRating()  + "\t"+ review.getValue().getStartSentIndex() + "\t" + review.getValue().getEndSentIndex() + "\n", true);
				//System.out.println(review.getValue().getExtract().size());
				for(Extraction extract: review.getValue().getExtract()){
					IOOperator.getInstance().writeToFileUTF8(ParameterSetting.PATHTOEXTRACTIONWITHRATING + "/" + restaurant.getKey(),  extract.getVal().get_txt() + "\t" + extract.getAttr().get_txt() +"\t" + extract.getSentIndex() + "\n", true);
				}
			}
		}
	}
	
	private static Map<String, Map<String, Integer>> extractionHash = new HashMap<String, Map<String, Integer>>();
	private static ArrayList<String> valList,attrList;
	private static Map<String, ArrayList<String>> clustering = new HashMap<String, ArrayList<String>>();
	
	public static void clusteNouns(){
		DBUtil db1 = new DBUtil();
		Logger.getInstance().getElapseTime(true);
		String sql = "select * from extractions";
		ResultSet rs1 = db1.executeQuerySQL(sql);
		try{
			while(rs1.next()){
				int sentIndex = rs1.getInt("sentindex");
				String attr = rs1.getString("attr");
				String value = rs1.getString("value");
				if(extractionHash.containsKey(attr)){
					if(extractionHash.get(attr).containsKey(value)){
						extractionHash.get(attr).put(value, extractionHash.get(attr).get(value)+1);
					}else{
						extractionHash.get(attr).put(value, 1);
					}
				}else{
					Map<String, Integer> tmp = new HashMap<String, Integer>();
					tmp.put(value, 1);
					extractionHash.put(attr, tmp);
				}
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Logger.getInstance().getElapseTime(true);
		attrList = new ArrayList<String>();
		sql = "select DISTINCT attr from extractions";
		rs1 = db1.executeQuerySQL(sql);
		try{
			while(rs1.next()){
				String attr = rs1.getString("attr");
				attrList.add(attr);
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		valList = new ArrayList<String>();
		sql = "select DISTINCT value from extractions";
		rs1 = db1.executeQuerySQL(sql);
		try{
			while(rs1.next()){
				String val = rs1.getString("value");
				valList.add(val);
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Logger.getInstance().getElapseTime(true);
		db1.rundown();
		OutputtoCytoscapeFile("output.csv");
	}
	
	public static void OutputToWekaFile(String path){
		IOOperator.getInstance().writeToFileUTF8(path, "% ARFF file for the weather data with some numric features \n", true);
		IOOperator.getInstance().writeToFileUTF8(path, "% \n", true);
		IOOperator.getInstance().writeToFileUTF8(path, "@relation wordpairs \n", true);

		for(String tmp:valList)
			IOOperator.getInstance().writeToFileUTF8(path, "@attribute "+ tmp +" numeric\n", true);
		IOOperator.getInstance().writeToFileUTF8(path, "@DATA \n", true);
		int i = 0;
		for(Entry<String, Map<String, Integer>> entry: extractionHash.entrySet()){
			String content = "";
			Map<String, Integer> curAdjMap = entry.getValue();
			int counter = 0;
			for(String tmp:valList){
				if(curAdjMap.containsKey(tmp)){
					content += curAdjMap.get(tmp);
					counter++;
				}else
					content +=0;
				content += ",";
			}
			if(counter <= 5){
				System.out.println(entry.getKey());
				continue;
			}
			i++;
			content = content.substring(0, content.length()-1);
			IOOperator.getInstance().writeToFileUTF8(path, content + "\n", true);
		}
		System.out.println(i);
	}
	
	public static void OutputtoCytoscapeFile(String path){
		for(Entry<String, Map<String, Integer>> entry: extractionHash.entrySet()){
			Map<String, Integer> curAdjMap = entry.getValue();
			for(Entry<String, Integer> subEntry : curAdjMap.entrySet()){
				IOOperator.getInstance().writeToFileUTF8(path, entry.getKey() + "\t" + subEntry.getKey() + "\t" + subEntry.getValue() + "\n", true);
			}
		}
	}
}
