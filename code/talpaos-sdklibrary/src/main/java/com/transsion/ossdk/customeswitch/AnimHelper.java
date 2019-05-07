package com.transsion.ossdk.customeswitch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by liang.wu1 on 2017/8/2.
 */

public class AnimHelper {
    private ValueAnimator mPositionAnimator;

    private AnimatorSet mThumbAnimatorSet;
    //thumb scale
    private ValueAnimator mThumbScaleLargeAnimator;
    private ValueAnimator mThumbScaleSmallAnimator;
    private float mThumbScale;

    //thumba alpha
    private ValueAnimator mGrayThumbAlphaAnimator;
    private int mGrayThumbAlpha;

    private ValueAnimator mRedThumbAlphaAnimator;
    private int mRedThumbAlpha;

    private float mScaleBg;
    private ValueAnimator mScaleBgAnimator;

    private ValueAnimator mTrackOnAlphaAnimator;
    private int mTrackOnAlpha;

    private AnimatorSet mAnimatorSet;

    private ThumbPositionListener mListener;

    private boolean mIsInAnim;

    public void start(float thumbPosition, boolean newCheckedState, ThumbPositionListener listener){
        mListener = listener;
        initPositionAnim(thumbPosition, newCheckedState);
        initThumbAnim(newCheckedState);

        mAnimatorSet = new AnimatorSet();
        initRedTrackAlphaAnim(newCheckedState);
        if(newCheckedState){
            initGrayTrackScaleAnimator();
            mAnimatorSet.playTogether(mPositionAnimator, mThumbAnimatorSet, mTrackOnAlphaAnimator, mScaleBgAnimator, mRedThumbAlphaAnimator);
        } else{
            mAnimatorSet.playTogether(mPositionAnimator, mThumbAnimatorSet, mTrackOnAlphaAnimator, mRedThumbAlphaAnimator);
        }
        mAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIsInAnim = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mIsInAnim = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsInAnim = false;
            }
        });
        mAnimatorSet.start();
    }

    //1.
    private void initPositionAnim(float thumbPosition, boolean isChecked){
        if(isChecked){
            createOpenPositionAnim(thumbPosition);
        } else{
            createClosePositionAnim(thumbPosition);
        }
    }

    //The last state is unChecked. The thumbPosition is [-0.1f, 1]
    private void createOpenPositionAnim(float thumbPosition){
        if(thumbPosition < 0){
            mPositionAnimator = createPositionAnim(580, 100, thumbPosition, 0, 1, 1.1f, 1);//580=100+380+100
            return;
        }
        if(thumbPosition == 0){
            mPositionAnimator = createPositionAnim(580, 100, 0, 1, 1.1f, 1);//580=100+380+100
            return;
        }

        mPositionAnimator = createPositionAnim(580, 100, thumbPosition, 1, 1.1f, 1);//580=100+380+100
    }

    //The last state is checked. The thumbPosition is [0, 1.1f]
    private void createClosePositionAnim(float thumbPosition){
        if(thumbPosition > 1){
            mPositionAnimator = createPositionAnim(540, 0, thumbPosition, 1, 0, -0.1f, 0);//540=340+200
            return;
        }
        if(thumbPosition == 1){
            mPositionAnimator = createPositionAnim(540, 0, 1, 0, -0.1f, 0);//540=340+200
            return;
        }
        //[0,1)
        mPositionAnimator = createPositionAnim(540, 0, thumbPosition, 0, -0.1f, 0);//540=340+200
    }

    private ValueAnimator createPositionAnim(int duration, int delay, float... values){
        ValueAnimator positionAnimator = ValueAnimator.ofFloat(values);
        positionAnimator.setDuration(duration);
        if(delay > 0){
            positionAnimator.setStartDelay(delay);
        }

        positionAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        positionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(mListener != null){
                    mListener.updteThumbPosition((Float) animation.getAnimatedValue());
                }
            }
        });
        return positionAnimator;
    }

    //2.
    private void initThumbAnim(boolean isChecked){
        initThumbScaleAnim(isChecked);

        initThumbAlphaAnimator(isChecked);

        AnimatorSet mThumbOffScaleAnimatorSet = new AnimatorSet();
        mThumbAnimatorSet = new AnimatorSet();
        if(isChecked){
            mThumbOffScaleAnimatorSet.playTogether(mGrayThumbAlphaAnimator, mThumbScaleSmallAnimator);
            mThumbAnimatorSet.playSequentially(mThumbScaleLargeAnimator, mThumbOffScaleAnimatorSet);
        } else {
            mThumbOffScaleAnimatorSet.playSequentially(mThumbScaleLargeAnimator, mThumbScaleSmallAnimator);
            mThumbAnimatorSet.playTogether(mGrayThumbAlphaAnimator, mThumbOffScaleAnimatorSet);
        }
    }

    private void initThumbScaleAnim(boolean isChecked){
        createThumbScaleLargeAnimator();
        createThumbScaleSmallAnimator(isChecked);
    }

    //mThumbScale: [1,1.1f] [1.1f,1]
    private void createThumbScaleLargeAnimator(){
        if(mThumbScale >= 1) {
            mThumbScaleLargeAnimator = createThumbScaleAnimator(200, 0, mThumbScale, 1.1f); //(1, 1.1f)
        } else {
            mThumbScaleLargeAnimator = createThumbScaleAnimator(200, 0, 1, 1.1f); //(1, 1.1f)
        }
    }

    //mThumbScale: [1,1.1f] [1.1f,1]
    private void createThumbScaleSmallAnimator(boolean isChecked){
        if(isChecked){
            if(mThumbScale >= 1){
                mThumbScaleSmallAnimator = createThumbScaleAnimator(180, 0, mThumbScale, 1 ); //(1.1f, 1)
            } else{
                mThumbScaleSmallAnimator = createThumbScaleAnimator(180, 0, 1.1f, 1 ); //(1.1f, 1)
            }
        } else{
            if(mThumbScale >= 1){
                mThumbScaleSmallAnimator = createThumbScaleAnimator(140, 0, mThumbScale, 1); //(1.1f, 1)
            } else{
                mThumbScaleSmallAnimator = createThumbScaleAnimator(140, 0, 1.1f, 1); //(1.1f, 1)
            }
        }
    }

    private ValueAnimator createThumbScaleAnimator(int duration, int delay, float... values){
        ValueAnimator thumbScaleAnimator = ValueAnimator.ofFloat(values);
        thumbScaleAnimator.setDuration(duration);
        if(delay > 0){
            thumbScaleAnimator.setStartDelay(delay);
        }
        thumbScaleAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        thumbScaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mThumbScale = (float) animation.getAnimatedValue();
            }
        });
        return thumbScaleAnimator;
    }

    private void initThumbAlphaAnimator(boolean isChecked){
        if(isChecked){
            if(mGrayThumbAlpha > 0){
                mGrayThumbAlphaAnimator = createGrayThumbAlphaAnimator(180, 0, mGrayThumbAlpha, 0); //(255, 0)
            } else{
                mGrayThumbAlphaAnimator = createGrayThumbAlphaAnimator(180, 0, 255, 0);
            }

            if(mRedThumbAlpha > 0){
                mRedThumbAlphaAnimator = createRedThumbAlphaAnimator(200, 0, mGrayThumbAlpha, 255); //(0, 255)
            } else{
                mRedThumbAlphaAnimator = createRedThumbAlphaAnimator(200, 0, 0, 255);
            }
        } else{
            if(mGrayThumbAlpha > 0){
                mGrayThumbAlphaAnimator = createGrayThumbAlphaAnimator(180, 0, mGrayThumbAlpha, 255); //(0, 255)
            } else{
                mGrayThumbAlphaAnimator = createGrayThumbAlphaAnimator(180, 0, 0, 255); //(0, 255)
            }

            if(mRedThumbAlpha > 0){
                mRedThumbAlphaAnimator = createRedThumbAlphaAnimator(380, 200, mRedThumbAlpha, 0); //(255, 0)
            } else {
                mRedThumbAlphaAnimator = createRedThumbAlphaAnimator(380, 200, 255, 0); //(255, 0)
            }
        }
    }

    private ValueAnimator createGrayThumbAlphaAnimator(int duration, int delay, int... values){
        ValueAnimator thumbAlphaAnimator = ValueAnimator.ofInt(values);
        thumbAlphaAnimator.setDuration(duration);
        if(delay > 0){
            thumbAlphaAnimator.setStartDelay(delay);
        }
        thumbAlphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        thumbAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mGrayThumbAlpha = (int) animation.getAnimatedValue();
            }
        });
        return thumbAlphaAnimator;
    }

    private ValueAnimator createRedThumbAlphaAnimator(int duration, int delay, int... values){
        ValueAnimator thumbAlphaAnimator = ValueAnimator.ofInt(values);
        thumbAlphaAnimator.setDuration(duration);
        if(delay > 0){
            thumbAlphaAnimator.setStartDelay(delay);
        }
        thumbAlphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        thumbAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRedThumbAlpha = (int) animation.getAnimatedValue();
            }
        });
        return thumbAlphaAnimator;
    }

    //3.
    private void initRedTrackAlphaAnim(boolean isChecked){
        if(isChecked){
            mTrackOnAlphaAnimator = createRedTrackAlphaAnimator(360, 100, mTrackOnAlpha, 255);//(0, 255)
        } else{
            if(mTrackOnAlpha > 0){
                mTrackOnAlphaAnimator = createRedTrackAlphaAnimator(300, 0, mTrackOnAlpha, 0); //(255, 0)
            } else{
                mTrackOnAlphaAnimator = createRedTrackAlphaAnimator(300, 0, 255, 0); //(255, 0)
            }
        }
    }

    private ValueAnimator createRedTrackAlphaAnimator(int duration, int delay, int... values){
        ValueAnimator redTrackAlphaAnimator = ValueAnimator.ofInt(values);
        redTrackAlphaAnimator.setDuration(duration);
        if(delay > 0){
            redTrackAlphaAnimator.setStartDelay(delay);
        }
        redTrackAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTrackOnAlpha = (int) animation.getAnimatedValue();
            }
        });
        return redTrackAlphaAnimator;
    }

    //4.
    private void initGrayTrackScaleAnimator(){
        //Under open situation, scale the gray track(Track off) drawable
        mScaleBgAnimator = ValueAnimator.ofFloat(1, 0);
        mScaleBgAnimator.setDuration(270);
        mScaleBgAnimator.setInterpolator(new AccelerateInterpolator());
        mScaleBgAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mScaleBg = (float) animation.getAnimatedValue();
            }
        });
    }

    public void resetAllAnim(){
        if (mPositionAnimator != null) {
            mPositionAnimator.removeAllUpdateListeners();
        }

        if(mThumbScaleLargeAnimator != null){
            mThumbScaleLargeAnimator.removeAllUpdateListeners();
        }

        if(mThumbScaleSmallAnimator != null){
            mThumbScaleSmallAnimator.removeAllUpdateListeners();
        }

        if(mGrayThumbAlphaAnimator != null){
            mGrayThumbAlphaAnimator.removeAllUpdateListeners();
        }

        if(mRedThumbAlphaAnimator != null){
            mRedThumbAlphaAnimator.removeAllUpdateListeners();
        }

        if(mScaleBgAnimator != null){
            mScaleBgAnimator.removeAllUpdateListeners();
        }

        if(mTrackOnAlphaAnimator != null){
            mTrackOnAlphaAnimator.removeAllUpdateListeners();
        }
    }

    public void cancel() {
        if(mAnimatorSet != null){
            mAnimatorSet.cancel();
        }
    }

    public boolean isInAnim() {
        return  mIsInAnim;//mAnimatorSet != null && mAnimatorSet.isRunning();
    }

    public int getGrayThumbAlpha() {
        return mGrayThumbAlpha;
    }

    public int getRedThumbAlpha(){
        return mRedThumbAlpha;
    }

    public int getTrackOnAlpha() {
        return mTrackOnAlpha;
    }

    public float getThumbOffScale() {
        return mThumbScale;
    }

    public float getScaleBg() {
        return mScaleBg;
    }

    public void end() {
        if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
            mAnimatorSet.end();
            mAnimatorSet = null;
        }
    }

    interface ThumbPositionListener{
        void updteThumbPosition(float thumbPosition);
    }
}
