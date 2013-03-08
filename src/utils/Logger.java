package utils;

public class Logger {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static Logger singleton;
	
	public static Logger getInstance(){
		if(singleton == null)
			singleton = new Logger();
		return singleton;
	}

	private long lasttime = -1;
	public Logger(){
		lasttime = System.currentTimeMillis();
	}
	
	public void getElapseTime(){
		long tmp = System.currentTimeMillis();
		long res = tmp - lasttime;
		lasttime = tmp;
		System.out.println("elapse time: " + res + "\n");
	}
}
