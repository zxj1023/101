package com.transsion.ossdk.btn;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import com.transsion.ossdk.R;

/**
 * Created by liang.wu1 on 2017/7/20.
 */

public class TalpaOssdkTwoBtnWithDivider extends LinearLayout {
    private Button mLeftBtn;
    private Button mRightBtn;

    public TalpaOssdkTwoBtnWithDivider(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.talpaossdk_two_btn_with_divider, this, true);
    }

    @Override
    protected void onFinishInflate() {
        mLeftBtn = (Button) findViewById(R.id.talpaossdk_left_btn);
        mRightBtn = (Button) findViewById(R.id.talpaossdk_right_btn);
        //mLeftBtn.setBackground(OssdkViewBgHelper.create(mLeftBtn));
        //mRightBtn.setBackground(OssdkViewBgHelper.create(mRightBtn));
    }

    public void initBtn(CharSequence leftText, OnClickListener leftListener,
                        CharSequence rightText, OnClickListener rightListener){
        mLeftBtn.setText(leftText);
        mLeftBtn.setOnClickListener(leftListener);
        mRightBtn.setText(rightText);
        mRightBtn.setOnClickListener(rightListener);
    }

    public Button getLeftButton() {
        return mLeftBtn;
    }

    public Button getRightBtn() {
        return mRightBtn;
    }
}
