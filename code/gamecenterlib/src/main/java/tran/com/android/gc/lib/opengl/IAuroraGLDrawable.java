package tran.com.android.gc.lib.opengl;

import android.graphics.Bitmap;

public interface IAuroraGLDrawable {
	
	public void setAlpha(float alpha);
	
	public void setTransition(int x, int y);
	
	public void setRotation(float angle);
	
	public void setScale(float scaleX , float scaleY);
	
	public void setMartix(int transX, int transY , float angle , float scaleX , float scaleY);
	
	public void draw();
	
	public void onDestory();
	
	public void resetBitmap(Bitmap bitmap);
	
	public int getWidth();
	
	public int getHeight();
}
