package com.transsion.ossdk.dialog;

import android.content.Context;
import android.view.View;

import com.transsion.ossdk.R;
import com.transsion.ossdk.dialog.view.TalpaOssdkDialogBtnContainer;
import com.transsion.ossdk.dialog.view.TalpaOssdkDialogCustomeContentContainer;

/**
 * Created by liang.wu1 on 2017/8/17.
 */

public class TalpaOssdkContentBottomDialog extends TalpaOssdkBottomDialog {
    private TalpaOssdkDialogCustomeContentContainer mContentContainer;

    public TalpaOssdkContentBottomDialog(Context context) {
        super(context, R.layout.talpaossdk_content_dialog);
        init();
    }

    private void init(){
        mContentContainer = (TalpaOssdkDialogCustomeContentContainer) mRootView.findViewById(R.id.talpaossdk_dialog_custome_content_container);
    }

    public static TalpaOssdkContentBottomDialog createDialog(Context context, CharSequence title, CharSequence content,
                                                            CharSequence cancelTitle, CharSequence positiveTitle,
                                                            TalpaOssdkDialogBtnContainer.DialogBtnClickListener cancelListener,
                                                            TalpaOssdkDialogBtnContainer.DialogBtnClickListener positiveListener){
        TalpaOssdkContentBottomDialog dialog = showDialogBase(context, title);
        dialog.initContentContainter(content);
        dialog.initBtnContainer(cancelTitle, positiveTitle, cancelListener, positiveListener);
        return dialog;
    }

    public static TalpaOssdkContentBottomDialog showDialog(Context context, CharSequence title, CharSequence content,
                                                           CharSequence cancelTitle, CharSequence positiveTitle,
                                                           TalpaOssdkDialogBtnContainer.DialogBtnClickListener cancelListener,
                                                           TalpaOssdkDialogBtnContainer.DialogBtnClickListener positiveListener){
        TalpaOssdkContentBottomDialog dialog = createDialog(context, title, content, cancelTitle, positiveTitle, cancelListener, positiveListener);
        dialog.show();
        return dialog;
    }

    public static TalpaOssdkContentBottomDialog createDialog(Context context, CharSequence title, CharSequence content,
                                                           CharSequence cancelTitle, CharSequence positiveTitle, CharSequence firstTitle,
                                                           TalpaOssdkDialogBtnContainer.DialogBtnClickListener cancelListener,
                                                           TalpaOssdkDialogBtnContainer.DialogBtnClickListener positiveListener,
                                                           TalpaOssdkDialogBtnContainer.DialogBtnClickListener firstListener){
        TalpaOssdkContentBottomDialog dialog = showDialogBase(context, title);
        dialog.initContentContainter(content);
        dialog.initBtnContainer(cancelTitle, positiveTitle, firstTitle, cancelListener, positiveListener, firstListener);
        return dialog;
    }

    public static TalpaOssdkContentBottomDialog showDialog(Context context, CharSequence title, CharSequence content,
                                                           CharSequence cancelTitle, CharSequence positiveTitle, CharSequence firstTitle,
                                                           TalpaOssdkDialogBtnContainer.DialogBtnClickListener cancelListener,
                                                           TalpaOssdkDialogBtnContainer.DialogBtnClickListener positiveListener,
                                                           TalpaOssdkDialogBtnContainer.DialogBtnClickListener firstListener){
        TalpaOssdkContentBottomDialog dialog = createDialog(context, title, content, cancelTitle, positiveTitle, firstTitle, cancelListener,
                positiveListener, firstListener);
        dialog.show();
        return dialog;
    }

    public static TalpaOssdkContentBottomDialog createDialog(Context context, CharSequence title, CharSequence content,
                                                           int iconId,
                                                           CharSequence cancelTitle, CharSequence positiveTitle,
                                                           TalpaOssdkDialogBtnContainer.DialogBtnClickListener cancelListener,
                                                           TalpaOssdkDialogBtnContainer.DialogBtnClickListener positiveListener){
        TalpaOssdkContentBottomDialog dialog = showDialogBase(context, title);
        dialog.initContentContainter(content, iconId);
        dialog.initBtnContainer(cancelTitle, positiveTitle, cancelListener, positiveListener);
        return dialog;
    }

    public static TalpaOssdkContentBottomDialog showDialog(Context context, CharSequence title, CharSequence content,
                                                           int iconId,
                                                           CharSequence cancelTitle, CharSequence positiveTitle,
                                                           TalpaOssdkDialogBtnContainer.DialogBtnClickListener cancelListener,
                                                           TalpaOssdkDialogBtnContainer.DialogBtnClickListener positiveListener){
        TalpaOssdkContentBottomDialog dialog = createDialog(context, title, content, iconId, cancelTitle, positiveTitle, cancelListener, positiveListener);
        dialog.show();
        return dialog;
    }

    private static TalpaOssdkContentBottomDialog showDialogBase(Context context, CharSequence title){
        TalpaOssdkContentBottomDialog dialog = new TalpaOssdkContentBottomDialog(context);
        dialog.setCustomeTitle(title);
        dialog.updateDividerVisibility(View.GONE);
        return dialog;
    }

    private void initContentContainter(CharSequence content){
        mContentContainer.init(content);
    }

    private void initContentContainter(CharSequence content, int iconId){
        mContentContainer.init(content, iconId);
    }
}
