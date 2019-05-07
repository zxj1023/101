package com.tran.com.android.gc.update.util;

import android.content.Context;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class RootCmdUtil {

    /**
     * 静默安装
     *
     * @param file
     * @param
     * @param
     * @return
     */
    public static boolean slientInstall(Context context, File file) {
        boolean result = false;
        Process process = null;
        OutputStream out = null;
        try {
            process = Runtime.getRuntime().exec("su");
            out = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(out);
            // dataOutputStream.writeBytes("chmod 777 " + file.getPath() +
            // "\n");
            dataOutputStream
                    .writeBytes("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install -r "
                            + file.getPath());
            // 提交命令
            dataOutputStream.flush();

            // 关闭流操作
            dataOutputStream.close();
            out.close();
            int value = process.waitFor();

            // 代表获取成功
            if (value == 0) {
                result = true;
            } else if (value == 1) { // 获取失败
                result = false;
            } else { // 未知情况
                result = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

}
