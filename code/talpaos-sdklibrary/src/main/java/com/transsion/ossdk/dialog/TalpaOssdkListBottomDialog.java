package com.transsion.ossdk.dialog;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.transsion.ossdk.R;

import java.util.List;

/**
 * Created by liang.wu1 on 2017/8/17.
 */

public class TalpaOssdkListBottomDialog extends TalpaOssdkBottomDialog implements AdapterView.OnItemClickListener {
    public ListView mListView;
    protected OnDialogItemSelectListener mSelectListener;

    public TalpaOssdkListBottomDialog(Context context) {
        super(context, R.layout.talpaossdk_list_bottom_dialog);
        initListView();
    }

    public void initListView(){
        mListView = (ListView)findViewById(R.id.talpaossdk_dialog_listview);
        mListView.setOnItemClickListener(this);
    }

    public void setSelectListener(OnDialogItemSelectListener selectListener) {
        this.mSelectListener = selectListener;
    }

    public static TalpaOssdkListBottomDialog createDialog(Context context, List<String> items, CharSequence mTitle,
                                                        OnDialogItemSelectListener selectListener){
        TalpaOssdkListBottomDialog dialog = new TalpaOssdkListBottomDialog(context);
        ArrayAdapter adapter = new ArrayAdapter(context, R.layout.talpaossdk_dialog_text_item, android.R.id.text1, items);
        dialog.updateTitleAndDivider(mTitle);
        dialog.mListView.setAdapter(adapter);
        dialog.updateTitleViewLayoutParams();
        dialog.setSelectListener(selectListener);
        return dialog;
    }

    public static TalpaOssdkListBottomDialog showDialog(Context context, List<String> items, CharSequence mTitle,
                                                        OnDialogItemSelectListener selectListener){
        TalpaOssdkListBottomDialog dialog = createDialog(context, items, mTitle, selectListener);
        dialog.show();
        return dialog;
    }

    //do  not set minHeight if the listview exist
    @Override
    public void initMinHeight(View parentPanel, int parentPanelPaddingButtom, int parentPanelPaddingTop) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mSelectListener != null){
            mSelectListener.onDialogItemSelect(position,parent.getAdapter().getItem(position));
        }
        this.dismiss();
    }
}
