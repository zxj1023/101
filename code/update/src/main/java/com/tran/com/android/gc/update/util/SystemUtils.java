
package com.tran.com.android.gc.update.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageParser;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.tran.com.android.gc.update.R;
import com.tran.com.android.gc.update.activity.module.UpdateAlertActivity;
import com.tran.com.android.gc.update.activity.setting.DownloadManagerActivity;
import com.tran.com.android.gc.update.model.DownloadData;
import com.tran.com.android.gc.update.service.AppDownloadService;
import com.tran.com.android.gc.update.service.AutoUpdateService;
import com.tran.com.android.gc.update.updateApp;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tran.com.android.gc.lib.app.AuroraAlertDialog;

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

    public static String getModelNumber() {
        return URLEncoder.encode(Build.MODEL);
    }

    public static String getBuildNumber() {
        return URLEncoder.encode(Build.DISPLAY);
    }

    public static boolean isEmulator(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        return EMULATOR_DEVICE_ID.equalsIgnoreCase(manager.getDeviceId());
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
        if(context==null){
            return  false;
        }
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

    public static boolean isWifiAvailable(Context context) {
        WifiManager manager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = manager.getConnectionInfo();
        String macAddress = (wifiInfo == null ? null : wifiInfo.getMacAddress());

        return (macAddress != null);
    }

    public static boolean isMobileNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

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
                .getSystemService(Context.CONNECTIVITY_SERVICE);

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
        Context context = updateApp.getInstance();
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            State wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .getState();
            State mobileState = cm.getNetworkInfo(
                    ConnectivityManager.TYPE_MOBILE).getState();
            if (wifiState == State.CONNECTED || mobileState == State.CONNECTED) {
                return true;
            } else {
                return false;
            }
        } else {
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

    public static boolean isCPUFreqOK() {
        ProcessBuilder cmd;
        String result = null;

        try {
            String[] args = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
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
        boolean nFlag = sp.getBoolean("none_download_pic_key", false);
        SharedPreferences sp1 = cxt.getSharedPreferences(Globals.SHARED_WIFI_UPDATE,
                cxt.MODE_APPEND);

        int iswifi = sp1.getInt(
                Globals.SHARED_NETSTATUS_KEY_ISEXITS, 0);
        if (nFlag && (iswifi == 2)) {
            return false;
        } else {
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
     * @param content
     * @return
     * @description 获取一段字符串的字符个数（包含中英文，一个中文算2个字符）
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
     * @param s
     * @return
     * @description 返回字符串里中文字或者全角字符的个数
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
     * @param @param context
     * @param @param apkFile
     * @param @param observer
     * @return void
     * @throws
     * @Title: intstallApp
     * @Description: 安装应用
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
        pm.installPackage(mPackageURI, observer, installFlags, null);
    }

    private static PackageParser.Package getPackageInfo(File sourceFile) {
        final String archiveFilePath = sourceFile.getAbsolutePath();
        //PackageParser packageParser = new PackageParser(archiveFilePath);
        PackageParser packageParser = new PackageParser();
        DisplayMetrics metrics = new DisplayMetrics();
        metrics.setToDefaults();
//        PackageParser.Package pkg = packageParser.parsePackage(sourceFile,
//                archiveFilePath, metrics, 0);
        PackageParser.Package pkg = null;
        try {
            pkg = packageParser.parsePackage(sourceFile,
                    0);
        } catch (PackageParser.PackageParserException e) {
            e.printStackTrace();
        }
        // Nuke the parser reference.
        packageParser = null;
        return pkg;
    }

    /**
     * @param @param  bytes
     * @param @return
     * @return String
     * @throws
     * @Title: bytes2kb
     * @Description: byte转为KB或者MB字符串
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
     * @param @param  time
     * @param @return
     * @return String
     * @throws
     * @Title: getTimeString
     * @Description: 获取时间戳，格式为20140702102040
     */
    public static String getTimeString(long time) {
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        return dateFormat.format(date);
    }

    /**
     * @param @param  cxt
     * @param @return
     * @return boolean
     * @throws
     * @Title: isDownload
     * @Description: wifi下才能下载
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

    public static String getBuildVersion() {
        String sdkVersion = android.os.SystemProperties.get("ro.gn.iuniznvernumber");
        return sdkVersion;
    }

    public static void openNetDialog() {
        AuroraAlertDialog networkDialog = new AuroraAlertDialog.Builder(
                updateApp.getInstance(),
                AuroraAlertDialog.THEME_AMIGO_FULLSCREEN)
                .setTitle(
                        updateApp.getInstance().getResources().getString(
                                R.string.dialog_prompt))
                .setMessage(
                        updateApp.getInstance().getResources().getString(
                                R.string.no_network_download_toast))
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                Intent intent = new Intent(
                                        android.provider.Settings.ACTION_SETTINGS);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                updateApp.getInstance().startActivity(intent);
                            }
                        }).create();
        networkDialog.getWindow().setType(
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        );
        networkDialog.show();
    }

    public static void openForceUpDialog(final UpdateAlertActivity context, final int apkId) {
//        Cursor cursor = AutoUpdateService.getAutoUpdateDao().query(new String[]{"filesize"},
//                "packageName=?", new String[]{packageName}, null);
//        cursor.moveToNext();
//        String size = cursor.getString(0);
//        int appSize = Integer.getInteger(size);
//        int sizeM = appSize / 1024 /1024;
//        int sizeK = appSize % (1024 * 1024);

        if (apkId == 0) {
            Log.i(TAG, "apkId = 0");
            return;
        }

        final AuroraAlertDialog forceUpDialog = new AuroraAlertDialog.Builder(
                context,
                AuroraAlertDialog.THEME_TRADITIONAL)
                .setTitle(
                        updateApp.getInstance().getResources().getString(
                                R.string.dialog_prompt))
                .setMessage(
                        updateApp.getInstance().getResources().getString(
                                R.string.force_upgrade_query))
                        //+ " " + sizeM + "." + sizeK  + " M" )
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                killProcess(context, apkId);
                                context.finish();
                            }
                        })
                .setPositiveButton(R.string.dialog_update,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(updateApp.getInstance(),
                                        DownloadManagerActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                updateApp.getInstance().startActivity(intent);

                                if (!AppDownloadService.getDownloaders().containsKey(apkId)) {
                                    DownloadData downloadData = AutoUpdateService.getAutoUpdateDao()
                                            .getDownloadData(apkId);
                                    AppDownloadService.startDownload(updateApp.getInstance(),
                                            downloadData);
                                }
                            }
                        }).create();
        forceUpDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                forceUpDialog.dismiss();
                killProcess(context, apkId);
                context.finish();
                return true;
            }
        });
        forceUpDialog.show();
    }

    public static void killProcess(Context context, int apkId) {
        ActivityManager am =
                (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        String packageName = AutoUpdateService.getAutoUpdateDao()
                .getDownloadData(apkId).getPackageName();

        if (packageName == null) {
            Log.i("Test", "packageName is null");
        } else {
            Log.i("Test", "packageName = " + packageName);
        }

        Method forceStopPackage = null;
        try {
            forceStopPackage = am.getClass().
                    getDeclaredMethod("forceStopPackage", String.class);
            forceStopPackage.setAccessible(true);
            //fix me
            if (isRunning(context, packageName) || isAppRunningFGround(context, packageName)) {
                forceStopPackage.invoke(am, packageName);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static boolean isRunning(Context context, String packageName) {
        boolean isAppRunning = false;

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(10);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName)
                    && info.baseActivity.getPackageName().equals(packageName)) {
                isAppRunning = true;
                break;
            }
        }
        return isAppRunning;
    }

    public static boolean isAppRunningFGround(final Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(3);
        for (ActivityManager.RunningTaskInfo info : tasks) {
            ComponentName baseActivity = info.baseActivity;
            String updatePackageName = baseActivity.getPackageName();
            if (updatePackageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static void openSdkErrDialog() {
        AuroraAlertDialog forceUpDialog = new AuroraAlertDialog.Builder(
                updateApp.getInstance(),
                AuroraAlertDialog.THEME_AMIGO_FULLSCREEN)
                .setTitle(
                        updateApp.getInstance().getResources().getString(
                                R.string.dialog_prompt))
                .setMessage(
                        updateApp.getInstance().getResources().getString(
                                R.string.sdk_error_notify))
                        //.setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, null
                ).create();
        forceUpDialog.getWindow().setType(
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        );
        forceUpDialog.show();
    }
}
