package tran.com.android.gc.lib.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import tran.com.android.gc.lib.R;


class AuroraInputMethod extends PopupWindow {

	
	private Context mContext;
	private AuroraInputView input;
	private static final int CHAR_CLICK_MSG = 0x001;
	private static final int UPPER_CLICK_MSG = 0x002;
	private static final int BACK_CLICK_MSG = 0x003;
	private static final int BACK_LONG_CLICK_MSG = 0x004;
	
	private static final int MSG_BUTTON_CLICK_BEFORE = 0x005;
	private static final int MSG_BUTTON_CLICK_AFTER = 0x006;
	
	private Handler mClickHandler;
	
	private boolean isLowerCase = true;
	
	private LinearLayout mInputAreaTop;
	private LinearLayout mInputAreaMiddle;
	private LinearLayout mInputAreaBottom;
	
	private int topCount;
	private int middleCount;
	private int bottomCount;
	
	private String[] firstRowChars;
    private	String[] secondRowChars; 
	private String[] thirdRowChars;
	
	private String currentChar;
	
	private TextView mEditableView;
	
	private Drawable mButtonClickBefore;
	private Drawable mButtonClickAfter;
	
	private CharacterChangeListener mCharacterChangeListener;
	
	private ButtonBackGroundHandler mButtonBackgroundHandler;

	
	public interface CharacterChangeListener {
		/**
		 * input the character into editable view
		 * @param targetText
		 */
		public void getCharacter(String targetText);
		
		/**
		 * press back button to delete one character
		 * @param editableView
		 */
		public void onDeletedCharacter(TextView editableView);
		
		public void onLongPressedDeletedCharacter(TextView editableView);
	}
	
	public AuroraInputMethod(TextView editableView,Context context){
		mContext = context;
		mEditableView = editableView;
		mClickHandler = new ClickHandler();
		initView();
	    setContentView(input);
	    setFocusable(false);
	    setOutsideTouchable(true);
	    setBackgroundDrawable(new ColorDrawable(0));
	    setWidth(LayoutParams.MATCH_PARENT);
	    setHeight(LayoutParams.WRAP_CONTENT);
	    update();
	    mButtonBackgroundHandler = new ButtonBackGroundHandler();
	    
	    mButtonClickBefore = mContext.getResources().getDrawable(R.drawable.aurora_inputmethod_btn_normal);
	    mButtonClickAfter = mContext.getResources().getDrawable(R.drawable.aurora_inputmethod_btn_pressed);
	    

	}
	
	private void initView(){
		input = new AuroraInputView(mContext);
		
	}
	public void show(View anchor){
		showAtLocation(anchor, Gravity.BOTTOM, 0, 0);
	    
	}
	
	public boolean keyBoardIsShowing(){
		return isShowing();
	}
	public void dismissKeyBoard(){
		if(isShowing()){
			dismiss();
		}
	}
	public void setCharacterChangeListener(CharacterChangeListener listener){
		mCharacterChangeListener = listener;
	}
	
	class AuroraInputView extends LinearLayout implements OnClickListener,OnLongClickListener {
		private StringBuffer mTempCharacters;
		private Context mContext;
		
		
		public AuroraInputView(Context context) {
			super(context);
			mContext = context;
			// TODO Auto-generated constructor stub
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// aurora
			inflater.inflate(R.layout.aurora_input_method, this, true);
			mInputAreaTop = (LinearLayout) findViewById(R.id.input_top);
			mInputAreaMiddle = (LinearLayout) findViewById(R.id.input_middle);
			mInputAreaBottom = (LinearLayout) findViewById(R.id.input_bottom);
			
			topCount = mInputAreaTop.getChildCount();
			middleCount = mInputAreaMiddle.getChildCount();
			bottomCount = mInputAreaBottom.getChildCount();
			
			firstRowChars = mContext.getResources().getStringArray(R.array.auroraInputMethodChara);
			secondRowChars = mContext.getResources().getStringArray(R.array.auroraInputMethodCharb);
			thirdRowChars = mContext.getResources().getStringArray(R.array.auroraInputMethodCharc);
			
			initKeyBoard();
			mTempCharacters = new StringBuffer();
			// setOnclickListener to button
			setOnCharacterClickListener(mInputAreaTop);
			setOnCharacterClickListener(mInputAreaMiddle);
			setOnCharacterClickListener(mInputAreaBottom);
		}

		private void setOnCharacterClickListener(LinearLayout parent) {
			int childCount = parent.getChildCount();
			for (int i = 0; i < childCount; i++) {
				if (parent.getChildAt(i) instanceof Button) {
					parent.getChildAt(i).setOnClickListener(this);
					parent.getChildAt(i).setOnLongClickListener(this);
				}
			}
//			((Button)mInputAreaBottom.findViewById(R.id.char_back)).setOnLongClickListener(this);
		}
		
		private void initKeyBoard(){
			int firstRowCharNum = firstRowChars.length;
			int secondRowCharNum = secondRowChars.length;
			int thirdRowCharNum = thirdRowChars.length;
			int tempThirdNum = 0;
			if(firstRowCharNum == topCount){
				for(int i = 0;i<topCount;i++){
					((Button)mInputAreaTop.getChildAt(i)).setText(firstRowChars[i]);
				}
			}
			if(secondRowCharNum == middleCount){
				for(int i = 0;i<middleCount;i++){
					((Button)mInputAreaMiddle.getChildAt(i)).setText(secondRowChars[i]);
				}
			}
			if(thirdRowCharNum == bottomCount-2){
				for(int i = 1;i<bottomCount-1;i++){
					if(tempThirdNum <7){
						((Button)mInputAreaBottom.getChildAt(i)).setText(thirdRowChars[tempThirdNum]);
						tempThirdNum ++;
					}
					
				}
			}
		}

		
		@Override
		public void onClick(final View v) {
			if(v instanceof Button){
				mButtonBackgroundHandler.setTargetButton((Button)v);
				mButtonBackgroundHandler.sendEmptyMessage(MSG_BUTTON_CLICK_AFTER);
				
			}
			if (mCharacterChangeListener != null) {
				if(v.getId() == R.id.char_upper){
					mClickHandler.sendEmptyMessage(UPPER_CLICK_MSG);
				}else if(v.getId() == R.id.char_back){
					// delete one character
					mClickHandler.sendEmptyMessage(BACK_CLICK_MSG);
				}else {
					currentChar = ((Button) v).getText().toString();
					mClickHandler.sendEmptyMessage(CHAR_CLICK_MSG);
				}
				
			}
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
				try {
					Thread.sleep(10);
					mButtonBackgroundHandler.setTargetButton((Button)v);
					mButtonBackgroundHandler.sendEmptyMessage(MSG_BUTTON_CLICK_BEFORE);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				}
			}).start();

		}

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId() == R.id.char_back){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							while(mEditableView.getText().toString().length()>0){
								Thread.sleep(50);
								mClickHandler.sendEmptyMessage(BACK_LONG_CLICK_MSG);
							}
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
				
			}
			else if(v.getId() == R.id.char_upper){
				;
			}else{
//				currentChar = ((Button)v).getText().toString();
//				if(!"".equals(currentChar)){
//					if(isLowerCase){
//						currentChar = currentChar.toUpperCase();
//					}else{
//						currentChar = currentChar.toLowerCase();
//					}
//					
//				}
//				mCharacterChangeListener.getCharacter(currentChar);
//				return false;
			}
			return false;
		}
		
		

	}
	
	class ButtonBackGroundHandler extends Handler{
		private Button mButton;
		public void setTargetButton(Button btn){
			mButton = btn;
		}
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_BUTTON_CLICK_BEFORE:
				if(mButton != null){
					mButton.setBackground(mButtonClickBefore);
					mButton.invalidate();
				}
				break;
			case MSG_BUTTON_CLICK_AFTER:
				if(mButton != null){
					mButton.setBackground(mButtonClickAfter);
					mButton.invalidate();
				}
				break;
			default:
				break;
			}
		}
		
	}

	class ClickHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case CHAR_CLICK_MSG:
				mCharacterChangeListener.getCharacter(currentChar);
				break;
			case UPPER_CLICK_MSG:
				if(isLowerCase){
					for(int i = 0;i<topCount;i++){
						Button btn = (Button) mInputAreaTop.getChildAt(i);
						btn.setText(btn.getText().toString().toUpperCase());
					}
					for(int i = 0;i<middleCount;i++){
						Button btn = (Button) mInputAreaMiddle.getChildAt(i);
						btn.setText(btn.getText().toString().toUpperCase());
					}
					for(int i = 1;i<bottomCount-1;i++){
						Button btn = (Button) mInputAreaBottom.getChildAt(i);
						btn.setText(btn.getText().toString().toUpperCase());
					}
					
					isLowerCase = false;
				}else
				{
					for(int i = 0;i<topCount;i++){
						Button btn = (Button) mInputAreaTop.getChildAt(i);
						btn.setText(btn.getText().toString().toLowerCase());
					}
					for(int i = 0;i<middleCount;i++){
						Button btn = (Button) mInputAreaMiddle.getChildAt(i);
						btn.setText(btn.getText().toString().toLowerCase());
					}
					for(int i = 1;i<bottomCount-1;i++){
						Button btn = (Button) mInputAreaBottom.getChildAt(i);
						btn.setText(btn.getText().toString().toLowerCase());
					}
					isLowerCase = true;
				}
				break;
			case BACK_CLICK_MSG:
				mCharacterChangeListener.onDeletedCharacter(mEditableView);
				break;
			case BACK_LONG_CLICK_MSG:
				mCharacterChangeListener.onLongPressedDeletedCharacter(mEditableView);
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	}
	
	

}
