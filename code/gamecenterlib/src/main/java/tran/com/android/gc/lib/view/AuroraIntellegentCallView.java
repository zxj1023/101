package tran.com.android.gc.lib.view;

import android.R.mipmap;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import tran.com.android.gc.lib.R;

public class AuroraIntellegentCallView extends RelativeLayout {
	ImageView mImageView;
	AnimationDrawable mAnimationDrawable;
	private static final int MAX_INDEX = 9;

	public AuroraIntellegentCallView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mImageView = new ImageView(context);
		mImageView.setImageResource(R.drawable.aurora_intellegent_call_drag);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		mImageView.setLayoutParams(params);
		addView(mImageView);
	}

	public AuroraIntellegentCallView(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Drawable img = getResources().getDrawable(R.drawable.aurora_intellegent_call_drag);
		int width = img.getIntrinsicWidth();
		int height = img.getIntrinsicHeight();

		setMeasuredDimension(width, height);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void reset() {
		mImageView.setImageResource(R.drawable.aurora_intellegent_call_drag);
	}

	public void setDragProgress(float progress) {
		int index = (int) (progress * 10);
		if (index > MAX_INDEX) {
			index = MAX_INDEX;
		}

		mImageView.setImageLevel(index);
	}

	public void startShowAnimation() {
		mImageView.setImageResource(R.drawable.aurora_intellegent_call_show);
		((AnimationDrawable) mImageView.getDrawable()).start();
	}

	@Override
	public boolean isLayoutRequested() {
		return true;
	}
}
