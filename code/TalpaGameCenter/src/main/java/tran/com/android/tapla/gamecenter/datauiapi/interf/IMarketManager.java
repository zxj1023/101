package tran.com.android.tapla.gamecenter.datauiapi.interf;

import android.content.Context;

import tran.com.android.tapla.gamecenter.datauiapi.bean.AppInfo;
import tran.com.android.tapla.gamecenter.datauiapi.bean.AppListObject;
import tran.com.android.tapla.gamecenter.datauiapi.bean.CategoryList;
import tran.com.android.tapla.gamecenter.datauiapi.bean.CategoryListObject;
import tran.com.android.tapla.gamecenter.datauiapi.bean.MainListObject;
import tran.com.android.tapla.gamecenter.datauiapi.bean.MarketListObject;
import tran.com.android.tapla.gamecenter.datauiapi.bean.SpecialListObject;
import tran.com.android.tapla.gamecenter.datauiapi.bean.detailsObject;
import tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse;


public interface IMarketManager {

	
	/** 
	* @Title: getMarketItems
	* @Description: 得到应用市场列表数据(包括banner头)
	* @param @param response
	* @param @param context
	* @return void
	* @throws 
	*/ 
	public void getMarketItems(final tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse<MarketListObject> response, final Context context, final int type, final String app_type, final String catid, final int pageNum, final int rowCount, final boolean isCacheData);
	
	/** 
	* @Title: getMainListItems
	* @Description: 得到应用市场列表数据(包括banner头)
	* @param @param response
	* @param @param context
	* @return void
	* @throws 
	*/ 
	public void getMainListItems(final tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse<MainListObject> response, final Context context, final int type, final String app_type, final String catid, final int pageNum, final int rowCount, final boolean isCacheData);
	
	/** 
	* @Title: getDetailsItems
	* @Description: 得到应用详细数据
	* @param @param response
	* @param @param context
	* @return void
	* @throws 
	*/ 
	public void getDetailsItems(final tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse<detailsObject> response, final Context context, final String packagename);
	public void getAppInfoFrom9Apps(final tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse<AppInfo> response, final Context context, final String packagename, final String publishIds);

	/** 
	* @Title: getAppListItems
	* @Description: 应用市场列表数据
	* @param @param response
	* @param @param context
	* @return void
	* @throws 
	*/ 
	public void getCategoryListItems(final tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse<CategoryListObject> response, final Context context, final String type, final String style, final boolean isCacheData);
	/** 
	* @Title: getSpecialListItems
	* @Description: 专题列表数据
	* @param @param response
	* @param @param context
	* @return void
	* @throws 
	*/ 
	public void getSpecialListItems(final tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse<SpecialListObject> response, final Context context, final String type, final int pageNum, final int rowCount, final boolean isCacheData);
	/** 
	* @Title: getSpecialListItems
	* @Description: 单个专题全部数据
	* @param @param response
	* @param @param context
	* @return void
	* @throws 
	*/ 
	public void getSpecialAllItems(final tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse<AppListObject> response, final Context context, final String specialId, final int pageNum, final int rowCount, final boolean isCacheData);
	
	/**
	 * Put in here everything that has to be cleaned up after leaving an activity.
	 */
	public void postActivity();


	//9Apps获取分类列表
	public void getCategoryList(final DataResponse<CategoryList> response, final Context context, final int categoryType, final int page, final int size);

}
