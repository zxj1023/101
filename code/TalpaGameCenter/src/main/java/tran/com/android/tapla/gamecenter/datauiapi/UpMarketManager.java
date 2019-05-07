package tran.com.android.tapla.gamecenter.datauiapi;


import android.content.Context;
import android.util.Log;

import tran.com.android.tapla.gamecenter.datauiapi.bean.UpgradeListObject;
import tran.com.android.tapla.gamecenter.datauiapi.bean.upcountinfo;
import tran.com.android.tapla.gamecenter.datauiapi.interf.IMarketManager;
import tran.com.android.tapla.gamecenter.datauiapi.implement.Command;
import tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse;
import tran.com.android.tapla.gamecenter.datauiapi.interf.IUpMarketManager;
import tran.com.android.tapla.gamecenter.market.http.data.HttpRequestGetMarketData;


public class UpMarketManager extends baseManager implements IUpMarketManager{

	/**
	 * @uml.property name="tAG"
	 */
	private final String TAG = "VideoManager";




	@Override
	public void getUpAppListItems(final DataResponse<UpgradeListObject> response,
			final Context context) {
		mHandler.post(new Command<UpgradeListObject>(response, this) {
			@Override
			public void doRun() throws Exception {
				String result = HttpRequestGetMarketData
						.getUpAppListObject(context);
				
				Log.v(TAG, "aurora.jiangmx return data:" + result);
				// String result =
				// HttpRequstData.doRequest("http://m.weather.com.cn/data/101110101.html");
				setResponse(response, result, tran.com.android.tapla.gamecenter.datauiapi.bean.UpgradeListObject.class);
			}
		});
	}
	/** (非 Javadoc) 
	* Title: getUpdateCount
	* Description:
	* @param response
	* @param context
	* @see IUpMarketManager#getUpdateCount(DataResponse, android.content.Context)
	*/ 
	@Override
	public void getUpdateCount(final DataResponse<upcountinfo> response,
			final Context context) {
		// TODO Auto-generated method stub
		mHandler.post(new Command<upcountinfo>(response, this) {
			@Override
			public void doRun() throws Exception {
				String result = HttpRequestGetMarketData
						.getUpdateCountObject(context);
				// String result =
				// HttpRequstData.doRequest("http://m.weather.com.cn/data/101110101.html");
				setResponse(response, result, upcountinfo.class);
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



}
