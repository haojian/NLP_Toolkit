package advance;

import java.util.Map;

import data_structure.RestaurantEntry;

public class TF_Calc {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getTopNounsinSingleRestaurant("Amber India Restaurant_4.0_736");
	}
	
	public static void getTopNounsinSingleRestaurant(String s){
		Map<String, RestaurantEntry> dataHash = DBLoader.getInstance().getDataHash();
		/*
		for(Map.Entry<String , RestaurantEntry> entry: dataHash.entrySet()){
			System.out.println(entry.getKey());
		}
		*/
		if(dataHash.containsKey(s)){
			System.out.println(dataHash.get(s).getTopNounsbyTF(10));
			System.out.println(dataHash.get(s).getTopKeyPhrasebyTF(10));
		}else{
			System.out.println("unknown restaurant query.");
		}
	}

}
