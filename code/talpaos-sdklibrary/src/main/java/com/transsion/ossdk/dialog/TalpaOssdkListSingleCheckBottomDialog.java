package com.transsion.ossdk.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.transsion.ossdk.R;

import java.util.List;

/**
 * Created by liang.wu1 on 2017/8/17.
 */

public class TalpaOssdkListSingleCheckBottomDialog extends TalpaOssdkListBottomDialog implements AdapterView.OnItemClickListener {

    public TalpaOssdkListSingleCheckBottomDialog(Context context) {
        super(context);
    }

    @Override
    public void initListView(){
        super.initListView();
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    public void setCheckItem(int checkItem){
        if (checkItem > -1) {
            mListView.setItemChecked(checkItem, true);
            mListView.setSelection(checkItem);
        }
    }

    public static TalpaOssdkListSingleCheckBottomDialog createDialog(Context context, CharSequence[] items, CharSequence mTitle,
                                                                   int checkItem, OnDialogItemSelectListener selectListener){
        TalpaOssdkListSingleCheckBottomDialog dialog = new TalpaOssdkListSingleCheckBottomDialog(context);
        ArrayAdapter adapter = new CheckedItemAdapter(context, R.layout.talpaossdk_dialog_singlechoice_item, android.R.id.text1, items);
        dialog.updateTitleAndDivider(mTitle);
        dialog.mListView.setAdapter(adapter);
        dialog.updateTitleViewLayoutParams();
        dialog.setSelectListener(selectListener);
        dialog.setCheckItem(checkItem);
        return dialog;
    }

    public static TalpaOssdkListSingleCheckBottomDialog showDialog(Context context, CharSequence[] items, CharSequence mTitle,
                                                                   int checkItem, OnDialogItemSelectListener selectListener){
        TalpaOssdkListSingleCheckBottomDialog dialog = createDialog(context, items, mTitle, checkItem, selectListener);
        dialog.show();
        return dialog;
    }

    public static TalpaOssdkListSingleCheckBottomDialog createDialog(Context context, List<String> items, CharSequence mTitle,
                                                                   String checkItemStr, OnDialogItemSelectListener selectListener){
        TalpaOssdkListSingleCheckBottomDialog dialog = new TalpaOssdkListSingleCheckBottomDialog(context);
        ArrayAdapter adapter = new CheckedItemAdapter2(context, R.layout.talpaossdk_dialog_singlechoice_item,
                android.R.id.text1, items);
        dialog.updateTitleAndDivider(mTitle);
        dialog.mListView.setAdapter(adapter);
        dialog.updateTitleViewLayoutParams();
        dialog.setSelectListener(selectListener);
        int checkItem = -1;
        if(!TextUtils.isEmpty(checkItemStr) && items != null && items.size() > 0){
            checkItem = items.indexOf(checkItemStr);
        }
        dialog.setCheckItem(checkItem);
        return dialog;
    }

    public static TalpaOssdkListSingleCheckBottomDialog showDialog(Context context, List<String> items, CharSequence mTitle,
                                                                   String checkItemStr, OnDialogItemSelectListener selectListener){
        TalpaOssdkListSingleCheckBottomDialog dialog = createDialog(context, items, mTitle, checkItemStr, selectListener);
        dialog.show();
        return dialog;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mSelectListener != null){
            mSelectListener.onDialogItemSelect(position,parent.getAdapter().getItem(position));
        }
        dismissDialog();
    }

    private static class CheckedItemAdapter extends ArrayAdapter<CharSequence> {
        public CheckedItemAdapter(Context context, int resource, int textViewResourceId,
                                  CharSequence[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    private static class CheckedItemAdapter2 extends ArrayAdapter<String> {

        public CheckedItemAdapter2(Context context, int resource, int textViewResourceId,
                                  List<String> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    public void dismissDialog(){
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                TalpaOssdkListSingleCheckBottomDialog.this.dismiss();
            }
        }, 360);
    }
}
