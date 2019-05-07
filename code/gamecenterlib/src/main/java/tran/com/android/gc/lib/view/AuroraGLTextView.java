package tran.com.android.gc.lib.view;

import javax.microedition.khronos.opengles.GL10;

import tran.com.android.gc.lib.opengl.AuroraTextGLDrawable;
import tran.com.android.gc.lib.opengl.IAuroraGLDrawable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.content.res.ColorStateList;

public class AuroraGLTextView extends AuroraGLSurfaceView {
	
	private String mText = "";
	
	private TextPaint mPaint = new TextPaint();
	
	/**
	 * just text Rect , not contain paddings
	 */
	private Rect mTextRect = new Rect();
	
	private IAuroraGLDrawable mDrawable;
	
	public AuroraGLTextView(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}

	public AuroraGLTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		CharSequence text = "";
		
		int textSize = 15;
		
		ColorStateList textColor = null;
		
		TypedArray a = context.obtainStyledAttributes(attrs,
                com.android.internal.R.styleable.TextView, com.android.internal.R.attr.textViewStyle, 0);
                
        int n = a.getIndexCount();
        
        for (int i = 0; i < n; i++) 
        {
            int attr = a.getIndex(i);     
            
            switch (attr)
            {
				case com.android.internal.R.styleable.TextView_text:
					text = a.getText(attr);
					break;
				case com.android.internal.R.styleable.TextView_textSize:
					textSize = a.getDimensionPixelSize(attr, textSize);
					break;
				case com.android.internal.R.styleable.TextView_textColor:
					textColor = a.getColorStateList(attr);
					break;		
			}
		}  
		
		setTextSize(textSize);
		
		setTextColor(0xff000000);
	
		setText(text);
		
		//setTypeface(TextView.auroraDefaultTf);
		
		a.recycle();
		/*
		TypedArray a = context.obtainStyledAttributes(
                attrs, com.tran.com.android.gc.lib.opengl.R.styleable.AuroraGLTextView,0,com.tran.com.android.gc.lib.opengl.R.style.AuroraGLTextViewStyle);
		
		float textSize = a.getDimension(com.tran.com.android.gc.lib.opengl.R.styleable.AuroraGLTextView_auroraGLTextSize, 0);
		
		int textColor = a.getColor(com.tran.com.android.gc.lib.opengl.R.styleable.AuroraGLTextView_auroraGLTextColor, 0);
		
		mText = a.getString(com.tran.com.android.gc.lib.opengl.R.styleable.AuroraGLTextView_auroraGLText);
		
		a.recycle();
		
		setTextColor(textColor);
		
		setTextSize(textSize);
		
		setText(mText);
		
		setTypeface(Typeface.DEFAULT);*/
	}
	
	@Override
	protected void doDraw(GL10 gl)
	{
		Log.e("liuwei", "text doDraw !!!");
		draw();
	}
	
	@Override
	protected Rect getRect()
	{
		if(mText != null && !mText.equals(""))
        	mPaint.getTextBounds(mText, 0, mText.length() , mTextRect);
		
		return mTextRect;
	}
	
	
	/**
	 * 
	 * @param id
	 */
	public void setText(int id)
	{
		String str = getResources().getString(id);
		
		setText(str);
	}
	
	/**
	 * 
	 * @param str
	 */
	public void setText(String str)
	{
		if(str == null || str.equals(mText))return;
		
		mText = str;	
	}
	
	/**
	 * 
	 * @param str
	 */
	public void setText(CharSequence str)
	{
		String string = str.toString();
		
		setText(string);
	}
	
	/**
	 * 
	 * @param color
	 */
	public void setTextColor(int color)
	{
		int curColor = mPaint.getColor();
		
		if(curColor != color)
			mPaint.setColor(color);
	}
	
	/**
	 * 
	 * @param size
	 */
	public void setTextSize(float size)
	{
		float density = getResources().getDisplayMetrics().density;
		
		size *= density;
		
		float curSize = mPaint.getTextSize();
		
		if(size != curSize)
			mPaint.setTextSize(size);
	}
	
	/**
	 * 
	 * @param font
	 */
	public void setTypeface(Typeface font)
	{
		Typeface type = mPaint.getTypeface();
		
		if(type != font)
			mPaint.setTypeface(font);
	}
	
	private void draw()
	{
		mDrawable = new AuroraTextGLDrawable(this, mText , mPaint);
		
		mDrawable.setTransition(super.mPaddingLeft, super.mPaddingTop);
		
		mDrawable.draw();
	}
	

	@Override
	public void onPause() 
	{
		super.onPause();
		
		mDrawable.onDestory();
		
		mDrawable = null;
	}
	
	@Override
	public void onResume() 
	{
		super.onResume();
		
		Log.e("liuwei", "text onResume !!!");
	} 
}
