package tran.com.android.tapla.gamecenter.market.http.data;

import android.content.Context;

import tran.com.android.tapla.gamecenter.market.http.Base64;
import tran.com.android.tapla.gamecenter.market.http.HttpRequstData;
import tran.com.android.tapla.gamecenter.market.http.MD5;
import tran.com.android.tapla.gamecenter.market.marketApp;
import tran.com.android.tapla.gamecenter.market.util.Globals;
import tran.com.android.tapla.gamecenter.market.util.SystemUtils;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestSearchData {
	private static final String TAG = "HttpRequestSearchData";

	// 搜索应用接口
	public static String getSearchAppListObject(String query,
			int page, int count) throws Exception{
		StringWriter str = new StringWriter();
		Map<String, Object> map = new HashMap<String, Object>();
//		String model = SystemUtils.getBuildProproperties("ro.product.model");
//		model = model.replace(" ", "%20");
//		model = model.replace("+", "%2B");
//		String vernumber = SystemUtils.getBuildProproperties("ro.gn.iuniznvernumber");
//		String romDate = vernumber.substring(vernumber.lastIndexOf("-") + 1);
		try {
			ObjectMapper mapper = new ObjectMapper();
			map.put("query", HttpRequstData.getDecodeStr(query));
//			map.put("model", model);
//			map.put("romDate", romDate);
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
				Globals.HTTP_SERVICE_NAME_APPLIST,
				Globals.HTTP_SEARCHAPPLIST_METHOD);
		url += Globals.HTTP_ACTION_PARAM+str.toString();

		String returnData = new String();
		if (Globals.isTestData) {
			// 先使用模拟数据
			returnData = SystemUtils.getFromAssets(marketApp.getInstance()
					.getinstance(), "newSearchApp.json");
		} else {
				returnData = HttpRequstData.doRequest(url);

		}
		return returnData;
	}

	// 搜索应用接口
	public static String getSearchAppListObjectFrom9Apps(Context context, String query, int page, int count, int searchType) throws Exception{
		//拼装必须的公参数
		Map<String, String> params= new HashMap<String, String>();
		params.put("partnerId",Globals.KEY_PARTNERID_9APPS);
		params.put("ip",SystemUtils.getIPAddress(context));
		params.put("langCode",SystemUtils.getSysLang());
		params.put("net",SystemUtils.getNetworkTypeName(context));
		params.put("sid",SystemUtils.getImei(context));
		params.put("APILevel",String.valueOf(android.os.Build.VERSION.SDK_INT));
		params.put("keyword",query);
		//搜索模式，不填默认为0 0：搜索使用纠正模式 1：搜索使用普通模式（不纠错）
		params.put("searchType",String.valueOf(searchType));
		params.put("p",String.valueOf(page));
		params.put("size",String.valueOf(count));

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
		String uriInBase64 = Base64.encode(Globals.HTTP_SEARCH_URI_9APPS.getBytes());

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

		String url = Globals.HTTP_REQUEST_URL_9APPS + Globals.HTTP_SEARCH_URI_9APPS + "?" + sb.toString();

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




	// 搜索实时接口
		public static String getSearchTimeLyObject(String query) throws Exception{
			StringWriter str = new StringWriter();
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				ObjectMapper mapper = new ObjectMapper();
				map.put("query", HttpRequstData.getDecodeStr(query));
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
					Globals.HTTP_SERVICE_NAME_SEARCHRECLIST,
					Globals.HTTP_SEARCHSUGGEST_METHOD);
			url += Globals.HTTP_ACTION_PARAM+str.toString();

			String returnData = new String();
			if (Globals.isTestData) {
				// 先使用模拟数据
				returnData = SystemUtils.getFromAssets(marketApp.getInstance()
						.getinstance(), "SearchSuggest.json");

			} else {
				returnData = HttpRequstData.doRequest(url);
			}
			return returnData;
		}
	// 搜索推荐接口
	public static String getSearcjRecObject() throws Exception{
		String url = HttpRequstData.getURLStr(Globals.HTTP_REQUEST_URL,
				Globals.HTTP_SERVICE_NAME_SEARCHRECLIST,
				Globals.HTTP_SEARCHRECLIST_METHOD);

		String returnData = new String();
		if (Globals.isTestData) {
			// 先使用模拟数据
			returnData = SystemUtils.getFromAssets(marketApp.getInstance()
					.getinstance(), "recommend.json");

		} else {
				returnData = HttpRequstData.doRequest(url);		
		}
		return returnData;
	}

}
