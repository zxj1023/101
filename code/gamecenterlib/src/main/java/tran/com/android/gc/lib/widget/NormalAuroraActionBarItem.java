package tran.com.android.gc.lib.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import tran.com.android.gc.lib.R;

/**
 * @author leftaven
 * @2013年9月12日
 * @TODO actionbar item style
 */
public class NormalAuroraActionBarItem extends AuroraActionBarItem {

	@Override
	protected View createItemView(int resId) {
		return LayoutInflater.from(mContext).inflate(R.layout.aurora_action_bar_item_base, mActionBar, false);
	}

	@Override
	protected void prepareItemView() {
		super.prepareItemView();
		final ImageButton imageButton = (ImageButton) mItemView.findViewById(R.id.aurora_action_bar_item);
		//imageButton.setBackgroundDrawable(mDrawable);
		imageButton.setImageResource(mDrawableId);
		imageButton.setContentDescription(mContentDescription);
	}

	@Override
	protected void onContentDescriptionChanged() {
		super.onContentDescriptionChanged();
		mItemView.findViewById(R.id.aurora_action_bar_item).setContentDescription(mContentDescription);
	}

	@Override
	protected void onDrawableChanged() {
		super.onDrawableChanged();
		ImageButton imageButton = (ImageButton) mItemView.findViewById(R.id.aurora_action_bar_item);
		//imageButton.setBackgroundDrawable(mDrawable);
		imageButton.setImageResource(mDrawableId);
	}

}
