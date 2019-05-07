package com.transsion.ossdk.dialog.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.transsion.ossdk.R;

/**
 * Created by liang.wu1 on 2017/8/17.
 */

public class TalpaOssdkDialogCustomeEditContainer extends LinearLayout{
    private EditText mEditText;
    private TextView mSecondTitleTv;
    private boolean mHasForceShowInputMethod;

    public TalpaOssdkDialogCustomeEditContainer(Context context) {
        super(context);
    }

    public TalpaOssdkDialogCustomeEditContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TalpaOssdkDialogCustomeEditContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mEditText = (EditText) findViewById(R.id.talpaossdk_dialog_et);
        mSecondTitleTv = (TextView) findViewById(R.id.talpaossdk_dialog_secodetitle_tv);
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && !mHasForceShowInputMethod){
                    showInputMethod(mEditText);
                    mHasForceShowInputMethod = true;
                }
            }
        });
    }

    public void init(CharSequence iputHint, CharSequence secondTitle) {
        mEditText.setHint(iputHint);
        if(!TextUtils.isEmpty(secondTitle)){
            mSecondTitleTv.setVisibility(VISIBLE);
            mSecondTitleTv.setText(secondTitle);
        }
    }

    private static void showInputMethod(final View view){
        view.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    public EditText getEditText() {
        return mEditText;
    }
}
