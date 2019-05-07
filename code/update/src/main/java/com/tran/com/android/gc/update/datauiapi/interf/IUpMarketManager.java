package com.tran.com.android.gc.update.datauiapi.interf;

import android.content.Context;

import com.tran.com.android.gc.update.datauiapi.bean.UpgradeListObject;
import com.tran.com.android.gc.update.datauiapi.bean.upcountinfo;
import com.tran.com.android.gc.update.datauiapi.implement.DataResponse;


public interface IUpMarketManager {
    /**
     * @param @param response
     * @param @param context
     * @return void
     * @throws
     * @Title: getUpAppListItems
     * @Description: 更新应用市场列表数据
     */
    public void getUpAppListItems(final DataResponse<UpgradeListObject> response, final Context context);

    /**
     * @param @param response
     * @param @param context
     * @return void
     * @throws
     * @Title: getUpdateCount
     * @Description: 得到需要更新的应用数量
     */
    public void getUpdateCount(final DataResponse<upcountinfo> response, final Context context);

    /**
     * Put in here everything that has to be cleaned up after leaving an activity.
     */
    public void postActivity();


}
