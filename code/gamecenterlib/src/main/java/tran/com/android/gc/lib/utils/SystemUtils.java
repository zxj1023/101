package tran.com.android.gc.lib.utils;

public class SystemUtils {
	
	/**
	 * get current sdk version
	 * @return
	 */
	public static int getCurrentVersion(){
		
		return android.os.Build.VERSION.SDK_INT;
	}
	
	public static String getMode(){
		return android.os.Build.MODEL;
	}

}
