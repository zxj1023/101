package com.tran.com.android.gc.update.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tran.com.android.gc.update.activity.module.UpdateAlertActivity;
import com.tran.com.android.gc.update.util.Log;

/**
 * Created on 9/25/14.
 */
public class UpdateReqReceiver extends BroadcastReceiver {
    String TAG = "UpdateReqReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        int apkId = intent.getIntExtra("apkId", 0);
        Log.i(TAG, apkId + " ");

//        if (AppDownloadService.getDownloaders().containsKey(apkId)) {
//            Intent myIntent = new Intent(updateApp.getInstance(),
//                    DownloadManagerActivity.class);
//            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            updateApp.getInstance().startActivity(myIntent);
//
//
//            SystemUtils.killProcess(context, apkId);
//            return;
//        }

        Intent upIntent = new Intent(context, UpdateAlertActivity.class);
        upIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        upIntent.putExtra("apkId", apkId);
        context.startActivity(upIntent);
    }

}
