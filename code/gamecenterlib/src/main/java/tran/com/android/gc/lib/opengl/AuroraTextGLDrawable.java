package tran.com.android.gc.lib.opengl;

import tran.com.android.gc.lib.view.AuroraGLSurfaceView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;


public class AuroraTextGLDrawable implements IAuroraGLDrawable {
	
	private IAuroraGLDrawable mDrawable;
	
	private Rect mTextRect = new Rect();
	
	public AuroraTextGLDrawable(AuroraGLSurfaceView view , int ResID ,Paint paint)
	{
		String str = view.getResources().getString(ResID);
		
		init(view,str,paint);
	}
	
	public AuroraTextGLDrawable(AuroraGLSurfaceView view , String str ,Paint paint)
	{
		init(view,str,paint);
	}
	
	private void init(AuroraGLSurfaceView view , String str ,Paint paint)
	{
		paint.getTextBounds(str, 0, str.length(), mTextRect);
		
		Bitmap bitmap = Bitmap.createBitmap(mTextRect.width(), mTextRect.height(), Bitmap.Config.ARGB_8888);
		
		Canvas canvas = new Canvas(bitmap);
		
		canvas.translate(0, mTextRect.height());
		
		canvas.drawText(str, 0, 0, paint);
		
		mDrawable = new AuroraGLDrawable(view , bitmap);
		
		bitmap.recycle();
	}
	
	@Override
	public void setAlpha(float alpha) {
		// TODO Auto-generated method stub
		mDrawable.setAlpha(alpha);
	}

	@Override
	public void setTransition(int x, int y) {
		// TODO Auto-generated method stub
		mDrawable.setTransition(x, y);
	}

	@Override
	public void setRotation(float angle) {
		// TODO Auto-generated method stub
		mDrawable.setRotation(angle);
	}
	
	public void setScale(float scaleX , float scaleY)
	{
		mDrawable.setScale(scaleX,scaleY);
	}
	

	@Override
	public void setMartix(int transX, int transY, float angle, float scaleX,
			float scaleY) {
		// TODO Auto-generated method stub
		mDrawable.setMartix(transX, transY, angle, scaleX, scaleY);
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		mDrawable.draw();
	}

	@Override
	public void onDestory() {
		// TODO Auto-generated method stub
		mTextRect = null;
		
		mDrawable.onDestory();
		
		mDrawable = null;
	}

	@Override
	public void resetBitmap(Bitmap bitmap) {
		// TODO Auto-generated method stub
		mDrawable.resetBitmap(bitmap);
	}
	
	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return mDrawable.getWidth();
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return mDrawable.getHeight();
	}
}
