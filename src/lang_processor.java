import java.util.Scanner;


public class lang_processor {
	static Scanner in = new Scanner(System.in);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
	
	public static void print_menu(){
		
		System.out.println("--------------Help Menu-------");
		System.out.println("1. Sentence Splitting.");
		System.out.println("2. Frequence Calculator.");
		System.out.println("3. Sentiment Analysis.");
		
		System.out.println("Input your command:");
		wait_new_command();
	}
	
	
	public static void wait_new_command(){
		String s;
		s = in.nextLine();
		process_new_command(s);
	}
	
	public static void process_new_command(String s){
		
	}
}
