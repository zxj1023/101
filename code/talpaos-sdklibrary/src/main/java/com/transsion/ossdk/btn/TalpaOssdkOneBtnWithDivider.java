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

public class TalpaOssdkOneBtnWithDivider extends LinearLayout {
    private Button mButton;

    public TalpaOssdkOneBtnWithDivider(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.talpaossdk_one_btn_with_divider, this, true);
    }

    @Override
    protected void onFinishInflate() {
        mButton = (Button) findViewById(R.id.btn);
        //mButton.setBackground(OssdkViewBgHelper.create(mButton));
    }

    public void initBtn(CharSequence text, OnClickListener listener){
        mButton.setText(text);
        mButton.setOnClickListener(listener);
    }

    public Button getButton() {
        return mButton;
    }
}
