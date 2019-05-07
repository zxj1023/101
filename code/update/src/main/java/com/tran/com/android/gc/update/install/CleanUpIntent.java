package com.tran.com.android.gc.update.install;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tran.com.android.gc.update.download.ApkUtil;

public class CleanUpIntent extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String action = intent.getAction();
        if (action.equals("notification_installed_cancelled")) {
            InstallNotification.cancleInsatlledNotify();
        } else if (action.equals("notification_installed_one")) {
            InstallNotification.cancleInsatlledNotify();

            String pakagename = intent.getStringExtra("pkgName");

            ApkUtil.openApp(context, pakagename);

        } else if (action.equals("notification_failed_cancelled")) {
            InstallNotification.cancleInstallFailedNotify();
        }

    }
}
