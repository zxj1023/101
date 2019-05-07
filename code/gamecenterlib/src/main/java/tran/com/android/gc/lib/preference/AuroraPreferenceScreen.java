package tran.com.android.gc.lib.preference;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;


import tran.com.android.gc.lib.app.AuroraActivity;
import tran.com.android.gc.lib.widget.AuroraListView;
import tran.com.android.gc.lib.widget.AuroraUtil;
import tran.com.android.gc.lib.R;

/**
 * Represents a top-level {@link Preference} that
 * is the root of a Preference hierarchy. A {@link PreferenceActivity}
 * points to an instance of this class to show the preferences. To instantiate
 * this class, use {@link PreferenceManager#createPreferenceScreen(Context)}.
 * <ul>
 * This class can appear in two places:
 * <li> When a {@link PreferenceActivity} points to this, it is used as the root
 * and is not shown (only the contained preferences are shown).
 * <li> When it appears inside another preference hierarchy, it is shown and
 * serves as the gateway to another screen of preferences (either by showing
 * another screen of preferences as a {@link Dialog} or via a
 * {@link Context#startActivity(android.content.Intent)} from the
 * {@link Preference#getIntent()}). The children of this {@link PreferenceScreen}
 * are NOT shown in the screen that this {@link PreferenceScreen} is shown in.
 * Instead, a separate screen will be shown when this preference is clicked.
 * </ul>
 * <p>Here's an example XML layout of a PreferenceScreen:</p>
 * <pre>
&lt;PreferenceScreen
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:key="first_preferencescreen"&gt;
    &lt;CheckBoxPreference
            android:key="wifi enabled"
            android:title="WiFi" /&gt;
    &lt;PreferenceScreen
            android:key="second_preferencescreen"
            android:title="WiFi settings"&gt;
        &lt;CheckBoxPreference
                android:key="prefer wifi"
                android:title="Prefer WiFi" /&gt;
        ... other preferences here ...
    &lt;/PreferenceScreen&gt;
&lt;/PreferenceScreen&gt; </pre>
 * <p>
 * In this example, the "first_preferencescreen" will be used as the root of the
 * hierarchy and given to a {@link PreferenceActivity}. The first screen will
 * show preferences "WiFi" (which can be used to quickly enable/disable WiFi)
 * and "WiFi settings". The "WiFi settings" is the "second_preferencescreen" and when
 * clicked will show another screen of preferences such as "Prefer WiFi" (and
 * the other preferences that are children of the "second_preferencescreen" tag).
 * 
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For information about building a settings UI with Preferences,
 * read the <a href="{@docRoot}guide/topics/ui/settings.html">Settings</a>
 * guide.</p>
 * </div>
 *
 * @see PreferenceCategory
 */
public final class AuroraPreferenceScreen extends AuroraPreferenceGroup implements AdapterView.OnItemClickListener,
        DialogInterface.OnDismissListener {

    private ListAdapter mRootAdapter;
    
    private Dialog mDialog;

    private AuroraListView mListView;
    
    
    private AuroraActivity mActivity;
    
    private Intent mIntent;
    private View mTitleLayout;
    
    /**
     * Do NOT use this constructor, use {@link PreferenceManager#createPreferenceScreen(Context)}.
     * @hide-
     */
    public AuroraPreferenceScreen(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.preferenceScreenStyle);
    }

    /**
     * Returns an adapter that can be attached to a {@link PreferenceActivity}
     * or {@link PreferenceFragment} to show the preferences contained in this
     * {@link PreferenceScreen}.
     * <p>
     * This {@link PreferenceScreen} will NOT appear in the returned adapter, instead
     * it appears in the hierarchy above this {@link PreferenceScreen}.
     * <p>
     * This adapter's {@link Adapter#getItem(int)} should always return a
     * subclass of {@link Preference}.
     * 
     * @return An adapter that provides the {@link Preference} contained in this
     *         {@link PreferenceScreen}.
     */
    public ListAdapter getRootAdapter() {
        if (mRootAdapter == null) {
            mRootAdapter = onCreateRootAdapter();
        }
        
        return mRootAdapter;
    }
    
    /**
     * Creates the root adapter.
     * 
     * @return An adapter that contains the preferences contained in this {@link PreferenceScreen}.
     * @see #getRootAdapter()
     */
    protected ListAdapter onCreateRootAdapter() {
        return new AuroraPreferenceGroupAdapter(this, getContext(), true);
    }

    /**
     * Binds a {@link AuroraListView} to the preferences contained in this {@link PreferenceScreen} via
     * {@link #getRootAdapter()}. It also handles passing list item clicks to the corresponding
     * {@link Preference} contained by this {@link PreferenceScreen}.
     * 
     * @param listView The list view to attach to.
     */
    public void bind(AuroraListView listView) {
        listView.setOnItemClickListener(this);
        listView.setAdapter(getRootAdapter());
        
        listView.setSelector(R.drawable.aurora_transparent);
        listView.setDivider(null);
        
        onAttachedToActivity();
    }
    
    @Override
    protected void onClick() {
        if (getIntent() != null || getFragment() != null || getPreferenceCount() == 0) {
            return;
        }
        
        showDialog(null);
    }
    private  Typeface createTitleFont( ) {
        Typeface face = null;
        try {
            
            face = Typeface.createFromFile(AuroraUtil.ACTION_BAR_TITLE_FONT);
        } catch (Exception e) {
            // TODO: handle exception
            e.getCause();
            e.printStackTrace();
            face = null;
        }
        
        return face;
    }
    private void showDialog(Bundle state) {
        Log.e("luofu", "AuroraPreferenceScreen:"+"showDialog");
        Context context = getContext();
        if (mListView != null) {
            mListView.setAdapter(null);
        }

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View childPrefScreen = inflater.inflate(
                    R.layout.aurora_preference_screen_list_fragment, null);
        mListView = (AuroraListView) childPrefScreen.findViewById(android.R.id.list);
        mListView.setSelector(android.R.color.transparent);
        mListView.setOnScrollListener(new OnScrollListener() {
            
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        
                        break;
                    case  AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        
                        break;

                    default:
                        break;
                }
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {
                // TODO Auto-generated method stub
                
            }
        });
        
        mTitleLayout = childPrefScreen.findViewById(R.id.aurora_preference_screen_title);
        ImageButton backButton = (ImageButton)childPrefScreen.findViewById(R.id.aurora_action_bar_item);
        TextView titleTextView = (TextView)childPrefScreen.findViewById(R.id.aurora_action_bar_home_item_back);
        titleTextView.setOnTouchListener(titleLayoutListener);
        if(titleTextView != null){
            titleTextView.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                   
                }
            });
        }
        if(backButton != null){
            Log.e("back", "backButton");
            backButton.setOnTouchListener(new OnTouchListener() {
                
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if(mTitleLayout != null){
                            mTitleLayout.onTouchEvent(event);
                        }
                       
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if(mTitleLayout != null){
                            mTitleLayout.setPressed(false);
                            if(!isOutOfBounds(getContext(), event, mTitleLayout)){
                                mDialog.dismiss();
                            }
                        }
                        
                    }
                    if(event.getAction() == MotionEvent.ACTION_MOVE){
                        if(isOutOfBounds(getContext(), event, mTitleLayout)){
                            if(mTitleLayout != null){
                                mTitleLayout.setPressed(false);
                            }
                        }
                    }
                    return true;
                }
            });
        }
        bind(mListView);

        // Set the title bar if title is available, else no title bar
        final CharSequence title = getTitle();
        Log.e("luofu", "screen title:"+title);
        Dialog dialog = mDialog = new Dialog(context, context.getThemeResId());
//        dialog.a
        if (TextUtils.isEmpty(title)) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        } else {
            if(titleTextView != null){
                //titleTextView.setTypeface(createTitleFont());
                titleTextView.setText(title);
            }
            
        }
        dialog.setContentView(childPrefScreen);
        dialog.setOnDismissListener(this);
        if (state != null) {
            dialog.onRestoreInstanceState(state);
        }

        // Add the screen to the list of preferences screens opened as dialogs
        getPreferenceManager().addPreferencesScreen(dialog);
        
        dialog.show();
    }
    
    public void onDismiss(DialogInterface dialog) {
        mDialog = null;
        getPreferenceManager().removePreferencesScreen(dialog);
    }
    
    /**
     * Used to get a handle to the dialog. 
     * This is useful for cases where we want to manipulate the dialog
     * as we would with any other activity or view.
     */
    public Dialog getDialog() {
        return mDialog;
    }
    
    private OnTouchListener titleLayoutListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if(mTitleLayout != null){
                    mTitleLayout.onTouchEvent(event);
                }
               
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if(mTitleLayout != null){
                    mTitleLayout.setPressed(false);
                    if(!isOutOfBounds(getContext(), event, mTitleLayout)){
                        mDialog.dismiss();
                    }
                }
                
            }
            if(event.getAction() == MotionEvent.ACTION_MOVE){
                if(isOutOfBounds(getContext(), event, mTitleLayout)){
                    if(mTitleLayout != null){
                        mTitleLayout.setPressed(false);
                    }
                }
            }
            return false;
        }
    };
    private boolean isOutOfBounds(Context context, MotionEvent event,View target) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        return (x < -slop) || (y < -slop)
                || (x > (target.getWidth()+slop))
                || (y > (target.getHeight()+slop));
}

    public void onItemClick(AdapterView parent, View view, int position, long id) {
        // If the list has headers, subtract them from the index.
        if (parent instanceof AuroraListView) {
            position -= ((AuroraListView) parent).getHeaderViewsCount();
        }
        Object item = getRootAdapter().getItem(position);
        if (!(item instanceof AuroraPreference)) return;

        final AuroraPreference preference = (AuroraPreference) item; 
        preference.performClick(this);
    }

    @Override
    protected boolean isOnSameScreenAsChildren() {
        return false;
    }
    
    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        final Dialog dialog = mDialog;
        if (dialog == null || !dialog.isShowing()) {
            return superState;
        }
        
        final SavedState myState = new SavedState(superState);
        myState.isDialogShowing = true;
        myState.dialogBundle = dialog.onSaveInstanceState();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }
         
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        if (myState.isDialogShowing) {
            showDialog(myState.dialogBundle);
        }
    }
    
    private static class SavedState extends BaseSavedState {
        boolean isDialogShowing;
        Bundle dialogBundle;
        
        public SavedState(Parcel source) {
            super(source);
            isDialogShowing = source.readInt() == 1;
            dialogBundle = source.readBundle();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(isDialogShowing ? 1 : 0);
            dest.writeBundle(dialogBundle);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
    
}
