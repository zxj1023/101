package tran.com.android.tapla.gamecenter.market.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import tran.com.android.gc.lib.widget.AuroraListView;

import tran.com.android.tapla.gamecenter.datauiapi.bean.upappListtem;
import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.settings.MarketUpdateActivity;
import tran.com.android.tapla.gamecenter.settings.MarketUpdateIgnoredActivity;
import tran.com.android.tapla.gamecenter.market.db.IgnoreAppAdapter;
import tran.com.android.tapla.gamecenter.market.model.DownloadData;
import tran.com.android.tapla.gamecenter.market.util.ProgressBtnUtil;
import tran.com.android.tapla.gamecenter.market.util.SystemUtils;
import tran.com.android.tapla.gamecenter.market.widget.ProgressBtn;
import universalimageloader.core.DisplayImageOptions;
import universalimageloader.core.ImageLoader;
import universalimageloader.core.display.FadeInBitmapDisplayer;
import universalimageloader.core.display.RoundedBitmapDisplayer;
import universalimageloader.core.listener.ImageLoadingListener;
import universalimageloader.core.listener.SimpleImageLoadingListener;

public class AppUpdateAdapter extends BaseAdapter {

	public final static int UPDATE_APP = 0;
	public final static int UPDATE_APP_IGNORED = 1;

	private List<upappListtem> mAppUpdateList;
	private LayoutInflater inflater;
	private int mPageType;
	private String mImgUrl;
	private Context mContext;

	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	// 图片加载工具
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions optionsImage;
	private ProgressBtnUtil progressBtnUtil;

	private List<DownloadData> mDownload;
	private List<DownloadData> down_data = new ArrayList<DownloadData>();
	
	public boolean mDeleteFlag = false;
	private int mTotalHeight;

	private IgnoreAppAdapter mIgnoreAppAdapter;
	private View mListItem;
	
	
	public AppUpdateAdapter(Context pContext, List<upappListtem> pAppList,
			List<DownloadData> pDownData, int pPageType) {
//		//假数据 rjz add
//		if(pAppList.size()==0){
//			upappListtem updpp=new upappListtem();
//			upappListtem updpp1=new upappListtem();
//			upappListtem updpp2=new upappListtem();
//			addTestData(updpp);
//			addTestData(updpp1);
//			addTestData(updpp2);
//			pAppList.add(updpp);
//			pAppList.add(updpp1);
//			pAppList.add(updpp2);
//			DownloadData tmp_data = new DownloadData();
//			DownloadData tmp_data1 = new DownloadData();
//			DownloadData tmp_data2 = new DownloadData();
//			downTestData(tmp_data);
//			downTestData(tmp_data1);
//			downTestData(tmp_data2);
//			pDownData.add(tmp_data);
//			pDownData.add(tmp_data1);
//			pDownData.add(tmp_data2);
//		}
	    mContext = pContext;
		inflater = LayoutInflater.from(pContext);
		mAppUpdateList = pAppList;
		mPageType = pPageType;

		mDownload = pDownData;
		mTotalHeight = Math.round(mContext.getResources().getDimension(
				R.dimen.app_update_list_item_height));
		optionsImage = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.page_appicon_big)
				.showImageForEmptyUri(R.drawable.page_appicon_big)
				.showImageOnFail(R.drawable.page_appicon_big)
				.displayer(new RoundedBitmapDisplayer(10)).cacheInMemory(true)
				.cacheOnDisc(true).build();

		progressBtnUtil = new ProgressBtnUtil();
		
		mIgnoreAppAdapter = new IgnoreAppAdapter(pContext);
	}

	private void downTestData(DownloadData tmp_data) {
		tmp_data.setApkId(/*list.getId()*/13254645);
		tmp_data.setApkDownloadPath(/*list.getDownloadURL()*/"https://tr-s3-mumbai-itel.s3.ap-south-1.amazonaws.com/20170921/07492575636.apk");
		tmp_data.setApkLogoPath("dsadsadasdas");
		tmp_data.setApkName("name dds");
		tmp_data.setPackageName(/*list.getPackageName()*/"com.android.mms");

		tmp_data.setVersionCode(/*list.getVersionCode()*/1708251003);
		tmp_data.setVersionName(/*list.getVersionName()*/"cccsss");
	}

	private void addTestData(upappListtem updpp) {
		updpp.setTitle("ppppll");
		updpp.setAppSizeStr("36.5M");
		updpp.setVersionName("lllll");
		updpp.setClientVersionName("llllClient");
		updpp.setVersionCode(52);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
	    
		return mAppUpdateList == null? 0 : mAppUpdateList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int pPosition, View pView, ViewGroup pViewGroup) {
		// TODO Auto-generated method stub

		Holder lHolder = null;
		final upappListtem lListItem = mAppUpdateList.get(pPosition);
		DownloadData lData = null;
		LinearLayout lSlidLayout = null;
		if(mPageType == UPDATE_APP)
		    lData = mDownload.get(pPosition);
		
		if (pView == null) {
			pView = inflater.inflate(
					R.layout.aurora_slid_listview, null);
			RelativeLayout front = (RelativeLayout) pView
					.findViewById(R.id.aurora_listview_front);
			inflater.inflate(R.layout.app_update_list_item,
					front);
			
			lSlidLayout = (LinearLayout)pView.findViewById(R.id.aurora_listview_back);
			//ImageView lImg = (ImageView)lSlidLayout.findViewById(R.id.aurora_rubbish);
			//lImg.setVisibility(View.GONE);
			lSlidLayout.addView(newIgnoreTextView());
			
			/*
			 * upappListtem lAppItem = mAppUpdateList.get(pPosition);
			 * 
			 * if(lAppItem != null){ mAppListItem = (UpMarketListViewItem)pView;
			 * mAppListItem.bind(lAppItem, pPosition, mPageType); }
			 */

			lHolder = new Holder();

			lHolder.mAppAvatar = (ImageView) pView
					.findViewById(R.id.app_avatar);
			lHolder.mAppName = (TextView) pView
					.findViewById(R.id.avail_upate_app_name);
			lHolder.mAppSize = (TextView) pView
					.findViewById(R.id.avail_app_size);
			lHolder.mUpMarketTxt = (TextView) pView
					.findViewById(R.id.up_market_txt);
			

			
			lHolder.mVersionUpdate = (TextView) pView
					.findViewById(R.id.avail_version_update);
			lHolder.mProgressBtn = (ProgressBtn) pView
					.findViewById(R.id.progressBtn);

			pView.setTag(lHolder);
            
			if(mPageType == UPDATE_APP){
				lHolder.mProgressBtn.setStatus(ProgressBtn.STAUTS_DOWNLOAD_UPDATE);
				lHolder.mProgressBtn.setTag(lData.getApkId());
			}
		} else {
			lHolder.mProgressBtn.setStatus(ProgressBtn.STATUS_NORMAL);
			lHolder = (Holder) pView.getTag();
		}
		
		mListItem = pView;
		final View vv = pView;
		
		changeViewData(lHolder, lListItem, lData);
		lHolder.mUpMarketTxt.setOnClickListener( new OnClickListener() { 
             
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    
//                  mIgnoreAppAdapter.open();
//                  mIgnoreAppAdapter.deleteDataById(mAppUpdateList.get(pPosition).getPackageName());
//                  mIgnoreAppAdapter.close();
//                  
//                  mAppUpdateList.remove(pPosition);
//                  notifyDataSetChanged();
//                  
//                  ((MarketUpdateIgnoredActivity)mContext).updateIgnoreAppNum();
//                  mContext.sendBroadcast(new Intent(Globals.MARKET_UPDATE_ACTION));
                  /*((MarketUpdateIgnoredActivity)mContext).addCoverView();*/
                	
              	
                	((MarketUpdateIgnoredActivity)mContext).removeIgnoreApp(vv);
                    
                }
            });
		
		/*
		 * lHolder.mProgressBtn.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub //slideview(view,view1,0,1000);
		 * 
		 * ((MarketUpdateActivity)inflater.getContext()).setAnimal1(view); } });
		 */
		
		if (mDeleteFlag) {
			resetListItemHeight(pView);
		}
		
		return pView;
	}

	private TextView newIgnoreTextView(){
	    TextView lIngnoreTxt = new TextView(mContext);
        lIngnoreTxt.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 
                LayoutParams.MATCH_PARENT));
        lIngnoreTxt.setGravity(Gravity.CENTER);
        lIngnoreTxt.setTextColor(Color.WHITE);
        lIngnoreTxt.setTextSize(16);
        lIngnoreTxt.setText(R.string.ignore_update_app);
        
        return lIngnoreTxt;
	}
	
	private void changeViewData(Holder pHolder, upappListtem pListItem,
			DownloadData pData) {
		final ImageView view = pHolder.mAppAvatar;
		view.setDrawingCacheEnabled(true);
		pHolder.mProgressBtn.setOnButtonClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((MarketUpdateActivity) inflater.getContext()).setAnimal1(view);
			}
		});
		mImgUrl = pListItem.getIcons().getPx256();
		if (SystemUtils.isLoadingImage(inflater.getContext())) {
			imageLoader.displayImage(mImgUrl, pHolder.mAppAvatar, optionsImage,
					animateFirstListener);
		} else {
			pHolder.mAppAvatar.setImageResource(R.drawable.page_appicon_big);
		}
		pHolder.mAppName.setText(pListItem.getTitle());

		pHolder.mVersionUpdate.setText(formatVersionUpdate(
		        pListItem.getClientVersionName(), pListItem.getVersionName()));

		pHolder.mAppSize.setText(pListItem.getAppSizeStr());

		if(mPageType == UPDATE_APP){
		   pHolder.mUpMarketTxt.setVisibility(View.GONE);
		   pHolder.mProgressBtn.setVisibility(View.VISIBLE);
		   progressBtnUtil.updateProgressBtn(pHolder.mProgressBtn, pData);
		}
		else{
		    pHolder.mProgressBtn.setVisibility(View.GONE);
		    pHolder.mUpMarketTxt.setVisibility(View.VISIBLE);
		}
	}

	public void updateView(AuroraListView listView) {
		if (listView == null) {
			return;
		}

		int headerCount = listView.getHeaderViewsCount();
		int firstVisiblePosition = listView.getFirstVisiblePosition();
		int offset = headerCount - firstVisiblePosition;
		boolean containerHeader = false;
		if (headerCount > 0) {
			if (firstVisiblePosition < headerCount) {
				containerHeader = true;
			}
		}
		int count = listView.getChildCount();

		for (int i = 0; i < count; i++) {
			int position = 0;
			if (containerHeader) {
				if (i < offset) {
					continue;
				}
				position = i - offset;
			} else {
				position = i + firstVisiblePosition - headerCount;
			}

			if (position >= mDownload.size()) {
				continue;
			}
			DownloadData data = mDownload.get(position);
			upappListtem listitem = mAppUpdateList.get(position);

			View view = listView.getChildAt(i);
			Holder holder = (Holder) view.getTag();

			if (holder == null) {
				continue;
			}

			changeViewData(holder, listitem, data);
		}
	}

	/**
	 * 
	 * @Title: formatVersionUpdate
	 * @Description: TODO()
	 * @param @param pNewVersion
	 * @param @param pOldVersion
	 * @param @return
	 * @return String
	 * @throws
	 */
	private String formatVersionUpdate(String pOldVersion, String pNewVersion) {

		SpannableStringBuilder lBuf = new SpannableStringBuilder();

		return pOldVersion + "→" + pNewVersion;
	}

	/**
	 * 
	 * @Title: formatAppSizeUpdate
	 * @Description: TODO()
	 * @param @param pNewSize
	 * @param @param pOldSize
	 * @param @return
	 * @return String
	 * @throws
	 */
	private CharSequence formatAppSizeUpdate(int pOldSize, int pNewSize) {

		SpannableStringBuilder lBuf = new SpannableStringBuilder();
		if (pOldSize != 0) {
			lBuf.append(pOldSize + "MB").setSpan(new StrikethroughSpan(), 0,
					lBuf.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			lBuf.append("  ");
		}

		lBuf.append(pNewSize + "MB");

		return lBuf;
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
	
	public void setmDeleteFlag(boolean mDeleteFlag) {
		this.mDeleteFlag = mDeleteFlag;
	}
	
	private void resetListItemHeight(View convertView) {
		Object convertTag = convertView.getTag();
		if (null == convertTag || !(convertTag instanceof Holder)) {
			return;
		}

		ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) convertView
				.getLayoutParams();
		ViewGroup.LayoutParams lpFront = (ViewGroup.LayoutParams) convertView
				.findViewById(R.id.aurora_listview_front)
				.getLayoutParams();
		LinearLayout.LayoutParams lpRubbish = (LinearLayout.LayoutParams) convertView
				.findViewById(R.id.aurora_listview_back)
				.getLayoutParams();

		if (null != lp && null != lpFront && null != lpRubbish) {
			lp.height = mTotalHeight + 1;
			lpFront.height = mTotalHeight;
			lpRubbish.height = mTotalHeight;
			convertView.findViewById(R.id.content).setAlpha(255);

		}
	}

	static final class Holder {
		ImageView mAppAvatar;

		TextView mAppName;
		TextView mVersionUpdate;
		TextView mAppSize;

		TextView mUpMarketTxt;
		ProgressBtn mProgressBtn;
	}

}
