package jsqueak;

import sun.rmi.runtime.Log;

public class Logger {
	private static Logger mLogger;
	private Log mLog;
	
	public boolean DEBUG;
	
	private Logger() {
		DEBUG = false;
		//mLog = Log.getLog(arg0, arg1, arg2);
	}
	
	public static Logger getLogger() {
		
		
		return mLogger;
	}
	
	public void debug(String message) {
		mLog.log(Log.BRIEF, message);
	}
}
