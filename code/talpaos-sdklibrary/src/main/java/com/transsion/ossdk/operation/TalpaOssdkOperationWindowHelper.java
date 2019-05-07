package com.transsion.ossdk.operation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.transsion.ossdk.TalpaOssdkViewBgHelper;
import com.transsion.ossdk.R;
import com.transsion.ossdk.operation.data.TalpaOssdkOperationItemData;
import com.transsion.ossdk.operation.view.TalpaOssdkOperationItemView;

import java.util.List;

/**
 * Created by liang.wu1 on 2017/6/30.
 */

public class TalpaOssdkOperationWindowHelper {
    private PopupWindow mPopupWindow;

    public PopupWindow createPopupWindow(Context context, List<TalpaOssdkOperationItemData> datas, OnItemClickListener listener){
        View view = LayoutInflater.from(context).inflate(R.layout.talpaossdk_operation_window, null);
        ViewGroup rootView = (ViewGroup) view.findViewById(R.id.talpaossdk_container);
        int size = datas.size();
        boolean isNeedOmit = size > 4;
        for(int i = 0; i < size; i++){
            if(isNeedOmit && i == 3){
                break;
            }

            rootView.addView(createChildView(context, datas.get(i), listener, rootView));
        }
        if(isNeedOmit){
            TalpaOssdkOperationItemData data = new TalpaOssdkOperationItemData(3, context.getString(R.string.more), R.drawable.ossdk_ic_more);
            rootView.addView(createChildView(context, data, null, rootView));
        }
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(R.style.TalpaOssdk_Operation_Animation);
        return mPopupWindow;
    }

    private View createChildView(Context context, final TalpaOssdkOperationItemData data, final OnItemClickListener listener, ViewGroup parentView){
        TalpaOssdkOperationItemView itemView = (TalpaOssdkOperationItemView)LayoutInflater.from(context).inflate(R.layout.talpaossdk_operation_item, parentView, false);
        itemView.bindData(data);
        itemView.setEnabled(data.isIsEnable());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(data.isIsMoreItem()){
                    //do some
                } else{
                    if(listener != null){
                        listener.onItemClick(data.getIndex());
                    }
                }
                if(mPopupWindow != null && mPopupWindow.isShowing()){
                    mPopupWindow.dismiss();
                }
            }
        });
        itemView.setBackground(TalpaOssdkViewBgHelper.createForOperationItem(itemView));
        return itemView;
    }

    public interface OnItemClickListener{
        void onItemClick(int index);
    }
}
