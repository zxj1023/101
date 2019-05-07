package tran.com.android.tapla.gamecenter.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.Map;

import tran.com.android.gc.lib.widget.AuroraActionBar;
import tran.com.android.gc.lib.widget.AuroraCustomActionBar;
import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.market.activity.BaseActivity;
import tran.com.android.tapla.gamecenter.market.activity.setting.MarketSettingsPreferenceActivity;
import tran.com.android.tapla.gamecenter.market.download.FileDownloader;
import tran.com.android.tapla.gamecenter.market.service.AppDownloadService;
import tran.com.android.tapla.gamecenter.market.util.SystemUtils;

/**
 * Created by jiazhuo.ren on 2017/9/19.
 */

public class MarketSettingsActivity extends BaseActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {
    private AuroraCustomActionBar mActionBar;
    private LinearLayout gameUpdateSettingLinear;
    private Switch wifiDownOnlySw;
    private SharedPreferences mUpdateSettingsPref;
    private Switch noneDownloadPicKeySw;
    private Switch holdAppKeySw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAuroraContentView(R.layout.aurora_game_setting,
                AuroraActionBar.Type.NEW_COSTOM, true);
        initTransparentTheme();
        setStatusBarIconDark(true);
        initActionBar();
        initViews();
        setListener();
//        initData();
//        getNetData();
    }

    private void setListener() {
        gameUpdateSettingLinear.setOnClickListener(this);
    }

    private void initViews() {
        gameUpdateSettingLinear = (LinearLayout) findViewById(R.id.game_update_setting_linear);
        wifiDownOnlySw = (Switch) findViewById(R.id.wifi_down_only_sw);
        noneDownloadPicKeySw = (Switch) findViewById(R.id.none_download_pic_key_sw);
        holdAppKeySw = (Switch) findViewById(R.id.hold_app_key_sw);
        wifiDownOnlySw.setOnCheckedChangeListener(this);
        noneDownloadPicKeySw.setOnCheckedChangeListener(this);
        holdAppKeySw.setOnCheckedChangeListener(this);
        mUpdateSettingsPref = PreferenceManager.getDefaultSharedPreferences(MarketSettingsActivity.this);
        wifiDownOnlySw.setChecked(mUpdateSettingsPref.getBoolean(MarketSettingsPreferenceActivity.WIFI_DOWNLOAD_KEY,false));
        noneDownloadPicKeySw.setChecked(mUpdateSettingsPref.getBoolean(MarketSettingsPreferenceActivity.NONE_DOWNLOAD_KEY,false));
        holdAppKeySw.setChecked(mUpdateSettingsPref.getBoolean(MarketSettingsPreferenceActivity.HOLD_APP_KEY,false));


        wifiDownOnlySw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<Integer, FileDownloader> downloaders = AppDownloadService.getDownloaders();
                if (wifiDownOnlySw.isChecked() && downloaders!=null && downloaders.size()>0
                        && SystemUtils.isMobileNetworkConnected(MarketSettingsActivity.this)) {
                    AppDownloadService.pauseAllDownloads();
                    Toast.makeText(MarketSettingsActivity.this
                            , getString(R.string.pause_all_downloading_app), Toast.LENGTH_SHORT).show();
                }
            }
        });

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
        mActionBar.setTitle(getResources().getString(R.string.pref_settings_title),Color.parseColor("#505050"));
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
            case R.id.game_update_setting_linear:
                Intent lInt = new Intent(this, MarketUpdateSettingsActivity.class);
                startActivity(lInt);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor= mUpdateSettingsPref.edit();
        switch (buttonView.getId()){
            case R.id.wifi_down_only_sw:
                if(isChecked){
                    editor.putBoolean(MarketSettingsPreferenceActivity.WIFI_DOWNLOAD_KEY,true);
                    editor.apply();
                }else{
                    editor.putBoolean(MarketSettingsPreferenceActivity.WIFI_DOWNLOAD_KEY,false);
                    editor.apply();
                }
            break;
            case R.id.none_download_pic_key_sw:
                if(isChecked){
                    editor.putBoolean(MarketSettingsPreferenceActivity.NONE_DOWNLOAD_KEY,true);
                    editor.apply();
                }else{
                    editor.putBoolean(MarketSettingsPreferenceActivity.NONE_DOWNLOAD_KEY,false);
                    editor.apply();
                }
                break;
            case R.id.hold_app_key_sw:
                if(isChecked){
                    editor.putBoolean(MarketSettingsPreferenceActivity.HOLD_APP_KEY,true);
                    editor.apply();
                }else{
                    editor.putBoolean(MarketSettingsPreferenceActivity.HOLD_APP_KEY,false);
                    editor.apply();
                }
                break;
        }
    }
}
