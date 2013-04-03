import java.io.File;
import java.util.Scanner;

import utils.DataManager;
import utils.ParameterSetting;


public class ReviewQueryGUI {
	static Scanner in = new Scanner(System.in);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("--------------Help Menu-------");
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
			
		}else{
			//process_new_command(context + s);
			return;
		}
	}
	
	public static void output_nouns_by_tfidf(String s){
		
	}
	
	public static void output_nouns_by_tf(String s){
		
	}
	
	public static void output_summary(String s){
		
	}
	
	public static void printRestaurantNameList(){
		int i = 0 ;
		for(File input : DataManager.getFilesUnderFolder(ParameterSetting.PATHTOCRAWLEDDATA3)){
			System.out.println(i + " : \t"+ input.getName());
			i++;
		}
		System.out.println("Select restaurant by index:");
		wait_new_command("restaurants_");
	}
}
