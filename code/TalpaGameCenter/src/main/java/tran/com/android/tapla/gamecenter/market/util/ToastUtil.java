package tran.com.android.tapla.gamecenter.market.util;

import android.widget.Toast;

import tran.com.android.tapla.gamecenter.market.marketApp;

/**
 * Toast工具类
 * @author JimXia
 * @date 2014-4-22 上午10:23:01
 */
public class ToastUtil {
    
    public static void shortToast(String msg) {
        Toast.makeText(marketApp.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }
    
    public static void longToast(String msg) {
        Toast.makeText(marketApp.getInstance(), msg, Toast.LENGTH_LONG).show();
    }
}
