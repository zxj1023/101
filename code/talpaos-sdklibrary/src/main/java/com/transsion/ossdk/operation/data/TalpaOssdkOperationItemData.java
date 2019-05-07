package com.transsion.ossdk.operation.data;

/**
 * Created by liang.wu1 on 2017/6/30.
 */

public class TalpaOssdkOperationItemData {
    private int mIndex;
    private CharSequence mTitle;
    private int mIconId;
    private boolean mIsMoreItem;
    private boolean mIsEnable;

    public TalpaOssdkOperationItemData(int index, CharSequence title, int iconId) {
        this.mIndex = index;
        this.mTitle = title;
        this.mIconId = iconId;
        this.mIsEnable = true;
    }

    public TalpaOssdkOperationItemData(int index, CharSequence title, int iconId, boolean isEnable) {
        this.mIndex = index;
        this.mTitle = title;
        this.mIconId = iconId;
        this.mIsEnable = isEnable;
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public int getIconId() {
        return mIconId;
    }

    public void setIsMoreItem(boolean isMoreItem) {
        this.mIsMoreItem = isMoreItem;
    }

    public boolean isIsMoreItem() {
        return mIsMoreItem;
    }

    public int getIndex() {
        return mIndex;
    }

    public boolean isIsEnable() {
        return mIsEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.mIsEnable = isEnable;
    }
}
