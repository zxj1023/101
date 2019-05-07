package tran.com.android.gc.lib.widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tran.com.android.gc.lib.utils.DensityUtil;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import android.os.Message;

import tran.com.android.gc.lib.R;

public class AuroraLoginView extends LinearLayout implements TextWatcher
,OnItemClickListener,OnClickListener,OnPreDrawListener{

	private static final String TAG = "AuroraLoginView";
	
	private static final String EMAIL_LABLE = "@";
	
	private static final int STATE_EDITABLE = 0x01;
	
	private static final int STATE_UNEDITALBE = 0x02;
	
	private static final int CHANGE_STATE_TIME = 150;
	
	private static final int TYPE_EMAIL = 1;
	
	private static final int TYPE_NORMAL = 2;
	/**
	 * username type
	 * 1 representatives of common user name
	 * 2 representatives of common email address
	 */
	private int mUserNameType = TYPE_EMAIL;
	
	/**
	 * it will Prompt to select an email address
	 */
	private ListView mEmailList;
	
	/**
	 * edit text for input username
	 */
	private AuroraEditText mUserNameInput;
	
	/**
	 * edittext for input password
	 */
	private AuroraEditText mPasswordInput;
	
	/**
	 * when you input some error msg,it will show
	 */
	private TextView mErrorTipText;
	
	private TextView mUserNameHintView;
	
	private TextView mPasswordHintView;
	
	/**
	 * click this view to enter other page
	 */
	private TextView mLinkText;
	
	/**
	 * submit your msg to server
	 */
	private Button mSubmitButton;
	
	
	private String[] mSelectableEmailList;
	
	/**
	 * adapter for selectable email
	 */
	private EmailListAdapter mEmailAdapter;
	
	private String mTempInput;
	
	private EventListener mEventListener;
	
	private CharSequence mUserName;
	
	private CharSequence mPassword;
	
	private CharSequence mUserNameHint;
	
	private CharSequence mPasswordHint;
	
	private View mPasswordLayout;
	
	private ImageButton mDeleteUserNameBtn ,mDeletePasswordBtn;
	
	private View mProgressView;
	

	
	
	private boolean mNeedLinkText = true;
	/**
	 * 
	 * all event will deal with by this listener
	 *
	 */
	public interface EventListener{
		/**
		 * it will called when user click link text view.
		 */
		public void onLinkClick();
		
		/**
		 * it will called when user click link text view.
		 */
		public void onLinkClick(CharSequence userName,CharSequence password);
		
		/**
		 * it will called when user click submit button.
		 */
		public void onSubmitClick(CharSequence userName,CharSequence password);
	}
	
	
	private Runnable mCheckInputRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
				mEmailAdapter.update(mTempInput);
				showEmailList();
			
		}
	};
	
	
	private Handler mCheckInputHandler = new Handler();
	
	private Handler mStateHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STATE_EDITABLE:
				if(mProgressView.getVisibility() == View.GONE){
				if (mDeleteUserNameBtn != null) {
					
					mDeleteUserNameBtn.setEnabled(true);
				}
				if (mDeletePasswordBtn != null) {
					mDeletePasswordBtn.setEnabled(true);
				}
				}
				break;
			case STATE_UNEDITALBE:
				
				break;
			default:
				break;

			}
		}
		
		};
	
	private void showInputMethod(){
//		InputMethodManager imm = (InputMethodManager) getContext()
//				.getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.showSoftInput(mUserNameInput, InputMethodManager.SHOW_FORCED);
	}
	
	public AuroraLoginView(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}
	
	public AuroraLoginView(Context context, AttributeSet attrs) {
		this(context, attrs,R.attr.aurora_login_style);
		// TODO Auto-generated constructor stub
	}
	
	public AuroraLoginView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		setOrientation(LinearLayout.VERTICAL);
		mSelectableEmailList = context.getResources().getStringArray(R.array.aurora_email_list);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.aurora_login_layout, this,true);
//		addView(view,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		mEmailAdapter = new EmailListAdapter(context, mSelectableEmailList);
		mPasswordLayout = findViewById(R.id.aurora_password_layout);
		if(attrs != null){
			 int i;
		   for (i=0; i<attrs.getAttributeCount(); i++) {
               int id = attrs.getAttributeNameResource(i);
			   if (id == R.attr.userNameType) {
				   mUserNameType = attrs.getAttributeIntValue(i, TYPE_EMAIL);

//                   case R.attr.passwordHint:
//                	   mPasswordHint = attrs.getAttributeValue(i);
//                	   break;
//                   case R.attr.userNameHint:
//                	   mUserNameHint = attrs.getAttributeValue(i);
//                	   break;
			   }
           }
		}
		
		mDeleteUserNameBtn =(ImageButton) findViewById(R.id.aurora_user_name_delete_btn);
		mDeletePasswordBtn = (ImageButton)findViewById(R.id.aurora_password_delete_btn);
		mDeleteUserNameBtn.setOnClickListener(this);
		mDeletePasswordBtn.setOnClickListener(this);
		
		mProgressView = findViewById(R.id.aurora_login_progress);
		mUserNameInput = (AuroraEditText)findViewById(R.id.aurora_user_name);
		mPasswordInput = (AuroraEditText)findViewById(R.id.aurora_password);
		
		mUserNameHintView = (TextView)findViewById(R.id.aurora_login_username_hint_text_view);
		mPasswordHintView = (TextView)findViewById(R.id.aurora_login_password_hint_text_view);
		
//		if(!TextUtils.isEmpty(mPasswordHint)){
//			mPasswordInput.setHint(mPasswordHint);
//		}
//		if(!TextUtils.isEmpty(mUserNameHint)){
//			mUserNameInput.setHint(mUserNameHint);
//		}
		
		mErrorTipText = (TextView)findViewById(R.id.aurora_login_error_tip);
		mLinkText = (TextView)findViewById(R.id.aurora_login_forget_password);
		
		mSubmitButton = (Button)findViewById(R.id.aurora_login_submit);
		
		mEmailList = (ListView)findViewById(R.id.aurora_email_list);
		
		mSubmitButton.setOnClickListener(this);
		mLinkText.setOnClickListener(this);
		if(mUserNameType == TYPE_EMAIL){
			mUserNameInput.addTextChangedListener(this);
		}
		
//		mUserNameInput.setIsNeedDeleteAll(true);
//		mPasswordInput.setIsNeedDeleteAll(true);
		mEmailList.setAdapter(mEmailAdapter);
		mEmailList.setOnItemClickListener(this);
		mPasswordInput.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(arg0)){
					mPasswordHintView.setVisibility(View.VISIBLE);
				}else{
					mPasswordHintView.setVisibility(View.INVISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mUserNameInput.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View view, int code, KeyEvent event) {
				// TODO Auto-generated method stub
				if(code == KeyEvent.KEYCODE_BACK){
					if(emaliListIsShow()){
						hideEmailList();
						return true;
					}
				}
				return false;
			}
		});
/*		mUserNameInput.setFocusChangeListener(new tran.com.android.gc.lib.widget.AuroraEditText.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				Log.e("focus",hasFocus+"  hasFocus ");
				if(hasFocus){
					showInputMethod();
					  
				}
			}
		});*/
		setPressed(true);
		setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if(emaliListIsShow()){
					hideEmailList();
					return true;
				}
				return false;
			}
		});
//		showInputMethod();
		
	}
	

	public void setUserNameHint(CharSequence hint){
		if(TextUtils.isEmpty(hint)){
			return;
		}
		mUserNameHintView.setHint(hint);
	}
	public void setPasswordHint(CharSequence hint){
		if(TextUtils.isEmpty(hint)){
			return;
		}
		mPasswordHintView.setHint(hint);
	}
	
	
	/**
	 * add event listener to login widget
	 * @param listener
	 */
	public void setEventListener(EventListener listener){
		this.mEventListener = listener;
	}
	
	/**
	 * get visibility of email list
	 * @return  
	 */
	public boolean emaliListIsShow(){
		return mEmailList.getVisibility() == View.VISIBLE;
	}
	
	/**
	 * hide the email list
	 */
    public void hideEmailList(){
    	mEmailAdapter.init(mSelectableEmailList);
    	if(mEmailList.getVisibility() == View.VISIBLE){
    		mEmailList.setVisibility(View.GONE);
    	}
    	if(mPasswordLayout.getVisibility() == View.GONE){
    		mPasswordLayout.setVisibility(View.VISIBLE);
    	}
    }
    
    /**
     * show the email list
     */
    private void showEmailList(){
    	
    	int items = mEmailList.getAdapter().getCount();
//    	if(!(items >0)){
//    		mEmailList.setVisibility(View.GONE);
//    	}
    	if(mPasswordLayout.getVisibility() == View.VISIBLE){
    		mPasswordLayout.setVisibility(View.GONE);
    	}
    	mEmailList.setVisibility(View.VISIBLE);
    }
	
	@Override
	public void afterTextChanged(Editable edit) {
		// TODO Auto-generated method stub
		int count = mEmailAdapter.getCount();
		if(count == 0){
			if(mEmailList.getVisibility() == View.VISIBLE){
	    		mEmailList.setVisibility(View.GONE);
	    	}
	    	if(mPasswordLayout.getVisibility() == View.GONE){
	    		mPasswordLayout.setVisibility(View.VISIBLE);
	    	}
		}
		
		if(emaliListIsShow()){
			String input = mUserNameInput.getText().toString();
			if(!TextUtils.isEmpty(input)){
				mEmailAdapter.filter(input);
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence str, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
	
		if(!TextUtils.isEmpty(str)){
			showHint(false);
			String input = str + "";
			if(isChinese(input)){
				hideEmailList();
				return;
			}
			
			if(input.endsWith(EMAIL_LABLE)){
				input = input.substring(0,input.length()-1);
			}
			if(input.contains(EMAIL_LABLE)){
				if(needShowEmailList(input)){
					mEmailAdapter.filter(input);
					showEmailList();
					return;
				}
			}
			if(!checkEmail(input+EMAIL_LABLE+mSelectableEmailList[0]) ){
				hideEmailList();
				return;
			}
			mTempInput = input;
			mCheckInputHandler.post(mCheckInputRunnable);
		}else{
			showHint(true);
			hideEmailList();
		}
	}
	
	private void showHint(boolean show){
		if(show){
			mUserNameHintView.setVisibility(View.VISIBLE);
		}else{
			mUserNameHintView.setVisibility(View.INVISIBLE);
		}
	}

	private  boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    // 完整的判断中文汉字和符号
    public  boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    } 
	
	
	
	
	/**
	 * 
	 *adapter for emali list
	 */
	class EmailListAdapter extends BaseAdapter{

		private Context mContext;
		private LayoutInflater mInflater;
		private List<String> mItems = new ArrayList<String>();
		private List<String> mFilterItems = new ArrayList<String>();
		private EmailFilter mEmailFilter;
		private String[] mTempEmail;
		private String mTmpStr;
		public EmailListAdapter(Context context,String[] emails){
			
			init(emails);
			this.mContext = context;
			mInflater = LayoutInflater.from(context);
		}
		
		public void init(String[] emails){
			mItems.clear();
			mTempEmail = new String[emails.length];
			for(int i = 0;i<emails.length;i++){
				mItems.add(EMAIL_LABLE+emails[i]);
				mTempEmail[i] = EMAIL_LABLE+emails[i];
			}
			notifyDataSetChanged();
		}
		
		public Filter getFilter(){
			if(mEmailFilter == null){
				mEmailFilter = new EmailFilter();
			}
			return mEmailFilter;
		}
		
		/**
		 * update email list datas
		 * @param input
		 */
		public void update(String input) {
			mItems.clear();
			for (int i = 0; i < mTempEmail.length; i++) {
				String email = input + mTempEmail[i];
				
				if (checkEmail(email)) {
					mItems.add(email);
				}
			}
			notifyDataSetChanged();
		}
		
		
		/**
		 * 
		 * @param src
		 * @param tar
		 * @return
		 */
		private boolean contains(String src,String tar){
			int indexTar = 0;
			for(int i = tar.length()-1;i>=0;i--){
				if('@'==tar.charAt(i)){
					indexTar = i;
					break;
				}
			}
			String subTar = tar.substring(indexTar+1,tar.length());
			src = src.substring(1, src.length());
			mTmpStr = tar.substring(0,indexTar);
			if(src.startsWith(subTar)){
				return true;
			}
			
			return false;
		}
		
		/**
		 * 
		 * @param input
		 */
		public void filter(String input){
			mItems.clear();
			for (int i = 0; i < mTempEmail.length; i++) {
				String email = mTempEmail[i];
				
				if(contains(email, input)){
					email = mTmpStr+email;
					mItems.add(email);
				}
			}
			
			int count = mItems.size();
			if(count == 1){
				String inputStr = mUserNameInput.getText().toString();
				String selectable = mItems.get(0);
				if(!TextUtils.isEmpty(selectable) && !TextUtils.isEmpty(inputStr)){
					if(selectable.equals(inputStr)){
						hideEmailList();
					}
				}
			}
			notifyDataSetChanged();
			
			
		}
		
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mItems.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mItems.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(view == null){
				view = mInflater.inflate(android.R.layout.simple_list_item_1, null);
				holder = new ViewHolder();
				holder.mText = (TextView)view.findViewById(android.R.id.text1);
				view.setTag(holder);
			}else{
				holder = (ViewHolder) view.getTag();
			}
				holder.mText.setText(mItems.get(position));
				holder.mText.setTextColor(0xFF010101);
				holder.mText.setTextSize(16f);
				holder.mText.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
				holder.mText.setPadding(holder.mText.getPaddingLeft(), holder.mText.getPaddingTop(),
						DensityUtil.dip2px(mContext, 45), holder.mText.getPaddingBottom());
			return view;
		}
		
		class EmailFilter extends Filter{

			@Override
			protected FilterResults performFiltering(CharSequence filter) {
				// TODO Auto-generated method stub
				FilterResults result = new FilterResults();
				for (Iterator<String> iterator = mItems.iterator(); iterator.hasNext();) {
			        String name = iterator.next();
			        if (name.contains(filter)) {
			        	mFilterItems.add(name);
			        }
			      }
				result.values = mFilterItems;
			      return result;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence arg0, FilterResults result) {
				// TODO Auto-generated method stub
				if(result.values instanceof List){
					mItems = (List<String>) result.values;
				      if (result.count > 0) {
				        notifyDataSetChanged();
				      } else {
				        notifyDataSetInvalidated();
				      }
				}
			
			}
			
		}
		class ViewHolder{
			TextView mText;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		
		String selectedItem = (String) mEmailAdapter.getItem(position);
		mUserNameInput.setText(selectedItem);
		mUserNameInput.setSelection(selectedItem.length());
		hideEmailList();
	}
	
	
	
	
	
	
	/**
	 * check your string is email or not
	 * @param email need check string
	 * */
	private boolean checkEmail(String email) {
		String reg = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]*@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern pattern = Pattern
				.compile(reg);
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}
	
	private boolean needShowEmailList(String input){
		int num = 0;
		for(int i = 0;i<input.length();i++){
			if('@' == input.charAt(i)){
				num++;
			}
		}
		if(num == 1){
			return true;
		}
		return false;
	}
	

	/**
	 * set login error msg
	 * @param error
	 */
	public void setErrorMsg(CharSequence error){
		mErrorTipText.setText(error);
		mStateHandler.sendEmptyMessageDelayed(STATE_EDITABLE, CHANGE_STATE_TIME);
		
	}
	
	public void setTip(CharSequence tip){
		
	}
	
	public void needLinkText(boolean need){
		mNeedLinkText = need;
		mLinkText.setVisibility(need?View.VISIBLE:View.GONE);
	}
	
	public void setLinkText(CharSequence text){
		mLinkText.setText(text);
	}
	
	public void showSubmitButton(){
		
	}
	
	public void showProgress(){
		mProgressView.setVisibility(View.VISIBLE);
		mSubmitButton.setVisibility(View.GONE);
		mLinkText.setVisibility(View.GONE);
		mUserNameInput.setEnabled(false);
		mPasswordInput.setEnabled(false);
	}
	public void dismissProgress(){
		mProgressView.setVisibility(View.GONE);
		mSubmitButton.setVisibility(View.VISIBLE);
		if(mNeedLinkText){
			mLinkText.setVisibility(View.VISIBLE);
		}
		mUserNameInput.setEnabled(true);
		mPasswordInput.setEnabled(true);
		mStateHandler.sendEmptyMessageDelayed(STATE_EDITABLE,CHANGE_STATE_TIME);
	}
	
	private void unDeleteable(){
		if (mDeleteUserNameBtn != null) {
			Log.e("error", "setError deletePassword != null");
			mDeleteUserNameBtn.setEnabled(false);
		}
		if (mDeletePasswordBtn != null) {
			mDeletePasswordBtn.setEnabled(false);
		}
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int i = view.getId();
		if (i == R.id.aurora_login_submit) {
			if (mEventListener != null) {
				mUserName = mUserNameInput.getText();
				mPassword = mPasswordInput.getText();
				mEventListener.onSubmitClick(mUserName, mPassword);
				unDeleteable();
			}


		} else if (i == R.id.aurora_login_forget_password) {
			if (mEventListener != null) {
				mEventListener.onLinkClick(mUserNameInput.getText(), mPasswordInput.getText());
			}

		} else if (i == R.id.aurora_password_delete_btn) {
			mPasswordInput.setText("");

		} else if (i == R.id.aurora_user_name_delete_btn) {
			mUserNameInput.setText("");

		} else {
		}
		
	}

	@Override
	public boolean onPreDraw() {
		// TODO Auto-generated method stub
		Log.e("focus", "onPreDraw");
		return false;
	}
	
	
	


	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


}

