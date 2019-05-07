package tran.com.android.gc.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 *
 * @author liushuai
 *
 */
public abstract class AuroraSmartLayout extends LinearLayout {
	
	public static final int X_HIGH_DEGREE = 100;
	public static final int HIGH_DEGREE = 90;
	public static final int MIDDLE_DEGREE = 60;
	public static final int LOW_DEGREE = 30;
	
	/**
	 * This is a time that this layout change to visible.
	 */
	protected long mVisibleTime;
	
	/**
	 * This is a time that this layout change to gone.
	 */
	protected long mGoneTime;
	
	/**
	 * The degree is a necessary level of this layout.
	 */
	protected int mSmartLayoutDegree = MIDDLE_DEGREE;
	
	/**
	 * This is a system data.The data relates to user degree of proficiency.
	 */
	protected int mUserDegree = LOW_DEGREE;
	
	protected Context mContext;

	public AuroraSmartLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public AuroraSmartLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
//	public SmartLayout(Context context, AttributeSet attrs, int defStyle, String userDegreeName) {
//		super(context, attrs, defStyle);
//		// TODO Auto-generated constructor stub
//		init(context, userDegreeName);
//	}
	
	private void init(Context context){
		mContext = context;
		mUserDegree = getUserDegree();	
	}
	
	/**
	 * set the degree of smart layout.The degree is a necessary level of this layout.
	 * @param degree the degree of smart layout.May be X_HIGH_DEGREE, HIGH_DEGREE, MIDDLE_DEGREE, LOW_DEGREE
	 */
	public void setSmartDegree(int degree){
		mSmartLayoutDegree = degree;
	}
	
	/**
	 * get the degree of smart layout.The degree is a necessary level of this layout.
	 * @return the degree of smart layout
	 */
	public int getSmartDegree(){
		return mSmartLayoutDegree;
	}
	
	/**
	 * The time is that the view from visible to gone.
	 * @return a time internal such as 1000 mearns 1000ms
	 */
	public long getVisibleTime(){
		if(mGoneTime > mVisibleTime){
			return mGoneTime - mVisibleTime;
		} else {
			return 0;
		}
		
	}

	@Override
	public void setVisibility(int visibility) {
		// TODO Auto-generated method stub
		
		switch(visibility){
		case View.VISIBLE:
			mUserDegree = getUserDegree();	
			if(mSmartLayoutDegree >= mUserDegree){
				mVisibleTime = System.currentTimeMillis();	
			} else {
				return;
			}
			break;
		default:
			break;
		}
		
		super.setVisibility(visibility);

	}

	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		if(mSmartLayoutDegree < mUserDegree){
			setVisibility(View.GONE);
		}
		mVisibleTime = System.currentTimeMillis();
	}	
	
	abstract protected void processUserDegree();
	
	abstract protected int getUserDegree();
	
	abstract protected boolean saveUserDegree(int userDegree);
	
}
