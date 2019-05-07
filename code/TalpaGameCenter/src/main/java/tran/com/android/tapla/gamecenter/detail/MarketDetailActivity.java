package tran.com.android.tapla.gamecenter.detail;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import tran.com.android.gc.lib.app.AuroraActivity;
import tran.com.android.gc.lib.utils.DensityUtil;
import tran.com.android.gc.lib.widget.AuroraActionBar;
import tran.com.android.gc.lib.widget.AuroraCustomActionBar;
import tran.com.android.talpa.app_core.log.LogPool;
import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.datauiapi.ManagerThread;
import tran.com.android.tapla.gamecenter.datauiapi.MarketManager;
import tran.com.android.tapla.gamecenter.datauiapi.bean.AppInfo;
import tran.com.android.tapla.gamecenter.datauiapi.bean.AppsItem;
import tran.com.android.tapla.gamecenter.datauiapi.bean.CategoryList;
import tran.com.android.tapla.gamecenter.datauiapi.bean.appiteminfo;
import tran.com.android.tapla.gamecenter.datauiapi.bean.detailsObject;
import tran.com.android.tapla.gamecenter.datauiapi.implement.Command;
import tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse;
import tran.com.android.tapla.gamecenter.datauiapi.interf.INotifiableController;
import tran.com.android.tapla.gamecenter.datauiapi.interf.INotifiableManager;
import tran.com.android.tapla.gamecenter.download.AppDownloadService;
import tran.com.android.tapla.gamecenter.market.activity.BaseActivity;
import tran.com.android.tapla.gamecenter.market.db.CategoryListDataAdapter;
import tran.com.android.tapla.gamecenter.settings.DownloadManagerActivity;
import tran.com.android.tapla.gamecenter.market.download.DownloadUpdateListener;
import tran.com.android.tapla.gamecenter.market.download.FileDownloader;
import tran.com.android.tapla.gamecenter.market.marketApp;
import tran.com.android.tapla.gamecenter.market.model.DownloadData;
import tran.com.android.tapla.gamecenter.market.ui.ExpandableTextView;
import tran.com.android.tapla.gamecenter.market.ui.XCRoundRectImageView;
import tran.com.android.tapla.gamecenter.market.util.BitmapUtil;
import tran.com.android.tapla.gamecenter.market.util.CustomAnimCallBack;
import tran.com.android.tapla.gamecenter.market.util.CustomAnimation;
import tran.com.android.tapla.gamecenter.market.util.DataFromUtils;
import tran.com.android.tapla.gamecenter.market.util.DetailProgressBtnUtil;
import tran.com.android.tapla.gamecenter.market.util.Globals;
import tran.com.android.tapla.gamecenter.market.util.LoadingPageUtil;
import tran.com.android.tapla.gamecenter.market.util.LoadingPageUtil.OnHideListener;
import tran.com.android.tapla.gamecenter.market.util.LoadingPageUtil.OnRetryListener;
import tran.com.android.tapla.gamecenter.market.util.LoadingPageUtil.OnShowListener;
import tran.com.android.tapla.gamecenter.market.util.Log;
import tran.com.android.tapla.gamecenter.market.util.PicBrowseUtils;
import tran.com.android.tapla.gamecenter.market.util.SystemUtils;
import tran.com.android.tapla.gamecenter.market.util.TimeUtils;
import tran.com.android.tapla.gamecenter.market.widget.DetailProgressBtn;
import universalimageloader.core.DisplayImageOptions;
import universalimageloader.core.ImageLoader;
import universalimageloader.core.display.FadeInBitmapDisplayer;
import universalimageloader.core.display.RoundedBitmapDisplayer;
import universalimageloader.core.listener.ImageLoadingListener;
import universalimageloader.core.listener.SimpleImageLoadingListener;

public class MarketDetailActivity extends BaseActivity implements
		OnClickListener, INotifiableController {
	private static final String TAG = "MarketDetailActivity";
	private AuroraActivity mActivity;
	private marketApp app = null;
	private AuroraCustomActionBar mActionBar;
	private LinearLayout mAppPicBrowseLayout = null;
	private View mAppRelacContent;
	private ImageView mappavatar;
	RelativeLayout mDetailPageTop;
	private DetailProgressBtn mDownloadBtn = null;
	private RatingBar mRatingBar;
	private TextView mAppname;
	private TextView mAppTitle;
	private TextView mAppVersion;
	private TextView mAppDeveloper;
	private TextView mAppUpdateTime;
	private TextView mAppCategory;
	private ExpandableTextView mAppDescContent;
	private TextView mDownloadCount;// dis_download_text;
	private ImageView downloadIcon;
	private TextView mAppSize;
	private TextView mViewComment;
	private ImageView mShareBtn;
	private ImageView mmoreView;
	private ScrollView mDetailScrollview;
	private MarketManager mmarketManager;
	private static final int AURORA_NEW_MARKET = 0;
	private detailsObject obj = new detailsObject();
	private AppInfo objFrom9Apps = new AppInfo();
	// 图片加载工具
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions optionsImage, opIconsImage;
	private LinearLayout mLoadingView;
	private ProgressBar mLoadingImg;
	private ManagerThread thread;
	private boolean stopFlag = false;
	private int current_status = FileDownloader.STATUS_DEFAULT;
	private DownloadData downloaddata;
	private AnimationDrawable animationDrawable;
	private boolean isOpenAnimal = true;
	private boolean isUpdate = false;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private SharedPreferences app_update;
	private int update_status = 0;
	private LoadingPageUtil loadingPageUtil;
	private MyBroadcastReciver broadcastReceiver;

	private int mScreenWidth;
	private int mScreenHeight;
	private boolean mIsU3 = false;
	private TextView mAppSource;
	private TextView mAppSourceInfo;
	/**
	 * 判断是否滑动到顶端或者地底端
	 */
	public final double ALPHT_START=  0.4;
	public final double ALPHT_END=  0.8;
	public final int IMAGE_WIDTH=123;
	public final int IMAGE_HEIGHT=218;
	public final int SHOW_ACTIONBAR_ANIMATION=165;
	public final int HIDDEN_ACTIONBAR_ANIMATION=26;

	private int tenDp;
	private int thirtySixDp;
	private int sixtyFourDp;
	private int oneHundredAndFourDp;

	private DetailProgressBtnUtil progressBtnUtil;
	// private Animation anim;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAuroraContentView(R.layout.activity_detail_page,AuroraActionBar.Type.NEW_COSTOM, true);
		initTransparentTheme();
		setStatusBarIconDark(true);
		app_update = getSharedPreferences(Globals.SHARED_APP_UPDATE,MODE_APPEND);
		update_status = app_update.getInt(Globals.SHARED_DOWNORUPDATE_KEY_ISEXITS, 0);
		downloaddata = (DownloadData) getIntent().getParcelableExtra("downloaddata");
		//解决parcelable跨进程传递object失败问题
		String packageName = getIntent().getStringExtra("packageName");
		if(downloaddata == null && null != packageName){
			downloaddata = new DownloadData();
			downloaddata.setPackageName(packageName);
		}
		initActionBar();
		initViews();
		initimageLoad();
		//监听滑动，改变渐变和缩放 start 2017/9/14
		initScrollListener();
		//监听滑动，改变渐变和缩放 end 2017/9/14
		setListener();
		initLoadingPage();
		mmarketManager = new MarketManager();
		thread = new ManagerThread(mmarketManager);
		thread.market(this);
		getCategoryList();
		initdata();
		initBroadCast();

		tenDp = getResources().getDimensionPixelSize(
				R.dimen.detail_avatar_ten_dp);

		thirtySixDp = getResources().getDimensionPixelSize(
				R.dimen.detail_title_thirty_six_dp);

		sixtyFourDp = getResources().getDimensionPixelSize(
				R.dimen.detail_title_sixty_four_dp);

		oneHundredAndFourDp = getResources().getDimensionPixelSize(
				R.dimen.detail_title_one_hundred_and_four_dp);

		mActivity = this;

	}

	/**
	 * 小幅度滑动，自动滑到最顶端
	 */
	Runnable scrollviewRunnable=new Runnable() {
		@Override
		public void run() {
			float alpha=mAppname.getAlpha();
				if(alpha>ALPHT_START) {
					mDetailScrollview.fullScroll(ScrollView.FOCUS_UP);
				}else if(alpha>-ALPHT_END){
					mDetailScrollview.smoothScrollTo((int) mDetailScrollview.getScaleY(),291);
				}
		}
	};
	/**
	 *初始化滑动监听
	 *
	 */
	@TargetApi(Build.VERSION_CODES.M)
	private void initScrollListener() {
		mDetailScrollview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
			@Override
			public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
				setScrollAnim(scrollY);
			}
		});
		mDetailScrollview.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					mDetailScrollview.post(scrollviewRunnable);
				}
				return false;
			}
		});
	}

	/**
	 * @Title: initBroadCast
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param
	 * @return void
	 * @throws
	 */
	private void initBroadCast() {
		// TODO Auto-generated method stub
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Globals.BROADCAST_ACTION_DOWNLOAD);
		intentFilter.addAction(Globals.MARKET_UPDATE_ACTION);
		broadcastReceiver = new MyBroadcastReciver();
		registerReceiver(broadcastReceiver, intentFilter);
	}
	boolean isStartAnimato;
	/**
	 * 滑动途中的动画效果
	 * @param scrollAnimY 滑动的Y轴值
     */
	public void setScrollAnim(int scrollAnimY) {
		LogPool.d("scrollAnimY = " + scrollAnimY);

		if(scrollAnimY<= tenDp){
			float alpha = scrollAnimY * 1.0f  / tenDp;
			mappavatar.setAlpha(1.0f - alpha);
			mappavatar.setScaleX(1.0f - alpha*0.5f);
			mappavatar.setScaleY(1.0f - alpha*0.5f);
		}else if(scrollAnimY>tenDp){
			mappavatar.setAlpha(0.0f);
			mappavatar.setScaleX(0.5f);
			mappavatar.setScaleY(0.5f);
		}

		if(thirtySixDp <= scrollAnimY && scrollAnimY <= sixtyFourDp){
			float alpha1 =  (scrollAnimY - thirtySixDp) * 1.0f  / (sixtyFourDp - thirtySixDp);
			mAppname.setAlpha(1.0f - alpha1);
			mRatingBar.setAlpha(1.0f -alpha1);
			mDownloadCount.setAlpha(1.0f -alpha1);
			downloadIcon.setAlpha(1.0f -alpha1);
			mAppSize.setAlpha(1.0f -alpha1);
		}else if(scrollAnimY>sixtyFourDp){
			mAppname.setAlpha(0.0f);
			mRatingBar.setAlpha(0.0f);
			mDownloadCount.setAlpha(0.0f);
			downloadIcon.setAlpha(0.0f);
			mAppSize.setAlpha(0.0f);
		}else if(scrollAnimY<thirtySixDp){
			mAppname.setAlpha(1.0f);
			mRatingBar.setAlpha(1.0f);
			mDownloadCount.setAlpha(1.0f);
			downloadIcon.setAlpha(1.0f);
			mAppSize.setAlpha(1.0f);
		}

		Log.v("heightPixels","heightPixels:"+scrollAnimY);
		mActionBar.setTitle(mAppname.getText(), Color.parseColor("#505050"));
		if(scrollAnimY >= oneHundredAndFourDp){
			if(!isStartAnimato){
				ObjectAnimator.ofFloat(mActionBar.getTitleView(), "alpha", 0.0f, 1.0f).setDuration(200).start();
				isStartAnimato=true;
			}
		}else{
			if(isStartAnimato){
				ObjectAnimator.ofFloat(mActionBar.getTitleView(), "alpha", 1.0f, 0.0f).setDuration(200).start();
				isStartAnimato=false;
			}
		}
		if(scrollAnimY>=300){
			mActionBar.changeActionBarBg(R.drawable.actionbar_touying);
		}else{
			mActionBar.changeActionBarBgColor(Color.parseColor("#FAFAFA"));
		}
	}
	private class MyBroadcastReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Globals.BROADCAST_ACTION_DOWNLOAD) || action.equals(Globals.MARKET_UPDATE_ACTION)) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						getUpAppSign();
					}
				}, 1000);

			}
		}

	}

	private void getUpAppSign() {

		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				int count = AppDownloadService.getDownloaders().size();
				int sum = 0;
				if (count == 0) {
					DataFromUtils up_data = new DataFromUtils();
					sum = up_data.getUpdateSum(MarketDetailActivity.this);
				}
				if ((sum > 0) || (count > 0)) {
					runOnUiThread(new Runnable() {
						public void run() {
							setUpdateSign(1);
						}

					});
				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							setUpdateSign(0);
						}

					});
				}

			}

		}.start();
	}

	private void initLoadingPage() {
		loadingPageUtil = new LoadingPageUtil();
		loadingPageUtil.init(this, findViewById(R.id.detailLayout));
		loadingPageUtil.setOnRetryListener(new OnRetryListener() {
			@Override
			public void retry() {
				initdata();
			}
		});
		loadingPageUtil.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow() {
				// mListView.setVisibility(View.GONE);
			}
		});
		loadingPageUtil.setOnHideListener(new OnHideListener() {
			@Override
			public void onHide() {
				// mListView.setVisibility(View.VISIBLE);
			}
		});
		loadingPageUtil.showLoadPage();
		loadingPageUtil.showLoading();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		PicBrowseUtils.resetImgVContainer();
		((MarketManager) mmarketManager).setController(null);
		thread.quit();
		unregisterReceiver(broadcastReceiver);
	}

	/**
	 * @Title: initimageLoad
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param
	 * @return void
	 * @throws
	 */
	private void initimageLoad() {
		// TODO Auto-generated method stub
		optionsImage = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_screen_shot_bg)
				.showImageForEmptyUri(R.drawable.default_screen_shot_bg)
				.showImageOnFail(R.drawable.default_screen_shot_bg).cacheInMemory(false)
				.displayer(new RoundedBitmapDisplayer(getResources().getDimensionPixelOffset(R.dimen.app_icon_displayer_rounded)))
				.cacheOnDisc(false).build();
		opIconsImage = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_icon)
				.showImageForEmptyUri(R.drawable.default_icon)
				.displayer(new RoundedBitmapDisplayer(getResources().getDimensionPixelOffset(R.dimen.app_icon_displayer)))
				.showImageOnFail(R.drawable.default_icon)
				.cacheInMemory(true).cacheOnDisc(true).build();

	}

	private void disView() {
		// TODO Auto-generated method stub

		appiteminfo info = obj.getAppInfo();


		Uri mImageCaptureUri =
		Bitmap photoBmp = null;
		if (mImageCaptureUri != null) {
			photoBmp = MediaStore.Images.Media.getBitmap(MarketDetailActivity.this, mImageCaptureUri);
		}


//		checkSoftwareState(false);
		mAppname.setText(info.getTitle());
		mRatingBar.setRating(info.getLikesRate());
		mDownloadCount.setText(info.getDownloadCountStr());
		mAppSize.setText(info.getAppSizeStr());
		mAppVersion.setText(info.getVersionName());
		String source = info.getMarket();
		if (source != null && !source.isEmpty()) {
			mAppSourceInfo.setText(source);
			mAppSource.setVisibility(View.VISIBLE);
			mAppSourceInfo.setVisibility(View.VISIBLE);
		} else {
			mAppSource.setVisibility(View.GONE);
			mAppSourceInfo.setVisibility(View.GONE);
		}

		Log.v(TAG, "aurora.jiangmx " + info.getCreateTime());

		mAppUpdateTime.setText(TimeUtils.getFormatDate(
				TimeUtils.getLongFromStrTime(info.getCreateTime())));
		mAppDeveloper.setText(info.getDeveloper());
		mAppCategory.setText(info.getCategory());

		// 开始头像图片异步加载
		if (SystemUtils.isLoadingImage(this)) {
			imageLoader.displayImage(info.getIcons().getPx256(), mappavatar,
					opIconsImage);
		}
		if (isUpdate) {
			mAppTitle.setText(getString(R.string.app_update_desc));

			if (TextUtils.isEmpty(info.getChangelog())) {
				mAppDescContent
						.setText(getString(R.string.app_detail_no_desctext));
			} else {
				mAppDescContent.setText(info.getChangelog());
			}
		} else {
			mAppTitle.setText(getString(R.string.app_intro));
			mAppDescContent.setText(info.getDescription());
		}
		boolean ifFold = false;
		boolean ifHide = false;
		int lines = mAppDescContent.getLineCount();
		if (lines > 3) {
			ifFold = true;
			ifHide = true;
			mAppDescContent.setLines(3);
			mAppDescContent.setCollapseLines(3, true);
			mmoreView.setBackgroundResource(R.drawable.more_view);
			mAppRelacContent.setOnClickListener(new ShowListener(ifFold,
					ifHide, mAppDescContent, mmoreView, lines));

		} else {
			ifFold = false;
			ifHide = false;
			mAppDescContent.setLines(lines);
			mAppDescContent.setCollapseLines(lines, false);
			mmoreView.setVisibility(View.GONE);
			mmoreView.setBackgroundResource(R.drawable.more_view_up);
		}

		setupAppDetailDisplay(info.getScreenshots());

		loadingPageUtil.hideLoadPage();
		// showLoadingView(false);
	}


	private void disViewFrom9Apps() {
		// TODO Auto-generated method stub

		AppsItem info = objFrom9Apps.getData().getApps().get(0);
		Log.v("suwei","info ==== " + info.getFileSize());
//		checkSoftwareState(false);
		mAppname.setText(info.getTitle());
		mRatingBar.setRating(info.getRate() / 2);
		mDownloadCount.setText(info.getDownloadTotal()+"");
		mAppSize.setText(SystemUtils.bytes2kb((long)info.getFileSize()));
		mAppVersion.setText(info.getVersionName());
		String source = null; //info.getMarket();
		if (source != null && !source.isEmpty()) {
			mAppSourceInfo.setText(source);
			mAppSource.setVisibility(View.VISIBLE);
			mAppSourceInfo.setVisibility(View.VISIBLE);
		} else {
			mAppSource.setVisibility(View.GONE);
			mAppSourceInfo.setVisibility(View.GONE);
		}

		Log.v(TAG, "aurora.jiangmx " + info.getPublishTime());

		mAppUpdateTime.setText(info.getUpdateTime());

		mAppDeveloper.setText("Unknown");

		CategoryListDataAdapter ldb = new CategoryListDataAdapter(MarketDetailActivity.this);
		ldb.open();
		String category = ldb.getTitle(info.getCategoryId());
		if("".equals(category)){
			category = "Unknown";
		}
		mAppCategory.setText(category);
		ldb.close();
		// 开始头像图片异步加载
		if (SystemUtils.isLoadingImage(this)) {
			imageLoader.displayImage(info.getIcon(), mappavatar,
					opIconsImage);
		}
		if (isUpdate) {
			mAppTitle.setText(getString(R.string.app_update_desc));

			if (TextUtils.isEmpty(info.getChangeLog())) {
				mAppDescContent
						.setText(getString(R.string.app_detail_no_desctext));
			} else {
				mAppDescContent.setText(info.getChangeLog());
			}
		} else {
			mAppTitle.setText(getString(R.string.app_intro));
			mAppDescContent.setText(info.getDescription());
		}
		boolean ifFold = false;
		boolean ifHide = false;
		int lines = mAppDescContent.getLineCount();
		if (lines > 3) {
			ifFold = true;
			ifHide = true;
			mAppDescContent.setLines(3);
			mAppDescContent.setCollapseLines(3, true);
			mmoreView.setBackgroundResource(R.drawable.more_view);
			mAppRelacContent.setOnClickListener(new ShowListener(ifFold,
					ifHide, mAppDescContent, mmoreView, lines));

		} else {
			ifFold = false;
			ifHide = false;
			mAppDescContent.setLines(lines);
			mAppDescContent.setCollapseLines(lines, false);
			mmoreView.setVisibility(View.GONE);
			mmoreView.setBackgroundResource(R.drawable.more_view_up);
		}

		setupAppDetailDisplay(info.getScreenshots().split(";"));

		loadingPageUtil.hideLoadPage();
		// showLoadingView(false);
	}
	private void initViews() {
		mAppPicBrowseLayout = (LinearLayout) findViewById(R.id.app_pic_browse_view);

		mAppRelacContent = (View) findViewById(R.id.app_desc_content);
		mappavatar = (ImageView) findViewById(R.id.app_avatar);
		mDownloadBtn = (DetailProgressBtn) findViewById(R.id.download_btn);
		mDetailPageTop= (RelativeLayout) findViewById(R.id.detail_page_top);
		mAppname = (TextView) findViewById(R.id.app_name);
		mRatingBar = (RatingBar) findViewById(R.id.app_rating);
		mAppVersion = (TextView) findViewById(R.id.version_info);
		mAppDeveloper = (TextView) findViewById(R.id.developer_info);
		mAppSource = (TextView) findViewById(R.id.market_source);
		mAppSourceInfo = (TextView) findViewById(R.id.source_info);
		mAppUpdateTime = (TextView) findViewById(R.id.update_time_info);
		mAppCategory = (TextView) findViewById(R.id.category_info);
		mAppDescContent = (ExpandableTextView) findViewById(R.id.desc_content);
		mAppTitle = (TextView) findViewById(R.id.app_title);
		mDownloadCount = (TextView) findViewById(R.id.download_count);
		downloadIcon = (ImageView) findViewById(R.id.download_icon);
		mAppSize = (TextView) findViewById(R.id.app_size);
//		mProgressBar = (ProgressBar) findViewById(R.id.download_progress_rate);
		mViewComment = (TextView) findViewById(R.id.view_comment);
		mmoreView = (ImageView) findViewById(R.id.expand_more_img);
		mDetailScrollview = (ScrollView) findViewById(R.id.detail_scrollview);
//		mDownloadBtnLayout = (FrameLayout) findViewById(R.id.download_btn_layout);
//		mDownloadBtnInstall = (LinearLayout) findViewById(R.id.download_btn_install);
//		download_text = (TextView) findViewById(R.id.download_text);
//		mDownloadProLayout = (FrameLayout) findViewById(R.id.download_progress);
//		dis_download_text = (TextView) findViewById(R.id.dis_download_text);
//		mCancelDownloadBtn = (ImageView) findViewById(R.id.cancel_download_btn);
//		mDownloadInstallView = (ImageView) findViewById(R.id.download_install);
//		mToDownloadManagerBtn = (ImageView) findViewById(R.id.redirect_download_btn);
		if (update_status == 0) {
			setUpdateSign(0);
		} else {
			setUpdateSign(1);
		}
		progressBtnUtil = new DetailProgressBtnUtil();
		progressBtnUtil.updateProgressBtn(mDownloadBtn, downloaddata);
	}

	private void setUpdateSign(int sign_type) {
//		if (sign_type == 0) {
//			main_update.setVisibility(View.GONE);
//		} else {
//			main_update.setVisibility(View.VISIBLE);
//		}

	}



	private void setListener() {

		mDownloadBtn.setOnClickListener(this);

		mViewComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

//		mCancelDownloadBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				AppDownloadService.cancelDownload(MarketDetailActivity.this,
//						downloaddata);
//
//			}
//		});

//		mToDownloadManagerBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				Intent lInt = new Intent(MarketDetailActivity.this,
//						DownloadManagerActivity.class);
//				startActivity(lInt);
//			}
//		});
	}

	private void setShowExPandAnimal() {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				int lines = msg.what;
				// 这里接受到消息，让后更新TextView设置他的maxLine就行了

				mAppDescContent.setLines(lines);
				mAppDescContent.setCollapseLines(lines, true);
			}
		};
		if (thread != null)
			handler.removeCallbacks(thread);

		Thread thread = new Thread() {
			@Override
			public void run() {
				int count = mAppDescContent.getLineCount();
				while (count-- > 3) {
					// 每隔20mms发送消息
					Message message = new Message();
					message.what = count;
					handler.sendMessage(message);

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				super.run();
			}
		};
		thread.start();

	}

	class ShowListener implements OnClickListener {
		private boolean ifFold;
		private boolean ifHide;
		private ExpandableTextView content;
		private ImageView show;
		private int lines;

		public ShowListener(boolean ifFold, boolean ifHide,
				ExpandableTextView content, ImageView show, int lines) {
			this.ifFold = ifFold;
			this.ifHide = ifHide;
			this.content = content;
			this.show = show;
			this.lines = lines;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (ifFold) {
				if (!ifHide) {
					show.setVisibility(View.VISIBLE);
					mmoreView
							.setBackgroundResource(R.drawable.more_view);
					/*
					 * content.setLines(3); content.setCollapseLines(3, true);
					 */
					ifHide = true;
					setShowExPandAnimal();

				} else {
					show.setVisibility(View.VISIBLE);
					content.setLines(lines);
					content.setCollapseLines(lines, true);
					mmoreView
							.setBackgroundResource(R.drawable.more_view_up);
					ifHide = false;
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							mDetailScrollview.fullScroll(ScrollView.FOCUS_DOWN);
						}
					});

				}
			}
		}

	}

	private void initActionBar() {
		mActionBar = getCustomActionBar();
		mActionBar.setTitleAnim(0);
//		mActionBar.setTitle(R.string.app_detail_page);
		mActionBar.setBackground(getResources().getDrawable(
				R.drawable.aurora_action_bar_top_bg_green));
		mActionBar.showHomeIcon(true);
//		mActionBar.setBackground(getResources().getColor(R.color.white));
		/*mActionBar.setDefaultOptionItemDrawable(getResources().getDrawable(
				R.drawable.btn_main_right_selector));*/
		mActionBar.setTitleViewColor(true);


		mActionBar.showDefualtItem(false);
		mActionBar.setOptionLayoutMarginRight(0, 16, 0, 0);
		mActionBar.addItemView(R.layout.actionbar_main_right2);

//		main_update = (ImageView) mActionBar.findViewById(R.id.actionbar_main_update);
		View view = mActionBar.findViewById(R.id.download_layout);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(MarketDetailActivity.this,
//						MarketManagerPreferenceActivity.class);
//				startActivity(intent);
				Intent dInt = new Intent(MarketDetailActivity.this, DownloadManagerActivity.class);
				startActivity(dInt);
			}
		});
	}

	private void initdata() {
		// showLoadingView(true);
		if("9APPS".equals(downloaddata.getServerTag())){
			getNetDataFrom9Apps();
		}else{
			getNetDataFromTalpa();
		}
	}

	private void showLoadingView(boolean pIsShow) {

		if (null == mLoadingImg && null == mLoadingView) {

			mLoadingView = (LinearLayout) findViewById(R.id.loading_view);
			mLoadingImg = (ProgressBar) mLoadingView
					.findViewById(R.id.loading_img);

		}

		if (pIsShow) {
			mLoadingView.setVisibility(View.VISIBLE);
			// mLoadingImg.startAnimation( createRotateAnimation() );
		} else
			mLoadingView.setVisibility(View.GONE);

	}
	public void initTransparentTheme() {
		Window window = getWindow();//获取window
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.setStatusBarColor(getResources().getColor(R.color.app_detail_page_text_color_top_bg));
		initStatusBarHeight();
	}

	public void initStatusBarHeight() {
		LinearLayout linear = (LinearLayout) this.findViewById(R.id.top_bar);
		linear.setVisibility(View.VISIBLE);
		int statusHeight = getStatusBarHeight();
		ViewGroup.LayoutParams params = linear.getLayoutParams();
		params.height = statusHeight;
		linear.setLayoutParams(params);
	}

	/**
	 * 获取状态栏的高度
	 */
	public int getStatusBarHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	private void setStatusBarIconDark(boolean lightStatusBar) {
		Window window = getWindow();
		View decor = window.getDecorView();
		int ui = decor.getSystemUiVisibility();
		if (lightStatusBar) {
			ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
		} else {
			ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
		}
		decor.setSystemUiVisibility(ui);
	}
	private void upDownLoadData() {
		appiteminfo list = obj.getAppInfo();
		downloaddata.setApkId(list.getId());
		downloaddata.setApkDownloadPath(list.getDownloadURL());
		downloaddata.setApkLogoPath(list.getIcons().getPx256());
		downloaddata.setApkName(list.getTitle());
		downloaddata.setVersionCode(list.getVersionCode());
		downloaddata.setVersionName(list.getVersionName());
	}

	private void upDownLoadDataFor9Apps() {
		AppsItem list = objFrom9Apps.getData().getApps().get(0);
		downloaddata.setPublishId(list.getPublishId());
		downloaddata.setDownloadAddress(list.getDownloadAddress());
		downloaddata.setIcon(list.getIcon());
		downloaddata.setApkName(list.getTitle());
		downloaddata.setVersionCode(list.getVersionCode());
		downloaddata.setVersionName(list.getVersionName());
	}

	private void getNetDataFromTalpa() {
		mmarketManager.getDetailsItems(new DataResponse<detailsObject>() {
			public void run() {
				if (value != null) {
					Log.i(TAG, "the value=" + value.getCode());
					obj = value;
					if (null != obj)
						upDownLoadData();
					disView();
				}
			}

		}, MarketDetailActivity.this, downloaddata.getPackageName());
	}

	private void getCategoryList(){
		if(!SystemUtils.getIfCategoryList(MarketDetailActivity.this)){
			mmarketManager.getCategoryList(new DataResponse<CategoryList>() {
				public void run() {
					if (value != null) {
						if(value.getData()!=null){
							if(value.getData().getCategories()!=null && value.getData().getCategories().size()>0){
								//保存CategoryList
								CategoryListDataAdapter ldb = new CategoryListDataAdapter(MarketDetailActivity.this);
								ldb.open();
								ldb.insert(value.getData().getCategories());
								ldb.close();
							}
						}
					}
				}
			}, MarketDetailActivity.this, 1, 1, 100);
		}
	}

	private void getNetDataFrom9Apps() {
		mmarketManager.getAppInfoFrom9Apps(new DataResponse<AppInfo>() {
			public void run() {
				if (value != null) {
					Log.i(TAG, "the value=" + value.getCode());
					objFrom9Apps = value;
					if (null != objFrom9Apps)
						upDownLoadDataFor9Apps();

					disViewFrom9Apps();
				}
			}

		}, MarketDetailActivity.this, downloaddata.getPackageName(), String.valueOf(downloaddata.getPublishId()));
	}

	private void setupAppDetailDisplay(String[] icons) {
		XCRoundRectImageView lImg = null;
		View lView = null;
		PicBrowseUtils.resetImgVContainer();
		/*
		 * DisplayMetrics metric = new DisplayMetrics();
		 * getWindowManager().getDefaultDisplay().getMetrics(metric);
		 * mScreenWidth = metric.widthPixels; mScreenHeight =
		 * metric.heightPixels;
		 * 
		 * if( mScreenWidth == SystemUtils.U3_SCREEN_WIDTH && mScreenHeight ==
		 * SystemUtils.U3_SCREEN_HEIGHT ){ mIsU3 = true;
		 * mAppPicBrowseLayout.setLayoutParams( new
		 * FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
		 * DensityUtil.dip2px(this, 195))); }else{ mIsU3 = false;
		 * mAppPicBrowseLayout.setLayoutParams( new
		 * FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
		 * DensityUtil.dip2px(this, 214))); }
		 */
		mAppPicBrowseLayout
				.setLayoutParams(new FrameLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, getResources()
								.getDimensionPixelOffset(
										R.dimen.app_detail_pic_browse)));
		if ((icons.length == 0) || (!SystemUtils.isLoadingImage(this))) {
			Bitmap bit = BitmapFactory.decodeResource(getResources(),
					R.drawable.default_icon);
			int h = bit.getHeight();
			int w = getResources().getDimensionPixelOffset(
					R.dimen.app_detail_pic_browse)
					* bit.getWidth() / h;

			/*
			 * if(!mIsU3) w= DensityUtil.dip2px(this, 214) * bit.getWidth() / h;
			 * else w= DensityUtil.dip2px(this, 195) * bit.getWidth() / h;
			 */
			for (int i = 0; i < 3; i++) {

				lImg = new XCRoundRectImageView(this);
				// lImg.setBackgroundColor(Color.BLACK); //
				// setBackgroundResource(pResIds[i]);
				/*
				 * if(!mIsU3) lImg.setLayoutParams(new
				 * LinearLayout.LayoutParams(w, DensityUtil.dip2px(this, 214)));
				 * else lImg.setLayoutParams(new LinearLayout.LayoutParams(w,
				 * DensityUtil.dip2px(this, 195)));
				 */
				lImg.setLayoutParams(new LinearLayout.LayoutParams(w,
						getResources().getDimensionPixelOffset(
								R.dimen.app_detail_pic_browse)));
				/*
				 * lImg.setLayoutParams(new
				 * LayoutParams(LayoutParams.WRAP_CONTENT, 640));
				 */
				lImg.setTag(i);
				lImg.setScaleType(ScaleType.FIT_XY);
				lImg.setBackgroundResource(R.drawable.page_thumbnail);
				if (i == 0) {
					mAppPicBrowseLayout.addView(addDivider(/*52*/DensityUtil.dip2px(this,20)));
				} else {
					mAppPicBrowseLayout.addView(addDivider(/*36*/DensityUtil.dip2px(this,10)));
				}
				mAppPicBrowseLayout.addView(lImg,new LayoutParams(DensityUtil.dip2px(this,IMAGE_WIDTH)
						,DensityUtil.dip2px(this,IMAGE_HEIGHT)));
				if (i == 2) {
					mAppPicBrowseLayout.addView(addDivider(51));
				}
			}
		} else {
			for (int i = 0; i < icons.length; i++) {

				lImg = new XCRoundRectImageView(this);
				// lImg.setBackgroundColor(Color.BLACK); //
				// setBackgroundResource(pResIds[i]);

				/*
				 * if(!mIsU3) lImg.setLayoutParams(new LayoutParams(
				 * LayoutParams.WRAP_CONTENT, DensityUtil.dip2px(this, 214)));
				 * else lImg.setLayoutParams(new LayoutParams(
				 * LayoutParams.WRAP_CONTENT, DensityUtil.dip2px(this, 195)));
				 */
				lImg.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, getResources()
								.getDimensionPixelOffset(
										R.dimen.app_detail_pic_browse)));
				lImg.setTag(i);
				lImg.setScaleType(ScaleType.FIT_XY);
				
				if (mIsU3)
					lImg.setPadding(0, DensityUtil.dip2px(this, 10), 0,
							DensityUtil.dip2px(this, 10));

				lImg.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					    
						String index = v.getTag().toString();

						Intent intent = new Intent(MarketDetailActivity.this,
								PictureViewActivity.class);
						
						intent.putExtra("index", Integer.valueOf(index));
						if("9APPS".equals(downloaddata.getServerTag())){
							intent.putExtra("content", objFrom9Apps.getData().getApps().get(0).getScreenshots().split(";"));
						}else{
							intent.putExtra("content", obj.getAppInfo()
									.getScreenshots());
						}

						
						startActivity(intent);
						overridePendingTransition(0, 0);
						//view.setDrawingCacheEnabled(false);

					}
				});
				// DensityUtil.dip2px(context, dpValue)
				if (SystemUtils.isLoadingImage(this)) {
					imageLoader.displayImage(icons[i], lImg, optionsImage,
							animateFirstListener);
//					Glide.with(this).load(icons[i]).diskCacheStrategy(DiskCacheStrategy.RESULT).into(lImg);//跳过内存缓存
//					Glide.with(this).load(icons[i]).into(lImg);
				}
				if (i == 0) {
					mAppPicBrowseLayout.addView(addDivider(DensityUtil.dip2px(
							this, 17)));
				} else {
					mAppPicBrowseLayout.addView(addDivider(DensityUtil.dip2px(
							this, 12)));
				}
				mAppPicBrowseLayout.addView(lImg,new LayoutParams(DensityUtil.dip2px(MarketDetailActivity.this,IMAGE_WIDTH)
						,DensityUtil.dip2px(MarketDetailActivity.this,IMAGE_HEIGHT)));
				 int[] location = new  int[2] ;
			     final Rect rect = new Rect();
//			     mAppPicBrowseLayout.getLocationOnScreen(location);

				PicBrowseUtils.addImgV(lImg);
				
				if (i == icons.length - 1) {
					mAppPicBrowseLayout.addView(addDivider(DensityUtil.dip2px(
							this, 17)));
				}

			}
		}
	}

	private class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				final ImageView imageView = (ImageView) view;
				// aurora ukiliu add 2014-09-10 begin
				if (loadedImage.getHeight() < loadedImage.getWidth()) {
					loadedImage = BitmapUtil.rotateBitmap(loadedImage, 90);
				}
				// aurora ukiliu add 2014-09-10 end

				int h = loadedImage.getHeight();
				int w = getResources().getDimensionPixelOffset(
						R.dimen.app_detail_pic_browse)
						* loadedImage.getWidth() / h;
				Log.i(TAG, "zhangwei the h="+h+" the w="+w);
				/*
				 * if(!mIsU3) w= DensityUtil.dip2px(MarketDetailActivity.this,
				 * 214) * loadedImage.getWidth() / h; else w=
				 * DensityUtil.dip2px(MarketDetailActivity.this, 195) *
				 * loadedImage.getWidth() / h;
				 */

				imageView.setLayoutParams(new LinearLayout.LayoutParams(/*w*/DensityUtil.dip2px(MarketDetailActivity.this,IMAGE_WIDTH),
						getResources().getDimensionPixelOffset(
								R.dimen.app_detail_pic_browse)));
				
				imageView.setImageBitmap(loadedImage);
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}

			}
		}

		/**
		 * @Title: runOnUiThread
		 * @Description: TODO(这里用一句话描述这个方法的作用)
		 * @param @param runnable
		 * @return void
		 * @throws
		 */
		private void runOnUiThread(Runnable runnable) {
			// TODO Auto-generated method stub

		}
	}

	private View addDivider(int pDividerLen) {
		View lView = new View(this);
		lView.setBackgroundColor(Color.WHITE);
		lView.setLayoutParams(new LayoutParams(pDividerLen, 640));
		lView.setBackgroundColor(getResources().getColor(R.color.app_detail_page_text_color_centent_bg));
		return lView;
	}

	private RotateAnimation createRotateAnimation() {
		RotateAnimation animation = null;
		animation = new RotateAnimation(0, 3600, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setFillAfter(true);
		animation.setDuration(10000);
		animation.setStartOffset(0);
		animation.setRepeatCount(1000);
		return animation;
	}

	@Override
	public void onWrongConnectionState(int state, INotifiableManager manager,
			Command<?> source) {
		// TODO Auto-generated method stub
		mHandler.sendEmptyMessage(Globals.NETWORK_ERROR);
	}

	@Override
	public void onError(int code, String message, INotifiableManager manager) {
		// TODO Auto-generated method stub
		switch (code) {
		case INotifiableController.CODE_UNKNONW_HOST:
		case INotifiableController.CODE_WRONG_DATA_FORMAT:
		case INotifiableController.CODE_REQUEST_TIME_OUT:
		case INotifiableController.CODE_CONNECT_ERROR:
		case INotifiableController.CODE_GENNERAL_IO_ERROR:
		case INotifiableController.CODE_NOT_FOUND_ERROR:
		case INotifiableController.CODE_JSON_PARSER_ERROR:
		case INotifiableController.CODE_JSON_MAPPING_ERROR:
		case INotifiableController.CODE_UNCAUGHT_ERROR:
			mHandler.sendEmptyMessage(Globals.NETWORK_ERROR);
			break;
		case INotifiableController.CODE_NOT_NETWORK:
			mHandler.sendEmptyMessage(Globals.NO_NETWORK);
			break;
		default:
			break;
		}
	}

	@Override
	public void onMessage(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void runOnUI(DataResponse<?> response) {
		// TODO Auto-generated method stub
		mHandler.post(response);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			// super.handleMessage(msg);
			switch (msg.what) {
			case 0:

//				mDownloadBtn.getBackground().setAlpha(255);
//				mDownloadBtn.setScaleY(1.0f);
//				mDownloadBtn.setScaleX(1.0f);
//				download_text.setTextSize(18);
				break;

			case Globals.NETWORK_ERROR:
				if (loadingPageUtil.isShowing()) {
					loadingPageUtil.showNetworkError();
				}
				break;
			case Globals.NO_NETWORK:
				if (loadingPageUtil.isShowing()) {
					loadingPageUtil.showNoNetWork();
				}
				break;
			default:
				break;
			}

		}

	};

	public void setAnimal1() {
		// int[] loc = new int[2];

//		mProgressBar.setProgress(0);
		/*
		 * dis_download_text.getLocationInWindow(loc); ViewGroup flayout =
		 * (ViewGroup) getWindowLayout(); Rect rect = new Rect();
		 * dis_download_text.getHitRect(rect);
		 * 
		 * flayout.offsetDescendantRectToMyCoords(dis_download_text, rect);
		 */
//		TranslateAnimation animation = new TranslateAnimation(0, 0,
//				-DensityUtil.dip2px(MarketDetailActivity.this, 5), 0);
//
//		AlphaAnimation animation2 = new AlphaAnimation(0, 1.0f);
//
//		AnimationSet set = new AnimationSet(true);
//		set.setDuration(350);//AURORA UKILIU MODIFY 2014-10-10 END
//		set.setInterpolator(new DecelerateInterpolator());
//		set.addAnimation(animation);
//		set.addAnimation(animation2);
		// download_process_wait
//		dis_download_text.setText(getString(R.string.download_process_wait));
//		dis_download_text.startAnimation(set);

	}

	private void dotheDownOpr() {
		CustomAnimation animation = new CustomAnimation(
				new CustomAnimCallBack() {
					int isStartAnimal = 0;
					int isEndAnimal = 0;

					@Override
					public void callBack(float interpolatedTime,
							Transformation t) {

						Log.i(TAG, "the interpolatedTime=" + interpolatedTime);
						if (isEndAnimal == 1)
							return;

						//AURORA UKILIU MODIFY 2014-10-10 BEGIN
						mDownloadBtn.setScaleX(1.0f - 0.239f * interpolatedTime);
						mDownloadBtn.getBackground().setAlpha(
								(int) (255 * (1.0f - interpolatedTime)));
						mDownloadBtn.setScaleY(1.0f - 0.9f * (interpolatedTime));
//						download_text
//								.setTextSize(18 * (1 - 0.4f * (interpolatedTime)));
						//AURORA UKILIU MODIFY 2014-10-10 END
						if (interpolatedTime == 1.0f) {
							isEndAnimal = 1;
//							mDownloadBtnLayout.setVisibility(View.GONE);
							mHandler.sendEmptyMessageDelayed(0, 200);
//							mDownloadProLayout.setVisibility(View.VISIBLE);

							Animation anim1 = AnimationUtils.loadAnimation(
									MarketDetailActivity.this, R.anim.alpha);
							anim1.setInterpolator(new DecelerateInterpolator());
							/* mDownloadProLayout.startAnimation(anim1); */
//							mCancelDownloadBtn.startAnimation(anim1);
//							mToDownloadManagerBtn.startAnimation(anim1);
							setAnimal1();
							AppDownloadService.startDownload(
									MarketDetailActivity.this, downloaddata);
						}

					}
				});

		animation.setDuration(350);//AURORA UKILIU MODIFY 2014-10-10 END
		animation.setFillAfter(true);
		animation.setInterpolator(new DecelerateInterpolator());

		mDownloadBtn.startAnimation(animation);
	}

	private void dotheOpenOpr() {
		CustomAnimation animation = new CustomAnimation(
				new CustomAnimCallBack() {
					int isStartAnimal = 0;
					int isEndAnimal = 0;

					@Override
					public void callBack(float interpolatedTime,
							Transformation t) {

						Log.i(TAG, "the interpolatedTime=" + interpolatedTime);
						if (isEndAnimal == 1)
							return;
						mDownloadBtn.getBackground().setAlpha(
								(int) (255 * (0.1 + 0.9 * interpolatedTime)));
						mDownloadBtn.setScaleY(0.1f + 0.9f * interpolatedTime);
//						download_text
//								.setTextSize(18 * (0.8f + 0.2f * interpolatedTime));
						if (interpolatedTime == 1.0f) {
							isEndAnimal = 1;
							// mHandler.sendEmptyMessageDelayed(0, 200);
						}

					}
				});

		animation.setDuration(1000);
		animation.setFillAfter(true);
		animation.setInterpolator(new DecelerateInterpolator());

		mDownloadBtn.startAnimation(animation);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
//		int id = view.getId();
//		switch (id) {
//		case R.id.download_btn:
//
//			if (!SystemUtils.hasNetwork()) {
//				Toast.makeText(this,
//						getString(R.string.no_network_download_toast),
//						Toast.LENGTH_SHORT).show();
//				return;
//			}
//			String dis_text = download_text.getText().toString();
//
//			if (dis_text
//					.equals(getResources().getString(R.string.app_download))
//					|| dis_text.equals(getResources().getString(
//							R.string.download_process_update))) {
//				if (!SystemUtils.isDownload(MarketDetailActivity.this)) {
//
//					AuroraAlertDialog mWifiConDialog = new AuroraAlertDialog.Builder(
//							MarketDetailActivity.this,
//							AuroraAlertDialog.THEME_AMIGO_FULLSCREEN)
//							.setTitle(
//									getResources().getString(
//											R.string.dialog_prompt))
//							.setMessage(
//									getResources().getString(
//											R.string.no_wifi_download_message))
//							.setNegativeButton(android.R.string.cancel, null)
//							.setPositiveButton(android.R.string.ok,
//									new DialogInterface.OnClickListener() {
//
//										@Override
//										public void onClick(
//												DialogInterface dialog,
//												int which) {
//
//											SharedPreferences sp = PreferenceManager
//													.getDefaultSharedPreferences(MarketDetailActivity.this);
//											Editor ed = sp.edit();
//											ed.putBoolean("wifi_download_key",
//													false);
//											ed.commit();
//											dotheDownOpr();
//
//										}
//
//									}).create();
//					mWifiConDialog.show();
//				} else {
//					dotheDownOpr();
//				}
//			} else if (dis_text.equals(getResources().getString(
//					R.string.app_install))) {
////				mDownloadBtnLayout.setVisibility(view.GONE);
////				mDownloadBtnInstall.setVisibility(view.VISIBLE);
////				animationDrawable = (AnimationDrawable) mDownloadInstallView
////						.getBackground();
//
//				animationDrawable.start();
//				/*
//				 * DownloadData tempData = AppDownloadService
//				 * .getAppDownloadDao().getDownloadData(
//				 * downloaddata.getApkId()); String fileDir =
//				 * tempData.getFileDir(); fileDir = fileDir == null ? "" :
//				 * fileDir; String fileName = tempData.getFileName(); fileName =
//				 * fileName == null ? "" : fileName; final File file = new
//				 * File(fileDir, fileName);
//				 * ApkUtil.installApp(MarketDetailActivity.this, file);
//				 */
//				DownloadData tempData = AppDownloadService.getAppDownloadDao()
//						.getDownloadData(downloaddata.getApkId());
//				String fileDir = tempData.getFileDir();
//				fileDir = fileDir == null ? "" : fileDir;
//				String fileName = tempData.getFileName();
//				fileName = fileName == null ? "" : fileName;
//				final File file = new File(fileDir, fileName);
//				/*
//				 * PackageInstallObserver observer = new
//				 * PackageInstallObserver();
//				 *
//				 * SystemUtils.intstallApp(MarketDetailActivity.this,
//				 * tempData.getPackageName(), file, observer);
//				 */
//				tempData.setStatus(FileDownloader.STATUS_INSTALL_WAIT);
//				AppDownloadService.getAppDownloadDao().updateStatus(tempData.getApkId(),
//						FileDownloader.STATUS_INSTALL_WAIT);
//
//				AppInstallService.startInstall(MarketDetailActivity.this,
//						tempData, AppInstallService.TYPE_NORMAL);
//				/* RootCmdUtil.slientInstall(MarketDetailActivity.this, file); */
//			} else if (dis_text.equals(getResources().getString(
//					R.string.item_open))) {
//				ApkUtil.openApp(MarketDetailActivity.this,
//						downloaddata.getPackageName());
//			}
//
//			break;
//		default:
//			break;
//		}
	}

	class PackageInstallObserver extends
			android.content.pm.IPackageInstallObserver.Stub {
		public void packageInstalled(String packageName, int returnCode) {
			Message msg = mHandler.obtainMessage(1);
			msg.arg1 = returnCode;
			mHandler.sendMessage(msg);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (stopFlag) {
			updateListener.downloadProgressUpdate();
			stopFlag = false;
		}
		AppDownloadService.registerUpdateListener(updateListener);
	}

	@Override
	protected void onStop() {
		super.onStop();

		stopFlag = true;
		AppDownloadService.unRegisterUpdateListener(updateListener);
	}

	private DownloadUpdateListener updateListener = new DownloadUpdateListener() {
		@Override
		public void downloadProgressUpdate() {
			/*
			 * FileDownloader downloader = AppDownloadService.getDownloaders()
			 * .get(id); if(null != downloader) { long downloadSize =
			 * downloader.getDownloadSize(); long fileSize =
			 * downloader.getFileSize(); double pre = 0; if (fileSize != 0) {
			 * pre = (downloadSize * 1.0) / fileSize; } int progress = (int)
			 * (pre * 100); String test = String.format(
			 * getResources().getString( R.string.download_process_tip),
			 * progress) + getString(R.string.download_process_sign);
			 * dis_download_text.setText(test);
			 * mProgressBar.setProgress(progress); } else {
			 * mDownloadBtnLayout.setVisibility(View.VISIBLE);
			 * mDownloadProLayout.setVisibility(View.GONE);
			 * download_text.setText(R.string.app_install); }
			 */
			checkSoftwareState(true);
		}
	};

    /**
     * 检查应用的状态, 并显示相应布局
     */
	public void checkSoftwareState(boolean type) {

		// 检测是否安装
        FileDownloader downloader = AppDownloadService.getDownloaders().get(downloaddata.getApkId());
		if(downloader!=null) {
			downloaddata = downloader.getDownloadData();
			progressBtnUtil.updateProgressBtn(mDownloadBtn, downloaddata);
		}else{
			DownloadData tempData = AppDownloadService.getAppDownloadDao().getDownloadData(downloaddata.getApkId());
			if(tempData!=null) {
				progressBtnUtil.updateProgressBtn(mDownloadBtn, tempData);
			}else {
				progressBtnUtil.updateProgressBtn(mDownloadBtn, downloaddata);
			}
		}
//		// 未安装的情况
//		if (installedAppInfo == null) {
//			FileDownloader downloader = AppDownloadService.getDownloaders().get(downloaddata.getApkId());
//            DownloadData tempData =  AppDownloadService.getAppDownloadDao().getDownloadData(downloaddata.getApkId());
//            progressBtnUtil.updateProgressBtn(mDownloadBtn, downloader.getDownloadData());
////			// 如果下载器任务存在, 显示各状态信息
//			if (downloader != null) {
//				int status = downloader.getStatus();
//				current_status = status;
//                progressBtnUtil.updateProgressBtn(mDownloadBtn, downloader.getDownloadData());
////				mDownloadBtnLayout.setVisibility(View.GONE);
////				mDownloadProLayout.setVisibility(View.VISIBLE);
//
////				long downloadSize = downloader.getDownloadSize();
////				long fileSize = downloader.getFileSize();
////				double pre = 0;
////				if (fileSize != 0) {
////					pre = (downloadSize * 1.0) / fileSize;
////				}
////				int progress = (int) (pre * 100);
////				if (status == FileDownloader.STATUS_DOWNLOADING) {
////					String test = String.format(getResources().getString(R.string.download_process_tip), progress)+ getString(R.string.download_process_sign);
////					dis_download_text.setText(test);
////					mProgressBar.setProgress(progress);
////				} else if ((status == FileDownloader.STATUS_WAIT)|| (status == FileDownloader.STATUS_CONNECTING)) {
////
////					dis_download_text.setText(getString(R.string.download_process_wait));
////					mProgressBar.setProgress(0);
////				} else {
////					String test = String.format(getResources().getString(R.string.download_process_pause), progress)+ getString(R.string.download_process_sign);
////					dis_download_text.setText(test);
////					mProgressBar.setProgress(progress);
////				}
//			} else { // 任务完成或者没有记录
//				// long start = System.currentTimeMillis();
//				DownloadData tempData = AppDownloadService.getAppDownloadDao().getDownloadData(downloaddata.getApkId());
//				if (null == tempData) {
//                    progressBtnUtil.updateProgressBtn(mDownloadBtn, downloader.getDownloadData());
//				} else {
//					int status = tempData.getStatus();
//
//					current_status = status;
//					String fileDir = tempData.getFileDir();
//					fileDir = fileDir == null ? "" : fileDir;
//					String fileName = tempData.getFileName();
//					fileName = fileName == null ? "" : fileName;
//					final File file = new File(fileDir, fileName);
//					// 查看数据库中该任务状态是否为完成, 并且文件是存在的
//					if (((status == FileDownloader.STATUS_INSTALLFAILED) || (status == FileDownloader.STATUS_INSTALLED))&& file.exists()) {
//
//						if (status == FileDownloader.STATUS_INSTALLED&& ((mDownloadProLayout.getVisibility() == View.VISIBLE) || (mDownloadBtnInstall.getVisibility() == View.VISIBLE))) {
//							return;
//						}
//						if(status == FileDownloader.STATUS_INSTALLFAILED)
//						{
//							mDownloadBtnInstall.setVisibility(View.GONE);
//							mDownloadProLayout.setVisibility(View.GONE);
//							mDownloadBtnLayout.setVisibility(View.VISIBLE);
//							download_text.setText(R.string.app_install);
//						}
//						else
//						{
//						mDownloadBtnLayout.setVisibility(View.VISIBLE);
//						mDownloadProLayout.setVisibility(View.GONE);
//						download_text.setText(R.string.app_install);
//						}
//					} else if (status >= FileDownloader.STATUS_INSTALL_WAIT
//							&& file.exists()) {
//						// download_text.setText(R.string.app_install);
//
//						mDownloadProLayout.setVisibility(View.GONE);
//
//
//						if(status == FileDownloader.STATUS_INSTALLFAILED)
//						{
//							mDownloadBtnInstall.setVisibility(View.GONE);
//							mDownloadBtnLayout.setVisibility(View.VISIBLE);
//							download_text.setText(R.string.app_install);
//						}
//						else
//						{
//							mDownloadBtnLayout.setVisibility(View.GONE);
//							mDownloadBtnInstall.setVisibility(View.VISIBLE);
//						}
//
//
//
//
//						animationDrawable = (AnimationDrawable) mDownloadInstallView.getBackground();
//
//						animationDrawable.start();
//					} else { // 条件不符合则显示下载
//						mDownloadProLayout.setVisibility(View.GONE);
//						mDownloadBtnLayout.setVisibility(View.VISIBLE);
//						download_text.setText(R.string.app_download);
//					}
//				}
//			}
//		} else {
//			// 这里判断是否为最新版本
//			/*
//			 * if(installedAppInfo.getStatus() !=
//			 * AppInstallService.OPERATION_FINISH_INSTALL) {
//			 * mDownloadProLayout.setVisibility(View.GONE);
//			 * mDownloadBtnLayout.setVisibility(View.GONE);
//			 * mDownloadBtnInstall.setVisibility(View.VISIBLE);
//			 * animationDrawable=(AnimationDrawable)
//			 * mDownloadInstallView.getBackground();
//			 *
//			 * animationDrawable.start(); } else {
//			 */
//			if ((null != animationDrawable) && (animationDrawable.isRunning()))
//				animationDrawable.stop();
//			if (downloaddata.getVersionCode() > installedAppInfo
//					.getVersionCode()) { // 不是最新版本
//				FileDownloader downloader = AppDownloadService.getDownloaders()
//						.get(downloaddata.getApkId());
//				// 如果下载器任务存在, 显示各状态信息
//				if (downloader != null) {
//					mDownloadBtnInstall.setVisibility(View.GONE);
//					mDownloadBtnLayout.setVisibility(View.GONE);
//					mDownloadProLayout.setVisibility(View.VISIBLE);
//					int status = downloader.getStatus();
//					current_status = status;
//					long downloadSize = downloader.getDownloadSize();
//					long fileSize = downloader.getFileSize();
//					double pre = 0;
//					if (fileSize != 0) {
//						pre = (downloadSize * 1.0) / fileSize;
//					}
//					int progress = (int) (pre * 100);
//					if (status == FileDownloader.STATUS_DOWNLOADING) {
//
//						String test = String.format(
//								getResources().getString(
//										R.string.download_process_tip),
//								progress)
//								+ getString(R.string.download_process_sign);
//						dis_download_text.setText(test);
//						mProgressBar.setProgress(progress);
//					} else {
//						String test = String.format(
//								getResources().getString(
//										R.string.download_process_pause),
//								progress)
//								+ getString(R.string.download_process_sign);
//						dis_download_text.setText(test);
//						mProgressBar.setProgress(progress);
//					}
//				} else { // 任务完成或者没有记录
//					// long start = System.currentTimeMillis();
//
//					DownloadData tempData = AppDownloadService
//							.getAppDownloadDao().getDownloadData(
//									downloaddata.getApkId());
//					// long end = System.currentTimeMillis();
//					// Log.i(TAG, "db getDownloadData time: " + (end - start));
//					if (tempData == null) {
//						if (!type) {
//							isUpdate = true;
//						}
//						if ((null != animationDrawable) && (animationDrawable.isRunning()))
//							animationDrawable.stop();
//						mDownloadBtnInstall.setVisibility(View.GONE);
//						mDownloadBtnLayout.setVisibility(View.VISIBLE);
//						mDownloadProLayout.setVisibility(View.GONE);
//						download_text.setText(R.string.download_process_update);
//					} else {
//						int status = tempData.getStatus();
//
//						current_status = status;
//						String fileDir = tempData.getFileDir();
//						fileDir = fileDir == null ? "" : fileDir;
//						String fileName = tempData.getFileName();
//						fileName = fileName == null ? "" : fileName;
//						final File file = new File(fileDir, fileName);
//						// 查看数据库中该任务状态是否为完成, 并且文件是存在的
//
//						if (tempData.getVersionCode() == downloaddata
//								.getVersionCode()) {
//
//							if (status == FileDownloader.STATUS_INSTALLING) { // 安装中
//								mDownloadProLayout.setVisibility(View.GONE);
//								mDownloadBtnLayout.setVisibility(View.GONE);
//								mDownloadBtnInstall.setVisibility(View.VISIBLE);
//								animationDrawable = (AnimationDrawable) mDownloadInstallView
//										.getBackground();
//
//								animationDrawable.start();
//							} else if (((status == FileDownloader.STATUS_INSTALLFAILED) || (status == FileDownloader.STATUS_INSTALLED))
//									&& file.exists()) {
//
//								if (status == FileDownloader.STATUS_INSTALLED
//										&& ((mDownloadProLayout.getVisibility() == View.VISIBLE) || (mDownloadBtnInstall
//												.getVisibility() == View.VISIBLE))) {
//									return;
//								}
//
//								if ((null != animationDrawable) && (animationDrawable.isRunning()))
//									animationDrawable.stop();
//								mDownloadBtnInstall.setVisibility(View.GONE);//aurora ukiliu added for BUG #10913
//								mDownloadBtnLayout.setVisibility(View.VISIBLE);
//								mDownloadProLayout.setVisibility(View.GONE);
//								download_text.setText(R.string.app_install);
//							} else { // 条件不符合则显示下载
//								if (!type) {
//									isUpdate = true;
//								}
//								if (status >= FileDownloader.STATUS_INSTALL_WAIT
//										&& (mDownloadProLayout.getVisibility() == View.VISIBLE)) {
//									return;
//								}
//								if ((null != animationDrawable) && (animationDrawable.isRunning()))
//									animationDrawable.stop();
//								mDownloadBtnInstall.setVisibility(View.GONE);
//								mDownloadBtnLayout.setVisibility(View.VISIBLE);
//								mDownloadProLayout.setVisibility(View.GONE);
//								download_text
//										.setText(R.string.download_process_update);
//							}
//						} else {
//							if (!type) {
//								isUpdate = true;
//							}
//							if (status >= FileDownloader.STATUS_INSTALL_WAIT
//									&& (mDownloadProLayout.getVisibility() == View.VISIBLE)) {
//								return;
//							}
//							mDownloadBtnLayout.setVisibility(View.VISIBLE);
//							mDownloadProLayout.setVisibility(View.GONE);
//							download_text
//									.setText(R.string.download_process_update);
//
//						}
//					}
//				}
//
//			} else { // 如果是最新版本
//				if ((null != animationDrawable)
//						&& (animationDrawable.isRunning()))
//					animationDrawable.stop();
//				mDownloadBtnInstall.setVisibility(View.GONE);
//				mDownloadBtnLayout.setVisibility(View.VISIBLE);
//				mDownloadProLayout.setVisibility(View.GONE);
//				download_text.setText(R.string.item_open);
//
//
//				Animation anim = AnimationUtils.loadAnimation(
//						MarketDetailActivity.this, R.anim.scale1);
//
//				anim.setFillAfter(true);
//				Animation anim1 = AnimationUtils.loadAnimation(
//						MarketDetailActivity.this, R.anim.scale2);
//				anim1.setFillAfter(true);
//				anim.setAnimationListener(new AnimationListener() {
//
//					@Override
//					public void onAnimationStart(Animation animation) {
//						// TODO Auto-generated method stub
//
//					}
//
//					@Override
//					public void onAnimationRepeat(Animation animation) {
//						// TODO Auto-generated method stub
//
//					}
//
//					@Override
//					public void onAnimationEnd(Animation animation) {
//						// TODO Auto-generated method stub
//
//					/*	Animation anim2 = AnimationUtils.loadAnimation(
//								MarketDetailActivity.this, R.anim.scale3);
//
//						anim2.setFillAfter(true);
//						mDownloadBtn.startAnimation(anim2);*/
//					}
//				});
//
//				/*
//				 * if (!type) isOpenAnimal = false;
//				 */
//				if ((current_status == FileDownloader.STATUS_INSTALLING)
//						&& isOpenAnimal && type) {
//					mDownloadBtn.startAnimation(anim);
//
//					download_text.startAnimation(anim1);
//					//mDownloadBtn.startAnimation(anim2);
//
//					// dotheOpenOpr();
//					isOpenAnimal = false;
//				}
//				current_status = downloaddata.getStatus();
//			}
//		}
//		// }

	}
}
