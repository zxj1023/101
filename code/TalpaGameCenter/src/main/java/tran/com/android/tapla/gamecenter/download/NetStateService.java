package tran.com.android.tapla.gamecenter.download;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.camera2.utils.ListUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import tran.com.android.gc.lib.app.AuroraAlertDialog;
import tran.com.android.gc.lib.utils.LogUtils;
import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.market.activity.setting.UpdateSettingsPreferenceActivity;
import tran.com.android.tapla.gamecenter.market.download.FileDownloader;
import tran.com.android.tapla.gamecenter.market.install.InstallNotification;
import tran.com.android.tapla.gamecenter.market.model.DownloadManagerBean;
import tran.com.android.tapla.gamecenter.market.util.Globals;
import tran.com.android.tapla.gamecenter.market.util.SystemUtils;

/**
 * Created by jie.chen on 2017/9/26.
 */

public class NetStateService extends Service {
    private static final String TAG = "NetStateService";
    private ConnectivityManager connectivityManager;
    private NetworkInfo info;
    private int networkStatus;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.logd(TAG, "onCreate");
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mFilter.addAction("AUTO_UPDATE_CLOCK");
        registerReceiver(mReceiver, mFilter);
    }

    /**
     * 广播实例
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction(); //当前接受到的广播的标识(行动/意图)
            LogUtils.logd("TAG", "action=" + action);
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
                    String name = info.getTypeName();
                    if (name.equals("WIFI")) {
                        networkStatus = 2;

                        //判断自动Update升级下载
                        if (UpdateSettingsPreferenceActivity.getPreferenceValue(context,
                                UpdateSettingsPreferenceActivity.WIFI_AUTO_UPGRADE_KEY)) {
                            LogUtils.logd(TAG, "Wifi Auto Update");
                            AutoUpdateService.startAutoUpdate(NetStateService.this, 0);
                        }


                        LogUtils.logd(TAG, "Wifi networkStatus=" + name + " = " + networkStatus);
                        List<DownloadManagerBean> downloading = getDownLoading();
                        if (downloading != null && downloading.size() > 0) {
                            for (int i = 0; i < downloading.size(); i++) {
                                LogUtils.logd(TAG, "AppDownloadService.startDownload");
                                AppDownloadService.pauseOrContinueDownload(NetStateService.this, downloading.get(i).getDownloadData());
                            }
                            InstallNotification.cancleUpdateNotify();
                        }
                    } else {
                        final List<DownloadManagerBean> downloading = getDownLoading();
                        if (downloading != null && downloading.size() > 0) {
                            AuroraAlertDialog mWifiConDialog = new AuroraAlertDialog.Builder(
                                    NetStateService.this, AuroraAlertDialog.THEME_AMIGO_FULLSCREEN)
                                    .setTitle(NetStateService.this.getResources().getString(
                                            R.string.dialog_prompt))
                                    .setMessage(NetStateService.this
                                            .getResources()
                                            .getString(R.string.downloadman_continue_download_by_mobile))
                                    .setNegativeButton(android.R.string.cancel, null)
                                    .setPositiveButton(android.R.string.ok,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int which) {
                                                    for (int i = 0; i < downloading.size(); i++) {
                                                        LogUtils.logd(TAG, "AppDownloadService.startDownload");
                                                        AppDownloadService.pauseOrContinueDownload(NetStateService.this, downloading.get(i).getDownloadData());
                                                    }
                                                    InstallNotification.cancleUpdateNotify();
                                                }

                                            }).create();
                            mWifiConDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
                            mWifiConDialog.show();
                        }
                        networkStatus = 1;
                        LogUtils.logd(TAG, "Mobile networkStatus=" + name + " = " + networkStatus);

                    }
                } else {
                    // NetworkInfo为空或者是不可用的情况下
                    LogUtils.logd("TAG", "net is  unavailable");
                }
            }
            //自动检查更新的广播
            if (action.equals("AUTO_UPDATE_CLOCK")) {
                LogUtils.logd(TAG, "AUTO_UPDATE_CLOCK");
                if (SystemUtils.hasNetwork()) {
                    if (SystemUtils.getNetworkTypeName(NetStateService.this).equals("WIFI") && UpdateSettingsPreferenceActivity.getPreferenceValue(context,
                            UpdateSettingsPreferenceActivity.WIFI_AUTO_UPGRADE_KEY)) {
                        AutoUpdateService.startAutoUpdate(NetStateService.this, 0);
                    } else {
                        AutoUpdateService.startAutoUpdate(NetStateService.this, 2);
                    }

                }
            }

        }
    };





    public List<DownloadManagerBean> getDownLoading() {
        LogUtils.logd(TAG, "NetStateSerce.startDownload");
        List<DownloadManagerBean> tempList = new ArrayList<DownloadManagerBean>();
        // 获取下载管理列表
        List<DownloadManagerBean> tempDownloadingList = new ArrayList<DownloadManagerBean>();
        Map<Integer, FileDownloader> map = AppDownloadService.getDownloaders();
        if (map == null) {
            return null;
        }
        List<FileDownloader> downloaders = new ArrayList<FileDownloader>();
        for (int key : map.keySet()) {
            downloaders.add(map.get(key));
        }
        for (FileDownloader downloader : downloaders) {
            if (!(downloader.getStatus() >= FileDownloader.STATUS_INSTALL_WAIT) && (downloader.getStatus() >= FileDownloader.STATUS_DOWNLOADING)) {
                DownloadManagerBean bean = new DownloadManagerBean();
                bean.setType(DownloadManagerBean.TYPE_DOWNLOADING);
                bean.setDownloadData(downloader.getDownloadData());
                long fileSize = downloader.getFileSize();
                long downloadSize = downloader.getDownloadSize();
                int progress = (int) ((float) (downloadSize * 1.0)
                        / (fileSize * 1.0) * 100);
                bean.setFileSize(fileSize);
                bean.setDownloadSize(downloadSize);
                bean.setProgress(progress);
                bean.setDownloadStatus(downloader.getStatus());
                bean.setCreateTime(downloader.getCreateTime());
                tempDownloadingList.add(bean);
            }
        }
        sortDownloadingList(tempDownloadingList);

        return tempDownloadingList;
    }

    /*
     *根据下载进程建立的时间的先后顺序排序
     */
    private void sortDownloadingList(List<DownloadManagerBean> downloadingList) {
        Collections.sort(downloadingList,
                new Comparator<DownloadManagerBean>() {
                    @Override
                    public int compare(DownloadManagerBean bean1,
                                       DownloadManagerBean bean2) {
                        if (bean1.getCreateTime() < bean2.getCreateTime()) {
                            return -1;
                        } else if (bean1.getCreateTime() > bean2
                                .getCreateTime()) {
                            return 1;
                        }
                        return 0;
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.logd(TAG, "onDestroy");
        unregisterReceiver(mReceiver);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
