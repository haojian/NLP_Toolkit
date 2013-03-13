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
	
	public long getElapseTime(boolean ifoutput){
		long tmp = System.currentTimeMillis();
		long res = tmp - lasttime;
		lasttime = tmp;
		if(ifoutput)
			System.out.println("elapse time: " + res + "\n");
		return res;
	}
	
	
	public void reportProcess(long donepart, long all, String event){
		float precess = donepart/all;
		System.err.println(event + " current progress: " + (float)donepart/all + "\t time: " + getElapseTime(false));
	}
}
