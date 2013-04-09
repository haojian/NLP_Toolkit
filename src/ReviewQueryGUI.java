import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

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
		String s;
		s = in.nextLine();
		process_new_command(context + s);
	}
	
	public static void process_new_command(String s){
		if(s.equals("main_1")){
			printRestaurantNameList();
		}else if(s.startsWith("restaurants_")){
			//do something.
			s = s.replace("restaurants_", "");
			int i = Integer.valueOf(s.trim());
			output_summary(resturantNames.get(i));
		}else if(s.startsWith("back_")){
			System.out.println("--------------Help Menu-------");
			System.out.println("0. Load Data.");
			System.out.println("1. Restaurant Query.");
			
			System.out.println("Input your command:");
			wait_new_command("main_");
		}
		else{
			//process_new_command(context + s);
			return;
		}
	}
	
	public static void output_nouns_by_tfidf(String s){
		
	}
	
	public static void output_nouns_by_tf(String s){
		
	}
	
	public static void output_summary(String s){
		TF_Calc.getTopNounsinSingleRestaurant(s);
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
}
