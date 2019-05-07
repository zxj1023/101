package tran.com.android.gc.lib.view;

import javax.microedition.khronos.opengles.GL10;

import tran.com.android.gc.lib.opengl.AuroraGLDrawable;
import tran.com.android.gc.lib.opengl.IAuroraGLDrawable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

public class AuroraGLImageView extends AuroraGLSurfaceView {
	
	private IAuroraGLDrawable mDrawableSrc;
	
	private Drawable mSrc = null;

	public AuroraGLImageView(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}

	public AuroraGLImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray a = context.obtainStyledAttributes(attrs,
                com.android.internal.R.styleable.ImageView, 0, 0);
                
        Drawable d = a.getDrawable(com.android.internal.R.styleable.ImageView_src); 
        
        if (d != null)
        {
            mSrc = d;
            
            mSrc.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        }      
        
        a.recycle(); 
		
	}
	

	@Override
	protected void doDraw(GL10 gl) {
		// TODO Auto-generated method stub
		if(Debug)
			Log.e("liuwei","image doDraw !!! ");
			
		if(mDrawableSrc == null)
		{
			setImageSrc(mSrc);
		}
		
		mDrawableSrc.setTransition(super.mPaddingLeft, super.mPaddingTop);
		
		mDrawableSrc.draw();
	}
	
	@Override
	public void onPause() 
	{
		super.onPause();
		
		if(Debug)
			Log.e("liuwei","imageView Destoryed !!!");
		
		if(mDrawableSrc != null)
		{
			mDrawableSrc.onDestory();
			
			mDrawableSrc = null;
		}
	}
	
	@Override
	public void onResume() 
	{
		if(Debug)
			Log.e("liuwei","image onResume !!! ");
			
		super.onResume();
		
		
	} 

	@Override
	protected Rect getRect()
	{
		if(Debug)
			Log.e("liuwei","getRect !!! mSrc.getBounds() = " + mSrc.getBounds());
		return (mSrc != null)?mSrc.getBounds():super.getRect();
	}
	
	public void setImageSrc(Drawable d)
	{
		Bitmap bitmap = super.drawableToBitmap(d);
		
		if(mDrawableSrc == null)
			mDrawableSrc = new AuroraGLDrawable(this, bitmap);
		else
			mDrawableSrc.resetBitmap(bitmap);
	}
	
	
}
