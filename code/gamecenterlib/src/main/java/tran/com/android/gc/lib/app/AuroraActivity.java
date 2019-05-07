package tran.com.android.gc.lib.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import tran.com.android.gc.lib.utils.DensityUtil;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import tran.com.android.gc.lib.widget.AuroraActionBar;
import tran.com.android.gc.lib.widget.AuroraActionBar.OnAuroraActionBarItemClickListener;
import tran.com.android.gc.lib.widget.AuroraActionBar.Type;
import tran.com.android.gc.lib.widget.AuroraActionBarBase;
import tran.com.android.gc.lib.widget.AuroraActionBarHost;
import tran.com.android.gc.lib.widget.AuroraActionBarItem;
import tran.com.android.gc.lib.widget.AuroraActionBottomBarMenuAdapter;
import tran.com.android.gc.lib.widget.AuroraAlphaListener;
import tran.com.android.gc.lib.widget.AuroraCustomActionBar;
import tran.com.android.gc.lib.widget.AuroraCustomMenu;
import tran.com.android.gc.lib.widget.AuroraCustomMenu.CallBack;
import tran.com.android.gc.lib.widget.AuroraCustomMenu.OnMenuItemClickLisener;
import tran.com.android.gc.lib.widget.AuroraMenu;
import tran.com.android.gc.lib.widget.AuroraMenuAdapterBase;
import tran.com.android.gc.lib.widget.AuroraMenuBase;
import tran.com.android.gc.lib.widget.AuroraMenuItem;
import tran.com.android.gc.lib.widget.AuroraSearchView;
import tran.com.android.gc.lib.widget.AuroraSearchView.OnQueryTextListener;
import tran.com.android.gc.lib.widget.AuroraSystemMenu;
import tran.com.android.gc.lib.widget.AuroraSystemMenuAdapter;
import tran.com.android.gc.lib.widget.AuroraUtil;
import tran.com.android.gc.lib.R;

/**
 * @author leftaven
 * @2013年9月12日 for base aurora activity
 */
public class AuroraActivity extends Activity implements AuroraActionBarBase,CallBack {

	private TextView mLeftView;
	private TextView mRightView;

	private View currentView;

	private View mCoverView = null;
	private FrameLayout windowLayout;
	private FrameLayout bg;
	private Animation coverAnimation;

	// ActionBar init start
	/**
	 * mActionBarType mActionBarHost mActionBar
	 */
	private AuroraActionBar.Type mActionBarType;
	private AuroraActionBarHost mActionBarHost;
	private AuroraActionBar mActionBar;
	private OnAuroraActionBarItemClickListener mActionBarListener;
	private List<View> views;
	// ActionBar init end

	// Menu init start
	private AuroraMenu auroraActionBottomBarMenu;
	private AuroraSystemMenu auroraMenu;
	private AuroraCustomMenu auroraCustomMenu;
	private ArrayList<AuroraMenuItem> menuItems;
	private ArrayList<AuroraMenuItem> mAddMenuItems = new ArrayList<AuroraMenuItem>();
    private AuroraMenuAdapterBase auroraCustomMenuAdapter;
	private AuroraMenuAdapterBase auroraMenuAdapter;
	private AuroraMenuBase.OnAuroraMenuItemClickListener auroraMenuCallBack;
	private Boolean menuIsEnable = true;
	private Map<Integer, Integer> menuIds;
	private Map<Integer, Integer> menuCustomIds = new HashMap<Integer, Integer>();
	private AuroraMenuAdapterBase auroraActionBottomBarMenuAdapter;

	// Menu init end

	private AuroraAlphaListener alphaListener;
	private Boolean firstCreateAllOperation=true;
	
	private FrameLayout mSearchviewlayout;
	private LinearLayout mSearchviewBack;
	private LinearLayout searchStateBar;
	private ImageButton cancelBtn;
	private boolean isSearchviewAnimRun = false;
	private boolean isCanClickToHide = false;
	private boolean mClearSearchViewText = true;
	
	// Aurora <Luofu> <2013-11-28> modify for searchView begin
	private AuroraSearchView mSearchView;
	private View mSearchBackgroud;
	private OnSearchViewQueryTextChangeListener mSearchViewQueryTextListener;
	private boolean mNeedSearchView = false;
	
	private View mSearchViewBorder;
	// Aurora <Luofu> <2013-11-28> modify for SearchView end
	
	//aurora add by tangjun start 2014.1.9
	private int lastMenu = 0;
	//aurora add by tangjun end 2014.1.9
	
	//aurora add by tangjun start 2014.5.7
	private LinearLayout cancelBtnLinear;
	private boolean isOutOfBounds = false;
	//aurora add by tangjun end 2014.5.7
	
	
	private Animation mSearchViewUpAnimation;
	private Animation mSearchViewScaleBigAnimation;
	private Animation mSearchViewButtonShowAnimation;
	
	private Animation mSearchViewDownAnimation;
	private Animation mSearchViewScaleSmallAnimation;
	private Animation mSearchViewButtonHideAnimation;
	
	private Drawable mSearchViewAnimStopBackGdDrawable = null;
	private Drawable mSearchViewAnimRunBackGdDrawable = null;
	
	
	AuroraCustomActionBar mAuroraCustomActionBar;
	
	private long mSearchViewAnimDuration = -1;
	
	private boolean mIsNeedShowMenuWhenKeyMenuClick = true;


	private int animationStartPointBg;
	
	private LinearLayout mAuroraCustomActionBarHost;
	private int mCoverViewColor = 0x99000000;

	int maxDistance;
	int startPoint;
	int scrollY;

	public AuroraActivity() {
		this(AuroraActionBar.Type.Normal);// 默认为一般模式，有返回键，有标题
	}

	public AuroraActivity(AuroraActionBar.Type actionBarType) {
		super();
		mActionBarType = actionBarType;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    initSearchViewAnimation();
	    auroraCustomMenu = new AuroraCustomMenu(this,
                 R.style.PopupAnimation,
                R.layout.aurora_menu_layout);
//        initCustomeMenu();
	    auroraCustomMenu.setOnDismissListener(new OnDismissListener() {
            
            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                removeCoverView();
            }
        });
	    auroraCustomMenu.setCallBack(this);
	    
	    new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				judgeIfApkNeedToUpdate( );
			}
		}).start();

		maxDistance = getResources().getDimensionPixelOffset(R.dimen.max_distance);

		startPoint = getResources().getDimensionPixelOffset(R.dimen.bg_aniamtion_start_point);
	}
	
	private void judgeIfApkNeedToUpdate( ) {
		
		if ( getIntent() == null || getIntent().getAction() == null || 
				!getIntent().getAction().equals("android.intent.action.MAIN") ) {
			return;
		}
		
		long aa = System.currentTimeMillis();
		
		Uri updateUri = Uri.parse("content://com.tran.com.android.gc.update.provider.forceupprovider/");
		
		String packageName = this.getPackageName();
		
		Cursor cursor = getContentResolver().query(updateUri, null, null, new String[]{packageName}, null);

		//Log.e("111111", "----cursor = ------" + cursor);
		//Log.e("111111", "----this.getPackageName() = ------" + this.getPackageName());
		
		if( cursor != null ) {
			try {
				if (cursor.moveToNext()) {
					int apkId = cursor.getInt(0);
					Log.e("111111", "----apkId = ------" + apkId);
					Intent intent = new Intent();
					intent.setAction("com.aurora.appupdate.UPDATE");
					intent.putExtra("apkId", apkId);
					startActivity(intent);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				cursor.close();
			}
		}
		
		long bb = System.currentTimeMillis() - aa;
		//Log.e("111111", "--111cost bb = -----" + bb);
		
	}

	   // Aurora <Luofu> <2014-1-7> modify for Add menu begin
    public void addMenu(int menuId,int titleRes,OnMenuItemClickLisener listener){
        auroraCustomMenu.addMenu(menuId, getResources().getString(titleRes), listener);
    }
    public void addMenu(int menuId,CharSequence menuTitle,OnMenuItemClickLisener listener){
        auroraCustomMenu.addMenu(menuId, menuTitle, listener);
    }
    public void addMenu(int menuId,View menuView,OnMenuItemClickLisener listener){
        auroraCustomMenu.addMenu(menuId, menuView,listener);
    }
    
    public void removeMenuById(int menuId){
        auroraCustomMenu.removeMenuById(menuId);
    }
    
    private AuroraMenuItem createMenuItem(int menuId){
        AuroraMenuItem menu = new AuroraMenuItem();
        menu.setId(menuId);
        return menu;
    }
    
    public void hideCustomMenu(){
        if(auroraCustomMenu == null){
            return;
        }
        if(auroraCustomMenu.isShowing()){
            auroraCustomMenu.dismiss();
        }
    }
    
    public void showCustomMenu(){
//        Log.e("mm", "+++++++++++++");
        if (auroraCustomMenu != null) {
            if (auroraCustomMenu.isShowing()) {
                Log.e("mm", "----------------");
                dismissAuroraMenu();
            } else {
                auroraCustomMenu.showAtLocation(getWindow().getDecorView(),
                        Gravity.BOTTOM, 0, 0);
                addCoverView();
                // alphaListener.startMenuShowThread();
            }
        }  
    }
    // Aurora <Luofu> <2014-1-7> modify for Add menu end
	public void setAuroraMagicBarNull() { // add for contact
	}

	public boolean isOptionsMenuExpand() {// add for mms
		return auroraMenu.isShowing() ? true : false;
	}

	private void ensureLayout() {
		if (!verifyLayout()) {
			setContentView(createLayout());
		}
	}

	private boolean verifyLayout() {
		return mActionBarHost != null;
	}

	@Override
	public int createLayout() {// 3种类型的actionbar样式
	    if(mActionBarType == null){
	        return  R.layout.aurora_content_normal;
	    }
		switch (mActionBarType) {
		case Dashboard:
			return  R.layout.aurora_content_dashboard;
		case Empty:
			return R.layout.aurora_content_empty;
		case Custom:
			return  R.layout.aurora_content_custom;
		case Normal:
			return  R.layout.aurora_content_normal;
		default:
			return  R.layout.aurora_content_normal;
		}
	}
	
	/**
	 * 设置搜索框的背景 tangjun 2014.06.19
	 */
	public void setSearchviewBarBackgroundResource(int stopResid, int runResid) {
		mSearchViewAnimRunBackGdDrawable = this.getResources().getDrawable(runResid);
		mSearchViewAnimStopBackGdDrawable = this.getResources().getDrawable(stopResid);
	}
	
	/**
	 * 设置搜索框的背景 tangjun 2014.06.19
	 */
	public void setSearchviewBarBackgroundDrawable(Drawable stopDrawable, Drawable runDrawable) {
		mSearchViewAnimRunBackGdDrawable = runDrawable;
		mSearchViewAnimStopBackGdDrawable = stopDrawable;
	}
	
	/**
	 * addSearchview tangjun 2013.11.28
	 */
	private void addSearchview( ) {
		//long aa = System.currentTimeMillis();
		mSearchviewlayout = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.aurora_searchview_activity, null);
		//long bb = System.currentTimeMillis();
		//Log.e("222222", "addSearchview --1 = " + String.valueOf(bb - aa) );
		windowLayout.addView(mSearchviewlayout);
		//long cc = System.currentTimeMillis();
		//Log.e("222222", "addSearchview --1 = " + String.valueOf(cc - bb) );
		cancelBtn = (ImageButton) findViewById( R.id.aurora_activity_searchviewcancelbtn);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ( mSearchViewButtonClickListener != null ) {
					mSearchViewButtonClickListener.onSearchViewButtonClick();
				} else {
					hideSearchviewLayout();
				}
			}
		});
		mSearchviewBack = (LinearLayout)findViewById(R.id.aurora_activity_searchviewbar);
		searchStateBar  = (LinearLayout) this.findViewById(R.id.search_top_bar);
		cancelBtnLinear = (LinearLayout)findViewById(R.id.cancelbuttonlinear);
		//cancelBtnLinear.setBackgroundColor(Color.RED);
		cancelBtnLinear.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch ( event.getAction() ){
				case MotionEvent.ACTION_DOWN:
					cancelBtn.setPressed(true);
					break;
				case MotionEvent.ACTION_UP:
					if(!isOutOfBounds){
						if ( mSearchViewButtonClickListener != null ) {
							mSearchViewButtonClickListener.onSearchViewButtonClick();
						} else {
							hideSearchviewLayout();
						}
					}
					cancelBtn.setPressed(false);
					isOutOfBounds = false;
					break;
				case MotionEvent.ACTION_MOVE:
					if(isOutOfBounds(AuroraActivity.this, event, cancelBtnLinear)){
						isOutOfBounds = true;
						cancelBtn.setPressed(false);
					}
					break;
				default:
					break;
				}
				return true;
			}
		});
	}
	
	
	
	/**
	 * 设置actionbar view tangjun 2013.11.28
	 * 
	 * @param resID
	 */
	public void setAuroraContentView(int resID,
			AuroraActionBar.Type actionBarType) {
		setAuroraContentView(resID, actionBarType, false);
	}
	
	/**
	 * 设置actionbar view tangjun 2013.11.28
	 * 
	 * @param resID
	 */
	public void setAuroraContentView(int resID,
			AuroraActionBar.Type actionBarType, boolean needSearchview) {
	    if(actionBarType == Type.NEW_COSTOM){
	        mActionBarType = actionBarType;
	        setContentView(R.layout.aurora_activity_content_layout);
//	        FrameLayout content = (FrameLayout)findViewById(R.id.aurora_custom_content_layout);
//	        if(content != null){
//	            if ( content.getChildCount() > 0 ) {
//	                content.removeAllViews();
//	            }
//	            View customContent = LayoutInflater.from(this).inflate(resID, content,true);
	            mAuroraCustomActionBar = (AuroraCustomActionBar)findViewById(R.id.aurora_custom_action_bar_layout);
	            if(mAuroraCustomActionBar != null){
	                mAuroraCustomActionBar.bindToActivity(this);
	                mAuroraCustomActionBar.showSearchView(false, 0);
	                mAuroraCustomActionBar.addContentView(resID);
	            }
	           
//	        }
	        windowLayout = (FrameLayout) findViewById(R.id.windowLayout);
			bg = (FrameLayout) findViewById(R.id.bg);
	        mAuroraCustomActionBarHost = (LinearLayout)findViewById(R.id.aurora_action_bar_host);
	    }else{
    		mActionBarType = actionBarType;
    		ensureLayout();
    		onPreContentChanged();
    		FrameLayout contentView = getContentView();// 获得内容区域，前端设置界面
    		if ( contentView.getChildCount() > 0 ) {
    			contentView.removeAllViews();
    		}
    		currentView = LayoutInflater.from(this).inflate(resID, contentView);
    		setContentView(windowLayout);
	    }
		mNeedSearchView = needSearchview;
		if (needSearchview) {
			addSearchview();
			
			initSearchView();
		}
	}
	public AuroraCustomActionBar getCustomActionBar(){
        return mAuroraCustomActionBar;
    }
	/**
	 * 设置actionbar 为图库设定单独一种actionbar布局 tangjun 2013.12.26
	 * 
	 * @param resID
	 */
	public void setAuroraPicContentView(int resID) {
		mActionBarType = AuroraActionBar.Type.Normal;
		setContentView(R.layout.aurora_content_normal_pic);
		onPreContentChanged();
		FrameLayout contentView = getContentView();// 获得内容区域，前端设置界面
		if ( contentView.getChildCount() > 0 ) {
			contentView.removeAllViews();
		}
		currentView = LayoutInflater.from(this).inflate(resID, contentView);
		//setContentView(windowLayout);
	}

	// Aurora <Luofu> <2013-11-28> modify for SearchView begin
	
	public void addSearchviewInwindowLayout( ) {
		mNeedSearchView = true;
		addSearchview();
		initSearchView();
	}
	
	public void setOnQueryTextListener(OnSearchViewQueryTextChangeListener listener){
	    this.mSearchViewQueryTextListener = listener;
	}
	
	public AuroraSearchView getSearchView(){
	    return mSearchView;
	}
	
	public View getSearchViewGreyBackground(){
	    return mSearchBackgroud;
	}

	public LinearLayout getSearchStateBar(){
		return searchStateBar;
	}
	
	public View getWindowLayout ( ) {
		return windowLayout;
	}
	
	public void initSearchView(){
	    mSearchView = (AuroraSearchView)findViewById(R.id.aurora_activity_searchviewwidget);
	    mSearchBackgroud = findViewById(R.id.aurora_activity_searchview_background);
	    mSearchViewBorder = mSearchView.getBackgroundView();
//	    mSearchView.setKeyBackListener(new OnKeyBackListener() {
//            
//            @Override
//            public void pressed() {
//                // TODO Auto-generated method stub
//                if(mSearchviewlayout != null){
//                    if(mSearchviewlayout.getVisibility() == View.VISIBLE){
//                        hideSearchviewLayout();
//                    }
//                }
//            }
//        });
	    if(mSearchBackgroud != null){
	        mSearchBackgroud.setOnClickListener(new OnClickListener() {

	        	@Override
	        	public void onClick(View v) {
//	        		hideSearchviewLayout( );
//	        		if ( searchBackgroundClickListener != null ) {
//	        			searchBackgroundClickListener.searchBackgroundClick();
//	        		}
					return;
	        	}
	        });
	    }
	    mSearchView.setOnQueryTextListener(new OnQueryTextListener() {
            
            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub
                if(mSearchViewQueryTextListener != null){
                	if ( !TextUtils.isEmpty(query) ) {
                		mSearchBackgroud.setVisibility(View.GONE);
                	} else {
                		mSearchBackgroud.setVisibility(View.VISIBLE);
                	}
                   return mSearchViewQueryTextListener.onQueryTextSubmit(query);
                }
                return false;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub
                if(mSearchViewQueryTextListener != null){
                	if ( !TextUtils.isEmpty(newText) ) {
                		mSearchBackgroud.setVisibility(View.GONE);
                	} else {
                		mSearchBackgroud.setVisibility(View.VISIBLE);
                	}
                    return mSearchViewQueryTextListener.onQueryTextChange(newText);
                 }
                return false;
            }
        });
	}
	
	// Aurora <Luofu> <2013-11-28> modify for SearchView end

	public View getCurrentView() {
		return currentView;
	}

	public void setAuroraContentView(int resID) {
		setAuroraContentView(resID, AuroraActionBar.Type.Normal);
	}

	public void onPreContentChanged() {
		windowLayout = (FrameLayout) findViewById(R.id.windowLayout);
		mActionBarHost = (AuroraActionBarHost) findViewById(R.id.aurora_action_bar_host);
		if (mActionBarHost == null) {
			throw new RuntimeException(
					"Your content must have an ActionBarHost whose id attribute is R.id.aurora_action_bar_host");
		}
		mActionBar = mActionBarHost.getActionBar();
		mActionBarHost.getActionBar().setOnAuroraActionBarListener(
				mActionBarListener);
		// windowLayout.setBackgroundDrawable(getWallpaper());
	}

	@Override
	public FrameLayout getContentView() {// 获取内容区域
		
		if(mActionBarType == Type.NEW_COSTOM){
		    FrameLayout contentView = (FrameLayout)findViewById(R.id.aurora_custom_action_bar_bottom_widget);
		    return contentView;
		}
		ensureLayout();
		return mActionBarHost.getContentView();
	}

	@Override
	public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
		super.onCreate(savedInstanceState, persistentState);
	}

	public AuroraActionBar getAuroraActionBar() {// 获得actionbar
		ensureLayout();
		if (mActionBar == null) {
		    if(mActionBarHost == null){
		      mActionBarHost = (AuroraActionBarHost) findViewById(R.id.aurora_action_bar_host);
		    }
		    mActionBar = mActionBarHost.getActionBar();
		}
		return mActionBar;
	}
	
	public ImageButton getSearchViewRightButton(){
	    return cancelBtn;
	}
	
	private void initSearchViewAnimation(){
	    
	   mSearchViewUpAnimation =AnimationUtils.loadAnimation(this, R.anim.aurora_activity_searchviewlayout_enter);
	   mSearchViewScaleBigAnimation = AnimationUtils.loadAnimation(this, R.anim.aurora_activity_searchviewwidget_enter);
	   mSearchViewButtonShowAnimation = AnimationUtils.loadAnimation(this, R.anim.aurora_activity_searchviewbutton_enter);
	    
	   mSearchViewDownAnimation = AnimationUtils.loadAnimation(this, R.anim.aurora_activity_searchviewlayout_exit);
	   mSearchViewScaleSmallAnimation = AnimationUtils.loadAnimation(this, R.anim.aurora_activity_searchviewwidget_exit);
	   mSearchViewButtonHideAnimation =AnimationUtils.loadAnimation(this, R.anim.aurora_activity_searchviewbutton_exit);
	}
	
	public void setSearchViewAnimDuration( long searchViewAnimDuration) {
		mSearchViewAnimDuration = searchViewAnimDuration;
	}
	
	public void showSearchviewLayoutWithOnlyAlphaAnim( ) {
		if(!mNeedSearchView){
	        return;
	    }
		
		if ( !isSearchviewAnimRun ) {

			isCanClickToHide = true;
			
			Animation searchviewlayoutAlphaAnim = AnimationUtils.loadAnimation(this, R.anim.aurora_activity_searchviewonlyalpha_enter);
			
			mSearchviewlayout.setVisibility(View.VISIBLE);
			mSearchBackgroud.setVisibility(View.VISIBLE);
			
			searchviewlayoutAlphaAnim.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					
					mSearchView.getFocus();
					
					isSearchviewAnimRun = false;
					mSearchviewlayout.clearAnimation();
				}
			});

			mSearchviewlayout.startAnimation(searchviewlayoutAlphaAnim);
			isSearchviewAnimRun = true;
			//mSearchviewBack.setBackgroundResource(R.drawable.talpa_actionbar_background); //chenjie 注释掉 2017-09-20
//			if ( mSearchViewAnimStopBackGdDrawable != null ) {
//				mSearchviewBack.setBackground(mSearchViewAnimStopBackGdDrawable);
//			} else {
//				mSearchviewBack.setBackgroundResource(R.drawable.talpa_actionbar_background);
//			}
		}
	}
	
	public void hideSearchViewLayoutWithOnlyAlphaAnim( ) {
		
        if(!mNeedSearchView){
            return;
        }
        
        if ( !isSearchviewAnimRun && isCanClickToHide ) {

            isCanClickToHide = false;
			
            if(mClearSearchViewText){
                mSearchView.clearText();
            }
            
			Animation searchviewlayoutAlphaAnim = AnimationUtils.loadAnimation(this, R.anim.aurora_activity_searchviewonlyalpha_exit);

			searchviewlayoutAlphaAnim.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					mSearchView.clearEditFocus();
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					mSearchviewlayout.setVisibility(View.GONE);
                    mSearchBackgroud.setVisibility(View.GONE);

                    mSearchviewlayout.clearAnimation();
                    isSearchviewAnimRun = false;

                    if(quitListener != null){
                        quitListener.quit();
                    }
				}
			});
            
            mSearchviewlayout.startAnimation(searchviewlayoutAlphaAnim);
            isSearchviewAnimRun = true;
            
//			if ( mSearchViewAnimStopBackGdDrawable != null ) {
//				mSearchviewBack.setBackground(mSearchViewAnimStopBackGdDrawable);
//			} else {
//				mSearchviewBack.setBackgroundResource(R.drawable.talpa_actionbar_background);
//			}
			//mSearchviewBack.setBackgroundResource(R.drawable.talpa_actionbar_background); //chenjie 注释掉 2017-09-20
        }
	}
	
	public void setSearchViewTextSize(float size){
		if(mSearchView != null){
			mSearchView.setTextSize(size);
		}
	}
	
	/**
	 * showSearchviewLayout tangjun 2013.11.28
	 */
	public void showSearchviewLayout( ) {
		
	    if(!mNeedSearchView){
	        return;
	    }
		
		if ( !isSearchviewAnimRun ) {

			isCanClickToHide = true;
//			if ( mSearchViewAnimRunBackGdDrawable != null ) {
//				mSearchviewBack.setBackground(mSearchViewAnimRunBackGdDrawable);
//			} else {
//				mSearchviewBack.setBackgroundResource(R.drawable.talpa_actionbar_background);
//			}
			//mSearchviewBack.setBackgroundResource(R.drawable.talpa_actionbar_background); //chenjie 注释掉 2017-09-20
			float actionbarHeight = DensityUtil.dip2px(AuroraActivity.this, 56);
			Log.e("111111", "---actionbarHeight = ----" + actionbarHeight);
			//Animation mActionBarHostAnimation = AnimationUtils.loadAnimation(this, R.anim.aurora_activity_searchviewhost_enter);
			Animation mActionBarHostAnimation = new TranslateAnimation(0, 0, actionbarHeight, 0);
			mActionBarHostAnimation.setFillAfter(true);
			mActionBarHostAnimation.setDuration(250);
			mActionBarHostAnimation.setInterpolator(new DecelerateInterpolator());
			//对actionbar单独跑动画是因为在U3上面对mActionBarHost先设置TranslationY再跑动画会有问题(U2无此问题)
			Animation mActionBarAnimation = AnimationUtils.loadAnimation(this, R.anim.aurora_activity_searchviewactionbar_enter);
			if ( mSearchViewAnimDuration != -1 ) {
				mActionBarHostAnimation.setDuration(mSearchViewAnimDuration);
				mActionBarAnimation.setDuration(mSearchViewAnimDuration);
			}
			if(mActionBarType == Type.NEW_COSTOM){
				mAuroraCustomActionBarHost.setTranslationY(-actionbarHeight);
				mAuroraCustomActionBarHost.startAnimation(mActionBarHostAnimation);
				mAuroraCustomActionBar.startAnimation(mActionBarAnimation);
			}else{
				mActionBarHost.setTranslationY(-actionbarHeight);
				mActionBarHost.startAnimation(mActionBarHostAnimation);
				mActionBar.startAnimation(mActionBarAnimation);
			}

			mSearchviewlayout.setVisibility(View.VISIBLE);
			mSearchBackgroud.setVisibility(View.VISIBLE);
			mSearchView.getFocus();
			
			mActionBarHostAnimation.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					isSearchviewAnimRun = false;
					cancelBtn.clearAnimation();
					mSearchViewBorder.clearAnimation();
					mSearchviewlayout.clearAnimation();
					
//					if ( mSearchViewAnimStopBackGdDrawable != null ) {
//						mSearchviewBack.setBackground(mSearchViewAnimStopBackGdDrawable);
//					} else {
//						mSearchviewBack.setBackgroundResource(R.drawable.aurora_activity_searchbar_bg);
//					}
					//mSearchviewBack.setBackgroundResource(R.drawable.talpa_actionbar_background); //chenjie 注释掉 2017-09-20
					if(mActionBarType == Type.NEW_COSTOM){
			            mAuroraCustomActionBarHost.clearAnimation();
			            mAuroraCustomActionBar.clearAnimation();
			        }else{
			            mActionBarHost.clearAnimation();
			            mActionBar.clearAnimation();
			        }
				}
			});
		    
			cancelBtn.startAnimation(mSearchViewButtonShowAnimation);
			mSearchViewBorder.startAnimation(mSearchViewScaleBigAnimation);
			mSearchviewlayout.startAnimation(mSearchViewUpAnimation);
			if ( mSearchViewAnimDuration != -1 ) {
				mSearchViewUpAnimation.setDuration(mSearchViewAnimDuration);
				mSearchViewButtonShowAnimation.setDuration(mSearchViewAnimDuration);
				mSearchViewScaleBigAnimation.setDuration(mSearchViewAnimDuration);
			}
			isSearchviewAnimRun = true;
		}
	}
	
	/**
	 * showSearchviewLayout tangjun 2013.11.28
	 */
	public void showSearchviewLayout_Ex( ) {
		
	    if(!mNeedSearchView){
	        return;
	    }
		
		if ( !isSearchviewAnimRun ) {

			isCanClickToHide = true;
//			if ( mSearchViewAnimRunBackGdDrawable != null ) {
//				mSearchviewBack.setBackground(mSearchViewAnimRunBackGdDrawable);
//			} else {
//				mSearchviewBack.setBackgroundResource(R.drawable.aurora_activity_searchbar_bg2);
//			}
			//mSearchviewBack.setBackgroundResource(R.drawable.talpa_actionbar_background); //chenjie 注释掉 2017-09-20
			/*
			AuroraTranslateAnimation anim2 = new AuroraTranslateAnimation(mActionBarHost, -AuroraUtil.ACTION_BAR_HEIGHT_PX, 0);
		    anim2.setDuration(300);
		    anim2.setInterpolator(this, android.R.anim.accelerate_interpolator);
		    mActionBarHost.startAnimation(anim2);
		    */
			 
            float actionbarHeight = DensityUtil.dip2px(this, 42);
//            Log.e("222222", "--actionbarHeight = ---" + actionbarHeight);
            ObjectAnimator translateOut = null;
            if(mActionBarType == Type.NEW_COSTOM){
//                Log.e("new", "********");
                translateOut = ObjectAnimator.ofFloat(mAuroraCustomActionBarHost, "TranslationY", 0f,-actionbarHeight);
            }else{
                translateOut = ObjectAnimator.ofFloat(mActionBarHost, "TranslationY", 0f,-actionbarHeight);
            }
			 
			if ( mSearchViewAnimDuration != -1 ) {
				translateOut.setDuration(mSearchViewAnimDuration);
			} else {
				translateOut.setDuration(250);
			}
			translateOut.setInterpolator(new AccelerateInterpolator());
			translateOut.addListener(new AnimatorListener() {
				
				@Override
				public void onAnimationStart(Animator animation) {
					// TODO Auto-generated method stub
					mSearchviewlayout.setVisibility(View.VISIBLE);
					mSearchBackgroud.setVisibility(View.VISIBLE);
					
					mSearchView.getFocus();
				}
				
				@Override
				public void onAnimationRepeat(Animator animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animator animation) {
					// TODO Auto-generated method stub
					isSearchviewAnimRun = false;
					//mActionBarHost.clearAnimation();
					cancelBtn.clearAnimation();
					mSearchViewBorder.clearAnimation();
					mSearchviewlayout.clearAnimation();
					
//					if ( mSearchViewAnimStopBackGdDrawable != null ) {
//						mSearchviewBack.setBackground(mSearchViewAnimStopBackGdDrawable);
//					} else {
//						mSearchviewBack.setBackgroundResource(R.drawable.talpa_actionbar_background);
//					}
					// mSearchviewBack.setBackgroundResource(R.drawable.talpa_actionbar_background); //chenjie 注释掉 2017-09-20
				}
				
				@Override
				public void onAnimationCancel(Animator animation) {
					// TODO Auto-generated method stub
					
				}
			});
			translateOut.start();
		    
			cancelBtn.startAnimation(mSearchViewButtonShowAnimation);
			mSearchViewBorder.startAnimation(mSearchViewScaleBigAnimation);
			mSearchviewlayout.startAnimation(mSearchViewUpAnimation);
			if ( mSearchViewAnimDuration != -1 ) {
				mSearchViewUpAnimation.setDuration(mSearchViewAnimDuration);
				mSearchViewButtonShowAnimation.setDuration(mSearchViewAnimDuration);
				mSearchViewScaleBigAnimation.setDuration(mSearchViewAnimDuration);
			}
			isSearchviewAnimRun = true;
			
			/*
			anim2.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					
					mSearchviewlayout.setVisibility(View.VISIBLE);
					mSearchBackgroud.setVisibility(View.VISIBLE);
					
					mSearchView.getFocus();
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					
					isSearchviewAnimRun = false;
					mActionBarHost.clearAnimation();
					cancelBtn.clearAnimation();
					mSearchViewBorder.clearAnimation();
					//mSearchviewlayout.clearAnimation();
					
//					FrameLayout.LayoutParams layoutParamsOfMoveView = (FrameLayout.LayoutParams) mActionBarHost.getLayoutParams();
//					layoutParamsOfMoveView.topMargin = layoutParamsOfMoveView.topMargin - AuroraUtil.ACTION_BAR_HEIGHT_PX;
//					mActionBarHost.setLayoutParams(layoutParamsOfMoveView);
//					mActionBarHost.requestLayout();
					
					//mSearchView.getFocus();
					
					mSearchviewBack.setBackgroundResource(R.drawable.aurora_activity_searchbar_bg);
				}
			});
			*/
		}
	}
	
	public void showSearchviewLayoutWithNoAnim( ) {
		
	    if(!mNeedSearchView){
	        return;
	    }
		
		float actionbarHeight = DensityUtil.dip2px(this, 42);
        
        if(mActionBarType == Type.NEW_COSTOM){
            mAuroraCustomActionBarHost.setTranslationY(-actionbarHeight);
        }else{
            mActionBarHost.setTranslationY(-actionbarHeight);
        }
        
		mSearchviewlayout.setVisibility(View.VISIBLE);
		mSearchBackgroud.setVisibility(View.VISIBLE);
		
		mSearchView.getFocus();
		
		isSearchviewAnimRun = false;
		cancelBtn.clearAnimation();
		mSearchViewBorder.clearAnimation();
		mSearchviewlayout.clearAnimation();
		
//		if ( mSearchViewAnimStopBackGdDrawable != null ) {
//			mSearchviewBack.setBackground(mSearchViewAnimStopBackGdDrawable);
//		} else {
//			mSearchviewBack.setBackgroundResource(R.drawable.aurora_activity_searchbar_bg);
//		}
		//mSearchviewBack.setBackgroundResource(R.drawable.talpa_actionbar_background); //chenjie 注释掉 2017-09-20
	}
	
	public void hideSearchViewLayoutWithNoAnim ( ) {
		
	    if(!mNeedSearchView){
	        return;
	    }
		
		float actionbarHeight = DensityUtil.dip2px(this, 42);
        
        if(mActionBarType == Type.NEW_COSTOM){
            mAuroraCustomActionBarHost.setTranslationY(0);
        }else{
            mActionBarHost.setTranslationY(0);
        }
        
		mSearchView.clearEditFocus();
		mSearchviewlayout.setVisibility(View.GONE);
        mSearchBackgroud.setVisibility(View.GONE);
        //mActionBarHost.clearAnimation();
        cancelBtn.clearAnimation();
        mSearchViewBorder.clearAnimation();
        mSearchviewlayout.clearAnimation();
        isSearchviewAnimRun = false;

        if(quitListener != null){
            quitListener.quit();
        }
	}
	
	public void hideSearchViewLayout(boolean clearText){
	    mClearSearchViewText = clearText;
	    hideSearchViewInternal();
	}
	
	private void hideSearchViewInternal_Ex(){

        if(!mNeedSearchView){
            return;
        }
        
        if ( !isSearchviewAnimRun && isCanClickToHide ) {

            isCanClickToHide = false;
            
//			if ( mSearchViewAnimRunBackGdDrawable != null ) {
//				mSearchviewBack.setBackground(mSearchViewAnimRunBackGdDrawable);
//			} else {
//				mSearchviewBack.setBackgroundResource(R.drawable.talpa_actionbar_background);
//			}
			// mSearchviewBack.setBackgroundResource(R.drawable.talpa_actionbar_background);//chenjie 注释掉 2017-09-20
            if(mClearSearchViewText){
                mSearchView.clearText();
            }
            
            float actionbarHeight = DensityUtil.dip2px(this, 42);
			//Animation mActionBarHostAnimation = AnimationUtils.loadAnimation(this, R.anim.aurora_activity_searchviewhost_exit);
            Animation mActionBarHostAnimation = new TranslateAnimation(0, 0, -actionbarHeight, 0);
            mActionBarHostAnimation.setDuration(250);
            mActionBarHostAnimation.setInterpolator(new DecelerateInterpolator());
			if ( mSearchViewAnimDuration != -1 ) {
				mActionBarHostAnimation.setDuration(mSearchViewAnimDuration);
			}
			if(mActionBarType == Type.NEW_COSTOM){
				mAuroraCustomActionBarHost.setTranslationY(0);
				mAuroraCustomActionBarHost.startAnimation(mActionBarHostAnimation);
            }else{
            	mActionBarHost.setTranslationY(0);
                mActionBarHost.startAnimation(mActionBarHostAnimation);
            }
			mActionBarHostAnimation.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					mSearchView.clearEditFocus();
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					mSearchviewlayout.setVisibility(View.GONE);
                    mSearchBackgroud.setVisibility(View.GONE);
                    cancelBtn.clearAnimation();
                    mSearchViewBorder.clearAnimation();
                    mSearchviewlayout.clearAnimation();
                    isSearchviewAnimRun = false;
                    
//        			if ( mSearchViewAnimStopBackGdDrawable != null ) {
//        				mSearchviewBack.setBackground(mSearchViewAnimStopBackGdDrawable);
//        			} else {
//        				mSearchviewBack.setBackgroundResource(R.drawable.aurora_activity_searchbar_bg);
//        			}
					//mSearchviewBack.setBackgroundResource(R.drawable.talpa_actionbar_background); //chenjie 注释掉 2017-09-20
                    if(quitListener != null){
                        quitListener.quit();
                    }
                    
                    if(mActionBarType == Type.NEW_COSTOM){
			            mAuroraCustomActionBarHost.clearAnimation();
			        }else{
			            mActionBarHost.clearAnimation();
			        }
				}
			});
            
            cancelBtn.startAnimation(mSearchViewButtonHideAnimation);
            mSearchViewBorder.startAnimation(mSearchViewScaleSmallAnimation);
            mSearchviewlayout.startAnimation(mSearchViewDownAnimation);
            if ( mSearchViewAnimDuration != -1 ) {
            	mSearchViewButtonHideAnimation.setDuration(mSearchViewAnimDuration);
            	mSearchViewDownAnimation.setDuration(mSearchViewAnimDuration);
			}
            isSearchviewAnimRun = true;
        }
	}
	
	private void hideSearchViewInternal(){

        if(!mNeedSearchView){
            return;
        }
        
        if ( !isSearchviewAnimRun && isCanClickToHide ) {

            isCanClickToHide = false;
            
//			if ( mSearchViewAnimRunBackGdDrawable != null ) {
//				mSearchviewBack.setBackground(mSearchViewAnimRunBackGdDrawable);
//			} else {
//				mSearchviewBack.setBackgroundResource(R.drawable.aurora_activity_searchbar_bg2);
//			}
			// mSearchviewBack.setBackgroundResource(R.drawable.talpa_actionbar_background); //chenjie 注释掉 2017-09-20
            if(mClearSearchViewText){
                mSearchView.clearText();
            }
            /*
            AuroraTranslateAnimation anim2 = new AuroraTranslateAnimation(mActionBarHost, AuroraUtil.ACTION_BAR_HEIGHT_PX, 0);
            anim2.setDuration(300);
            anim2.setInterpolator(this, android.R.anim.accelerate_interpolator);
            mActionBarHost.startAnimation(anim2);
            */
            
            float actionbarHeight = DensityUtil.dip2px(this, 42);
            ObjectAnimator translateOut = null;
            if(mActionBarType == Type.NEW_COSTOM){
                translateOut = ObjectAnimator.ofFloat(mAuroraCustomActionBarHost, "TranslationY", -actionbarHeight, 0f);
            }else{
                translateOut = ObjectAnimator.ofFloat(mActionBarHost, "TranslationY", -actionbarHeight, 0f);
            }
			
			if ( mSearchViewAnimDuration != -1 ) {
				translateOut.setDuration(mSearchViewAnimDuration);
			} else {
				translateOut.setDuration(250);
			}
			translateOut.addListener(new AnimatorListener() {
				
				@Override
				public void onAnimationStart(Animator animation) {
					// TODO Auto-generated method stub
					
					mSearchView.clearEditFocus();
				}
				
				@Override
				public void onAnimationRepeat(Animator animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animator animation) {
					// TODO Auto-generated method stub
					
					mSearchviewlayout.setVisibility(View.GONE);
                    mSearchBackgroud.setVisibility(View.GONE);
                    //mActionBarHost.clearAnimation();
                    cancelBtn.clearAnimation();
                    mSearchViewBorder.clearAnimation();
                    mSearchviewlayout.clearAnimation();
                    isSearchviewAnimRun = false;
                    
//        			if ( mSearchViewAnimStopBackGdDrawable != null ) {
//        				mSearchviewBack.setBackground(mSearchViewAnimStopBackGdDrawable);
//        			} else {
//        				mSearchviewBack.setBackgroundResource(R.drawable.aurora_activity_searchbar_bg);
//        			}
					//mSearchviewBack.setBackgroundResource(R.drawable.talpa_actionbar_background); //chenjie 注释掉 2017-09-20
                    if(quitListener != null){
                        quitListener.quit();
                    }
				}
				
				@Override
				public void onAnimationCancel(Animator animation) {
					// TODO Auto-generated method stub
					
				}
			});
			translateOut.start();
            
            cancelBtn.startAnimation(mSearchViewButtonHideAnimation);
            mSearchViewBorder.startAnimation(mSearchViewScaleSmallAnimation);
            mSearchviewlayout.startAnimation(mSearchViewDownAnimation);
            if ( mSearchViewAnimDuration != -1 ) {
            	mSearchViewButtonHideAnimation.setDuration(mSearchViewAnimDuration);
            	mSearchViewDownAnimation.setDuration(mSearchViewAnimDuration);
			}
            isSearchviewAnimRun = true;
            /*
            anim2.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub
                    mSearchView.clearEditFocus();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // TODO Auto-generated method stub
                    
                    mSearchviewlayout.setVisibility(View.GONE);
                    mSearchBackgroud.setVisibility(View.GONE);
                    mActionBarHost.clearAnimation();
                    cancelBtn.clearAnimation();
                    mSearchViewBorder.clearAnimation();
                    isSearchviewAnimRun = false;
                    mSearchviewBack.setBackgroundResource(R.drawable.aurora_activity_searchbar_bg);

                    if(quitListener != null){
                        quitListener.quit();
                    }
                    
                }
            });
            */
        }
	}
	
	
	/**
	 * hideSearchviewLayout tangjun 2013.11.28
	 */
	public void hideSearchviewLayout( ) {
	    hideSearchViewInternal();
	}
	
	public boolean isSearchviewLayoutShow( ) {
		return isCanClickToHide;
	}

	public AuroraActionBarItem addAuroraActionBarItem(AuroraActionBarItem item) {
		return getAuroraActionBar().addItem(item);
	}

	public AuroraActionBarItem addAuroraActionBarItem(AuroraActionBarItem item,
			int itemId) {
		return getAuroraActionBar().addItem(item, itemId);
	}

	public AuroraActionBarItem addAuroraActionBarItem(
			AuroraActionBarItem.Type actionBarItemType) {
		return getAuroraActionBar().addItem(actionBarItemType);
	}

	public AuroraActionBarItem addAuroraActionBarItem(
			AuroraActionBarItem.Type actionBarItemType, int itemId) {
		return getAuroraActionBar().addItem(actionBarItemType, itemId);
	}

	@Override
	/**
	 * 拦截MENU
	 */
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (mActionBarHost != null && mIsNeedShowMenuWhenKeyMenuClick) {
			showAuroraMenu();
			return false;
		}
		return true;// 返回为true 则显示系统menu
	}
	
	public void setIsNeedShowMenuWhenKeyMenuClick(boolean isNeedShowMenuWhenKeyMenuClick) {
		mIsNeedShowMenuWhenKeyMenuClick = isNeedShowMenuWhenKeyMenuClick;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 菜单消失
	 */
	public void dismissAuroraMenu() {
		if (auroraMenu != null && auroraMenu.isShowing()) {
			auroraMenu.dismiss();
		}
		if (auroraCustomMenu != null && auroraCustomMenu.isShowing()) {
//		    Log.e("mm", "************");
            auroraCustomMenu.dismiss();
		}
		removeCoverView();
		// alphaListener.startMenuDismissThread();
	}

	private void loadAnimation(int animId) {
		try {
			coverAnimation = AnimationUtils.loadAnimation(this, animId);
			mCoverView.startAnimation(coverAnimation);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public View getCoverView(){
		return mCoverView;
	}

	public void addCoverView() {
		//if (mCoverView == null) {
			mCoverView = new TextView(this);
			mCoverView.setBackgroundColor(mCoverViewColor);
			mCoverView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT));
			windowLayout.addView(mCoverView);
			Log.e("111111", "----addCoverView----------");
		//}
		loadAnimation(R.anim.aurora_menu_cover_enter);
	}

	public void removeCoverView() {
		Log.e("111111", "----removeCoverView----------");
	    if(mCoverView != null && windowLayout!=null){
	        windowLayout.removeView(mCoverView);
	    }
		loadAnimation(R.anim.aurora_menu_cover_exit);
	}

	/**
	 * 显示菜单
	 */
	public void showAuroraMenu() {
		getAuroraActionBar().setMenuType(AuroraMenu.Type.System);
		if (auroraMenu != null && menuIsEnable) {
			if (auroraMenu.isShowing()) {
				dismissAuroraMenu();
			} else {
				auroraMenu.showAtLocation(getWindow().getDecorView(),
						Gravity.BOTTOM, 0, 0);
				addCoverView();
				// alphaListener.startMenuShowThread();
			}
		}
	}

	public void setMenuEnable(Boolean enable) {
		menuIsEnable = enable;
	}

	/**
	 * @return 得到 aurora menu 当前状态
	 */
	public Boolean auroraMenuIsEnable() {
		return menuIsEnable;
	}

	/**
	 * 设置menu菜单
	 * 
	 * @param menu
	 */
	public void setAuroraMenuItems(int menu, int resId) {
		//aurora add by tangjun start 2014.1.9
//		Log.e("222222", "setAuroraMenuItems----lastMenu = " + lastMenu);
//		Log.e("222222", "setAuroraMenuItems----menu = " + menu);
		if ( lastMenu == menu ) {
			//aurora add by tangjun 2014.5.20 修改两次menu相等直接返回时要把该标志位置为false，
			//防止下次由AuroraMenu变成AuroraSystemMenu时，initMenuData()方法还是会跑AuroraMenu的bug
			getAuroraActionBar().setActionBottomBarMenu(false);
			return;
		}
		lastMenu = menu;
		//aurora add by tangjun end 2014.1.9
		
		parseMenu(menu);
		initMenuData(resId);
	}

	public void setAuroraMenuItems(int menu) {
		setAuroraMenuItems(menu, R.layout.aurora_menu_normal);
	}

	/**
	 * @return 得到aurora菜单
	 */
	public AuroraSystemMenu getAuroraMenu() {
		return auroraMenu;
	}
	
	/**
	 * @return 得到auroracustommenu菜单
	 */
	public AuroraCustomMenu getAuroraCustomMenu() {
		return auroraCustomMenu;
	}

	/**
	 * 解析menu.xml
	 * 
	 * @param menu
	 */
	private void parseMenu(int menu) {
		menuItems = new ArrayList<AuroraMenuItem>();
		menuIds = new HashMap<Integer, Integer>();
		AuroraMenuItem item = null;
		try {
			XmlResourceParser xpp = getResources().getXml(menu);
			xpp.next();
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					startParseMenuDataXml(xpp);
				}
				eventType = xpp.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 开始解析menu xml
	 * 
	 * @param xpp
	 */
	private void startParseMenuDataXml(XmlResourceParser xpp) {
		AuroraMenuItem item;
		if (AuroraUtil.MENU_ITEM.equals(xpp.getName())) {

			String titleId = xpp.getAttributeValue(AuroraUtil.ANDROID_XMLNS,
					AuroraUtil.MENU_TITLE);
			String iconId = xpp.getAttributeValue(AuroraUtil.ANDROID_XMLNS,
					AuroraUtil.MENU_ICON);
			String resId = xpp.getAttributeValue(AuroraUtil.ANDROID_XMLNS,
					AuroraUtil.MENU_ID);
			item = new AuroraMenuItem();
			if (titleId != null) {
				item.setTitle(Integer.valueOf(titleId.replace("@", "")));// 通过R.string设置
				item.setTitleText(this.getResources().getString(item.getTitle()));
			}
			if (iconId != null) {
				item.setIcon(Integer.valueOf(iconId.replace("@", "")));
			}
			if (resId != null) {
				item.setId(Integer.valueOf(resId.replace("@", "")));
			}
			menuItems.add(item);
			menuIds.put((Integer) item.getId(), menuItems.indexOf(item));// 设置itemId,position对应关系
		}
	}

	public void initMenuData(int resId) {
		if (getAuroraActionBar().isActionBottomBarMenu()) {
			//if (this.auroraActionBottomBarMenuAdapter == null) {
				this.auroraActionBottomBarMenuAdapter = new AuroraActionBottomBarMenuAdapter(
						this, menuItems);
			//}
			auroraActionBottomBarMenu = new AuroraMenu(this,
					this.getAuroraActionBar(), this.auroraMenuCallBack,
					this.auroraActionBottomBarMenuAdapter,
					R.style.ActionBottomBarPopupAnimation, resId);
			auroraActionBottomBarMenu.setMenuIds(menuIds);
			getAuroraActionBar().setAuroraActionBottomBarMenu(
					auroraActionBottomBarMenu);
			auroraActionBottomBarMenu.update();
			getAuroraActionBar().setActionBottomBarMenu(false);
			return;
		}

		//if (this.auroraMenuAdapter == null) {
			this.auroraMenuAdapter = new AuroraSystemMenuAdapter(this,
					menuItems);
		//}
		auroraMenu = new AuroraSystemMenu(this, this.auroraMenuCallBack,
				this.auroraMenuAdapter, R.style.PopupAnimation,
				resId);
		auroraMenu.setMenuIds(menuIds);
		auroraMenu.setPullLvHeight(auroraMenu.menuListView);
		auroraMenu.update();
		alphaListener = new AuroraAlphaListener(this);
	}

	public AuroraMenuAdapterBase getAuroraMenuAdapter() {
		return auroraMenuAdapter;
	}

	public void setAuroraMenuAdapter(AuroraMenuAdapterBase auroraMenuAdapter) {
		this.auroraMenuAdapter = auroraMenuAdapter;
	}

	public AuroraMenuAdapterBase getAuroraActionBottomBarMenuAdapter() {
		return auroraActionBottomBarMenuAdapter;
	}

	public void setAuroraActionBottomBarMenuAdapter(
			AuroraMenuAdapterBase auroraActionBottomBarMenuAdapter) {
		this.auroraActionBottomBarMenuAdapter = auroraActionBottomBarMenuAdapter;
	}

	public void setAuroraMenuCallBack(
			AuroraMenuBase.OnAuroraMenuItemClickListener auroraMenuCallBack) {
		this.auroraMenuCallBack = auroraMenuCallBack;
	}

	public void addAuroraMenuItemById(int itemId) {
		auroraMenu.addMenuItemById(itemId);
	}

	public void removeAuroraMenuItemById(int itemId) {
		auroraMenu.removeMenuItemById(itemId);
	}

	public void addAuroraActionBottomBarMenuItemById(int itemId) {
		auroraActionBottomBarMenu.addMenuItemById(itemId);
	}

	public void removeAuroraActionBottomBarMenuItemById(int itemId) {
		auroraActionBottomBarMenu.removeMenuItemById(itemId);
	}

	// Aurora <aven> <2013年9月16日> modify for custom view begin
	public AuroraActionBarHost getAuroraActionBarHost() {
		return mActionBarHost;
	}

	/**
	 * 设置actionbar的布局
	 * 
	 * @param resId
	 */
	public void setCustomView(int resId) {
		if (verifyLayout()) {
			ViewGroup view = (ViewGroup) LayoutInflater.from(this).inflate(
					resId, null);
			views = new ArrayList<View>();
			for (int i = 0; i < mActionBar.getChildCount(); i++) {
				View view2 = mActionBar.getChildAt(i);
				views.add(view2);
			}
			mActionBar.removeAllViews();
			LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);
			mActionBar.addView(view, lp);
		}
	}

	public void goToSelectView() {
		if (verifyLayout()&&firstCreateAllOperation) {
			views = new ArrayList<View>();
			for (int i = 0; i < mActionBar.getChildCount(); i++) {
				View view2 = mActionBar.getChildAt(i);
				views.add(view2);
			}
			mActionBar.removeAllViews();
			LayoutInflater.from(this).inflate(
					R.layout.aurora_action_bar_dashboard,
					mActionBar, true);
			Animation myAnimation_Alpha;
			myAnimation_Alpha = new AlphaAnimation(0.1f, 1.0f);
			myAnimation_Alpha.setDuration(1000);
			// mActionBar.startAnimation(myAnimation_Alpha);
			mLeftView = (TextView) mActionBar
					.findViewById(R.id.aurora_action_bar_btn_cancel);
			mRightView = (TextView) mActionBar
					.findViewById(R.id.aurora_action_bar_btn_right);
			mLeftView.setText(getResources().getString(
					R.string.aurora_action_bar_cancel_btn));
			mRightView.setText(getResources().getString(
					R.string.aurora_action_bar_ok_btn));

			mLeftView.startAnimation(myAnimation_Alpha);
			mRightView.startAnimation(myAnimation_Alpha);
			firstCreateAllOperation=false;
		}
	}

	public View getLeftButton() {
		return mLeftView;
	}

	public View getRightButton() {
		return mRightView;
	}

	/**
	 * 返回到初始布局
	 */
	public void backToFrontView() {
		mActionBar.removeAllViews();
		for (int i = 0; i < views.size(); i++) {
			View view2 = views.get(i);
			mActionBar.addView(view2);
		}
		firstCreateAllOperation=true;
	}

	/**
	 * @param viewId
	 * @return 得到actionbar的自定义布局
	 */
	public ViewGroup getCustomView() {
		return (ViewGroup) mActionBar.getChildAt(0);
	}

	// Aurora <aven> <2013年9月16日> modify for custom view end

	/*
	 * 对action bar menu 消失的处理，返回键消失
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		AuroraMenu auroraActionBarMenu = null;
		AuroraMenu actionBottomBarMenu = null;
		AuroraSystemMenu auroraMenu = null;
		boolean opt = false;

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			opt = closeMenu(auroraActionBarMenu, actionBottomBarMenu,
					auroraMenu, opt);
			if(mSearchviewlayout != null){
			    if(mSearchviewlayout.getVisibility() == View.VISIBLE){
	                hideSearchviewLayout();
	                return true;
	            }
			}
			
		} else if ( keyCode == KeyEvent.KEYCODE_MENU ) {
			if ( this.auroraMenuCallBack == null ) {
				return true;
			}
		}

		if (opt)
			return false;
		return super.onKeyDown(keyCode, event);
	}

	private boolean closeMenu(AuroraMenu auroraActionBarMenu,
			AuroraMenu actionBottomBarMenu, AuroraSystemMenu auroraMenu,
			boolean opt) {
		if (mActionBar != null) {
			auroraMenu = getAuroraMenu();
			auroraActionBarMenu = mActionBar.getActionBarMenu();
			actionBottomBarMenu = mActionBar.getAuroraActionBottomBarMenu();
		}
		if (auroraActionBarMenu != null && auroraActionBarMenu.isShowing()) {
			// auroraActionBarMenu.dismiss();
			mActionBar.showActionBarMenu();
			opt = true;
		}
		if (mActionBar != null && mActionBar.auroraIsEditMode()) {
			mActionBar.showActionBarDashBoard();
			opt = true;
		}
		if (actionBottomBarMenu != null && actionBottomBarMenu.isShowing()) {
			actionBottomBarMenu.dismiss();
			getAuroraActionBar().contentViewFloatDown();
			opt = true;
		}

		if (auroraMenu != null && auroraMenu.isShowing()) {
			dismissAuroraMenu();
			opt = true;
		}
		return opt;
	}
	
	
	
	
	public interface OnSearchViewQueryTextChangeListener{
	    
        public boolean onQueryTextSubmit(String query) ;
        
        public boolean onQueryTextChange(String newText);
	}
	public interface OnSearchViewQuitListener{
	    public boolean quit();
	}
	
	private OnSearchViewQuitListener quitListener;
	public void setOnSearchViewQuitListener(OnSearchViewQuitListener quitListener){
	    this.quitListener = quitListener;
	}
	
	public interface OnSearchBackgroundClickListener{
	    public boolean searchBackgroundClick();
	}
	
	private OnSearchBackgroundClickListener searchBackgroundClickListener;
	public void setOnSearchBackgroundClickListener(OnSearchBackgroundClickListener searchBackgroundClickListener){
	    this.searchBackgroundClickListener = searchBackgroundClickListener;
	}
	
	
	public class AuroraTranslateAnimation extends Animation {
	    private FrameLayout.LayoutParams mLayoutParamsOfMoveView;  
	    private View moveView;
	    private View changeAlphaView;
	    private int movingDistance;
	    private AnimationListener listener;

	    /**
	     * 
	     * @param moveView
	     * @param movingDistance
	     * @param type RIGHT = 0,LEFT = 1;
	     */
	    public AuroraTranslateAnimation(View moveView,int movingDistance,int type) {
	        this.moveView = moveView;
	        this.movingDistance = movingDistance;
	        this.changeAlphaView = changeAlphaView;
	        mLayoutParamsOfMoveView = ((FrameLayout.LayoutParams) moveView.getLayoutParams());
	    }
	    

	    @Override
	    protected void applyTransformation(float interpolatedTime, Transformation t) {
	        //super.applyTransformation(interpolatedTime, t);
	        if(movingDistance >0){
	        	//changeAlphaView.setAlpha(1-interpolatedTime);
	            mLayoutParamsOfMoveView.topMargin = (int)(movingDistance*interpolatedTime)-movingDistance;
            
	        }else{
	        	//changeAlphaView.setAlpha(interpolatedTime);
	            mLayoutParamsOfMoveView.topMargin = (int)(movingDistance*interpolatedTime);
	        }
	        moveView.setLayoutParams(mLayoutParamsOfMoveView);
	        
	    }
	}


    @Override
    public void callBack(boolean flag) {
        // TODO Auto-generated method stub
        if(auroraCustomMenu != null && auroraCustomMenu.isShowing()){
            auroraCustomMenu.dismiss();
        }
    }
	
	
	private boolean isOutOfBounds(Context context, MotionEvent event,View target) {
		final int x = (int) event.getX();
		final int y = (int) event.getY();
		final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
		return (x < -slop) || (y < -slop)
				|| (x > (target.getWidth()+slop))
				|| (y > (target.getHeight()+slop));
	}
	
	public interface OnSearchViewButtonClickListener{
	    public boolean onSearchViewButtonClick();
	}
	private OnSearchViewButtonClickListener mSearchViewButtonClickListener;
	public void setOnSearchViewButtonListener(OnSearchViewButtonClickListener searchViewButtonClickListener){
	    this.mSearchViewButtonClickListener = searchViewButtonClickListener;
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if( mSearchView != null ) {
			mSearchView.clearEditFocus();
		}
	}

	public void setProgress(float progress, int dy){

		if(dy < startPoint ){
			dy = startPoint;
		}

		if(dy > maxDistance){
			dy = maxDistance;
		}

		if(scrollY != dy){
			scrollY = dy;
			progress = (scrollY - startPoint) * 1.0f  / (maxDistance-startPoint);
			if(progress == 1){
				bg.setAlpha(1);
				bg.setBackgroundColor(Color.parseColor("#FAFAFA"));
			}else{
				bg.setAlpha(1-progress);
				bg.setBackgroundResource(R.drawable.bg1);
			}
		}
	}

	public void setContentViewBg(int res){
		bg.setBackgroundResource(res);
	}
}
