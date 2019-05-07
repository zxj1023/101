package com.tran.com.android.gc.update.http.data;

import android.content.Context;

import com.tran.com.android.gc.update.db.IgnoreAppAdapter;
import com.tran.com.android.gc.update.http.HttpRequstData;
import com.tran.com.android.gc.update.install.InstallAppManager;
import com.tran.com.android.gc.update.model.InstalledAppInfo;
import com.tran.com.android.gc.update.updateApp;
import com.tran.com.android.gc.update.util.FileLog;
import com.tran.com.android.gc.update.util.Globals;
import com.tran.com.android.gc.update.util.SystemUtils;
import com.tran.com.android.gc.update.datauiapi.bean.UpInputObject;
import com.tran.com.android.gc.update.datauiapi.bean.UpinputItem;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestGetMarketData {
    private static final String TAG = "HttpRequestGetMarketData";

    // 0主界面 1 新品 2排行 3分类 4.专题  5 必备  6 设计
    public static String getMarketListObject(int type, String rank_type,
                                             int catid, int page, int count) throws Exception {
        /*
		 * StringBuffer param = new StringBuffer(); param.append("&uid=" + uid);
		 * param.append("&userType=" + userType);
		 * param.append("&userKey="+userKey); param.append("&nickId=" + nickId);
		 */

        StringWriter str = new StringWriter();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            if ((type == 0) || (type == 1) || (type == 6)) {
                map.put("count", count);
                map.put("page", page);
            } else if ((type == 2) || (type == 5)) {
                map.put("type", rank_type);
                map.put("count", count);
                map.put("page", page);
            } else if (type == 3) {
                map.put("catId", catid);
                map.put("count", count);
                map.put("page", page);
            }
            mapper.writeValue(str, map);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String action = "";
        if (type == 0) {
            action = Globals.HTTP_APPLIST_METHOD;
        } else if (type == 1) {
            action = Globals.HTTP_APPNEW_METHOD;
        } else if (type == 2) {
            action = Globals.HTTP_RANKLIST_METHOD;
        } else if (type == 5) {
            action = Globals.HTTP_STARTER_METHOD;
        } else if (type == 6) {
            action = Globals.HTTP_DESIGN_METHOD;
        } else {
            action = Globals.HTTP_CATEGORYLIST_METHOD;
        }

        String url = HttpRequstData.getURLStr(Globals.HTTP_REQUEST_URL,
                Globals.HTTP_SERVICE_NAME_APPLIST, action);
        url += Globals.HTTP_ACTION_PARAM + str.toString();

        String returnData = new String();
        if (Globals.isTestData) {
            // 先使用模拟数据
            returnData = SystemUtils.getFromAssets(updateApp.getInstance()
                    .getinstance(), "market" + page + ".json");

        } else {
            returnData = HttpRequstData.doRequest(url);
        }
        return returnData;
    }

    public static String getCategoryListObject(String type, String style) throws Exception {
		/*
		 * StringBuffer param = new StringBuffer(); param.append("&uid=" + uid);
		 * param.append("&userType=" + userType);
		 * param.append("&userKey="+userKey); param.append("&nickId=" + nickId);
		 */

        StringWriter str = new StringWriter();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            map.put("type", type);
            if (!style.equals(""))
                map.put("style", style);
            mapper.writeValue(str, map);
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String url = HttpRequstData.getURLStr(Globals.HTTP_REQUEST_URL,
                Globals.HTTP_SERVICE_NAME_CATOGORY_LIST,
                Globals.HTTP_APPLIST_METHOD);
        url += Globals.HTTP_ACTION_PARAM + str.toString();
        String returnData = new String();
        if (Globals.isTestData) {
            // 先使用模拟数据
            returnData = SystemUtils.getFromAssets(updateApp.getInstance()
                    .getinstance(), "category1.json");

        } else {

            returnData = HttpRequstData.doRequest(url);
        }
        return returnData;
    }

    public static String getSpeciaListObject(String type, int page, int count) throws Exception {

        StringWriter str = new StringWriter();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            map.put("type", type);
            map.put("count", count);
            map.put("page", page);
            mapper.writeValue(str, map);
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String url = HttpRequstData.getURLStr(Globals.HTTP_REQUEST_URL,
                Globals.HTTP_SERVICE_NAME_SPECIAL_LIST,
                Globals.HTTP_APPLIST_METHOD);
        url += Globals.HTTP_ACTION_PARAM + str.toString();
        String returnData = new String();
        if (Globals.isTestData) {
            // 先使用模拟数据
            returnData = SystemUtils.getFromAssets(updateApp.getInstance()
                    .getinstance(), "special.json");

        } else {

            returnData = HttpRequstData.doRequest(url);
        }
        return returnData;
    }

    public static String getSpeciaAllObject(int specialId, int page, int count) throws Exception {

        StringWriter str = new StringWriter();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            map.put("specialId", specialId);
            map.put("count", count);
            map.put("page", page);
            mapper.writeValue(str, map);
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String url = HttpRequstData.getURLStr(Globals.HTTP_REQUEST_URL,
                Globals.HTTP_SERVICE_NAME_SPECIAL_LIST,
                Globals.HTTP_SPECIALLIST_METHOD);
        url += Globals.HTTP_ACTION_PARAM + str.toString();
        String returnData = new String();
        if (Globals.isTestData) {
            // 先使用模拟数据
            returnData = SystemUtils.getFromAssets(updateApp.getInstance()
                    .getinstance(), "special.json");

        } else {

            returnData = HttpRequstData.doRequest(url);
        }
        return returnData;
    }


    public static String getDetailsObject(String packagename) throws Exception {
		/*
		 * StringBuffer param = new StringBuffer(); param.append("&uid=" + uid);
		 * param.append("&userType=" + userType);
		 * param.append("&userKey="+userKey); param.append("&nickId=" + nickId);
		 */
        StringWriter str = new StringWriter();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ObjectMapper mapper = new ObjectMapper();

            map.put("packageName", packagename);
            mapper.writeValue(str, map);
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String url = HttpRequstData.getURLStr(Globals.HTTP_REQUEST_URL,
                Globals.HTTP_SERVICE_NAME_APPLIST,
                Globals.HTTP_APPDETAILS_METHOD);
        url += Globals.HTTP_ACTION_PARAM + str.toString();
        String returnData = new String();
        if (Globals.isTestData) {
            // 先使用模拟数据
            returnData = SystemUtils.getFromAssets(updateApp.getInstance()
                    .getinstance(), "appdetail.json");

        } else {

            returnData = HttpRequstData.doRequest(url);

        }
        return returnData;
    }

    public static String getUpJason(Context context) {
        // 检测是否安装
        List<InstalledAppInfo> listinfo = InstallAppManager
                .getInstalledAppList(updateApp.getInstance().getinstance());
        UpInputObject obj = new UpInputObject();
        List<UpinputItem> instApps = new ArrayList<UpinputItem>();

        IgnoreAppAdapter mIgnoreAdapter = new IgnoreAppAdapter(context);

        mIgnoreAdapter.open();
        ArrayList<String> packs = mIgnoreAdapter.queryAllPackageData();
        mIgnoreAdapter.close();
        for (InstalledAppInfo appinfo : listinfo) {
            UpinputItem tmp = new UpinputItem();
            String packagename = appinfo.getPackageName();
            if ((null != packs) && (packs.indexOf(packagename) != -1)) {
                continue;
            }

            tmp.setPackageName(packagename);
            tmp.setVersionCode(appinfo.getVersionCode());
            tmp.setVersionName(appinfo.getVersion());
            tmp.setBuildVersion(Globals.BuildVersion);
            instApps.add(tmp);
        }
        obj.setInstApps(instApps);

        StringWriter str = new StringWriter();

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(str, obj);
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return str.toString();
    }

    public static String getUpAppListObject(Context context) {

        String url = HttpRequstData.getURLStr(Globals.HTTP_REQUEST_URL,
                Globals.HTTP_SERVICE_NAME_APPLIST,
                Globals.HTTP_UPGRADEAPP_METHOD);

        String returnData = new String();
        if (Globals.isTestData) {
            // 先使用模拟数据
            returnData = SystemUtils.getFromAssets(updateApp.getInstance()
                    .getinstance(), "updateApp.json");
        } else {
            try {
                returnData = HttpRequstData.doPost(url, getUpJason(context));
            } catch (MalformedURLException e) {
                FileLog.e(TAG, e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                // MyLog.e("error2", e.getMessage());
                FileLog.e(TAG, e.toString());
                e.printStackTrace();
            }
        }
        return returnData;
    }

    public static String getUpdateCountObject(Context context) {
        String url = HttpRequstData.getURLStr(Globals.HTTP_REQUEST_URL,
                Globals.HTTP_SERVICE_NAME_APPLIST,
                Globals.HTTP_UPAPPCOUNT_METHOD);

        String returnData = new String();
        if (Globals.isTestData) {
            // 先使用模拟数据
            returnData = SystemUtils.getFromAssets(updateApp.getInstance()
                    .getinstance(), "updateCount.json");

        } else {
            try {
                returnData = HttpRequstData.doPost(url, getUpJason(context));
            } catch (MalformedURLException e) {
                FileLog.e(TAG, e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                // MyLog.e("error2", e.getMessage());
                FileLog.e(TAG, e.toString());
                e.printStackTrace();
            }
        }
        return returnData;
    }

}
