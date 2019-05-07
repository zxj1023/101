package tran.com.android.gc.lib.provider;

import android.net.Uri;
import android.os.Bundle;
import android.content.IContentProvider;
import android.content.ContentResolver;
import android.util.Log;

import android.os.Build;
import java.lang.reflect.Method;

public class AuroraSettings {
    private static final String TAG = "AuroraSettings";

    public static final String AMIGO_PROP_SETTING_VERSION = "sys.settings_aurora_version";
    public static final String AUTHORITY = "aurorasettings";
    public static final String TABLE_CONFIG = "config";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_CONFIG);
    
    private static Object sLock = new Object();
    
    private static IContentProvider mAuroraContentProvider = null;
    public static final String CALL_METHOD_GET_CONFIG = "GET_config";
    public static final String CALL_METHOD_SET_CONFIG = "SET_config";
    public static final String NAME = "name";
    public static final String VALUE = "value";
    
    //Gionee fengjianyi 2012-08-22 add for CR00673800 start
    public static final String FLIP_SOUNDS_ENABLED = "gn_flip_sounds_enabled";
    public static final String FLIP_ON_SOUND = "gn_flip_on_sound";
    public static final String FLIP_OFF_SOUND = "gn_flip off_sound";
    //Gionee fengjianyi 2012-08-22 add for CR00673800 end
    
    //Gionee:zhang_xin 2012-12-09 add for start
    public static final String POWER_SAVER = "gn_power_saver";
    //Gionee:zhang_xin 2012-12-09 add for end
    
    //Gionee <zhang_xin><2013-03-26> add for CR00788411 begin          
    public static final String FONT_SIZE = "gn_font_size";
    //Gionee <zhang_xin><2013-03-26> add for CR00788411 end

    //Gionee <jackycheng><2013-09-11> add for settings begin          
    public static final int FONT_SIZE_SYSTEM = 0;		
    public static final int FONT_SIZE_LARGE = 1;			
    public static final int FONT_SIZE_EXTRA_LARGE = 2;
    //Gionee <jackycheng><2013-09-11> add for settings end
    
    //gionee zengxuanhui 20121022 add for CR00716758 begin     
    public static final String HAPTIC_VIBRATION_ENABLED = "haptic_vibration_enabled";
    //gionee zengxuanhui 20121022 add for CR00716758 end
    
    //Gionee Jingjc 20121122 modify for CR00722601 begin
    public static final String SWITCH_VIBRATION_ENABLED = "switch_vibration_enabled";
    public static final String DIALPAD_VIBRATION_ENABLED = "dialpad_vibration_enabled";
    public static final String LOCKSCREEN_VIBRATION_ENABLED = "lockscreen_vibration_enabled";
    public static final String SELECTAPP_VIBRATION_ENABLED = "selectapp_vibration_enabled";
    //Gionee Jingjc 20121122 modify for CR00722601 end
    
    // Gionee <zengxuanhui> <2013-04-26> add for CR00797390 begin
    public static final String RING_VIBRATION_ENABLED = "ring_vibration_enabled";
    public static final String MMS_VIBRATION_ENABLED = "mms_vibration_enabled";
    public static final String NOTIFICATION_VIBRATION_ENABLED = "notification_vibration_enabled";
    // Gionee <zengxuanhui> <2013-04-26> add for CR00797390 end
    
    // Gionee <wangyaohui><2013-05-30> add for CR00820909 begin 
    public static final String FANFAN_WIDGET_AUTO_PUSH = "gn_fanfan_widget_auto_push";           
    // Gionee <wangyaohui><2013-05-30> add for CR00820909 end
    
    // Gionee <wangyaohui><2013-06-05> add for CR00823496 begin 
    public static final String RESPIRATION_LAMP_LOW_POWER = "gn_respirationlamp_low_power";
    public static final String RESPIRATION_LAMP_IN_CHARGE = "gn_respirationlamp_in_charge";
    public static final String RESPIRATION_LAMP_NOTIFICATION = "gn_respirationlamp_notification"; 
    public static final String RESPIRATION_LAMP_MUSIC = "gn_respirationlamp_music";
    public static final String RESPIRATION_LAMP_CALL = "gn_respirationlamp_call";                 
    // Gionee <wangyaohui><2013-06-05> add for CR00823496 end

    //Gionee <zengxh><2013-06-21> add for CR00828066 begin
    /**
    * @hide
    */		
    public static final String SSG_AUTO_DIAL = "ssg_auto_dial";
    public static final String SSG_CALL_ACCESS = "ssg_call_access";
    public static final String SSG_DELAY_ALARM = "ssg_delay_alarm";
    public static final String SSG_SWITCH_SCREEN = "ssg_switch_screen";
    public static final String SDG_CALL_ACCESS = "sdg_call_access";
    public static final String SDG_BROWSE_PHOTOS = "sdg_browse_photos";
    public static final String SDG_VIDEO_PROGRESS = "sdg_video_progress";
    public static final String SDG_VIDEO_VOLUME = "sdg_video_volume";
    public static final String SDG_VIDEO_PAUSE = "sdg_video_pause";
    //Gionee <zengxh><2013-06-21> add for CR00828066 end
    
    //Gionee <chenml><2013-05-20> add for CR00817466  begin    
    public static final String SOUND_CONTROL_SWITCH = "sound_control_switch";
    public static final String SOUND_CONTROL_CALLING= "sound_control_calling";
    public static final String SOUND_CONTROL_MESSAGE= "sound_control_message";
    public static final String SOUND_CONTROL_LOCKSCREEN= "sound_control_lockscreen";
    public static final String SOUND_CONTROL_ALARMCLOCK= "sound_control_alarmclock";
    //Gionee <chenml><2013-05-20> add for CR00817466 end  

    //Gionee <chenml><2013-05-30> add for CR00821135  begin        
    public static final String SUSPEND_BUTTON = "suspend_button";
    public static final String PHONE_KEYBOARD = "phone_keyboard";
    public static final String INPUT_METHOD_KEYBOARD = "input_method_keyboard";
    public static final String PATTERN_UNLOCKSCREEN = "pattern_unlockscreen";
    public static final String SMALL_SCREEN_MODE = "small_screen_mode";
    public static final String SCREEN_SIZE = "screen_size";
    //Gionee <chenml><2013-05-30> add for CR00821135 end
    
    // Gionee <liuyb> <2013-06-08> add for CR00824683 begin
    /**
     * @hide
     */
    public static final String ALIGN_WAKE = "align_wake";
    /**
     * @hide
     */
    public static final int ALIGN_WAKE_ON = 1;
    /**
     * @hide
     */
    public static final int ALIGN_WAKE_OFF = 0;
    /**
     * @hide
     */
    public static final int ALIGN_WAKE_DEFAULT = 1;
    // Gionee <liuyb> <2013-06-08> add for CR00824683 end     

    //Gionee <wangguojing><2013-07-27> add for CR00844271 begin
    public static final String GN_SSG_SWITCH = "ssg_switch";
    public static final String GN_DG_SWITCH = "dg_switch";
    //Gionee <wangguojing><2013-07-27> add for CR00844271 end

    private static IContentProvider lazyGetProvider(ContentResolver cr) {
        IContentProvider cp = null;
        synchronized (sLock) {
            cp = mAuroraContentProvider;
            if (cp == null) {
                cp = mAuroraContentProvider = cr.acquireProvider(CONTENT_URI.getAuthority());
            }
        }
        return cp;
    }

    public static int getInt(ContentResolver cr, String name, int defaultValue) {
        String value = getStringValueFromTable(cr, AuroraSettings.TABLE_CONFIG, name, null);
        return (value != null) ? Integer.parseInt(value) : defaultValue;
    }
    
    public static String getString(ContentResolver cr, String name, String defaultValue) {
        return getStringValueFromTable(cr, AuroraSettings.TABLE_CONFIG, name, defaultValue);
    }

    private static String getStringValueFromTable(ContentResolver cr, String table, String name, String defaultValue) {
//        try {
            Log.d(TAG, "AuroraSettings get string name = " + name + " , defaultValue = " + defaultValue);

            IContentProvider cp = lazyGetProvider(cr);
//            Bundle b = cp.call(CALL_METHOD_GET_CONFIG, name, null);
            Bundle b = null;
            if (Build.VERSION.SDK_INT >= 18) {
                try {
                    Class<?> sPolicy = null;
                    sPolicy = Class.forName("android.content.IContentProvider");
                    Method method = sPolicy.getMethod("call", String.class, String.class, String.class, Bundle.class);
                    b = (Bundle)method.invoke(cp, 0, CALL_METHOD_GET_CONFIG, name, null);
                } catch (Exception e) {
                    Log.d(TAG, "AuroraSettings cp.call error");
                }  
            } else {
                try {
                    Class<?> sPolicy = null;
                    sPolicy = Class.forName("android.content.IContentProvider");
                    Method method = sPolicy.getMethod("call", String.class, String.class, Bundle.class);
                    b = (Bundle)method.invoke(cp, CALL_METHOD_GET_CONFIG, name, null);
                } catch (Exception e) {
                    Log.d(TAG, "AuroraSettings cp.call error");
                }
            }

            if (b != null) {
                String value = b.getString(AuroraSettings.VALUE);
                Log.d(TAG, "AuroraSettings get string name = " + name + " , value = " + value);
                return value;
            }
        /*} catch (RemoteException e) {
            Log.e(TAG, "Can't get name " + name, e);
            return defaultValue;
        }*/
        return defaultValue;
    }
    
    public static boolean putInt(ContentResolver cr, String name, int value) {
        return putString(cr, name, Integer.toString(value));
    }
    
    public static boolean putString(ContentResolver cr, String name, String value) {
//        try {
            Log.d(TAG, "AuroraSettings put string name = " + name + " , value = " + value);
            
            Bundle arg = new Bundle();
            arg.putString(AuroraSettings.VALUE, value);
            IContentProvider cp = lazyGetProvider(cr);
//            Bundle result = cp.call(CALL_METHOD_SET_CONFIG, name, arg);
            Bundle result = null;
            if (Build.VERSION.SDK_INT >= 18) {
                try {
                    Class<?> sPolicy = null;
                    sPolicy = Class.forName("android.content.IContentProvider");
                    Method method = sPolicy.getMethod("call", String.class, String.class, String.class, Bundle.class);
                    result = (Bundle)method.invoke(cp, 0, CALL_METHOD_GET_CONFIG, name, null);
                } catch (Exception e) {
                    Log.d(TAG, "AuroraSettings cp.call error");
                }  
            } else {
                try {
                    Class<?> sPolicy = null;
                    sPolicy = Class.forName("android.content.IContentProvider");
                    Method method = sPolicy.getMethod("call", String.class, String.class, Bundle.class);
                    result = (Bundle)method.invoke(cp, CALL_METHOD_GET_CONFIG, name, null);
                } catch (Exception e) {
                    Log.d(TAG, "AuroraSettings cp.call error");
                }
            }
            return result.getBoolean(AuroraSettings.VALUE, true);
/*        } catch (RemoteException e) {
            Log.e(TAG, "Can't set key " + name, e);
            return false;
        }*/
    }
    
    public static Uri getUriFor(String name) {
        return getUriFor(CONTENT_URI, name);
    }
    
    public static Uri getUriFor(Uri uri, String name) {
        return Uri.withAppendedPath(uri, name);
    }
}
