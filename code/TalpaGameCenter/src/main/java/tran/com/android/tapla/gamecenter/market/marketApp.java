package tran.com.android.tapla.gamecenter.market;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.tran.com.android.gc.update.activity.setting.MarketManagerPreferenceActivity;

import tran.com.android.gc.lib.utils.LogUtils;
import tran.com.android.talpa.app_core.base.IApplication;
import tran.com.android.tapla.gamecenter.download.NetStateService;
import tran.com.android.tapla.gamecenter.market.activity.setting.MarketSettingsPreferenceActivity;
import tran.com.android.tapla.gamecenter.market.activity.setting.UpdateSettingsPreferenceActivity;
import tran.com.android.tapla.gamecenter.market.download.FilePathUtil;
import tran.com.android.tapla.gamecenter.market.install.InstallAppManager;
import tran.com.android.tapla.gamecenter.download.AppDownloadService;
import tran.com.android.tapla.gamecenter.install.AppInstallService;
import tran.com.android.tapla.gamecenter.download.AutoUpdateService;
import tran.com.android.tapla.gamecenter.market.util.Globals;
import tran.com.android.tapla.gamecenter.util.AlarmManagerUtile;
import universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import universalimageloader.cache.memory.impl.LruMemoryCache;
import universalimageloader.core.DisplayImageOptions;
import universalimageloader.core.ImageLoader;
import universalimageloader.core.ImageLoaderConfiguration;
import universalimageloader.core.assist.QueueProcessingType;
import universalimageloader.utils.StorageUtils;

public class marketApp extends Application implements IApplication {

//	public static marketApp ysApp;

    public static MarketManagerPreferenceActivity m_content2 = null;
    public static MarketMainActivity m_content = null;
    private List<WeakReference<Activity>> activityList = new LinkedList<WeakReference<Activity>>();
    private static marketApp instance;
    private SharedPreferences mUpdateSettingsPref;
    private SharedPreferences mFirstLoginSettingsPref;
    private boolean mIsFirst;

    //private MyBroadcastReciver broadcastReceiver;
    public MarketMainActivity getinstance() {
        return m_content;
    }

    public void setInstance(MarketMainActivity m_obj) {
        m_content = m_obj;
    }

    private static final String TAG = "marketApp";

    @Override
    public void onCreate() {

        instance = this;
        LogUtils.logd(TAG, "marketApp -- oncreate");
        super.onCreate();

        initUpdateSettings();

        AppDownloadService.checkInit(this, null);
        AppInstallService.checkInit(this);
        InstallAppManager.initInstalledAppList(this);

        AutoUpdateService.startAutoUpdate(this, 0);

        initImageLoader(getApplicationContext());


        //Set Alarm chenjie
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        boolean isSet = sp.getBoolean("auto_alarm_check", false);
        LogUtils.logd(TAG, "Create_alarm isSet=" + isSet);
        if (!isSet) {
            AlarmManagerUtile.sendAutoUpdateBroadcast(this);
        }

        //Set Alarm chenjie
        Intent netStateIntent = new Intent(this, NetStateService.class);
        startService(netStateIntent);

	/*	IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
		intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		broadcastReceiver = new MyBroadcastReciver();
		this.registerReceiver(broadcastReceiver, intentFilter);*/
    }

    /*	private class MyBroadcastReciver extends BroadcastReceiver {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.i("app", "zhangwei the action2="+action);
                if(action.equals(Intent.ACTION_SCREEN_ON))
                {
                    AutoUpdateService.pauseAutoUpdate();
                }
                else if(action.equals(Intent.ACTION_SCREEN_OFF))
                {
                    AutoUpdateService.continueAutoUpdate(marketApp.getInstance());
                }
            }

        }*/
    private void initUpdateSettings() {

        mFirstLoginSettingsPref = getSharedPreferences(Globals.SHARED_FIRST_LOGIN_CONFIG,
                Activity.MODE_PRIVATE);
        mIsFirst = mFirstLoginSettingsPref.getBoolean(Globals.SHARED_FIRST_KEY, true);
        mUpdateSettingsPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (mIsFirst) {
            mUpdateSettingsPref.edit().putBoolean(UpdateSettingsPreferenceActivity.SOFTWARE_AUTO_UPDATE_TIP_KEY, true).commit();
            mUpdateSettingsPref.edit().putBoolean(MarketSettingsPreferenceActivity.WIFI_DOWNLOAD_KEY, true).commit();

            mFirstLoginSettingsPref.edit().putBoolean(Globals.SHARED_FIRST_KEY, false).commit();
            mUpdateSettingsPref.edit().putString(Globals.DSIK_NAME, FilePathUtil.setDownDir(this)).commit();
        }
        mUpdateSettingsPref.edit().putBoolean(MarketSettingsPreferenceActivity.IS_SHOW_DOWNLOAD_PIC_KEY, false).commit();
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                "market/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheSize(100 * 1024 * 1024)

                .memoryCache(new LruMemoryCache(20 * 1024 * 1024))
                //.memoryCache(new WeakMemoryCache())
                .memoryCacheSize(20 * 1024 * 1024).writeDebugLogs() // Remove
                // for
                // release
                // app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    // 单例模式中获取唯一的ExitApplication 实例
    public static marketApp getInstance() {
    /*	if (null == instance) {
			instance = new marketApp();
		}*/
        return instance;
    }
    /**
     *
     * @作者 xiaojun.zhu
     *
     * @CreatTime 2018/9/3
     *
     * @dis 你好
     */


    // 添加Activity 到容器中
    public void addActivity(Activity activity) {
        activityList.add(new WeakReference<Activity>(activity));
    }

    // 遍历所有Activity 并finish

    public void exit() {
        // 回设亮度
        // if(m_content != null) {
        // m_content.resetBrightnessValue();
        // }
        for (WeakReference<Activity> activity : activityList) {

            if (null != activity.get())
                activity.get().finish();
        }
        activityList.clear();
        System.exit(0);

    }

    @Override
    public Application getApplicaitonInstance() {
        return this;
    }

    @Override
    public Context getApplicationsContext() {
        return this;
    }
}
