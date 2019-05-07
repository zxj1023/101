package com.transsion.ossdk.dialog.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.transsion.ossdk.R;

/**
 * Created by liang.wu1 on 2017/8/17.
 */

public class TalpaOssdkDialogCustomeSingleCheckContainer extends FrameLayout{
    private TextView mContentTv;
    private CheckBox mCheckBox;

    public TalpaOssdkDialogCustomeSingleCheckContainer(Context context) {
        super(context);
    }

    public TalpaOssdkDialogCustomeSingleCheckContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TalpaOssdkDialogCustomeSingleCheckContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentTv = (TextView) findViewById(R.id.talpaossdk_dialog_content_tv);
        mCheckBox = (CheckBox) findViewById(R.id.talpaossdk_dialog_cb);
    }

    public void init(CharSequence content,
                     boolean defaultValue, CharSequence checkTitle, CompoundButton.OnCheckedChangeListener listener) {
        mContentTv.setText(content);
        mCheckBox.setText(checkTitle);
        mCheckBox.setChecked(defaultValue);
        mCheckBox.setOnCheckedChangeListener(listener);
    }
}
