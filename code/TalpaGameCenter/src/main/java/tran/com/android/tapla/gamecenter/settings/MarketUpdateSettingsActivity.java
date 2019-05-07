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

import java.lang.reflect.Field;

import tran.com.android.gc.lib.widget.AuroraActionBar;
import tran.com.android.gc.lib.widget.AuroraCustomActionBar;
import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.download.AutoUpdateService;
import tran.com.android.tapla.gamecenter.market.activity.BaseActivity;
import tran.com.android.tapla.gamecenter.market.activity.setting.UpdateSettingsPreferenceActivity;

/**
 * Created by jiazhuo.ren on 2017/9/19.
 */

public class MarketUpdateSettingsActivity extends BaseActivity implements View.OnClickListener {
    private AuroraCustomActionBar mActionBar;
    private Switch wifiAutoUpgrade;
    private LinearLayout ignoreAppLinear;
    private SharedPreferences mSharedPrefs;
    private Switch softwareAutoUpdateTipSw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAuroraContentView(R.layout.aurora_game_update_setting,
                AuroraActionBar.Type.NEW_COSTOM, true);
        initTransparentTheme();
        setStatusBarIconDark(true);
        initActionBar();
        initViews();
        setListener();
        initData();
//        getNetData();
    }

    private void initData() {
        mSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(MarketUpdateSettingsActivity.this);
        boolean autoUpgrade=  mSharedPrefs.getBoolean(UpdateSettingsPreferenceActivity.WIFI_AUTO_UPGRADE_KEY,false);
        wifiAutoUpgrade.setChecked(autoUpgrade);
        softwareAutoUpdateTipSw.setChecked(mSharedPrefs.getBoolean(UpdateSettingsPreferenceActivity.SOFTWARE_AUTO_UPDATE_TIP_KEY,false));
    }

    private void setListener() {
        ignoreAppLinear.setOnClickListener(this);
    }

    private void initViews() {
        ignoreAppLinear = (LinearLayout) findViewById(R.id.ignore_app_linear);
        wifiAutoUpgrade = (Switch) findViewById(R.id.wifi_auto_upgrade);
        softwareAutoUpdateTipSw = (Switch) findViewById(R.id.software_auto_update_tip_sw);
        wifiAutoUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wifiAutoUpgrade.isChecked()) {
                    wifiAutoUpgrade.setChecked(true);
                    AutoUpdateService.startAutoUpdate(MarketUpdateSettingsActivity.this, 0);
                    SharedPreferences mSharedPrefs = PreferenceManager
                            .getDefaultSharedPreferences(MarketUpdateSettingsActivity.this);
                    SharedPreferences.Editor editor=mSharedPrefs.edit();
                    editor.putBoolean(UpdateSettingsPreferenceActivity.WIFI_AUTO_UPGRADE_KEY,true);
                    editor.apply();
//                    AuroraAlertDialog.Builder builder = new AuroraAlertDialog.Builder(MarketUpdateSettingsActivity.this);
//                    builder.setMessage(getString(R.string.wifi_auto_upgrade_open_tip));
//                    builder.setPositiveButton(getString(R.string.dialog_confirm),
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface arg0, int arg1) {
//                                    wifiAutoUpgrade.setChecked(true);
//                                    AutoUpdateService.startAutoUpdate(MarketUpdateSettingsActivity.this, 0);
//                                    SharedPreferences mSharedPrefs = PreferenceManager
//                                            .getDefaultSharedPreferences(MarketUpdateSettingsActivity.this);
//                                    SharedPreferences.Editor editor=mSharedPrefs.edit();
//                                    editor.putBoolean(UpdateSettingsPreferenceActivity.WIFI_AUTO_UPGRADE_KEY,true);
//                                    editor.apply();
//                                }
//                            });
//                    builder.setNegativeButton(getString(R.string.dialog_cancel),
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface arg0, int arg1) {
//                                    wifiAutoUpgrade.setChecked(false);
//                                }
//                            });
//
//                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                        @Override
//                        public void onCancel(DialogInterface arg0) {
//                            wifiAutoUpgrade.setChecked(false);
//                        }
//                    });
//                    builder.show();
                } else {
                    SharedPreferences mSharedPrefs = PreferenceManager
                            .getDefaultSharedPreferences(MarketUpdateSettingsActivity.this);
                    SharedPreferences.Editor editor=mSharedPrefs.edit();
                    editor.putBoolean(UpdateSettingsPreferenceActivity.WIFI_AUTO_UPGRADE_KEY,false);
                    editor.apply();
                    AutoUpdateService.stopAutoUpdate(MarketUpdateSettingsActivity.this);
                }
            }
        });
        softwareAutoUpdateTipSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor=mSharedPrefs.edit();
                if(isChecked){
                    editor.putBoolean(UpdateSettingsPreferenceActivity.SOFTWARE_AUTO_UPDATE_TIP_KEY,true);
                    editor.apply();
                }else{
                    editor.putBoolean(UpdateSettingsPreferenceActivity.SOFTWARE_AUTO_UPDATE_TIP_KEY,false);
                    editor.apply();
                }
            }
        });
//        wifiAutoUpgrade.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//            }
//        });
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
//        mActionBar.setTitleString(getResources().getString(R.string.app_update_settings));
        mActionBar.setTitle(getResources().getString(R.string.app_update_settings), Color.parseColor("#505050"));
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
            case R.id.ignore_app_linear:
                Intent lInt = new Intent(this, MarketUpdateIgnoredActivity.class);
                startActivity(lInt);
                break;
        }
    }
}
