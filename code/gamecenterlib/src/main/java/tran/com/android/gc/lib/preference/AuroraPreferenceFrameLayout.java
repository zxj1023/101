package tran.com.android.gc.lib.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @hide
 */
public class AuroraPreferenceFrameLayout extends FrameLayout {
    private static final int DEFAULT_BORDER_TOP = 0;
    private static final int DEFAULT_BORDER_BOTTOM = 0;
    private static final int DEFAULT_BORDER_LEFT = 0;
    private static final int DEFAULT_BORDER_RIGHT = 0;
    private final int mBorderTop;
    private final int mBorderBottom;
    private final int mBorderLeft;
    private final int mBorderRight;
    private boolean mPaddingApplied;

    public AuroraPreferenceFrameLayout(Context context) {
        this(context, null);
    }

    public AuroraPreferenceFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);//com.android.internal.R.attr.preferenceFrameLayoutStyle);
    }

    public AuroraPreferenceFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        /*TypedArray a = context.obtainStyledAttributes(attrs,
                com.android.internal.R.styleable.PreferenceFrameLayout, defStyle, 0);*/

        float density = context.getResources().getDisplayMetrics().density;
        int defaultBorderTop = (int) (density * DEFAULT_BORDER_TOP + 0.5f);
        int defaultBottomPadding = (int) (density * DEFAULT_BORDER_BOTTOM + 0.5f);
        int defaultLeftPadding = (int) (density * DEFAULT_BORDER_LEFT + 0.5f);
        int defaultRightPadding = (int) (density * DEFAULT_BORDER_RIGHT + 0.5f);

        mBorderTop = 0;/*a.getDimensionPixelSize(
                com.android.internal.R.styleable.PreferenceFrameLayout_borderTop,
                defaultBorderTop);*/
        mBorderBottom = 0;/*a.getDimensionPixelSize(
                com.android.internal.R.styleable.PreferenceFrameLayout_borderBottom,
                defaultBottomPadding);*/
        mBorderLeft = 0;/*a.getDimensionPixelSize(
                com.android.internal.R.styleable.PreferenceFrameLayout_borderLeft,
                defaultLeftPadding);*/
        mBorderRight = 0;/*a.getDimensionPixelSize(
                com.android.internal.R.styleable.PreferenceFrameLayout_borderRight,
                defaultRightPadding);*/

        // a.recycle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    public void addView(View child) {
        int borderTop = getPaddingTop();
        int borderBottom = getPaddingBottom();
        int borderLeft = getPaddingLeft();
        int borderRight = getPaddingRight();

        android.view.ViewGroup.LayoutParams params = child.getLayoutParams();
        LayoutParams layoutParams = params instanceof AuroraPreferenceFrameLayout.LayoutParams
            ? (AuroraPreferenceFrameLayout.LayoutParams) child.getLayoutParams() : null;
        // Check on the id of the child before adding it.
        if (layoutParams != null && layoutParams.removeBorders) {
            if (mPaddingApplied) {
                borderTop -= mBorderTop;
                borderBottom -= mBorderBottom;
                borderLeft -= mBorderLeft;
                borderRight -= mBorderRight;
                mPaddingApplied = false;
            }
        } else {
            // Add the padding to the view group after determining if the
            // padding already exists.
            if (!mPaddingApplied) {
                borderTop += mBorderTop;
                borderBottom += mBorderBottom;
                borderLeft += mBorderLeft;
                borderRight += mBorderRight;
                mPaddingApplied = true;
            }
        }

        int previousTop = getPaddingTop();
        int previousBottom = getPaddingBottom();
        int previousLeft = getPaddingLeft();
        int previousRight = getPaddingRight();
        if (previousTop != borderTop || previousBottom != borderBottom
                || previousLeft != borderLeft || previousRight != borderRight) {
            setPadding(borderLeft, borderTop, borderRight, borderBottom);
        }

        super.addView(child);
    }

    public static class LayoutParams extends FrameLayout.LayoutParams {
        public boolean removeBorders = false;
        /**
         * {@inheritDoc}
         */
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            /*TypedArray a = c.obtainStyledAttributes(attrs,
                    com.android.internal.R.styleable.PreferenceFrameLayout_Layout);
            removeBorders = a.getBoolean(
                    com.android.internal.R.styleable.PreferenceFrameLayout_Layout_layout_removeBorders,
                    false);
            a.recycle();*/
            removeBorders = false;
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }
}