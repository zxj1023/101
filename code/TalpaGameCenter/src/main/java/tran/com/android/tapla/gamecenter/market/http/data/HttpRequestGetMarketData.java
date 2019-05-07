package tran.com.android.tapla.gamecenter.market.http.data;

import android.content.Context;

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
import java.util.TimeZone;

import tran.com.android.gc.lib.utils.LogUtils;
import tran.com.android.tapla.gamecenter.datauiapi.bean.UpInputObject;
import tran.com.android.tapla.gamecenter.datauiapi.bean.UpinputItem;
import tran.com.android.tapla.gamecenter.market.db.IgnoreAppAdapter;
import tran.com.android.tapla.gamecenter.market.http.Base64;
import tran.com.android.tapla.gamecenter.market.http.HttpRequstData;
import tran.com.android.tapla.gamecenter.market.http.MD5;
import tran.com.android.tapla.gamecenter.market.install.InstallAppManager;
import tran.com.android.tapla.gamecenter.market.marketApp;
import tran.com.android.tapla.gamecenter.market.model.InstalledAppInfo;
import tran.com.android.tapla.gamecenter.market.util.FileLog;
import tran.com.android.tapla.gamecenter.market.util.Globals;
import tran.com.android.tapla.gamecenter.market.util.Log;
import tran.com.android.tapla.gamecenter.market.util.SystemUtils;

import static tran.com.android.tapla.gamecenter.market.util.Globals.HTTP_MAINLIST_METHOD;

public class HttpRequestGetMarketData {
	private static final String TAG = "HttpRequestGetMarketData";

	// 0主界面
	public static String getMainListObject(int type, String rank_type,
			String catid, int page, int count) throws Exception {
		type = 0;
//		String isher = SystemUtils.getBuildProproperties("ro.iuni.os");
//		String model = SystemUtils.getBuildProproperties("ro.product.model");
//		model = model.replace(" ", "%20");
//		model = model.replace("+", "%2B");
//
//		String vernumber = SystemUtils.getBuildProproperties("ro.gn.iuniznvernumber");
//		String romDate = vernumber.substring(vernumber.lastIndexOf("-") + 1);
//		Log.i(TAG, "iuni minor version is "+isher +" model: "+model+ " date:" +romDate);
//		if (isher != null && isher.equals("her")) {
//			type = 1;
//		} else {
//			type = 0;
//		}
		StringWriter str = new StringWriter();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ObjectMapper mapper = new ObjectMapper();
//			map.put("osedition", type);
//			map.put("model", model);
//			map.put("romDate", romDate);
			map.put("count", count);
			map.put("page", page);

			mapper.writeValue(str, map);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		String action = "";
//		if (type == 0) {
			action = HTTP_MAINLIST_METHOD;
//		}

		String url = HttpRequstData.getURLStr(Globals.HTTP_REQUEST_URL,
				Globals.HTTP_SERVICE_NAME_APPLIST, action);
		url += Globals.HTTP_ACTION_PARAM + str.toString();

		String returnData = new String();
		if (Globals.isTestData) {
			// 先使用模拟数据
			returnData = SystemUtils.getFromAssets(marketApp.getInstance()
					.getinstance(), "market" + page + ".json");

		} else {
			returnData = HttpRequstData.doRequest(url);
		}
		return returnData;
	}
		
	// 0主界面 1 新品 2排行 3分类 4.专题  5 必备  6 设计
	public static String getMarketListObject(int type, String rank_type,
			String catid, int page, int count) throws Exception {
		/*
		 * StringBuffer param = new StringBuffer(); param.append("&uid=" + uid);
		 * param.append("&userType=" + userType);
		 * param.append("&userKey="+userKey); param.append("&nickId=" + nickId);
		 */

		StringWriter str = new StringWriter();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			if ((type == 0) || (type == 1)||(type == 6)) {
				map.put("count", count);
				map.put("page", page);
			} else if ((type == 2)||(type == 5)) {
//				map.put("type", rank_type);
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
//			action = Globals.HTTP_FEEDLIST_METHOD;
			action = Globals.HTTP_MAINLIST_METHOD;
		} else if (type == 1) {
			action = Globals.HTTP_APPNEW_METHOD;
		} else if (type == 2) {
			action = Globals.HTTP_RANKLIST_METHOD;

		}
		 else if (type == 5) {
			action = Globals.HTTP_STARTER_METHOD;
			}
		 else if (type == 6) {
				action = Globals.HTTP_DESIGN_METHOD;
			}
		else if (type == 9) {
			action = Globals.HTTP_DESIGN_METHOD;
		}
		else {
			action = Globals.HTTP_CATEGORYLIST_METHOD;
		}

		String url = HttpRequstData.getURLStr(Globals.HTTP_REQUEST_URL,
				Globals.HTTP_SERVICE_NAME_APPLIST, action);
		url += Globals.HTTP_ACTION_PARAM + str.toString();

		String returnData = new String();
		if (Globals.isTestData) {
			// 先使用模拟数据
            if (type == 0) {
                returnData = SystemUtils.getFromAssets(marketApp.getInstance()
                        .getinstance(), "newAppList.json");
            }else if(type == 1){
				returnData = SystemUtils.getFromAssets(marketApp.getInstance()
						.getinstance(), "freash.json");
			}else if(type == 5){
				returnData = SystemUtils.getFromAssets(marketApp.getInstance()
						.getinstance(), "starter.json");
			}else if(type == 2){
				returnData = SystemUtils.getFromAssets(marketApp.getInstance()
						.getinstance(), "rank.json");
			}else{
				returnData = SystemUtils.getFromAssets(marketApp.getInstance()
						.getinstance(), "market"+page+".json");
			}
		} else {
			returnData = HttpRequstData.doRequest(url);
		}
		return returnData;
	}

	public static String getCategoryListObject(String type,String style) throws Exception {
		/*
		 * StringBuffer param = new StringBuffer(); param.append("&uid=" + uid);
		 * param.append("&userType=" + userType);
		 * param.append("&userKey="+userKey); param.append("&nickId=" + nickId);
		 */

		StringWriter str = new StringWriter();
		Map<String, Object> map = new HashMap<String, Object>();
		int forher = 0;
		String isher = SystemUtils.getBuildProproperties("ro.iuni.os");
		Log.e(TAG, "iuni minor version is "+isher);
		if (isher != null && isher.equals("her")) {
			forher = 1;
		} else {
			forher = 0;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			if (forher == 1) {
				map.put("osedition", forher);
			}
			map.put("type", type);
			if(!style.equals(""))
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
			returnData = SystemUtils.getFromAssets(marketApp.getInstance()
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
			returnData = SystemUtils.getFromAssets(marketApp.getInstance()
					.getinstance(), "special.json");
		} else {
			returnData = HttpRequstData.doRequest(url);
		}
		return returnData;
	}
	public static String getSpeciaAllObject(String specialId, int page, int count) throws Exception {

//		String model = SystemUtils.getBuildProproperties("ro.product.model");
//		model = model.replace(" ", "%20");
//		model = model.replace("+", "%2B");
		
//		String vernumber = SystemUtils.getBuildProproperties("ro.gn.iuniznvernumber");
//		String romDate = vernumber.substring(vernumber.lastIndexOf("-") + 1);
		StringWriter str = new StringWriter();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			map.put("specialId", specialId);
			map.put("count", count);
			map.put("page", page);
//			map.put("model", model);
//			map.put("romDate", romDate);
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

		//Log.i(TAG, "iuni minor version is " +" model: "+model+ " date:" +romDate);
		String returnData = new String();
		if (Globals.isTestData) {
			// 先使用模拟数据
			returnData = SystemUtils.getFromAssets(marketApp.getInstance()
					.getinstance(), "newSpecial.json");

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
			returnData = SystemUtils.getFromAssets(marketApp.getInstance()
					.getinstance(), "newAppdetail.json");

		} else {

			returnData = HttpRequstData.doRequest(url);

		}
		return returnData;
	}


	// 搜索应用接口
	public static String getDetailsObjectFrom9Apps(Context context, String packageName, String publishIds) throws Exception{
		//拼装必须的公参数
		Map<String, String> params= new HashMap<String, String>();
		params.put("partnerId",Globals.KEY_PARTNERID_9APPS);
		params.put("ip",SystemUtils.getIPAddress(context));
		params.put("langCode",SystemUtils.getSysLang());
		params.put("net",SystemUtils.getNetworkTypeName(context));
		params.put("sid",SystemUtils.getImei(context));
		params.put("APILevel",String.valueOf(android.os.Build.VERSION.SDK_INT));
		params.put("packageNames",packageName);

//		params.put("publishIds",publishIds);

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
		String uriInBase64 = Base64.encode(Globals.HTTP_INFO_URI_9APPS.getBytes());

		//按字典升序排序参数
		String sortedParams = SystemUtils.sortLetterByAsc(sb.toString());

		//用base64对排序结果编码
		String paramsInBase64 = Base64.encode(sortedParams.getBytes());

		//使用MD5对编码后的uri和params用secret签名
		String sign = MD5.getMD5(uriInBase64 + paramsInBase64 + Globals.KEY_9APPS_SECRET);

		//把签名公参添加到请求参数里面来
		sb.append("&");
		sb.append("sign");
		sb.append("=");
		sb.append(sign);

		String url = Globals.HTTP_REQUEST_URL_9APPS + Globals.HTTP_INFO_URI_9APPS + "?" + sb.toString();

		String returnData = new String();
		if (Globals.isTestData) {
			// 先使用模拟数据
			returnData = SystemUtils.getFromAssets(marketApp.getInstance()
					.getinstance(), "9Apps_SearchList.json");
		} else {
			returnData = HttpRequstData.doRequest(url);
		}
		return returnData;
	}

	// 获取分类列表接口
	public static String getCategoryListFrom9Apps(Context context, int categoryType, int page, int size) throws Exception{
		//拼装必须的公参数
		Map<String, String> params= new HashMap<String, String>();
		params.put("partnerId",Globals.KEY_PARTNERID_9APPS);
		params.put("ip",SystemUtils.getIPAddress(context));
		params.put("langCode",SystemUtils.getSysLang());
		params.put("net",SystemUtils.getNetworkTypeName(context));
		params.put("sid",SystemUtils.getImei(context));
		params.put("APILevel",String.valueOf(android.os.Build.VERSION.SDK_INT));


		params.put("categoryType",String.valueOf(categoryType));
		params.put("p",String.valueOf(page));
		params.put("size",String.valueOf(size));

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
		String uriInBase64 = Base64.encode(Globals.HTTP_CATEGORY_LIST_URI_9APPS.getBytes());

		//按字典升序排序参数
		String sortedParams = SystemUtils.sortLetterByAsc(sb.toString());

		//用base64对排序结果编码
		String paramsInBase64 = Base64.encode(sortedParams.getBytes());

		//使用MD5对编码后的uri和params用secret签名
		String sign = MD5.getMD5(uriInBase64 + paramsInBase64 + Globals.KEY_9APPS_SECRET);

		//把签名公参添加到请求参数里面来
		sb.append("&");
		sb.append("sign");
		sb.append("=");
		sb.append(sign);

		String url = Globals.HTTP_REQUEST_URL_9APPS + Globals.HTTP_CATEGORY_LIST_URI_9APPS + "?" + sb.toString();

		String returnData = new String();
		if (Globals.isTestData) {
			// 先使用模拟数据
			returnData = SystemUtils.getFromAssets(marketApp.getInstance()
					.getinstance(), "9Apps_SearchList.json");
		} else {
			returnData = HttpRequstData.doRequestForPost(url);
		}
		return returnData;
	}

	public static String getUpJason(Context context) {
		// 检测是否安装
		List<InstalledAppInfo> listinfo = InstallAppManager.getInstalledAppList(marketApp.getInstance().getinstance());
		UpInputObject obj = new UpInputObject();
		List<UpinputItem> instApps = new ArrayList<UpinputItem>();

		IgnoreAppAdapter mIgnoreAdapter = new IgnoreAppAdapter(context);

		mIgnoreAdapter.open();
		ArrayList<String> packs = mIgnoreAdapter.queryAllPackageData();
		mIgnoreAdapter.close();

		/*if(packs == null || packs.size() ==0){
			return "";
		}*/

		//LogUtils.logd("packs","packs:"+packs.toString());
		for (InstalledAppInfo appinfo : listinfo) {
			UpinputItem tmp = new UpinputItem();
			String packagename = appinfo.getPackageName();
			if ((null != packs) && (packs.indexOf(packagename) != -1)) {
				continue;
			}
            //[{"packageName":"Dusty Wisoky","versionCode":"2","versionName":"5"}]
//			tmp.setPackageName("Dusty Wisoky");
//			tmp.setVersionCode(2);
//			tmp.setVersionName("5");

			tmp.setPackageName(packagename);
			tmp.setVersionCode(appinfo.getVersionCode());
			tmp.setVersionName(appinfo.getVersion());
			instApps.add(tmp);
		}
		obj.setInstApps(instApps);

		StringWriter str = new StringWriter();

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(str, instApps);
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
//		url += Globals.HTTP_ACTION_PARAM + "{}";

		String returnData = new String();
		if (Globals.isTestData) {
			// 先使用模拟数据
			returnData = SystemUtils.getFromAssets(marketApp.getInstance()
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
//		url += Globals.HTTP_ACTION_PARAM + "{}";

		String returnData = new String();
		if (Globals.isTestData) {
			// 先使用模拟数据
			returnData = SystemUtils.getFromAssets(marketApp.getInstance()
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


	public static String getAppPush(Context context) throws Exception {
		StringWriter str = new StringWriter();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			map.put("timezone", TimeZone.getDefault().getID());
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
				Globals.HTTP_APPPUSH_METHOD);
		url += Globals.HTTP_ACTION_PARAM + str.toString();
		String returnData = new String();
		if (true) {
			// 先使用模拟数据
			returnData = SystemUtils.getFromAssets(context, "push.json");

		} else {
			returnData = HttpRequstData.doRequest(url);

		}
		return returnData;
	}

}
