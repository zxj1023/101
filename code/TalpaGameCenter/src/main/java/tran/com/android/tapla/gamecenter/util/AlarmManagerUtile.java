package tran.com.android.tapla.gamecenter.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;



/**
 * TODO<what to do>
 */
public class AlarmManagerUtile {
    private static AlarmManager getAlarmManager(Context ctx) {
        return (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
    }

    private static final String TAG = "AlarmManagerUtile";

    /**
     * 设置自动升级的路径设置
     *
     * @param ctx
     */
    public static void sendAutoUpdateBroadcast(Context ctx) {
        //
        /*//得到日历实例，主要是为了下面的获取时间
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        //系统当前的时间值
        long systemTime = System.currentTimeMillis();
        //是设置日历的时间，主要是让日历的年月日和当前同步
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        // mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //设置提醒的时间点
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        //
        long selectTime = mCalendar.getTimeInMillis();
        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (systemTime > selectTime) {
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }*/
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("auto_alarm_check",
                true);
        ed.commit();
        AlarmManager am = getAlarmManager(ctx);
        Intent i = new Intent("AUTO_UPDATE_CLOCK");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 24 * 60 * 60 * 36, pendingIntent);
    }

}
