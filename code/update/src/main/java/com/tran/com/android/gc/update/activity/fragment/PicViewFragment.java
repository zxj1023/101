package com.tran.com.android.gc.update.activity.fragment;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tran.com.android.gc.update.R;
import com.tran.com.android.gc.update.activity.picbrowser.PictureViewActivity;
import com.tran.com.android.gc.update.util.BitmapUtil;
import com.tran.com.android.gc.update.util.PicBrowseUtils;
import com.tran.com.android.gc.update.util.SystemUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;


public class PicViewFragment extends Fragment {
    private String mImageUrl;
    private ImageView mImageView;
    private ImageView mAnimaImgV;
    private ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;
    private ImageView defaultImage;

    private int mAnimPos;

    // Add begin for animation
    private float mScreenWidth;
    private float mScreenHeight;
    private int mStatusHeight;
    private int[] mCurLocation;
    private int[] mCurDimension;
    private int mCurrentPos;
    // Add end


    public static PicViewFragment newInstance(String imageUrl, int position) {
        final PicViewFragment f = new PicViewFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        args.putInt("current_pos", position);

        //m_bitmap = bitmap;
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImageUrl = getArguments() != null ? getArguments().getString("url")
                : null;

        mCurrentPos = getArguments() != null ? getArguments().getInt("current_pos")
                : null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout fl = (RelativeLayout) inflater.inflate(
                R.layout.picture_view_item, null);
        mImageView = (ImageView) fl.findViewById(R.id.pic_view_image);
        if (mCurrentPos == PicBrowseUtils.getDefaultPicIndex()) {
            mAnimaImgV = (ImageView) fl.findViewById(R.id.anim_image);
            mImageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    startBackAnimation();
                }
            });
        }


        mAttacher = new PhotoViewAttacher(mImageView);

        mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                //((PictureViewActivity) getActivity()).controlToolViews();
                startBackAnimation();
            }
        });
        mAttacher.setScaleType(ScaleType.FIT_XY);
        progressBar = (ProgressBar) fl.findViewById(R.id.loading);
        defaultImage = (ImageView) fl.findViewById(R.id.default_image);
        setDefaultImageSize(fl);
        return fl;
    }

    /**
     * author lwx192606 2014-2-25 TODO 设置默认图的位置和大小
     *
     * @param parentView return void
     */
    private ImageView setDefaultImageSize(RelativeLayout parentView) {
        ImageView originalDefaultView = (ImageView) parentView
                .findViewById(R.id.default_image);
        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
                PictureViewActivity.screenWidth * 1 / 2,
                PictureViewActivity.screenWidth * 1 / 2);
        rl.addRule(RelativeLayout.CENTER_IN_PARENT);
        originalDefaultView.setLayoutParams(rl);
        return originalDefaultView;
    }

    @Override
    public void onDestroyView() {
        if (mAttacher != null) {
            mAttacher.cleanup();
            mAttacher = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mImageUrl == null) return;

        initAnimationUtils();
        // add pic browse animation

        Log.v("aurora.jiangmx fragment", "currentpos:  " + mCurrentPos);
/*		if(mCurrentPos == PicBrowseUtils.getDefaultPicIndex() ){
            Log.v("aurora.jiangmx fragment", "init animation");
		   		   
		    mAnimaImgV.setBackground(PicBrowseUtils.getImgVByIndex(mCurrentPos));
		   
		 
		}*/

        ImageView lImg;
        if (mCurrentPos == PicBrowseUtils.getDefaultPicIndex()) {
            if (SystemUtils.isLoadingImage(getActivity())) {
                ((PictureViewActivity) getActivity()).imageLoader.displayImage(
                        mImageUrl, mAnimaImgV,
                        ((PictureViewActivity) getActivity()).options,
                        new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                                defaultImage.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view,
                                                        FailReason failReason) {
                                String message = null;
                                switch (failReason.getType()) {
                                    case IO_ERROR:
                                        message = "下载错误";
                                        break;
                                    case DECODING_ERROR:
                                        message = "图片无法显示";
                                        break;
                                    case NETWORK_DENIED:
                                        message = "网络有问题，无法下载";
                                        break;
                                    case OUT_OF_MEMORY:
                                        message = "图片太大无法显示";
                                        break;
                                    case UNKNOWN:
                                        message = "未知的错误";
                                        break;
                                }
                                Log.i("henry", (getActivity() == null) + "");
                                if (getActivity() != null) {
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                }
                                defaultImage.setImageResource(R.drawable.big_pic_break);
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view,
                                                          Bitmap loadedImage) {
                                // aurora ukiliu add 2014-09-10 begin
                                if (loadedImage.getHeight() < loadedImage.getWidth()) {
                                    loadedImage = BitmapUtil.rotateBitmap(loadedImage, 90);
                                }
                                // aurora ukiliu add 2014-09-10 end
                                defaultImage.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);

                                mAnimaImgV.setVisibility(View.VISIBLE);
                                mAnimaImgV.setImageBitmap(loadedImage);
                                startPicBrowseAnim(loadedImage);

                            }

                        });
            }
        } else {
            if (SystemUtils.isLoadingImage(getActivity())) {
                ((PictureViewActivity) getActivity()).imageLoader.displayImage(
                        mImageUrl, mImageView,
                        ((PictureViewActivity) getActivity()).options,
                        new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                                defaultImage.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view,
                                                        FailReason failReason) {
                                String message = null;
                                switch (failReason.getType()) {
                                    case IO_ERROR:
                                        message = "下载错误";
                                        break;
                                    case DECODING_ERROR:
                                        message = "图片无法显示";
                                        break;
                                    case NETWORK_DENIED:
                                        message = "网络有问题，无法下载";
                                        break;
                                    case OUT_OF_MEMORY:
                                        message = "图片太大无法显示";
                                        break;
                                    case UNKNOWN:
                                        message = "未知的错误";
                                        break;
                                }
                                Log.i("henry", (getActivity() == null) + "");
                                if (getActivity() != null) {
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                }
                                defaultImage.setImageResource(R.drawable.big_pic_break);
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view,
                                                          Bitmap loadedImage) {
                                // aurora ukiliu add 2014-9-10 begin
                                if (loadedImage.getHeight() < loadedImage.getWidth()) {
                                    loadedImage = BitmapUtil.rotateBitmap(loadedImage, 90);
                                }
                                // aurora ukiliu add 2014-9-10 end
                                defaultImage.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);

                                mImageView.setAlpha(1f);
                                mImageView.setImageBitmap(loadedImage);
//	                      rotateBitmapIfNeeded(imageUri, loadedImage);
                                if (null == mAttacher) {
                                    mAttacher = new PhotoViewAttacher(mImageView);
                                    mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {

                                        @Override
                                        public void onPhotoTap(View arg0, float arg1, float arg2) {
                                            //((PictureViewActivity) getActivity()).controlToolViews();
//	                                  ((PictureViewActivity) getActivity()).finish();
                                            startBackAnimation();
                                        }
                                    });
                                }
                                mAttacher.update();
                            }

//	                  private void rotateBitmapIfNeeded(String imageUri, Bitmap loadedImage) {
//	                      int degree = BitmapUtil.readPictureDegree(imageUri);
//	                      if (degree > 0) {
//	                          loadedImage = BitmapUtil.rotateBitmap(loadedImage, degree);
//	                          mImageView.setImageBitmap(loadedImage);
//	                      }
//	                  }
                        });
            }
        }

    }


    // Add begin for animation
    private void initAnimationUtils() {

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;

        mStatusHeight = getStatusHeight(getActivity());

    }

    private void initAnimImgV() {
        mCurLocation = PicBrowseUtils.getCurImgVLoc(PicBrowseUtils.getDefaultPicIndex());
        mCurLocation[1] = mCurLocation[1] - mStatusHeight;

        mCurDimension = PicBrowseUtils.getCurImgVDimension(PicBrowseUtils.getDefaultPicIndex());

//        mAnimaImgV.setX(mCurLocation[0]);
//        mAnimaImgV.setY(mCurLocation[1]);

        mAnimaImgV.setLayoutParams(new RelativeLayout.LayoutParams(mCurDimension[0], mCurDimension[1]));
    }

    public void startPicBrowseAnim(final Bitmap pBt) {
        initAnimImgV();

        float startX = (mAnimaImgV.getX() + mCurDimension[0] * 1.0f / 2) / mCurDimension[0];
        float startY = (mAnimaImgV.getY() + mCurDimension[1] * 1.0f / 2) / mCurDimension[1];

        final float deltX = mScreenWidth / mCurDimension[0];
        final float deltY = (mScreenHeight - mStatusHeight) / mCurDimension[1];

        float moveX = mScreenWidth / 2 - (0 + mCurDimension[0] * 1.0f / 2);
        float moveY = (mScreenHeight - mStatusHeight) / 2 - (0 + mCurDimension[1] * 1.0f / 2);

        AnimationSet lSetAnimation = new AnimationSet(false);

        TranslateAnimation lTranAnim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, mCurLocation[0] * 1.0f / mCurDimension[0], Animation.RELATIVE_TO_SELF, moveX / mCurDimension[0],
                Animation.RELATIVE_TO_SELF, mCurLocation[1] * 1.0f / mCurDimension[1], Animation.RELATIVE_TO_SELF, moveY / mCurDimension[1]);

        ScaleAnimation lScaleAnim =
                new ScaleAnimation(1f, deltX, 1f, deltY,
                        Animation.RELATIVE_TO_SELF, startX, Animation.RELATIVE_TO_SELF, startY);

        Log.v("aurora.jiangmx startAnimation", "TranslateAnimation moveX/: " + moveX / mCurDimension[0] + "moveY/ : " + moveX / mCurDimension[1]);
        Log.v("aurora.jiangmx startAnimation", "ScaleAnimation deltX: " + deltX + "deltY : " + deltY + " startX: " + startX + " startY: " + startY);

        lSetAnimation.addAnimation(lScaleAnim);
        lSetAnimation.addAnimation(lTranAnim);
        lSetAnimation.setInterpolator(new AccelerateInterpolator());
//        lSetAnimation.setFillAfter(true);
        lSetAnimation.setDuration(300);
        lSetAnimation.setAnimationListener(new AnimationListener() {

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
                mAnimaImgV.setVisibility(View.GONE);
//                mImageView.setLayoutParams(new RelativeLayout.LayoutParams(
//                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                BitmapDrawable lBtDrawable = new BitmapDrawable(pBt);
                mImageView.setBackground(lBtDrawable);
                mImageView.setAlpha(1f);
            }
        });

        mAnimaImgV.startAnimation(lSetAnimation);

    }

    public void startBackAnimation() {

        int[] mDimension = PicBrowseUtils.getCurImgVDimension(mCurrentPos);
        int[] mLocation = PicBrowseUtils.getCurImgVLoc(mCurrentPos);

        int lWidth = mImageView.getWidth();
        int lHeight = mImageView.getHeight();

        float startX = lWidth * 1.0f / 2;
        float startY = lHeight * 1.0f / 2;

        float deltX = mDimension[0] / mScreenWidth;
        float deltY = mDimension[1] / (mScreenHeight - mStatusHeight);

        float lDisplayPicX = mImageView.getX();
        float lDisplayPicY = mImageView.getY();

        float moveX = (mLocation[0] * 1.0f + mDimension[0] * 1.0f / 2) - (lDisplayPicX + mImageView.getWidth() * 1.0f / 2);
        float moveY = (mLocation[1] + mDimension[1] * 1.0f / 2) - (lDisplayPicY + mStatusHeight + mImageView.getHeight() * 1.0f / 2);

        AnimationSet lSetAnimation = new AnimationSet(true);

        TranslateAnimation lTranAnim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, moveX / lWidth,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, moveY / lHeight);

        ScaleAnimation lScaleAnim =
                new ScaleAnimation(1f, deltX, 1f, deltY,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        AlphaAnimation lAlphaAnim =
                new AlphaAnimation(1, 0.8f);
        lAlphaAnim.setInterpolator(new DecelerateInterpolator());

//        lSetAnimation.setFillAfter(true);
        lSetAnimation.setDuration(300);
        lSetAnimation.addAnimation(lScaleAnim);
        lSetAnimation.addAnimation(lTranAnim);
//        lSetAnimation.addAnimation(lAlphaAnim);

        lSetAnimation.setAnimationListener(new AnimationListener() {

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
                mImageView.setAlpha(0f);
//                mImageView.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        getActivity().finish();
                    }
                }, 200);
            }
        });
        mImageView.startAnimation(lSetAnimation);

    }


    private static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject;
                localObject = localClass.newInstance();

                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (java.lang.InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return statusHeight;
    }


}
