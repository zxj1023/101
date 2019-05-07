package com.tran.com.android.gc.update.install;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;

import com.tran.com.android.gc.update.db.InstalledAppDao;
import com.tran.com.android.gc.update.model.InstalledAppInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InstallAppManager {

    private static List<InstalledAppInfo> installedAppList;
    private static Map<String, InstalledAppInfo> installedAppMap;

    public static void initInstalledAppList(Context context) {
        installedAppList = new ArrayList<InstalledAppInfo>();
        installedAppMap = new HashMap<String, InstalledAppInfo>();
        InstalledAppDao appDao = new InstalledAppDao(context);
        appDao.openDatabase();
        int dbAppCount = appDao.getInstalledCount();
        if (dbAppCount > 0) {
            installedAppList = appDao.getInstalledAppList();
            for (InstalledAppInfo info : installedAppList) {
                installedAppMap.put(info.getPackageName(), info);
            }
        } else {
            PackageManager pm = context.getPackageManager();
			// ukiliu 2014-09-30 modify begin
			final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
			mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);
			// List<PackageInfo> list = pm.getInstalledPackages(0);
			for (ResolveInfo rinfo : list) {
				PackageInfo pinfo;
				try {
					pinfo = pm.getPackageInfo(rinfo.activityInfo.packageName,
							PackageManager.GET_ACTIVITIES);
					addInstallAppInfo(pinfo, appDao, pm);
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// ukiliu 2014-09-30 modify end
        }
        appDao.closeDatabase();
    }

    public static List<InstalledAppInfo> getInstalledAppList(Context context) {
        if (installedAppList == null) {
            initInstalledAppList(context);
        }
        return installedAppList;
    }

    public static InstalledAppInfo getInstalledAppInfo(Context context,
                                                       String packageName) {
        if (installedAppMap == null) {
            initInstalledAppList(context);
        }
        if (installedAppMap.containsKey(packageName)) {
            return installedAppMap.get(packageName);
        }
        return null;
    }

    public static void setInstalledAppList(
            List<InstalledAppInfo> installedAppList) {
        InstallAppManager.installedAppList = installedAppList;
    }

    public static void setInstalledAppMap(
            Map<String, InstalledAppInfo> installedAppMap) {
        InstallAppManager.installedAppMap = installedAppMap;
    }

    public static void clearAll() {
        if (installedAppList != null) {
            installedAppList.clear();
        }
        if (installedAppMap != null) {
            installedAppMap.clear();
        }
    }

    public static void addInstallAppInfo(PackageInfo pinfo,
                                         InstalledAppDao appDao, PackageManager pm) {
        InstalledAppInfo installedAppInfo = new InstalledAppInfo();
        ApplicationInfo appInfo = pinfo.applicationInfo;
        boolean add = true;

        installedAppInfo.setAppFlag(InstalledAppInfo.FLAG_SYSTEM);
        if ((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            // 代表的是系统的应用,但是被用户升级了. 用户应用
            installedAppInfo.setAppFlag(InstalledAppInfo.FLAG_UPDATE);
        } else if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            // 代表的用户的应用
            installedAppInfo.setAppFlag(InstalledAppInfo.FLAG_USER);
            add = false;
        }

        if (add) {
            installedAppInfo.setName(pinfo.applicationInfo.loadLabel(pm).toString());
            installedAppInfo.setIconId(appInfo.icon);
            installedAppInfo.setPackageName(appInfo.packageName);
            installedAppInfo.setVersionCode(pinfo.versionCode);
            installedAppInfo.setVersion(pinfo.versionName);
            installedAppInfo.setApkPath(appInfo.sourceDir);
            installedAppList.add(installedAppInfo);
            installedAppMap.put(appInfo.packageName, installedAppInfo);
            appDao.insert(installedAppInfo);
        }
    }

}
