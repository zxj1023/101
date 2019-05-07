package com.transsion.ossdk.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.transsion.ossdk.R;
import com.transsion.ossdk.dialog.data.TalpaOssdkDialogIconItemData;
import com.transsion.ossdk.dialog.view.TalpaOssdkDialogListIconItem;

import java.util.List;

/**
 * Created by liang.wu1 on 2017/8/17.
 */

public class TalpaOssdkListSingleCheckWithIconBottomDialog extends TalpaOssdkListSingleCheckBottomDialog{

    public TalpaOssdkListSingleCheckWithIconBottomDialog(Context context) {
        super(context);
    }

    public static TalpaOssdkListSingleCheckWithIconBottomDialog createIconListDialog(Context context, List<TalpaOssdkDialogIconItemData> items,
                                                                                     int checkItem,
                                                                             CharSequence mTitle, OnDialogItemSelectListener selectListener){
        TalpaOssdkListSingleCheckWithIconBottomDialog dialog = new TalpaOssdkListSingleCheckWithIconBottomDialog(context);
        ArrayAdapter adapter = new CheckedItemAdapter(context, items);
        dialog.updateTitleAndDivider(mTitle);
        dialog.mListView.setAdapter(adapter);
        dialog.updateTitleViewLayoutParams();
        dialog.setSelectListener(selectListener);
        dialog.setCheckItem(checkItem);
        return dialog;
    }

    public static TalpaOssdkListSingleCheckWithIconBottomDialog showIconListDialog(Context context, List<TalpaOssdkDialogIconItemData> items,
                                                                                   int checkItem,
                                                                           CharSequence mTitle, OnDialogItemSelectListener selectListener){
        TalpaOssdkListSingleCheckWithIconBottomDialog dialog = createIconListDialog(context, items, checkItem, mTitle, selectListener);
        dialog.show();
        return dialog;
    }

    public static TalpaOssdkListSingleCheckWithIconBottomDialog createIconListDialog(Context context, BaseAdapter adapter,
                                                                                     int checkItem,
                                                                                     CharSequence mTitle, OnDialogItemSelectListener selectListener){
        TalpaOssdkListSingleCheckWithIconBottomDialog dialog = new TalpaOssdkListSingleCheckWithIconBottomDialog(context);
        dialog.updateTitleAndDivider(mTitle);
        dialog.mListView.setAdapter(adapter);
        dialog.updateTitleViewLayoutParams();
        dialog.setSelectListener(selectListener);
        dialog.setCheckItem(checkItem);
        return dialog;
    }

    public static TalpaOssdkListSingleCheckWithIconBottomDialog showIconListDialog(Context context, BaseAdapter adapter,
                                                                                   int checkItem,
                                                                                   CharSequence mTitle, OnDialogItemSelectListener selectListener){
        TalpaOssdkListSingleCheckWithIconBottomDialog dialog = createIconListDialog(context, adapter, checkItem, mTitle, selectListener);
        dialog.show();
        return dialog;
    }

    private static class CheckedItemAdapter extends ArrayAdapter<TalpaOssdkDialogIconItemData> {
        private LayoutInflater mInflater;

        public CheckedItemAdapter(Context context, List datas) {
            super(context, -1, datas);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TalpaOssdkDialogIconItemData data = getItem(position);
            if(data == null) return null;
            View view;
            if (convertView == null) {
                view = newView(parent);
            } else {
                view = convertView;
            }
            if(view instanceof TalpaOssdkDialogListIconItem){
                TalpaOssdkDialogListIconItem listIconItem = (TalpaOssdkDialogListIconItem) view;
                listIconItem.bindView(data);
            }
            return view;
        }

        private View newView(ViewGroup parent){
            return mInflater.inflate( R.layout.talpaossdk_dialog_icon_singlechoice_item, parent, false);
        }
    }
}
