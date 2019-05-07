
package tran.com.android.gc.lib.widget;

import android.os.Handler;
import android.util.Log;

import android.widget.Checkable;
import android.widget.TextView;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewDebug;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import tran.com.android.gc.lib.utils.DensityUtil;

import tran.com.android.gc.lib.R;

public class AuroraCheckedTextView extends TextView implements Checkable {

    private boolean mChecked;
    private boolean isSingleChoice = false;
    private int mCheckMarkResource;
    private Drawable mCheckMarkDrawable;
    private int mBasePadding;
    private int mCheckMarkWidth;
    private boolean mNeedRequestlayout;
    private int mCheckMarkPosition = 0;
    private boolean mCheckMarkInLeft = true;
    private int mMarginLeft;
    private int mTextMarginLeft;

    private Drawable auroraSrcDrawable = null;

    private int index = 0;

    private ValueAnimator mValueImagesChecked;
    private ValueAnimator mValueImagesUnChecked;

    private Runnable mRefreshDrawableThread = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            // mCheckMarkDrawable =
            // getResources().getDrawable(mImageIndex[index]);
            setCheckMarkDrawable(getResources().getDrawable(mImageIndex[index]));
        }
    };

    private Handler mHandler = new Handler();// {
    // public void handleMessage(android.os.Message msg) {
    // Log.e("test", "index:"+index);
    // mCheckMarkDrawable = getDrawable(index);
    // postInvalidate();
    // };
    //
    // };

    private int mImageIndex[] = {
            R.drawable.aurora_checkbox0,
            R.drawable.aurora_checkbox1,
            R.drawable.aurora_checkbox2,
            R.drawable.aurora_checkbox3,
            R.drawable.aurora_checkbox4,
            R.drawable.aurora_checkbox5,
            R.drawable.aurora_checkbox6,
            R.drawable.aurora_checkbox7,
            R.drawable.aurora_checkbox8,
            R.drawable.aurora_checkbox9,
            R.drawable.aurora_checkbox10,
            R.drawable.aurora_checkbox11,
            R.drawable.aurora_checkbox12,
            R.drawable.aurora_checkbox13,
            R.drawable.aurora_checkbox14,
            R.drawable.aurora_checkbox15,
            R.drawable.aurora_checkbox16,
            R.drawable.aurora_checkbox17,
            R.drawable.aurora_checkbox18,
            R.drawable.aurora_checkbox19,
            R.drawable.aurora_checkbox20,
            R.drawable.aurora_checkbox21

    };
    private int mMaxIndex = mImageIndex.length;
    private int mDuration = 400;
    private static final int[] CHECKED_STATE_SET = {
            com.android.internal.R.attr.state_checked
    };

    public AuroraCheckedTextView(Context context) {
        this(context, null);
    }

    public AuroraCheckedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.checkedTextViewStyle);
    }

    public AuroraCheckedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs,
                com.android.internal.R.styleable.CheckedTextView, defStyle, 0);

        Drawable d = a.getDrawable(com.android.internal.R.styleable.CheckedTextView_checkMark);
        if (d != null) {
            setCheckMarkDrawable(d);
        }

        boolean checked = a.getBoolean(com.android.internal.R.styleable.CheckedTextView_checked,
                false);
        setChecked(checked);

       
        a.recycle();
        a = context.obtainStyledAttributes(attrs,
                R.styleable.AuroraCheckedTextView, defStyle, 0);
        int position = a.getInt(R.styleable.AuroraCheckedTextView_position, 0);
        boolean singleChoice = a.getBoolean(R.styleable.AuroraCheckedTextView_isSingleChoice,false);
        Log.e("demo", "isSingleChoice:"+singleChoice);
        mCheckMarkPosition = position;
        Log.e("demo", "position:"+position);
        a.recycle();
        setCheckboxRight(mCheckMarkPosition);
        mMarginLeft = DensityUtil.dip2px(context, 33.0f);
        mTextMarginLeft = DensityUtil.dip2px(context, 10.0f);
        auroraSrcDrawable = getResources().getDrawable(
                R.drawable.aurora_btn_checkbox_light);
        if (!singleChoice) {
            setCheckMarkDrawable(mImageIndex[0]);
            initAnimation();
        }

    }

    private void initAnimation() {
        mValueImagesUnChecked = ValueAnimator.ofInt(mImageIndex.length - 1, 0);
        // TODO Auto-generated constructor stub
        mValueImagesUnChecked.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // TODO Auto-generated method stub
                mCheckMarkDrawable = getContext().getResources().getDrawable(
                        (mImageIndex[(Integer) animation.getAnimatedValue()]));
                invalidate();
            }

        });
        mValueImagesUnChecked.setDuration(mDuration);
        mValueImagesUnChecked.addListener(new AnimatorListener() {

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                mValueImagesUnChecked.cancel();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub

            }

        });

        mValueImagesChecked = ValueAnimator.ofInt(0, mImageIndex.length - 1);
        // TODO Auto-generated constructor stub
        mValueImagesChecked.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // TODO Auto-generated method stub
                mCheckMarkDrawable = getContext().getResources().getDrawable(
                        (mImageIndex[(Integer) animation.getAnimatedValue()]));
                invalidate();
            }

        });
        mValueImagesChecked.setDuration(mDuration);
        mValueImagesChecked.addListener(new AnimatorListener() {

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                mValueImagesChecked.cancel();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub

            }

        });
    }

    public void toggle() {
        setChecked(!mChecked);
    }

    @ViewDebug.ExportedProperty
    public boolean isChecked() {
        return mChecked;
    }

    /**
     * <p>
     * Changes the checked state of this text view.
     * </p>
     * 
     * @param checked true to check the text, false to uncheck it
     */
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;

            if (checked) {
                if (mValueImagesChecked != null) {
                    mValueImagesChecked.start();
                }
            } else {
                if (mValueImagesUnChecked != null) {
                    mValueImagesUnChecked.start();
                }

            }
        }
        refreshDrawableState();
        //notifyAccessibilityStateChanged();
        notifyViewAccessibilityStateChangedIfNeeded(
                    AccessibilityEvent.CONTENT_CHANGE_TYPE_UNDEFINED);
    }

    private Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    /**
     * Set the checkmark to a given Drawable, identified by its resourece id.
     * This will be drawn when {@link #isChecked()} is true.
     * 
     * @param resid The Drawable to use for the checkmark.
     * @see #setCheckMarkDrawable(Drawable)
     * @see #getCheckMarkDrawable()
     * @attr ref android.R.styleable#CheckedTextView_checkMark
     */
    public void setCheckMarkDrawable(int resid) {
        if (resid != 0 && resid == mCheckMarkResource) {
            return;
        }

        mCheckMarkResource = resid;

        Drawable d = null;
        if (mCheckMarkResource != 0) {
            d = getResources().getDrawable(mCheckMarkResource);
        }
        setCheckMarkDrawable(d);
    }

    /**
     * Set the checkmark to a given Drawable. This will be drawn when
     * {@link #isChecked()} is true.
     * 
     * @param d The Drawable to use for the checkmark.
     * @see #setCheckMarkDrawable(int)
     * @see #getCheckMarkDrawable()
     * @attr ref android.R.styleable#CheckedTextView_checkMark
     */
    public void setCheckMarkDrawable(Drawable d) {
        if (mCheckMarkDrawable != null) {
            mCheckMarkDrawable.setCallback(null);
            unscheduleDrawable(mCheckMarkDrawable);
        }
        mNeedRequestlayout = (d != mCheckMarkDrawable);
        if (d != null) {
            d.setCallback(this);
            d.setVisible(getVisibility() == VISIBLE, false);
            d.setState(CHECKED_STATE_SET);
            setMinHeight(d.getIntrinsicHeight());

            mCheckMarkWidth = d.getIntrinsicWidth();
            d.setState(getDrawableState());
        } else {
            mCheckMarkWidth = 0;
        }
        mCheckMarkDrawable = d;
        // Do padding resolution. This will call internalSetPadding() and do a
        // requestLayout() if needed.
        resolvePadding();
    }

    /**
     * Gets the checkmark drawable
     * 
     * @return The drawable use to represent the checkmark, if any.
     * @see #setCheckMarkDrawable(Drawable)
     * @see #setCheckMarkDrawable(int)
     * @attr ref android.R.styleable#CheckedTextView_checkMark
     */
    public Drawable getCheckMarkDrawable() {
        return mCheckMarkDrawable;
    }

    /**
     * @hide
     */
    @Override
    protected void internalSetPadding(int left, int top, int right, int bottom) {
        super.internalSetPadding(left, top - 2, right, bottom + 2);
        setBasePadding(mCheckMarkInLeft);
    }

    @Override
    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        updatePadding();
    }

    private void updatePadding() {
        resetPaddingToInitialValues();
        int newPadding = (mCheckMarkDrawable != null) ?
                mCheckMarkWidth + mBasePadding + mMarginLeft + mTextMarginLeft : mBasePadding;
        if (mCheckMarkInLeft) {
            mNeedRequestlayout |= (mPaddingLeft != newPadding);
            mPaddingLeft = newPadding;
        } else {
            mNeedRequestlayout |= (mPaddingRight != newPadding);
            mPaddingRight = newPadding;
        }
        if (mNeedRequestlayout) {
            requestLayout();
            mNeedRequestlayout = false;
        }
    }

    private void setBasePadding(boolean isLayoutRtl) {
        if (isLayoutRtl) {
            mBasePadding = mPaddingLeft;
        } else {
            mBasePadding = mPaddingRight;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final Drawable checkMarkDrawable = mCheckMarkDrawable;
        if (checkMarkDrawable != null) {
            canvas.save();
            final int verticalGravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
            final int height = checkMarkDrawable.getIntrinsicHeight();

            int y = 0;

            switch (verticalGravity) {
                case Gravity.BOTTOM:
                    y = getHeight() - height;
                    break;
                case Gravity.CENTER_VERTICAL:
                    y = (getHeight() - height) / 2;
                    break;
            }

            final int width = getWidth();
            final int top = y;
            final int bottom = top + height;
            final int left;
            final int right;
            if (mCheckMarkInLeft) {
                left = mBasePadding + mMarginLeft;
                right = left + mCheckMarkWidth;
            } else {
                right = width - mBasePadding - mMarginLeft;
                left = right - mCheckMarkWidth;
            }
            checkMarkDrawable.setBounds(left, top, right, bottom);
            checkMarkDrawable.draw(canvas);
            canvas.restore();
        }
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        if (mCheckMarkDrawable != null) {
            int[] myDrawableState = getDrawableState();
            mCheckMarkDrawable.setState(myDrawableState);

            invalidate();
        }
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(AuroraCheckedTextView.class.getName());
        event.setChecked(mChecked);
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(AuroraCheckedTextView.class.getName());
        info.setCheckable(true);
        info.setChecked(mChecked);
    }

    private void setCheckboxRight(int position) {
        if (position == 1) {
            mCheckMarkInLeft = false;
        } else {
            mCheckMarkInLeft = true;
        }
    }

}
