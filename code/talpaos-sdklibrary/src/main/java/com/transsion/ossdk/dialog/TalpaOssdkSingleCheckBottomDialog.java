package com.transsion.ossdk.dialog;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;

import com.transsion.ossdk.R;
import com.transsion.ossdk.dialog.view.TalpaOssdkDialogBtnContainer;
import com.transsion.ossdk.dialog.view.TalpaOssdkDialogCustomeSingleCheckContainer;

/**
 * Created by liang.wu1 on 2017/8/17.
 */

public class TalpaOssdkSingleCheckBottomDialog extends TalpaOssdkBottomDialog{
    private TalpaOssdkDialogCustomeSingleCheckContainer mContentContainer;

    public TalpaOssdkSingleCheckBottomDialog(Context context) {
        super(context, R.layout.talpaossdk_single_check_dialog);

        mContentContainer = (TalpaOssdkDialogCustomeSingleCheckContainer)findViewById(R.id.talpaossdk_dialog_custome_single_check_container);
    }

    public static TalpaOssdkSingleCheckBottomDialog createDialog(Context context, CharSequence title, CharSequence content,
                                                               boolean defaultValue, CharSequence checkTitle,
                                                               CompoundButton.OnCheckedChangeListener listener,
                                                               CharSequence cancelTitle, CharSequence positiveTitle,
                                                               TalpaOssdkDialogBtnContainer.DialogBtnClickListener cancelListener,
                                                               TalpaOssdkDialogBtnContainer.DialogBtnClickListener positiveListener){
        TalpaOssdkSingleCheckBottomDialog dialog = new TalpaOssdkSingleCheckBottomDialog(context);
        dialog.setCustomeTitle(title);
        dialog.updateDividerVisibility(View.GONE);
        dialog.initContentContainer(content, defaultValue, checkTitle, listener);
        dialog.initBtnContainer(cancelTitle, positiveTitle, cancelListener, positiveListener);
        return dialog;
    }

    public static TalpaOssdkSingleCheckBottomDialog showDialog(Context context, CharSequence title, CharSequence content,
                                                               boolean defaultValue, CharSequence checkTitle,
                                                               CompoundButton.OnCheckedChangeListener listener,
                                                               CharSequence cancelTitle, CharSequence positiveTitle,
                                                               TalpaOssdkDialogBtnContainer.DialogBtnClickListener cancelListener,
                                                               TalpaOssdkDialogBtnContainer.DialogBtnClickListener positiveListener){
        TalpaOssdkSingleCheckBottomDialog dialog = createDialog(context, title, content, defaultValue, checkTitle, listener, cancelTitle,
                                                    positiveTitle, cancelListener, positiveListener);
        dialog.show();
        return dialog;
    }

    private void initContentContainer(CharSequence content, boolean defaultValue, CharSequence checkTitle, CompoundButton.OnCheckedChangeListener listener){
        mContentContainer.init(content, defaultValue, checkTitle, listener);
    }
}
