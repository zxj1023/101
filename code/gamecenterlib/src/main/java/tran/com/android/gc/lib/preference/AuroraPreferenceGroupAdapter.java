package tran.com.android.gc.lib.preference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import tran.com.android.gc.lib.preference.AuroraPreference.OnPreferenceChangeInternalListener;
import tran.com.android.gc.lib.R;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


class AuroraPreferenceGroupAdapter extends BaseAdapter implements OnPreferenceChangeInternalListener {
    
    private static final String TAG = "PreferenceGroupAdapter";

    /**
     * The group that we are providing data from.
     */
    private AuroraPreferenceGroup mPreferenceGroup;
    
    /**
     * Maps a position into this adapter -> {@link Preference}. These
     * {@link Preference}s don't have to be direct children of this
     * {@link PreferenceGroup}, they can be grand children or younger)
     */
    private List<AuroraPreference> mPreferenceList;
    
    /**
     * List of unique Preference and its subclasses' names. This is used to find
     * out how many types of views this adapter can return. Once the count is
     * returned, this cannot be modified (since the ListView only checks the
     * count once--when the adapter is being set). We will not recycle views for
     * Preference subclasses seen after the count has been returned.
     */
    private ArrayList<PreferenceLayout> mPreferenceLayouts;

    private PreferenceLayout mTempPreferenceLayout = new PreferenceLayout();

    /**
     * Blocks the mPreferenceClassNames from being changed anymore.
     */
    private boolean mHasReturnedViewTypeCount = false;
    
    private volatile boolean mIsSyncing = false;
    
    private Handler mHandler = new Handler(); 
    
    
    private int mTitleColor;
    private int mSummaryColor;
    
    private Runnable mSyncRunnable = new Runnable() {
        public void run() {
            syncMyPreferences();
        }
    };

    private static class PreferenceLayout implements Comparable<PreferenceLayout> {
        private int resId;
        private int widgetResId;
        private String name;

        public int compareTo(PreferenceLayout other) {
            int compareNames = name.compareTo(other.name);
            if (compareNames == 0) {
                if (resId == other.resId) {
                    if (widgetResId == other.widgetResId) {
                        return 0;
                    } else {
                        return widgetResId - other.widgetResId;
                    }
                } else {
                    return resId - other.resId;
                }
            } else {
                return compareNames;
            }
        }
    }

    public AuroraPreferenceGroupAdapter(AuroraPreferenceGroup preferenceGroup) {
        mPreferenceGroup = preferenceGroup;
        // If this group gets or loses any children, let us know
        mPreferenceGroup.setOnPreferenceChangeInternalListener(this);

        mPreferenceList = new ArrayList<AuroraPreference>();
        mPreferenceLayouts = new ArrayList<PreferenceLayout>();

        syncMyPreferences();
    }
    
    // Gionee zhangxx 2012-12-12 add for CR00715173 begin
    public AuroraPreferenceGroupAdapter(AuroraPreferenceGroup preferenceGroup, Context context, boolean isGioneeStyle) {
        mPreferenceGroup = preferenceGroup;
        // If this group gets or loses any children, let us know
        mPreferenceGroup.setOnPreferenceChangeInternalListener(this);

        mPreferenceList = new ArrayList<AuroraPreference>();
        mPreferenceLayouts = new ArrayList<PreferenceLayout>();

        mContext = context;
        mIsGioneeStyle = isGioneeStyle;
        if (mIsGioneeStyle) {
            getFrameListBackground(mContext);
            getCheckableFrameListBackground(mContext);
            getCheckableTextColor(mContext);
        }
        
        syncMyPreferences();
    }    
    // Gionee zhangxx 2012-12-12 add for CR00715173 end

    private void syncMyPreferences() {
        synchronized(this) {
            if (mIsSyncing) {
                return;
            }

            mIsSyncing = true;
        }

        List<AuroraPreference> newPreferenceList = new ArrayList<AuroraPreference>(mPreferenceList.size());
        flattenPreferenceGroup(newPreferenceList, mPreferenceGroup);
        mPreferenceList = newPreferenceList;
        
        // Gionee zhangxx 2012-12-12 add for CR00715173 begin
        if (mIsGioneeStyle) {
            mPreferenceBackgroundIndexs = getPreferenceBackgroundIndexs(mPreferenceList);
        }
        // Gionee zhangxx 2012-12-12 add for CR00715173 end
        notifyDataSetChanged();

        synchronized(this) {
            mIsSyncing = false;
            notifyAll();
        }
    }
    
    // Gionee zhangxx 2012-12-12 add for CR00715173 begin
    private Context mContext;
    private boolean mIsGioneeStyle = false;
    private int[] mPreferenceBackgroundIndexs;
    private int[] mPreferenceBackgroundRes;
    
    private int[] mPreferenceBackgroundResForCheckable;
    
    private final int FRAME_LIST_BACKGROUND_NULL = 0;
    private final int FRAME_LIST_BACKGROUND_FULL = 1;
    private final int FRAME_LIST_BACKGROUND_TOP = 2;
    private final int FRAME_LIST_BACKGROUND_MIDDLE = 3;
    private final int FRAME_LIST_BACKGROUND_BOTTOM = 4;
    private final int FRAME_LIST_BACKGROUND_TOTAL = 5;
    
    private int[] getPreferenceBackgroundIndexs(List<AuroraPreference> preferences) {
        if (preferences == null || preferences.size() <= 0) {
            return null;
        }
        
        int[] arrays = new int[preferences.size()];
        for (int i = 0; i < preferences.size(); i++) {
            if (preferences.get(i) instanceof AuroraPreferenceCategory) {
                arrays[i] = FRAME_LIST_BACKGROUND_NULL;
                continue;
            }
            
            if (i > 0) {
                switch (arrays[i - 1]) {
                    case 0:
                        arrays[i] = FRAME_LIST_BACKGROUND_FULL;
                        break;
                    case 1:
                        arrays[i - 1] = FRAME_LIST_BACKGROUND_TOP;
                        arrays[i] = FRAME_LIST_BACKGROUND_BOTTOM;
                        break;
                    case 2:
                        arrays[i] = FRAME_LIST_BACKGROUND_BOTTOM;
                        break;
                    case 3:
                        arrays[i] = FRAME_LIST_BACKGROUND_BOTTOM;
                        break;
                    case 4:
                        arrays[i - 1] = FRAME_LIST_BACKGROUND_MIDDLE;
                        arrays[i] = FRAME_LIST_BACKGROUND_BOTTOM;
                        break;
                    default:
                        break;
                }
            } else {
                arrays[i] = FRAME_LIST_BACKGROUND_FULL;
            }
        }
        return arrays;
    }
    
    private void getFrameListBackground(Context context) {
        mPreferenceBackgroundRes = new int[FRAME_LIST_BACKGROUND_TOTAL];
        mPreferenceBackgroundRes[FRAME_LIST_BACKGROUND_NULL] = 0;
        TypedValue outValue = new TypedValue();
        
        context.getTheme().resolveAttribute(R.attr.auroraframeListBackground,
                outValue, true);
        mPreferenceBackgroundRes[FRAME_LIST_BACKGROUND_FULL] = outValue.resourceId;
        context.getTheme().resolveAttribute(R.attr.auroraframeListTopBackground,
                outValue, true);
        mPreferenceBackgroundRes[FRAME_LIST_BACKGROUND_TOP] = outValue.resourceId;
        context.getTheme().resolveAttribute(R.attr.auroraframeListMiddleBackground,
                outValue, true);
        mPreferenceBackgroundRes[FRAME_LIST_BACKGROUND_MIDDLE] = outValue.resourceId;
        context.getTheme().resolveAttribute(R.attr.auroraframeListBottomBackground,
                outValue, true);
        mPreferenceBackgroundRes[FRAME_LIST_BACKGROUND_BOTTOM] = outValue.resourceId;
    }
    // Gionee zhangxx 2012-12-12 add for CR00715173 end
    
    private void getCheckableFrameListBackground(Context context){
        mPreferenceBackgroundResForCheckable = new int[FRAME_LIST_BACKGROUND_TOTAL];
        mPreferenceBackgroundResForCheckable[FRAME_LIST_BACKGROUND_NULL] = 0;
        
        TypedValue outValue = new TypedValue();
        
        context.getTheme().resolveAttribute(R.attr.auroraCheckableframeListBackground,
                outValue, true);
        mPreferenceBackgroundResForCheckable[FRAME_LIST_BACKGROUND_FULL] = outValue.resourceId;
        context.getTheme().resolveAttribute(R.attr.auroraCheckableframeListTopBackground,
                outValue, true);
        mPreferenceBackgroundResForCheckable[FRAME_LIST_BACKGROUND_TOP] = outValue.resourceId;
        context.getTheme().resolveAttribute(R.attr.auroraCheckableframeListMiddleBackground,
                outValue, true);
        mPreferenceBackgroundResForCheckable[FRAME_LIST_BACKGROUND_MIDDLE] = outValue.resourceId;
        context.getTheme().resolveAttribute(R.attr.auroraCheckableframeListBottomBackground,
                outValue, true);
        mPreferenceBackgroundResForCheckable[FRAME_LIST_BACKGROUND_BOTTOM] = outValue.resourceId;
        
    }
    
    private void getCheckableTextColor(Context context){
       TypedValue outValue = new TypedValue();
        
        context.getTheme().resolveAttribute(R.attr.auroraCheckablePreferenceTitleColor,
                outValue, true);
        mTitleColor = outValue.resourceId;
        mTitleColor = context.getResources().getColor(mTitleColor);
        context.getTheme().resolveAttribute(R.attr.auroraCheckablePreferenceSummaryColor,
                outValue, true);
        mSummaryColor = outValue.resourceId;
        mSummaryColor = context.getResources().getColor(mSummaryColor);
    }
    
    private void flattenPreferenceGroup(List<AuroraPreference> preferences, AuroraPreferenceGroup group) {
        // TODO: shouldn't always?
        group.sortPreferences();

        final int groupSize = group.getPreferenceCount();
        for (int i = 0; i < groupSize; i++) {
            final AuroraPreference preference = group.getPreference(i);
            
            preferences.add(preference);
            
            if (!mHasReturnedViewTypeCount && !preference.hasSpecifiedLayout()) {
                addPreferenceClassName(preference);
            }
            
            if (preference instanceof AuroraPreferenceGroup) {
                final AuroraPreferenceGroup preferenceAsGroup = (AuroraPreferenceGroup) preference;
                if (preferenceAsGroup.isOnSameScreenAsChildren()) {
                    flattenPreferenceGroup(preferences, preferenceAsGroup);
                }
            }

            preference.setOnPreferenceChangeInternalListener(this);
        }
    }

    /**
     * Creates a string that includes the preference name, layout id and widget layout id.
     * If a particular preference type uses 2 different resources, they will be treated as
     * different view types.
     */
    private PreferenceLayout createPreferenceLayout(AuroraPreference preference, PreferenceLayout in) {
        PreferenceLayout pl = in != null? in : new PreferenceLayout();
        pl.name = preference.getClass().getName();
        pl.resId = preference.getLayoutResource();
        pl.widgetResId = preference.getWidgetLayoutResource();
        return pl;
    }

    private void addPreferenceClassName(AuroraPreference preference) {
        final PreferenceLayout pl = createPreferenceLayout(preference, null);
        int insertPos = Collections.binarySearch(mPreferenceLayouts, pl);

        // Only insert if it doesn't exist (when it is negative).
        if (insertPos < 0) {
            // Convert to insert index
            insertPos = insertPos * -1 - 1;
            mPreferenceLayouts.add(insertPos, pl);
        }
    }
    
    public int getCount() {
        return mPreferenceList.size();
    }

    public AuroraPreference getItem(int position) {
        if (position < 0 || position >= getCount()) return null;
        return mPreferenceList.get(position);
    }

    public long getItemId(int position) {
        if (position < 0 || position >= getCount()) return ListView.INVALID_ROW_ID;
        return this.getItem(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final AuroraPreference preference = this.getItem(position);
        // Build a PreferenceLayout to compare with known ones that are cacheable.
        mTempPreferenceLayout = createPreferenceLayout(preference, mTempPreferenceLayout);

        // If it's not one of the cached ones, set the convertView to null so that 
        // the layout gets re-created by the Preference.
        if (Collections.binarySearch(mPreferenceLayouts, mTempPreferenceLayout) < 0) {
            convertView = null;
        }

        // Gionee zhangxx 2012-12-12 add for CR00715173 begin
        if (mIsGioneeStyle) {
            View view = preference.getView(convertView, parent);
            TextView title = (TextView)view.findViewById(android.R.id.title);
            TextView summary = (TextView)view.findViewById(android.R.id.summary);
            if(preference instanceof AuroraCheckBoxPreference){
                if (mPreferenceBackgroundIndexs != null && mPreferenceBackgroundIndexs.length > position) {
                    if (mPreferenceBackgroundResForCheckable != null
                            && mPreferenceBackgroundIndexs[position] < mPreferenceBackgroundResForCheckable.length
                            && mPreferenceBackgroundResForCheckable[mPreferenceBackgroundIndexs[position]] > 0) {
                        view.setBackgroundResource(mPreferenceBackgroundResForCheckable[mPreferenceBackgroundIndexs[position]]);
                    }
                }
                if(title != null){
                    title.setTextColor(mTitleColor);
                }
                if(summary != null){
                    summary.setTextColor(mSummaryColor);
                }
            }else{
                if (mPreferenceBackgroundIndexs != null && mPreferenceBackgroundIndexs.length > position) {
                    if (mPreferenceBackgroundRes != null
                            && mPreferenceBackgroundIndexs[position] < mPreferenceBackgroundRes.length
                            && mPreferenceBackgroundRes[mPreferenceBackgroundIndexs[position]] > 0) {
                        view.setBackgroundResource(mPreferenceBackgroundRes[mPreferenceBackgroundIndexs[position]]);
                    }
                }
        }
            return view;
        } else {
        // Gionee zhangxx 2012-12-12 add for CR00715173 end
        return preference.getView(convertView, parent);
        // Gionee zhangxx 2012-12-12 add for CR00715173 begin
        }
        // Gionee zhangxx 2012-12-12 add for CR00715173 end
    }

    @Override
    public boolean isEnabled(int position) {
        if (position < 0 || position >= getCount()) return true;
        return this.getItem(position).isSelectable();
    }

    @Override
    public boolean areAllItemsEnabled() {
        // There should always be a preference group, and these groups are always
        // disabled
        return false;
    }

    public void onPreferenceChange(AuroraPreference preference) {
        notifyDataSetChanged();
    }

    public void onPreferenceHierarchyChange(AuroraPreference preference) {
        mHandler.removeCallbacks(mSyncRunnable);
        mHandler.post(mSyncRunnable);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getItemViewType(int position) {
        if (!mHasReturnedViewTypeCount) {
            mHasReturnedViewTypeCount = true;
        }
        
        final AuroraPreference preference = this.getItem(position);
        if (preference.hasSpecifiedLayout()) {
            return IGNORE_ITEM_VIEW_TYPE;
        }

        mTempPreferenceLayout = createPreferenceLayout(preference, mTempPreferenceLayout);

        int viewType = Collections.binarySearch(mPreferenceLayouts, mTempPreferenceLayout);
        if (viewType < 0) {
            // This is a class that was seen after we returned the count, so
            // don't recycle it.
            return IGNORE_ITEM_VIEW_TYPE;
        } else {
            return viewType;
        }
    }

    @Override
    public int getViewTypeCount() {
        if (!mHasReturnedViewTypeCount) {
            mHasReturnedViewTypeCount = true;
        }
        
        return Math.max(1, mPreferenceLayouts.size());
    }

}
