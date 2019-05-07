package tran.com.android.gc.lib.preference;


import tran.com.android.gc.lib.app.AuroraAlertDialog.Builder;
import tran.com.android.gc.lib.R;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;



/**
 * A {@link Preference} that displays a list of entries as
 * a dialog.
 * <p>
 * This preference will store a string into the SharedPreferences. This string will be the value
 * from the {@link #setEntryValues(CharSequence[])} array.
 * 
 * @attr ref android.R.styleable#ListPreference_entries
 * @attr ref android.R.styleable#ListPreference_entryValues
 */
public class AuroraListPreference extends AuroraDialogPreference {
    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;
    private String mValue;
    private String mSummary;
    private int mClickedDialogEntryIndex;
    private int mListLayoutRes; 
    private Context mContext;
    private ListPreferenceAdapter mDataAdapter;
    public AuroraListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mListLayoutRes= R.layout.aurora_list_preference;
        mContext = context;
        // Gionee <zhangxx> <2013-08-14> add for CR00857086 begin
        if (NativePreferenceManager.getAnalyzeNativePreferenceXml() && attrs != null) {
            int i;
            String mTempStr;
            for (i=0; i<attrs.getAttributeCount(); i++) {
                int id = attrs.getAttributeNameResource(i);
                switch (id) {
                    case android.R.attr.entries:
                        mEntries = NativePreferenceManager.getAttributeStringArrayValue(context, attrs, i);
                        break;
                    case android.R.attr.entryValues:
                        mEntryValues = NativePreferenceManager.getAttributeStringArrayValue(context, attrs, i);
                        break;
                    case android.R.attr.summary:
                        mSummary = NativePreferenceManager.getAttributeStringValue(context, attrs, i);
                        break;
                }
            }
        } else {
        // Gionee <zhangxx> <2013-08-14> add for CR00857086 end
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.AuroraListPreference, 0, 0);
            mEntries = a.getTextArray(R.styleable.AuroraListPreference_auroraentries);
            mEntryValues = a.getTextArray(R.styleable.AuroraListPreference_auroraentryValues);
            a.recycle();
            
            /* Retrieve the Preference summary attribute since it's private
             * in the Preference class.
             */
            a = context.obtainStyledAttributes(attrs,
                    R.styleable.AuroraPreference, 0, 0);
            mSummary = a.getString(R.styleable.AuroraPreference_aurorasummary);
            a.recycle();
        // Gionee <zhangxx> <2013-08-14> add for CR00857086 begin
        }
        // Gionee <zhangxx> <2013-08-14> add for CR00857086 end
    }

    public AuroraListPreference(Context context) {
        this(context, null);
    }

    /**
     * Sets the human-readable entries to be shown in the list. This will be
     * shown in subsequent dialogs.
     * <p>
     * Each entry must have a corresponding index in
     * {@link #setEntryValues(CharSequence[])}.
     * 
     * @param entries The entries.
     * @see #setEntryValues(CharSequence[])
     */
    public void setEntries(CharSequence[] entries) {
        mEntries = entries;
    }
    
    /**
     * @see #setEntries(CharSequence[])
     * @param entriesResId The entries array as a resource.
     */
    public void setEntries(int entriesResId) {
        setEntries(getContext().getResources().getTextArray(entriesResId));
    }
    
    /**
     * The list of entries to be shown in the list in subsequent dialogs.
     * 
     * @return The list as an array.
     */
    public CharSequence[] getEntries() {
        return mEntries;
    }
    
    /**
     * The array to find the value to save for a preference when an entry from
     * entries is selected. If a user clicks on the second item in entries, the
     * second item in this array will be saved to the preference.
     * 
     * @param entryValues The array to be used as values to save for the preference.
     */
    public void setEntryValues(CharSequence[] entryValues) {
        mEntryValues = entryValues;
    }

    /**
     * @see #setEntryValues(CharSequence[])
     * @param entryValuesResId The entry values array as a resource.
     */
    public void setEntryValues(int entryValuesResId) {
        setEntryValues(getContext().getResources().getTextArray(entryValuesResId));
    }
    
    @Override
    protected View onCreateDialogView() {
         LayoutInflater inflater = LayoutInflater.from(mContext);
         return inflater.inflate(mListLayoutRes, null);
    }
    
    
    /**
     * Returns the array of values to be saved for the preference.
     * 
     * @return The array of values.
     */
    public CharSequence[] getEntryValues() {
        return mEntryValues;
    }

    /**
     * Sets the value of the key. This should be one of the entries in
     * {@link #getEntryValues()}.
     * 
     * @param value The value to set for the key.
     */
    public void setValue(String value) {
        mValue = value;
        
        persistString(value);
    }

    /**
     * Returns the summary of this ListPreference. If the summary
     * has a {@linkplain java.lang.String#format String formatting}
     * marker in it (i.e. "%s" or "%1$s"), then the current entry
     * value will be substituted in its place.
     *
     * @return the summary with appropriate string substitution
     */
    @Override
    public CharSequence getSummary() {
        final CharSequence entry = getEntry();
        if (mSummary == null || entry == null) {
            return super.getSummary();
        } else {
            return String.format(mSummary, entry);
        }
    }

    /**
     * Sets the summary for this Preference with a CharSequence.
     * If the summary has a
     * {@linkplain java.lang.String#format String formatting}
     * marker in it (i.e. "%s" or "%1$s"), then the current entry
     * value will be substituted in its place when it's retrieved.
     *
     * @param summary The summary for the preference.
     */
    @Override
    public void setSummary(CharSequence summary) {
        super.setSummary(summary);
        if (summary == null && mSummary != null) {
            mSummary = null;
        } else if (summary != null && !summary.equals(mSummary)) {
            mSummary = summary.toString();
        }
    }

    /**
     * Sets the value to the given index from the entry values.
     * 
     * @param index The index of the value to set.
     */
    public void setValueIndex(int index) {
        if (mEntryValues != null) {
            setValue(mEntryValues[index].toString());
        }
    }
    
    /**
     * Returns the value of the key. This should be one of the entries in
     * {@link #getEntryValues()}.
     * 
     * @return The value of the key.
     */
    public String getValue() {
        return mValue; 
    }
    
    /**
     * Returns the entry corresponding to the current value.
     * 
     * @return The entry corresponding to the current value, or null.
     */
    public CharSequence getEntry() {
        int index = getValueIndex();
        return index >= 0 && mEntries != null ? mEntries[index] : null;
    }
    
    /**
     * Returns the index of the given value (in the entry values array).
     * 
     * @param value The value whose index should be returned.
     * @return The index of the value, or -1 if not found.
     */
    public int findIndexOfValue(String value) {
        if (value != null && mEntryValues != null) {
            for (int i = mEntryValues.length - 1; i >= 0; i--) {
                if (mEntryValues[i].equals(value)) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    private int getValueIndex() {
        return findIndexOfValue(mValue);
    }
    
    
    
    @Override
    protected void onPrepareDialogBuilder(Builder builder) {
        super.onPrepareDialogBuilder(builder);
        
        if (mEntries == null || mEntryValues == null) {
            throw new IllegalStateException(
                    "ListPreference requires an entries array and an entryValues array.");
        }
        
//        setDialogLayoutResource(mListLayoutRes);

        mClickedDialogEntryIndex = getValueIndex();
//        mDataAdapter = new ListPreferenceAdapter(mContext, mEntries);
//        builder.setAdapter(mDataAdapter, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                mClickedDialogEntryIndex = which;
//
//                /*
//                 * Clicking on an item simulates the positive button
//                 * click, and dismisses the dialog.
//                 */
//                AuroraListPreference.this.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
//                dialog.dismiss();
//            }
//        }).setShowTitleDivider(true);
        builder.setSingleChoiceItems(mEntries, mClickedDialogEntryIndex, 
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mClickedDialogEntryIndex = which;

                        /*
                         * Clicking on an item simulates the positive button
                         * click, and dismisses the dialog.
                         */
                        AuroraListPreference.this.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        dialog.dismiss();
                    }
        }).setTitleDividerVisible(true);
        
        /*
         * The typical interaction for list-based dialogs is to have
         * click-on-an-item dismiss the dialog instead of the user having to
         * press 'Ok'.
         */
        
        builder.setPositiveButton(null, null);
    }
   
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        
        if (positiveResult && mClickedDialogEntryIndex >= 0 && mEntryValues != null) {
            String value = mEntryValues[mClickedDialogEntryIndex].toString();
            if (callChangeListener(value)) {
                setValue(value);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setValue(restoreValue ? getPersistedString(mValue) : (String) defaultValue);
    }
    
    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }
        
        final SavedState myState = new SavedState(superState);
        myState.value = getValue();
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
        setValue(myState.value);
    }
    
    private static class SavedState extends BaseSavedState {
        String value;
        
        public SavedState(Parcel source) {
            super(source);
            value = source.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(value);
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
    
    class ListPreferenceAdapter extends BaseAdapter{
        private CharSequence[] datas;
        private LayoutInflater inflater;
        private Context con;
        public ListPreferenceAdapter(Context context,CharSequence[] entries){
            this.datas = entries;
            con = context;
            inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return datas!=null?datas.length:0;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return datas!=null?datas[position]:null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        
        
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder;
            if(convertView == null){
                holder = new Holder();
                convertView = convertView.inflate(con, R.layout.aurora_list_preference_item, null);
                holder.radio = (RadioButton)convertView.findViewById(R.id.aurora_list_preference_radio);
                holder.text = (TextView)convertView.findViewById(android.R.id.text1);
                convertView.setTag(holder);
            }else{
                holder = (Holder) convertView.getTag();
            }
            holder.text.setText(datas[position]);
//            final RadioButton radio = holder.radio;
//            convertView.setOnClickListener(new OnClickListener() {
//                
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    radio.setChecked(true);
//                }
//            });
            
            return convertView;
        }
        
        class Holder{
            RadioButton radio;
            TextView text;
        }
        
    }
    
}
