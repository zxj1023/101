package tran.com.android.gc.lib.view;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import tran.com.android.gc.lib.utils.AuroraLog;
import tran.com.android.gc.lib.utils.DensityUtil;

public class AuroraDialogCustomLayout extends FrameLayout {

	private int mMaxHeight = 220;

	public AuroraDialogCustomLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public AuroraDialogCustomLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public AuroraDialogCustomLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {
		mMaxHeight = DensityUtil.dip2px(getContext(), mMaxHeight);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		// if (heightMode == MeasureSpec.UNSPECIFIED) {
		// return;
		// }

		int height = getMeasuredHeight();
		int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
		AuroraLog.e("alert", "height:" + height);
		AuroraLog.e("alert", "mMaxHeight:" + mMaxHeight);
		if (Configuration.ORIENTATION_LANDSCAPE == getResources()
				.getConfiguration().orientation) {
			if (height > mMaxHeight) {
				View child = getChildAt(0);
				if (child instanceof ViewGroup) {
					ViewGroup.LayoutParams params = child.getLayoutParams();
					params.height = mMaxHeight;
					child.setLayoutParams(params);
					child.requestLayout();
				}
				int specHeightSize = MeasureSpec.getSize(mMaxHeight);
				setMeasuredDimension(specWidthSize, specHeightSize);
			} else {
				setMeasuredDimension(specWidthSize, height);

			}
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

}
