package com.tran.com.android.gc.update.util;

import android.widget.Toast;

import com.tran.com.android.gc.update.updateApp;

/**
 * Toast工具类
 * @author JimXia
 * @date 2014-4-22 上午10:23:01
 */
public class ToastUtil {
    
    public static void shortToast(String msg) {
        Toast.makeText(updateApp.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }
    
    public static void longToast(String msg) {
        Toast.makeText(updateApp.getInstance(), msg, Toast.LENGTH_LONG).show();
    }
}
