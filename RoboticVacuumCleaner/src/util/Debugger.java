package util;

public class Debugger {

    private static boolean mode = true;
    
    private static LogActivity logActivity = new LogActivity();

    public static void log(Object o) {
        if (mode)
            System.out.println(o.toString());
        
		logActivity.loggingCleaning(o.toString());
    }

	static boolean getMode() {
        return mode;
    }

}