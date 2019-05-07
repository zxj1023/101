package com.tran.com.android.gc.update;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.tran.com.android.gc.update.activity.setting.MarketManagerPreferenceActivity;
import com.tran.com.android.gc.update.install.InstallAppManager;
import com.tran.com.android.gc.update.service.AppDownloadService;
import com.tran.com.android.gc.update.service.AppInstallService;
import com.tran.com.android.gc.update.service.AutoUpdateService;
import com.tran.com.android.gc.update.util.Globals;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

public class updateApp extends Application {

    //	public static updateApp ysApp;
    public static MarketManagerPreferenceActivity m_content = null;
    private List<WeakReference<Activity>> activityList = new LinkedList<WeakReference<Activity>>();
    private static updateApp instance;
    private SharedPreferences mUpdateSettingsPref;
    private SharedPreferences mFirstLoginSettingsPref;
    private boolean mIsFirst;

    //private MyBroadcastReciver broadcastReceiver;
    public MarketManagerPreferenceActivity getinstance() {
        return m_content;
    }

    public void setInstance(MarketManagerPreferenceActivity m_obj) {
        m_content = m_obj;
    }

    @Override
    public void onCreate() {

        instance = this;
        Log.i("Test", "updateApp oncreate");
        super.onCreate();

        initUpdateSettings();

        AppDownloadService.checkInit(this, null);
        AppInstallService.checkInit(this);
        InstallAppManager.initInstalledAppList(this);

        AutoUpdateService.startMyself(this);
        AutoUpdateService.updateDownloadList(this);

        initImageLoader(getApplicationContext());
        Log.i("Test", "updateApp onCreate finished");
    }

    private void initUpdateSettings() {

        mFirstLoginSettingsPref = getSharedPreferences(Globals.SHARED_FIRST_LOGIN_CONFIG,
                Activity.MODE_PRIVATE);
        mIsFirst = mFirstLoginSettingsPref.getBoolean(Globals.SHARED_FIRST_KEY, true);
        if (mIsFirst) {
            mUpdateSettingsPref = PreferenceManager.getDefaultSharedPreferences(this);
            //mUpdateSettingsPref.edit().putBoolean(UpdateSettingsPreferenceActivity.SOFTWARE_AUTO_UPDATE_TIP_KEY, true).commit();

            mFirstLoginSettingsPref.edit().putBoolean(Globals.SHARED_FIRST_KEY, false).commit();
        }

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
    public static updateApp getInstance() {
    /*	if (null == instance) {
			instance = new updateApp();
		}*/
        return instance;

    }

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
}
