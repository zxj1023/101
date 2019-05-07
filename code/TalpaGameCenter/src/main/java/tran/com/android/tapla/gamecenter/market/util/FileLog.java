package tran.com.android.tapla.gamecenter.market.util;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
  
/**  
 * 带日志文件输入的，又可控开关的日志调试  
 *   
 * @author BaoHang  
 * @version 1.0  
 * @data 2012-2-20  
 */  
public class FileLog {   
	private static final String TAG = "MyLog";
    private static Boolean MYLOG_SWITCH=true; // 日志文件总开关   
    private static Boolean MYLOG_WRITE_TO_FILE=true;// 日志写入文件开关   
    private static char MYLOG_TYPE='v';// 输入日志类型，w代表只输出告警信息等，v代表输出所有信息   
    private static String MYLOG_PATH_SDCARD_DIR="/sdcard/market/log";// 日志文件在sdcard中的路径   
    private static int SDCARD_LOG_FILE_SAVE_DAYS = 7;// sd卡中日志文件的最多保存天数   
    private static String MYLOGFILEName = "Log.txt";// 本类输出的日志文件名称   
    
    private  static StackTraceElement ste = new Throwable().getStackTrace()[1];
    private static SimpleDateFormat myLogSdf = new SimpleDateFormat(   
            "yyyy-MM-dd HH:mm:ss");// 日志的输出格式   
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式   
  
    public static void w(String tag, Object msg) { // 警告信息   
        log(tag, msg.toString(), 'w');   
    }   
  
    public static void e(String tag, Object msg) { // 错误信息   
        log(tag, msg.toString(), 'e');   
    }   
  
    public static void d(String tag, Object msg) {// 调试信息   
        log(tag, msg.toString(), 'd');   
    }   
  
    public static void i(String tag, Object msg) {//   
        log(tag, msg.toString(), 'i');   
    }   
  
    public static void v(String tag, Object msg) {   
        log(tag, msg.toString(), 'v');   
    }   
  
    public static void w(String tag, String text) {   
        log(tag, text, 'w');   
    }   
  
    public static void e(String tag, String text) {   
        log(tag, text, 'e');   
    }   
  
    public static void d(String tag, String text) {   
        log(tag, text, 'd');   
    }   
  
    public static void i(String tag, String text) {   
        log(tag, text, 'i');   
    }   
  
    public static void v(String tag, String text) {   
        log(tag, text, 'v');   
    }   
  
    /**  
     * 根据tag, msg和等级，输出日志  
     *   
     * @param tag  
     * @param msg  
     * @param level  
     * @return void  
     * @since v 1.0  
     */  
    private static void log(String tag, String msg, char level) {   
        if (MYLOG_SWITCH) {  
        	msg = "  the line "+ste.getLineNumber() + msg;
            if ('e' == level && ('e' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) { // 输出错误信息   
                Log.e(tag, msg);   
            } else if ('w' == level && ('w' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {   
                Log.w(tag, msg);   
            } else if ('d' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {   
                Log.d(tag, msg);   
            } else if ('i' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {   
                Log.i(tag, msg);   
            } else {   
                Log.v(tag, msg);   
            }   
            if (MYLOG_WRITE_TO_FILE)   
                writeLogtoFile(String.valueOf(level), tag, msg);   
        }   
    }   
  
    /**  
     * 打开日志文件并写入日志  
     *   
     * @return  
     * **/  
    private static void writeLogtoFile(String mylogtype, String tag, String text) {// 新建或打开日志文件   
        Date nowtime = new Date();   
        String needWriteFiel = logfile.format(nowtime);   
        String needWriteMessage = myLogSdf.format(nowtime) + "    " + mylogtype   
                + "    " + tag + "    " + text;  
        File path = new File(MYLOG_PATH_SDCARD_DIR);
        if (!path.exists())
        {
        	path.mkdirs();
        }
        File file = new File(MYLOG_PATH_SDCARD_DIR, needWriteFiel   
                + MYLOGFILEName); 
        FileWriter filerWriter = null;
        BufferedWriter bufWriter = null;
        try {   
            filerWriter = new FileWriter(file, true);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖   
            bufWriter = new BufferedWriter(filerWriter);   
            bufWriter.write(needWriteMessage);   
            bufWriter.newLine();    
        } catch (IOException e) {   
            // TODO Auto-generated catch block   
            e.printStackTrace();   
            //Log.e(TAG, e.toString());
        }finally{
        	try {
				if(bufWriter != null){
					bufWriter.close();
				}
				if(filerWriter != null){
        			filerWriter.close();
        		}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//Log.e(TAG, e.toString());
			}
        }
    }   
  
    /**  
     * 删除制定的日志文件  
     * */  
    public static void delFile() {// 删除日志文件   
    	try
    	{
        String needDelFiel = logfile.format(getDateBefore()); 
        Long needtime = TimeUtils.getLongFromStrTime1(needDelFiel);
        File srcDir = new File(MYLOG_PATH_SDCARD_DIR);
        File[] files = srcDir.listFiles();
        for(int i  = 0; i < files.length; i ++)
        {
        	String tmpname = files[i].getName();
        	int index = tmpname.indexOf(MYLOGFILEName, 0);
        	if(index != -1)
        	{
        		String name = tmpname.substring(0, index);
        		Log.i(TAG, "the name="+name);
        		Long nowtime = TimeUtils.getLongFromStrTime1(name);
        		if(nowtime <= needtime)
        		{
        			 File file = new File(MYLOG_PATH_SDCARD_DIR, tmpname);   
        		        if (file.exists()) {   
        		            file.delete();   
        		        }   
        		}
        		
        	}
        }
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
        
      /*  File file = new File(MYLOG_PATH_SDCARD_DIR, needDelFiel + MYLOGFILEName);   
        if (file.exists()) {   
            file.delete();   
        }   */
    }   
  
    /**  
     * 得到现在时间前的几天日期，用来得到需要删除的日志文件名  
     * */  
    private static Date getDateBefore() {   
        Date nowtime = new Date();   
        Calendar now = Calendar.getInstance();   
        now.setTime(nowtime);   
        now.set(Calendar.DATE, now.get(Calendar.DATE)   
                - SDCARD_LOG_FILE_SAVE_DAYS);   
        return now.getTime();   
    } 
  
}