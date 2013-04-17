package advance;

import java.util.ArrayList;
import java.util.Map;

import data_structure.RestaurantEntry;

public class TF_Calc {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//getTopNounsinSingleRestaurant("Amber India Restaurant_4.0_736");
		getstructuredDescinSingleRestaurant("Amber India Restaurant_4.0_736", 10);
	}
	
	public static ArrayList<String> getTopAttributessinSingleRestaurant(String s, int n){
		Map<String, RestaurantEntry> dataHash = DBLoader.getInstance().getDataHash();

		if(dataHash.containsKey(s)){
			return dataHash.get(s).getTopNounsbyTF(10);
		}else{
			System.out.println("unknown restaurant query.");
			return null;
		}
	}
	
	public static ArrayList<String> getTopDescriptionsinSingleRestaurant(String s, int n){
		Map<String, RestaurantEntry> dataHash = DBLoader.getInstance().getDataHash();

		if(dataHash.containsKey(s)){
			return dataHash.get(s).getTopKeyPhrasebyTF(10);
		}else{
			System.out.println("unknown restaurant query.");
			return null;
		}
	}
	
	public static Map<String, ArrayList<String>> getstructuredDescinSingleRestaurant(String s, int n){
		Map<String, RestaurantEntry> dataHash = DBLoader.getInstance().getDataHash();

		if(dataHash.containsKey(s)){
			//System.out.println(dataHash.get(s).getStructuredPhrasebyTF(10));
			return dataHash.get(s).getStructuredPhrasebyTF(10);
		}else{
			System.out.println("unknown restaurant query.");
			return null;
		}
	}
}
