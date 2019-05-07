package tran.com.android.tapla.gamecenter.datauiapi.interf;

import android.content.Context;

import tran.com.android.tapla.gamecenter.datauiapi.bean.AppListObject;
import tran.com.android.tapla.gamecenter.datauiapi.bean.SearchRecommend;
import tran.com.android.tapla.gamecenter.datauiapi.bean.SearchTimelyObject;
import tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse;



public interface ISearchAppManager {
	/** 
	* @Title: getSearchRecItems
	* @Description: 搜索推荐列表数据
	* @param @param response
	* @param @param context
	* @return void
	* @throws 
	*/ 
	public void getSearchRecItems(final DataResponse<SearchRecommend> response, final Context context,  final int type, final String app_type, final String catid,final boolean isCacheData);
	
	/** 
	* @Title: getSearchListItems
	* @Description: 搜索应用接口
	* @param @param response
	* @param @param context
	* @return void
	* @throws 
	*/ 
	public void getSearchListItemsFrom9Apps(final DataResponse<AppListObject> response, final Context context, final String  query, final int pageNum, final int rowCount);

	public void getSearchListItems(final DataResponse<AppListObject> response, final Context context, final String  query, final int pageNum, final int rowCount);

	/** 
	* @Title: getSearchTimelyItems
	* @Description: 及时搜索接口
	* @param @param response
	* @param @param context
	* @param @param query
	* @return void
	* @throws 
	*/ 
	public void getSearchTimelyItems(final DataResponse<SearchTimelyObject> response,final Context context,final String  query);
	
	/**
	 * Put in here everything that has to be cleaned up after leaving an activity.
	 */
	public void postActivity();


}
