package com.transsion.ossdk.dialog;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.transsion.ossdk.R;
import com.transsion.ossdk.dialog.view.TalpaOssdkDialogBtnContainer;
import com.transsion.ossdk.dialog.view.TalpaOssdkDialogCustomeEditContainer;

/**
 * Created by liang.wu1 on 2017/8/17.
 */

public class TalpaOssdkWithEditBottomDialog extends TalpaOssdkBottomDialog {

    private TalpaOssdkDialogCustomeEditContainer mEditContainer;

    public TalpaOssdkWithEditBottomDialog(Context context) {
        super(context, R.layout.talpaossdk_with_edit_bottom_dialog);
        initEditContainer();
    }

    private void initEditContainer(){
        mEditContainer = (TalpaOssdkDialogCustomeEditContainer)mRootView.findViewById(R.id.talpaossdk_dialog_edit_container);
    }

    public static TalpaOssdkWithEditBottomDialog createDialog(Context context, CharSequence title,
                                                            CharSequence iputHint, CharSequence secondTitle,
                                                            CharSequence cancelTitle, CharSequence positiveTitle,
                                                            TalpaOssdkDialogBtnContainer.DialogBtnClickListener cancelBtnClickListener,
                                                            TalpaOssdkDialogBtnContainer.DialogBtnClickListener positiveBtnClickListener
                                                            ){
        TalpaOssdkWithEditBottomDialog dialog = new TalpaOssdkWithEditBottomDialog(context);
        dialog.setCustomeTitle(title);
        dialog.updateDividerVisibility(View.GONE);
        dialog.initEidtContainer(iputHint, secondTitle);
        dialog.initBtnContainer(cancelTitle, positiveTitle, cancelBtnClickListener, positiveBtnClickListener);
        return dialog;
    }

    public static TalpaOssdkWithEditBottomDialog showDialog(Context context, CharSequence title,
                                                            CharSequence iputHint, CharSequence secondTitle,
                                                            CharSequence cancelTitle, CharSequence positiveTitle,
                                                            TalpaOssdkDialogBtnContainer.DialogBtnClickListener cancelBtnClickListener,
                                                            TalpaOssdkDialogBtnContainer.DialogBtnClickListener positiveBtnClickListener){
        TalpaOssdkWithEditBottomDialog dialog = createDialog(context, title, iputHint, secondTitle, cancelTitle, positiveTitle,
                    cancelBtnClickListener, positiveBtnClickListener);
        dialog.show();
        return dialog;
    }

    private void initEidtContainer(CharSequence iputHint, CharSequence secondTitle) {
        mEditContainer.init(iputHint, secondTitle);
    }

    public EditText getEditText(){
        if(mEditContainer != null){
            return mEditContainer.getEditText();
        }
        return null;
    }
}
