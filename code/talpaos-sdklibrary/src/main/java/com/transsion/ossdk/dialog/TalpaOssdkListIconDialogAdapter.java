package com.transsion.ossdk.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.transsion.ossdk.R;
import com.transsion.ossdk.dialog.view.TalpaOssdkDialogListIconItem;

/**
 * Created by liang.wu1 on 2017/9/1.
 */

public abstract class TalpaOssdkListIconDialogAdapter extends BaseAdapter{
    private LayoutInflater mInflater;

    public TalpaOssdkListIconDialogAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = mInflater.inflate( R.layout.talpaossdk_dialog_icon_singlechoice_item, parent, false);
        } else {
            view = convertView;
        }
        if(view instanceof TalpaOssdkDialogListIconItem){
            TalpaOssdkDialogListIconItem listIconItem = (TalpaOssdkDialogListIconItem) view;
            bindView(position, listIconItem);
        }
        return view;
    }

    public abstract void bindView(int position, TalpaOssdkDialogListIconItem listIconItem);
}
