package tran.com.android.tapla.gamecenter.market.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.common.ColorEvaluator;
import tran.com.android.tapla.gamecenter.common.TextViewInMainTabView;
import tran.com.android.tapla.gamecenter.datauiapi.interf.INotifiableController;

public class MainTabItemView extends FrameLayout {

	private ImageView iv_icon;
	private TextViewInMainTabView tv_text;
	private ImageView background;
	private ImageView img_top;
//	private TextView tv_text_horizontal;

	private int paddingTop = 0;
	private int paddingTopHor = 0;
	private int paddingTopHorText = 0;
	private int marginLeftHorText = 0;
	private int transLeft = 0;

	private float fast = 0.0f;

	int imgSrc ;
    int imgTop;
    int animationStartPoint;
    int scrollY = 0;

	public MainTabItemView(Context context) {
		super(context);
	}

	public MainTabItemView(Context context, AttributeSet attrs) {
		super(context, attrs);

		initView();

		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.main_tab_item);

		imgSrc = mTypedArray.getResourceId(R.styleable.main_tab_item_tab_img, R.drawable.ic_launcher);
		imgTop = mTypedArray.getResourceId(R.styleable.main_tab_item_tab_top, R.drawable.ic_launcher);
		int textSrc = mTypedArray.getResourceId(R.styleable.main_tab_item_tab_text, R.string.app_name);

//		int texthorSrc = mTypedArray.getResourceId(R.styleable.main_tab_item_tab_text_hor, R.string.app_name);
		int bg = mTypedArray.getResourceId(R.styleable.main_tab_item_tab_bg, R.drawable.ic_launcher);

		iv_icon.setImageResource(imgSrc);
		tv_text.setText(textSrc);
//		tv_text_horizontal.setText(texthorSrc);
		background.setImageResource(bg);
		img_top.setImageResource(imgTop);

		mTypedArray.recycle();

        animationStartPoint = getResources().getDimensionPixelSize(
                R.dimen.homepgae_maintabview_animation_start_point);
    }

	private void initView() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.view_main_tab_item, this);

		iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
		tv_text = (TextViewInMainTabView) view.findViewById(R.id.tv_text);
		img_top = (ImageView) view.findViewById(R.id.icon_top);
//		tv_text_horizontal = (TextView) view.findViewById(R.id.tv_text_horizontal);
		background = (ImageView) view.findViewById(R.id.background);

		paddingTop = getResources().getDimensionPixelOffset(R.dimen.homepage_main_tab_item_padding_top); // 原20dp
		paddingTopHor = getResources().getDimensionPixelOffset(R.dimen.homepage_main_tab_item_hor_padding_top); // 原8.3dp
		paddingTopHorText = getResources().getDimensionPixelOffset(R.dimen.homepage_main_tab_item_hor_text_padding_top);
		marginLeftHorText = getResources().getDimensionPixelOffset(R.dimen.homepage_main_tab_item_hor_text_margin_left);
		transLeft = getResources().getDimensionPixelOffset(R.dimen.homepage_main_tab_item_trans_left);
	}

	/**
	 * @Title: setProgress
	 * @Description: 设置动画进度
	 * @param @param progress
	 * @return void
	 * @throws
	 */
	public void setProgress(int dy, int type) {

//		background.setScaleY(1 - progress);
//		background.setScaleX(1 - progress);

        //滑上去的动画触发点
        if(dy > animationStartPoint && scrollY < animationStartPoint){
            scrollY = dy;
//			Animation bg = animationScale(100,1.0f, 0.46f, 1.0f, 1.0f);
//			bg.setAnimationListener(new ScaleAnimationListener(background, 0.46f, 0.0f, 1.0f, 0.0f));
//			background.startAnimation(bg);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)tv_text.getLayoutParams();
			params.bottomMargin = getResources().getDimensionPixelSize(
					R.dimen.main_tab_view_item_bottom1);;
			tv_text.setLayoutParams(params);

			upAnimation();
//			iv_icon.startAnimation(iconAnimationSet());
//			img_top.startAnimation(iconTopAnimationSet());
        }

        //滑下来的动画触发点
        if(dy < animationStartPoint && scrollY > animationStartPoint){
            scrollY = dy;
//			Animation bg = animationScale(100,0.0f, 0.46f, 0.0f, 1.0f);
//			bg.setAnimationListener(new ScaleAnimationListener(background, 0.46f, 1.0f, 1.0f, 1.0f));
//			background.startAnimation(bg);
//			iv_icon.startAnimation(iconAnimationSet2());
//			img_top.startAnimation(iconTopAnimationSet2());
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)tv_text.getLayoutParams();
			params.bottomMargin = getResources().getDimensionPixelSize(
					R.dimen.main_tab_view_item_bottom2);;
			tv_text.setLayoutParams(params);
			downAnimation();
        }


//		float fastProgress = 0;
//		float fastEndValue = 1 - fast;
//		if (progress <= fastEndValue) {
//			fastProgress = progress / fastEndValue;
//		} else {
//			fastProgress = 1;
//		}

//		// 缩放图片(最大缩放为maxScale, 总进度的imgEndValue跑完进度)
//		float imgProgress = 0;
//		float imgEndValue = 0.8f - fast;
//		if (progress <= imgEndValue) {
//			imgProgress = (imgEndValue - progress) / imgEndValue;
//		} else {
//			imgEndValue = 0;
//		}
//		float maxScale = 0.8f;
//		iv_icon.setScaleX((maxScale + imgProgress * (1 - maxScale)));
//		iv_icon.setScaleY((maxScale + imgProgress * (1 - maxScale)));

		// 缩放底部文字
//		tv_text.setScaleX((1 - fastProgress * 0.3f));
//		tv_text.setScaleY((1 - fastProgress * 0.3f));

		// 下移图片、下移底部文字、下移横向文字
//		iv_icon.setTranslationY(progress * (paddingTop + paddingTopHor));
//		tv_text.setTranslationY((progress * (paddingTop + paddingTopHor) * (2 * 1.0f / 3)));
//		tv_text_horizontal.setTranslationY(progress * (paddingTop + paddingTopHor + paddingTopHorText));

//		// 左移图片及底部文字
//		iv_icon.setTranslationX(-(progress * transLeft));
//		tv_text.setTranslationX(-(progress * transLeft));

		// 底部文字透明度(总进度的tvEndValue跑完进度)
//		float tvProgress = 0;
//		float tvEndValue = 0.6f;
//		if (progress <= tvEndValue) {
//			tvProgress = (tvEndValue - fastProgress) / tvEndValue;
//		} else {
//			tvProgress = 0;
//		}
//		tv_text.setAlpha(tvProgress);
//
//		// 右移横向文字
//		float hTxtProgress = 0;
//		float hTxtEndValue = 1 - fast;
//		if (progress <= hTxtEndValue) {
//			hTxtProgress = progress / hTxtEndValue;
//		} else {
//			hTxtProgress = 1;
//		}
////		tv_text_horizontal.setTranslationX(hTxtProgress * marginLeftHorText);
//
//		// 横向文字透明度(从总进度的horStartValue开始跑)
//		float horProgress = 0;
//		float horStartValue = 0.5f;
//		if (fastEndValue >= horStartValue) {
//			horProgress = (fastProgress - horStartValue) / (1 - horStartValue);
//		} else {
//			horProgress = 0;
//		}
//		tv_text_horizontal.setAlpha(horProgress);

	}

	public void setFast(float fast) {
		this.fast = fast;
	}

	public void upAnimation(){
		AnimatorSet animatorSet = new AnimatorSet();//组合动画
		//背景 用时200ms
		background.setPivotY(background.getHeight());
		background.setPivotX(background.getWidth()/2);
		ObjectAnimator backGroundScaleX1 = ObjectAnimator.ofFloat(background, "scaleX", 1f, 0.46f);
		backGroundScaleX1.setInterpolator(new AccelerateDecelerateInterpolator());
		backGroundScaleX1.setDuration(100);

		background.setPivotY(background.getHeight()/2-34);
		background.setPivotX(background.getWidth()/2);
		ObjectAnimator backGroundScaleX2 = ObjectAnimator.ofFloat(background, "scaleX", 0.46f, 0.0f);
		backGroundScaleX2.setInterpolator(new AccelerateInterpolator());
		backGroundScaleX2.setDuration(100);
		backGroundScaleX2.setStartDelay(100);

		ObjectAnimator backGroundScaleY1 = ObjectAnimator.ofFloat(background, "scaleY", 1.0f, 0.0f);
		backGroundScaleY1.setInterpolator(new AccelerateInterpolator());
		backGroundScaleY1.setDuration(100);
		backGroundScaleY1.setStartDelay(100);


		//浅色图片 200ms
		iv_icon.setPivotX(iv_icon.getWidth()/2);
		iv_icon.setPivotY(iv_icon.getHeight()/2);
		ObjectAnimator iconScaleX1 = ObjectAnimator.ofFloat(iv_icon, "scaleX", 1.0f, 0.85f);
		iconScaleX1.setDuration(200);
		iconScaleX1.setInterpolator(new AccelerateDecelerateInterpolator());

		ObjectAnimator iconScaleY1 = ObjectAnimator.ofFloat(iv_icon, "scaleY", 1.0f, 0.85f);
		iconScaleY1.setDuration(200);
		iconScaleY1.setInterpolator(new AccelerateDecelerateInterpolator());

		ObjectAnimator iconAlphaIn = ObjectAnimator.ofFloat(iv_icon, "Alpha", 1.0f, 0.0f);
		iconAlphaIn.setDuration(100);
		iconAlphaIn.setStartDelay(100);
		iconAlphaIn.addListener(iconListener);

		//文字 100ms
		ObjectAnimator textColor = ObjectAnimator.ofObject(tv_text,
				"color", new ColorEvaluator(), "#FFFFFF", "#8D8D8D");
		textColor.setStartDelay(200);
		textColor.setDuration(100);

		//红色图片 560ms
		img_top.setPivotX(img_top.getWidth()/2);
		img_top.setPivotY(img_top.getHeight()/2);
		img_top.setVisibility(VISIBLE);
		ObjectAnimator topImgScaleX1 = ObjectAnimator.ofFloat(img_top, "scaleX", 1.29f, 1.0f);
		topImgScaleX1.setInterpolator(new AccelerateDecelerateInterpolator());
		topImgScaleX1.setDuration(200);
		ObjectAnimator topImgScaleY1 = ObjectAnimator.ofFloat(img_top, "scaleY", 1.29f, 1.0f);
		topImgScaleY1.setInterpolator(new AccelerateDecelerateInterpolator());
		topImgScaleY1.setDuration(200);

		ObjectAnimator topImgScaleX2 = ObjectAnimator.ofFloat(img_top, "scaleX", 1.0f, 1.25f);
		topImgScaleX2.setInterpolator(new AccelerateDecelerateInterpolator());
		topImgScaleX2.setDuration(160);
		topImgScaleX2.setStartDelay(200);
		ObjectAnimator topImgScaleY2 = ObjectAnimator.ofFloat(img_top, "scaleY", 1.0f, 1.25f);
		topImgScaleY2.setInterpolator(new AccelerateDecelerateInterpolator());
		topImgScaleY2.setDuration(160);
		topImgScaleY2.setStartDelay(200);

		ObjectAnimator topImgScaleX3 = ObjectAnimator.ofFloat(img_top, "scaleX", 1.25f, 1.0f);
		topImgScaleX3.setInterpolator(new AccelerateDecelerateInterpolator());
		topImgScaleX3.setDuration(200);
		topImgScaleX3.setStartDelay(360);
		ObjectAnimator topImgScaleY3 = ObjectAnimator.ofFloat(img_top, "scaleY", 1.25f, 1.0f);
		topImgScaleY3.setInterpolator(new AccelerateDecelerateInterpolator());
		topImgScaleY3.setDuration(200);
		topImgScaleY3.setStartDelay(360);

		ObjectAnimator topAlphaIn = ObjectAnimator.ofFloat(img_top, "Alpha", 0.0f, 1.0f);
		topAlphaIn.setStartDelay(100);
		topAlphaIn.setDuration(100);

		animatorSet.play(backGroundScaleX1).with(backGroundScaleX2).with(backGroundScaleY1)
				.with(iconScaleX1).with(iconScaleY1).with(iconAlphaIn)
				.with(textColor)
				.with(topImgScaleX1).with(topImgScaleY1).with(topImgScaleX2).with(topImgScaleY2).with(topImgScaleX3).with(topImgScaleY3).with(topAlphaIn);
		animatorSet.start();

	}

	public void downAnimation(){
		AnimatorSet animatorSet = new AnimatorSet();//组合动画
		//背景 用时200ms
		background.setPivotY(background.getHeight()/2-40);
		background.setPivotX(background.getWidth()/2);
		ObjectAnimator backGroundScaleX1 = ObjectAnimator.ofFloat(background, "scaleX", 0.0f, 0.46f);
		backGroundScaleX1.setInterpolator(new AccelerateDecelerateInterpolator());
		backGroundScaleX1.setDuration(100);

		ObjectAnimator backGroundScaleY1 = ObjectAnimator.ofFloat(background, "scaleY", 0.0f, 1.0f);
		backGroundScaleY1.setInterpolator(new DecelerateInterpolator());
		backGroundScaleY1.setDuration(100);

		background.setPivotY(background.getHeight()/2);
		background.setPivotX(background.getWidth()/2);
		ObjectAnimator backGroundScaleX2 = ObjectAnimator.ofFloat(background, "scaleX", 0.46f, 1.0f);
		backGroundScaleX2.setInterpolator(new DecelerateInterpolator());
		backGroundScaleX2.setDuration(100);
		backGroundScaleX2.setStartDelay(100);

		//浅色图片 200ms
		iv_icon.setPivotX(iv_icon.getWidth()/2);
		iv_icon.setPivotY(iv_icon.getHeight()/2);
		iv_icon.setVisibility(VISIBLE);
		ObjectAnimator iconAlphaIn = ObjectAnimator.ofFloat(iv_icon, "Alpha", 0.0f, 1.0f);
		iconAlphaIn.setDuration(100);

		ObjectAnimator iconScaleX1 = ObjectAnimator.ofFloat(iv_icon, "scaleX", 0.85f, 1.0f);
		iconScaleX1.setDuration(200);
		iconScaleX1.setInterpolator(new DecelerateInterpolator());

		ObjectAnimator iconScaleY1 = ObjectAnimator.ofFloat(iv_icon, "scaleY", 0.85f, 1.0f);
		iconScaleY1.setDuration(200);
		iconScaleY1.setInterpolator(new DecelerateInterpolator());


		//文字 100ms
		ObjectAnimator textColor = ObjectAnimator.ofObject(tv_text,
				"color", new ColorEvaluator(), "#8D8D8D", "#FFFFFF");
		textColor.setStartDelay(100);
		textColor.setDuration(100);

		//红色图片 300ms
		img_top.setPivotX(img_top.getWidth()/2);
		img_top.setPivotY(img_top.getHeight()/2);

		ObjectAnimator topAlphaIn = ObjectAnimator.ofFloat(img_top, "Alpha", 1.0f, 0.0f);
		topAlphaIn.setDuration(100);
		topAlphaIn.addListener(iconListener2);

//		ObjectAnimator topImgScaleX1 = ObjectAnimator.ofFloat(img_top, "scaleX", 1.0f, 1.29f);
//		topImgScaleX1.setInterpolator(new AccelerateDecelerateInterpolator());
//		topImgScaleX1.setDuration(200);
//		topImgScaleX1.setStartDelay(100);
//		ObjectAnimator topImgScaleY1 = ObjectAnimator.ofFloat(img_top, "scaleY", 1.0f, 1.29f);
//		topImgScaleY1.setInterpolator(new AccelerateDecelerateInterpolator());
//		topImgScaleY1.setDuration(200);
//		topImgScaleY1.setStartDelay(100);

//		ObjectAnimator topImgScaleX2 = ObjectAnimator.ofFloat(img_top, "scaleX", 1.25f, 1.0f);
//		topImgScaleX2.setInterpolator(new AccelerateDecelerateInterpolator());
//		topImgScaleX2.setDuration(160);
//		topImgScaleX2.setStartDelay(200);
//		ObjectAnimator topImgScaleY2 = ObjectAnimator.ofFloat(img_top, "scaleY", 1.25f, 1.0f);
//		topImgScaleY2.setInterpolator(new AccelerateDecelerateInterpolator());
//		topImgScaleY2.setDuration(160);
//		topImgScaleY2.setStartDelay(200);
//
//		ObjectAnimator topImgScaleX3 = ObjectAnimator.ofFloat(img_top, "scaleX", 1.0f, 1.25f);
//		topImgScaleX3.setInterpolator(new AccelerateDecelerateInterpolator());
//		topImgScaleX3.setDuration(200);
//		topImgScaleX3.setStartDelay(360);
//		ObjectAnimator topImgScaleY3 = ObjectAnimator.ofFloat(img_top, "scaleY", 1.0f, 1.25f);
//		topImgScaleY3.setInterpolator(new AccelerateDecelerateInterpolator());
//		topImgScaleY3.setDuration(200);
//		topImgScaleX3.setStartDelay(360);


		animatorSet.play(backGroundScaleX1).with(backGroundScaleX2).with(backGroundScaleY1)
				.with(iconScaleX1).with(iconScaleY1).with(iconAlphaIn)
				.with(textColor)
				.with(topAlphaIn);
//				.with(topImgScaleX1).with(topImgScaleY1).with(topAlphaIn);
//				.with(topImgScaleX1).with(topImgScaleY1).with(topImgScaleX2).with(topImgScaleY2).with(topImgScaleX3).with(topImgScaleY3).with(topAlphaIn);
		animatorSet.start();

	}

	private Animator.AnimatorListener iconListener = new Animator.AnimatorListener() {

		@Override
		public void onAnimationStart(Animator animation) {

		}

		@Override
		public void onAnimationEnd(Animator animation) {
			iv_icon.setVisibility(INVISIBLE);
			img_top.setVisibility(VISIBLE);
		}

		@Override
		public void onAnimationCancel(Animator animation) {

		}

		@Override
		public void onAnimationRepeat(Animator animation) {

		}
	};


	private Animator.AnimatorListener iconListener2 = new Animator.AnimatorListener() {

		@Override
		public void onAnimationStart(Animator animation) {

		}

		@Override
		public void onAnimationEnd(Animator animation) {
			iv_icon.setVisibility(VISIBLE);
			img_top.setVisibility(INVISIBLE);
		}

		@Override
		public void onAnimationCancel(Animator animation) {

		}

		@Override
		public void onAnimationRepeat(Animator animation) {

		}
	};


}
