package tran.com.android.tapla.gamecenter.market.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import tran.com.android.tapla.gamecenter.download.AppDownloadService;
import tran.com.android.tapla.gamecenter.download.AutoUpdateService;

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
