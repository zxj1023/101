package tran.com.android.tapla.gamecenter.market.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Map;

import tran.com.android.gc.lib.preference.AuroraPreference;
import tran.com.android.gc.lib.preference.AuroraPreferenceScreen;
import tran.com.android.gc.lib.preference.AuroraSwitchPreference;
import tran.com.android.gc.lib.widget.AuroraActionBar;
import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.market.activity.BasePreferenceActivity;
import tran.com.android.tapla.gamecenter.market.download.FileDownloader;
import tran.com.android.tapla.gamecenter.download.AppDownloadService;
import tran.com.android.tapla.gamecenter.market.util.SystemUtils;
import tran.com.android.tapla.gamecenter.market.widget.AuroraMarketPreference;
import tran.com.android.tapla.gamecenter.settings.MarketUpdateSettingsActivity;

public class MarketSettingsPreferenceActivity extends BasePreferenceActivity {

	private final static String UPDATE_SETTINGS_KEY = "update_settings_key";
	public final static String WIFI_DOWNLOAD_KEY = "wifi_download_key";
	public final static String NONE_DOWNLOAD_KEY = "none_download_pic_key";
	public final static String IS_SHOW_DOWNLOAD_PIC_KEY = "is_show_download_pic_key";
	public final static String HOLD_APP_KEY = "hold_app_key";
	
	private AuroraActionBar mActionBar;
	private static final int AURORA_NEW_MARKET = 0;
	
	private AuroraMarketPreference mUpdateSettingsPref;
	private AuroraSwitchPreference mWifiDownloadPref;
	private AuroraSwitchPreference mNoneDownloadPicKey;
	private AuroraSwitchPreference mHoldAppKey;
	private Context mContext;
    
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		//this.setTheme(R.style.Theme_Aurora_Dark_Transparent);
		super.onCreate(arg0);
		addPreferencesFromResource(R.xml.settings_prefs);
		mContext = this;
		initActionBar();
		initViews();
	}

	private void initViews(){
		mUpdateSettingsPref = (AuroraMarketPreference) findPreference(UPDATE_SETTINGS_KEY);
		mWifiDownloadPref = (AuroraSwitchPreference) findPreference(WIFI_DOWNLOAD_KEY);
		mNoneDownloadPicKey = (AuroraSwitchPreference) findPreference(NONE_DOWNLOAD_KEY);
		mHoldAppKey = (AuroraSwitchPreference) findPreference(HOLD_APP_KEY);

		mWifiDownloadPref.setOnPreferenceChangeListener(new AuroraPreference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(AuroraPreference preference, Object newValue) {
				Map<Integer, FileDownloader> downloaders = AppDownloadService.getDownloaders();
				if ((boolean)newValue && downloaders!=null && downloaders.size()>0 && SystemUtils.isMobileNetworkConnected(mContext)) {
					AppDownloadService.pauseAllDownloads();
					Toast.makeText(mContext, getString(R.string.pause_all_downloading_app), Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		});
	}

	private void initActionBar() {
		mActionBar = getAuroraActionBar();
		mActionBar.setTitle(R.string.settings_pref);
        mActionBar.setBackground(getResources().getDrawable(
                R.drawable.actionbar1));
		// addAuroraActionBarItem(AuroraActionBarItem.Type.Add,
		// AURORA_NEW_MARKET);
	}

	@Override
	@Deprecated
	public boolean onPreferenceTreeClick(
			AuroraPreferenceScreen preferenceScreen, AuroraPreference preference) {
		// TODO Auto-generated method stub
		
		if(UPDATE_SETTINGS_KEY.equals(preference.getKey())){
			Intent lInt = new Intent(this, MarketUpdateSettingsActivity.class);
			startActivity(lInt);
		}else if(WIFI_DOWNLOAD_KEY.equals(preference.getKey())){

		}else if(NONE_DOWNLOAD_KEY.equals(preference.getKey())){
			
		}else if(HOLD_APP_KEY.equals(preference.getKey())){
			
		}
		
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}
 
	
}
