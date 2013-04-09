package advance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import utils.MathCalc;

import data_structure.RestaurantEntry;

public class AttributeCluster {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AttributeCluster.getInstance().ClusterAttribute();
	}
	
	public static AttributeCluster singleton;
	
	public static AttributeCluster getInstance(){
		if(singleton == null)
			singleton = new AttributeCluster();
		return singleton;
	} 
	
	private String[] seed_food_list = {"food", "chicken", "dish", "lamb", "ramen", "coffee", "fruit", "pizza"};
	private String[] seed_service_list = {"service", "wait", "waiting", "line"};
	private String[] seed_decor_list = {"atmosphere", "view", "place", "outdoor"};
	private String[] seed_price_list = {"price", "money", "tip"};
	
	
	public Map<String, Map<String, Double>> attrVectorSpace = new HashMap<String, Map<String, Double>>();
	public Map<String, Map<String, Double>> seedVectorSpace = new HashMap<String, Map<String, Double>>();
	ArrayList<String> valueList;

	
	public  void LoadData(){
		Map<String, RestaurantEntry> dataHash = DBLoader.getInstance().getDataHash();
		valueList = DBLoader.getInstance().GetValueList();
		//put the datahash into attrVectorSpace;
		for(Map.Entry<String, RestaurantEntry> restaurant:dataHash.entrySet()){
			for(Map.Entry<String, Integer> keyPhrase: restaurant.getValue().getphrasemap().getSortedMap().entrySet()){
				String[] key = keyPhrase.getKey().split("\t");
				String attr = key[1];
				String value = key[0];
				if(attrVectorSpace.containsKey(attr)){
					if(attrVectorSpace.get(attr).containsKey(value)){
						attrVectorSpace.get(attr).put(value, keyPhrase.getValue()+ attrVectorSpace.get(attr).get(value));
					}else{
						attrVectorSpace.get(attr).put(value, (double)keyPhrase.getValue());
					}
				}else{
					attrVectorSpace.put(attr, new HashMap<String, Double>());
					attrVectorSpace.get(attr).put(value, (double)keyPhrase.getValue());
				}
			}
		}
		for(Map.Entry<String, Map<String, Double>> attrVector: attrVectorSpace.entrySet()){
			double total = 0;;
			for(Entry<String, Double> vector : attrVector.getValue().entrySet()){
				total += vector.getValue();
			}
			for(Entry<String, Double> vector : attrVector.getValue().entrySet()){
				attrVector.getValue().put(vector.getKey(), vector.getValue()/total);
			}
		}
	}
	
	public  void InitSeedVector(String category, String[]list){
		seedVectorSpace.put(category, new HashMap<String, Double>());
		for(String tmp:list){
			Map<String, Double> vector = attrVectorSpace.get(tmp);
			System.out.println(tmp);
			for(Map.Entry<String, Double> entry:vector.entrySet()){
				if(seedVectorSpace.get(category).containsKey(entry.getKey())){
					seedVectorSpace.get(category).put(entry.getKey(), seedVectorSpace.get(category).get(entry.getKey()) + ((double)entry.getValue())/list.length);
				}else{
					seedVectorSpace.get(category).put(entry.getKey(), ((double)entry.getValue())/list.length);
				}
			}
		}
		//normalization to 0-1;
		double total = 0;;
		for(Map.Entry<String, Double> vector : seedVectorSpace.get(category).entrySet()){
			total += vector.getValue();
		}
		for(Map.Entry<String, Double> vector : seedVectorSpace.get(category).entrySet()){
			seedVectorSpace.get(category).put(vector.getKey(),  vector.getValue()/total);
		}
	}
	
	public  double[] IntMapToVectorArray(Map<String, Integer> map){
		ArrayList<String> valueList = DBLoader.getInstance().GetValueList();
		double[] array = new double[valueList.size()];
		for(int i = 0; i<valueList.size(); i++){
			if(map.containsKey(array[i])){
				array[i] = map.get(array[i]);
			}else{
				array[i]  = 0;
			}
		}
		return array;
	}

	public  double[] DoubleMapToVectorArray(Map<String, Double> map){
		
		double[] array = new double[valueList.size()];
		for(int i = 0; i<valueList.size(); i++){
			if(map.containsKey(valueList.get(i))){
				array[i] = map.get(valueList.get(i));
			}else{
				array[i]  = 0;
			}
		}
		return array;
	}
	
	public  void ClusterAttribute(){
		LoadData();
		InitSeedVector("food", seed_food_list);
		InitSeedVector("service", seed_service_list);
		InitSeedVector("decor", seed_decor_list);
		InitSeedVector("price", seed_price_list);
		
		for(Map.Entry<String, Map<String, Double>> attrVector: attrVectorSpace.entrySet()){
			double weight1 = MathCalc.klDivergence(DoubleMapToVectorArray(attrVector.getValue()), DoubleMapToVectorArray(seedVectorSpace.get("food")));
			double weight2 = MathCalc.klDivergence(DoubleMapToVectorArray(attrVector.getValue()), DoubleMapToVectorArray(seedVectorSpace.get("service")));
			double weight3 = MathCalc.klDivergence(DoubleMapToVectorArray(attrVector.getValue()), DoubleMapToVectorArray(seedVectorSpace.get("decor")));
			double weight4 = MathCalc.klDivergence(DoubleMapToVectorArray(attrVector.getValue()), DoubleMapToVectorArray(seedVectorSpace.get("price")));
			
			System.out.println(attrVector.getKey() + "\t" + weight1 + "\t" + weight2 + "\t" + weight3 + "\t" + weight4);
		}
	}
}
