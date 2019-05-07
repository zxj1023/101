
package tran.com.android.gc.lib.preference;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;


import tran.com.android.gc.lib.utils.Constants;

import tran.com.android.gc.lib.widget.AuroraListView;
import tran.com.android.gc.lib.R;

public abstract class AuroraPreferenceFragment extends Fragment implements
        AuroraPreferenceManager.OnPreferenceTreeClickListener {

    private static final String PREFERENCES_TAG = "android:preferences";

    private AuroraPreferenceManager mPreferenceManager;
    private AuroraListView mList;
    private boolean mHavePrefs;
    private boolean mInitDone;

    private Activity mContainerActivity;
    private AuroraPreference mFirstPreference;
    private LinearLayout mContainerlayout;
    private LinearLayout mContainerlayout2;
    private AuroraPreferenceScreen mPreferenceScreen;
    /**
     * The starting request code given out to preference framework.
     */
    private static final int FIRST_REQUEST_CODE = 100;

    private static final int MSG_BIND_PREFERENCES = 1;
    
    private ListScrollChangeListener mListScrollListener;
    
    /**
     * if you want to invoke ListView.setOnScrollChangeListener,you should use this interface.
     * 
     * scroll listener for list in preference page
     * @author luofu
     *
     */
    public interface ListScrollChangeListener{
        /**
         * 
         * @param view
         * @param scrollState
         */
        public void onScrollStateChanged(AbsListView view, int scrollState);
        
        /**
         * 
         * @param view
         * @param firstVisibleItem
         * @param visibleItemCount
         * @param totalItemCount
         */
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                int totalItemCount);
    }
    
    public void setOnListScrollChangeListener(ListScrollChangeListener listScrollListener){
        mListScrollListener = listScrollListener;
    }
    
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case MSG_BIND_PREFERENCES:
                    bindPreferences();
                    break;
            }
        }
    };

    final private Runnable mRequestFocus = new Runnable() {
        public void run() {
            mList.focusableViewAvailable(mList);
        }
    };

    /**
     * Interface that PreferenceFragment's containing activity should implement
     * to be able to process preference items that wish to switch to a new
     * fragment.
     */
    public interface OnPreferenceStartFragmentCallback {
        /**
         * Called when the user has clicked on a Preference that has a fragment
         * class name associated with it. The implementation to should
         * instantiate and switch to an instance of the given fragment.
         */
        boolean onPreferenceStartFragment(AuroraPreferenceFragment caller, AuroraPreference pref);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferenceManager = new AuroraPreferenceManager(getActivity(), FIRST_REQUEST_CODE);
        mPreferenceManager.setFragment(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        /*
         * return inflater.inflate(com.android.internal.R.layout.
         * aurora_preference_list_fragment, container, false);
         */
        return inflater.inflate(R.layout.aurora_preference_list_fragment, container,
                false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // if (mHavePrefs) {
        // bindPreferences();
        // }

        mInitDone = true;

        if (savedInstanceState != null) {
            Bundle container = savedInstanceState.getBundle(PREFERENCES_TAG);
            if (container != null) {
                final AuroraPreferenceScreen preferenceScreen = getPreferenceScreen();
                if (preferenceScreen != null) {
                    preferenceScreen.restoreHierarchyState(container);
                }
            }
        }

    }

    private void addHeaderAndFooterViewToList() {
        if (hasCategoryInFirst()) {
            return;
        }
        if (mList == null) {
            return;
        }
        if (mList.getAdapter() != null) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View headerView = inflater.inflate(R.layout.aurora_preference_list_padding_top,
                null);
        View footerView = inflater.inflate(
                R.layout.aurora_preference_list_padding_bottom, null);

        mList.addHeaderView(headerView, null, false);
        mList.addFooterView(footerView, null, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO Auto-generated method stub
        // resetPaddingTop();

    }

    private boolean hasCategoryInFirst() {
        mContainerActivity = getActivity();
        mPreferenceScreen = mPreferenceManager.getPreferenceScreen();
        if (mPreferenceScreen == null) {
            return false;
        }
        if (mPreferenceScreen.getPreferenceCount() > 0) {

            mFirstPreference = mPreferenceScreen.getPreference(0);
            if (mFirstPreference == null) {
                return false;
            }
            if (mFirstPreference instanceof AuroraPreferenceCategory) {
                CharSequence title = ((AuroraPreferenceCategory) mFirstPreference)
                        .getTitle();
                if (!TextUtils.isEmpty(title)) {
                   return true;
                }
            }

        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("luofu", "onStart");
        if (mHavePrefs) {
            bindPreferences();
        }
        mPreferenceManager.setOnPreferenceTreeClickListener(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        mPreferenceManager.dispatchActivityStop();
        mPreferenceManager.setOnPreferenceTreeClickListener(null);
    }

    @Override
    public void onDestroyView() {
        mList = null;
        mHandler.removeCallbacks(mRequestFocus);
        mHandler.removeMessages(MSG_BIND_PREFERENCES);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPreferenceManager.dispatchActivityDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        final AuroraPreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            Bundle container = new Bundle();
            preferenceScreen.saveHierarchyState(container);
            outState.putBundle(PREFERENCES_TAG, container);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mPreferenceManager.dispatchActivityResult(requestCode, resultCode, data);
    }

    /**
     * Returns the {@link PreferenceManager} used by this fragment.
     * 
     * @return The {@link PreferenceManager}.
     */
    public AuroraPreferenceManager getPreferenceManager() {
        return mPreferenceManager;
    }

    /**
     * Sets the root of the preference hierarchy that this fragment is showing.
     * 
     * @param preferenceScreen The root {@link PreferenceScreen} of the
     *            preference hierarchy.
     */
    public void setPreferenceScreen(AuroraPreferenceScreen preferenceScreen) {
        if (mPreferenceManager.setPreferences(preferenceScreen) && preferenceScreen != null) {
            mHavePrefs = true;
            if (mInitDone) {
                postBindPreferences();
            }
        }
    }

    /**
     * Gets the root of the preference hierarchy that this fragment is showing.
     * 
     * @return The {@link PreferenceScreen} that is the root of the preference
     *         hierarchy.
     */
    public AuroraPreferenceScreen getPreferenceScreen() {
        return mPreferenceManager.getPreferenceScreen();
    }

    /**
     * Adds preferences from activities that match the given {@link Intent}.
     * 
     * @param intent The {@link Intent} to query activities.
     */
    public void addPreferencesFromIntent(Intent intent) {
        requirePreferenceManager();

        setPreferenceScreen(mPreferenceManager.inflateFromIntent(intent, getPreferenceScreen()));
    }

    /**
     * Inflates the given XML resource and adds the preference hierarchy to the
     * current preference hierarchy.
     * 
     * @param preferencesResId The XML resource ID to inflate.
     */
    public void addPreferencesFromResource(int preferencesResId) {
        requirePreferenceManager();

        setPreferenceScreen(mPreferenceManager.inflateFromResource(getActivity(),
                preferencesResId, getPreferenceScreen()));
    }

    /**
     * {@inheritDoc}
     */
    public boolean onPreferenceTreeClick(AuroraPreferenceScreen preferenceScreen,
            AuroraPreference preference) {
        if (preference.getFragment() != null &&
                getActivity() instanceof OnPreferenceStartFragmentCallback) {
            return ((OnPreferenceStartFragmentCallback) getActivity()).onPreferenceStartFragment(
                    this, preference);
        }
        return false;
    }

    /**
     * Finds a {@link Preference} based on its key.
     * 
     * @param key The key of the preference to retrieve.
     * @return The {@link Preference} with the key, or null.
     * @see PreferenceGroup#findPreference(CharSequence)
     */
    public AuroraPreference findPreference(CharSequence key) {
        if (mPreferenceManager == null) {
            return null;
        }
        return mPreferenceManager.findPreference(key);
    }

    private void requirePreferenceManager() {
        if (mPreferenceManager == null) {
            throw new RuntimeException("This should be called after super.onCreate.");
        }
    }

    private void postBindPreferences() {
        if (mHandler.hasMessages(MSG_BIND_PREFERENCES))
            return;
        mHandler.obtainMessage(MSG_BIND_PREFERENCES).sendToTarget();
    }

    private void bindPreferences() {
        final AuroraPreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            preferenceScreen.bind(getListView());
        }
    }

    /** @hide */
    public AuroraListView getListView() {
        ensureList();
        return mList;
    }

    private void ensureList() {
        if (mList != null) {
            return;
        }
        View root = getView();
        if (root == null) {
            throw new IllegalStateException("Content view not yet created");
        }
        View rawListView = root.findViewById(android.R.id.list);
        if (!(rawListView instanceof AuroraListView)) {
            throw new RuntimeException(
                    "Content has view with id attribute 'android.R.id.list' "
                            + "that is not a AuroraListView class");
        }
        mList = (AuroraListView) rawListView;
        if (mList == null) {
            throw new RuntimeException(
                    "Your content must have a AuroraListView whose id attribute is " +
                            "'android.R.id.list'");
        }

        addHeaderAndFooterViewToList();
        mList.setSelector(android.R.color.transparent);
        mList.setOnKeyListener(mListOnKeyListener);
        mList.setOnScrollListener(new OnScrollListener() {
            
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                if(mListScrollListener != null){
                    mListScrollListener.onScrollStateChanged(view, scrollState);
                }
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    Constants.PREFERENCE_ATTACHED = false;
                }
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {
                // TODO Auto-generated method stub
                if(mListScrollListener != null){
                    mListScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
                
            }
        });
        mHandler.post(mRequestFocus);
    }

    private OnKeyListener mListOnKeyListener = new OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            Object selectedItem = mList.getSelectedItem();
            if (selectedItem instanceof AuroraPreference) {
                View selectedView = mList.getSelectedView();
                return ((AuroraPreference) selectedItem).onKey(
                        selectedView, keyCode, event);
            }
            return false;
        }

    };
}
