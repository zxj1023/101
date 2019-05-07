package tran.com.android.tapla.gamecenter.datauiapi;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import tran.com.android.tapla.gamecenter.datauiapi.bean.AppListObject;
import tran.com.android.tapla.gamecenter.datauiapi.bean.SearchRecommend;
import tran.com.android.tapla.gamecenter.datauiapi.bean.SearchTimelyObject;
import tran.com.android.tapla.gamecenter.datauiapi.bean.cacheitem;
import tran.com.android.tapla.gamecenter.datauiapi.implement.Command;
import tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse;
import tran.com.android.tapla.gamecenter.datauiapi.interf.ISearchAppManager;
import tran.com.android.tapla.gamecenter.market.db.CacheDataAdapter;
import tran.com.android.tapla.gamecenter.market.http.data.HttpRequestSearchData;
import tran.com.android.tapla.gamecenter.market.util.Globals;
import tran.com.android.tapla.gamecenter.market.util.TimeUtils;


public class SearchManager extends baseManager implements ISearchAppManager {

	/**
	 * @uml.property name="tAG"
	 */
	private final String TAG = "SearchManager";





	/** (非 Javadoc) 
	* Title: getSearchRecItems
	* Description:
	* @param response
	* @param context
	*/
	@Override
	public void getSearchRecItems(final DataResponse<SearchRecommend> response,
			final Context context, final int type, final String app_type, final String catid, final boolean isCacheData) {
		// TODO Auto-generated method stub
		mHandler.post(new Command<SearchRecommend>(response,this,isCacheData) {
			@Override
			public void doRun() throws Exception {
				String result;
				CacheDataAdapter ldb = new CacheDataAdapter(context);
				ldb.open();
				if(isCacheData)
				{
					result = ldb.queryCacheByType(type,app_type,catid,"");
					if(TextUtils.isEmpty(result))
						updatePreferenceRecommned(context,1);

				} else {
					result = HttpRequestSearchData.getSearcjRecObject();
					if (result != null && result.length() > 0) {
						//先删除之前的缓存
						ldb.deleteDataByType(type);

						//保存最新的数据
						cacheitem item = new cacheitem();
						item.setContext(result);
						item.setApp_type(app_type);
						item.setCat_id(catid);
						item.setType(type);
						item.setUpdate_time(String.valueOf(System.currentTimeMillis()));
						ldb.insert(item);
						updatePreferenceRecommned(context,0);
					}
				}
				ldb.close();
				if(result == null){
					result = new String();
                }
				setResponse(response, result, SearchRecommend.class);
			}
		});
	}


    private void updatePreferenceRecommned(Context context, int type) {
        SharedPreferences sp1 = context.getSharedPreferences(
                Globals.SHARED_DATA_UPDATE, context.MODE_APPEND);
        SharedPreferences.Editor ed = sp1.edit();
        if (type == 0)
            ed.putString(Globals.SEARCH_RECOMMEND_DATA_CACHE_UPDATETIME, TimeUtils.getStringDateShort());
        else
            ed.putString(Globals.SEARCH_RECOMMEND_DATA_CACHE_UPDATETIME, "0");
        ed.commit();
    }


	/** (非 Javadoc) 
	* Title: getSearchListItems
	* Description:
	* @param response
	* @param context
	* @param query
	* @param pageNum
	* @param rowCount
	* @see ISearchAppManager#getSearchListItems(tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse, android.content.Context, java.lang.String, int, int)
	*/ 
	@Override
	public void getSearchListItemsFrom9Apps(final tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse<AppListObject> response,
			final Context context, final String query, final int pageNum, final int rowCount) {
		// TODO Auto-generated method stub
		mHandler.post(new Command<AppListObject>(response, this) {
			@Override
			public void doRun() throws Exception {
				String result = HttpRequestSearchData
						.getSearchAppListObjectFrom9Apps(context, query, pageNum, rowCount, 0);

				setResponse(response, result, AppListObject.class);
			}
		});
	}

	@Override
	public void getSearchListItems(final DataResponse<AppListObject> response,
			final Context context, final String query, final int pageNum, final int rowCount) {
		// TODO Auto-generated method stub
		mHandler.post(new Command<tran.com.android.tapla.gamecenter.datauiapi.bean.AppListObject>(response, this) {
			@Override
			public void doRun() throws Exception {
				String result = HttpRequestSearchData
						.getSearchAppListObject(query, pageNum, rowCount);

				setResponse(response, result, tran.com.android.tapla.gamecenter.datauiapi.bean.AppListObject.class);
			}
		});
	}


	/** (非 Javadoc)
	* Title: getSearchTimelyItems
	* Description:
	* @param response
	* @param context
	* @param query
	* @see ISearchAppManager#getSearchTimelyItems(tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse, android.content.Context, java.lang.String)
	*/ 
	@Override
	public void getSearchTimelyItems(final tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse<SearchTimelyObject> response,
			Context context, final String query) {
		// TODO Auto-generated method stub
		mHandler.post(new Command<SearchTimelyObject>(response, this) {
			@Override
			public void doRun() throws Exception {
				String result = HttpRequestSearchData
						.getSearchTimeLyObject(query);
		
				setResponse(response, result, SearchTimelyObject.class);
			}
		});
	}


	/** (非 Javadoc) 
	* Title: postActivity
	* Description:
	* @see ISearchAppManager#postActivity()
	*/ 
	@Override
	public void postActivity() {
		// TODO Auto-generated method stub
		
	}
}
