package tran.com.android.gc.lib.view;
import tran.com.android.gc.lib.utils.DensityUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class ShutdownTextView extends View{

	
	private String mText="Text" ;
	
	private Paint mPaint;
	
	private int mTextColor = Color.WHITE;
	
	private Rect mRect ;
	
	int left ;
	int right;
	int top;
	int bottom ;
	
	public ShutdownTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public ShutdownTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public ShutdownTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	private void init(){
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(mTextColor);
		mPaint.setTextSize(DensityUtil.dip2px(getContext(), 12));
		setText("Airmode");
	}
	
	public void setText(String text){
		if(TextUtils.isEmpty(text)){
			return;
		}
		mText = text;
		mRect = new Rect();
		mPaint.getTextBounds(mText, 0, mText.length(), mRect);
		left = mRect.left;
		right = mRect.right;
		top = mRect.top;
		bottom = mRect.bottom;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		int width = right-left;
		int height = bottom-top;
		if(!TextUtils.isEmpty(mText)){
			canvas.drawText(mText, 0,height, mPaint);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		
		
		int width =measureWidth(widthMeasureSpec) ;//right - left;
		int height = measureHeight(heightMeasureSpec);//bottom - top;
		if(width == 0){
			width = right - left;
		}
		if(height == 0){
			height = bottom - top;
		}
		setMeasuredDimension(width+10, height+10);
	}
	
	private int measureWidth(int measureSpec){
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		int result = 0;
		if(specMode == MeasureSpec.AT_MOST){
			result  = 0;
		}else if(specMode == MeasureSpec.EXACTLY){
			result = specSize;
		}

		return result;
	}
	private int measureHeight(int measureSpec){
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		int result = 0;
		if(specMode == MeasureSpec.AT_MOST){
			result  = 0;
		}else if(specMode == MeasureSpec.EXACTLY){
			result = specSize;
		}

		return result;
	}

}
