package com.transsion.ossdk.dialog.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.transsion.ossdk.R;


/**
 * Created by liang.wu1 on 2017/8/17.
 */

public class TalpaOssdkDialogCustomeContentContainer extends FrameLayout{
    private TextView mContentTv;
    private ImageView mLeftIconIv;

    public TalpaOssdkDialogCustomeContentContainer(Context context) {
        super(context);
    }

    public TalpaOssdkDialogCustomeContentContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TalpaOssdkDialogCustomeContentContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentTv = (TextView) findViewById(R.id.talpaossdk_dialog_content_tv);
        mLeftIconIv = (ImageView) findViewById(R.id.talpaossdk_dialog_left_iv);
    }

    public void init(CharSequence content){
        mContentTv.setText(content);
        this.post(new Runnable() {
            @Override
            public void run() {
                if (mContentTv.getLineCount() == 1) {
                    mContentTv.setGravity(Gravity.CENTER);
                }
            }
        });

        if(mLeftIconIv != null){
            mLeftIconIv.setVisibility(View.GONE);
        }
    }

    public void init(CharSequence content, int iconId){
        mContentTv.setText(content);
        mLeftIconIv.setImageResource(iconId);
    }
}
