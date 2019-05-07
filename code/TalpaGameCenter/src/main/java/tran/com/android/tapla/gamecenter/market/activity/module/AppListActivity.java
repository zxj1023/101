package tran.com.android.tapla.gamecenter.market.activity.module;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

import tran.com.android.gc.lib.app.AuroraActivity.OnSearchViewQuitListener;
import tran.com.android.gc.lib.widget.AuroraActionBar;
import tran.com.android.gc.lib.widget.AuroraCustomActionBar;
import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.datauiapi.implement.Command;
import tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse;
import tran.com.android.tapla.gamecenter.datauiapi.interf.INotifiableController;
import tran.com.android.tapla.gamecenter.datauiapi.interf.INotifiableManager;
import tran.com.android.tapla.gamecenter.market.activity.BaseActivity;
import tran.com.android.tapla.gamecenter.market.activity.fragment.AppListFragment;
import tran.com.android.tapla.gamecenter.settings.MarketManagerActivity;
import tran.com.android.tapla.gamecenter.market.util.Globals;
import tran.com.android.tapla.gamecenter.market.util.SearchUtils;

public class AppListActivity extends BaseActivity implements OnSearchViewQuitListener,
        INotifiableController {

    public static final String OPEN_TYPE = "open_type";
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_NAME = "category_name";
    public static final String SPECIAL_ID = "special_id";
    public static final String SPECIAL_NAME = "special_name";
    // 0主界面 1 新品 2排行 3分类 4.专题  5 必备  6 设计 7 专题主页 8 分类主页 9 必玩
    public static final int TYPE_NEW = 1;
    public static final int TYPE_RANK = 2;
    public static final int TYPE_CATEGORY = 3;
    public static final int TYPE_SPECIAL = 4;
    public static final int TYPE_STARTER = 5;
    public static final int TYPE_DESIGN = 6;
    public static final int TYPE_SPECIAL_MAIN = 7;
    public static final int TYPE_CATEGORY_MAIN = 8;
    private AppListFragment appListFragment;

    private AuroraCustomActionBar mActionBar;
    //	public ImageView main_update;
    public View installAllBtn;
    private int openType;
    private String categoryName;
    //	private int categoryId;
    private String categoryId;
    private String specialName;
    //	private int specialId;
    private String specialId;
    private SearchUtils mSearchUtils;
    private ImageView mSearchImgV;
    SharedPreferences oneKeyPref;
    int statusHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAuroraContentView(R.layout.activity_applist,
                AuroraActionBar.Type.NEW_COSTOM, true);
        initTransparentTheme();
        setStatusBarIconDark(true);

        getIntentData();
        initViews();
//        setContentViewBg();
        initActionBar();
        setListener();
        initdata();

        mSearchUtils = new SearchUtils();
        mSearchUtils.initSearchMode(this, statusHeight);

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

        if (this.isSearchviewLayoutShow()) {
            hideSearchviewLayout();
        }
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
        statusHeight = getStatusBarHeight();
        ViewGroup.LayoutParams params = linear.getLayoutParams();
        params.height = statusHeight;
        linear.setLayoutParams(params);
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

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        mSearchUtils.removeSearchMode();
    }

    private void getIntentData() {
        openType = getIntent().getIntExtra(OPEN_TYPE, TYPE_STARTER);
        if (openType == TYPE_CATEGORY) {
            categoryName = getIntent().getStringExtra(CATEGORY_NAME);
            categoryId = getIntent().getStringExtra(CATEGORY_ID);
        } else if (openType == TYPE_SPECIAL) {
//			specialName = getIntent().getStringExtra(SPECIAL_NAME);
            specialId = getIntent().getStringExtra(SPECIAL_ID);
        }
    }

    public void setAnimal1(final ImageView view) {
        appListFragment.setAnimal1(view);
    }

    private void initViews() {
        installAllBtn = findViewById(R.id.download_btn_layout);
        String girls = getResources().getString(R.string.girls_language);
        if (specialName != null && !specialName.isEmpty() && specialName.equals(girls)) {
            oneKeyPref = getSharedPreferences(
                    Globals.SHARED_ONE_KEY_FOR_HER, Activity.MODE_PRIVATE);
            boolean enable = oneKeyPref.getBoolean(
                    "one_key_4her", true);
            if (enable) {
                installAllBtn.setVisibility(View.VISIBLE);
                installAllBtn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        appListFragment.installAll();
                        Animation animation;
                        animation = AnimationUtils.loadAnimation(AppListActivity.this, R.anim.aurora_menu_exit);
                        installAllBtn.startAnimation(animation);
                        installAllBtn.setVisibility(View.GONE);
                        oneKeyPref.edit().putBoolean("one_key_4her", false)
                                .commit();
                    }

                });
            } else {
                installAllBtn.setVisibility(View.GONE);
            }
        }

    }

    private void initActionBar() {
        mActionBar = getCustomActionBar();
        if (openType == TYPE_NEW) {
            mActionBar.setTitle(R.string.tab_new, Color.parseColor("#505050"));
        } else if (openType == TYPE_STARTER) {
            mActionBar.setTitle(R.string.tab_starter, Color.parseColor("#505050"));
        } else if (openType == TYPE_DESIGN) {
            mActionBar.setTitle(R.string.tab_design, Color.BLACK);
        } else if (openType == TYPE_SPECIAL) {
            mActionBar.setTitle(specialName, Color.parseColor("#505050"));
        } else if (openType == TYPE_RANK) {
            mActionBar.setTitle(R.string.tab_ranking, Color.parseColor("#505050"));
        } else {
            mActionBar.setTitle(categoryName);
        }
//		mActionBar.setBackgroundResource(R.drawable.actionbar_bg);

//        mActionBar.setDefaultOptionItemDrawable(getResources()
//                .getDrawable(
//                        R.drawable.btn_main_right_selector));

        mActionBar.changeActionBarBg(R.drawable.actionbar_touying);
        mActionBar.showDefualtItem(false);
        mActionBar.setOptionLayoutMarginRight(0, 16, 0, 0);
        mActionBar.addItemView(R.layout.actionbar_main_right_in_applist_acitivity);
        mActionBar.addItemView(R.layout.actionbar_search_item);
        mSearchImgV = (ImageView) mActionBar.findViewById(R.id.bar_search_item);
//		main_update = (ImageView) mActionBar.findViewById(R.id.actionbar_main_update);
        View view = mActionBar.findViewById(R.id.download_layout);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(AppListActivity.this,
                        MarketManagerActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initdata() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        int type = 0;
        type = openType;
        if (type == 4) {
            appListFragment = AppListFragment.newInstance(type, Globals.TYPE_APP,
                    specialId);
        } else {
            appListFragment = AppListFragment.newInstance(type, Globals.TYPE_APP,
                    categoryId);
        }
        ft.add(R.id.container, appListFragment);
        ft.commit();
    }

    private void setListener() {

	/*	mActionBar.setOnOptionItemClickListener(new onOptionItemClickListener() {
            @Override
			public void click(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AppListActivity.this,
						MarketManagerPreferenceActivity.class);
				startActivity(intent);
			}
		});*/

        mSearchImgV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                appListFragment.getmListView().auroraOnPause();
                mSearchUtils.startSearchMode();
            }
        });

        setOnSearchViewQuitListener(new OnSearchViewQuitListener() {

            @Override
            public boolean quit() {
                appListFragment.getmListView().auroraOnResume();
                return false;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (mSearchUtils.isInSearchMode()) {
                AppListActivity.this.hideSearchViewLayoutWithOnlyAlphaAnim();
                mSearchUtils.setSearchMode(false);
            }

        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWrongConnectionState(int state, INotifiableManager manager,
                                       Command<?> source) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onError(int code, String message, INotifiableManager manager) {
        // TODO Auto-generated method stub

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
        }
    };

    @Override
    public boolean quit() {
        // TODO Auto-generated method stub
        return false;
    }

}
