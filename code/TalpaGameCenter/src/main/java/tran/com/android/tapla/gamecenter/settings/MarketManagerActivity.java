package tran.com.android.tapla.gamecenter.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

import tran.com.android.gc.lib.widget.AuroraActionBar;
import tran.com.android.gc.lib.widget.AuroraCustomActionBar;
import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.datauiapi.ManagerThread;
import tran.com.android.tapla.gamecenter.datauiapi.UpMarketManager;
import tran.com.android.tapla.gamecenter.datauiapi.bean.upcountinfo;
import tran.com.android.tapla.gamecenter.datauiapi.implement.Command;
import tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse;
import tran.com.android.tapla.gamecenter.datauiapi.interf.INotifiableController;
import tran.com.android.tapla.gamecenter.datauiapi.interf.INotifiableManager;
import tran.com.android.tapla.gamecenter.market.activity.BaseActivity;
import tran.com.android.tapla.gamecenter.market.download.DownloadUpdateListener;
import tran.com.android.tapla.gamecenter.market.service.AppDownloadService;
import tran.com.android.tapla.gamecenter.market.util.Globals;

/**
 * Created by jiazhuo.ren on 2017/9/19.
 */

public class MarketManagerActivity extends BaseActivity implements View.OnClickListener ,INotifiableController {
    private AuroraCustomActionBar mActionBar;
    LinearLayout gameUpdateLinear;
    private ManagerThread thread;
    private UpMarketManager mupManager;
    private SharedPreferences app_update;
    private int mUpdateCount;
    private int downloadCount;
    private boolean stopFlag = false;
    private boolean isFirst = true;
    private TextView downloadCountTv;
    private TextView updateCountTv;
    private LinearLayout gameSettingLinear;
    private LinearLayout gameDownLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAuroraContentView(R.layout.aurora_game_manager,
                AuroraActionBar.Type.NEW_COSTOM, true);
        initTransparentTheme();
        setStatusBarIconDark(true);
        initActionBar();
        initViews();
        setListener();
        initData();
        getNetData();
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
        getNetData();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            // super.handleMessage(msg);
        }
    };
    @Override
    protected void onStop() {
        super.onStop();
        stopFlag = true;
        AppDownloadService.unRegisterUpdateListener(updateListener);
    }

    private void initData() {
        mupManager = new UpMarketManager();
        thread = new ManagerThread(mupManager);
        thread.market(this);
        app_update = getSharedPreferences(Globals.SHARED_APP_UPDATE,
                MODE_APPEND);
        int sum = app_update.getInt(Globals.SHARED_UPDATE_SUM_KEY_ISEXITS, 0);
        if(sum>0){
            updateCountTv.setText(sum+"");
        }
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
//                mAppUpdateCheckPref.setDisUpSum(value.getCount());
                if(value.getCount()!=0){
                    updateCountTv.setText(value.getCount()+"");
                    updateCountTv.setVisibility(View.VISIBLE);
                }else{
                    updateCountTv.setVisibility(View.GONE);
                }
                SharedPreferences.Editor ed = app_update.edit();
                ed.putInt(Globals.SHARED_UPDATE_SUM_KEY_ISEXITS,
                        value.getCount());
                ed.commit();

            }
        }, MarketManagerActivity.this);
    }
    private void setListener() {
        gameUpdateLinear.setOnClickListener(this);
        gameSettingLinear.setOnClickListener(this);
        gameDownLinear.setOnClickListener(this);
    }

    private void initViews() {
        gameUpdateLinear= (LinearLayout) findViewById(R.id.game_update_linear);
        gameSettingLinear = (LinearLayout) findViewById(R.id.game_setting_linear);
        gameDownLinear = (LinearLayout) findViewById(R.id.game_down_linear);
        updateCountTv = (TextView) findViewById(R.id.update_count_tv);
        downloadCountTv = (TextView) findViewById(R.id.download_count_tv);
    }

    public void initTransparentTheme() {
        Window window = getWindow();//获取window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.app_detail_page_text_color_top_bg));
        initStatusBarHeight();
    }
    public void initStatusBarHeight() {
        LinearLayout linear = (LinearLayout) this.findViewById(R.id.top_bar);
        linear.setVisibility(View.VISIBLE);
        int statusHeight = getStatusBarHeight();
        ViewGroup.LayoutParams params = linear.getLayoutParams();
        params.height = statusHeight;
        linear.setLayoutParams(params);
    }
    /**
     * 获取状态栏的高度
     */
    public int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    private void setStatusBarIconDark(boolean lightStatusBar) {
        Window window = getWindow();
        View decor = window.getDecorView();
        int ui = decor.getSystemUiVisibility();
        if (lightStatusBar) {
            ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        decor.setSystemUiVisibility(ui);
    }
    private void initActionBar() {
        mActionBar = getCustomActionBar();
//		mActionBar.setTitleString(getResources().getString(R.string.pref_app_manager_title));
        mActionBar.setTitle(getResources().getString(R.string.pref_app_manager_title), Color.parseColor("#505050"));
        mActionBar.setTitleAnim(1);
        mActionBar.setBackground(getResources().getDrawable(
                R.drawable.actionbar_touying));
        mActionBar.showHomeIcon(true);
        mActionBar.setIcon(getResources().getDrawable(R.drawable.vector_drawable_home_btn_right));
        mActionBar.setTitleViewColor(true);
        mActionBar.showDefualtItem(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.game_update_linear:
                Intent lInt = new Intent(this, MarketUpdateActivity.class);
                lInt.putExtra("update_count", mUpdateCount);
                startActivity(lInt);
                break;
            case R.id.game_down_linear:
                Intent dInt = new Intent(this, DownloadManagerActivity.class);
                startActivity(dInt);
                break;
            case R.id.game_setting_linear:
                Intent settingIntent = new Intent(this,
                        MarketSettingsActivity.class);
                startActivity(settingIntent);
                break;
        }

    }
    private DownloadUpdateListener updateListener = new DownloadUpdateListener() {
        @Override
        public void downloadProgressUpdate() {
            if (downloadCount != AppDownloadService.getDownloaders().size()) {
                downloadCount = AppDownloadService.getDownloaders().size();
//                mDownloadManagerPref.setDisUpSum(downloadCount);
                if(downloadCount!=0){
                    downloadCountTv.setText(downloadCount+"");
                    downloadCountTv.setVisibility(View.VISIBLE);
                }else{
                    downloadCountTv.setVisibility(View.GONE);
                }
            }
        }
    };

    @Override
    public void onWrongConnectionState(int state, INotifiableManager manager, Command<?> source) {

    }

    @Override
    public void onError(int code, String message, INotifiableManager manager) {

    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void runOnUI(DataResponse<?> response) {
        mHandler.post(response);
    }
}
