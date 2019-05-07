package tran.com.android.tapla.gamecenter.market.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import tran.com.android.talpa.app_core.log.LogPool;
import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.market.activity.module.AppListActivity;


public class MainTabView extends FrameLayout implements OnClickListener {

	private LinearLayout bg;
    private LinearLayout mainTabViewLayout;
	private MainTabItemView mtiv_new;
//	private MainTabItemView mtiv_special;
	private MainTabItemView mtiv_ranking;
//	private MainTabItemView mtiv_category;

	private MainTabItemView mtiv_play;
	
	private int height;
	private int bgHeight;
	private int maintabMaxHeight;
	private int maintabMinHeight;
	private int mActionBarHeight;
	private int tempHeight;
	private int scrollY;
	private float mProgress;

	private View lineBottom;
	private ViewGroup.LayoutParams   mainTabViewLayoutLayoutParams;
	private ValueAnimator valueAnimator;
	private int Dy;

	public MainTabView(Context context) {
		super(context);
		initView();
	}

	public MainTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	public MainTabView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		mActionBarHeight = getResources().getDimensionPixelSize(
				R.dimen.aurora_action_bar_search_view_height);

		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.view_main_tab, this);

		mainTabViewLayout = (LinearLayout) view.findViewById(R.id.main_tab_view_layout);
		mainTabViewLayoutLayoutParams = mainTabViewLayout.getLayoutParams();

		bg = (LinearLayout) view.findViewById(R.id.bg);
		mtiv_new = (MainTabItemView) view.findViewById(R.id.mtiv_new);
//		mtiv_special = (MainTabItemView) view.findViewById(mtiv_special);
		mtiv_ranking = (MainTabItemView) view.findViewById(R.id.mtiv_ranking);
//		mtiv_category = (MainTabItemView) view.findViewById(mtiv_category);

		mtiv_play = (MainTabItemView) view.findViewById(R.id.mtiv_play);

		lineBottom=  view.findViewById(R.id.line_bottom);


		maintabMaxHeight = getResources().getDimensionPixelOffset(R.dimen.homepage_main_tab_height_max);
		maintabMinHeight = getResources().getDimensionPixelOffset(R.dimen.homepage_main_tab_height_min2);
		tempHeight = getResources().getDimensionPixelOffset(R.dimen.homepage_main_tab_temp_top);
		
		mtiv_new.setOnClickListener(this);
		mtiv_play.setOnClickListener(this);
//		mtiv_special.setOnClickListener(this);
		mtiv_ranking.setOnClickListener(this);
//		mtiv_category.setOnClickListener(this);

		valueAnimator = new ValueAnimator();
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				mainTabViewLayoutLayoutParams.height = (Integer) animator.getAnimatedValue();
				mainTabViewLayout.setLayoutParams(mainTabViewLayoutLayoutParams);
			}
		});
		valueAnimator.setInterpolator(new DecelerateInterpolator());
		valueAnimator.setDuration(400);

		Dy = maintabMaxHeight - maintabMinHeight;
	}

	/**
	 * @Title: setProgress
	 * @Description: 设置动画进度
	 * @param @param progress
	 * @return void
	 * @throws
	 */
	public void setProgress(float progress, int dy) {

		if(scrollY != dy){
			scrollY = dy;
			setTranslationY(-dy);
		}

		if(progress != mProgress){
			mProgress = progress;
			if(progress == 1){
				bg.setAlpha(progress);
				bg.setBackgroundResource(R.drawable.actionbar_touying);
				lineBottom.setVisibility(VISIBLE);
				scrollToTop();
			}else{
				bg.setBackgroundColor(Color.parseColor("#F5F5F5"));
				bg.setAlpha(0);
				lineBottom.setVisibility(GONE);
				scrollToBottom();
			}
		}

		mtiv_play.setFast(0.2f);
		mtiv_new.setFast(0.1f);
		mtiv_ranking.setFast(0.0f);

		mtiv_play.setProgress(dy, 0);
		mtiv_new.setProgress(dy, 1);
		mtiv_ranking.setProgress(dy, 2);
	}

	public void scrollToTop(){
		if (valueAnimator != null) {
			valueAnimator.cancel();

			valueAnimator.setIntValues(maintabMaxHeight,maintabMinHeight);
			valueAnimator.start();
		}
	}

	public void scrollToBottom(){
		if (valueAnimator != null) {
			valueAnimator.cancel();
		}
		mainTabViewLayoutLayoutParams.height = maintabMaxHeight;
		mainTabViewLayout.setLayoutParams(mainTabViewLayoutLayoutParams);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mtiv_new:
			Intent newIntent = new Intent(getContext(),
					AppListActivity.class);
			newIntent.putExtra(AppListActivity.OPEN_TYPE, AppListActivity.TYPE_NEW);
			getContext().startActivity(newIntent);
			break;
		case R.id.mtiv_ranking:
//			Intent rankingIntent = new Intent(getContext(),
//					AppRankingActivity.class);
//			getContext().startActivity(rankingIntent);
			Intent rankingIntent = new Intent(getContext(),
					AppListActivity.class);
			rankingIntent.putExtra(AppListActivity.OPEN_TYPE, AppListActivity.TYPE_RANK);
			getContext().startActivity(rankingIntent);
			break;
		case R.id.mtiv_play:
			Intent playIntent = new Intent(getContext(),
				AppListActivity.class);
			playIntent.putExtra(AppListActivity.OPEN_TYPE, AppListActivity.TYPE_STARTER);
			getContext().startActivity(playIntent);
			break;
//		case mtiv_category:
//			Intent categoryIntent = new Intent(getContext(),
//					CategoryActivity.class);
//			getContext().startActivity(categoryIntent);
//			break;
// 		case mtiv_special:
//			Intent specialIntent = new Intent(getContext(),
//					SpecialActivity.class);
//			getContext().startActivity(specialIntent);
//			break;

		}
	}

}
