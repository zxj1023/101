package tran.com.android.gc.lib.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class AuroraUnderLineTextView extends TextView {

	public AuroraUnderLineTextView(Context context) {
		super(context);
		setUnderline();
		// TODO Auto-generated constructor stub
	}

	public AuroraUnderLineTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setUnderline();
	}

	public AuroraUnderLineTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		setUnderline();
	}
	
	private void setUnderline(){
		getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		getPaint().setAntiAlias(true);
	}


}
