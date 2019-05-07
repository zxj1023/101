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

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import tran.com.android.talpa.app_core.glide.transformations.BlurTransformation;
import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.datauiapi.bean.appListtem;
import tran.com.android.tapla.gamecenter.market.MarketMainActivity;
import tran.com.android.tapla.gamecenter.market.activity.module.AppListActivity;
import tran.com.android.tapla.gamecenter.market.activity.module.AppRankingActivity;
import tran.com.android.tapla.gamecenter.market.model.DownloadData;
import tran.com.android.tapla.gamecenter.market.util.MiddleProgressBtnUtil;
import tran.com.android.tapla.gamecenter.market.util.ProgressBtnUtil;
import tran.com.android.tapla.gamecenter.market.util.SystemUtils;
import tran.com.android.tapla.gamecenter.market.widget.MiddleProgressBtn;
import tran.com.android.tapla.gamecenter.market.widget.ProgressBtn;
import tran.com.android.tapla.gamecenter.market.widget.RoundProgressView;
import universalimageloader.core.DisplayImageOptions;
import universalimageloader.core.ImageLoader;
import universalimageloader.core.display.FadeInBitmapDisplayer;
import universalimageloader.core.display.RoundedBitmapDisplayer;
import universalimageloader.core.imageaware.ImageViewAware;
import universalimageloader.core.listener.ImageLoadingListener;
import universalimageloader.core.listener.SimpleImageLoadingListener;

public class AppAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<appListtem> applist;
    private List<DownloadData> download;
    private MiddleProgressBtnUtil progressBtnUtil;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    // 图片加载工具
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions optionsImage;
    private DisplayImageOptions barnar;
    private boolean loadImage = true;
    private Context m_context;

    private static final int VIEW_COUNT = 2;
    public static final int BANNER_ITEM = 0;
    public static final int APP_ITEM = 1;

    public AppAdapter(Context context, List<appListtem> app_list, List<DownloadData> down_data) {
        inflater = LayoutInflater.from(context);

        this.applist = app_list;
        this.download = down_data;
        this.m_context = context;
        optionsImage = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_icon)
                .showImageForEmptyUri(R.drawable.default_icon)
                .showImageOnFail(R.drawable.default_icon)
                .displayer(new RoundedBitmapDisplayer(context.getResources().getDimensionPixelOffset(R.dimen.banner_icon_displayer)))
                .cacheInMemory(true).cacheOnDisk(true).build();
        barnar = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_screen_shot_bg)
                .showImageForEmptyUri(R.drawable.default_screen_shot_bg)
                .showImageOnFail(R.drawable.default_screen_shot_bg)
                .displayer(new RoundedBitmapDisplayer(context.getResources().getDimensionPixelOffset(R.dimen.banner_icon_displayer)))
                .cacheInMemory(true).cacheOnDisk(true).build();

        progressBtnUtil = new MiddleProgressBtnUtil();
    }

    @Override
    public int getCount() {
        int size = (download.size() < applist.size()) ? download.size() : applist.size();
        return size;
    }

    @Override
    public int getItemViewType(int position) {
        if ("banner".equals(applist.get(position).getType())) {
            return BANNER_ITEM;
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
        HolderBanner holderBanner = null;
        int type = getItemViewType(position);

        if (applist == null) {
            return null;
        }
        appListtem listitem = null;

        if (applist.size() > 0) {
            listitem = applist.get(position);
        }

        if (listitem == null) {
            return null;
        }

        DownloadData data = download.get(position);
        int itemType = this.getItemViewType(position);
        if (convertView == null) {
            //按当前所需的样式，确定new的布局
            if (itemType == BANNER_ITEM) {
                convertView = inflater.inflate(R.layout.item_app_banner, null);
                holderBanner = new HolderBanner();
                holderBanner.iv_icon_banner = (ImageView) convertView.findViewById(R.id.iv_icon_banner);
                convertView.setTag(holderBanner);
            } else {
                convertView = inflater.inflate(R.layout.item_app, null);
                holder = new Holder();
                holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tv_appname = (TextView) convertView
                        .findViewById(R.id.tv_appname);
                holder.rb_score = (RatingBar) convertView
                        .findViewById(R.id.rb_score);
                holder.tv_download_count = (TextView) convertView
                        .findViewById(R.id.tv_download_count);

                holder.tv_download_size = (TextView) convertView
                        .findViewById(R.id.tv_download_size);

                holder.progressBtn = (MiddleProgressBtn) convertView.findViewById(R.id.progressBtn);
                //holder.shadowBg = (ImageView) convertView.findViewById(R.id.blur);
                convertView.setTag(holder);
                holder.progressBtn.setTag(data.getApkId());
            }
        } else {
            //有convertView，按样式，取得不用的布局
            if (itemType == BANNER_ITEM) {
                holderBanner = (HolderBanner) convertView.getTag();
            } else {
                holder = (Holder) convertView.getTag();
            }
        }
        //设置资源
        if (itemType == BANNER_ITEM) {
            changeViewData(holderBanner, listitem, data);
        } else {
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
        ImageView shadowBg;
        TextView tv_appname;
        RatingBar rb_score;
        TextView tv_download_count;
        TextView tv_download_size;
        RoundProgressView round_progress_view;
        MiddleProgressBtn progressBtn;
    }

    static final class HolderBanner {
        ImageView iv_icon_banner;
    }

    private void changeViewData(HolderBanner holder, appListtem listitem, DownloadData data) {
        // 开始头像图片异步加载
        if (SystemUtils.isLoadingImage(inflater.getContext())) {
            if (loadImage) {
                imageLoader.displayImage(listitem.getPicURL(),
                        holder.iv_icon_banner, barnar, animateFirstListener);
            } else {
                imageLoader.displayImage1(listitem.getPicURL(),
                        new ImageViewAware(holder.iv_icon_banner), barnar, animateFirstListener, null, m_context, 2);
                /*imageLoader.displayImage(listitem.getIcons().getPx256(),
						holder.iv_icon, optionsImage, animateFirstListener);*/
            }
        } else {
            holder.iv_icon_banner.setImageResource(R.drawable.banner_default);
        }
        final ImageView view = holder.iv_icon_banner;
        view.setDrawingCacheEnabled(true);
    }

    private void changeViewData(final Holder holder, appListtem listitem, DownloadData data) {
        // 开始头像图片异步加载

        if (SystemUtils.isLoadingImage(inflater.getContext())) {
            if (loadImage) {
                imageLoader.displayImage(listitem.getIcons().getPx256(),
                        holder.iv_icon, optionsImage, animateFirstListener);
            } else {
                imageLoader.displayImage1(listitem.getIcons().getPx256(),
                        new ImageViewAware(holder.iv_icon), optionsImage, animateFirstListener, null, m_context, 2);
				/*imageLoader.displayImage(listitem.getIcons().getPx256(),
						holder.iv_icon, optionsImage, animateFirstListener);*/
            }
        } else {
            holder.iv_icon.setImageResource(R.drawable.default_icon);
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
                } else if (inflater.getContext() instanceof AppRankingActivity) {
                    ((AppRankingActivity) inflater.getContext())
                            .setAnimal1(view);
                } else if (inflater.getContext() instanceof AppListActivity) {
                    ((AppListActivity) inflater.getContext())
                            .setAnimal1(view);
                }
            }
        });
        //holder.iv_icon.setTag(listitem.getIcons().getPx256());

        holder.tv_appname.setText(listitem.getTitle());
        holder.tv_download_count.setText(listitem.getDownloadCountStr());

        holder.tv_download_size.setText(listitem.getAppSizeStr());

        holder.rb_score.setRating(listitem.getLikesRate());


//        //拿到初始图
//        Bitmap initBitmap = TalpaBitmapUtil.drawableToBitmap(m_context.getResources().getDrawable(R.drawable.layer_list_download_btn_shadow_white));
//        //处理得到模糊效果的图
//        Bitmap blurBitmap = TalpaBitmapUtil.blurBitmap(m_context, initBitmap, 20f);
//        holder.shadowBg.setImageBitmap(blurBitmap);
//          holder.shadowBg.setVisibility(View.GONE);

//        holder.shadowBg.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                holder.shadowBg.getViewTreeObserver().removeOnPreDrawListener(this);
//                holder.shadowBg.buildDrawingCache();
//                Bitmap bmp = holder.shadowBg.getDrawingCache();
//                TalpaBitmapUtil.blur(bmp, holder.shadowBg,m_context);
//                return true;
//            }
//        });

//        Glide.with(m_context).load(R.drawable.layer_list_download_btn_shadow_white)
//                .bitmapTransform(new BlurTransformation(m_context,80))
//                .into(holder.shadowBg);



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

            if (view.getTag() instanceof Holder) {
                Holder holder = (Holder) view.getTag();
                if (holder == null) {
                    continue;
                }
                changeViewData(holder, listitem, data);
            }
            if (view.getTag() instanceof HolderBanner) {
                HolderBanner holderBanner = (HolderBanner) view.getTag();
                if (holderBanner == null) {
                    continue;
                }
                changeViewData(holderBanner, listitem, data);
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

            if (view.getTag() instanceof Holder) {
                Holder holder = (Holder) view.getTag();
                if (holder == null) {
                    continue;
                }
                holder.progressBtn.setTag(0);
            }
        }
    }

}
