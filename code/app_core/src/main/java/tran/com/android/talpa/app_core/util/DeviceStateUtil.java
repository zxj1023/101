package tran.com.android.talpa.app_core.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import tran.com.android.talpa.app_core.log.LogPool;

/**
 * 说明：
 *
 * @auther lianbing.chen@itel-mobile.com
 * @date 2017/9/6 14:23
 */

public class DeviceStateUtil {

    private final static String TAG ="StateUtil";

    public static synchronized boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LogPool.e(TAG, "isNetworkAvailable--->" + true);
            return true;
        } else {
            LogPool.e(TAG, "isNetworkAvailable--->" + false);
            return false;
        }
    }
}
