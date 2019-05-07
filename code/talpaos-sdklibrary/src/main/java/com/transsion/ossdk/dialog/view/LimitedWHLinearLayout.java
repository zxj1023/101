/*
 * 类型：LimitedHeightLinearLayout
 * 版本：
 * 日期：2010-11-8
 * Copyright (C) 2010 中国广东省珠海市魅族科技有限公司版权所有
 * 修改历史记录：
 * 2010-11-8  zero  初始版本创建      
 */
package com.transsion.ossdk.dialog.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import static android.view.View.MeasureSpec.EXACTLY;

/**
 * the linerLayout which can set the max height via method setMaxHeight
 * and set the max width via method setMaxWidth。
 * 
 * @hide
 */
public class LimitedWHLinearLayout extends LinearLayout {

    private int mMaxHeight = Integer.MAX_VALUE;
    private int mMaxWidth = Integer.MAX_VALUE;
    
    /**
     * @param context
     * @param attrs
     */
    public LimitedWHLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param context
     */
    public LimitedWHLinearLayout(Context context) {
        super(context);
    }

    /* (non-Javadoc)
     * @see android.widget.LinearLayout#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
       
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        boolean measure = false;
        
        // the height can't be higher then max height
        if (height > mMaxHeight) {
            height = mMaxHeight;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,EXACTLY);
            measure = true;
        }
        
        if(width > mMaxWidth) {
            width = mMaxWidth;
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,EXACTLY);
            measure = true;
        }
        
        if (measure) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);            
        }
    }
    
    /**
     * set the max height
     * @param max 
     */
    public void setMaxHeight(int max) {
        mMaxHeight = max;
    }
    
    public int getMaxHeight() {
        return mMaxHeight;
    }
    
    /**
     * set the max width
     * @param max
     */
    public void setMaxWidth(int max) {
        mMaxWidth = max;
    }
    
    public int getMaxWidth() {
        return mMaxWidth;
    }
}
