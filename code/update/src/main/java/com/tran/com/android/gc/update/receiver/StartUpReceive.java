package com.tran.com.android.gc.update.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.tran.com.android.gc.update.service.AutoUpdateService;
import com.tran.com.android.gc.update.util.Log;

/**
 * Created on 9/25/14.
 */
public class StartUpReceive extends BroadcastReceiver {
    private static final String TAG = "StartUpReceive";
    ComponentName cn;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Test", "StartUpReceive onReceive start");
        AutoUpdateService.startMyself(context);
        Log.i("Test", "StartUpReceive onReceive end");
    }
}
