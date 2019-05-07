package com.tran.com.android.gc.update.http.data;

import com.tran.com.android.gc.update.http.HttpRequstData;
import com.tran.com.android.gc.update.updateApp;
import com.tran.com.android.gc.update.util.Globals;
import com.tran.com.android.gc.update.util.SystemUtils;

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
                                                int page, int count) throws Exception {
        StringWriter str = new StringWriter();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            map.put("query", HttpRequstData.getDecodeStr(query));
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
        url += Globals.HTTP_ACTION_PARAM + str.toString();

        String returnData = new String();
        if (Globals.isTestData) {
            // 先使用模拟数据
            returnData = SystemUtils.getFromAssets(updateApp.getInstance()
                    .getinstance(), "searchApp.json");

        } else {
            returnData = HttpRequstData.doRequest(url);

        }
        return returnData;
    }

    // 搜索实时接口
    public static String getSearchTimeLyObject(String query) throws Exception {
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
        url += Globals.HTTP_ACTION_PARAM + str.toString();

        String returnData = new String();
        if (Globals.isTestData) {
            // 先使用模拟数据
            returnData = SystemUtils.getFromAssets(updateApp.getInstance()
                    .getinstance(), "searchApp.json");

        } else {
            returnData = HttpRequstData.doRequest(url);

        }
        return returnData;
    }

    // 搜索推荐接口
    public static String getSearcjRecObject() throws Exception {
        String url = HttpRequstData.getURLStr(Globals.HTTP_REQUEST_URL,
                Globals.HTTP_SERVICE_NAME_SEARCHRECLIST,
                Globals.HTTP_SEARCHRECLIST_METHOD);

        String returnData = new String();
        if (Globals.isTestData) {
            // 先使用模拟数据
            returnData = SystemUtils.getFromAssets(updateApp.getInstance()
                    .getinstance(), "searchRec.json");

        } else {
            returnData = HttpRequstData.doRequest(url);
        }
        return returnData;
    }

}
