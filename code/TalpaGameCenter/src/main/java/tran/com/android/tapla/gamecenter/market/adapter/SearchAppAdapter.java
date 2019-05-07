package tran.com.android.tapla.gamecenter.market.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import tran.com.android.tapla.gamecenter.datauiapi.bean.appListtem;
import tran.com.android.tapla.gamecenter.market.MarketMainActivity;
import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.market.activity.module.AppListActivity;
import tran.com.android.tapla.gamecenter.market.activity.module.AppRankingActivity;
import tran.com.android.tapla.gamecenter.market.model.DownloadData;
import tran.com.android.tapla.gamecenter.market.util.ProgressBtnUtil;
import tran.com.android.tapla.gamecenter.market.util.StringUtils;
import tran.com.android.tapla.gamecenter.market.util.SystemUtils;
import tran.com.android.tapla.gamecenter.market.widget.ProgressBtn;
import tran.com.android.tapla.gamecenter.market.widget.RoundProgressView;
import universalimageloader.core.DisplayImageOptions;
import universalimageloader.core.ImageLoader;
import universalimageloader.core.display.FadeInBitmapDisplayer;
import universalimageloader.core.display.RoundedBitmapDisplayer;
import universalimageloader.core.imageaware.ImageViewAware;
import universalimageloader.core.listener.ImageLoadingListener;
import universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SearchAppAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<appListtem> applist;
	private List<DownloadData> download;
	private ProgressBtnUtil progressBtnUtil;

	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	// 图片加载工具
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions optionsImage;
	private boolean loadImage = true;
	private Context m_context;
//	DecimalFormat df = new DecimalFormat("0.0");
	private static final int VIEW_COUNT = 2;
	public static final int SUPPLIER = 0;
	public static final int APP_ITEM = 1;

	public SearchAppAdapter(Context context, List<appListtem> app_list,
                            List<DownloadData> down_data) {
		inflater = LayoutInflater.from(context);

		this.applist = app_list;
		this.download = down_data;
		this.m_context = context;
		optionsImage = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_icon)
				.showImageForEmptyUri(R.drawable.default_icon)
				.showImageOnFail(R.drawable.default_icon)
				.displayer(new RoundedBitmapDisplayer(context.getResources().getDimensionPixelOffset(R.dimen.app_icon_displayer)))
				.cacheInMemory(true).cacheOnDisk(true).build();

		progressBtnUtil = new ProgressBtnUtil();
	}

	@Override
	public int getCount() {
		int size = (download.size()<applist.size())?download.size():applist.size();
		return size;
	}

	@Override
	public int getItemViewType(int position) {
		if ("supplier".equals(applist.get(position).getType()) ){
			return SUPPLIER;
		} else {
			return APP_ITEM;
		}
	}

	@Override
	public int getViewTypeCount() {
		return VIEW_COUNT;
	}

	@Override
	public DownloadData getItem(int position) {
		return download.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		int type = getItemViewType(position);

		if (applist == null) {
			return null;
		}
		appListtem listitem = null;

		if(applist.size() > 0) {
			listitem= applist.get(position);
		}

		if (listitem == null) {
			return null;
		}

		DownloadData data = download.get(position);
		int itemType = this.getItemViewType(position);
		if (convertView == null) {
			//按当前所需的样式，确定new的布局
			if(itemType == SUPPLIER){
				convertView = inflater.inflate(R.layout.item_app_supplier, null);
			}else{
				convertView = inflater.inflate(R.layout.item_app, null);
				holder = new Holder();
				holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
				holder.tv_appname = (TextView) convertView
						.findViewById(R.id.tv_appname);
				holder.rb_score = (RatingBar) convertView
						.findViewById(R.id.rb_score);
				holder.tv_download_count = (TextView) convertView
						.findViewById(R.id.tv_download_count);

				holder.progressBtn = (ProgressBtn) convertView
						.findViewById(R.id.progressBtn);
				convertView.setTag(holder);
				holder.progressBtn.setTag(data.getApkId());


				holder.tv_download_size = (TextView) convertView
						.findViewById(R.id.tv_download_size);
			}
		}else{
			//有convertView，按样式，取得不用的布局
			if(itemType != SUPPLIER){
				holder = (Holder) convertView.getTag();
			}
		}
		//设置资源
		if(itemType != SUPPLIER){
			changeViewData(holder, listitem, data);
		}

		return convertView;
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

	static final class Holder {
		ImageView iv_icon;

		TextView tv_appname;
		RatingBar rb_score;
		TextView tv_download_count;
		RoundProgressView round_progress_view;
		ProgressBtn progressBtn;
		TextView tv_download_size;
	}

	private void changeViewData(Holder holder, appListtem listitem, DownloadData data) {
		// 开始头像图片异步加载
		String appSize = null;
		String iconUrl = null;
		String downloadCount = null;
		int rate;

		if("9APPS".equals(listitem.getServerTag())){
			iconUrl = listitem.getIcon();
			appSize = SystemUtils.bytes2kb((long)listitem.getFileSize());
//			appSize = String.valueOf(listitem.getFileSize())+"KB";
			downloadCount = String.valueOf(listitem.getDownloadTotal());
			rate = listitem.getRate() / 2;
		}else{
			iconUrl = listitem.getIcons().getPx256();
			appSize = String.valueOf(listitem.getAppSizeStr());
			downloadCount = listitem.getDownloadCountStr();
			rate = listitem.getLikesRate();
		}

		if (SystemUtils.isLoadingImage(inflater.getContext()) && iconUrl != null) {
			if(loadImage){
				imageLoader.displayImage(iconUrl,
						holder.iv_icon, optionsImage, animateFirstListener);
			}else {
				imageLoader.displayImage1(iconUrl,
						new ImageViewAware(holder.iv_icon), optionsImage, animateFirstListener, null, m_context, 2);
			}
		} else {
			holder.iv_icon.setImageResource(R.drawable.page_appicon_big);
		}


		final ImageView view = holder.iv_icon;
		view.setDrawingCacheEnabled(true);
		holder.progressBtn.setOnButtonClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (inflater.getContext() instanceof MarketMainActivity) {
					((MarketMainActivity) inflater.getContext())
							.setAnimal1(view);
				}
				else if(inflater.getContext() instanceof AppRankingActivity) {
					((AppRankingActivity) inflater.getContext())
							.setAnimal1(view);
				}
				else if(inflater.getContext() instanceof AppListActivity) {
					((AppListActivity) inflater.getContext())
							.setAnimal1(view);
				}
			}
		});
		//holder.iv_icon.setTag(listitem.getIcons().getPx256());
		holder.tv_appname.setText(listitem.getTitle());
		holder.tv_download_count.setText(downloadCount);
		holder.tv_download_size.setText(appSize);
		holder.rb_score.setRating(rate);

		progressBtnUtil.updateProgressBtn(holder.progressBtn, data);
	}

	public void updateView(ListView listView) {
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

			if (position >= download.size()) {
				continue;
			}
			DownloadData data = download.get(position);
			appListtem listitem = applist.get(position);

			View view = listView.getChildAt(i);

			if(view.getTag() instanceof Holder){
				Holder holder = (Holder)view.getTag();
				if (holder == null) {
					continue;
				}
				changeViewData(holder, listitem, data);
			}
		}
	}

	public void setLoadImage(boolean loadImage) {
		this.loadImage = loadImage;
	}

	public void clearProgressBtnTag(ListView listView) {
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

			if (position >= download.size()) {
				continue;
			}

			DownloadData data = download.get(position);
			View view = listView.getChildAt(i);

			if(view.getTag() instanceof Holder){
				Holder holder = (Holder)view.getTag();
				if (holder == null) {
					continue;
				}
				holder.progressBtn.setTag(0);
			}
		}
	}
	public appListtem getAppListItem(int postion) {
		if (applist != null && !applist.isEmpty()) {
			return applist.get(postion);
		} else {
			return null;
		}
	}
}
