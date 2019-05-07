package tran.com.android.tapla.gamecenter.market.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import tran.com.android.gc.lib.app.AuroraActivity;
import tran.com.android.gc.lib.utils.DensityUtil;
import tran.com.android.gc.lib.widget.AuroraAutoCompleteTextView.OnDismissListener;
import tran.com.android.gc.lib.widget.AuroraCustomActionBar;
import tran.com.android.gc.lib.widget.AuroraSearchView;
import tran.com.android.gc.lib.widget.AuroraSearchView.OnQueryTextListener;
import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.datauiapi.ManagerThread;
import tran.com.android.tapla.gamecenter.datauiapi.SearchManager;
import tran.com.android.tapla.gamecenter.datauiapi.bean.RecommendationItem;
import tran.com.android.tapla.gamecenter.datauiapi.bean.SearchRecommend;
import tran.com.android.tapla.gamecenter.datauiapi.bean.SearchTimelyObject;
import tran.com.android.tapla.gamecenter.datauiapi.implement.Command;
import tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse;
import tran.com.android.tapla.gamecenter.datauiapi.interf.INotifiableController;
import tran.com.android.tapla.gamecenter.datauiapi.interf.INotifiableManager;
import tran.com.android.tapla.gamecenter.market.MarketMainActivity;
import tran.com.android.tapla.gamecenter.market.activity.module.AppListActivity;
import tran.com.android.tapla.gamecenter.market.activity.module.MarketSearchActivity;
import tran.com.android.tapla.gamecenter.market.adapter.PopQueryResultAdapter;

import tran.com.android.tapla.gamecenter.widget.FlowLayout;
import universalimageloader.core.display.FadeInBitmapDisplayer;
import universalimageloader.core.listener.SimpleImageLoadingListener;


public class SearchUtils implements INotifiableController{

    private final static String TAG = "SearchUtils";
    private SearchManager mSearchManager;
    private ManagerThread mSearchThread;

    private AuroraSearchView mSearchView;
    private ImageView mHintIcon;
    private ImageButton mCancelBtn;
    private String mQuery;
    private List<Map<String, Object>> mHistoryRecords = new ArrayList<Map<String, Object>>();
    private SharedPreferences mSharedPreference;
    private SearchRecommend recObj = new SearchRecommend();
    private SearchRecommend recObjCache = new SearchRecommend();
    private SearchTimelyObject m_searchobj = new SearchTimelyObject();
    public final static int SEARCH_REQUEST = 1;

    private FrameLayout mBackgroundSearchLayout;
    private FrameLayout mSearchViewBackground;
    private LinearLayout mSearchViewStateBar;
    private View mGotoSearchLayout;
    //	private GridView mRecGrid;
//    private LinearLayout mEssentialView;
//    private LinearLayout mDesignAwardView;
    //	private AuroraListView mHistoryListView;
    private LinearLayout mHistoryLayout;
    private TextView mHistoryTextView1;
    private TextView mHistoryTextView2;
    private TextView mHistoryTextView3;
    private ImageView mClearHistoryTxt;

//	private SimpleAdapter mHistoryAdapter;

    private AuroraActivity mActivity;
    private View mMaskLayer;
    private View mBottomPanel;

    private boolean isLoadDataFinish = false;

    private boolean isSearchMode = false;

    private final static int SHOW_SIZE_LIMIT = 6;
    private ListView mPopListView;
    private PopQueryResultAdapter mPopResultAdatper;
    private ArrayAdapter<String> mArrAdapter;
    private int mPopResultSize;
    private PopupWindow mPopWin;
    private boolean mIsShowPopWin = false;
    private int pageCount;
    private int currentPage;
    private int rowCount = 15;
    //	private SearchRecInfoAdapter lGvAdatper;
    private LinearLayout mRrecommendBtnLayout;
    private ImageView mRefreshBtn;
    private TextView mrecommendTextView1;
    private TextView mrecommendTextView2;
    private TextView mrecommendTextView3;
    private TextView mrecommendTextView4;
    private TextView mrecommendTextView5;
    private TextView mrecommendTextView6;
    private boolean isCacheData = false;
    int stateBarHeight;
    private boolean getIfCache = false;

    public void initSearchMode(final AuroraActivity pActivity, int height) {
        mActivity = pActivity;

        mSearchManager = new SearchManager();
        mSearchThread = new ManagerThread(mSearchManager);
        mSearchThread.market(this);
//        mSearchThread.market((INotifiableController) pActivity);

        mSharedPreference = mActivity.getSharedPreferences(
                Globals.HISTORY_RECORDS_FILENAME, Activity.MODE_PRIVATE);
        SharedPreferences sp1 = pActivity.getSharedPreferences(
                Globals.SHARED_DATA_UPDATE, pActivity.MODE_APPEND);
        String time = sp1.getString(Globals.SEARCH_RECOMMEND_DATA_CACHE_UPDATETIME,
                "0");
        if (time.equals(TimeUtils.getStringDateShort())) {
            isCacheData = true;
        }

        getIfCache = SystemUtils.getIfCache(mActivity,100,"recommend","","");

        stateBarHeight = height;
        initViews();
    }

    public void startSearchMode(final AuroraCustomActionBar mCustomActionBar) {

        //setCustomActionBarSearchListener(mCustomActionBar);
    }

    public void startSearchMode() {
        showSearchViewLayout();
        initPopWin();
    }

    private void updateHistory() {
        int lInsertPos = mSharedPreference.getInt(
                Globals.HISTORY_NEXT_INSERT_POSITION, 0);
        int lIndex;
        if (lInsertPos == Globals.HISTORY_MAX_LIMIT) {
            lIndex = lInsertPos - 1;
        } else {
            lIndex = lInsertPos;
        }
        String lHistoryRecord = null;

        Map<String, Object> lMap;
        mHistoryRecords.clear();
        for (int i = lIndex; i >= 0; i--) {

            lHistoryRecord = mSharedPreference.getString(
                    Globals.HISTORY_RECORDS + i, null);

            if (lHistoryRecord != null) {
                lMap = new HashMap<String, Object>();
                lMap.put("HISTORY_RECORD", lHistoryRecord);
                mHistoryRecords.add(lMap);
            }
        }

        updateHistoryLayout(mHistoryRecords);
        if (mHistoryRecords.size() == 0) {
           mHistoryLayout.setVisibility(View.GONE);
        } else {
            mHistoryLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showSearchViewLayout() {
        // initLabelAdapter();
        // setOnQueryTextListener(new svQueryTextListener());

        if (mActivity instanceof MarketMainActivity)
            mActivity.showSearchviewLayout();
        else
            mActivity.showSearchviewLayoutWithOnlyAlphaAnim();

        setSearchMode(true);
        updateHistory();
        if (recObj.getRecommendations().size() == 0)
            getSearchRecData();
    }

    private void initViews() {
        // addSearchviewInwindowLayout();
        // mGotoSearchLayout = (LinearLayout) inflater.inflate(
        // R.layout.aurora_goto_search_mode, mListView, false)
//		mActivity.setSearchviewBarBackgroundResource(
//				R.drawable.talpa_actionbar_background,
//				R.drawable.talpa_actionbar_background);

        mSearchViewBackground = (FrameLayout) mActivity
                .getSearchViewGreyBackground();

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(0, mActivity.getResources().getDimensionPixelOffset(
                R.dimen.homepage_main_tab_height_min2) + stateBarHeight, 0, 0);//left top right button
        mSearchViewBackground.setLayoutParams(lp);

        mSearchViewStateBar = mActivity.getSearchStateBar();
        ViewGroup.LayoutParams params = mSearchViewStateBar.getLayoutParams();
        params.height = stateBarHeight;
        mSearchViewStateBar.setLayoutParams(params);


        mBackgroundSearchLayout = (FrameLayout) LayoutInflater.from(mActivity)
                .inflate(R.layout.activity_search_page, null, false);
        if (mSearchViewBackground != null) {
            mSearchViewBackground.addView(mBackgroundSearchLayout);
            // mSearchViewBackground.setBackgroundColor(mActivity.getResources().getColor(
            // R.color.progressRoundGreenColor));

//			mRecGrid = (GridView) mBackgroundSearchLayout
//					.findViewById(R.id.recommend_grid);
//
//			replacementLayout = (RelativeLayout) mBackgroundSearchLayout.findViewById(R.id.replacement_layout);

//            mDesignAwardView = (LinearLayout) mBackgroundSearchLayout.findViewById(R.id.design_award_view);

//            mEssentialView = (LinearLayout) mBackgroundSearchLayout.findViewById(R.id.essential_view);

//			mHistoryListView = (AuroraListView) mBackgroundSearchLayout
//					.findViewById(R.id.search_history_list);


            mRrecommendBtnLayout = (LinearLayout) mBackgroundSearchLayout
                    .findViewById(R.id.replacement_btn_layout);

            mrecommendTextView1 = (TextView) mBackgroundSearchLayout.findViewById(R.id.recommend_0);
            mrecommendTextView2 = (TextView) mBackgroundSearchLayout.findViewById(R.id.recommend_1);
            mrecommendTextView3 = (TextView) mBackgroundSearchLayout.findViewById(R.id.recommend_2);
            mrecommendTextView4 = (TextView) mBackgroundSearchLayout.findViewById(R.id.recommend_3);
            mrecommendTextView5 = (TextView) mBackgroundSearchLayout.findViewById(R.id.recommend_4);
            mrecommendTextView6 = (TextView) mBackgroundSearchLayout.findViewById(R.id.recommend_5);


            mHistoryLayout = (LinearLayout) mBackgroundSearchLayout
                    .findViewById(R.id.search_history_layout);

            mHistoryTextView1 = (TextView) mBackgroundSearchLayout.findViewById(R.id.history_0);
            mHistoryTextView2 = (TextView) mBackgroundSearchLayout.findViewById(R.id.history_1);
            mHistoryTextView3 = (TextView) mBackgroundSearchLayout.findViewById(R.id.history_2);

//			mHistoryListView.setSelector(R.drawable.list_item_selector);

            mClearHistoryTxt = (ImageView) mBackgroundSearchLayout
                    .findViewById(R.id.clear_history_btn);

            mRefreshBtn = (ImageView) mBackgroundSearchLayout
                    .findViewById(R.id.refresh_btn);

            mBottomPanel = (View) mBackgroundSearchLayout.findViewById(R.id.bottom_panel);

            mMaskLayer = (View) mBackgroundSearchLayout.findViewById(R.id.mark_layer);
            // mLabelListContainer =
            // mBackgroundSearchLayout.findViewById(R.id.label_list_container);
            // mLabelList = (LabelList) mBackgroundSearchLayout
            // .findViewById(R.id.label_list);
        }
        mSearchView = mActivity.getSearchView();
        mHintIcon = mSearchView.getHintIcon();
        //mActivity.setSearchViewAnimDuration(250);
        mSearchView.setTextColor(Color.parseColor("#505050"));
        mSearchView.setSearchViewBorder(R.drawable.search_bar_bg_talpa);
        mSearchView.setSearchIconDrawable(R.drawable.aurora_search_view);
        mSearchView.setDeleteButtonDrawable(R.drawable.searchcancel);


        mCancelBtn = mActivity.getSearchViewRightButton();
//		mCancelBtn.setBackground(mActivity.getResources().getDrawable(
//				R.drawable.button_white_normal));

//		if (mSearchView.getQuery().toString().trim().equals("")) {
//			mCancelBtn.setText(mActivity.getResources().getString(
//					R.string.dialog_cancel));
//		} else {
//			mCancelBtn.setText(mActivity.getResources().getString(
//					R.string.search_page_submit));
//		}
//
//		mCancelBtn.setTextColor(mActivity.getResources().getColor(
//				R.color.progressRoundGreenColor));
//		mCancelBtn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        mCancelBtn.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View pView, MotionEvent pEvent) {
                // TODO Auto-generated method stub
                switch (pEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mCancelBtn.getBackground().setAlpha(100);
                        break;
                    case MotionEvent.ACTION_UP:
                        mCancelBtn.getBackground().setAlpha(255);
                        break;

                }
                return false;
            }
        });
        if (null == mSearchView) {
            return;
        }
        if (mCancelBtn != null) {
            mCancelBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

//					if (mCancelBtn.getText().equals(
//							mActivity.getResources().getString(
//									R.string.dialog_cancel))) {
//
//					    if(mActivity instanceof MarketMainActivity){
//					       mActivity.hideSearchViewLayout(true);
//					    }
//					    else{
//						   mActivity.hideSearchViewLayoutWithOnlyAlphaAnim();
//					    }
//					    setSearchMode(false);
//						return;
//					}
//
//					mQuery = mSearchView.getQuery().toString();
//					saveSearchRecords();
//
//					if (mQuery.trim().equals("") || mQuery == null) {
//
//						Toast.makeText(mActivity, R.string.search_check_query,
//								Toast.LENGTH_SHORT).show();
//
//					} else {
//
//						startSearchActivity(mQuery);
//
//					}

                    if (mActivity instanceof MarketMainActivity) {
                        mActivity.hideSearchViewLayout(true);
                    } else {
                        mActivity.hideSearchViewLayoutWithOnlyAlphaAnim();
                    }
                    setSearchMode(false);
                    return;

                }
            });
        }
        setSearchListener();
        disSearchView(mActivity);
    }


    private void getSearchRecData() {
        mSearchManager.getSearchRecItems(
                new DataResponse<SearchRecommend>() {
                    public void run() {
                        if (value != null) {
                            recObj.getRecommendations().clear();
                            if (value.getRecommendations().size() > 0) {
                                isCacheData = true;
                                recObjCache = value;
                                if(value.getRecommendations().size()>6){
                                    mRefreshBtn.setVisibility(View.VISIBLE);
                                }else{
                                    mRefreshBtn.setVisibility(View.GONE);
                                }
                                pageCount = SystemUtils.getPageCount(value.getRecommendations(), SHOW_SIZE_LIMIT);
                                //一旦有数据变化，就将currentPage设置为首页
                                currentPage = 1;
                                List<RecommendationItem> list = SystemUtils.page(value.getRecommendations(), SHOW_SIZE_LIMIT, pageCount, currentPage);
                                recObj.getRecommendations().addAll(list);

//								lGvAdatper.notifyDataSetChanged();
//								replacementLayout.setVisibility(View.VISIBLE);
                                updateRecommendLayout(recObj.getRecommendations());
                            }
                        }
                    }
                }, mActivity, 100, "recommend", "", isCacheData);
    }

    private void disSearchView(Context pContext) {
//		lGvAdatper = new SearchRecInfoAdapter(pContext, (ArrayList<RecommendationItem>)
//				recObj.getRecommendations());
//
//		mRecGrid.setAdapter(lGvAdatper);

        updateRecommendLayout(recObj.getRecommendations());

//		mHistoryAdapter = new SimpleAdapter(mActivity, mHistoryRecords,
//				R.layout.search_page_history_item,
//				new String[] { "HISTORY_RECORD" },
//				new int[] { R.id.history_title });
//
//		mHistoryListView.setAdapter(mHistoryAdapter);

        updateHistoryLayout(mHistoryRecords);
    }

    public void updateHistoryLayout(List<Map<String, Object>> record) {
        if (record != null && record.size() > 0) {
            for (int i = 0; i < record.size(); i++) {
                if (i == 0) {
                    mHistoryTextView1.setText(record.get(i).get("HISTORY_RECORD").toString());
                    mHistoryTextView1.setVisibility(View.VISIBLE);
                } else if (i == 1) {
                    mHistoryTextView2.setText(record.get(i).get("HISTORY_RECORD").toString());
                    mHistoryTextView2.setVisibility(View.VISIBLE);
                } else if (i == 2) {
                    mHistoryTextView3.setText(record.get(i).get("HISTORY_RECORD").toString());
                    mHistoryTextView3.setVisibility(View.VISIBLE);
                }
            }
            mHistoryLayout.setVisibility(View.VISIBLE);
        } else {
            mHistoryLayout.setVisibility(View.GONE);
        }
    }


    public void updateRecommendLayout(List<RecommendationItem> record) {
        if(record != null && record.size()>0){
            mRrecommendBtnLayout.setVisibility(View.VISIBLE);
            loadRecommenData(record);
        }else{
            mRrecommendBtnLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 加载推荐数据
     * @param record
     */
    private void loadRecommenData(List<RecommendationItem> record) {
        for (int i = 0; i < record.size(); i++) {
            if (i == 0) {
                mrecommendTextView1.setText(record.get(i).getName());
            } else if (i == 1) {
                mrecommendTextView2.setText(record.get(i).getName());
            } else if (i == 2) {
                mrecommendTextView3.setText(record.get(i).getName());
            } else if (i == 3) {
                mrecommendTextView4.setText(record.get(i).getName());
            } else if (i == 4) {
                mrecommendTextView5.setText(record.get(i).getName());
            } else if (i == 5) {
                mrecommendTextView6.setText(record.get(i).getName());
            }
        }
    }


    private void getTimeLyNetData() {
        mSearchManager.getSearchTimelyItems(
                new DataResponse<SearchTimelyObject>() {
                    public void run() {
                        if (value != null) {
                            Log.i(TAG, "the value=" + value.getCode());
                            m_searchobj.getSuggestions().clear();
                            m_searchobj.getSuggestions().addAll(
                                    value.getSuggestions());

                            buildSearchPopWin();
                        }
                    }

                }, mActivity, mQuery);
    }


    private void initPopWin() {
        mMaskLayer.setVisibility(View.GONE);
        WindowManager wm = (WindowManager) mActivity
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        mSearchView.getQueryTextView().setThreshold(1);
        mSearchView.getQueryTextView().setDropDownHorizontalOffset(0);
        mSearchView.getQueryTextView().setDropDownVerticalOffset(
                DensityUtil.dip2px(mActivity, 6));
        mSearchView.getQueryTextView().setDropDownWidth(width);

        mSearchView.getQueryTextView().setDropDownBackgroundDrawable(
                mActivity.getResources().getDrawable(R.drawable.search_autotext_bg));
        mPopResultAdatper = new PopQueryResultAdapter(mActivity,
                R.layout.search_pop_list_item, R.id.pop_text,
                m_searchobj.getSuggestions(), mSearchView);
        mSearchView.getQueryTextView().setAdapter(mPopResultAdatper);

        mSearchView.getQueryTextView().setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                mMaskLayer.setVisibility(View.GONE);
            }
        });

    }

    private void buildSearchPopWin() {
        if (m_searchobj.getSuggestions().size() > 0) {
            mMaskLayer.setVisibility(View.VISIBLE);
        }
        if (null != mPopResultAdatper)
            mPopResultAdatper.notifyDataSetChanged();
    }

    private void setSearchListener() {

//		mRecGrid.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> pParent, View pView,
//									int pPos, long arg3) {
//				// TODO Auto-generated method stub
//				/*
//				 * TextView lRecApp = (TextView) pView
//				 * .findViewById(R.id.rec_app_name); mQuery =
//				 * lRecApp.getText().toString(); saveSearchRecords();
//				 * startSearchActivity(mQuery);
//				 */
//
//				// mActivity.hideSearchviewLayout();
//
//				RecommendationItem info = (RecommendationItem) mRecGrid.getAdapter()
//						.getItem(pPos);
//
////				DownloadData tmp = new DownloadData();
////				tmp.setPackageName(info.getPackageName());
////
////				Intent intent = new Intent(mActivity,
////						MarketDetailActivity.class);
//////				intent.putExtra("downloaddata", tmp);
////				mActivity.startActivity(intent);
//
//				startSearchActivity(info.getName());
//			}
//		});

        mrecommendTextView1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView recommend = (TextView) v;
                startSearchActivity(recommend.getText().toString());
            }
        });
        mrecommendTextView2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView recommend = (TextView) v;
                startSearchActivity(recommend.getText().toString());
            }
        });
        mrecommendTextView3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView recommend = (TextView) v;
                startSearchActivity(recommend.getText().toString());
            }
        });

        mrecommendTextView4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView recommend = (TextView) v;
                startSearchActivity(recommend.getText().toString());
            }
        });
        mrecommendTextView5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView recommend = (TextView) v;
                startSearchActivity(recommend.getText().toString());
            }
        });
        mrecommendTextView6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView recommend = (TextView) v;
                startSearchActivity(recommend.getText().toString());
            }
        });


//        mDesignAwardView.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent newIntent = new Intent(mActivity,
//                        AppListActivity.class);
//                newIntent.putExtra(AppListActivity.OPEN_TYPE,
//                        AppListActivity.TYPE_DESIGN);
//                mActivity.startActivity(newIntent);
//
//            }
//        });

//        mEssentialView.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent newIntent = new Intent(mActivity,
//                        AppListActivity.class);
//                newIntent.putExtra(AppListActivity.OPEN_TYPE,
//                        AppListActivity.TYPE_STARTER);
//                mActivity.startActivity(newIntent);
//
//            }
//        });

        mHistoryTextView1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView lHistorySearch = (TextView) v;
                startSearchActivity(lHistorySearch.getText().toString());
            }
        });
        mHistoryTextView2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView lHistorySearch = (TextView) v;
                startSearchActivity(lHistorySearch.getText().toString());
            }
        });
        mHistoryTextView3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView lHistorySearch = (TextView) v;
                startSearchActivity(lHistorySearch.getText().toString());
            }
        });

        mClearHistoryTxt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View pView) {
                // TODO Auto-generated method stub
                if (mSharedPreference == null) {
                    mSharedPreference = mActivity.getSharedPreferences(
                            Globals.HISTORY_RECORDS_FILENAME,
                            Activity.MODE_PRIVATE);
                }

                mSharedPreference.edit().clear().commit();

                mHistoryRecords.clear();
                updateHistoryLayout(mHistoryRecords);

//				mLayoutDivider0.setVisibility(View.GONE);
//				mLayoutDivider.setVisibility(View.GONE);

            }
        });
        mSearchView.getQueryTextView().setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (KeyEvent.KEYCODE_ENTER == keyCode
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    mQuery = mSearchView.getQueryTextView().getText()
                            .toString();
                    if(mQuery==null || mQuery.equals("")){
                        Toast.makeText(mActivity,v.getContext().getResources()
                                .getString(R.string.search_content_empty_tip),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    saveSearchRecords();

                    startSearchActivity(mQuery);
                    return true;
                }
                return false;
            }
        });
        mSearchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pQuery) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onQueryTextChange(String pQuery) {
                // TODO Auto-generated method stub
                mQuery = pQuery;
                if (mQuery.trim().equals("")) {
//					mCancelBtn.setText(mActivity.getResources().getString(
//							R.string.dialog_cancel));
                } else {
//					mCancelBtn.setText(mActivity.getResources().getString(
//							R.string.search_page_submit));

                    getTimeLyNetData();
                }

                return false;
            }
        });
        mSearchView.getQueryTextView().setOnItemClickListener(
                new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        TextView lPopQuery = (TextView) arg1
                                .findViewById(R.id.pop_text);
                        mQuery = lPopQuery.getText().toString();

                        if(mQuery==null || mQuery.equals("")){
                            Toast.makeText(mActivity, "搜索内容不能为空！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        saveSearchRecords();

                        mSearchView.getQueryTextView().setText(mQuery);
                        mSearchView.getQueryTextView().setSelection(
                                mQuery.length());
                        startSearchActivity(mQuery);

                    }
                });

        mBottomPanel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (mActivity instanceof MarketMainActivity) {
                    mActivity.hideSearchViewLayout(true);
                } else {
                    mActivity.hideSearchViewLayoutWithOnlyAlphaAnim();
                }
                setSearchMode(false);
            }
        });

        mRefreshBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage = currentPage + 1;
                //当指定页超过总数时，重新调转到第一页。
                if (currentPage > pageCount) {
                    currentPage = 1;
                }
                recObj.getRecommendations().clear();
                List<RecommendationItem> list = SystemUtils.page(recObjCache.getRecommendations(), SHOW_SIZE_LIMIT, pageCount, currentPage);
                recObj.getRecommendations().addAll(list);
                updateRecommendLayout(recObj.getRecommendations());
            }
        });

        mHintIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuery = mSearchView.getQueryTextView().getText()
                        .toString();

                if(mQuery==null || mQuery.equals("")){
                    Toast.makeText(v.getContext(), v.getContext().getResources()
                            .getString(R.string.search_content_empty_tip),Toast.LENGTH_SHORT).show();
                    return;
                }

                saveSearchRecords();

                startSearchActivity(mQuery);
            }
        });
    }

    public class SearchRecInfoAdapter extends BaseAdapter {

        private final static String TAG = "SearchRecInfoAdapter";
        private LayoutInflater inflater;
        private ImageView mRecAppAvatar;
        private TextView mRecAppText;
        private LinearLayout mRecommendLayout;
        private final int mHeight = mActivity.getResources().getDimensionPixelOffset(
                R.dimen.search_page_height_32);
        private final int maxWidth = mActivity.getResources().getDimensionPixelOffset(
                R.dimen.homepgae_maintabview_animation_start_point);
        private final int paddingLeft = mActivity.getResources().getDimensionPixelOffset(
                R.dimen.app_detail_page_margin_20);
        private final int paddingRight = mActivity.getResources().getDimensionPixelOffset(
                R.dimen.app_detail_page_margin_20);
//		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

//		private ImageLoader imageLoader = ImageLoader.getInstance();
//		private DisplayImageOptions optionsImage;

        private ArrayList<RecommendationItem> mRecList;

        public SearchRecInfoAdapter(Context pContext,
                                    ArrayList<RecommendationItem> pRecList) {
            mRecList = pRecList;
            inflater = LayoutInflater.from(pContext);
//			optionsImage = new DisplayImageOptions.Builder()
//					.showImageOnLoading(R.drawable.page_appicon_mostsmall)
//					.showImageForEmptyUri(R.drawable.page_appicon_mostsmall)
//					.showImageOnFail(R.drawable.page_appicon_mostsmall)
//					.cacheInMemory(true).cacheOnDisc(true).build();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mRecList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return mRecList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
//			return mRecList.get(arg0).getId();
            return arg0;
        }

        @Override
        public View getView(int pPos, View pView, ViewGroup pParent) {
            // TODO Auto-generated method stub
            Log.v(TAG, "aurora.jiangmx getView() pPos: " + pPos);
//			if (pView == null){
//				pView = inflater.inflate(
//						R.layout.activity_search_recommend_item, null);
//			}


//			mRecAppAvatar = (ImageView) pView.findViewById(R.id.rec_app_avatar);
//			if(SystemUtils.isLoadingImage(inflater.getContext()))
//			{
//				imageLoader.displayImage(mRecList.get(pPos).getIcons().getPx100(),
//						mRecAppAvatar, optionsImage, animateFirstListener);
//			}
//			mRecAppText = (TextView) pView.findViewById(R.id.rec_app_name);
//			mRecAppText.setText(mRecList.get(pPos).getName());
//			mRecommendLayout = (LinearLayout) pView.findViewById(R.id.recommend_layout);

            //            if(pPos == 0){
//				mRecommendLayout.setBackgroundColor(Color.parseColor("#E6ECFC"));
//
//            }else if(pPos==1){
//				mRecommendLayout.setBackgroundColor(Color.parseColor("#F9EAEB"));
//
//            }else if(pPos==2){
//				mRecommendLayout.setBackgroundColor(Color.parseColor("#FEEDE3"));
//            }else if(pPos==3){
//				mRecommendLayout.setBackgroundColor(Color.parseColor("#E3F7FE"));
//			}else if(pPos==4){
//				mRecommendLayout.setBackgroundColor(Color.parseColor("#E6ECFC"));
//			}else if(pPos==5){
//				mRecommendLayout.setBackgroundColor(Color.parseColor("#F9EAEB"));
//			}
            return pView;
        }
    }

    private static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    private void startSearchActivity(String pQuery) {

        Intent lInt = new Intent(mActivity, MarketSearchActivity.class);
        lInt.putExtra("query", pQuery);
        mActivity.startActivity(lInt);

        // ((MarketMainActivity)mActivity).startActivityForResult(lInt,
        // SEARCH_REQUEST);
    }

    private void saveSearchRecords() {

        if (!mQuery.trim().equals("") && mQuery != null) {
            if (mSharedPreference == null) {
                mSharedPreference = mActivity
                        .getSharedPreferences(Globals.HISTORY_RECORDS_FILENAME,
                                Activity.MODE_PRIVATE);
            }

            for (int i = 0; i < Globals.HISTORY_MAX_LIMIT; i++) {

                String lHistory = mSharedPreference.getString(
                        Globals.HISTORY_RECORDS + i, null);

                if (lHistory != null) {
                    if (lHistory.equals(mQuery))
                        return;
                }
            }

            int lInsertPos = mSharedPreference.getInt(
                    Globals.HISTORY_NEXT_INSERT_POSITION, 0);

            if (lInsertPos == Globals.HISTORY_MAX_LIMIT) {
                for (int i = 0; i < Globals.HISTORY_MAX_LIMIT - 1; i++) {
                    mSharedPreference
                            .edit()
                            .putString(
                                    Globals.HISTORY_RECORDS + i,
                                    mSharedPreference.getString(
                                            Globals.HISTORY_RECORDS + (i + 1),
                                            null)).commit();
                }
                mSharedPreference
                        .edit()
                        .putString(Globals.HISTORY_RECORDS + (lInsertPos - 1),
                                mQuery).commit();
            } else {
                mSharedPreference
                        .edit()
                        .putString(Globals.HISTORY_RECORDS + lInsertPos, mQuery)
                        .commit();
            }
            if (lInsertPos < Globals.HISTORY_MAX_LIMIT)
                lInsertPos++;

            mSharedPreference.edit()
                    .putInt(Globals.HISTORY_NEXT_INSERT_POSITION, lInsertPos)
                    .commit();
        }
    }

    // 设置当前主界面的搜索模式
    public void setSearchMode(boolean pIsSearchMode) {
        isSearchMode = pIsSearchMode;
        /*
		 * if (mListView != null) { mListView.setCanMoveHeadView(!isSearchMode);
		 * mListView.showHeadView(); }
		 */
    }

    public boolean isInSearchMode() {
        return isSearchMode;
    }

    public void removeSearchMode() {
        mSearchThread.quit();
        mSearchManager.setController(null);
    }


    @Override
    public void onWrongConnectionState(int state, INotifiableManager manager, Command<?> source) {
        if (recObj.getRecommendations().size() == 0 && getIfCache){
            isCacheData = true;
            getSearchRecData();
        }
    }

    @Override
    public void onError(int code, String message, INotifiableManager manager) {
        if (recObj.getRecommendations().size() == 0 && getIfCache){
            isCacheData = true;
            getSearchRecData();
        }
    }

    @Override
    public void onMessage(String message) {
    }

    @Override
    public void runOnUI(DataResponse<?> response) {
        mHandler.post(response);
    }

    private Handler mHandler = new Handler() {};
}

