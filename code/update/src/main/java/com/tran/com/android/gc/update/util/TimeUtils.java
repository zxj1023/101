/*
 * @author  zw
 */
package com.tran.com.android.gc.update.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
	private static final String TAG = "TimeUtils";
	public static final String DATE_TIME_FORMAT = "%04d-%02d-%02d %02d:%02d:%02d";
	public static final String DATE_TIME_FORMAT_OTH = "%04d-%02d-%02d %02d:%02d";
	public static final String DATA_FORMAT = "%04d-%02d-%02d";
	public static final String TIME_FORMAT = "%02d:%02d:%02d";
	private static StringBuilder mFormatBuilder = new StringBuilder();
	private static java.util.Formatter mFormatter = new java.util.Formatter(
			mFormatBuilder, Locale.getDefault());
	/**
	 * * 获取现在时间 * * @return返回短时间格式 yyyy-MM-dd
	 */
	public static String getStringDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * * 获取现在时间 *
	 * 
	 * 
	 * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
	 * */
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	
	public static String getStringByDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd - HHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getCurrentDateTime() {
		Calendar calendar = Calendar.getInstance();

		return String.format(DATE_TIME_FORMAT, calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
	}

	public static String getCurrentTime() {
		Calendar calendar = Calendar.getInstance();

		return String.format(TIME_FORMAT, calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
	}


	public static String getDateTimeFromLong(long milliseconds) {
		// update Integrate message 相差一个月问题
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliseconds);
		return String.format(DATE_TIME_FORMAT, calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
	}

	public static String getDataTimeFromLongOth(long milliseconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliseconds);
		return String.format(DATE_TIME_FORMAT_OTH, calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE));
	}

	public static long getLongFromDateTime(String dataTime) {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date begin = null;
		try {
			begin = dfs.parse(dataTime);
		} catch (ParseException e) {
			FileLog.e(TAG, e.toString());
			e.printStackTrace();
		}
		if (null != begin)
			return begin.getTime();
		else
			return 0;
	}

	public static long getLongFromStrTime(String dataTime) {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date begin = null;
		try {
			begin = dfs.parse(dataTime);
		} catch (ParseException e) {
			FileLog.e(TAG, e.toString());
			e.printStackTrace();
		}
		if (null != begin)
			return begin.getTime();
		else
			return 0;
	}

	public static long getLongFromStrTime1(String dataTime) {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
		Date begin = null;
		try {
			begin = dfs.parse(dataTime);
		} catch (ParseException e) {
			FileLog.e(TAG, e.toString());
			e.printStackTrace();
		}
		if (null != begin)
			return begin.getTime();
		else
			return 0;
	}
	
	/* 得到小时分钟 */
	public static String getDisTime(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = sdf.parse(time, pos);
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		String dateString = formatter.format(strtodate);
		return dateString;
	}

	public static int getDistanceDays(String dataTime) {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date begin = null, end = null;
		try {
			begin = dfs.parse(dataTime);
			end = dfs.parse(getCurrentDateTime());
		} catch (ParseException e) {
			e.printStackTrace();
			FileLog.e(TAG, e.toString());
		}

		if (null != begin && end != null) {
			long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒

			long day = between / (24 * 3600);

			return (int) day;
		} else
			return 0;

	}

	/**
	 * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param strDate
	 * @return
	 */
	public static String strToDateLong(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = sdf.parse(strDate, pos);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(strtodate);
		return dateString;
	}

	/* 得到月日 */
	public static String getDateTime(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = sdf.parse(time, pos);
		SimpleDateFormat formatter = new SimpleDateFormat("M月dd日");
		String dateString = formatter.format(strtodate);
		return dateString;
	}
	
	public static String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  E  HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return sdf.format(calendar.getTime());
    }
	

	
	public static String getTimeStr(long timeMs) {
		long totalSeconds = timeMs % 1000 >= 500 ? (timeMs / 1000) + 1
				: timeMs / 1000;

		long seconds = totalSeconds % 60;
		long minutes = (totalSeconds / 60) % 60;
		// int hours = totalSeconds / 3600;
		mFormatBuilder.setLength(0);
		/*
		 * return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds)
		 * .toString();
		 */
		return mFormatter.format("%02d:%02d", minutes, seconds).toString();
	}
	
	public static String getDateStr(long timeMs) {
		long totalSeconds = timeMs % 1000 >= 500 ? (timeMs / 1000) + 1
				: timeMs / 1000;

		long seconds = totalSeconds % 60;
		long minutes = (totalSeconds / 60) % 60;
		long hours = totalSeconds / 3600;
		mFormatBuilder.setLength(0);

		return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds)
		  .toString();

	}

	public static String getFormatDate(long pMilliseconds){
	       Calendar calendar = Calendar.getInstance();
	        calendar.setTimeInMillis(pMilliseconds);
	        return String.format(DATA_FORMAT, calendar.get(Calendar.YEAR),
	                calendar.get(Calendar.MONTH) + 1,
	                calendar.get(Calendar.DAY_OF_MONTH));
	}
}
