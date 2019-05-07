package com.transsion.ossdk.operation;

import android.view.View;

import com.transsion.ossdk.TalpaOssdkViewBgHelper;

/**
 * Created by liang.wu1 on 2017/8/23.
 */

public class TalpaOssdkOperationBgHelper {
    public static void setBg(View view){
        view.setBackground(TalpaOssdkViewBgHelper.createForOperationItem(view));
    }
}
