package tran.com.android.tapla.gamecenter.market.activity.setting;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import tran.com.android.gc.lib.preference.AuroraPreference;
import tran.com.android.gc.lib.preference.AuroraPreferenceScreen;
import tran.com.android.gc.lib.widget.AuroraActionBar;
import tran.com.android.gc.lib.widget.AuroraActionBar.OnAuroraActionBarBackItemClickListener;

import tran.com.android.tapla.gamecenter.datauiapi.ManagerThread;
import tran.com.android.tapla.gamecenter.datauiapi.UpMarketManager;
import tran.com.android.tapla.gamecenter.datauiapi.bean.upcountinfo;
import tran.com.android.tapla.gamecenter.datauiapi.implement.Command;
import tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse;
import tran.com.android.tapla.gamecenter.datauiapi.interf.INotifiableController;
import tran.com.android.tapla.gamecenter.datauiapi.interf.INotifiableManager;
import tran.com.android.tapla.gamecenter.market.MarketMainActivity;
import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.market.activity.BasePreferenceActivity;
import tran.com.android.tapla.gamecenter.settings.DownloadManagerActivity;
import tran.com.android.tapla.gamecenter.settings.MarketUpdateActivity;
import tran.com.android.tapla.gamecenter.market.download.DownloadUpdateListener;
import tran.com.android.tapla.gamecenter.download.AppDownloadService;
import tran.com.android.tapla.gamecenter.market.util.Globals;
import tran.com.android.tapla.gamecenter.market.util.Log;
import tran.com.android.tapla.gamecenter.market.widget.AuroraMarketPreference;

public class MarketManagerPreferenceActivity extends BasePreferenceActivity
        implements INotifiableController {

    private final static String APP_UPDATE_KEY = "app_update_check_key";
    private final static String DOWNLOAD_MANAGER_KEY = "download_manager_key";
    private final static String APP_MANAGER_KEY = "app_manager_key";
    private final static String SETTINGS_KEY = "settings_key";
    // private final static String VERSION_UPDATE_KEY = "version_update_key";

    private AuroraActionBar mActionBar;
    private static final int AURORA_NEW_MARKET = 0;

    private AuroraMarketPreference mAppUpdateCheckPref;
    private AuroraMarketPreference mDownloadManagerPref;
    //	private AuroraMarketPreference mAppManagerPref;
    private AuroraMarketPreference mSettingsPref;
    // private AuroraMarketPreference mVersionUpdatePref;
    private ManagerThread thread;
    private UpMarketManager mupManager;

    private int mUpdateCount;
    private int downloadCount;

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
//		mAppManagerPref = (AuroraMarketPreference) findPreference(APP_MANAGER_KEY);
        mSettingsPref = (AuroraMarketPreference) findPreference(SETTINGS_KEY);
        if (sum > 0) {
            mAppUpdateCheckPref.setSum(sum);
        }
        // mVersionUpdatePref = (AuroraMarketPreference)
        // findPreference(VERSION_UPDATE_KEY);
    }

    private void initActionBar() {
        mActionBar = getAuroraActionBar();
        mActionBar.setTitle(R.string.market_manager_page);
        mActionBar.setBackground(getResources().getDrawable(
                R.drawable.actionbar1));
        // addAuroraActionBarItem(AuroraActionBarItem.Type.Add,
        // AURORA_NEW_MARKET);
        mActionBar
                .setmOnActionBarBackItemListener(new OnAuroraActionBarBackItemClickListener() {

                    @Override
                    public void onAuroraActionBarBackItemClicked(int itemId) {
                        // TODO Auto-generated method stub

                        Intent intent = new Intent(
                                MarketManagerPreferenceActivity.this,
                                MarketMainActivity.class);

                        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                        List<RunningTaskInfo> appTask = am.getRunningTasks(1);
                        boolean ifFront = false;
                        for (RunningTaskInfo app : appTask) {
                            if (app.baseActivity.equals(intent.getComponent())) {
                                ifFront = true;
                                break;
                            }
                        }

                        if (ifFront) {
                            finish();
                            Log.i("test", "setmOnActionBarBackItemListener back1");
                        } else {

                            finish();
                            startActivity(intent);
                            overridePendingTransition(
                                    R.anim.aurora_activity_close_enter,
                                    R.anim.aurora_activity_close_exit);
                            Log.i("test", "setmOnActionBarBackItemListener back2");
                        }
                    }
                });
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

        } else if (APP_MANAGER_KEY.equals(preference.getKey())) {
            Intent intent = new Intent();
            intent.putExtra("pkgName", "com.aurora.market");
            intent.setAction("android.settings.MANAGE_APPLICATIONS_SETTINGS");

            startActivity(intent);

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
     * @see INotifiableController#onWrongConnectionState(int,
     * INotifiableManager,
     * Command)
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
     * @see INotifiableController#onError(int,
     * java.lang.String,
     * INotifiableManager)
     */
    @Override
    public void onError(int code, String message, INotifiableManager manager) {
        // TODO Auto-generated method stub

    }

    /**
     * (非 Javadoc) Title: onMessage Description:
     *
     * @param message
     * @see INotifiableController#onMessage(java.lang.String)
     */
    @Override
    public void onMessage(String message) {
        // TODO Auto-generated method stub

    }

    /**
     * (非 Javadoc) Title: runOnUI Description:
     *
     * @param response
     * @see INotifiableController#runOnUI(DataResponse)
     */
    @Override
    public void runOnUI(DataResponse<?> response) {
        // TODO Auto-generated method stub
        mHandler.post(response);

    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            // super.handleMessage(msg);

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

            Intent intent = new Intent(MarketManagerPreferenceActivity.this,
                    MarketMainActivity.class);

			/*
			 * Intent intent1 = new Intent(DownloadManagerActivity.this,
			 * MarketMainActivity.class);
			 */
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<RunningTaskInfo> appTask = am.getRunningTasks(1);
            boolean ifFront = false;
            for (RunningTaskInfo app : appTask) {
                if (app.baseActivity.equals(intent.getComponent())) {
                    ifFront = true;
                    break;
                }
            }

            if (ifFront) {

                // startActivity(intent);
                finish();
                Log.i("test", "setmOnActionBarBackItemListener back1");
                return true;
                // overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                // Log.i(TAG, "setmOnActionBarBackItemListener back1");
            } else {

                finish();
                startActivity(intent);
                overridePendingTransition(
                        R.anim.aurora_activity_close_enter,
                        R.anim.aurora_activity_close_exit);
                Log.i("test", "setmOnActionBarBackItemListener back2");
                return true;
                // Log.i(TAG, "setmOnActionBarBackItemListener back2");
            }

        }

        return super.onKeyDown(keyCode, event);
    }

}
