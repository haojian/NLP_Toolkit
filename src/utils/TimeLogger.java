package utils;

public class TimeLogger {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static TimeLogger singleton;
	
	public static TimeLogger getInstance(){
		if(singleton == null)
			singleton = new TimeLogger();
		return singleton;
	}

	private long lasttime = -1;
	public TimeLogger(){
		lasttime = System.currentTimeMillis();
	}
	
	public void getElapseTime(){
		long tmp = System.currentTimeMillis();
		long res = tmp - lasttime;
		lasttime = tmp;
		System.out.println("elapse time: " + res + "\n");
	}
}
