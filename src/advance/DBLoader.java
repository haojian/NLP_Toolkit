package advance;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import snowballstemmer.SnowballStemmer;
import snowballstemmer.ext.englishStemmer;
import utils.DBUtil;
import utils.Logger;
import utils.TextUtil;
import data_structure.Extraction;
import data_structure.RestaurantEntry;
import data_structure.ReviewEntry;

public class DBLoader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBLoader.getInstance().getClusterAttribute();
	}

	static SnowballStemmer stemmer = (SnowballStemmer) new englishStemmer();
	public static DBLoader singleton;
	
	public static DBLoader getInstance(){
		if(singleton == null)
			singleton = new DBLoader();
		return singleton;
	}
	
	private static Map<String, RestaurantEntry> dataHash;
	private static Map<String, ArrayList<String>> clusterAttribute;
	
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
				stemmer.setCurrent(attr);
				stemmer.stem();
				attr = stemmer.getCurrent();
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
					return;
				}
			}
			}
		}
	}

	public static void LoadClusteringReults(){
		try {
			clusterAttribute.put("decor", new ArrayList<String>());
			BufferedReader tmp = new BufferedReader(new FileReader("clusteringresult/decor.txt"));
			String line = "";
			while((line=tmp.readLine())!=null){
				if(line.length() != 0)
					clusterAttribute.get("decor").add(line);
			}
			
			clusterAttribute.put("food", new ArrayList<String>());
			tmp = new BufferedReader(new FileReader("clusteringresult/food.txt"));
			line = "";
			while((line=tmp.readLine())!=null){
				if(line.length() != 0)
					clusterAttribute.get("food").add(line);
			}
			
			clusterAttribute.put("overall", new ArrayList<String>());
			tmp = new BufferedReader(new FileReader("clusteringresult/overall.txt"));
			line = "";
			while((line=tmp.readLine())!=null){
				if(line.length() != 0)
					clusterAttribute.get("overall").add(line);
			}
			
			clusterAttribute.put("price", new ArrayList<String>());
			tmp = new BufferedReader(new FileReader("clusteringresult/price.txt"));
			line = "";
			while((line=tmp.readLine())!=null){
				if(line.length() != 0)
					clusterAttribute.get("price").add(line);
			}
			
			clusterAttribute.put("service", new ArrayList<String>());
			tmp = new BufferedReader(new FileReader("clusteringresult/service.txt"));
			line = "";
			while((line=tmp.readLine())!=null){
				if(line.length() != 0)
					clusterAttribute.get("service").add(line);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	public Map<String, RestaurantEntry> getDataHash() {
		if(dataHash == null){
			dataHash = new HashMap<String, RestaurantEntry>();
			LoadReviewsFromSQL();
		}
		return dataHash;
	}

	public static ArrayList<String> GetValueList(){
		ArrayList<String> res = new ArrayList<String>();
		DBUtil db1 = new DBUtil();
		Logger.getInstance().getElapseTime(true);
		String sql = "select DISTINCT value from extractions";
		ResultSet rs1 = db1.executeQuerySQL(sql);
			try {
				while(rs1.next()){
					String val = rs1.getString("value");
					res.add(val);
				}
				return res;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		db1.rundown();
		return null;
	}

	public Map<String, ArrayList<String>> getClusterAttribute() {
		if(clusterAttribute == null){
			clusterAttribute = new HashMap<String, ArrayList<String>>();
			LoadClusteringReults();
			//System.out.println(clusterAttribute.get("decor"));
		}
		return clusterAttribute;
	}
}
