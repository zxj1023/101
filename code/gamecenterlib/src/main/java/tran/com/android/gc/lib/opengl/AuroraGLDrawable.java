package tran.com.android.gc.lib.opengl;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import tran.com.android.gc.lib.view.AuroraGLSurfaceView;
import android.app.ActivityManager;
import android.content.pm.ConfigurationInfo;

public class AuroraGLDrawable implements IAuroraGLDrawable{
	
	private AuroraGLSurfaceView mView;
	
	private AuroraTexture mTexture;
	
	private AuroraVertices mVertices;
	
	/**
	 * @drawable width
	 */
	public int mWidth;
	
	/**
	 * @drawable Height
	 */
	public int mHeight;
	
	/**
	 * @Drawable alpha
	 */
	private float mAlpha = 1;
	
	public int mTransitionX = 0;
	
	public int mTransitionY = 0;
	
	private float mAngle = 0;
	
	private float mScaleX = 1.0f;
	
	private float mScaleY = 1.0f;
	
	private int mBitmapWidth = 256;
	
	private int mBitmapHeight = 256;
	
	private float mOriginalScaleX = 1;
	
	private float mOriginalScaleY = 1;
	
	private float mOriginalScaleZ = 1;
	
	public final static int OPEN_GL_VERSION_2 = 0x00020000;
	
	private int mOpenGLVersion;
	
	public AuroraGLDrawable(AuroraGLSurfaceView view , int resId)
	{
		mView = view;
		
		mOpenGLVersion = getOpenGLVersion();
		
		Bitmap bitmap = BitmapFactory.decodeResource(mView.getResources(), resId);
		
		initTexture(view,bitmap);
		
		mVertices = new AuroraVertices(view, 4, 6, true, true);
		
		setVertics(1.0f);
		
		setTransition(0,0);
		
		setScale(1.0f , 1.0f);
	}
	
	public AuroraGLDrawable(AuroraGLSurfaceView view , Bitmap bitmap)
	{	
		mView = view;
		
		mOpenGLVersion = getOpenGLVersion();
		
		initTexture(view,bitmap);
		
		mVertices = new AuroraVertices(view, 4, 6, true, true);
		
		setVertics(1.0f);
		
		setTransition(0,0);
		
		setScale(1.0f , 1.0f);
	}
	
	public AuroraGLDrawable(AuroraGLSurfaceView view , int resId, float alpha)
	{
		mView = view;
		
		mOpenGLVersion = getOpenGLVersion();
		
		mAlpha = alpha;
		
		Bitmap bitmap = BitmapFactory.decodeResource(mView.getResources(), resId);
		
		initTexture(view,bitmap);
		
		mVertices = new AuroraVertices(view, 4, 6, true, true);
	
		setVertics(alpha);
		
		setTransition(0,0);
		
		setScale(1.0f , 1.0f);
	}
	
	private void initTexture(AuroraGLSurfaceView view,Bitmap bitmap)
	{
		mWidth = bitmap.getWidth();
		
		mHeight = bitmap.getHeight();
		
		if(mOpenGLVersion <= OPEN_GL_VERSION_2)
		{
			mBitmapWidth = getScaledLength(mWidth);
		
			mBitmapHeight = getScaledLength(mHeight);
		
			mOriginalScaleX =  mWidth / (float)mBitmapWidth;
		
			mOriginalScaleY =  mHeight / (float)mBitmapHeight;
		
			bitmap = Bitmap.createScaledBitmap(bitmap, mBitmapWidth, mBitmapHeight, true);
		}
		
		mTexture = new AuroraTexture(view, bitmap);
		
		bitmap.recycle();
	}
	
	/**
	 * get the width or height with 2^n; 
	 */
	private int getScaledLength(int originalLength)
	{	
		int i = 1;
		
		for(i = 1; i > 0; i++)
		{
			int len = (int)Math.pow(2,i);
			
			if(Math.pow(2,i) > originalLength)
			{
				break;
			}
		}
		
		int len1 = (int)Math.pow(2,i);
		
		int len2 = (int)Math.pow(2,i-1);
		
		return Math.abs(len1 - originalLength) > Math.abs(len2 - originalLength) ? len2 : len1;
	}
	
	/**
	 * 
	 * @param alpha : 0-1.0f
	 */
	public void setVertics(float alpha)
	{
		mAlpha = alpha;
		
		int width = (mOpenGLVersion > OPEN_GL_VERSION_2)? mWidth : mBitmapWidth;
		int height = (mOpenGLVersion > OPEN_GL_VERSION_2)? mHeight : mBitmapHeight;
		
		float[] vertics = new float[]{
				0,height,1,1,1,mAlpha ,0,0,
				0,0,1,1,1,mAlpha ,0,1,
				width,0,1,1,1,mAlpha ,1,1,
				width,height,1,1,1,mAlpha ,1,0
		};
		
		mVertices.setVertices(vertics, 0, 32);
		
		mVertices.setIndices(new short[]{
				0,1,2,
				2,3,0
		}, 0, 6);
		
		vertics = null;
	}
	
	/**
	 * 
	 * @param alpha : 0-1.0f
	 */
	public void setAlpha(float alpha)
	{
		if(mOpenGLVersion > OPEN_GL_VERSION_2)
		{
			mAlpha = alpha;
		
			mVertices.setAlpha(alpha);
		}
	}
	
	private int getOpenGLVersion()
	{
		ActivityManager am =(ActivityManager) mView.getContext().getSystemService(Context.ACTIVITY_SERVICE);
		
		ConfigurationInfo info = am.getDeviceConfigurationInfo();
		
		return info.reqGlEsVersion;
	}
	/**
	 * @default x = 0, y = 0
	 * @param x : x > 0 ,right ,x < 0 left
	 * @param y : y > 0 ,down , y < 0 up
	 */
	public void setTransition(int x, int y)
	{
		GL10 gl = mView.getGL();
		
		int height = mView.getHeight();
		
		mTransitionX = x;

		mTransitionY = height - mHeight - y;
	
	}
	
	/**
	 * 
	 * @param angle 0-360
	 */
	public void setRotation(float angle)
	{
		mAngle = angle;
	}
	
	/**
	 * 
	 * @param scaleX : scaleX > 1 ,size > normal ,else < normal
	 * @param scaleY
	 */
	public void setScale(float scaleX , float scaleY)
	{
		
		mScaleX = scaleX * mOriginalScaleX;
		
		mScaleY = scaleY * mOriginalScaleY;
		
	}
	
	/**
	 * @ setTransition && setRotation && setRotation
	 * @param transX
	 * @param transY
	 * @param angle
	 * @param scaleX
	 * @param scaleY
	 */
	public void setMartix(int transX, int transY , float angle , float scaleX , float scaleY)
	{
		int height = mView.getHeight();
		
		mTransitionX = transX;

		mTransitionY = height - mHeight - transY;
		
		mAngle = angle;
		
		setScale(scaleX, scaleY);
	}

	public void draw()
	{
		setMartix();
		
		mTexture.bind();
		
		mVertices.draw(GL10.GL_TRIANGLES, 0, 6);
		
	}
	
	private void setMartix()
	{
		GL10 gl = mView.getGL();
		
		gl.glLoadIdentity();

		gl.glTranslatef(mTransitionX, mTransitionY, 0);
		
		gl.glRotatef(mAngle, 0, 0, 1);
		
		gl.glScalef(mScaleX, mScaleY, 1);
	}
	
	public void onDestory()
	{
		mTexture.dispose();
		
		mVertices = null;
	}
	
	public void resetBitmap(Bitmap bitmap)
	{
		mTexture.setBitmap(bitmap);
		
		mTexture.reload();
	}
	
		@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return mTexture.getWidth();
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return mTexture.getHeight();
	}
}
