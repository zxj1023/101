
package tran.com.android.gc.lib.widget;

import java.util.Locale;

import android.content.Context;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import tran.com.android.gc.lib.utils.AuroraLog;
import tran.com.android.gc.lib.utils.DensityUtil;
import tran.com.android.gc.lib.utils.EventUtils;

import tran.com.android.gc.lib.R;
import tran.com.android.gc.lib.utils.SystemUtils;


public class AuroraCustomActionBar extends AuroraAbsActionBar implements OnClickListener,OnTouchListener,OnPreDrawListener{

    private static final int AURORA_FULL_TRANSPARENT = 0x00000000;
    
    private static final int AURORA_SEARCH_ICON_MAX_LEVELS = 13;
    
    
    private int mTitleStyle;

    private int mSubTitlestyle;

    private Drawable mBackground;

    private Drawable mIconDrawable;

    private View mParent;

    private Activity mParentActivity;

    private int mTempTouchDownY, mTouchDownY;

    private int mTouchSlop;

    private FrameLayout mBottomSearchBarLayout;

    private LinearLayout mBottomSearchViewParent;

    private RelativeLayout  mBottomSearchIconPanle;

    private ImageButton mBottomSearchShowButton;

    private int mSearchIconX, mSearchIconY;

    private int mMinTouchSpeedY, mMaxTouchSpeedY;

    private int mSearchViewWidth, mSearchViewHeight;

    private int mSearchIconPaddingTop, mSearchIconPaddingRight;
    
    private int mDefaultButtonWidth;

    private ImageButton mSearchView;

    private View mSearchHintTextView;
    private View mSearchHintImageView;
    private RelativeLayout.LayoutParams mSearchHintImageViewParams;

    private FrameLayout.LayoutParams mBottomSearchViewParentParams;

    private FrameLayout.LayoutParams mHintTextParams;
    
    private LinearLayout.LayoutParams mSearchIconParams;

    private LinearLayout.LayoutParams mSearchViewParams;

    private float mSearchViewDefaultLeftMargin;

    private int mSearchBarHeight;
    private int mActionBarWidth;
    private int mActionBarHeightAnimationDistance;

    private int mMoveDetalY;

    private int mScreenWidth, mSearchViewMarginLeft, mSearchViewMarginRight;

    private float mSearchIconDefaultMarginLeft, mSearchHintTextDefalutMarginLeft;

    private float mActionBarIconMarginTop;

    private onSearchViewClickedListener mSearchViewClickedListener;

    private onOptionItemClickListener mOptionItemClickListener;

    private VelocityTracker mVelocityTracker;
    private int mSpeed;

    private ImageButton mDefaultOptionButton;

    private Drawable mBigSearchIcon, mSmallSearchIcon;

    ValueAnimator mSearchIconScaleAnimation;
    
    private boolean mSeachIconClicked;
    private boolean mSearchIconInTop;
    private boolean mDealTitleEvent = true;
    
    private Context mContext;
	
    private int mHeight;
    
    private View mTitlePanelLayout;
    
    private View mActionBar;

    private View mSplitLine;
	
    private Drawable mActionBarBackground;
    
    private OnItemClickListener mItemClickListener;
    
    /*
     * 更新底部搜索条动画的Handler对象
     */
    private Handler mUpdateLayoutHandler;
    
    private float mUpdateLayoutProgress;
    private  int scrollY;
    
    private int mOptionPanelWith;

    private RelativeLayout talpaActionBar;
    
    /**
     * 更新和处理底部搜索条动画的线程，通过Handler机制来实现避免主线程阻塞
     */
    private Runnable mUpdateLayoutThread = new Runnable() {
        @Override
        public void run() {
//        		updateSearchIconAttributes(mUpdateLayoutProgress, scrollY);
            	auroraUpdateHintTextAlpha(mUpdateLayoutProgress);
                auroraUpdateSearchViewBgAlpha(mUpdateLayoutProgress);
                auroraUpdateTopMargins(mUpdateLayoutProgress, scrollY);
//                auroraUpdateSearchViewBgLeft(mUpdateLayoutProgress);
                auroraUpdateSearchIconLeft(mUpdateLayoutProgress);
        }
    };
    private static Typeface mTitleFace;
    private static String localeLanguage;
	
    /**
     * 加载actionbar字体文件
     */
    static {

        
    	auroraCreateTitleFont();

    }
    
    private static Typeface auroraCreateTitleFont( ) {
//    	if ( Locale.getDefault().getLanguage().equals("zh") ) {
//    		mTitleFace = FontUtils.auroraCreateTitleFont(AuroraUtil.ACTION_BAR_TITLE_FONT);
//    	} else {
//    		mTitleFace = FontUtils.auroraCreateTitleFont(AuroraUtil.ACTION_BAR_TITLE_FONT_FORENGLISH);
//    	}
//    	localeLanguage = Locale.getDefault().getLanguage();
    	return mTitleFace;
    }

    /**
     * 设置状态栏title文字
     * @param titleString 文字
     */
    public void setTitleString(CharSequence titleString) {
        mTitleView.setText(titleString);
    }

    /**
     * 设置状态栏文字透明度
     * @param titleAnim 透明度值
     */
    public void setTitleAnim(float titleAnim) {
        mTitleView.setAlpha(titleAnim);
    }

    /**
     * 设置actionbar title 颜色
     * @param titleViewColorBlack 是否是黑色
     */
    public void setTitleViewColor(boolean titleViewColorBlack) {
        if(titleViewColorBlack){
            mTitleView.setTextColor(Color.parseColor("#505050"));
        }else{
            mTitleView.setTextColor(Color.WHITE);
        }
    }
    public TextView getTitleView(){
        return mTitleView;
    }
    /**
     * actionbar上搜索条的点击事件回调接口
     * @author luofu
     *
     */
    public interface onSearchViewClickedListener {
        public void click(View searchView);
    }

    /**
     * 右边默认操作组件的点击回调接口
     * @author luofu
     *
     */
    public interface onOptionItemClickListener {
        public void click(View item);

    }

    public void setOnOptionItemClickListener(onOptionItemClickListener listener) {
        mOptionItemClickListener = listener;
    }

    public void setOnSearchViewClickListener(onSearchViewClickedListener listener) {
        mSearchViewClickedListener = listener;
    }
    
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public AuroraCustomActionBar(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public AuroraCustomActionBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public AuroraCustomActionBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        mContext = context;
        mInflater.inflate(R.layout.aurora_custom_action_bar, this, true);
        mHeight = getContext().getResources()
                .getDimensionPixelSize(R.dimen.aurora_action_bar_height);
        mUpdateLayoutHandler = new Handler();
        initView();
    }

    /**
     * show or hide searchview on action bar
     * @param show  flag to show or hide searchview
     * @param position searchview position on action bar
     */
    public void showSearchView(boolean show,int position){
        if(show){
            mBottomSearchViewParent.setVisibility(View.VISIBLE);
            mBottomSearchBarLayout.setVisibility(View.VISIBLE);
            mBottomSearchIconPanle.setVisibility(View.VISIBLE);
            mBottomSearchShowButton.setVisibility(View.INVISIBLE);
        }else{
            mBottomSearchViewParent.setVisibility(View.GONE);
            mBottomSearchBarLayout.setVisibility(View.GONE);
            mBottomSearchIconPanle.setVisibility(View.GONE);
            mBottomSearchShowButton.setVisibility(View.INVISIBLE);
        }
        calculateContentPosition(show);
    }
    
    private void calculateContentPosition(boolean showSearchView){

    }
    
    /**
     * 以后所有的activity的contentView都会被加载到这里来，这样减少了调用的复杂程度，便于维护，
     * 在AuroraActivity中直接调用该接口来加载内容布局视图
     * @param content
     */
    public void addContentView(View content){
        if(mBottomLayout != null){
            mBottomLayout.addView(content, 0);
        }
    }
    
    /**
     * @see addContentView(View content)
     * @param layoutRes
     */
    public void addContentView(int layoutRes){
        View view = mInflater.inflate(layoutRes, null);
        if(view != null){
            addContentView(view);
        }
    }
    /**
     * bind action bar to activity
     * 
     * @param activity activity need to show action bar
     */
    public void bindToActivity(Activity activity) {
        this.mParentActivity = activity;
    }

    private void initView() {
        mTitleView = (TextView) findViewById(R.id.aurora_custom_action_bar_title);
        mSubTitleView = (TextView) findViewById(R.id.aurora_custom_action_bar_subtitle);

        mTitlePanelLayout = findViewById(R.id.aurora_custom_action_bar_title_panel);
        
        mMiddleLayout = (LinearLayout) findViewById(R.id.aurora_custom_action_bar_middle_panel);
        mOptionLayout = (LinearLayout) findViewById(R.id.aurora_custom_action_bar_option_panel);
        mBottomLayout = (FrameLayout) findViewById(R.id.aurora_custom_action_bar_bottom_widget);

        mParent = findViewById(R.id.aurora_custom_action_bar_body);

        mBottomSearchIconPanle = (RelativeLayout) findViewById(R.id.aurora_custom_action_bar_bottom_search_icon_panel);

        mDefaultOptionButton = (ImageButton) findViewById(R.id.aurora_custom_action_bar_default_option_items);

        mHomeIcon = (ImageButton) findViewById(R.id.aurora_custom_action_bar_title_icon);

        mBottomSearchBarLayout = (FrameLayout) findViewById(R.id.aurora_custom_action_bar_bottom_search_widget);

        mBottomSearchViewParent = (LinearLayout) findViewById(R.id.aurora_custom_action_bar_search_view_parent);

        mBottomSearchShowButton = (ImageButton) findViewById(R.id.aurora_custom_action_bar_search_view_icon);

        mSearchView = (ImageButton) findViewById(R.id.aurora_custom_action_bar_search_view);

        mSearchHintTextView = findViewById(R.id.aurora_custom_action_bar_search_hint_text);
        mSearchHintImageView = findViewById(R.id.aurora_custom_action_bar_search_hint_btn);
        mSearchHintImageViewParams = (RelativeLayout.LayoutParams) mSearchHintImageView.getLayoutParams();
        mActionBar = findViewById(R.id.aurora_custom_action_bar);

        mSplitLine =  findViewById(R.id.split_line);
        
        mSearchViewWidth = mSearchView.getRight() - getLeft();

        mSearchIconX = (int) mBottomSearchShowButton.getX();


        talpaActionBar = (RelativeLayout)findViewById(R.id.talpa_actioin_bar);

//        mBottomSearchShowButton.setAlpha(0.5f);
//        mSearchHintTextView.setAlpha(0.5f);
        
        mSearchViewParams = (LayoutParams) mSearchView.getLayoutParams();
        mBottomSearchViewParentParams = (FrameLayout.LayoutParams) mBottomSearchViewParent
                .getLayoutParams();

        if (mBottomSearchViewParent != null) {
            mBottomSearchViewParentParams = (android.widget.FrameLayout.LayoutParams) mBottomSearchViewParent
                    .getLayoutParams();
        }

        mSearchIconParams = (LinearLayout.LayoutParams) mBottomSearchShowButton.getLayoutParams();



        // mHintTextParams =
        // (FrameLayout.LayoutParams)mSearchHintTextView.getLayoutParams();

        mSearchBarHeight = getContext().getResources().getDimensionPixelSize(
                R.dimen.aurora_searcch_bar_height2);

        //滑动35dp后，不透明度为0
        mActionBarHeightAnimationDistance = getContext().getResources().getDimensionPixelSize(
                R.dimen.aurora_searcch_bar_animation_distance);


        mTouchSlop = ViewConfiguration.getTapTimeout();
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinTouchSpeedY = configuration.getScaledMinimumFlingVelocity();
        mMaxTouchSpeedY = configuration.getScaledMaximumFlingVelocity();

        mScreenWidth = DensityUtil.getDisplayHeight(getContext())[1];
        mScreenWidth = mScreenWidth - mBottomSearchViewParentParams.leftMargin
                - mBottomSearchViewParentParams.rightMargin;
        mSearchViewMarginLeft = mBottomSearchViewParentParams.leftMargin;

        mTitleView.setOnTouchListener(this);
        
        setOnClickListener(mTitleView);
        setOnClickListener(mSubTitleView);
        setOnClickListener(mHomeIcon);

        mBigSearchIcon = getResources().getDrawable(
                R.drawable.header_search_activation);
        mSmallSearchIcon = getResources().getDrawable(
                R.drawable.header_search_icon_small);

        mSearchView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mSearchViewClickedListener != null) {
                    mSearchViewClickedListener.click(v);
                }
            }
        });
        mBottomSearchShowButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mSearchViewClickedListener != null) {
                    mSearchViewClickedListener.click(v);
                }
            }
        });
        mDefaultOptionButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mOptionItemClickListener != null) {
                    mOptionItemClickListener.click(v);
                }
                if(mSearchIconInTop){
                   // Log.e("f", "mSearchIconInTop");
                    postInvalidate();
                    requestLayout();
                }
            }
        });
        
        if (localeLanguage == null || !Locale.getDefault().getLanguage().equals(localeLanguage) ) {
        	auroraCreateTitleFont();
        }
        //mTitleView.setTypeface(mTitleFace);
     
        initMargins();
        showHomeIcon(true);
        ViewTreeObserver vto = mOptionLayout.getViewTreeObserver();
		  vto.addOnPreDrawListener(this);
    }
    
    @Override
    protected void onFinishInflate() {
    	// TODO Auto-generated method stub
    	super.onFinishInflate();
    }
    
    private void initAnimation(){
        
    }
    
    private void readAttr(){
        
    }

    private void initMargins() {
        mSearchViewDefaultLeftMargin = getContext().getResources().getDimension(
                R.dimen.aurora_custom_action_bar_search_view_margin_left);
        mSearchIconDefaultMarginLeft = getContext().getResources().getDimension(
                R.dimen.aurora_custom_action_bar_search_icon_margin_left);

        mActionBarIconMarginTop = getContext().getResources().getDimension(
                R.dimen.aurora_custom_action_bar_icon_margin_top);
        mSearchHintTextDefalutMarginLeft = getContext().getResources().getDimension(
                R.dimen.aurora_custom_action_bar_search_hint_text_margin_left);

        mSearchIconPaddingTop = (int) (getResources()
                .getDimension(R.dimen.aurora_custom_action_bar_search_icon_padding_top));
        mSearchIconPaddingRight = (int) (getResources()
                .getDimension(R.dimen.aurora_custom_action_bar_search_icon_padding_right));

    }
    
    /**
     * show the default option item on right,it will show by default.
     * @param show
     */
    public void showDefualtItem(boolean show){
            mDefaultOptionButton.setVisibility(show?View.VISIBLE:View.GONE);
        }
    
    
    /**
     * add view to option layout
     * @param itemView
     */
    public void addItemView(View itemView){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        if(mOptionLayout != null){
            mOptionLayout.addView(itemView, 1,params);
            
        }
    }

    public void setOptionLayoutMarginRight(int left, int right, int top, int bottom){
        if(mOptionLayout != null){
            int leftPx = 0;
            int rightPx = 0;
            int topPx = 0;
            int bottomPx = 0;
            if(left>0){
                leftPx =  DensityUtil.dip2px(mContext,left);
            }

            if(right>0){
                rightPx = DensityUtil.dip2px(mContext,right);
            }

            if(top>0){
                topPx = DensityUtil.dip2px(mContext,top);
            }

            if(bottom>0){
                bottomPx = DensityUtil.dip2px(mContext,bottom);
            }

            mOptionLayout.setPadding(leftPx, topPx, rightPx, bottomPx);
        }
    }
    
    /**
     * add view to option layout
     * @param layoutRes
     */
    public void addItemView(int layoutRes){
        View view = mInflater.inflate(layoutRes, null);
        if(view != null){
            addItemView(view);
        }
    }

    private void setOnClickListener(View view) {
        view.setOnClickListener(this);
    }

    /**
     * set background for action bar
     */
    public void setBackground(Drawable bg) {
        if (mParent != null && bg != null) {
            mParent.setBackground(bg);
            mActionBarBackground = bg;
        }
      
    }

    /**
     * set background for action bar
     */
    public void setBackground(int bg) {
        mParent.setBackgroundResource(bg);
    }
    
    public Drawable getBackground(){
    	return mActionBarBackground;
    }


    /**
     * set text for title,
     * 
     * @param res text res for title
     */
    public void setTitle(int res, int color) {
    	setTitle(getResources().getString(res), color);
    	
    }

    /**
     * set text for title,
     */
    public void setTitle(CharSequence title, int color) {
        setTitleInternal(title, color);
        showOrHideTitle(mTitleView, mSubTitleView);

    }

    private void setTitleInternal(CharSequence title, int color){
    	int maxTitleLength = (int)getResources().getDimension(R.dimen.aurora_action_bar_title_maxwidth);
	    String titleSuffix = "...";
	    int titleWidth = 0;
	    int index = 0;
		if (mTitleView != null) {
	    if(!TextUtils.isEmpty(title)) {
		    	title = TextUtils.ellipsize(title, mTitleView.getPaint(), maxTitleLength,
	    				TextUtils.TruncateAt.END);
		    }
    		mTitleView.setText(title);
            mTitleView.setTextColor(color);
		}
    }
    
    /**
     * set title textappearance ,such as text size,text color
     * 
     * @param style
     */
    public void setTitleStyle(int style) {
        if (mTitleView != null) {
            mTitleView.setTextAppearance(getContext(), style);
        }
    }

    /*
     * deal with sub title
     */

    /**
     * set text for sub title
     * 
     * @param res text res id for title
     */
    public void setSubTitle(int res) {
        if (mSubTitleView != null) {
            if (!TextUtils.isEmpty(getContext().getText(res))) {
                mSubTitleView.setText(res);
                mSubTitleView.setVisibility(View.VISIBLE);
            } else {
                mSubTitleView.setVisibility(View.GONE);
            }
        }
        showOrHideTitle(mTitleView, mSubTitleView);
    }

    /**
     * set text for sub title
     */
    public void setSubTitle(CharSequence title) {
        if (mSubTitleView != null) {
            if (!TextUtils.isEmpty(title)) {
                mSubTitleView.setText(title);
                mSubTitleView.setVisibility(View.VISIBLE);
            } else {
                mSubTitleView.setVisibility(View.GONE);
            }
        }
        showOrHideTitle(mTitleView, mSubTitleView);

    }

    /**
     * actionbar默认会在右边添加一个操作组件，通过该接口去设置默认组件的图片
     * @param d
     */
    public void setDefaultOptionItemDrawable(Drawable d) {
        if (mDefaultOptionButton != null) {
            mDefaultOptionButton.setImageDrawable(d);
        }
    }
    
    /**
     * 调用该方法来控制是否显示actionbar标题的图标
     * @param show
     */
    public void showHomeIcon(boolean show){
        if(mHomeIcon != null){
            mHomeIcon.setVisibility(show?View.VISIBLE:View.GONE);
        }
        resetTitlePadding(show);
    }
    
    /**
     * reset padding of title when show or hide home icon
     * @param showHomeIcon
     */
    private void resetTitlePadding(boolean showHomeIcon){
        int paddingLeft  = 0;
        if(mTitlePanelLayout != null){
        if(showHomeIcon){
            paddingLeft = 0;
            }else{
                paddingLeft = DensityUtil.dip2px(getContext(), 20);
            }
        mTitlePanelLayout.setPadding(paddingLeft, mTitlePanelLayout.getPaddingTop(),
                mTitlePanelLayout.getPaddingRight(), mTitlePanelLayout.getPaddingBottom());
        }
    }

    /**
     * @see setDefaultOptionItemDrawable(Drawable d)
     * @param res
     */
    public void setDefaultOptionItemDrawable(int res) {
        if (mDefaultOptionButton != null) {
            mDefaultOptionButton.setImageResource(res);
        }
    }

    /**
     * set subTitle textappearance ,such as text size,text color
     * 
     * @param style
     */
    public void setSubTitleStyle(int style) {
        if (mSubTitleView != null) {
            mSubTitleView.setTextAppearance(getContext(), style);
        }
    }

    /**
     * set drawable for home icon view,not background
     * 
     * @param icon
     */
    public void setIcon(Drawable icon) {
        if (mHomeIcon != null) {
            mHomeIcon.setImageDrawable(icon);
        }
    }

    /**
     * set drawable for home icon view,not background
     * 
     * @param res
     */
    public void setIcon(int res) {
        if (mHomeIcon != null) {
            mHomeIcon.setImageResource(res);
        }
    }

    /**
     * 通过该接口来隐藏或者显示actionbar的标题
     * @param title
     * @param subTitle
     */
    private void showOrHideTitle(TextView title, TextView subTitle) {
        if (title != null && subTitle != null) {
            CharSequence t = title.getText();
            CharSequence st = subTitle.getText();
            if (TextUtils.isEmpty(t)) {
                title.setVisibility(View.GONE);
            } else {
                title.setVisibility(View.VISIBLE);
            }

            if (TextUtils.isEmpty(st)) {
                subTitle.setVisibility(View.GONE);
            } else {
                subTitle.setVisibility(View.VISIBLE);
            }
        }
    }
    
    /**
     * Deal with title click event or not
     * @param flag true to deal with event but false not
     */
    public void dealTitleClickEvent(boolean flag){
        mDealTitleEvent = flag;
    }

    public void dealFocus(){
        postInvalidate();
        requestFocus();
    }
    
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int i = v.getId();
        if (i == R.id.aurora_custom_action_bar_title || i == R.id.aurora_custom_action_bar_subtitle || i == R.id.aurora_custom_action_bar_title_icon) {
            if (mDealTitleEvent) {
                if (mHomeIcon != null) {
                    mHomeIcon.setPressed(true);
                }
                if (mParentActivity != null) {
                    mParentActivity.onBackPressed();
                }
            }

        } else {
        }
    }

    /**
     * 调用该接口来播放actionbar底部searchview的动画，需要调用方自己计算动画播放系数（progress）
     * @param progress 范围在0.0~1.0之间的动画播放系数
     */
    private int maxDistance = getResources().getDimensionPixelOffset(
            R.dimen.max_distance);

    public void playSearchPanelAnimation(float progress, int dy) {
//    	if(Math.ceil((double)(progress*100)) == 50){
//    		mActionBar.setBackground(mActionBarBackground);
//    	}
        if(progress == 1 ){
            mSplitLine.setVisibility(VISIBLE);
        }else{
            mSplitLine.setVisibility(GONE);
        }

        updateSearchIconAttributes(dy);

        if(dy > mActionBarHeightAnimationDistance){
            dy = mActionBarHeightAnimationDistance;
        }

        if(scrollY != dy){
            scrollY = dy;
            mUpdateLayoutProgress = (scrollY * 1.0f) / mActionBarHeightAnimationDistance;
            mUpdateLayoutHandler.post(mUpdateLayoutThread);
        }

        changeActionBarTitleColor(progress);
    }

    private void changeActionBarTitleColor(float progress){
        if(progress == 1){
            Drawable arrow = getResources().getDrawable(R.drawable.vector_drawable_search_icon);
            arrow.setAutoMirrored(true);
            mBottomSearchShowButton.setImageDrawable(arrow);
            mTitleView.setTextColor(Color.parseColor("#505050"));
        }else{
            Drawable arrow = getResources().getDrawable(R.drawable.vector_drawable_search_white_icon);
            arrow.setAutoMirrored(true);
            mBottomSearchShowButton.setImageDrawable(arrow);
            mTitleView.setTextColor(Color.WHITE);
        }
    }

    /**
     * 设置底部搜索条提示文字的透明度
     * @param progress
     */
    private void auroraUpdateHintTextAlpha(float progress)
    {

//		float hintTextAlpha = 0.5f - progress * 2;
//		mSearchHintTextView.setAlpha(hintTextAlpha);
//		mSearchHintTextView.setVisibility(hintTextAlpha == 0 ? View.GONE : View.VISIBLE);

        mSearchHintTextView.setAlpha(1-progress);
//        mSearchHintTextView.setScaleX(1-progress);
//        mSearchHintTextView.setScaleY(1-progress);
        mSearchHintTextView.setVisibility((1-progress) == 0 ? View.GONE : View.VISIBLE);

        mSearchHintImageView.setAlpha(1-progress);
//        mSearchHintImageView.setScaleX(1-progress);
//        mSearchHintImageView.setScaleY(1-progress);
        mSearchHintImageView.setVisibility((1-progress) == 0 ? View.GONE : View.VISIBLE);

        int marginRight = (int) ((int) DensityUtil.dip2px(getContext(),34) * progress);
        mSearchHintImageViewParams.rightMargin = marginRight;

        mSearchHintImageView.setLayoutParams(mSearchHintImageViewParams);

	}
    
   /**
    * 设置底部搜索条透明度
    * @param progress
    */
    private void auroraUpdateSearchViewBgAlpha(float progress)
    {
		
//		float searchViewAlpha =1-progress*1.2f;
		
//		mBottomSearchViewParent.setAlpha(searchViewAlpha);
//		mBottomSearchBarLayout.setAlpha(searchViewAlpha);
//        mSearchView.setVisibility(searchViewAlpha == 0 ? View.GONE : View.VISIBLE);
       
	}

    private void updateSearchIconAttributes(int dy){
//        if(progress == 0){
//            mBottomSearchShowButton.setVisibility(View.GONE);
//        }else/* if(Math.floor((double)progress)*100>50)*/{
//            mBottomSearchShowButton.setVisibility(View.VISIBLE);
//            mBottomSearchShowButton.setAlpha(progress);
////            mBottomSearchShowButton.setScaleX(progress);
////            mBottomSearchShowButton.setScaleY(progress);
//        }

        if(dy > mActionBarHeightAnimationDistance){
            if(dy > mSearchBarHeight){
                dy = mSearchBarHeight;
            }
            float dp = (dy - mActionBarHeightAnimationDistance) * 1.0f / (mSearchBarHeight - mActionBarHeightAnimationDistance);
            mBottomSearchShowButton.setVisibility(View.VISIBLE);
            mBottomSearchShowButton.setAlpha(dp);
            if(dp <= 0.5f){
                mBottomSearchShowButton.setScaleX(dp + 0.5f);
                mBottomSearchShowButton.setScaleY(dp + 0.5f);
            }else{
                mBottomSearchShowButton.setScaleX(1.0f);
                mBottomSearchShowButton.setScaleY(1.0f);
            }


            int iconMarginRight = (int) ((int) DensityUtil.dip2px(getContext(), 18) * dp);
            mSearchIconParams.bottomMargin = iconMarginRight;

            mBottomSearchShowButton.setLayoutParams(mSearchIconParams);


        }else{
            mBottomSearchShowButton.setVisibility(View.INVISIBLE);
        }
    }
    
    /**
     * 更新底部搜索条的marginTop值，根据这个值来实现搜索条上下移动的动画
     * @param progress
     */
    private void auroraUpdateTopMargins(float progress, int dy)
    {

//        if(dy > mActionBarHeight){
//            dy = mActionBarHeight;
//        }
//
//        float scale = 1 - (dy * 1.0f / mActionBarHeight);

//        mSearchView.setAlpha(scale);
//        mSearchView.getLayoutParams().width = dy;
//        mSearchView.getLayoutParams().height = dy;
//        mSearchView.requestLayout();

//        mBottomSearchViewParent.setAlpha(scale);
//        mBottomSearchViewParent.getLayoutParams().width = dy;
//        mBottomSearchViewParent.getLayoutParams().height = dy;

//          int X = mBottomSearchBarLayout.getWidth();
         // mBottomSearchViewParent.setAlpha(1-mUpdateLayoutProgress);
//        mBottomSearchBarLayout.setPivotX(X/2);
//        mBottomSearchBarLayout.setPivotY(0);
//        mBottomSearchBarLayout.setScaleX(scale);
//        mBottomSearchBarLayout.setScaleY(scale);



        //滑动35dp后，不透明度为0
        mBottomSearchBarLayout.setAlpha(1-progress);

        //滑动35dp后,大小为90%
        float scale = 1-progress/8;
        mBottomSearchBarLayout.setScaleX(scale);
        mBottomSearchBarLayout.setScaleY(scale);

        if(progress==1){
            mBottomSearchBarLayout.setVisibility(GONE);
        }else{
            mBottomSearchBarLayout.setVisibility(VISIBLE);
        }



//        mBottomSearchBarLayout.getLayoutParams().height = mActionBarHeight - dy;
//        mBottomSearchBarLayout.requestLayout();

//        mBottomSearchBarLayout.setAlpha(scale);
//        mBottomSearchBarLayout.getLayoutParams().width = dy;
//        mBottomSearchBarLayout.getLayoutParams().height = dy;
//        mBottomSearchBarLayout.requestLayout();
		
//		mBottomSearchShowButton.setAlpha(alpha);
		
//        if (topMargin == 0) {
//
//            mSearchIconInTop = true;
            /*
             * 通过在drawable目录下配置一个level-list的xml文件，并调用setImageLevel(int level)
             * 接口来实现不同情况下不同图片的切换
             */
//            mBottomSearchShowButton.setImageLevel(AURORA_SEARCH_ICON_MAX_LEVELS);
            
//            mBottomSearchShowButton.setBackgroundResource(R.drawable.aurora_green_action_bar_normal_item_pressed_selector);
            
//            mSearchView.setVisibility(View.INVISIBLE);
//
//        } else {
//
//            mSearchIconInTop = false;
//
//            mBottomSearchShowButton.setImageLevel((int)(AURORA_SEARCH_ICON_MAX_LEVELS*progress));
            
//            mBottomSearchShowButton.setBackgroundColor(AURORA_FULL_TRANSPARENT);        
            
//            mSearchView.setVisibility(View.VISIBLE);
//
//        }
	}
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    	// TODO Auto-generated method stub
    	super.onLayout(changed, l, t, r, b);
        if(mActionBarWidth == 0){
            mActionBarWidth = mSearchView.getWidth();
        }
    }
    
    
    /**
     * 更新搜索条的marginLeft值，根据这个值来实现搜索条左右移动的动画
     * @param progress
     */
    private void auroraUpdateSearchViewBgLeft(float progress)
    {
		int marginLeft = (int) ((mScreenWidth / 2) * progress);
		 
		marginLeft = (int) Math.min(Math.max(mSearchViewDefaultLeftMargin, marginLeft),
                mScreenWidth / 2 + mScreenWidth / 5);
                
		mSearchViewParams.leftMargin = marginLeft;
		
        mSearchView.setLayoutParams(mSearchViewParams);
	}
    
    /**
     * 更新搜索条放大镜按钮的marginLeft值，根据这个值来实现搜索条放大镜按钮左右移动的动画
     * @param progress
     */
    private void auroraUpdateSearchIconLeft(float progress)
    {
    	
       /* int iconMarginLeft = (int)(auroraGetSearchIconFinalLeftLocation() * progress);
        
        iconMarginLeft = (int) Math.max(mSearchIconDefaultMarginLeft,iconMarginLeft);
        
        mSearchIconParams.leftMargin = iconMarginLeft;
        
        mBottomSearchIconPanle.setLayoutParams(mSearchIconParams);
        */
    	
//    	int iconMarginRight = (int) ((int) DensityUtil.dip2px(getContext(), 48) * progress);
//    	mSearchIconParams.rightMargin = iconMarginRight;
//
//    	mBottomSearchShowButton.setLayoutParams(mSearchIconParams);

	}
    
    private int auroraGetSearchIconFinalLeftLocation() {
    	   if(mOptionPanelWith == 0){
    		   return (int) (mScreenWidth - mBottomSearchShowButton.getWidth()*1.5f);//DensityUtil.dip2px(mContext, 10);
    	   }
     return (mDefaultOptionButton != null)?(mScreenWidth - mOptionPanelWith*2) : 0;
	}

    
    
   /*
    * (non-Javadoc)
    * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
    */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                 if(mHomeIcon != null){
                     mHomeIcon.setPressed(true);
                 }
                break;
            case MotionEvent.ACTION_UP:
                if(mHomeIcon != null){
                    mHomeIcon.setPressed(false);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(mHomeIcon != null){
                    if(EventUtils.isOutOfBounds(getContext(), event, mTitleView)){
                        mHomeIcon.setPressed(false);
                    }else{
                        mHomeIcon.setPressed(true);
                    }
                    
                }
                
                break;

            default:
                break;
        }
        return false;
    }

	@Override
	public boolean onPreDraw() {
		// TODO Auto-generated method stub
		if(mOptionLayout != null){
			if(mOptionPanelWith == 0){
				mOptionPanelWith = mOptionLayout.getMeasuredWidth();
			}
			AuroraLog.e("width", ""+mOptionPanelWith);
		}
		return true;
	}

	public void changeActionBarBg(int res){
        talpaActionBar.setBackgroundResource(res);
    }

    public void changeActionBarBgColor(int color){
        talpaActionBar.setBackgroundColor(color);
    }
}


