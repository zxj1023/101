package tran.com.android.gc.lib.widget;

import android.content.Context;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 
 * @author liushuai
 *
 */
public class AuroraSmartPopupLayout extends AuroraSmartLayout {
	
	public static String USER_DEGREE = "user_popup_degree";
	private long TIME_INTERNAL = (long) (2.5 * 1000);

	public AuroraSmartPopupLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public AuroraSmartPopupLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
//	public SmartPopupLayout(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle, USER_DEGREE);
//	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		setVisibility(View.GONE);
		mGoneTime = System.currentTimeMillis();
		Log.d("test", "mGoneTime = " + mGoneTime);
		processUserDegree();
		return super.dispatchTouchEvent(ev);
	}

	@Override
	protected void processUserDegree(){
		if(mGoneTime - mVisibleTime > TIME_INTERNAL){
			mUserDegree -= (120 - mSmartLayoutDegree)/6;
			if(mUserDegree < 0){
				mUserDegree = 0;
			}
		} else {
			mUserDegree += mSmartLayoutDegree/6;
			if(mUserDegree >= 100){
				mUserDegree = 99;
			}
		}		
		saveUserDegree(mUserDegree);
	}

	@Override
	protected int getUserDegree() {
		// TODO Auto-generated method stub
		int userDegree = LOW_DEGREE;
		try {
			userDegree = Settings.System.getInt(mContext.getContentResolver(), USER_DEGREE);
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			Settings.System.putInt(mContext.getContentResolver(), USER_DEGREE, LOW_DEGREE);
			e.printStackTrace();
		}
		return userDegree;
	}

	@Override
	protected boolean saveUserDegree(int userDegree) {
		// TODO Auto-generated method stub
		return Settings.System.putInt(mContext.getContentResolver(), USER_DEGREE, userDegree);
	}

}
