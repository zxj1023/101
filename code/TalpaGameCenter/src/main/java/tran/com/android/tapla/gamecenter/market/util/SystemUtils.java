/*
 * @author zw
 */
package tran.com.android.tapla.gamecenter.market.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageParser;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import tran.com.android.talpa.app_core.log.LogPool;
import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.market.activity.setting.MarketSettingsPreferenceActivity;
import tran.com.android.tapla.gamecenter.market.db.CacheDataAdapter;
import tran.com.android.tapla.gamecenter.market.db.CategoryListDataAdapter;
import tran.com.android.tapla.gamecenter.market.http.Base64;
import tran.com.android.tapla.gamecenter.market.http.HttpRequstData;
import tran.com.android.tapla.gamecenter.market.http.MD5;
import tran.com.android.tapla.gamecenter.market.marketApp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemUtils {
	private static String TAG = "SystemUtils";

	private SystemUtils() {
	}

	public final static int SDK_15 = 3;
	public final static int SDK_16 = 4;
	public final static int SDK_20 = 5;
	public final static int SDK_201 = 6;
	public final static int SDK_21 = 7;
	public final static int SDK_22 = 8;
	public final static int SDK_23 = 9;

	// screen resolution of u3
	public final static int U3_SCREEN_WIDTH = 1440;
	public final static int U3_SCREEN_HEIGHT = 2400;
	
	//add wandoujia need data
	public final static String wandoujia_key = "c15d9724a0f94bfb98e036c2b95deff3";
	public static String phone_imei = "";
	public static String mac_address = "";
	
	//end wandoujai data
	
	
	public final static String EMULATOR_DEVICE_ID = "000000000000000";

	public static String getSysLang() {
		return String.format("%s-%s", Locale.getDefault().getLanguage(), Locale
				.getDefault().getCountry());
	}

	public static String getFromAssets(Context context, String fileName) {
		StringBuffer sb = new StringBuffer();
		try {

			InputStreamReader inputReader = new InputStreamReader(context
					.getResources().getAssets().open(fileName), "utf-8");
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";

			while ((line = bufReader.readLine()) != null)
				sb.append(line);
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			FileLog.e(TAG, e.toString());
		}
		return sb.toString();
	}

	public static String getSysCountry() {
		return Locale.getDefault().getCountry();
	}

	// 取得mainfest中的versionCode
	public static int getVersionCode(Context context, String packageName) {
		int versionCode = -1;
		try {
			versionCode = context.getPackageManager().getPackageInfo(
					packageName, 0).versionCode;
		} catch (NameNotFoundException e) {

			FileLog.e(TAG, e.toString());
		}
		return versionCode;
	}

	// 取得mainfest中的VersionName
	public static String getVersionName(Context context, String packageName) {
		String versionName = "";
		try {
			versionName = context.getPackageManager().getPackageInfo(
					packageName, 0).versionName;
		} catch (NameNotFoundException e) {
			FileLog.e(TAG, e.toString());
		}
		return versionName;
	}


	public static String getBuildNumber() {
		return URLEncoder.encode(Build.DISPLAY);
	}

	//add for the wandoujia data
	public static String getWandoujia_Imei() {
		if(phone_imei == "")
		{
			if(null == getImei(marketApp.getInstance()))
			{
				return phone_imei;
			}
			
			String str = CipherUtils.encrypt(getImei(marketApp.getInstance()), wandoujia_key.substring(0, 16));
			phone_imei = URLEncoder.encode(str);
		}

		return phone_imei;

	}
	
	public static String getWandoujia_MacAddress() {
		if(mac_address == "")
		{
			if(null == getMacAddress(marketApp.getInstance()))
			{
				return mac_address;
			}
			String str = CipherUtils.encrypt(getMacAddress(marketApp.getInstance()), wandoujia_key.substring(0, 16));
			mac_address = URLEncoder.encode(str);
		}

		return mac_address;
	}
	
	public static String getModelNumber() {
		return URLEncoder.encode(Build.MODEL);
	}

	//end for the wandoujia data
	public static String getImei(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		return manager.getDeviceId();
	}
	
	public static boolean isEmulator(Context context) {
		String imei = getImei(context);
		return EMULATOR_DEVICE_ID.equalsIgnoreCase(imei);
	}

	public static boolean isSimReady(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		int nSimState = manager.getSimState();

		return (nSimState == TelephonyManager.SIM_STATE_READY);
	}

	public static boolean isAirplaneModeOn(Context context) {
		return Settings.System.getInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0) != 0;
	}

	public static boolean hasActiveNetwork(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService("connectivity");
		if (connectivityManager.getActiveNetworkInfo() != null) {
			return connectivityManager.getActiveNetworkInfo().isAvailable();
		} else {
			return false;
		}
	}

	public static boolean isNetworkReady(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		return (manager.getNetworkType() != TelephonyManager.NETWORK_TYPE_UNKNOWN);
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return false;
		} else {
			NetworkInfo[] info = manager.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].isAvailable()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return false;
		} else {
			NetworkInfo[] info = manager.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean isWifiEnabled(Context context) {
		WifiManager manager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		return (manager == null ? false : manager.isWifiEnabled());
	}

	public static String getMacAddress(Context context)
	{
		WifiManager manager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = manager.getConnectionInfo();
		String macAddress = (wifiInfo == null ? null : wifiInfo.getMacAddress());
		return macAddress;
	}
	
	
	public static boolean isWifiAvailable(Context context) {
		
		String macAddress = getMacAddress(context);

		return (macAddress != null);
	}

	public static boolean isMobileNetworkConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService("connectivity");

		NetworkInfo info = connectivityManager.getActiveNetworkInfo();

		if (info == null) {
			return false;
		}

		int netType = info.getType();

		// Check if Mobile Network is connected
		if (netType == ConnectivityManager.TYPE_MOBILE) {
			return info.isConnected();
		} else {
			return false;
		}
	}

	public static boolean isHighSpeedConnection(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService("connectivity");

		NetworkInfo info = connectivityManager.getActiveNetworkInfo();

		if (info == null) {
			return false;
		}

		int netType = info.getType();
		int netSubtype = info.getSubtype();

		// Check if WiFi or 3G is connected
		if (netType == ConnectivityManager.TYPE_WIFI) {
			return info.isConnected();
		} else if (netType == ConnectivityManager.TYPE_MOBILE
				&& netSubtype >= TelephonyManager.NETWORK_TYPE_UMTS) {
			return info.isConnected();
		} else {
			return false;
		}
	}
	/**
	 * 获取是否有网络
	 * 
	 * @return
	 */
	public static boolean hasNetwork() {
		Context context = marketApp.getInstance();
		if (context != null) {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			State wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
					.getState();
			State mobileState = cm.getNetworkInfo(
					ConnectivityManager.TYPE_MOBILE).getState();
			if (wifiState == State.CONNECTED || mobileState == State.CONNECTED) {
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return true;
		}
	}

	public static int getConnectingType(Context context) {
		ConnectivityManager mConnectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mTelephony = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);

		NetworkInfo info = mConnectivity.getActiveNetworkInfo();

		if (info == null || !mConnectivity.getBackgroundDataSetting()) {
			return -1;
		}

		int netType = info.getType();
		int netSubtype = info.getSubtype();

		if (netType == ConnectivityManager.TYPE_WIFI) {
			return Globals.NETWORK_WIFI;
		} else {
			if ((netSubtype == TelephonyManager.NETWORK_TYPE_GPRS)
					|| (netSubtype == TelephonyManager.NETWORK_TYPE_EDGE)
					|| (netSubtype == TelephonyManager.NETWORK_TYPE_CDMA)) {
				return Globals.NETWORK_2G;
			} else {
				return Globals.NETWORK_3G;
			}
		}
	}
	public static String getNetworkTypeName(Context context) {
		ConnectivityManager mConnectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mTelephony = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);

		NetworkInfo info = mConnectivity.getActiveNetworkInfo();

		if (info == null || !mConnectivity.getBackgroundDataSetting()) {
			return "";
		}
		return info.getTypeName();
	}

	public static String getIPAddress(Context context) {
		NetworkInfo info = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

		if (info != null && info.isConnected()) {
			if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
				try {
					for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
						NetworkInterface intf = en.nextElement();
						for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
							InetAddress inetAddress = enumIpAddr.nextElement();
							if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
								return inetAddress.getHostAddress();
							}
						}
					}
				} catch (SocketException e) {
					e.printStackTrace();
				}
			} else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
				WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
				return ipAddress;
			}
		}
		return null;
	}

	public static String intIP2StringIP(int ip) {
		return (ip & 0xFF) + "." +
				((ip >> 8) & 0xFF) + "." +
				((ip >> 16) & 0xFF) + "." +
				(ip >> 24 & 0xFF);
	}

	public static boolean isCPUFreqOK() {
		ProcessBuilder cmd;
		String result = null;

		try {
			String[] args = { "/system/bin/cat",
					"/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
			cmd = new ProcessBuilder(args);

			Process process = cmd.start();
			InputStream in = process.getInputStream();

			InputStreamReader sr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(sr);

			result = br.readLine();
			if (result != null) {
				Log.i("CPU_Freq", result);
			}

			in.close();

		} catch (IOException e) {
			FileLog.e(TAG, e.toString());
		}
		if (result != null) {
			return (Integer.valueOf(result) >= 990000);
		} else {
			return false;
		}

	}

	public static boolean isSnsAccount(String username) {
		if (username.startsWith("#") == true) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * base64的解码
	 */
	public static String decodeBase64(String s) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			decodeBase64(s, bos);
		} catch (IOException e) {
			FileLog.e(TAG, e.toString());
			throw new RuntimeException();
		}
		String result = bos.toString();
		try {
			bos.close();
			bos = null;
		} catch (IOException e) {
			FileLog.e(TAG, e.toString());
		}
		return result;
	}

	private static void decodeBase64(String s, OutputStream os)
			throws IOException {
		int i = 0;

		int len = s.length();

		while (true) {
			while (i < len && s.charAt(i) <= ' ')
				i++;

			if (i == len)
				break;

			int tri = (decodeBase64(s.charAt(i)) << 18)
					+ (decodeBase64(s.charAt(i + 1)) << 12)
					+ (decodeBase64(s.charAt(i + 2)) << 6)
					+ (decodeBase64(s.charAt(i + 3)));

			os.write((tri >> 16) & 255);
			if (s.charAt(i + 2) == '=')
				break;
			os.write((tri >> 8) & 255);
			if (s.charAt(i + 3) == '=')
				break;
			os.write(tri & 255);

			i += 4;
		}
	}

	private static int decodeBase64(char c) {
		if (c >= 'A' && c <= 'Z')
			return ((int) c) - 65;
		else if (c >= 'a' && c <= 'z')
			return ((int) c) - 97 + 26;
		else if (c >= '0' && c <= '9')
			return ((int) c) - 48 + 26 + 26;
		else
			switch (c) {
			case '+':
				return 62;
			case '/':
				return 63;
			case '=':
				return 0;
			default:
				throw new RuntimeException("unexpected code: " + c);
			}
	}

	public static int Dip2Px(Context context, int dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);

	}

	public static float Dip2Px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (float) dpValue * scale;

	}

	public static boolean isWifiNetwork(Context context) {
		if (!SystemUtils.isNetworkConnected(context)) {
			// 当前网络获取判断，如无网络连接，直接后台日志
			Log.d(TAG, "isWifiNetwork None network");
			return false;
		}
		// 连接后判断当前WIFI
		if (SystemUtils.getConnectingType(context) == Globals.NETWORK_WIFI) {
			return true;
		} else {
			return false;
		}
	}
	
	public static int isNetStatus(Context context) {
		if (!SystemUtils.isNetworkConnected(context)) {
			// 当前网络获取判断，如无网络连接，直接后台日志
			Log.d(TAG, "isWifiNetwork None network");
			return 0;
		}
		// 连接后判断当前WIFI
		if (SystemUtils.getConnectingType(context) == Globals.NETWORK_WIFI) {
			return 1;
		} else {
			return 2;
		}
	}
	
	public static boolean isLoadingImage(Context cxt) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(cxt);
		boolean nFlag = sp.getBoolean(MarketSettingsPreferenceActivity.NONE_DOWNLOAD_KEY, false);
		boolean isShowDownloadPicKey = sp.getBoolean(MarketSettingsPreferenceActivity.IS_SHOW_DOWNLOAD_PIC_KEY, false);
		SharedPreferences sp1 = cxt.getSharedPreferences(Globals.SHARED_WIFI_UPDATE,
				cxt.MODE_APPEND);
		
		int iswifi = sp1.getInt(
				Globals.SHARED_NETSTATUS_KEY_ISEXITS, 0);
		if(nFlag && (iswifi == 2))
		{
			if(!isShowDownloadPicKey){
				Toast.makeText(cxt, cxt.getString(R.string.download_picture_key), Toast.LENGTH_SHORT).show();
				sp.edit().putBoolean(MarketSettingsPreferenceActivity.IS_SHOW_DOWNLOAD_PIC_KEY, true).commit();
			}
			return false;
		}
		else
		{
			return true;
		}
		
	}
	public float getDimenPixenSize(Context context) {
		float size = 0;

		return size;
	}

	public static boolean isNull(String str) {
		boolean bl = false;
		if ((null != str) && (!str.equals("")) && (!str.equals("null"))) {
			bl = false;
		} else {
			bl = true;
		}

		return bl;

	}

	@SuppressLint("NewApi")
	public static void setBuildSDKBackground(View v, Drawable drawable) {
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			v.setBackgroundDrawable(drawable);
		} else {
			v.setBackground(drawable);
		}
	}

	// 正则表达式 替换字符串
	// String[][] object={new String[]{"\\[image::::","\\'"},new
	// String[]{"\\]","\\'"}};
	// Log.i(TAG,replace(str,object));
	// String content =
	// replace(viewHolder.tvw_card_content.getText().toString(),object);
	// String[][] object = { new String[] { "\\[image::::(.*?)\\::::]", "" } };
	public static String replace(final String sourceString, Object[] object) {
		String temp = sourceString;
		for (int i = 0; i < object.length; i++) {
			String[] result = (String[]) object[i];
			Pattern pattern = Pattern.compile(result[0]);
			Matcher matcher = pattern.matcher(temp);
			temp = matcher.replaceAll(result[1]);
		}
		return temp;
	}

	// "\\[image::::(.*?)\\]"
	//
	public static ArrayList<String> find(final String sourceString,
			String object) {
		ArrayList<String> rep_str = new ArrayList<String>();

		Pattern p = Pattern.compile(object);// 正则表达式，取=和|之间的字符串，不包括=和|
		Matcher m = p.matcher(sourceString);

		while (m.find()) {
			rep_str.add(m.group(1));
		}
		return rep_str;
	}

	public static int getCount(final String sourceString, String object) {
		int sum = 0;

		Pattern p = Pattern.compile(object);// 正则表达式，取=和|之间的字符串，不包括=和|
		Matcher m = p.matcher(sourceString);

		while (m.find()) {
			sum++;
		}
		return sum;
	}

	public static void lengthFilter(final Context context,
			final EditText editText, final int max_length, final String err_msg) {

		InputFilter[] filters = new InputFilter[1];

		filters[0] = new InputFilter.LengthFilter(max_length) {

			@Override
			public CharSequence filter(CharSequence source, int start, int end,

			Spanned dest, int dstart, int dend) {

				int destLen = getCharacterNum(dest.toString()); // 获取字符个数(一个中文算2个字符)

				int sourceLen = getCharacterNum(source.toString());

				if (destLen + sourceLen > max_length) {

					Toast.makeText(context, err_msg, Toast.LENGTH_SHORT).show();

					return "";

				}

				return source;

			}

		};

		editText.setFilters(filters);

	}

	/**
	 * 
	 * @description 获取一段字符串的字符个数（包含中英文，一个中文算2个字符）
	 * 
	 * @param content
	 * 
	 * @return
	 */

	public static int getCharacterNum(final String content) {

		if (null == content || "".equals(content)) {

			return 0;

		} else {

			// return (content.length() + getChineseNum(content));
			return content.length();

		}

	}

	/**
	 * 
	 * @description 返回字符串里中文字或者全角字符的个数
	 * 
	 * @param s
	 * 
	 * @return
	 */

	public static int getChineseNum(String s) {

		int num = 0;

		char[] myChar = s.toCharArray();

		for (int i = 0; i < myChar.length; i++) {

			if ((char) (byte) myChar[i] != myChar[i]) {

				num++;

			}

		}

		return num;

	}
	
	/**
	* @Title: intstallApp
	* @Description: 安装应用
	* @param @param context
	* @param @param apkFile
	* @param @param observer
	* @return void
	* @throws
	 */
	public static void intstallApp(Context context, String packageName, File apkFile, 
			IPackageInstallObserver.Stub observer) {
		PackageManager pm = context.getPackageManager();
		if (TextUtils.isEmpty(packageName)) {
			PackageParser.Package parsed = getPackageInfo(apkFile);
			packageName = parsed.packageName;
		}
		int installFlags = 0;
		try {
			PackageInfo pi = pm.getPackageInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			if (pi != null) {
				installFlags |= PackageManager.INSTALL_REPLACE_EXISTING;
			}
		} catch (NameNotFoundException e) {
//			e.printStackTrace();
		}
		
		Uri mPackageURI = Uri.fromFile(apkFile);
		String filepath = mPackageURI.getPath();
		Log.i(TAG, "zhangwei the filepath ="+filepath);
		pm.installPackage(mPackageURI, observer, installFlags, null);
	}
	
	private static PackageParser.Package getPackageInfo(File sourceFile) {
		
		DisplayMetrics metrics = new DisplayMetrics();
        metrics.setToDefaults();
        Object pkg = null;
		final String archiveFilePath = sourceFile.getAbsolutePath();
        try {
        	Class<?> clazz = Class.forName("android.content.pm.PackageParser");
			Object instance = getParserObject(archiveFilePath);
			if (Build.VERSION.SDK_INT >= 21) {
				Method method = clazz.getMethod("parsePackage", File.class, int.class);
				pkg = method.invoke(instance, sourceFile ,0);
			} else {
				Method method = clazz.getMethod("parsePackage", File.class, String.class, DisplayMetrics.class, int.class);
				pkg = method.invoke(instance, sourceFile, archiveFilePath, metrics, 0);
			}
			instance = null;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
        return (PackageParser.Package)pkg;

//        PackageParser packageParser = new PackageParser(archiveFilePath);
//        DisplayMetrics metrics = new DisplayMetrics();
//        metrics.setToDefaults();
//        PackageParser.Package pkg =  packageParser.parsePackage(sourceFile,
//                archiveFilePath, metrics, 0);
//        // Nuke the parser reference.
//        packageParser = null;

	}

	private static Object getParserObject(String archiveFilePath) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
		Class<?> clazz = Class.forName("android.content.pm.PackageParser");
		return Build.VERSION.SDK_INT >= 21 ?
				clazz.getConstructor().newInstance() : 
					clazz.getConstructor(String.class).newInstance(archiveFilePath);
	}
	
	/**
	* @Title: bytes2kb
	* @Description: byte转为KB或者MB字符串
	* @param @param bytes
	* @param @return
	* @return String
	* @throws
	 */
	public static String bytes2kb(long bytes) {
		BigDecimal filesize = new BigDecimal(bytes);
		BigDecimal megabyte = new BigDecimal(1024 * 1024);
		float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
				.floatValue();
		if (returnValue > 1)
			return (returnValue + "M");
		BigDecimal kilobyte = new BigDecimal(1024);
		returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
				.floatValue();
		return (returnValue + "K");
	}
	
	/**
	* @Title: getTimeString
	* @Description: 获取时间戳，格式为20140702102040
	* @param @param time
	* @param @return
	* @return String
	* @throws
	 */
	public static String getTimeString(long time) {
		Date date = new Date(time);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		return dateFormat.format(date);
	}
	
	/** 
	* @Title: isDownload
	* @Description: wifi下才能下载
	* @param @param cxt
	* @param @return
	* @return boolean
	* @throws 
	*/ 
	public static boolean isDownload(Context cxt) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(cxt);
		boolean nFlag = sp.getBoolean("wifi_download_key", false);

		boolean isMobile = SystemUtils.isMobileNetworkConnected(cxt);

		if (nFlag && isMobile) {
			return false;
		} else {
			return true;
		}
	}
	
	/** 
	* @Title: isHold
	* @Description: 判断下载的应用安装文件是否保留
	* @param @param cxt
	* @param @return
	* @return boolean
	* @throws 
	*/ 
	public static boolean isHold(Context cxt) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(cxt);
		boolean nFlag = sp.getBoolean("hold_app_key", false);

		return nFlag;
	}

	private static BufferedReader br;
	// 获取build.prop中的指定属性
	public static String getBuildProproperties(String PropertiesName) {
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(
					new File("/system/build.prop")));
			br = new BufferedReader(new InputStreamReader(is));
			String strTemp = "";
			while ((strTemp = br.readLine()) != null) {// 如果文件没有读完则继续
				if (strTemp.indexOf(PropertiesName) != -1)
					return strTemp.substring(strTemp.indexOf("=") + 1);
			}
			br.close();
			is.close();
			return null;
		} catch (Exception e) {
			if (e.getMessage() != null)
				System.out.println(e.getMessage());
			else
				e.printStackTrace();
			return null;
		}
	}
	
    public static int getStatusHeight(Context activity){
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) activity).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject;
                localObject = localClass.newInstance();
                
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

	//请求参数按字母升序排序
	public static String sortLetterByAsc(String str) {
		if(str == null || "".equals(str) || !str.contains("&")) {
			return str;
		}
		String[] strs = str.split("&");
		Arrays.sort(strs,String.CASE_INSENSITIVE_ORDER);
		StringBuilder result = new StringBuilder();
		for(int i = 0 ; i< strs.length ;i++ ){
			result.append(strs[i]);
		}
		return result.toString();
	}

	//对list进行分页，返回指定页
	public static <E> List<E> page(List<E> list, int size, int pageCount, int currentPage){
		try {
			int totalCount = list.size();
			int mod = totalCount % size;
			List<E> subList = null;

			if (mod==0){
				subList= list.subList((currentPage-1)*size, size*(currentPage));
			}else{
				if (currentPage==pageCount){
					subList= list.subList((currentPage-1)*size,totalCount);
				}else{
					subList= list.subList((currentPage-1)*size,size*(currentPage));
				}
			}
			for(int i=0; i<subList.size(); i++) {
				System.out.println("subList["+ i + "] = " + subList.get(i));
			}
			return subList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//获取list可以分多少页
	public static int getPageCount(List list, int size) {
		try {
			int totalCount = list.size();
			int pageCount = 0;
			int mod = totalCount % size;
			if (mod > 0){
				pageCount = totalCount / size + 1;
			}else{
				pageCount = totalCount / size;
			}
			return pageCount;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	//判断是否有缓存数据
	public static boolean getIfCache(Context activity,int type, String app_type,String cat_id,String spe_id ) {
		boolean result = false;
		CacheDataAdapter ldb = new CacheDataAdapter(activity);
		ldb.open();
		String context = ldb.queryCacheByType(type, app_type, cat_id, spe_id);
		if (null != context){
			result = true;
		}
		ldb.close();
		return result;
	}

	//判断还没有没有存储空间
	public static boolean hasSpace(long fileSize, String path) {
		StatFs sta = new StatFs(path);
		long blockSizeLong = sta.getBlockSizeLong();
		long availCountlong = sta.getAvailableBlocksLong();
		long availBytes = availCountlong * blockSizeLong;

		if (availBytes < fileSize) {
			return false;
		}
		return true;
	}


	public static boolean isAppInFore(final Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	//9aaps应用下载地址
	public static String get9AppsDownloadUrl(Context context,int publishId){
		//拼装必须的公参数
		Map<String, String> params= new HashMap<String, String>();
		params.put("partnerId",Globals.KEY_PARTNERID_9APPS);
		params.put("ip",SystemUtils.getIPAddress(context));
		params.put("langCode",SystemUtils.getSysLang());
		params.put("net",SystemUtils.getNetworkTypeName(context));
		params.put("APILevel",String.valueOf(android.os.Build.VERSION.SDK_INT));
		params.put("publishId",String.valueOf(publishId));
		params.put("sid",SystemUtils.getImei(context));

		final StringBuilder sb = new StringBuilder();
		if (params != null) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(entry.getKey());
				sb.append("=");
				sb.append(entry.getValue());
				sb.append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
		}

		//用Base64对uri编码
		String uriInBase64 = Base64.encode(Globals.HTTP_DOWNLOAD_URI_9APPS.getBytes());

		//按字典升序排序参数
		String sortedParams = SystemUtils.sortLetterByAsc(sb.toString());

		//用base64对排序结果编码
		String paramsInBase64 = Base64.encode(sortedParams.getBytes());

		//使用MD5对编码后的uri和params用secret签名
		String sign = null;
		try {
			sign = MD5.getMD5(uriInBase64 + paramsInBase64 + Globals.KEY_9APPS_SECRET);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		//把签名公参添加到请求参数里面来
		sb.append("&");
		sb.append("sign");
		sb.append("=");
		sb.append(sign);

		String url = Globals.HTTP_REQUEST_URL_9APPS + Globals.HTTP_DOWNLOAD_URI_9APPS + "?" + sb.toString();

		return url;
	}

	//判断是否有缓存数据
	public static boolean getIfCategoryList(Context context) {
		boolean result = false;
		CategoryListDataAdapter ldb = new CategoryListDataAdapter(context);
		ldb.open();
		result = ldb.isHasData();
		ldb.close();
		return result;
	}

}
