/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package universalimageloader.core.display;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import universalimageloader.core.imageaware.ImageViewAware;

/**
 * Can display bitmap with rounded corners. This implementation works only with ImageViews wrapped
 * in ImageViewAware.
 * <br />
 * This implementation is inspired by
 * <a href="http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/">
 * Romain Guy's article</a>. It rounds images using custom drawable drawing. Original bitmap isn't changed.
 * <br />
 * <br />
 * If this implementation doesn't meet your needs then consider
 * <a href="https://github.com/vinc3m1/RoundedImageView">this project</a> for usage.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.5.6
 */
public class RoundedBitmapDisplayer implements BitmapDisplayer {

	protected final int cornerRadius;
	protected final int margin;

	public RoundedBitmapDisplayer(int cornerRadiusPixels) {
		this(cornerRadiusPixels, 0);
	}

	public RoundedBitmapDisplayer(int cornerRadiusPixels, int marginPixels) {
		this.cornerRadius = cornerRadiusPixels;
		this.margin = marginPixels;
	}

	@Override
	public void display(Bitmap bitmap, universalimageloader.core.imageaware.ImageAware imageAware, universalimageloader.core.assist.LoadedFrom loadedFrom) {
		if (!(imageAware instanceof ImageViewAware)) {
			throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
		}

		imageAware.setImageDrawable(new RoundedDrawable(bitmap, cornerRadius, margin));
	}

	/** 反射倒影 */
	public Bitmap createReflectedImages(Bitmap originalImage)
	{
		final int reflectionGap = 4;
		final int Height = 200;
		int index = 0;

		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		float scale = Height / (float)height;

		Matrix sMatrix = new Matrix();
		sMatrix.postScale(scale, scale);
		Bitmap miniBitmap = Bitmap.createBitmap(originalImage, 0, 0,
				originalImage.getWidth(), originalImage.getHeight(), sMatrix, true);

		//是否原图片数据，节省内存
		originalImage.recycle();

		int mwidth = miniBitmap.getWidth();
		int mheight = miniBitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);			// 图片矩阵变换（从低部向顶部的倒影）
		Bitmap reflectionImage = Bitmap.createBitmap(miniBitmap, 0, mheight/2, mwidth, mheight/2, matrix, false);	// 截取原图下半部分
		Bitmap bitmapWithReflection = Bitmap.createBitmap(mwidth, (mheight + mheight / 2), Bitmap.Config.ARGB_8888);			// 创建倒影图片（高度为原图3/2）

		Canvas canvas = new Canvas(bitmapWithReflection);	// 绘制倒影图（原图 + 间距 + 倒影）
		canvas.drawBitmap(miniBitmap, 0, 0, null);		// 绘制原图
		Paint paint = new Paint();
		canvas.drawRect(0, mheight, mwidth, mheight + reflectionGap, paint);		// 绘制原图与倒影的间距
		canvas.drawBitmap(reflectionImage, 0, mheight + reflectionGap, null);	// 绘制倒影图

		paint = new Paint();
		LinearGradient shader = new LinearGradient(0, miniBitmap.getHeight(), 0, bitmapWithReflection.getHeight()
				+ reflectionGap, 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
		paint.setShader(shader);	// 线性渐变效果
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));		// 倒影遮罩效果
		canvas.drawRect(0, mheight, mwidth, bitmapWithReflection.getHeight() + reflectionGap, paint);		// 绘制倒影的阴影效果

		return bitmapWithReflection;
	}


	public static class RoundedDrawable extends Drawable {

		protected final float cornerRadius;
		protected final int margin;

		protected final RectF mRect = new RectF(),
				mBitmapRect;
		protected final BitmapShader bitmapShader;
		protected final Paint paint;

		public RoundedDrawable(Bitmap bitmap, int cornerRadius, int margin) {
			this.cornerRadius = cornerRadius;
			this.margin = margin;

			bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
			mBitmapRect = new RectF (margin, margin, bitmap.getWidth() - margin, bitmap.getHeight() - margin);
			
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setShader(bitmapShader);
		}

		@Override
		protected void onBoundsChange(Rect bounds) {
			super.onBoundsChange(bounds);
			mRect.set(margin, margin, bounds.width() - margin, bounds.height() - margin);
			
			// Resize the original bitmap to fit the new bound
			Matrix shaderMatrix = new Matrix();
			shaderMatrix.setRectToRect(mBitmapRect, mRect, Matrix.ScaleToFit.FILL);
			bitmapShader.setLocalMatrix(shaderMatrix);
			
		}

		@Override
		public void draw(Canvas canvas) {
			canvas.drawRoundRect(mRect, cornerRadius, cornerRadius, paint);
		}

		@Override
		public int getOpacity() {
			return PixelFormat.TRANSLUCENT;
		}

		@Override
		public void setAlpha(int alpha) {
			paint.setAlpha(alpha);
		}

		@Override
		public void setColorFilter(ColorFilter cf) {
			paint.setColorFilter(cf);
		}
	}
}
