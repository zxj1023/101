package com.tran.com.android.gc.update.activity.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;

import com.tran.com.android.gc.update.R;
import com.tran.com.android.gc.update.activity.BasePreferenceActivity;
import com.tran.com.android.gc.update.activity.module.MarketUpdateActivity;
import com.tran.com.android.gc.update.download.DownloadUpdateListener;
import com.tran.com.android.gc.update.service.AppDownloadService;
import com.tran.com.android.gc.update.util.Globals;
import com.tran.com.android.gc.update.util.Log;
import com.tran.com.android.gc.update.widget.AuroraMarketPreference;
import com.tran.com.android.gc.update.datauiapi.ManagerThread;
import com.tran.com.android.gc.update.datauiapi.UpMarketManager;
import com.tran.com.android.gc.update.datauiapi.bean.upcountinfo;
import com.tran.com.android.gc.update.datauiapi.implement.Command;
import com.tran.com.android.gc.update.datauiapi.implement.DataResponse;
import com.tran.com.android.gc.update.datauiapi.interf.INotifiableController;
import com.tran.com.android.gc.update.datauiapi.interf.INotifiableManager;

import tran.com.android.gc.lib.preference.AuroraPreference;
import tran.com.android.gc.lib.preference.AuroraPreferenceScreen;
import tran.com.android.gc.lib.widget.AuroraActionBar;

public class MarketManagerPreferenceActivity extends BasePreferenceActivity
        implements INotifiableController {

    private final static String APP_UPDATE_KEY = "app_update_check_key";
    private final static String DOWNLOAD_MANAGER_KEY = "download_manager_key";
    private final static String SETTINGS_KEY = "settings_key";
    private AuroraActionBar mActionBar;
    private static final int AURORA_NEW_MARKET = 0;
    public static final String TAG = "liumx";

    private AuroraMarketPreference mAppUpdateCheckPref;
    private AuroraMarketPreference mDownloadManagerPref;
    private AuroraMarketPreference mSettingsPref;
    // private AuroraMarketPreference mVersionUpdatePref;
    private ManagerThread thread;
    private UpMarketManager mupManager;

    private int downloadCount;
    private int mUpdateCount;

    private boolean stopFlag = false;
    private boolean isFirst = true;
    private SharedPreferences app_update;
    private int sum = 0;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        // this.setTheme(R.style.Theme_Aurora_Dark_Transparent);
        super.onCreate(arg0);
        addPreferencesFromResource(R.xml.market_manager_prefs);
        app_update = getSharedPreferences(Globals.SHARED_APP_UPDATE,
                MODE_APPEND);

        sum = app_update.getInt(Globals.SHARED_UPDATE_SUM_KEY_ISEXITS, 0);
        initActionBar();
        initViews();
        mupManager = new UpMarketManager();
        thread = new ManagerThread(mupManager);

        thread.market(this);
        initdata();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (stopFlag) {
            updateListener.downloadProgressUpdate();
            stopFlag = false;
        }
        AppDownloadService.registerUpdateListener(updateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopFlag = true;
        AppDownloadService.unRegisterUpdateListener(updateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirst) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateListener.downloadProgressUpdate();
                    isFirst = false;
                }
            }, 50);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initdata();
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        thread.quit();
    }

    private void initdata() {
        getNetData();
    }

    private void getNetData() {
        mupManager.getUpdateCount(new DataResponse<upcountinfo>() {
            public void run() {
                if (value != null) {
                    mUpdateCount = value.getCount();
                    disView();
                }
            }

            private void disView() {
                // TODO Auto-generated method stub
                mAppUpdateCheckPref.setDisUpSum(value.getCount());

                Editor ed = app_update.edit();
                ed.putInt(Globals.SHARED_UPDATE_SUM_KEY_ISEXITS,
                        value.getCount());
                ed.commit();
            }
        }, MarketManagerPreferenceActivity.this);
    }

    private void initViews() {
        mAppUpdateCheckPref = (AuroraMarketPreference) findPreference(APP_UPDATE_KEY);
        mDownloadManagerPref = (AuroraMarketPreference) findPreference(DOWNLOAD_MANAGER_KEY);
        mSettingsPref = (AuroraMarketPreference) findPreference(SETTINGS_KEY);
        if (sum > 0) {
            mAppUpdateCheckPref.setSum(sum);
        }
    }

    private void initActionBar() {
        mActionBar = getAuroraActionBar();
        mActionBar.getHomeButton().setVisibility(View.GONE);
        mActionBar.setTitle(R.string.market_manager_page);
        mActionBar.setBackground(getResources().getDrawable(
                R.drawable.aurora_action_bar_top_bg_green));
    }

    @Override
    @Deprecated
    public boolean onPreferenceTreeClick(
            AuroraPreferenceScreen preferenceScreen, AuroraPreference preference) {
        // TODO Auto-generated method stub
        if (APP_UPDATE_KEY.equals(preference.getKey())) {

            Intent lInt = new Intent(this, MarketUpdateActivity.class);
            lInt.putExtra("update_count", mUpdateCount);
            startActivity(lInt);

        } else if (APP_UPDATE_KEY.equals(preference.getKey())) {

        } else if (DOWNLOAD_MANAGER_KEY.equals(preference.getKey())) {

            Intent dInt = new Intent(this, DownloadManagerActivity.class);
            startActivity(dInt);

        } else if (SETTINGS_KEY.equals(preference.getKey())) {

            Intent lInt = new Intent(this,
                    MarketSettingsPreferenceActivity.class);
            startActivity(lInt);

        }/*
		 * else if(VERSION_UPDATE_KEY.equals(preference.getKey())){
		 *
		 * }
		 */

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    /**
     * (非 Javadoc) Title: onWrongConnectionState Description:
     *
     * @param state
     * @param manager
     * @param source
     * @see com.tran.com.android.gc.update.datauiapi.interf.INotifiableController#onWrongConnectionState(int,
     * com.tran.com.android.gc.update.datauiapi.interf.INotifiableManager,
     * com.tran.com.android.gc.update.datauiapi.implement.Command)
     */
    @Override
    public void onWrongConnectionState(int state, INotifiableManager manager,
                                       Command<?> source) {
        // TODO Auto-generated method stub

    }

    /**
     * (非 Javadoc) Title: onError Description:
     *
     * @param code
     * @param message
     * @param manager
     * @see com.tran.com.android.gc.update.datauiapi.interf.INotifiableController#onError(int,
     * java.lang.String,
     * com.tran.com.android.gc.update.datauiapi.interf.INotifiableManager)
     */
    @Override
    public void onError(int code, String message, INotifiableManager manager) {

    }

    /**
     * (非 Javadoc) Title: onMessage Description:
     *
     * @param message
     * @see com.tran.com.android.gc.update.datauiapi.interf.INotifiableController#onMessage(java.lang.String)
     */
    @Override
    public void onMessage(String message) {

    }

    /**
     * (非 Javadoc) Title: runOnUI Description:
     *
     * @param response
     * @see com.tran.com.android.gc.update.datauiapi.interf.INotifiableController#runOnUI(com.tran.com.android.gc.update.datauiapi.implement.DataResponse)
     */
    @Override
    public void runOnUI(DataResponse<?> response) {
        mHandler.post(response);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

        }

    };

    private DownloadUpdateListener updateListener = new DownloadUpdateListener() {
        @Override
        public void downloadProgressUpdate() {
            if (downloadCount != AppDownloadService.getDownloaders().size()) {
                downloadCount = AppDownloadService.getDownloaders().size();
                mDownloadManagerPref.setDisUpSum(downloadCount);
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // startActivity(intent);
            finish();
            Log.i("test", "setmOnActionBarBackItemListener back1");
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
