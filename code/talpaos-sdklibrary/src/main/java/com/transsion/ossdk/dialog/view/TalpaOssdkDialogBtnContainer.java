package com.transsion.ossdk.dialog.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.transsion.ossdk.R;

/**
 * Created by liang.wu1 on 2017/8/17.
 */

public class TalpaOssdkDialogBtnContainer extends LinearLayout implements View.OnClickListener {
    private Button mCancelBtn;
    private DialogBtnClickListener mCancelBtnClickListener;
    private Button mPositiveBtn;
    private DialogBtnClickListener mPositiveBtnClickListener;

    private Button mFirstBtn;
    private DialogBtnClickListener mFirstBtnClickListener;

    private DialogBtnClickFinishedClickListener mClickFinishedClickListener;

    public TalpaOssdkDialogBtnContainer(Context context) {
        super(context);
    }

    public TalpaOssdkDialogBtnContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TalpaOssdkDialogBtnContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mCancelBtn = (Button)findViewById(android.R.id.button2);
        mCancelBtn.setOnClickListener(this);
        mPositiveBtn = (Button)findViewById(android.R.id.button1);
        mPositiveBtn.setOnClickListener(this);

        mFirstBtn = (Button)findViewById(android.R.id.button3);
        mFirstBtn.setOnClickListener(this);
    }

    public void init(CharSequence cancelTitle, CharSequence positiveTitle, DialogBtnClickListener cancelBtnClickListener,
                     DialogBtnClickListener positiveBtnClickListener, DialogBtnClickFinishedClickListener clickFinishedClickListener) {
        mCancelBtn.setText(cancelTitle);
        mPositiveBtn.setText(positiveTitle);
        mCancelBtnClickListener = cancelBtnClickListener;
        mPositiveBtnClickListener = positiveBtnClickListener;
        mClickFinishedClickListener = clickFinishedClickListener;
    }

    public void init(CharSequence cancelTitle, CharSequence positiveTitle, CharSequence firstTitle,
                     DialogBtnClickListener cancelBtnClickListener,
                     DialogBtnClickListener positiveBtnClickListener,
                     DialogBtnClickListener firstBtnClickListener,
                     DialogBtnClickFinishedClickListener clickFinishedClickListener) {
        mCancelBtn.setText(cancelTitle);
        mPositiveBtn.setText(positiveTitle);
        mCancelBtnClickListener = cancelBtnClickListener;
        mPositiveBtnClickListener = positiveBtnClickListener;
        if(!TextUtils.isEmpty(firstTitle)){
            mFirstBtn.setVisibility(VISIBLE);
            mFirstBtn.setText(firstTitle);
            mFirstBtnClickListener = firstBtnClickListener;
        }
        mClickFinishedClickListener = clickFinishedClickListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case android.R.id.button2:
                if(mCancelBtnClickListener != null){
                    mCancelBtnClickListener.onClick();
                }
                break;
            case android.R.id.button1:
                if(mPositiveBtnClickListener != null){
                    mPositiveBtnClickListener.onClick();
                }
                break;
            case android.R.id.button3:
                if(mFirstBtnClickListener != null){
                    mFirstBtnClickListener.onClick();
                }
                break;
        }
        if(mClickFinishedClickListener != null){
            mClickFinishedClickListener.onClickFinished();
        }
    }

    public static final int BTN_TYPE_FIRST     = 0;
    public static final int BTN_TYPE_CANCEL    = 1;
    public static final int BTN_TYPE_POSITIVE  = 2;
    public void updateBtnColor(int color, int btnType){
        switch (btnType){
            case BTN_TYPE_FIRST:
                mFirstBtn.setTextColor(getColor(this.getContext(), color));
                break;
            case BTN_TYPE_CANCEL:
                mCancelBtn.setTextColor(getColor(this.getContext(), color));
                break;
            case BTN_TYPE_POSITIVE:
                mPositiveBtn.setTextColor(getColor(this.getContext(), color));
                break;
        }
    }

    private static int getColor(Context context, int color){
        if(color > 0){
            return color;
        }
        return context.getResources().getColor(R.color.talpaossdk_dialog_deletebtn_textcolor);
    }

    public void initFirstBtn(CharSequence firstTitle, DialogBtnClickListener firstBtnClickListener) {
        if(!TextUtils.isEmpty(firstTitle)){
            mFirstBtn.setVisibility(VISIBLE);
            mFirstBtn.setText(firstTitle);
            mFirstBtnClickListener = firstBtnClickListener;
        }
    }

    public Button getBtnById(int btnId){
        switch ((btnId)){
            case android.R.id.button1:
                return mPositiveBtn;
            case android.R.id.button2:
                return mCancelBtn;
            case android.R.id.button3:
                return mFirstBtn;
        }
        return null;
    }

    public void setClickFinishedClickListener(DialogBtnClickFinishedClickListener clickFinishedClickListener) {
        mClickFinishedClickListener = clickFinishedClickListener;
    }

    public interface DialogBtnClickListener{
        void onClick();
    }

    public interface DialogBtnClickFinishedClickListener{
        void onClickFinished();
    }
}
