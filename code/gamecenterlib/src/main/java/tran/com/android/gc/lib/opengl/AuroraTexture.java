package tran.com.android.gc.lib.opengl;
import javax.microedition.khronos.opengles.GL10;

import tran.com.android.gc.lib.view.AuroraGLSurfaceView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class AuroraTexture {

	GL10 mGL;
	
	Context mContext;
	
	int textureId;
	
	int minFilter;
	
	int magFilter;
	
	int resID = 0;
	
	int mWidth;
	
	int mHeight;
	
	Bitmap mBitmap;
	
	/**
	 * @constructor
	 * @param view
	 * @param resId
	 */
	public AuroraTexture(AuroraGLSurfaceView view , int resId )
	{	
		mGL = view.getGL();
		
		resID = resId;
		
		mBitmap = null;
		
		mContext = view.getContext();
		
		load();
	}
	
	public AuroraTexture(AuroraGLSurfaceView view , Bitmap bitmap)
	{	
		mGL = view.getGL();
		
		mBitmap = bitmap;
		
		mContext = view.getContext();
		
		load();
	}
	
	private void load()
	{
		GL10 gl = mGL;
		
		if(resID != 0)
			mBitmap = BitmapFactory.decodeResource(mContext.getResources(), resID);
		
		mWidth = mBitmap.getWidth();
		
		mHeight = mBitmap.getHeight();
		
		int textureIds[] = new int[1];
		
		gl.glGenTextures(1, textureIds, 0);
		
		textureId = textureIds[0];
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0);
		
		setFilters(GL10.GL_LINEAR,GL10.GL_LINEAR);
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		
		if(resID != 0)
			mBitmap.recycle();
	}
	
	/**
	 * @if Surface Destoryed , should reload it
	 */
	public void reload()
	{
		load();
		
		bind();
		
		setFilters(minFilter, magFilter);
		
		mGL.glBindTexture(GL10.GL_TEXTURE_2D, 0);
	}
	
	/**
	 * 
	 * @param minFilter : GL10.GL_LINEAR or GL10.GL_NEAREST
	 * @param magFilter : GL10.GL_LINEAR or GL10.GL_NEAREST
	 */
	public void setFilters(int minFilter , int magFilter)
	{
		this.minFilter = minFilter;
		
		this.magFilter = magFilter;
		
		GL10 gl = mGL;
		
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, minFilter);
		
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, magFilter);
	}
	
	/**
	 * @bind texture
	 */
	public void bind()
	{
		GL10 gl = mGL;
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		
		//defalut Linear
		setFilters(GL10.GL_LINEAR,GL10.GL_LINEAR);
	}
	
	/**
	 * @delete textures
	 */
	public void dispose()
	{
		GL10 gl = mGL;
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		
		int textureIds[] = {textureId};
		
		gl.glDeleteTextures(1, textureIds, 0);
		
	}
	
	public int getWidth()
	{
		return mWidth;
	}
	
	public int getHeight()
	{
		return mHeight;
	}
	
	public void setBitmap(Bitmap bitmap)
	{
		if(bitmap != null)
			mBitmap = bitmap;
	}
}
