package utils;

public class IOOperator {
	private static IOOperator singleton;
	private FileWriter writer;
	public String writeToFilePath;

	public IOOperator(){
		writeToFilePath = "";
	}
	
	public static IOOperator getInstance() {
		if (singleton == null)
			singleton = new IOOperator();
		return singleton;
	}
	
	public void closeStream(){
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeToFile(String filename, String content, boolean isappend){
		try {
			writer = new FileWriter(filename, isappend);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
