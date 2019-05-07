package tran.com.android.tapla.gamecenter.market.activity.module;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tran.com.android.gc.lib.widget.AuroraActionBar;
import tran.com.android.gc.lib.widget.AuroraListView;
import tran.com.android.gc.lib.widget.AuroraListView.AuroraDeleteItemListener;

import tran.com.android.tapla.gamecenter.datauiapi.bean.UpgradeListObject;
import tran.com.android.tapla.gamecenter.datauiapi.bean.upappListtem;
import tran.com.android.tapla.gamecenter.datauiapi.implement.Command;
import tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse;
import tran.com.android.tapla.gamecenter.datauiapi.interf.INotifiableController;
import tran.com.android.tapla.gamecenter.datauiapi.interf.INotifiableManager;
import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.detail.MarketDetailActivity;
import tran.com.android.tapla.gamecenter.market.MarketMainActivity;
import tran.com.android.tapla.gamecenter.market.activity.BaseActivity;
import tran.com.android.tapla.gamecenter.market.adapter.AppUpdateAdapter;
import tran.com.android.tapla.gamecenter.market.db.IgnoreAppAdapter;
import tran.com.android.tapla.gamecenter.market.download.DownloadUpdateListener;
import tran.com.android.tapla.gamecenter.market.model.DownloadData;
import tran.com.android.tapla.gamecenter.download.AppDownloadService;
import tran.com.android.tapla.gamecenter.market.util.Globals;
import tran.com.android.tapla.gamecenter.market.util.LoadingPageUtil;
import tran.com.android.tapla.gamecenter.market.util.LoadingPageUtil.OnHideListener;
import tran.com.android.tapla.gamecenter.market.util.LoadingPageUtil.OnRetryListener;
import tran.com.android.tapla.gamecenter.market.util.LoadingPageUtil.OnShowListener;
import tran.com.android.tapla.gamecenter.market.util.Log;

public class MarketUpdateIgnoredActivity extends BaseActivity implements
		INotifiableController {

	private final static String TAG = "MarketUpdateIgnoredActivity";
	private AuroraActionBar mActionBar;

	private AppUpdateAdapter mAppUpAdapter;
	private UpgradeListObject obj = new UpgradeListObject();

	private ArrayList<upappListtem> mIgnoreApps;
	
	private boolean isLoadDataFinish = false;
	private boolean ifScroll = true;
	private int pageNum = 1;
	private int rowCount = 15;
	
    private List<DownloadData> down_data = new ArrayList<DownloadData>();
    private boolean stopFlag = false;

    private IgnoreAppAdapter mIgnoreAppAdapter;
    private LoadingPageUtil loadingPageUtil;
    
    private FrameLayout mFooterLayout;
    private TextView mIngnoredUpNum;
    private TextView mUpdateSettingsBtn;
    private TextView mUpdateAllAppsBtn;
    private AuroraListView mUpAppsIgnoredList;
    
    private RelativeLayout mHeaderLayout;
    private LinearLayout mEmptyLayout;
    private TextView mEmptyView;
    private ImageView mEmptyImg;
	private Button mEmptyBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAuroraContentView(R.layout.activity_app_update_page);

		initActionBar();
		initViews();

		setListener();
		
		initLoadingPage();
		
		initData();
		setAdapter();
	    
	}

	

    @Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

	private void initActionBar() {
		mActionBar = getAuroraActionBar();
		mActionBar.setTitle(R.string.update_apps_ignored);
	    mActionBar.setBackground(getResources().getDrawable(
	                R.drawable.actionbar1));
        // addAuroraActionBarItem(AuroraActionBarItem.Type.Add,
        // AURORA_NEW_MARKET);
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

    private void initViews() {
		mIngnoredUpNum = (TextView) findViewById(R.id.avail_update_num_btn);

		mUpdateSettingsBtn = (TextView) findViewById(R.id.update_settings_btn);
		mUpdateAllAppsBtn = (TextView) findViewById(R.id.update_all_apps_btn);
		mUpdateSettingsBtn.setVisibility(View.GONE);
		mUpdateAllAppsBtn.setVisibility(View.VISIBLE);

		mUpAppsIgnoredList = (AuroraListView) findViewById(R.id.avail_update_apps_list);
		mUpAppsIgnoredList.auroraSetNeedSlideDelete(false);
		mUpAppsIgnoredList.auroraEnableSelector(true);
		mUpAppsIgnoredList.setSelector(R.drawable.list_item_selector);
		mUpAppsIgnoredList.setFocusable(true);

		mFooterLayout = (FrameLayout) findViewById(R.id.download_footer);
		mFooterLayout.setVisibility(View.GONE);
		
		mHeaderLayout = (RelativeLayout) findViewById(R.id.header_view);
	    mEmptyLayout = (LinearLayout) findViewById(R.id.upempty_layout);
	    mEmptyView = (TextView) findViewById(R.id.upempty_view);
	    mEmptyImg = (ImageView) findViewById(R.id.upempty_bg);
		mEmptyBtn = (Button) findViewById(R.id.upempty_btn);

		mIgnoreAppAdapter = new IgnoreAppAdapter(this);
	}

	private void setListener() {

		mEmptyBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(MarketUpdateIgnoredActivity.this,
						MarketMainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});

		mUpdateAllAppsBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View pView) {
				// TODO Auto-generated method stub
				Log.v(TAG, "aurora.jiangmx enter onClick(): Update All Apps ");
				AlertDialog.Builder lDialog = new AlertDialog.Builder(
						MarketUpdateIgnoredActivity.this);
				lDialog.setTitle(R.string.ignored_dialog_title_tip);
				
				lDialog.setMessage(R.string.ignored_dialog_msg_content);
				lDialog.setNegativeButton(R.string.dialog_cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface pDialog,
									int arg1) {
								// TODO Auto-generated method stub
								pDialog.dismiss();
                            }
                            
                         });

				lDialog.setPositiveButton(R.string.dialog_confirm,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface pDialog,
									int arg1) {
								// TODO Auto-generated method stub
							    if( mIgnoreApps != null){
							      mIgnoreAppAdapter.open();  
							      for(int i = 0; i < mIgnoreApps.size(); i++){
							          mIgnoreAppAdapter.deleteDataById(mIgnoreApps.get(i).getPackageName());
							      }
							      mIgnoreAppAdapter.close();
							      mIgnoreApps.clear();
							      mAppUpAdapter.notifyDataSetChanged();
							    }
							    
							    updateIgnoreAppNum();
							    
								pDialog.dismiss();
							}
						});
                
				if( mIgnoreApps != null)
				  lDialog.show();
			}

		});

		mUpAppsIgnoredList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                    long arg3) {
                // TODO Auto-generated method stub
                DownloadData lDownloadData = new DownloadData();
                lDownloadData.setPackageName(mIgnoreApps.get(position).getPackageName());
                
                Intent lInt = new Intent(MarketUpdateIgnoredActivity.this, MarketDetailActivity.class);
                lInt.putExtra("downloaddata", lDownloadData);
                MarketUpdateIgnoredActivity.this.startActivity(lInt);
            	
            }
        });
		
		mUpAppsIgnoredList.auroraSetDeleteItemListener(new AuroraDeleteItemListener() {

			@Override
			public void auroraDeleteItem(View view, int position) {
				
				mAppUpAdapter.setmDeleteFlag(true);
				
				mIgnoreAppAdapter.open();
		        mIgnoreAppAdapter.deleteDataById(mIgnoreApps.get(position).getPackageName());
		        mIgnoreAppAdapter.close();
		        
		        mIgnoreApps.remove(position);
		        
		        updateIgnoreAppNum();
		        sendBroadcast(new Intent(Globals.MARKET_UPDATE_ACTION));

		        mAppUpAdapter.notifyDataSetChanged();
			}
		});
	}

	private void setAdapter() {
		mAppUpAdapter = new AppUpdateAdapter(this, mIgnoreApps, down_data,
				AppUpdateAdapter.UPDATE_APP_IGNORED);
		mUpAppsIgnoredList.setAdapter(mAppUpAdapter);
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
	                mUpAppsIgnoredList.setVisibility(View.GONE);
	            }
	        });
	        loadingPageUtil.setOnHideListener(new OnHideListener() {
	            @Override
	            public void onHide() {
	                mUpAppsIgnoredList.setVisibility(View.VISIBLE);
	            }
	        });
	        loadingPageUtil.showLoadPage();
	        loadingPageUtil.showLoading();
	    }

	
	private void initData() {
		pageNum = 1;
		isLoadDataFinish = false;
		getNetData();
	}

	private void getNetData() {
	    
        mIgnoreAppAdapter.open();
        mIgnoreApps = mIgnoreAppAdapter.queryAllData();
        mIgnoreAppAdapter.close();
	    disView();
        
        loadingPageUtil.hideLoadPage();
	}

	private void disView(){
	    
       String lUpdateCount = getResources().getString(
               R.string.update_apps_ignored_num);
       
       int lAppNum = mIgnoreApps == null? 0 : mIgnoreApps.size();
       
       mIngnoredUpNum.setText(String.format(lUpdateCount, 
               lAppNum));
       
       if (lAppNum == 0) {
            mHeaderLayout.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
            mEmptyView.setText(R.string.page_empty_ignore_upate);
            mEmptyImg.setBackground(
                    getResources().getDrawable(R.drawable.ignore_update_app_empty_bg));
            
       } else {
           mHeaderLayout.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
       }
	       
	}
	  
	@Override
	protected void onStart() {
	        // TODO Auto-generated method stub
	        super.onStart();
	        
	        if (stopFlag) {
	            updateListener.downloadProgressUpdate();
	            stopFlag = false;
	            mUpAppsIgnoredList.postInvalidate();
	        }
	        AppDownloadService.registerUpdateListener(updateListener);
	}

	@Override
	protected void onStop() {
	   // TODO Auto-generated method stub
	   super.onStop();
	        
	   stopFlag = true;
	   AppDownloadService.unRegisterUpdateListener(updateListener);
	}

    private DownloadUpdateListener updateListener = new DownloadUpdateListener() {
        @Override
        public void downloadProgressUpdate() {
            if (mAppUpAdapter != null) {
//            adapter.notifyDataSetChanged();
                mAppUpAdapter.updateView(mUpAppsIgnoredList);
            }
        }
    };

	
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
	
	public void removeIgnoreApp(View v) {
		if (v != null) {
			mUpAppsIgnoredList.auroraDeleteSelectedItemAnim(v);
		}
	}
	
	public void updateIgnoreAppNum(){
	    
	       String lUpdateCount = getResources().getString(
	               R.string.update_apps_ignored_num);
	       
	       int lAppNum = mIgnoreApps == null? 0 : mAppUpAdapter.getCount();
	       
	       mIngnoredUpNum.setText(String.format(lUpdateCount, 
	               lAppNum));
	       
	       if(lAppNum == 0){
	            mHeaderLayout.setVisibility(View.GONE);
	            mEmptyLayout.setVisibility(View.VISIBLE);
	            mEmptyView.setText(R.string.page_empty_ignore_upate);
	            mEmptyImg.setBackground(
	                    getResources().getDrawable(R.drawable.ignore_update_app_empty_bg));
	       }
	    
	}
}
