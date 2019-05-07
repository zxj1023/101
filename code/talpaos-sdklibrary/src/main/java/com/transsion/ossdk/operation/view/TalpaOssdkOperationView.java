package com.transsion.ossdk.operation.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.transsion.ossdk.R;
import com.transsion.ossdk.TalpaOssdkViewBgHelper;
import com.transsion.ossdk.operation.TalpaOssdkOperationBgHelper;

/**
 * Created by liang.wu1 on 2017/6/30.
 */

public class TalpaOssdkOperationView extends RelativeLayout {

    private TextView mTv;
    private ImageView mIv;

    private int mIconResId;
    private int mTitleResId;

    public TalpaOssdkOperationView(Context context) {
        this(context, null);
    }

    public TalpaOssdkOperationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TalpaOssdkOperationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray typedAttributes = context.obtainStyledAttributes(attrs, R.styleable.TalpaOssdkOperation);
        mIconResId = typedAttributes.getResourceId(R.styleable.TalpaOssdkOperation_talpaossdkOperationItemIcon, -1);
        mTitleResId = typedAttributes.getResourceId(R.styleable.TalpaOssdkOperation_talpaossdkOperationItemTitle, -1);
        typedAttributes.recycle();

        final LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.talpaossdk_operation_item_content, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTv = (TextView) findViewById(R.id.talpaossdk_item_tv);
        mIv = (ImageView) findViewById(R.id.talpaossdk_item_iv);

        if(mIconResId > 0){
            mIv.setImageResource(mIconResId);
        }
        if(mTitleResId > 0){
            mTv.setText(mTitleResId);
        }
        //TalpaOssdkOperationBgHelper.setBg(this);
    }

    public void updateIcon(int iconId){
        mIv.setImageResource(iconId);
    }

    public void updateTitle(int titleId){
        mTv.setText(titleId);
    }

    public ImageView getIconView(){
        return mIv;
    }

    public TextView getTitleView(){
        return mTv;
    }
}
