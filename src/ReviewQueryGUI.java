import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import advance.DBLoader;
import advance.TF_Calc;

import utils.DataManager;
import utils.ParameterSetting;


public class ReviewQueryGUI {
	static Scanner in = new Scanner(System.in);
	static ArrayList<String> resturantNames  = new ArrayList<String>();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("--------------Help Menu-------");
		System.out.println("0. Load Data.");
		System.out.println("1. Restaurant Query.");
		
		System.out.println("Input your command:");
		wait_new_command("main_");
	}
	
	public static void wait_new_command(String context){
		String s ="";
		
		while(s.trim().length() == 0){
			s = in.nextLine();
		}
		process_new_command(context + s);
	}
	
	public static void process_new_command(String s){
		if(s.startsWith("main_")){
			s = s.replace("main_", "");
			int i = Integer.valueOf(s.trim());
			processMain(i);
		}
		else if(s.startsWith("restaurants_")){
			//do something.
			s = s.replace("restaurants_", "");
			int i = Integer.valueOf(s.trim());
			viewRestaurant(i);
			
		}else if(s.startsWith("back_")){
			System.out.println("--------------Help Menu-------");
			System.out.println("0. Load Data.");
			System.out.println("1. Restaurant Query.");
			
			System.out.println("Input your command:");
			wait_new_command("main_");
		}
		else if(s.startsWith("Loaddata_")){
			s = s.replace("Loaddata_", "");
			int i = Integer.valueOf(s.trim());
			loadData(i);
			return;
		}
	}
	
	public static void processMain(int i){
		if(i == 1){
			printRestaurantNameList();
		}else if(i==0){
			printLoadDataMenu();
		}
	}
	
	public static void loadData(int i){
		if(i==0){
			System.err.println(DBLoader.getInstance().getDataHash().size() + " restaurants loaded..");
		}else if (i==1){
			System.err.println(DBLoader.getInstance().getClusterAttribute().size() + " categories loaded..");
			System.err.println("food: " + DBLoader.getInstance().getClusterAttribute().get("food").size());
			System.err.println("decor: " + DBLoader.getInstance().getClusterAttribute().get("decor").size());
			System.err.println("overall: " + DBLoader.getInstance().getClusterAttribute().get("overall").size());
			System.err.println("service: " + DBLoader.getInstance().getClusterAttribute().get("service").size());
			System.err.println("price: " + DBLoader.getInstance().getClusterAttribute().get("price").size());
		}
		System.out.println("press anykey to return to main menu.");
		wait_new_command("back_");
	}
	
	public static void viewRestaurant(int i){
		System.out.println("--------------Help Menu-------");
		System.out.println("Query for summarization of " + resturantNames.get(i));
		System.out.println("0. Print top mentioned attributes.");
		System.out.println("1. Print top mentioned value-attribute word pairs.");
		System.out.println("2. Print Summarization based on categories.");
		output_summary(resturantNames.get(i));
	}
	
	
	public static void output_nouns_by_tfidf(String s){
		
	}
	
	public static void output_nouns_by_tf(String s){
		
	}
	
	public static void output_summary(String s){
		//TF_Calc.getTopNounsinSingleRestaurant(s);
		System.out.println("Press any key to return to the main menu.");
		wait_new_command("back_");
	}
	
	public static void printRestaurantNameList(){
		int i = 0 ;
		for(File input : DataManager.getFilesUnderFolder(ParameterSetting.PATHTOCRAWLEDDATA3)){
			System.out.println(i + " : \t"+ input.getName());
			resturantNames.add(input.getName());
			i++;
		}
		System.out.println("Select restaurant by index:");
		wait_new_command("restaurants_");
	}
	
	public static void printLoadDataMenu(){
		System.out.println("--------------Help Menu-------");
		System.out.println("0. Load review extractions.");
		System.out.println("1. Load clustering result.");
		
		System.out.println("Input your command:");
		wait_new_command("Loaddata_");
	}
}
