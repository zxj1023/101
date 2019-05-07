package com.transsion.ossdk.drawable;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.lang.ref.WeakReference;

/**
 * Created by liang.wu1 on 2017/7/24.
 */

public class TalpaOssdkCustomePressedDrawable extends Drawable {
    private Paint mMaskPaint;
    private int mCircleRadius;
    private int mCircleColor;

    private ValueAnimator mAnimation;
    private int mDuration;
    private int mStartAlpha;
    private int mEndAlpha;
    private int mCurrentAlpha;

    private int mBgColor;

    private int mWidth;
    private int mHeight;

    private Paint mBgPaint;
    private int mBgAlpha;

    private boolean mIsRipple;
    private boolean mIsDown = false;

    private int mType;
    public static final int BG_RECT_FOREBG_CIRCLE   = 0;
    public static final int BG_CIRCLE_FOREBG_CIRCLE = 1;
    public static final int BG_RECT_FOREBG_RECT      = 2;

    private WeakReference<View> mView;

    public TalpaOssdkCustomePressedDrawable(View view, int type,
                                            int circleRadius, int bgcolor, int maskColor, int duration, int startAlpha, int endAlpha) {
        mType = type;
        init(circleRadius, bgcolor, maskColor, duration, startAlpha, endAlpha);
        initWH(view);
        initPaint();
    }

    public TalpaOssdkCustomePressedDrawable(int type, int width, int bgcolor, int maskColor, int duration, int startAlpha, int endAlpha) {
        mType = type;
        init(width/2, width, width, bgcolor, maskColor, duration, startAlpha, endAlpha);
        initPaint();
    }

    private void initPaint(){
        mBgAlpha = Color.alpha(mBgColor);

        mMaskPaint = new Paint();
        mMaskPaint.setColor(mCircleColor);
        mMaskPaint.setAntiAlias(true);

        mBgPaint = new Paint();
        mBgPaint.setColor(mBgColor);
        mBgPaint.setAntiAlias(true);
    }

    public void initWH(View view){
        if(view != null){
            mView = new WeakReference<View>(view);
            if(mView != null){
                mView.get().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        initWH();
                    }
                });
                mView.get().post(new Runnable() {
                    @Override
                    public void run() {
                        initWH();
                    }
                });
            }
        }
    }

    private void initWH(){
        View view = mView.get();
        if (view == null) {
            return;
        }
        mWidth = view.getWidth();
        mHeight = view.getHeight();
    }

    private void init(int circleRadius, int width, int height, int bgColor, int circleColor,
                      int duration, int startAlpha, int endAlpha){
        mCircleRadius = circleRadius;
        mWidth = width;
        mHeight = height;
        mCircleColor = circleColor;
        mBgColor = bgColor;
        mDuration = duration;
        mStartAlpha = startAlpha;
        mEndAlpha = endAlpha;
    }

    private void init(int circleRadius, int bgColor, int circleColor, int duration, int startAlpha, int endAlpha){
        mCircleRadius = circleRadius;
        mCircleColor = circleColor;
        mBgColor = bgColor;
        mDuration = duration;
        mStartAlpha = startAlpha;
        mEndAlpha = endAlpha;
    }

    @Override
    protected boolean onStateChange(int[] stateSet) {
        final boolean changed = super.onStateChange(stateSet);
        boolean enabled = false;
        boolean pressed = false;

        for (int state : stateSet) {
            if (state == android.R.attr.state_enabled) {
                enabled = true;
            }

            if (state == android.R.attr.state_pressed) {
                pressed = true;
            }
        }

        if (enabled && pressed) {
            mIsDown = true;
            ripple();
        } else {
            if (mIsDown == true) {
                mIsDown = false;
                invalidateSelf();
                if (mIsRipple == false) {
                    rippleFade();
                }
            }
        }

        return changed;
    }

    @Override
    public void draw(Canvas canvas) {
        switch (mType){
            case BG_RECT_FOREBG_CIRCLE:
                canvas.drawRect(0, 0, mWidth, mHeight, mBgPaint);
                if (mIsRipple || mIsDown) {
                    mMaskPaint.setAlpha(mCurrentAlpha);
                    canvas.drawCircle(mWidth / 2, mHeight / 2, mCircleRadius, mMaskPaint);
                }
                break;
            case BG_CIRCLE_FOREBG_CIRCLE:
                canvas.drawCircle(mWidth/2, mWidth/2, mWidth/2, mBgPaint);
                if(mIsRipple || mIsDown){
                    mMaskPaint.setAlpha(mCurrentAlpha);
                    canvas.drawCircle(mWidth/2, mHeight/2, mCircleRadius, mMaskPaint);
                }
                break;
            case BG_RECT_FOREBG_RECT:
                canvas.drawRect(0, 0, mWidth, mHeight, mBgPaint);
                if(mIsRipple || mIsDown){
                    mMaskPaint.setAlpha(mCurrentAlpha);
                    canvas.drawRect(0, 0, mWidth, mHeight, mMaskPaint);
                }
                break;
        }
    }

    @Override
    public int getOpacity() {
        return mBgAlpha;
    }

    /** This is an empty implementation, it does nothing. Don't use. */
    @Override
    public void setColorFilter(ColorFilter aColorFilter) {/* do nothing */
        mMaskPaint.setColorFilter(aColorFilter);
    }

    @Override
    public void setAlpha(int aAlpha) {
        mBgAlpha = aAlpha;
        mMaskPaint.setAlpha(mBgAlpha);
    }
    @Override
    public boolean isStateful() {
        return true;
    }


    public void ripple() {
        cancelAnimation();

        mIsRipple = true;

        mAnimation = ValueAnimator.ofInt(mCurrentAlpha, mStartAlpha, mEndAlpha);
        mAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator aValueAnimator) {
                int alpha = (Integer) aValueAnimator.getAnimatedValue();
                mCurrentAlpha = alpha;
                if(alpha >= mEndAlpha){
                    mIsRipple = false;
                }
                invalidateSelf();
            }
        });
        mAnimation.setDuration(mDuration);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.start();
    }

    public void rippleFade() {
        cancelAnimation();

        mIsRipple = true;

        mAnimation = ValueAnimator.ofInt(mCurrentAlpha, mEndAlpha, mStartAlpha);
        mAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator aValueAnimator) {
                mCurrentAlpha = (Integer) aValueAnimator.getAnimatedValue();
                if(mCurrentAlpha <= mStartAlpha){
                    mIsRipple = false;
                }
                invalidateSelf();
            }
        });
        mAnimation.setDuration(mDuration);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.start();
    }

    private void cancelAnimation() {
        if (mAnimation != null && (mAnimation.isStarted() || mAnimation.isRunning())) {
            mAnimation.cancel();
        }
    }

}
