package tran.com.android.gc.lib.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import tran.com.android.gc.lib.widget.AuroraButton;

public class AuroraDialogButton extends AuroraButton {

	
//
	private CharSequence text;
	public AuroraDialogButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public AuroraDialogButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public AuroraDialogButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
//			switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//				this.setTextColor(Color.WHITE);
//				break;
//            case MotionEvent.ACTION_UP:
//            	this.setTextColor(Color.BLACK);
//				break;
//
//			default:
//				break;
//			}
			return super.onTouchEvent(event);
		}
	
	
	

}
