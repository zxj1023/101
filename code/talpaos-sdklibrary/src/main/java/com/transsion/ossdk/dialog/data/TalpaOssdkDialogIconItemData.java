package com.transsion.ossdk.dialog.data;

/**
 * Created by liang.wu1 on 2017/8/31.
 */

public class TalpaOssdkDialogIconItemData {
    private int mIconId;
    private CharSequence mTitle;
    private CharSequence mSubTitle;
    private boolean mIsChecked;

    public TalpaOssdkDialogIconItemData(int iconId, CharSequence title, CharSequence subTitle, boolean isChecked) {
        this.mIconId = iconId;
        this.mTitle = title;
        this.mSubTitle = subTitle;
        this.mIsChecked = isChecked;
    }

    public int getIconId() {
        return mIconId;
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public CharSequence getSubTitle() {
        return mSubTitle;
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.mIsChecked = isChecked;
    }
}
