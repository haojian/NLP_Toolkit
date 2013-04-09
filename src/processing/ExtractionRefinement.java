package processing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import utils.DBUtil;
import utils.Logger;
import data_structure.Extraction;

public class ExtractionRefinement {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		mergeDuplicateExtractionsFromSQL();
	}
	
	//Map<Attr, Map<Value, Counter>>.
	private static Map<String, Map<String, Integer>> extractionHash = new HashMap<String, Map<String, Integer>>();
	private static Map<Extraction, Extraction> mappingHash = new HashMap<Extraction, Extraction>();
	
	
	public static void mergeDuplicateExtractionsFromSQL(){
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
		sql = "select DISTINCT value, attr from extractions";
		rs1 = db1.executeQuerySQL(sql);
		try{
			while(rs1.next()){
				String attr = rs1.getString("attr");
				String value = rs1.getString("value");
				int original = extractionHash.get(attr).get(value);
				int reverse  = 0;
				if(extractionHash.containsKey(value) && extractionHash.get(value).containsKey(attr))
					reverse = extractionHash.get(value).get(attr);
				if(original<=reverse){
					mappingHash.put(new Extraction(value, attr), new Extraction(attr, value));
				}
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println(mappingHash.size());
		Logger.getInstance().getElapseTime(true);

		
		for(Map.Entry<Extraction, Extraction> entry : mappingHash.entrySet()){
			sql = "UPDATE extractions SET value='" +entry.getKey().getAttr().get_txt()
					+ "', attr = '" + entry.getKey().getVal().get_txt() + "' WHERE value = '" +
					entry.getKey().getVal().get_txt() + "' AND attr = '" + entry.getKey().getAttr().get_txt()+"'";
			System.out.println( db1.executeUpdateSQL(sql));
		}
		db1.rundown();
	}
	
	



}
