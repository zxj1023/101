package tran.com.android.tapla.gamecenter.settings;

import android.app.ActionBar.LayoutParams;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tran.com.android.gc.lib.app.AuroraAlertDialog;
import tran.com.android.gc.lib.widget.AuroraActionBar;
import tran.com.android.gc.lib.widget.AuroraActionBar.OnAuroraActionBarBackItemClickListener;
import tran.com.android.gc.lib.widget.AuroraActionBar.OnAuroraActionBarItemClickListener;
import tran.com.android.gc.lib.widget.AuroraListView;
import tran.com.android.gc.lib.widget.AuroraListView.AuroraBackOnClickListener;
import tran.com.android.gc.lib.widget.AuroraListView.AuroraDeleteItemListener;
import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.datauiapi.ManagerThread;
import tran.com.android.tapla.gamecenter.datauiapi.UpMarketManager;
import tran.com.android.tapla.gamecenter.datauiapi.bean.UpgradeListObject;
import tran.com.android.tapla.gamecenter.datauiapi.bean.upappListtem;
import tran.com.android.tapla.gamecenter.datauiapi.bean.upcountinfo;
import tran.com.android.tapla.gamecenter.datauiapi.implement.Command;
import tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse;
import tran.com.android.tapla.gamecenter.datauiapi.interf.INotifiableController;
import tran.com.android.tapla.gamecenter.datauiapi.interf.INotifiableManager;
import tran.com.android.tapla.gamecenter.detail.MarketDetailActivity;
import tran.com.android.tapla.gamecenter.install.AppInstallService;
import tran.com.android.tapla.gamecenter.market.MarketMainActivity;
import tran.com.android.tapla.gamecenter.market.activity.BaseActivity;
import tran.com.android.tapla.gamecenter.market.adapter.AppUpdateAdapter;
import tran.com.android.tapla.gamecenter.market.db.IgnoreAppAdapter;
import tran.com.android.tapla.gamecenter.market.download.DownloadUpdateListener;
import tran.com.android.tapla.gamecenter.market.download.FileDownloader;
import tran.com.android.tapla.gamecenter.market.install.InstallAppManager;
import tran.com.android.tapla.gamecenter.market.install.InstallNotification;
import tran.com.android.tapla.gamecenter.market.model.DownloadData;
import tran.com.android.tapla.gamecenter.market.model.InstalledAppInfo;
import tran.com.android.tapla.gamecenter.market.service.AppDownloadService;
import tran.com.android.tapla.gamecenter.market.util.Globals;
import tran.com.android.tapla.gamecenter.market.util.LoadingPageUtil;
import tran.com.android.tapla.gamecenter.market.util.LoadingPageUtil.OnHideListener;
import tran.com.android.tapla.gamecenter.market.util.LoadingPageUtil.OnRetryListener;
import tran.com.android.tapla.gamecenter.market.util.LoadingPageUtil.OnShowListener;
import tran.com.android.tapla.gamecenter.market.util.Log;
import tran.com.android.tapla.gamecenter.market.util.SystemUtils;
import tran.com.android.tapla.gamecenter.market.widget.DetailProgressBtn;
import tran.com.android.tapla.gamecenter.market.widget.ProgressBtn;
import tran.com.android.tapla.gamecenter.util.Config;
import tran.com.android.tapla.gamecenter.util.Utils;

public class MarketUpdateActivity extends BaseActivity implements
		INotifiableController {

	private final static String TAG = "MarketUpdateActivity";

	private UpMarketManager mUpMarketManager;
	private AppUpdateAdapter mAdapter;

	private AuroraActionBar mActionBar;

	private static final int AURORA_DOWNLOAD = 1;
	private boolean isLoadDataFinish = false;
	private int pageNum = 1;
	private int rowCount = 15;
	private ManagerThread thread;

	private UpgradeListObject obj = new UpgradeListObject();
	private List<DownloadData> down_data = new ArrayList<DownloadData>();
	private boolean stopFlag = false;
	private int mAppSize = 0;
	
	private boolean updateBtnEnable = true;

	private LoadingPageUtil loadingPageUtil;
	private View animal_view;

	private IgnoreAppAdapter mIgnoreAppAdapter;

	private FrameLayout mDownloadBtn;
	private TextView mDownloadNum;

	private TextView mAvailUpdateNumBtn;
	private TextView mUpdateSettingsBtn;
	private TextView mUpdateAllAppsBtn;

	private AuroraListView mAvailUpdateAppsList;
	private DetailProgressBtn mAppUpdateBtn;
//	private TextView mAppUpdateInfo;

	private LinearLayout mEmptyLayout;
	private TextView mEmptyView;
	private ImageView mEmptyImg;
	private Button mEmptyBtn;
	private int allSize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setAuroraContentView(R.layout.activity_app_update_page);
		InstallNotification.cancleUpdateNotify();
		initActionBar();

		initViews();
		setListener();
		setAdapter();
		registerBroadCastReceiver();

		initLoadingPage();

		mUpMarketManager = new UpMarketManager();
		thread = new ManagerThread(mUpMarketManager);

		thread.market(this);

		initData();
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (stopFlag) {
			updateListener.downloadProgressUpdate();
			stopFlag = false;
			mAvailUpdateAppsList.postInvalidate();
		}
		AppDownloadService.registerUpdateListener(updateListener);
	}

	@Override
	protected void onStop() {
		super.onStop();

		stopFlag = true;
		AppDownloadService.unRegisterUpdateListener(updateListener);
	}

	@Override
	protected void onPause() {
		mAvailUpdateAppsList.auroraOnPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mAvailUpdateAppsList.auroraOnResume();

		if (AppDownloadService.getDownloaders().size() == 0) {
			mDownloadNum.setVisibility(View.GONE);
		} else {
			mDownloadNum.setVisibility(View.VISIBLE);
			mDownloadNum.setText(String.valueOf(AppDownloadService
					.getDownloaders().size()));
		}

		updateDownloadBtnState();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		thread.quit();
		mUpMarketManager.setController(null);
		
		if (mBroadcastReceiver != null) {
			unregisterReceiver(mBroadcastReceiver);
		}
	}

	private void initActionBar() {
		mActionBar = getAuroraActionBar();
		mActionBar.setTitle(R.string.app_update_page);
		mActionBar.setBackground(getResources().getDrawable(
				R.drawable.actionbar_touying));
		mActionBar.addItem(R.layout.actionbar_download_num, AURORA_DOWNLOAD);
		mDownloadBtn = (FrameLayout) mActionBar
				.findViewById(R.id.download_layout);
		mDownloadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent lInt = new Intent(MarketUpdateActivity.this,
						DownloadManagerActivity.class);
				MarketUpdateActivity.this.startActivity(lInt);
			}
		});

		mDownloadNum = (TextView) mActionBar.findViewById(R.id.download_num);

		if (AppDownloadService.getDownloaders().size() == 0) {
			mDownloadNum.setVisibility(View.GONE);
		} else {
			mDownloadNum.setVisibility(View.VISIBLE);
			mDownloadNum.setText(String.valueOf(AppDownloadService
					.getDownloaders().size()));
		}

		mActionBar
				.setOnAuroraActionBarListener(auroraActionBarItemClickListener);

		mActionBar
				.setmOnActionBarBackItemListener(new OnAuroraActionBarBackItemClickListener() {

					@Override
					public void onAuroraActionBarBackItemClicked(int itemId) {
						// TODO Auto-generated method stub
						Log.i(TAG, "setmOnActionBarBackItemListener back");
						Intent intent = new Intent(MarketUpdateActivity.this,
								MarketManagerActivity.class);
						Intent intent1 = new Intent(MarketUpdateActivity.this,
								MarketMainActivity.class);
						ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
						List<RunningTaskInfo> appTask = am.getRunningTasks(1);
						boolean ifFront = false;
						for (RunningTaskInfo app : appTask) {
							if (app.baseActivity.equals(
									intent1.getComponent())) {
								ifFront = true;
								break;
							}
						}

						if (ifFront) {
							finish();
							Log.i(TAG, "setmOnActionBarBackItemListener back1");
						} else {

							finish();
							startActivity(intent);
							overridePendingTransition(
									R.anim.aurora_activity_close_enter,
									R.anim.aurora_activity_close_exit);
							Log.i(TAG, "setmOnActionBarBackItemListener back2");
						}
					}
				});

	}

	private OnAuroraActionBarItemClickListener auroraActionBarItemClickListener = new OnAuroraActionBarItemClickListener() {

		@Override
		public void onAuroraActionBarItemClicked(int arg0) {
			// TODO Auto-generated method stub
		}

	};

	private void initViews() {
		setAnimal();
		mAvailUpdateNumBtn = (TextView) findViewById(R.id.avail_update_num_btn);

		mUpdateSettingsBtn = (TextView) findViewById(R.id.update_settings_btn);
		mUpdateSettingsBtn.setVisibility(View.VISIBLE);

		mUpdateAllAppsBtn = (TextView) findViewById(R.id.update_all_apps_btn);
		mUpdateAllAppsBtn.setVisibility(View.GONE);

		mAvailUpdateAppsList = (AuroraListView) findViewById(R.id.avail_update_apps_list);
		mAvailUpdateAppsList.auroraSetNeedSlideDelete(true);
		mAvailUpdateAppsList.auroraEnableSelector(true);
		mAvailUpdateAppsList.setSelector(R.drawable.list_item_selector);
		mAvailUpdateAppsList.setFocusable(true);

		mAppUpdateBtn = (DetailProgressBtn) findViewById(R.id.app_update_btn);
		mAppUpdateBtn.setStatus(DetailProgressBtn.STAUTS_A_KEY_UPDATE);
//		mAppUpdateInfo = (TextView) findViewById(R.id.update_info);

		mEmptyLayout = (LinearLayout) findViewById(R.id.upempty_layout);
		mEmptyView = (TextView) findViewById(R.id.upempty_view);
		mEmptyImg = (ImageView) findViewById(R.id.upempty_bg);
		mEmptyBtn = (Button) findViewById(R.id.upempty_btn);

		mIgnoreAppAdapter = new IgnoreAppAdapter(MarketUpdateActivity.this);
	}

	private void setAdapter() {
		mAdapter = new AppUpdateAdapter(this, obj.getUpgradeApps(), down_data,
				AppUpdateAdapter.UPDATE_APP);

		mAvailUpdateAppsList.setAdapter(mAdapter);
	}

	private void initLoadingPage() {
		loadingPageUtil = new LoadingPageUtil();
		loadingPageUtil.init(this, this.getContentView());
		loadingPageUtil.setOnRetryListener(new OnRetryListener() {
			@Override
			public void retry() {
				initData();
			}
		});
		loadingPageUtil.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow() {
				mAvailUpdateAppsList.setVisibility(View.GONE);
			}
		});
		loadingPageUtil.setOnHideListener(new OnHideListener() {
			@Override
			public void onHide() {
				mAvailUpdateAppsList.setVisibility(View.VISIBLE);
			}
		});
		loadingPageUtil.showLoadPage();
		loadingPageUtil.showLoading();
	}

	private void registerBroadCastReceiver() {
		// Create a filter with the broadcast intents we are interested in.
		IntentFilter filter = new IntentFilter();
		filter.addAction(Globals.MARKET_UPDATE_ACTION);
		registerReceiver(mBroadcastReceiver, filter, null, null);
	}

	public void setAnimal() {
		animal_view = new View(this);
		animal_view.layout(0, 0, 200, 200);
		LayoutParams params = new LayoutParams(200, 200);
		ViewGroup tt = (ViewGroup) this.getWindowLayout();
		tt.addView(animal_view, params);
		animal_view.setVisibility(View.GONE);
		animal_view.setBackgroundColor(getResources().getColor(R.color.red));

	}

	public void setAnimal1(final ImageView view) {
		int[] loc = new int[2];
		// mListView.getLocationOnScreen(loc);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;// 宽度height = dm.heightPixels ;//高度
		view.getLocationInWindow(loc);
		ViewGroup flayout = (ViewGroup) getWindowLayout();
		Rect rect = new Rect();
		view.getHitRect(rect);

		flayout.offsetDescendantRectToMyCoords(view, rect);
		TranslateAnimation animation = new TranslateAnimation(rect.left, width,
				rect.top, 1);
		AnimationSet set = new AnimationSet(true);

		// animation.setInterpolator(new DecelerateInterpolator());

		ScaleAnimation animation1 = new ScaleAnimation(0.9f, 0.2f, 0.9f, 0.2f,
				Animation.RELATIVE_TO_PARENT, 0.9f,
				Animation.RELATIVE_TO_PARENT, 0.02f);

		AlphaAnimation animation2 = new AlphaAnimation(1, 0.5f);

		set.addAnimation(animation);
		set.addAnimation(animation1);
		set.addAnimation(animation2);
		set.setDuration(800);
		set.setInterpolator(new AccelerateDecelerateInterpolator());
		set.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				animal_view.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				animal_view.setVisibility(View.GONE);

				// mDownloadNum.setVisibility(View.VISIBLE);
				// mDownloadNum.setText(String.valueOf(AppDownloadService.getDownloaders().size()));
				// view.setDrawingCacheEnabled(false);
			}
		});
		if (null == view.getDrawingCache())
			return;
		Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);

		/*
		 * View view3 = new View(inflater.getContext()); view3.layout(0, 1000,
		 * 500, 200);
		 */
		Drawable drawable = new BitmapDrawable(bmp);

		animal_view.setBackground(drawable);

		animal_view.setVisibility(View.VISIBLE);
		animal_view.startAnimation(set);

	}

	private void initData() {
		pageNum = 1;
		isLoadDataFinish = false;

		getNetData();
	}

	private void upDownLoadData(UpgradeListObject up_obj) {
		for (int i = 0; i < up_obj.getUpgradeApps().size(); i++) {
			DownloadData tmp_data = new DownloadData();
			upappListtem list = up_obj.getUpgradeApps().get(i);
			tmp_data.setApkId(list.getId());
			tmp_data.setApkDownloadPath(list.getDownloadURL());
			tmp_data.setApkLogoPath(list.getIcons().getPx256());
			tmp_data.setApkName(list.getTitle());
			tmp_data.setPackageName(list.getPackageName());

			tmp_data.setVersionCode(list.getVersionCode());
			tmp_data.setVersionName(list.getVersionName());
			down_data.add(tmp_data);
		}
	}

	private void getNetData() {
        //根据安装的应用检测是否可以升级和更新，并返回升级信息
		mUpMarketManager.getUpAppListItems(
				new DataResponse<UpgradeListObject>() {

					public void run() {
						// TODO Auto-generated method stub
						if (value != null) {

							Log.i(TAG, "the value=" + value.getCode());
							if (pageNum == 1) {
								obj = value;

								down_data.clear();
								upDownLoadData(value);
							} else {
								Log.v(TAG, "aurora.jiangmx enter getNetData()");
								int size = value.getUpgradeApps().size();
								if (size < rowCount)
									isLoadDataFinish = true;

								for (int i = 0; i < size; i++) {
									obj.getUpgradeApps().add(
											value.getUpgradeApps().get(i));
								}

								upDownLoadData(value);
							}
							disView();
						}
					}

				}, MarketUpdateActivity.this);

		mUpMarketManager.getUpdateCount(new DataResponse<upcountinfo>() {
			public void run() {
				if (value != null) {
					mAppSize = value.getCount();
//					 disCountView();
				}
			}

			private void disCountView() {
				// TODO Auto-generated method stub
				String lUpdateCount=String.format(getResources().getString(
						R.string.available_update_number), mAppSize);
				SpannableStringBuilder spannableStringBuilder=Utils.setTVColor(lUpdateCount
						,Config.LEFT_PARENTHESIS
						, Config.RIGHT_PARENTHESIS
						,getResources().getColor(R.color.aurora_red_text_color));
				mAvailUpdateNumBtn.setText(spannableStringBuilder);
			}
		}, MarketUpdateActivity.this);
	}

	private void disView() {
		if (pageNum == 1) {
			setAdapter();
			loadingPageUtil.hideLoadPage();
		} else {
			mAdapter.notifyDataSetChanged();
		}
		if (null == obj) {
			return;
		}

		mAppSize = obj.getUpgradeApps().size();
		String lUpdateCount=String.format(getResources().getString(
				R.string.available_update_number), mAppSize);
		SpannableStringBuilder spannableStringBuilder=Utils.setTVColor(lUpdateCount
				,Config.LEFT_PARENTHESIS
				, Config.RIGHT_PARENTHESIS
				,getResources().getColor(R.color.aurora_red_text_color));
		mAvailUpdateNumBtn.setText(spannableStringBuilder);

		updateDownloadBtnState();

		if (obj.getUpgradeApps().size() == 0) {
			mEmptyLayout.setVisibility(View.VISIBLE);
			mEmptyView.setText(R.string.page_empty_update);
			mEmptyImg.setBackground(getResources().getDrawable(
					R.drawable.nodownloaddata));
			mAppUpdateBtn.setVisibility(View.GONE);
		} else {
			mEmptyLayout.setVisibility(View.GONE);
			mAppUpdateBtn.setVisibility(View.VISIBLE);
		}

		if (obj.getUpgradeApps().size() < pageNum * rowCount) {
			return;
		}

		if (isLoadDataFinish) {

		}
	}

	private void setListener() {

		mEmptyBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(MarketUpdateActivity.this,
						MarketMainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});

		mAvailUpdateNumBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

		mUpdateSettingsBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent lInt = new Intent(MarketUpdateActivity.this,
						MarketUpdateSettingsActivity.class);
				startActivity(lInt);
			}
		});

		mAppUpdateBtn.setOnButtonClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!SystemUtils.isDownload(MarketUpdateActivity.this)) {

					AuroraAlertDialog mWifiConDialog = new AuroraAlertDialog.Builder(
							MarketUpdateActivity.this,
							AuroraAlertDialog.THEME_AMIGO_FULLSCREEN)
							.setTitle(getResources().getString(
											R.string.dialog_prompt))
							.setMessage(getResources().getString(
											R.string.no_wifi_download_message))
							.setNegativeButton(android.R.string.cancel, null)
							.setPositiveButton(android.R.string.ok,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

											SharedPreferences sp = PreferenceManager
													.getDefaultSharedPreferences(MarketUpdateActivity.this);
											Editor ed = sp.edit();
											ed.putBoolean("wifi_download_key",
													false);
											ed.commit();
											updateAllApps();

										}

									}).create();
					mWifiConDialog.show();
				} else {
					if(mAppUpdateBtn.getStatus()==DetailProgressBtn.STAUTS_UPDATELING){
						return;
					}
					AuroraAlertDialog oneKeyUpdate = new AuroraAlertDialog.Builder(
							MarketUpdateActivity.this,
							AuroraAlertDialog.THEME_AMIGO_FULLSCREEN)
							.setTitle(getResources().getString(
									R.string.dialog_prompt))
							.setMessage(getResources().getString(
									R.string.one_key_app_update))
							.setNegativeButton(android.R.string.cancel, null)
							.setPositiveButton(android.R.string.ok,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											updateAllApps();
										}
									}).create();
					oneKeyUpdate.show();
				}

			}
		});

		mAvailUpdateAppsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				DownloadData lDownloadData = new DownloadData();
				lDownloadData.setPackageName(obj.getUpgradeApps().get(position)
						.getPackageName());
				lDownloadData.setApkId(obj.getUpgradeApps().get(position)
						.getId());

				Intent lInt = new Intent(MarketUpdateActivity.this,
						MarketDetailActivity.class);
				lInt.putExtra("downloaddata", lDownloadData);
				MarketUpdateActivity.this.startActivity(lInt);
			}

		});

		mAvailUpdateAppsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, final View view, int i, long l) {
				AuroraAlertDialog ignoreAppUpdate = new AuroraAlertDialog.Builder(
						MarketUpdateActivity.this,
						AuroraAlertDialog.THEME_AMIGO_FULLSCREEN)
						.setTitle(getResources().getString(
								R.string.dialog_prompt))
						.setMessage(getResources().getString(
								R.string.ignore_app_update))
						.setNegativeButton(android.R.string.cancel, null)
						.setPositiveButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										if (null != view) {
											ProgressBtn lView = (ProgressBtn) view.findViewById(R.id.progressBtn);
											if (lView != null) {
												lView.setClickable(false);
												lView.setEnabled(false);
											}
										}
										mAvailUpdateAppsList.auroraDeleteSelectedItemAnim();
									}
								}).create();
				ignoreAppUpdate.show();

				return false;
			}
		});

		mAvailUpdateAppsList
				.auroraSetAuroraBackOnClickListener(new AuroraBackOnClickListener() {

					@Override
					public void auroraPrepareDraged(int position) {
						mAdapter.setmDeleteFlag(false);
					}

					@Override
					public void auroraOnClick(final int listViewPosition) {
						Log.i(TAG, "auroraOnClick: " + listViewPosition);
						mAvailUpdateAppsList.auroraDeleteSelectedItemAnim();
					}

					@Override
					public void auroraDragedUnSuccess(int position) {
						int lFirstVisiblePosition = mAvailUpdateAppsList
								.getFirstVisiblePosition();
						int lIndex = position - lFirstVisiblePosition;
						ProgressBtn lView = (ProgressBtn) mAvailUpdateAppsList
								.getChildAt(lIndex).findViewById(
										R.id.progressBtn);
						if (lView != null) {
							lView.setClickable(true);
							lView.setEnabled(true);
						}
					}

					@Override
					public void auroraDragedSuccess(int position) {
						if (null != mAvailUpdateAppsList.getChildAt(position)) {
							int firstVisiblePosition = mAvailUpdateAppsList
									.getFirstVisiblePosition();
							int vIndex = position - firstVisiblePosition;

							ProgressBtn lView = (ProgressBtn) mAvailUpdateAppsList
									.getChildAt(vIndex).findViewById(
											R.id.progressBtn);
							if (lView != null) {
								lView.setClickable(false);
								lView.setEnabled(false);
							}
						}
					}
				});

		mAvailUpdateAppsList
				.auroraSetDeleteItemListener(new AuroraDeleteItemListener() {

					@Override
					public void auroraDeleteItem(View view, int position) {

						mAdapter.setmDeleteFlag(true);

						mIgnoreAppAdapter.open();
						mIgnoreAppAdapter.insert(obj.getUpgradeApps().get(
								position));
						mIgnoreAppAdapter.close();

						String lUpdateCount = getResources().getString(
								R.string.available_update_number);

						obj.getUpgradeApps().remove(
								obj.getUpgradeApps().get(position));
						down_data.remove(down_data.get(position));
						mAdapter.notifyDataSetChanged();
						// mAdapter.notifyDataSetChanged();
						// mAdapter.notifyDataSetChanged();
						/*
						 * mAdapter = new
						 * AppUpdateAdapter(MarketUpdateActivity.this,
						 * obj.getUpgradeApps(), down_data,
						 * AppUpdateAdapter.UPDATE_APP);
						 * mAvailUpdateAppsList.setAdapter(mAdapter);
						 * mAvailUpdateAppsList.clearFocus();
						 */

						mAvailUpdateNumBtn.setText(String.format(lUpdateCount,
								--mAppSize));

						if (mAppSize == 0) {
							mEmptyLayout.setVisibility(View.VISIBLE);
							mEmptyView.setText(R.string.page_empty_update);
							mEmptyImg.setBackground(getResources().getDrawable(
									R.drawable.nodownloaddata));
						}
						Intent finish = new Intent(
								Globals.BROADCAST_ACTION_DOWNLOAD);
						sendBroadcast(finish);

						int lFirstVisiblePosition = mAvailUpdateAppsList
								.getFirstVisiblePosition();
						int lIndex = position - lFirstVisiblePosition;
						View lView = mAvailUpdateAppsList.getChildAt(lIndex)
								.findViewById(R.id.progressBtn);
						if (lView != null)
							lView.setEnabled(true);
					}
				});

	}

	private DownloadUpdateListener updateListener = new DownloadUpdateListener() {
		@Override
		public void downloadProgressUpdate() {
			if (mAdapter != null) {
				// adapter.notifyDataSetChanged();
				mAdapter.updateView(mAvailUpdateAppsList);
			}

			if (AppDownloadService.getDownloaders().size() == 0) {
				mDownloadNum.setVisibility(View.GONE);
			} else {
				mDownloadNum.setVisibility(View.VISIBLE);
				mDownloadNum.setText(String.valueOf(AppDownloadService
						.getDownloaders().size()));
			}

			updateDownloadBtnState();
		}
	};

	private void updateAllApps() {
		for (int i = 0; i < mAdapter.getCount(); i++) {

			AppDownloadService.startDownload(this, down_data.get(i));

		}
		mAppUpdateBtn.setEnabled(false);
//		mAppUpdateInfo.setText(R.string.app_updating_info);
		mAppUpdateBtn.setStatus(DetailProgressBtn.STAUTS_UPDATELING);
//		mAppUpdateBtn.setBackground(getResources().getDrawable(
//				R.drawable.button_general_pressed));

		     //updateDownloadBtnState();
	}

	/**
	* @Title: updateDownloadBtnState
	* @Description: 更新底部一键更新按钮状态
	* @param 
	* @return void
	* @throws
	 */
	private void updateDownloadBtnState() {
		
		boolean enable = false;
		Map<Integer, FileDownloader> lDownloaders = AppDownloadService
				.getDownloaders();
		mAppUpdateBtn.setEnabled(true);
		allSize=0;
		if (lDownloaders != null && down_data != null &&
				AppInstallService.getInstalls() != null) {
			for (DownloadData data : down_data) {
				if (lDownloaders.get(data.getApkId()) == null) {	// 是否在下载列表中
					if (AppInstallService.getInstalls().get(data.getApkId()) == null) {   // 是否在安装列表中
						InstalledAppInfo info = InstallAppManager.getInstalledAppInfo(this, data.getPackageName());
						if (info == null || (info != null && info.getVersionCode() < data.getVersionCode()) ) {	// 由于安装完成到更新之前有个时间差，这里判断是否已经是最新版本了
							enable = true;
							break;
						}
					}
				}

			}
		}
		if (enable != updateBtnEnable) {
			updateBtnEnable = enable;
			
			if (updateBtnEnable) {
				mAppUpdateBtn.setEnabled(true);
				if(obj!=null&&obj.getUpgradeApps()!=null){
					for(int i=0;i<obj.getUpgradeApps().size();i++){
						allSize = allSize+obj.getUpgradeApps().get(i).getAppSize();
					}
				}
				mAppUpdateBtn.setAllSize(Utils.formatSize(allSize));
//				mAppUpdateInfo.setText(R.string.update_by_one_key);
				mAppUpdateBtn.setStatus(DetailProgressBtn.STAUTS_A_KEY_UPDATE);



//				mAppUpdateBtn.setBackground(getResources().getDrawable(
//						R.drawable.download_selector));
			} else{
				mAppUpdateBtn.setEnabled(false);
//				mAppUpdateInfo.setText(R.string.app_updating_info);
				mAppUpdateBtn.setStatus(DetailProgressBtn.STAUTS_UPDATELING);
//				mAppUpdateBtn.setBackground(getResources().getDrawable(
//						R.drawable.button_general_pressed));
			}
		}
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context pContext, Intent pIntent) {
			// TODO Auto-generated method stub
			if (Globals.MARKET_UPDATE_ACTION.equals(pIntent.getAction())) {
				initData();

			}
		}

	};

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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(MarketUpdateActivity.this,
					MarketManagerActivity.class);
			Intent intent1 = new Intent(MarketUpdateActivity.this,
					MarketMainActivity.class);
			ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			List<RunningTaskInfo> appTask = am.getRunningTasks(1);
			boolean ifFront = false;
			for (RunningTaskInfo app : appTask) {
				if (app.baseActivity
						.equals(intent1.getComponent())) {
					ifFront = true;
					break;
				}
			}
			if (ifFront) {
				finish();
				Log.i(TAG, "setmOnActionBarBackItemListener back1");
				return true;
			} else {

				finish();
				startActivity(intent);
				overridePendingTransition(
						R.anim.aurora_activity_close_enter,
						R.anim.aurora_activity_close_exit);
				Log.i(TAG, "setmOnActionBarBackItemListener back2");
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
