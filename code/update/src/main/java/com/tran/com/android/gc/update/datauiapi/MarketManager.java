package com.tran.com.android.gc.update.datauiapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.tran.com.android.gc.update.db.CacheDataAdapter;
import com.tran.com.android.gc.update.http.data.HttpRequestGetMarketData;
import com.tran.com.android.gc.update.util.Globals;
import com.tran.com.android.gc.update.util.SystemUtils;
import com.tran.com.android.gc.update.util.TimeUtils;
import com.tran.com.android.gc.update.datauiapi.bean.CategoryListObject;
import com.tran.com.android.gc.update.datauiapi.bean.MarketListObject;
import com.tran.com.android.gc.update.datauiapi.bean.SpecialAllObject;
import com.tran.com.android.gc.update.datauiapi.bean.SpecialListObject;
import com.tran.com.android.gc.update.datauiapi.bean.cacheitem;
import com.tran.com.android.gc.update.datauiapi.bean.detailsObject;
import com.tran.com.android.gc.update.datauiapi.implement.Command;
import com.tran.com.android.gc.update.datauiapi.implement.DataResponse;
import com.tran.com.android.gc.update.datauiapi.interf.IMarketManager;


public class MarketManager extends baseManager implements IMarketManager {

    /**
     * @uml.property name="tAG"
     */
    private final String TAG = "VideoManager";


    private void updatePreference(Context context, int type) {
        SharedPreferences sp1 = context.getSharedPreferences(
                Globals.SHARED_DATA_UPDATE, context.MODE_APPEND);
        Editor ed = sp1.edit();
        if (type == 0)
            ed.putString(Globals.SHARED_DATA_CACHE_KEY_UPDATETIME, TimeUtils.getStringDateShort());
        else
            ed.putString(Globals.SHARED_DATA_CACHE_KEY_UPDATETIME, "0");
        ed.commit();
    }

    /**
     * (非 Javadoc)
     * Title: getMarketItems
     * Description:
     *
     * @param response
     * @param context
     */
    @Override
    public void getMarketItems(final DataResponse<MarketListObject> response, final Context context, final int type, final String app_type, final int catid, final int pageNum, final int rowCount, final boolean isCacheData) {
        // TODO Auto-generated method stub
        mHandler.post(new Command<MarketListObject>(response, this, isCacheData) {
            @Override
            public void doRun() throws Exception {
                String result = new String();
                CacheDataAdapter ldb = new CacheDataAdapter(context);
                ldb.open();
                if (isCacheData) {
                    result = ldb.queryCacheByType(type, app_type, catid, 0);
                    if (TextUtils.isEmpty(result))
                        updatePreference(context, 1);
                } else {
                    result = HttpRequestGetMarketData
                            .getMarketListObject(type, app_type, catid, pageNum, rowCount);

                    if (pageNum == 1) {
                        cacheitem item = new cacheitem();
                        item.setContext(result);
                        item.setApp_type(app_type);
                        item.setCat_id(catid);
                        item.setType(type);
                        item.setUpdate_time(String.valueOf(System.currentTimeMillis()));
                        ldb.insert(item);
                        updatePreference(context, 0);
                    }
                }
                ldb.close();
                // String result =
                // HttpRequstData.doRequest("http://m.weather.com.cn/data/101110101.html");
                setResponse(response, result, MarketListObject.class);
            }
        });
    }

    /**
     * (非 Javadoc)
     * Title: getDetailsItems
     * Description:
     *
     * @param response
     * @param context
     * @param
     */
    @Override
    public void getDetailsItems(final DataResponse<detailsObject> response,
                                Context context, final String packagename) {
        // TODO Auto-generated method stub
        mHandler.post(new Command<detailsObject>(response, this) {
            @Override
            public void doRun() throws Exception {
                String result = HttpRequestGetMarketData
                        .getDetailsObject(packagename);
                //result = result.replaceAll("<br />", "\n");
                String[][] object = {new String[]{"\\<br />", "\n"}};
                result = SystemUtils.replace(result, object);
                // String result =
                // HttpRequstData.doRequest("http://m.weather.com.cn/data/101110101.html");
                setResponse(response, result, detailsObject.class);
            }
        });

    }


    /**
     * (非 Javadoc)
     * Title: getCategoryListItems
     * Description:
     *
     * @param response
     * @param context
     * @param type
     */
    @Override
    public void getCategoryListItems(final DataResponse<CategoryListObject> response,
                                     final Context context, final String type, final String style, final boolean isCacheData) {
        // TODO Auto-generated method stub
        mHandler.post(new Command<CategoryListObject>(response, this, isCacheData) {
            @Override
            public void doRun() throws Exception {
                String result = new String();
                CacheDataAdapter ldb = new CacheDataAdapter(context);
                ldb.open();
                if (isCacheData) {
                } else {
                    result = HttpRequestGetMarketData
                            .getCategoryListObject(type, style);

                    cacheitem item = new cacheitem();
                    item.setContext(result);
                    item.setApp_type(type);
                    item.setUpdate_time(String.valueOf(System.currentTimeMillis()));
                    ldb.insert(item);
                    updatePreference(context, 0);
                }
                ldb.close();
                // String result =
                // HttpRequstData.doRequest("http://m.weather.com.cn/data/101110101.html");
                setResponse(response, result, CategoryListObject.class);
            }
        });

    }

    @Override
    public void getSpecialListItems(final DataResponse<SpecialListObject> response,
                                    final Context context, final String type, final int pageNum, final int rowCount, final boolean isCacheData) {
        // TODO Auto-generated method stub
        mHandler.post(new Command<SpecialListObject>(response, this, isCacheData) {
            @Override
            public void doRun() throws Exception {
                String result = new String();
                CacheDataAdapter ldb = new CacheDataAdapter(context);
                ldb.open();
                if (isCacheData) {
                } else {
                    result = HttpRequestGetMarketData
                            .getSpeciaListObject(type, pageNum, rowCount);

                    if (pageNum == 1) {
                        cacheitem item = new cacheitem();
                        item.setContext(result);
                        item.setUpdate_time(String.valueOf(System.currentTimeMillis()));
                        ldb.insert(item);
                        updatePreference(context, 0);
                    }
                }
                ldb.close();
                // String result =
                // HttpRequstData.doRequest("http://m.weather.com.cn/data/101110101.html");
                //String result = SystemUtils.getFromAssets(context, "special.json");
                setResponse(response, result, SpecialListObject.class);

            }
        });
    }

    @Override
    public void getSpecialAllItems(final DataResponse<SpecialAllObject> response,
                                   final Context context, final int specialId, final int pageNum, final int rowCount, final boolean isCacheData) {
        // TODO Auto-generated method stub
        mHandler.post(new Command<SpecialAllObject>(response, this, isCacheData) {
            @Override
            public void doRun() throws Exception {
                String result = new String();
                CacheDataAdapter ldb = new CacheDataAdapter(context);
                ldb.open();
                if (isCacheData) {
                } else {
                    result = HttpRequestGetMarketData
                            .getSpeciaAllObject(specialId, pageNum, rowCount);

                    cacheitem item = new cacheitem();
                    item.setContext(result);
                    item.setApp_type(Globals.TYPE_APP);
                    item.setSpe_id(specialId);
                    item.setUpdate_time(String.valueOf(System.currentTimeMillis()));
                    ldb.insert(item);
                    updatePreference(context, 0);
                }
                ldb.close();
                // String result =
                // HttpRequstData.doRequest("http://m.weather.com.cn/data/101110101.html");
                //String result = SystemUtils.getFromAssets(context, "special.json");
                setResponse(response, result, SpecialAllObject.class);

            }
        });
    }

    /**
     * (非 Javadoc)
     * Title: postActivity
     * Description:
     *
     * @see com.tran.com.android.gc.update.datauiapi.interf.IMarketManager#postActivity()
     */
    @Override
    public void postActivity() {
        // TODO Auto-generated method stub
        /*
		 * if(failedRequests!=null){ failedRequests.clear(); }
		 */
        if (failedIORequests != null) {
            failedIORequests.clear();
        }
    }

}
