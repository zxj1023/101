package tran.com.android.tapla.gamecenter.market;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import tran.com.android.gc.lib.app.AuroraActivity;
import tran.com.android.gc.lib.app.AuroraActivity.OnSearchViewQuitListener;
import tran.com.android.gc.lib.utils.LogUtils;
import tran.com.android.gc.lib.widget.AuroraActionBar;
import tran.com.android.gc.lib.widget.AuroraCustomActionBar;
import tran.com.android.gc.lib.widget.AuroraCustomActionBar.onSearchViewClickedListener;
import tran.com.android.gc.lib.widget.AuroraListView;
import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.datauiapi.ManagerThread;
import tran.com.android.tapla.gamecenter.datauiapi.MarketManager;
import tran.com.android.tapla.gamecenter.datauiapi.bean.MarketListObject;
import tran.com.android.tapla.gamecenter.datauiapi.bean.appListtem;
import tran.com.android.tapla.gamecenter.datauiapi.bean.topVideoItem;
import tran.com.android.tapla.gamecenter.datauiapi.implement.Command;
import tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse;
import tran.com.android.tapla.gamecenter.datauiapi.interf.INotifiableController;
import tran.com.android.tapla.gamecenter.datauiapi.interf.INotifiableManager;
import tran.com.android.tapla.gamecenter.download.NetStateService;
import tran.com.android.tapla.gamecenter.market.activity.BaseActivity;
import tran.com.android.tapla.gamecenter.market.activity.module.AppListActivity;
import tran.com.android.tapla.gamecenter.market.activity.module.AppRankingActivity;
import tran.com.android.tapla.gamecenter.market.activity.module.CategoryActivity;
import tran.com.android.tapla.gamecenter.detail.MarketDetailActivity;
import tran.com.android.tapla.gamecenter.market.activity.module.SpecialActivity;
import tran.com.android.tapla.gamecenter.market.adapter.AppAdapter;
import tran.com.android.tapla.gamecenter.market.db.CacheDataAdapter;
import tran.com.android.tapla.gamecenter.market.download.DownloadUpdateListener;
import tran.com.android.tapla.gamecenter.market.model.DownloadData;
import tran.com.android.tapla.gamecenter.download.AppDownloadService;
import tran.com.android.tapla.gamecenter.market.util.DataFromUtils;
import tran.com.android.tapla.gamecenter.market.util.FileLog;
import tran.com.android.tapla.gamecenter.market.util.Globals;
import tran.com.android.tapla.gamecenter.market.util.LoadingPageUtil;
import tran.com.android.tapla.gamecenter.market.util.LoadingPageUtil.OnHideListener;
import tran.com.android.tapla.gamecenter.market.util.LoadingPageUtil.OnRetryListener;
import tran.com.android.tapla.gamecenter.market.util.LoadingPageUtil.OnShowListener;
import tran.com.android.tapla.gamecenter.market.util.Log;
import tran.com.android.tapla.gamecenter.market.util.SearchUtils;
import tran.com.android.tapla.gamecenter.market.util.SystemUtils;
import tran.com.android.tapla.gamecenter.market.widget.FrameBannerView;
import tran.com.android.tapla.gamecenter.market.widget.MainTabView;
import tran.com.android.tapla.gamecenter.settings.MarketManagerActivity;

public class MarketMainActivity extends BaseActivity implements
        OnClickListener, OnSearchViewQuitListener, INotifiableController {

    private static final String TAG = "MainActivity";

    private LoadingPageUtil loadingPageUtil;

    private AuroraListView mListView;
    private AppAdapter adapter;
//	private MainListAdapter adapter;

    private MainTabView frameMainTabView;

    private View mPlaceHolderView;
    private FrameBannerView headerAd;

    private int mPlaceHolderViewHeight;

    private int mScaleMainTabViewHeight;

    private int maintabMinHeight;

    private boolean startCheck = false;
    private int listViewYOffset = 0;

    private AuroraActivity mActivity;
    private marketApp app = null;
    // 加载更多面板
    private LinearLayout loadMoreView;
    // 底面板加载更多字段控件
    private TextView forum_foot_more;
    private ProgressBar foot_progress;
    // 数据是否加载完毕
    private boolean isLoadDataFinish = false;

    private int pageNum = 1;
    private int rowCount = 30;

    private MarketListObject obj = new MarketListObject();
    //	private MainListObject obj = new MainListObject();
    private List<DownloadData> down_data = new ArrayList<DownloadData>();

    private boolean stopFlag = false;

    private View mGotoSearchLayout;
    private boolean isSearchMode = false;

    private MarketManager mmarketManager;
    private ManagerThread marketThread;

    private AuroraCustomActionBar mCustomActionBar;

    private View animal_view;
    private ValueAnimator valueAnimator;
    private SearchUtils mSearchUtils;
    private MyBroadcastReciver broadcastReceiver;
    private SharedPreferences app_update;
    private boolean isCacheData = false;
    private boolean getIfCache = false;
    private CacheDataAdapter ldb;
    private ImageView main_update;
    //AURORA UKILIU ADD 2014-10-20 BEGIN
    private SharedPreferences sp;
    //AURORA UKILIU ADD 2014-10-20 END
    int stateBarHeight;

    private FrameLayout frameLayout;
    private View view_header;

    private ImageView mActionbarMainManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // this.setTheme(R.style.Theme_Aurora_Dark_Transparent);
        super.onCreate(savedInstanceState);
        initPreference();
        /*
         * ed.putBoolean(Globals.SHARED_WIFI_DISCONNECT_ISEXITS, false);
		 * ed.putBoolean(Globals.SHARED_MOBILE_STATUS_ISDOWNLOAD, false);
		 * ed.putBoolean(Globals.SHARED_MOBILE_DISCONNECT_COUNT, false);
		 */
        File fl = new File(Environment.getExternalStorageDirectory()
                + "/markettest1234567890");
        if (fl.isDirectory()) {
            if (fl.listFiles().length == 1) {
                Globals.HTTP_REQUEST_URL = "http://" + fl.list()[0].toString()
                        + "/service";
            } else {
                Globals.HTTP_REQUEST_URL = Globals.HTTP_REQUEST_TEST_URL;
            }
        } else {
            Globals.HTTP_REQUEST_URL = Globals.HTTP_REQUEST_DEFAULT_URL;
        }
        /*
         * DisplayMetrics metric = new DisplayMetrics();
		 * getWindowManager().getDefaultDisplay().getMetrics(metric); int width
		 * = metric.widthPixels; // 屏幕宽度（像素） int height = metric.heightPixels;
		 * // 屏幕高度（像素） float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
		 * int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
		 */

        setAuroraContentView(R.layout.activity_main,
                AuroraActionBar.Type.NEW_COSTOM, true);
        setContentViewBg(R.drawable.bg1);
        initTransparentTheme();
        setStatusBarIconDark(true);
        mActivity = MarketMainActivity.this;
        // 在任何地方调用时都这么写.
        app = (marketApp) this.getApplication();
        app.setInstance(this);
        app.addActivity(mActivity);
        initViews();
        setAdapter();
        initActionBar();
        setListener();

        initLoadingPage();

        mmarketManager = new MarketManager();
        marketThread = new ManagerThread(mmarketManager);
        marketThread.market(this);

        mSearchUtils = new SearchUtils();
        mSearchUtils.initSearchMode(this, stateBarHeight);
        deleteCacheData();
        initdata();

        initBroadCast();
        //AURORA UKILIU ADD 2014-10-20 BEGIN
        sp = mActivity.getSharedPreferences(Globals.SHARED_FIRST_LAUNCH,
                Activity.MODE_PRIVATE);
        SharedPreferences perf = PreferenceManager
                .getDefaultSharedPreferences(mActivity);
        boolean bl = perf.getBoolean("first_launch", true);
        if (bl) {
//			perf.edit().putBoolean(Globals.SHARED_FIRST_LAUNCH_KEY, false).commit();
//			Intent newIntent = new Intent(mActivity,
//					AppListActivity.class);
//			newIntent.putExtra(AppListActivity.OPEN_TYPE,
//					AppListActivity.TYPE_STARTER);
//			mActivity.startActivity(newIntent);
        }
        //AURORA UKILIU ADD 2014-10-20 END
    }

    public void initTransparentTheme() {
        Window window = getWindow();//获取window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        initStatusBarHeight();
    }

    public void initStatusBarHeight() {
        LinearLayout linear = (LinearLayout) this.findViewById(R.id.top_bar);
        linear.setVisibility(View.VISIBLE);
        stateBarHeight = getStatusBarHeight();
        ViewGroup.LayoutParams params = linear.getLayoutParams();
        params.height = stateBarHeight;
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

    private void deleteCacheData() {
        new Thread() {
            @Override
            public synchronized void start() {
                // TODO Auto-generated method stub
                ldb = new CacheDataAdapter(MarketMainActivity.this);
                ldb.open();
                ldb.deleteDataByType(1000);
                ldb.close();
            }
        }.start();
    }

    private void initPreference() {
        SharedPreferences sp = getSharedPreferences(Globals.SHARED_WIFI_UPDATE,
                MODE_APPEND);
        app_update = getSharedPreferences(Globals.SHARED_APP_UPDATE,
                MODE_APPEND);
        int netstatus = SystemUtils.isNetStatus(this);
        Editor ed = sp.edit();
        ed.putInt(Globals.SHARED_NETSTATUS_KEY_ISEXITS, netstatus);
        ed.commit();

        SharedPreferences sp1 = getSharedPreferences(
                Globals.SHARED_DATA_UPDATE, MODE_APPEND);
        String time = sp1.getString(Globals.SHARED_DATA_CACHE_KEY_UPDATETIME,
                "0");

        SharedPreferences sp2 = getSharedPreferences(Globals.SHARED_CLEAR_CACHE,
                MODE_PRIVATE);
        boolean needInit = sp2.getBoolean("need_clear", true);
        if (needInit) {
            time = "0";
            sp2.edit().putBoolean("need_clear", false).commit();
        }

//		if (time.equals(TimeUtils.getStringDateShort())) {
//			isCacheData = true;
//		}

        getIfCache = SystemUtils.getIfCache(this, 0, "APP", "", "");
    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: initBroadCast
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    private void initBroadCast() {
        // TODO Auto-generated method stub
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Globals.BROADCAST_ACTION_DOWNLOAD);
        intentFilter.addAction(Globals.MARKET_UPDATE_ACTION);
        /*
         * intentFilter.addAction(Intent.ACTION_SCREEN_ON);
		 * intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		 */
        broadcastReceiver = new MyBroadcastReciver();
        this.registerReceiver(broadcastReceiver, intentFilter);
    }

    private class MyBroadcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "zhangwei the action1=" + action);
            if (action.equals(Globals.BROADCAST_ACTION_DOWNLOAD)
                    || action.equals(Globals.MARKET_UPDATE_ACTION)) {

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        getUpAppSign();
                    }
                }, 1000);

            }
			/*
			 * else if(action.equals(Intent.ACTION_SCREEN_ON)) {
			 * AutoUpdateService.pauseAutoUpdate(); } else
			 * if(action.equals(Intent.ACTION_SCREEN_OFF)) {
			 * AutoUpdateService.continueAutoUpdate(MarketMainActivity.this); }
			 */
        }

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

    public Bitmap convertViewToBitmap(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        return bitmap;
    }

    public void setAnimal1(final ImageView view) {

        main_update.setVisibility(View.GONE);
        Editor ed = app_update.edit();
        ed.putInt(Globals.SHARED_DOWNORUPDATE_KEY_ISEXITS, 1);
        ed.commit();
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
        TranslateAnimation animation = new TranslateAnimation(rect.left, width-90,
                rect.top,305);
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
                animal_view.setVisibility(view.GONE);
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
        animal_view.setVisibility(view.VISIBLE);
        animal_view.startAnimation(set);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (stopFlag) {
            updateListener.downloadProgressUpdate();
            stopFlag = false;
            mListView.postInvalidate();
        }
        AppDownloadService.registerUpdateListener(updateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (this.isSearchviewLayoutShow()) {
            hideSearchviewLayout();
        }

        stopFlag = true;
        AppDownloadService.unRegisterUpdateListener(updateListener);

        if (adapter != null) {
//			adapter.clearProgressBtnTag(mListView);
        }
    }

    @Override
    protected void onPause() {
        mListView.auroraOnPause();
        if (headerAd != null) {
            headerAd.stop();
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        mListView.auroraOnResume();
        if (headerAd != null && headerAd.getPic_dotype() != 2) {
            headerAd.start();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        headerAd.exit();
        marketThread.quit();
        mmarketManager.setController(null);

        mSearchUtils.removeSearchMode();

        app.setInstance(null);
        this.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean quit() {
        // 退出搜索模式的时候需要放出mListView的搜索头部
        if (mGotoSearchLayout != null) {
            mGotoSearchLayout.setVisibility(View.VISIBLE);
        }
        setSearchMode(false);
        mListView.auroraSetNeedSlideDelete(true);
        // initdata();
        return false;
    }

    private DownloadUpdateListener updateListener = new DownloadUpdateListener() {
        @Override
        public void downloadProgressUpdate() {
            if (adapter != null) {
                // adapter.notifyDataSetChanged();
                adapter.updateView(mListView);
            }
        }
    };

    /**
     * @param
     * @return void
     * @throws
     * @Title: setAdapter
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    private void setAdapter() {
        // TODO Auto-generated method stub
        adapter = new AppAdapter(this, obj.getApps(), down_data);
//		adapter = new MainListAdapter(this, obj.getApps());
        mListView.setAdapter(adapter);
    }

    private void initdata() {
        valueAnimator = new ValueAnimator();
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                if (mListView != null) {
                    mListView.setSelectionFromTop(0, -(Integer) animator.getAnimatedValue());
                }
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(1000);

        pageNum = 1;
        isLoadDataFinish = false;


        getNetData();
        /*new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();*/
    }

    private void upDownLoadData(MarketListObject m_obj) {
        for (int i = 0; i < m_obj.getApps().size(); i++) {
            DownloadData tmp_data = new DownloadData();
            appListtem list = m_obj.getApps().get(i);
            tmp_data.setApkId(list.getId());
            tmp_data.setApkDownloadPath(list.getDownloadURL());
            tmp_data.setApkLogoPath(list.getIcons().getPx256());
            tmp_data.setApkName(list.getTitle());
            tmp_data.setPackageName(list.getPackageName());
            tmp_data.setVersionCode(list.getVersionCode());
            tmp_data.setVersionName(list.getVersionName());
            tmp_data.setAction(list.getAction());
            tmp_data.setPicURL(list.getPicURL());
            tmp_data.setType(list.getType());
            tmp_data.setTo(list.getTo());
            down_data.add(tmp_data);
        }
    }

    private void getUpAppSign() {

        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                LogUtils.logd(TAG, "getUpAppSign");
                int count = AppDownloadService.getDownloaders().size();
                int sum = 0;
                if (count == 0) {
                    DataFromUtils up_data = new DataFromUtils();
                    sum = up_data.getUpdateSum(MarketMainActivity.this);

                }
                if ((sum > 0) || (count > 0)) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            main_update.setVisibility(View.VISIBLE);// changeby chenjie
                            Editor ed = app_update.edit();
                            ed.putInt(Globals.SHARED_DOWNORUPDATE_KEY_ISEXITS,
                                    1);
                            ed.commit();
                        }

                    });
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {

                            main_update.setVisibility(View.GONE);
                            Editor ed = app_update.edit();
                            ed.putInt(Globals.SHARED_DOWNORUPDATE_KEY_ISEXITS,
                                    0);
                            ed.commit();
                        }

                    });
                }

            }

        }.start();
    }

    private void getNetData() {
//		PramJson parmJson = new PramJson(pageNum,rowCount);
//		Gson gson = new Gson();
//		RetrofitHelper.getInstance(marketApp.getInstance()).createService(GameNetInterface.class).getMainPage(gson.toJson(parmJson)).enqueue(new Callback<MarketListObject>() {
//			@Override
//			public void onResponse(Call<MarketListObject> call, Response<MarketListObject> response) {
//				if (pageNum == 1) {
//					obj = response.body();
//					upDownLoadData(response.body());
//					// 没有banner时增加一个空的banner项
//					if (obj.getBanners() == null || obj.getBanners().size() == 0) {
//						topVideoItem de = new topVideoItem();
//						obj.getBanners().add(de);
//					}
//					headerAd.setImages(obj.getBanners());
//					getUpAppSign();
//				} else {
//					int size = response.body().getApps().size();
//					adapter.notifyDataSetChanged();
//					if (size < rowCount)
//						isLoadDataFinish = true;
//
//					for (int i = 0; i < size; i++) {
//						obj.getApps().add(response.body().getApps().get(i));
//					}
//					upDownLoadData(response.body());
//				}
//				disView();
//			}
//
//			@Override
//			public void onFailure(Call<MarketListObject> call, Throwable t) {
//
//			}
//		});


        mmarketManager.getMarketItems(new DataResponse<MarketListObject>() {
                                          public void run() {
                                              if (value != null) {
                                                  // Log.i(TAG, "the value=" + value.getCode());
                                                  loadingPageUtil.hideLoadPageFirstLoad(new Animator.AnimatorListener() {
                                                      @Override
                                                      public void onAnimationStart(Animator animator) {
                                                      }

                                                      @Override
                                                      public void onAnimationEnd(Animator animator) {
                                                          isCacheData = false;

                                                          if (pageNum == 1) {
                                                              obj = value;
                                                              upDownLoadData(value);
                                                              // 没有banner时增加一个空的banner项
                                                              if (obj.getBanners() == null || obj.getBanners().size() == 0) {
                                                                  topVideoItem de = new topVideoItem();
                                                                  obj.getBanners().add(de);
                                                              }
                                                              headerAd.setImages(obj.getBanners());
                                                              getUpAppSign();
                                                          } else {
                                                              int size = value.getApps().size();
                                                              adapter.notifyDataSetChanged();
                                                              if (size < rowCount)
                                                                  isLoadDataFinish = true;

                                                              for (int i = 0; i < size; i++) {
                                                                  obj.getApps().add(value.getApps().get(i));
                                                              }
                                                              upDownLoadData(value);
                                                          }
                                                          disView();
                                                      }

                                                      @Override
                                                      public void onAnimationCancel(Animator animator) {
                                                      }

                                                      @Override
                                                      public void onAnimationRepeat(Animator animator) {
                                                          Log.d("chenjie", "chenjie--onAnimationRepeat");
                                                      }
                                                  });
                                              }
                                          }
                                      }, MarketMainActivity.this, 0, "APP", "", pageNum, rowCount,
                isCacheData);
    }

    private void disView() {
        if (pageNum == 1) {
            Log.d("chenjie", "pageNum=1");
            setAdapter();
            loadingPageUtil.hideLoadPage();
            mListView.setVisibility(View.VISIBLE);
        } else {
            adapter.notifyDataSetChanged();
        }

        if (null == obj) {
            foot_progress.setVisibility(View.GONE);
            forum_foot_more.setText(R.string.all_loaded);
            return;

        }
        if (obj.getApps().size() < pageNum * rowCount) {
            foot_progress.setVisibility(View.GONE);
            forum_foot_more.setText(R.string.all_loaded);
            return;

        }
        if (isLoadDataFinish) {
            foot_progress.setVisibility(View.GONE);
            forum_foot_more.setText(R.string.all_loaded);
        }
    }

    private void initActionBar() {
        // mActionBar = getAuroraActionBar();
        mCustomActionBar = getCustomActionBar();
        mCustomActionBar.showSearchView(true, 0);
        // mCustomActionBar.setDefaultOptionItemDrawable(d);

        if (mCustomActionBar != null) {
            // action.setScrollDecorView(mListView);
            mCustomActionBar.setTitle(R.string.main_title, Color.WHITE);
            // mCustomActionBar.setIcon(R.drawable.header_logo);
            mCustomActionBar.showHomeIcon(false);
            mCustomActionBar.showDefualtItem(false);
            ImageButton searchBtn = (ImageButton) mCustomActionBar.findViewById(R.id.aurora_custom_action_bar_search_view_icon);
            searchBtn.setImageResource(R.drawable.vector_drawable_search_white_icon);
            searchBtn.setBackgroundResource(R.drawable.ripple_bg);
            searchBtn.setFocusable(true);
			/*mCustomActionBar.setDefaultOptionItemDrawable(getResources()
					.getDrawable(R.drawable.btn_main_right_selector));*/
            mCustomActionBar.addItemView(R.layout.actionbar_main_right);
            main_update = (ImageView) mCustomActionBar.findViewById(R.id.actionbar_main_update);

			mActionbarMainManager = (ImageView) mCustomActionBar.findViewById(R.id.actionbar_main_manager);
			View view = mCustomActionBar.findViewById(R.id.download_layout);
			view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
//					Intent intent = new Intent(MarketMainActivity.this,
//							MarketManagerPreferenceActivity.class);
                    Intent intent = new Intent(MarketMainActivity.this, MarketManagerActivity.class);
                    startActivity(intent);
                }
            });
        }

        mCustomActionBar.dealTitleClickEvent(false);
    }

    private void initViews() {
        setAnimal();

        frameMainTabView = (MainTabView) findViewById(R.id.frame_main_tab);

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

        mListView = (AuroraListView) findViewById(R.id.lv_app);
        mListView.setSelector(R.drawable.list_item_selector);
        mListView.setScrollbarFadingEnabled(true);
        mListView.auroraEnableOverScroll(false);
        mListView.auroraHideHeaderView();
        mListView.setVerticalScrollBarEnabled(false);
//		mListView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);

        mPlaceHolderView = getLayoutInflater().inflate(
                R.layout.view_header_placeholder, mListView, false);
        headerAd = (FrameBannerView) mPlaceHolderView
                .findViewById(R.id.banner_ad);
        frameLayout.addView(mPlaceHolderView);

        view_header = getLayoutInflater().inflate(
                R.layout.view_header, mListView, false);
//		mListView.addHeaderView(view_header);
        mListView.addHeaderView(view_header, null, false);

        loadMoreView = (LinearLayout) getLayoutInflater().inflate(
                R.layout.listview_footer, null);
        loadMoreView.setLongClickable(false);
        forum_foot_more = (TextView) loadMoreView
                .findViewById(R.id.listview_foot_more);
        foot_progress = (ProgressBar) loadMoreView
                .findViewById(R.id.listview_foot_progress);

        mListView.addFooterView(loadMoreView);

        maintabMinHeight = getResources().getDimensionPixelOffset(
                R.dimen.homepage_main_tab_height_min2);


        mPlaceHolderViewHeight = getResources().getDimensionPixelOffset(
                R.dimen.homepage_main_tab_margin_top);

        mScaleMainTabViewHeight = getResources().getDimensionPixelOffset(
                R.dimen.scale_homepage_main_tab_margin_height);

    }

    private void setListener() {

        loadMoreView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isLoadDataFinish)
                    return;
                if (foot_progress.getVisibility() == View.VISIBLE)
                    return;
                forum_foot_more.setText(R.string.loading);
                foot_progress.setVisibility(View.VISIBLE);
                getNetData();
            }
        });
	/*	mCustomActionBar
				.setOnOptionItemClickListener(new onOptionItemClickListener() {

					@Override
					public void click(View view) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MarketMainActivity.this,
								MarketManagerPreferenceActivity.class);
						startActivity(intent);
					}
				});*/

        mCustomActionBar
                .setOnSearchViewClickListener(new onSearchViewClickedListener() {

                    @Override
                    public void click(View view) {
                        Log.i(TAG, "onClick-----------------");

                        startCheck = false;
                        mListView.auroraOnPause();
                        mSearchUtils.startSearchMode();
                    }

                });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                DownloadData item = (DownloadData) mListView.getAdapter().getItem(position);
                if (item == null) {
                    return;
                }
                if ("banner".equals(item.getType())) {
                    if (item.getAction() == 1) {    //跳转到游戏详情
                        Intent intent = new Intent(mActivity,
                                MarketDetailActivity.class);
                        intent.putExtra("downloaddata", ((DownloadData) mListView
                                .getAdapter().getItem(position)));
                        startActivity(intent);

                    } else if (item.getAction() == 2) { //跳转到专题
                        Intent i = new Intent(mActivity, AppListActivity.class);
                        i.putExtra(AppListActivity.OPEN_TYPE, AppListActivity.TYPE_SPECIAL);
                        i.putExtra(AppListActivity.SPECIAL_ID, item.getTo());
                        mActivity.startActivity(i);
                    }
                } else if ("app".equals(item.getType())) {
                    Intent intent = new Intent(mActivity,
                            MarketDetailActivity.class);
                    intent.putExtra("downloaddata", ((DownloadData) mListView
                            .getAdapter().getItem(position)));
                    startActivity(intent);
                }
            }

        });

        mListView.setOnScrollListener(new OnScrollListener() {

            private int mFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) { //触屏的方式滚动屏幕
                    if (headerAd.isRunning()) {
                        headerAd.stop();
                    }

                    if (mListView != null && mListView.getChildCount() > 0) {
                        startCheck = true;
                    } else {
                        startCheck = false;
                    }
                    adapter.setLoadImage(false);
                } else if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) { //滑动停止
                    if (!headerAd.isRunning() && mFirstVisibleItem == 0) {
                        headerAd.start();
                    }

                    scrollToRightY();
                    adapter.setLoadImage(true);
                    adapter.notifyDataSetChanged();

                } else if (scrollState == OnScrollListener.SCROLL_STATE_FLING) {//惯性滑动
                    if (headerAd.isRunning()) {
                        headerAd.stop();
                    }
                    adapter.setLoadImage(false);
                }

                Log.i(TAG, "zhangwei the scollState=" + scrollState);
                if (isLoadDataFinish)
                    return;
                boolean scrollEnd = false;
                try {
                    if (view.getPositionForView(loadMoreView) == view
                            .getLastVisiblePosition()) {
                        scrollEnd = true;
                    }
                } catch (Exception e) {
                    // MyLog.i(TAG, e.toString());
                    scrollEnd = false;
                }

                if (obj.getApps().size() < pageNum * rowCount) {
                    isLoadDataFinish = true;
                    return;
                }

                if (scrollEnd) {
                    forum_foot_more.setText(R.string.loading);
                    foot_progress.setVisibility(View.VISIBLE);
                    pageNum++;
                    getNetData();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                //滚动时一直回调，直到停止滚动时才停止回调。单击时回调一次。
                mFirstVisibleItem = firstVisibleItem;
                if (mPlaceHolderViewHeight == 0) {
                    //mPlaceHolderViewHeight = view_header.getHeight();//表示view在屏幕上显示的高度， getMeasuredHeight表示view的实际大小
                }

                checkTabView(firstVisibleItem);
                changeBannerViewHeight(firstVisibleItem);
            }
        });

        setOnSearchViewQuitListener(new OnSearchViewQuitListener() {

            @Override
            public boolean quit() {
                startCheck = true;
                mListView.auroraOnResume();
                return false;
            }
        });

    }

    private void initLoadingPage() {
        loadingPageUtil = new LoadingPageUtil();
        loadingPageUtil.init(this, frameLayout);
        loadingPageUtil.setOnRetryListener(new OnRetryListener() {
            @Override
            public void retry() {
                initdata();
            }
        });
        loadingPageUtil.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow() {
                mListView.setVisibility(View.GONE);
                frameMainTabView.setVisibility(View.GONE);
            }
        });
        loadingPageUtil.setOnHideListener(new OnHideListener() {
            @Override
            public void onHide() {
                mListView.setVisibility(View.VISIBLE);
                frameMainTabView.setVisibility(View.VISIBLE);
            }
        });
        loadingPageUtil.showLoadPage();
        loadingPageUtil.showLoading();
    }

    // 判断当前主界面是否为搜索模式
    public boolean isSearchMode() {
        return isSearchMode;
    }

    // 设置当前主界面的搜索模式
    public void setSearchMode(boolean isSearchMode) {
        this.isSearchMode = isSearchMode;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_new:
                Intent newIntent = new Intent(MarketMainActivity.this,
                        AppListActivity.class);
                newIntent.putExtra(AppListActivity.OPEN_TYPE,
                        AppListActivity.TYPE_NEW);
                startActivity(newIntent);
                break;
            case R.id.tab_special:
                Intent specialIntent = new Intent(MarketMainActivity.this,
                        SpecialActivity.class);
                startActivity(specialIntent);
                break;
            case R.id.tab_ranking:
                Intent rankingIntent = new Intent(MarketMainActivity.this,
                        AppRankingActivity.class);
                startActivity(rankingIntent);
                break;
            case R.id.tab_category:
                Intent categoryIntent = new Intent(MarketMainActivity.this,
                        CategoryActivity.class);
                startActivity(categoryIntent);
                break;
        }
    }

    private void checkTabView(int firstVisibleItem) {
        if (!startCheck) {
            return;
        }

        if (firstVisibleItem == 0) {
            if (null == mListView.getChildAt(firstVisibleItem))
                return;

            if (-mListView.getChildAt(firstVisibleItem).getTop() < (mPlaceHolderViewHeight - maintabMinHeight)) {
                mListView.setScrollbarFadingEnabled(true);
                // mListView.auroraSetHeaderViewYOffset(0);

                if (listViewYOffset != -500) {
                    listViewYOffset = -500;
                    mListView.auroraSetHeaderViewYOffset(listViewYOffset);
                    mListView.auroraHideHeaderView();
                }
            } else {
                if (listViewYOffset != maintabMinHeight) {
                    listViewYOffset = maintabMinHeight;
                    mListView.auroraSetHeaderViewYOffset(listViewYOffset);
                    mListView.auroraShowHeaderView();
                }

                mListView.setScrollbarFadingEnabled(false);
                // mListView
                // .auroraSetHeaderViewYOffset(frame_tab_view.getHeight());
            }
        } else {
            mListView.setScrollbarFadingEnabled(false);
            // mListView.auroraSetHeaderViewYOffset(frame_tab_view.getHeight());
            if (listViewYOffset != maintabMinHeight) {
                listViewYOffset = maintabMinHeight;
                mListView.auroraSetHeaderViewYOffset(listViewYOffset);
                mListView.auroraShowHeaderView();
            }
        }
    }

    private int getScrollY() {
        View c = mListView.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = mListView.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mListView.getHeight();
        }
        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    float progress = 0;
    int oldScrollY = 0;

    private void changeBannerViewHeight(int firstVisibleItem) {
        // TODO change
        int scrollY = getScrollY();

        if (firstVisibleItem != 0 || scrollY >= mPlaceHolderViewHeight) {
            if (progress != 1) {
                progress = 1;
                scrollToRightY(scrollY);
                this.setProgress(progress, scrollY);
                mCustomActionBar.playSearchPanelAnimation(progress, mPlaceHolderViewHeight);
//				headerAd.setTranslationY(-progress * (headerAd.getHeight()));
                headerAd.setProgress(mPlaceHolderViewHeight);
                frameMainTabView.setProgress(progress, mPlaceHolderViewHeight);
                //	headerAd.setTransY(scrollY);
                changeActionBarMainManager();
            }
            return;
        }

        if (oldScrollY != scrollY) {
            oldScrollY = scrollY;

            if (scrollY < (mPlaceHolderViewHeight)) {
                progress = scrollY * 1.0f / (mPlaceHolderViewHeight);
                if (progress > 1) {
                    progress = 1;
                }
                this.setProgress(progress, scrollY);
                mCustomActionBar.playSearchPanelAnimation(progress, scrollY);
                headerAd.setProgress(scrollY);
                frameMainTabView.setProgress(progress, scrollY);
                changeActionBarMainManager();
            }
        }

    }

    public void changeActionBarMainManager() {
        if (progress == 1) {
            Drawable arrow = getResources().getDrawable(R.drawable.vector_drawable_manager_setting_btn);
            arrow.setAutoMirrored(true);
            mActionbarMainManager.setImageDrawable(arrow);
            mActionbarMainManager.setBackgroundResource(R.drawable.ripple_bg);
        } else {
            Drawable arrow = getResources().getDrawable(R.drawable.vector_drawable_manager_setting_white_btn);
            arrow.setAutoMirrored(true);
            mActionbarMainManager.setImageDrawable(arrow);
            mActionbarMainManager.setBackgroundResource(R.drawable.ripple_bg);
        }
    }

    private void scrollToRightY() {
        int scrollY = getScrollY();
        if (scrollY < (mPlaceHolderViewHeight + mScaleMainTabViewHeight)) {
            if (scrollY < (mPlaceHolderViewHeight) / 2) {
                if (scrollY != 0) {
                    if (valueAnimator != null) {
                        valueAnimator.cancel();

                        valueAnimator.setIntValues(scrollY, 0);
                        valueAnimator.start();
                    }
                }
            } else {
                if (scrollY != (mPlaceHolderViewHeight + mScaleMainTabViewHeight)) {
                    if (valueAnimator != null) {
                        valueAnimator.cancel();

                        valueAnimator.setIntValues(scrollY,
                                (mPlaceHolderViewHeight + mScaleMainTabViewHeight));
                        valueAnimator.start();
                    }
                }
            }
        }
    }

    private void scrollToRightY(int scrollY) {
        if (scrollY < (mPlaceHolderViewHeight)) {
            if (scrollY < (mPlaceHolderViewHeight) / 2) {
                if (scrollY != 0) {
                    if (valueAnimator != null) {
                        valueAnimator.cancel();

                        valueAnimator.setIntValues(scrollY, 0);
                        valueAnimator.start();
                    }
                }
            } else {
                if (scrollY != (mPlaceHolderViewHeight)) {
                    if (valueAnimator != null) {
                        valueAnimator.cancel();

                        valueAnimator.setIntValues(scrollY,
                                (mPlaceHolderViewHeight + mScaleMainTabViewHeight));
                        valueAnimator.start();
                    }
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mSearchUtils.isInSearchMode()) {
                Log.i(TAG, "zhangwei the back");
                MarketMainActivity.this.hideSearchViewLayout(true);
                mSearchUtils.setSearchMode(false);
                return true;
            } else {
                FileLog.delFile();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                return true;
            }
        }
        //FileLog.delFile();
        return super.onKeyDown(keyCode, event);
    }

    /**
     * (非 Javadoc) Title: onWrongConnectionState Description:
     *
     * @param state
     * @param manager
     * @param source
     * @see INotifiableController#onWrongConnectionState(int,
     * INotifiableManager,
     * Command)
     */
    @Override
    public void onWrongConnectionState(int state, INotifiableManager manager,
                                       Command<?> source) {
        // TODO Auto-generated method stub
        // foot_progress.setVisibility(View.GONE);
        // forum_foot_more.setText(R.string.no_connection_prompt);
        mHandler.sendEmptyMessage(Globals.NETWORK_ERROR);
    }

    /**
     * (非 Javadoc) Title: onError Description:
     *
     * @param code
     * @param message
     * @param manager
     * @see INotifiableController#onError(int,
     * java.lang.String,
     * INotifiableManager)
     */
    @Override
    public void onError(int code, String message, INotifiableManager manager) {
        // TODO Auto-generated method stub
        Log.i(TAG, "the code=" + code + "  msg=" + message);
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

    /**
     * (非 Javadoc) Title: onMessage Description:
     *
     * @param message
     * @see INotifiableController#onMessage(java.lang.String)
     */
    @Override
    public void onMessage(String message) {
        // TODO Auto-generated method stub
    }

    /**
     * (非 Javadoc) Title: runOnUI Description:
     *
     * @param response
     * @see INotifiableController#runOnUI(com
     * .aurora.datauiapi.data.implement.DataResponse)
     */
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
                        if (getIfCache) {
                            isCacheData = true;
                            getNetData();
                        } else {
                            loadingPageUtil.showNetworkError();
                        }
                    }
                    foot_progress.setVisibility(View.GONE);
                    forum_foot_more.setText(R.string.no_connection_prompt);
                    break;
                case Globals.NO_NETWORK:
                    if (loadingPageUtil.isShowing()) {
                        if (getIfCache) {
                            isCacheData = true;
                            getNetData();
                        } else {
                            loadingPageUtil.showNoNetWork();
                        }
                    }
                    foot_progress.setVisibility(View.GONE);
                    forum_foot_more.setText(R.string.no_connection_prompt);
                    break;
                default:
                    break;
            }
        }

    };

}
