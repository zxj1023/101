package com.transsion.ossdk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.transsion.ossdk.R;
import com.transsion.ossdk.dialog.view.LimitedWHLinearLayout;
import com.transsion.ossdk.dialog.view.TalpaOssdkDialogBtnContainer;

/**
 * Created by liang.wu1 on 2017/8/17.
 */

public class TalpaOssdkBottomDialog extends Dialog {
    private Context mContext;

    protected View mRootView;
    protected TextView mTitleView;
    private View mHeadDivider;

    protected TalpaOssdkDialogBtnContainer mBtnContainer;

    public TalpaOssdkBottomDialog(Context context, int layoutId) {
        super(context, R.style.TalpaOssdk_Theme_Dialog);
        mContext = context;
        mRootView = LayoutInflater.from(context).inflate(layoutId, null);
        setContentView(mRootView);
        initDialog();

        initView();
    }

    private void initDialog(){
        Window window = this.getWindow();

        //max height
        LimitedWHLinearLayout parentPanel = (LimitedWHLinearLayout) window.findViewById(R.id.parentPanel);
        int parentPanelPaddingTop = parentPanel.getPaddingTop();
        int parentPanelPaddingButtom = parentPanel.getPaddingBottom();
        int parentPanelMaxHeight = mContext.getResources().getDimensionPixelSize(R.dimen.talpaossdk_dialog_max_height)
                + parentPanelPaddingTop + parentPanelPaddingButtom;
        parentPanel.setMaxHeight(parentPanelMaxHeight);

        initMinHeight(parentPanel, parentPanelPaddingButtom, parentPanelPaddingTop);

        //at bottom
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.gravity = Gravity.BOTTOM;
        this.onWindowAttributesChanged(wl);
    }

    private void initView(){
        mHeadDivider = mRootView.findViewById(R.id.talpaossdk_dialog_title_divider);
        mTitleView = (TextView) mRootView.findViewById(R.id.talpaossdk_dialog_title);
    }

    public void setCustomeTitle(CharSequence title){
        if(!TextUtils.isEmpty(title)){
            mTitleView.setText(title);
        } else{
            mTitleView.setVisibility(View.GONE);
        }
    }

    public void updateTitleAndDivider(CharSequence title){
        if(!TextUtils.isEmpty(title)){
            mTitleView.setText(title);
            updateDividerVisibility(View.VISIBLE);
        } else{
            mTitleView.setVisibility(View.GONE);
            updateDividerVisibility(View.GONE);
        }
    }

    public void updateDividerVisibility(int visibility){
        mHeadDivider.setVisibility(visibility);
    }

    public void updateTitleViewLayoutParams(){
        int paddingLr = mContext.getResources().getDimensionPixelSize(R.dimen.talpaossdk_dialog_title_in_list_paddingTop);
        mTitleView.setPadding(mTitleView.getPaddingLeft(), paddingLr, mTitleView.getPaddingRight(), paddingLr);
        mTitleView.setMinHeight(mContext.getResources().getDimensionPixelSize(R.dimen.talpaossdk_dialog_listitem_height));
        mTitleView.setGravity(Gravity.CENTER);
    }

    public void initBtnContainer(CharSequence cancelTitle, CharSequence positiveTitle,
                                 TalpaOssdkDialogBtnContainer.DialogBtnClickListener cancelBtnClickListener,
                                 TalpaOssdkDialogBtnContainer.DialogBtnClickListener positiveBtnClickListener){
        mBtnContainer = (TalpaOssdkDialogBtnContainer)findViewById(R.id.talpaossdk_dialog_btn_container);
        if(mBtnContainer != null){
            mBtnContainer.init(cancelTitle, positiveTitle, cancelBtnClickListener, positiveBtnClickListener, new TalpaOssdkDialogBtnContainer.DialogBtnClickFinishedClickListener() {
                @Override
                public void onClickFinished() {
                    TalpaOssdkBottomDialog.this.dismiss();
                }
            });
        }
    }

    public void initBtnContainer(CharSequence cancelTitle, CharSequence positiveTitle, CharSequence firstTitle,
                                 TalpaOssdkDialogBtnContainer.DialogBtnClickListener cancelBtnClickListener,
                                 TalpaOssdkDialogBtnContainer.DialogBtnClickListener positiveBtnClickListener,
                                 TalpaOssdkDialogBtnContainer.DialogBtnClickListener firstBtnClickListener){
        mBtnContainer = (TalpaOssdkDialogBtnContainer)findViewById(R.id.talpaossdk_dialog_btn_container);
        if(mBtnContainer != null){
            mBtnContainer.init(cancelTitle, positiveTitle,  firstTitle, cancelBtnClickListener,
                    positiveBtnClickListener, firstBtnClickListener, new TalpaOssdkDialogBtnContainer.DialogBtnClickFinishedClickListener() {
                @Override
                public void onClickFinished() {
                    TalpaOssdkBottomDialog.this.dismiss();
                }
            });
        }
    }

    public void initFirstBtn(CharSequence firstTitle, TalpaOssdkDialogBtnContainer.DialogBtnClickListener firstBtnClickListener){
        mBtnContainer = (TalpaOssdkDialogBtnContainer)findViewById(R.id.talpaossdk_dialog_btn_container);
        if(mBtnContainer != null){
            mBtnContainer.initFirstBtn(firstTitle, firstBtnClickListener);
        }
    }

    public void updateBtnColor(int color, int type){
        if(mBtnContainer != null){
            mBtnContainer.updateBtnColor(color, type);
        }
    }

    public void updatePositiveBtnColor(int color){
        updateBtnColor(color, TalpaOssdkDialogBtnContainer.BTN_TYPE_POSITIVE);
    }

    public Button getBtnById(int btnId){
        if(mBtnContainer != null){
            return mBtnContainer.getBtnById(btnId);
        }
        return null;
    }

    public void initMinHeight(View parentPanel, int parentPanelPaddingButtom, int parentPanelPaddingTop){
        //min height
        final int MIN_HEIGHT_WITH_BUTTON = mContext.getResources().getDimensionPixelSize(R.dimen.talpaossdk_dialog_min_height)
                + parentPanelPaddingButtom + parentPanelPaddingTop;
        parentPanel.setMinimumHeight(MIN_HEIGHT_WITH_BUTTON);
    }

    public void setClickFinishedClickListener(TalpaOssdkDialogBtnContainer.DialogBtnClickFinishedClickListener clickFinishedClickListener) {
        if(mBtnContainer != null){
            mBtnContainer.setClickFinishedClickListener(clickFinishedClickListener);
        }
    }

    public interface OnDialogItemSelectListener<T> {
        void onDialogItemSelect(int index, T t);
    }
}
