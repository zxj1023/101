package tran.com.android.tapla.gamecenter.datauiapi.interf;

import android.content.Context;

import tran.com.android.tapla.gamecenter.datauiapi.bean.UpgradeListObject;
import tran.com.android.tapla.gamecenter.datauiapi.bean.upcountinfo;
import tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse;


public interface IUpMarketManager {
	/** 
	* @Title: getUpAppListItems
	* @Description: 更新应用市场列表数据
	* @param @param response
	* @param @param context
	* @return void
	* @throws 
	*/ 
	public void getUpAppListItems(final DataResponse<UpgradeListObject> response, final Context context);
	
	/** 
	* @Title: getUpdateCount
	* @Description: 得到需要更新的应用数量
	* @param @param response
	* @param @param context
	* @return void
	* @throws 
	*/ 
	public void getUpdateCount(final DataResponse<upcountinfo> response, final Context context);
	/**
	 * Put in here everything that has to be cleaned up after leaving an activity.
	 */
	public void postActivity();
	

}
