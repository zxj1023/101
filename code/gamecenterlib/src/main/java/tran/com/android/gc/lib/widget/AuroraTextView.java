package tran.com.android.gc.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import android.util.Log;
import android.graphics.Canvas;

public class AuroraTextView extends TextView {

	private static final String TAG = "AuroraTextView";
	
	private static final boolean DEBUG = false;
	
	private void log(String str)
	{
		if(DEBUG)
			Log.e(TAG, str);
	
	}
	
	public AuroraTextView(Context context) 
	{
		
		this(context,null);
		// TODO Auto-generated constructor stub
		
	}
	
	public AuroraTextView(Context context, AttributeSet attrs) 
	{
		
		this(context, attrs,0);
		// TODO Auto-generated constructor stub
		
	}

	public AuroraTextView(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
		//auroraSetDefaultBaseLinePadding();
		
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		
		 super.onDraw(canvas);
		 
	}
	
}
