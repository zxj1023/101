package tran.com.android.gc.lib.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;

import tran.com.android.gc.lib.R;

public class AuroraAnimationLinearLayout extends LinearLayout {

    private static final String TAG = "AuroraAnimationLinearLayout";

    final float GLOW_MAX_SCALE_FACTOR = 1.2f;
    final float BUTTON_QUIESCENT_ALPHA = 1f;

    long mDownTime;
    int mTouchSlop;
    Drawable mAnimationBG;
    int mAnimationWidth, mAnimationHeight;
    float mAlpha = 0f, mScale = 1f, mDrawingAlpha = 1f;
    boolean mSupportsLongpress = true;
    RectF mRect = new RectF(0f, 0f, 0f, 0f);
    AnimatorSet mPressedAnim;

    ValueAnimator mAlphaAnimator;
    private boolean mPlayAnim = false;
    Runnable mCheckLongPress = new Runnable() {
        public void run() {
            if (isPressed()) {
                sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_LONG_CLICKED);
                performLongClick();
            }
        }
    };

    public AuroraAnimationLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AuroraAnimationLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        mAnimationBG = context.getResources().getDrawable(R.drawable.aurora_action_bar_icon_left_anim);
        if (mAnimationBG != null) {
            setDrawingAlpha(BUTTON_QUIESCENT_ALPHA);
            mAnimationWidth = mAnimationBG.getIntrinsicWidth();
            mAnimationHeight = mAnimationBG.getIntrinsicHeight();

        }

        setClickable(true);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mAnimationBG != null) {
            canvas.save();
            final int w = getWidth();
            final int h = getHeight();
            ViewGroup parent = (ViewGroup) this.getParent();
            if (parent != null) {
                parent.setClipChildren(false);
                parent.setClipToPadding(false);
            }

            final float aspect = (float) mAnimationWidth / mAnimationHeight;
            final int drawW = (int) (h * aspect);
            final int drawH = h;
            final int margin = (drawW - w) / 2;

            //canvas.scale(mScale - 0.4f, mScale - 0.3f, w * 0.5f, h * 0.5f);
            mAnimationBG.setBounds(-margin, 0, drawW - margin, drawH);
            mAnimationBG.setAlpha((int) (mDrawingAlpha * mAlpha * 255));
            mAnimationBG.draw(canvas);
            canvas.restore();
            mRect.right = w;
            mRect.bottom = h;
        }
        super.onDraw(canvas);
    }

    public void playAnim(boolean play) {
        this.mPlayAnim = play;
    }

    public float getDrawingAlpha() {
        if (mAnimationBG == null)
            return 0;
        return mDrawingAlpha;
    }

    public void setDrawingAlpha(float x) {
        if (mAnimationBG == null)
            return;
        // Calling setAlpha(int), which is an ImageView-specific
        // method that's different from setAlpha(float). This sets
        // the alpha on this ImageView's drawable directly
        setAlpha((int) (x * 255));
        mDrawingAlpha = x;
    }

    public float getGlowAlpha() {
        if (mAnimationBG == null)
            return 0;
        return mAlpha;
    }

    public void setGlowAlpha(float x) {
        if (mAnimationBG == null)
            return;
        mAlpha = x;
        invalidate();
    }

    public float getGlowScale() {
        if (mAnimationBG == null)
            return 0;
        return mScale;
    }

    public void setGlowScale(float x) {
        if (mAnimationBG == null)
            return;
        mScale = x;
        final float w = getWidth();
        final float h = getHeight();
        if (GLOW_MAX_SCALE_FACTOR <= 1.0f) {
            // this only works if we know the glow will never leave our bounds
            invalidate();
        } else {
            View parent = (View) getParent();
            if(parent != null){
                parent.invalidate();
            }
        }
    }

    public void setPressed(boolean pressed) {
        
        if (mAnimationBG != null) {
            if (pressed != isPressed()) {
                if (mPressedAnim != null && mPressedAnim.isRunning()) {
                    mPressedAnim.cancel();
                }
                final AnimatorSet as = mPressedAnim = new AnimatorSet();
                if (pressed) {
//                    if (mScale < GLOW_MAX_SCALE_FACTOR)
//                        mScale = GLOW_MAX_SCALE_FACTOR;
                    if (mAlpha < BUTTON_QUIESCENT_ALPHA)
                        mAlpha = BUTTON_QUIESCENT_ALPHA;
                    setDrawingAlpha(1f);
                    as.playTogether(
                            ObjectAnimator.ofFloat(this, "glowAlpha",1f)//,
                           // ObjectAnimator.ofFloat(this, "glowScale", GLOW_MAX_SCALE_FACTOR)
                            );
                    as.setDuration(300);
                } else {
                    as.playTogether(
                            ObjectAnimator.ofFloat(this, "glowAlpha", 0f),
                            //ObjectAnimator.ofFloat(this, "glowScale", 1f),
                            ObjectAnimator.ofFloat(this, "drawingAlpha", BUTTON_QUIESCENT_ALPHA)
                            );
                    as.setDuration(300);
                }
                if(mPlayAnim){
                    as.start();
                }
                
                //mAlphaAnimator.start();
            }
        }
         
        super.setPressed(pressed);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        int x, y;
        Log.e("luofu", "press button");
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownTime = SystemClock.uptimeMillis();
                setPressed(true);
                performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                if (mSupportsLongpress) {
                    removeCallbacks(mCheckLongPress);
                    postDelayed(mCheckLongPress, ViewConfiguration.getLongPressTimeout());
                }

                break;
            case MotionEvent.ACTION_MOVE:
                x = (int) ev.getX();
                y = (int) ev.getY();
                setPressed(x >= -mTouchSlop
                        && x < getWidth() + mTouchSlop
                        && y >= -mTouchSlop
                        && y < getHeight() + mTouchSlop);
                break;
            case MotionEvent.ACTION_CANCEL:
                setBackgroundResource(0);
                setPressed(false);
                if (mSupportsLongpress) {
                    removeCallbacks(mCheckLongPress);
                }
                break;
            case MotionEvent.ACTION_UP:
                final boolean doIt = isPressed();
                setPressed(false);
                // no key code, just a regular ImageView
                if (doIt) {
                    performClick();
                }
                if (mSupportsLongpress) {
                    removeCallbacks(mCheckLongPress);
                }
                break;
        }

        return true;
    }
}
