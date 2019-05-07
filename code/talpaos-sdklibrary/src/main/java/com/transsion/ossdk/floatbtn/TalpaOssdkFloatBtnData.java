package com.transsion.ossdk.floatbtn;

/**
 * Created by liang.wu1 on 2017/7/20.
 */

public class TalpaOssdkFloatBtnData {
    private int mFloatBtnWidth;
    private int mShadowColor;
    private int mShadowOffsetY;
    private int mShadowRadius;
    private int mMarginLeft;

    public TalpaOssdkFloatBtnData(int floatBtnWidth, int shadowColor, int shadowOffsetY, int shadowRadius, int marginLeft) {
        this.mFloatBtnWidth = floatBtnWidth;
        this.mShadowColor = shadowColor;
        this.mShadowOffsetY = shadowOffsetY;
        this.mShadowRadius = shadowRadius;
        this.mMarginLeft = marginLeft;
    }

    public int getFloatBtnWidth() {
        return mFloatBtnWidth;
    }

    public int getShadowColor() {
        return mShadowColor;
    }

    public int getShadowOffsetY() {
        return mShadowOffsetY;
    }

    public int getShadowRadius() {
        return mShadowRadius;
    }

    public int getMarginLeft() {
        return mMarginLeft;
    }
}
