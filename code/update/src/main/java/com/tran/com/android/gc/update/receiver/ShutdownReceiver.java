package com.tran.com.android.gc.update.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tran.com.android.gc.update.service.AppDownloadService;
import com.tran.com.android.gc.update.service.AutoUpdateService;

/**
 * Created on 9/26/14.
 */
public class ShutdownReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AppDownloadService.pauseAllDownloads();
        AutoUpdateService.pauseAutoUpdate();
    }
}
