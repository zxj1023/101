package com.transsion.ossdk.operation.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.transsion.ossdk.R;
import com.transsion.ossdk.operation.data.TalpaOssdkOperationItemData;

/**
 * Created by liang.wu1 on 2017/6/30.
 */

public class TalpaOssdkOperationItemView extends RelativeLayout {

    private TextView mTv;
    private ImageView mIv;

    public TalpaOssdkOperationItemView(Context context) {
        super(context);
    }

    public TalpaOssdkOperationItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TalpaOssdkOperationItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTv = (TextView) findViewById(R.id.talpaossdk_item_tv);
        mIv = (ImageView) findViewById(R.id.talpaossdk_item_iv);
    }

    public void bindData(TalpaOssdkOperationItemData data) {
        mTv.setText(data.getTitle());
        mIv.setImageResource(data.getIconId());
    }
}
