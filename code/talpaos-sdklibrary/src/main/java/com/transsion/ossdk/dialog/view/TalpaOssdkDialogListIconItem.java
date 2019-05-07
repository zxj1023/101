package com.transsion.ossdk.dialog.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.transsion.ossdk.R;
import com.transsion.ossdk.dialog.data.TalpaOssdkDialogIconItemData;

/**
 * Created by liang.wu1 on 2017/9/1.
 */

public class TalpaOssdkDialogListIconItem extends RelativeLayout implements Checkable {
    private ImageView mIconView;
    private TextView mTitleView;
    private TextView mSubTitleView;
    private CheckedTextView mCheckedTextView;

    private Context mContext;

    public TalpaOssdkDialogListIconItem(Context context) {
        super(context);
        mContext = context;
    }

    public TalpaOssdkDialogListIconItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public TalpaOssdkDialogListIconItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mIconView = (ImageView) findViewById(R.id.talpaossdk_iv);
        mTitleView = (TextView) findViewById(R.id.talpaossdk_title_tv);
        mSubTitleView = (TextView) findViewById(R.id.talpaossdk_subtitle_tv);
        mCheckedTextView = (CheckedTextView) findViewById(R.id.talpaossdk_checktextview);
    }

    public void bindView(TalpaOssdkDialogIconItemData data){
        if(data.getIconId() <=0 ){
            mIconView.setVisibility(View.GONE);
        } else {
            mIconView.setImageResource(data.getIconId());
            mIconView.setVisibility(View.VISIBLE);
        }

        if(TextUtils.isEmpty(data.getTitle())){
            mTitleView.setVisibility(View.GONE);
        } else {
            mTitleView.setText(data.getTitle());
            mTitleView.setVisibility(View.VISIBLE);
        }

        if(TextUtils.isEmpty(data.getSubTitle())){
            mSubTitleView.setVisibility(View.GONE);
            this.setMinimumHeight(mContext.getResources().getDimensionPixelSize(R.dimen.talpaossdk_dialog_listitem_height));
        } else {
            mSubTitleView.setText(data.getSubTitle());
            mSubTitleView.setVisibility(View.VISIBLE);
            this.setMinimumHeight(mContext.getResources().getDimensionPixelSize(R.dimen.talpaossdk_dialog_listitem_twoline_height));
        }
    }

    @Override
    public void setChecked(boolean checked) {
        if (mCheckedTextView != null){
            mCheckedTextView.setChecked(checked);
        }
    }

    @Override
    public boolean isChecked() {
        if(mCheckedTextView != null){
            return mCheckedTextView.isChecked();
        }
        return false;
    }

    @Override
    public void toggle() {
        if(mCheckedTextView != null){
            setChecked(!isChecked());
        }
    }

    public ImageView getIconView() {
        return mIconView;
    }

    public TextView getTitleView() {
        return mTitleView;
    }

    public TextView getSubTitleView() {
        return mSubTitleView;
    }

    public CheckedTextView getCheckedTextView() {
        return mCheckedTextView;
    }
}
