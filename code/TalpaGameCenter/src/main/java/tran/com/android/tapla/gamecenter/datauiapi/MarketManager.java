package tran.com.android.tapla.gamecenter.datauiapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import tran.com.android.tapla.gamecenter.datauiapi.bean.AppInfo;
import tran.com.android.tapla.gamecenter.datauiapi.bean.AppListObject;
import tran.com.android.tapla.gamecenter.datauiapi.bean.CategoryList;
import tran.com.android.tapla.gamecenter.datauiapi.bean.MainListObject;
import tran.com.android.tapla.gamecenter.datauiapi.bean.CategoryListObject;
import tran.com.android.tapla.gamecenter.datauiapi.bean.MarketListObject;
import tran.com.android.tapla.gamecenter.datauiapi.bean.SpecialAllObject;
import tran.com.android.tapla.gamecenter.datauiapi.bean.SpecialListObject;
import tran.com.android.tapla.gamecenter.datauiapi.bean.cacheitem;
import tran.com.android.tapla.gamecenter.datauiapi.bean.detailsObject;
import tran.com.android.tapla.gamecenter.datauiapi.implement.Command;
import tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse;
import tran.com.android.tapla.gamecenter.datauiapi.interf.IMarketManager;
import tran.com.android.tapla.gamecenter.market.activity.module.AppListActivity;
import tran.com.android.tapla.gamecenter.market.db.CacheDataAdapter;
import tran.com.android.tapla.gamecenter.market.http.data.HttpRequestGetMarketData;
import tran.com.android.tapla.gamecenter.market.util.Globals;
import tran.com.android.tapla.gamecenter.market.util.Log;
import tran.com.android.tapla.gamecenter.market.util.SystemUtils;
import tran.com.android.tapla.gamecenter.market.util.TimeUtils;


public class MarketManager extends baseManager implements IMarketManager{


	private final String TAG = "VideoManager";



	private void updatePreference(Context context,int type)
	{
		SharedPreferences sp1 = context.getSharedPreferences(
				Globals.SHARED_DATA_UPDATE, context.MODE_APPEND);
		Editor ed = sp1.edit();
		if(type == 0)
			ed.putString(Globals.SHARED_DATA_CACHE_KEY_UPDATETIME, TimeUtils.getStringDateShort());
		else
			ed.putString(Globals.SHARED_DATA_CACHE_KEY_UPDATETIME, "0");
		ed.commit();
	}

	@Override
	public void getMarketItems(final DataResponse<MarketListObject> response, final Context context, final int type, final String app_type, final String catid, final int pageNum, final int rowCount, final boolean isCacheData) {
		// TODO Auto-generated method stub
		mHandler.post(new Command<MarketListObject>(response, this,isCacheData) {
			@Override
			public void doRun() throws Exception {
				String result = new String();
				CacheDataAdapter ldb = new CacheDataAdapter(context);
				ldb.open();
				if(isCacheData)
				{
					result = ldb.queryCacheByType(type,app_type,catid,"");
//					if(TextUtils.isEmpty(result))
//						updatePreference(context,1);
				}
				else
				{
					result = HttpRequestGetMarketData
						.getMarketListObject(type,app_type,catid,pageNum,rowCount);

					if (pageNum == 1 && result != null) {
						//删除之前的老数据
						ldb.deleteDataByType(0);

						//保存最近一次的数据
						tran.com.android.tapla.gamecenter.datauiapi.bean.cacheitem item = new tran.com.android.tapla.gamecenter.datauiapi.bean.cacheitem();
						item.setContext(result);
						item.setApp_type(app_type);
						item.setCat_id(catid);
						item.setType(type);
						item.setUpdate_time(String.valueOf(System.currentTimeMillis()));
						ldb.insert(item);
//						updatePreference(context,0);
					}
				}
				ldb.close();
				setResponse(response, result, tran.com.android.tapla.gamecenter.datauiapi.bean.MarketListObject.class);
			}
		});
	}

	public void getAppListObject(final tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse<AppListObject> response, final Context context, final int type, final String app_type, final String catid, final int pageNum, final int rowCount, final boolean isCacheData) {
		// TODO Auto-generated method stub
		mHandler.post(new tran.com.android.tapla.gamecenter.datauiapi.implement.Command<AppListObject>(response, this,isCacheData) {
			@Override
			public void doRun() throws Exception {
                String result = new String();
                CacheDataAdapter ldb = new CacheDataAdapter(context);
                ldb.open();

                if(pageNum == 1 && SystemUtils.getIfCache(context,type,app_type,catid,"")){
                    result = ldb.queryCacheByType(type,app_type,catid,"");
                } else{
                    result = HttpRequestGetMarketData
                            .getMarketListObject(type,app_type,catid,pageNum,rowCount);

                    if (pageNum == 1 && result != null) {
                        tran.com.android.tapla.gamecenter.datauiapi.bean.cacheitem item = new tran.com.android.tapla.gamecenter.datauiapi.bean.cacheitem();
                        item.setContext(result);
                        item.setApp_type(app_type);
                        item.setCat_id(catid);
                        item.setType(type);
                        item.setUpdate_time(String.valueOf(System.currentTimeMillis()));
                        ldb.insert(item);
                    }
                }
                ldb.close();
                setResponse(response, result, AppListObject.class);
			}
		});
	}



	/** (非 Javadoc)  
	* Title: getMainListItems
	* Description:
	* @param response
	* @param context
	*/
	@Override
	public void getMainListItems(final DataResponse<MainListObject> response, final Context context, final int type, final String app_type, final String catid, final int pageNum, final int rowCount, final boolean isCacheData) {
		// TODO Auto-generated method stub
		mHandler.post(new Command<MainListObject>(response, this,isCacheData) {
			@Override
			public void doRun() throws Exception {
				String result = new String();
				CacheDataAdapter ldb = new CacheDataAdapter(context);
				ldb.open();
				if(isCacheData)
				{
					result = ldb.queryCacheByType(type,app_type,catid,"");
					if(TextUtils.isEmpty(result))
						updatePreference(context,1);
				}
				else
				{
					result = HttpRequestGetMarketData
						.getMainListObject(type,app_type,catid,pageNum,rowCount);
					
					if (pageNum == 1) {
						tran.com.android.tapla.gamecenter.datauiapi.bean.cacheitem item = new tran.com.android.tapla.gamecenter.datauiapi.bean.cacheitem();
						item.setContext(result);
						item.setApp_type(app_type);
						item.setCat_id(catid);
						item.setType(type);
						item.setUpdate_time(String.valueOf(System.currentTimeMillis()));
						ldb.insert(item);
						updatePreference(context,0);
					}
				}
				ldb.close();
				// String result =
				// HttpRequstData.doRequest("http://m.weather.com.cn/data/101110101.html");
				setResponse(response, result, MainListObject.class);
			}
		});
	}
	
	/** (非 Javadoc) 
	* Title: getDetailsItems
	* Description:
	* @param response
	* @param context
	*/
	@Override
	public void getDetailsItems(final DataResponse<detailsObject> response,
									 Context context, final String packagename) {
		// TODO Auto-generated method stub
		mHandler.post(new tran.com.android.tapla.gamecenter.datauiapi.implement.Command<detailsObject>(response, this) {
			@Override
			public void doRun() throws Exception {
				String result = HttpRequestGetMarketData
						.getDetailsObject(packagename);
				//result = result.replaceAll("<br />", "\n");
				String[][] object = { new String[] { "\\<br />", "\n" } };
				result = SystemUtils.replace(result, object);
				// String result =
				// HttpRequstData.doRequest("http://m.weather.com.cn/data/101110101.html");
				setResponse(response, result, tran.com.android.tapla.gamecenter.datauiapi.bean.detailsObject.class);
			}
		});

	}

	public void getAppInfoFrom9Apps(final tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse<AppInfo> response,
								final Context context, final String packagename, final String publishIds) {
		// TODO Auto-generated method stub
		mHandler.post(new tran.com.android.tapla.gamecenter.datauiapi.implement.Command<AppInfo>(response, this) {
			@Override
			public void doRun() throws Exception {
				String result = HttpRequestGetMarketData
						.getDetailsObjectFrom9Apps(context,packagename, publishIds);

				setResponse(response, result, tran.com.android.tapla.gamecenter.datauiapi.bean.AppInfo.class);
			}
		});

	}

	
	/** (非 Javadoc) 
	* Title: getCategoryListItems
	* Description:
	* @param response
	* @param context
	* @param type
	*/
	@Override
	public void getCategoryListItems(final DataResponse<CategoryListObject> response,
			final Context context, final String type,final String style,final boolean isCacheData) {
		// TODO Auto-generated method stub
		mHandler.post(new  Command<CategoryListObject>(response, this,isCacheData) {
			@Override
			public void doRun() throws Exception {
				String result = new String();
				CacheDataAdapter ldb = new CacheDataAdapter(context);
				ldb.open();
				if(isCacheData)
				{
					result = ldb.queryCacheByType(AppListActivity.TYPE_CATEGORY_MAIN,type,"","");
				}
				else
				{
					result = HttpRequestGetMarketData
							.getCategoryListObject(type,style);
					
					cacheitem item = new cacheitem();
					item.setContext(result);
					item.setApp_type(type);
					item.setType(AppListActivity.TYPE_CATEGORY_MAIN);
					item.setUpdate_time(String.valueOf(System.currentTimeMillis()));
					ldb.insert(item);
					updatePreference(context,0);
				}
				ldb.close();
				// String result =
				// HttpRequstData.doRequest("http://m.weather.com.cn/data/101110101.html");
				setResponse(response, result, CategoryListObject.class);
			}
		});
		
	}
	
	@Override
	public void getSpecialListItems(final tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse<SpecialListObject> response,
			final Context context, final String type,final int pageNum,final int rowCount,final boolean isCacheData) {
		// TODO Auto-generated method stub
		mHandler.post(new tran.com.android.tapla.gamecenter.datauiapi.implement.Command<SpecialListObject>(response, this,isCacheData) {
			@Override
			public void doRun() throws Exception {
				String result = new String();
				CacheDataAdapter ldb = new CacheDataAdapter(context);
				ldb.open();
				if(isCacheData)
				{
					result = ldb.queryCacheByType(AppListActivity.TYPE_SPECIAL_MAIN,Globals.TYPE_APP,"","");
				}
				else
				{
					result = HttpRequestGetMarketData
							.getSpeciaListObject(type,pageNum,rowCount);
					
					if (pageNum == 1) {
						tran.com.android.tapla.gamecenter.datauiapi.bean.cacheitem item = new tran.com.android.tapla.gamecenter.datauiapi.bean.cacheitem();
						item.setContext(result);
						item.setType(AppListActivity.TYPE_SPECIAL_MAIN);
						item.setUpdate_time(String.valueOf(System.currentTimeMillis()));
						ldb.insert(item);
						updatePreference(context,0);
					}
				}
				ldb.close();
				// String result =
				// HttpRequstData.doRequest("http://m.weather.com.cn/data/101110101.html");
				//String result = SystemUtils.getFromAssets(context, "special.json");
				setResponse(response, result, tran.com.android.tapla.gamecenter.datauiapi.bean.SpecialListObject.class);
				
			}
		});
	}

	@Override
	public void getSpecialAllItems(final tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse<AppListObject> response,
			final Context context, final String specialId,final int pageNum,final int rowCount,final boolean isCacheData) {
		// TODO Auto-generated method stub
		mHandler.post(new tran.com.android.tapla.gamecenter.datauiapi.implement.Command<AppListObject>(response, this,isCacheData) {
			@Override
			public void doRun() throws Exception {
				String result = new String();
				CacheDataAdapter ldb = new CacheDataAdapter(context);
				ldb.open();
				if(pageNum == 1 && SystemUtils.getIfCache(context,AppListActivity.TYPE_SPECIAL,Globals.TYPE_APP,"",specialId)){
					result = ldb.queryCacheByType(AppListActivity.TYPE_SPECIAL,Globals.TYPE_APP,"",specialId);
				} else{
					result = HttpRequestGetMarketData
							.getSpeciaAllObject(specialId,pageNum,rowCount);

					if (pageNum == 1 && result != null) {
						tran.com.android.tapla.gamecenter.datauiapi.bean.cacheitem item = new tran.com.android.tapla.gamecenter.datauiapi.bean.cacheitem();
						item.setContext(result);
						item.setApp_type(Globals.TYPE_APP);
						item.setSpe_id(specialId);
						item.setType(AppListActivity.TYPE_SPECIAL);
						item.setUpdate_time(String.valueOf(System.currentTimeMillis()));
						ldb.insert(item);
					}
				}
				ldb.close();
				setResponse(response, result, AppListObject.class);
			}
		});
	}

	/** (非 Javadoc) 
	* Title: postActivity
	* Description:
	* @see IMarketManager#postActivity()
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


	@Override
	public void getCategoryList(final DataResponse<CategoryList> response, final Context context, final int categoryType, final int page, final int size) {
		mHandler.post(new tran.com.android.tapla.gamecenter.datauiapi.implement.Command<CategoryList>(response, this) {
			@Override
			public void doRun() throws Exception {
				String result = HttpRequestGetMarketData.getCategoryListFrom9Apps(context,categoryType,page, size);

				setResponse(response, result, tran.com.android.tapla.gamecenter.datauiapi.bean.CategoryList.class);
			}
		});
	}
}
