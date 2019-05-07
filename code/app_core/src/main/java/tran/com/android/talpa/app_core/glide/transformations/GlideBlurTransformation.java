package tran.com.android.talpa.app_core.glide.transformations;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.renderscript.RSRuntimeException;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import tran.com.android.talpa.app_core.glide.transformations.internal.FastBlur;
import tran.com.android.talpa.app_core.glide.transformations.internal.RSBlur;

/**
 * 说明：
 *
 * @auther lianbing.chen@itel-mobile.com
 * @date 2017/9/27 16:07
 */

public class GlideBlurTransformation extends BitmapTransformation {

    private static int MAX_RADIUS = 25;
    private static int DEFAULT_DOWN_SAMPLING = 1;

    private Context mContext;
    private BitmapPool mBitmapPool;

    private int mRadius;
    private int mSampling;


    public GlideBlurTransformation(Context context) {
        super(context);
        mContext = context.getApplicationContext();
    }

    public GlideBlurTransformation(BitmapPool bitmapPool) {
        super(bitmapPool);
    }

    public GlideBlurTransformation(Context context, int radius, int sampling) {
        super(context);
        mContext = context.getApplicationContext();
        mRadius = radius;
        mSampling = sampling;
    }

    @Override
    protected Bitmap transform(BitmapPool mBitmapPool, Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap source = toTransform;

        int width = source.getWidth();
        int height = source.getHeight();
        int scaledWidth = width / mSampling;
        int scaledHeight = height / mSampling;

        Bitmap bitmap = mBitmapPool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        canvas.scale(1 / (float) mSampling, 1 / (float) mSampling);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(source, 0, 0, paint);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            try {
                bitmap = RSBlur.blur(mContext, bitmap, mRadius);
            } catch (RSRuntimeException e) {
                bitmap = FastBlur.blur(bitmap, mRadius, true);
            }
        } else {
            bitmap = FastBlur.blur(bitmap, mRadius, true);
        }

        return BitmapResource.obtain(bitmap, mBitmapPool).get();
    }

    @Override
    public String getId() {
        return null;
    }
}
