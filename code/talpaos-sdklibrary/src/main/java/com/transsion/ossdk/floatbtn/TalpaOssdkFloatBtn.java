package com.transsion.ossdk.floatbtn;

import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.transsion.ossdk.drawable.TalpaOssdkCustomePressedDrawable;

import static android.view.View.MeasureSpec.EXACTLY;
import com.transsion.ossdk.R;

/**
 * Created by liang.wu1 on 2017/7/20.
 */

public class TalpaOssdkFloatBtn extends FrameLayout {
    private int mFloatBtnColor;
    private int mShadowColor;
    private int mShadowOffsetY;
    private int mShadowRadius;
    private int mFloatBtnSrc;

    private int mFloatBtnWidth;
    private int mMarginLeft;
    private int mMaxHeight;
    private int mMaxWidth;

    private int mStartAlpha;
    private int mEndAlpha;

    public TalpaOssdkFloatBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray typedAttributes = context.obtainStyledAttributes(attrs, R.styleable.TalpaOssdkFloatBtn);
        this.mFloatBtnColor = typedAttributes.getColor(R.styleable.TalpaOssdkFloatBtn_talpaossdkFloatBtnColor, Color.BLACK);
        this.mShadowColor = typedAttributes.getColor(R.styleable.TalpaOssdkFloatBtn_talpaossdkShadowColor, Color.BLACK);
        this.mShadowOffsetY = (int) typedAttributes.getDimension(R.styleable.TalpaOssdkFloatBtn_talpaossdkShadowOffsetY, 0);
        this.mShadowRadius = (int) typedAttributes.getDimension(R.styleable.TalpaOssdkFloatBtn_talpaossdkShadowRadius, 0);
        this.mFloatBtnSrc = typedAttributes.getResourceId(R.styleable.TalpaOssdkFloatBtn_talpaossdkFloatBtnSrc, -1);
        typedAttributes.recycle();

        this.mFloatBtnWidth = context.getResources().getDimensionPixelSize(R.dimen.talpaossdk_floatbtn_wh);
        this.mMarginLeft = context.getResources().getDimensionPixelSize(R.dimen.talpaossdk_floatbtn_marginLeft);
        this.mMaxHeight = context.getResources().getDimensionPixelSize(R.dimen.talpaossdk_floatbtn_maxHeight);
        this.mMaxWidth = context.getResources().getDimensionPixelSize(R.dimen.talpaossdk_floatbtn_maxWidth);

        this.mStartAlpha = context.getResources().getInteger(R.integer.talpaossdk_floatbtn_mask_startalpha);
        this.mEndAlpha = context.getResources().getInteger(R.integer.talpaossdk_floatbtn_mask_endalpha);
    }

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

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        TalpaOssdkShadowBgView view = new TalpaOssdkShadowBgView(getContext(), new TalpaOssdkFloatBtnData(mFloatBtnWidth, mShadowColor, mShadowOffsetY, mShadowRadius, mMarginLeft));
        this.addView(view);

        ImageView iv = new ImageView(getContext());
        iv.setStateListAnimator(AnimatorInflater.loadStateListAnimator(this.getContext(), R.animator.talpaossdk_btn_shadow_anim));
        iv.setScaleType(ImageView.ScaleType.CENTER);
        iv.setBackground(new TalpaOssdkCustomePressedDrawable(TalpaOssdkCustomePressedDrawable.BG_CIRCLE_FOREBG_CIRCLE,
                mFloatBtnWidth, mFloatBtnColor, Color.BLACK,
                this.getContext().getResources().getInteger(R.integer.talpaossdk_operation_item_bg_animation_duration),
                mStartAlpha, mEndAlpha));
        if(mFloatBtnSrc > 0){
            iv.setImageResource(mFloatBtnSrc);
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(mFloatBtnWidth, mFloatBtnWidth);
        layoutParams.setMarginStart(mMarginLeft);
        layoutParams.topMargin = 2;
        this.addView(iv, layoutParams);
    }
}
