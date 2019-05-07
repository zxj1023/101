package tran.com.android.gc.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class AuroraExtraViewContainer extends RelativeLayout {
	private View mView;

	public AuroraExtraViewContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setExtraView(View view) {
		if (mView != null) {
			removeView(mView);
		}
		mView = view;
		if (view != null) {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			mView.setLayoutParams(params);
			addView(mView);
		}
	}

	public View getExtraView() {
		return mView;
	}
}
