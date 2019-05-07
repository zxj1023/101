package tran.com.android.talpa.app_core.log;

import android.text.TextUtils;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * 说明：
 *
 * @auther lianbing.chen@itel-mobile.com
 * @date 2017/9/6 14:29
 */

public class LogPool {

    private static boolean DEBUG = true;

    private static final String DEFAULT_TAG = "TaplaGame";

    public static void d(String msg) {
        d(DEFAULT_TAG, msg);
    }

    public static void d(String msg, Throwable throwable) {
        d(DEFAULT_TAG, msg, throwable);
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(tag)) {
                Log.d(tag, "" + msg);
            } else {
                Log.d(DEFAULT_TAG, "" + msg);
            }
        }
    }

    public static void logd(String tag, String msg) {
        if (DEBUG) {
            Log.d(DEFAULT_TAG, tag + "    " + msg);
        }
    }

    public static void d(String tag, String msg, Throwable throwable) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(tag)) {
                Log.d(tag, "" + msg + "\t throwable:" + getStackTraceInfo(throwable));
            } else {
                Log.d(DEFAULT_TAG, "" + msg + "\t throwable:" + getStackTraceInfo(throwable));
            }
        }
    }


    public static void e(String msg) {
        e(DEFAULT_TAG, msg);
    }

    public static void e(String msg, Throwable throwable) {
        e(DEFAULT_TAG, msg, throwable);
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(tag)) {
                Log.e(tag, "" + msg);
            } else {
                Log.e(DEFAULT_TAG, "" + msg);
            }
        }
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (DEBUG) {
            if (!TextUtils.isEmpty(tag)) {
                Log.e(tag, "" + msg + "\t throwable:" + getStackTraceInfo(throwable));
            } else {
                Log.e(DEFAULT_TAG, "" + msg + "\t throwable:" + getStackTraceInfo(throwable));
            }
        }
    }

    private synchronized static String getStackTraceInfo(final Throwable throwable) {
        String trace = "";
        try {
            if (throwable != null) {
                Writer writer = new StringWriter();
                PrintWriter pw = new PrintWriter(writer);
                throwable.printStackTrace(pw);
                trace = writer.toString();
                pw.close();
            } else {
                return "throwable is null";
            }
        } catch (Exception e) {
            return "";
        }
        return trace;
    }

}
