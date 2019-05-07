package tran.com.android.gc.lib.preference;

import tran.com.android.gc.lib.app.AuroraAlertDialog.Builder;
import tran.com.android.gc.lib.R;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;

import java.util.HashSet;
import java.util.Set;


/**
 * A {@link Preference} that displays a list of entries as
 * a dialog.
 * <p>
 * This preference will store a set of strings into the SharedPreferences.
 * This set will contain one or more values from the
 * {@link #setEntryValues(CharSequence[])} array.
 * 
 * @attr ref android.R.styleable#MultiSelectListPreference_entries
 * @attr ref android.R.styleable#MultiSelectListPreference_entryValues
 */
public class AuroraMultiSelectListPreference extends AuroraDialogPreference {
    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;
    private Set<String> mValues = new HashSet<String>();
    private Set<String> mNewValues = new HashSet<String>();
    private boolean mPreferenceChanged;
    
    public AuroraMultiSelectListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

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
                }
            }
        } else {
        // Gionee <zhangxx> <2013-08-14> add for CR00857086 end
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.AuroraMultiSelectListPreference, 0, 0);
            mEntries = a.getTextArray(R.styleable.AuroraMultiSelectListPreference_auroraentries);
            mEntryValues = a.getTextArray(R.styleable.AuroraMultiSelectListPreference_auroraentryValues);
            a.recycle();
        // Gionee <zhangxx> <2013-08-14> add for CR00857086 begin
        }
        // Gionee <zhangxx> <2013-08-14> add for CR00857086 end
    }
    
    public AuroraMultiSelectListPreference(Context context) {
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
    
    /**
     * Returns the array of values to be saved for the preference.
     * 
     * @return The array of values.
     */
    public CharSequence[] getEntryValues() {
        return mEntryValues;
    }
    
    /**
     * Sets the value of the key. This should contain entries in
     * {@link #getEntryValues()}.
     * 
     * @param values The values to set for the key.
     */
    public void setValues(Set<String> values) {
        mValues.clear();
        mValues.addAll(values);

        persistStringSet(values);
    }
    
    /**
     * Retrieves the current value of the key.
     */
    public Set<String> getValues() {
        return mValues;
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
    
    @Override
    protected void onPrepareDialogBuilder(Builder builder) {
        super.onPrepareDialogBuilder(builder);
        
        if (mEntries == null || mEntryValues == null) {
            throw new IllegalStateException(
                    "MultiSelectListPreference requires an entries array and " +
                    "an entryValues array.");
        }
        
        boolean[] checkedItems = getSelectedItems();
        builder.setMultiChoiceItems(mEntries, checkedItems,
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            mPreferenceChanged |= mNewValues.add(mEntryValues[which].toString());
                        } else {
                            mPreferenceChanged |= mNewValues.remove(mEntryValues[which].toString());
                        }
                    }
                });
        mNewValues.clear();
        mNewValues.addAll(mValues);
    }
    
    private boolean[] getSelectedItems() {
        final CharSequence[] entries = mEntryValues;
        final int entryCount = entries.length;
        final Set<String> values = mValues;
        boolean[] result = new boolean[entryCount];
        
        for (int i = 0; i < entryCount; i++) {
            result[i] = values.contains(entries[i].toString());
        }
        
        return result;
    }
    
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        
        if (positiveResult && mPreferenceChanged) {
            final Set<String> values = mNewValues;
            if (callChangeListener(values)) {
                setValues(values);
            }
        }
        mPreferenceChanged = false;
    }
    
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        final CharSequence[] defaultValues = a.getTextArray(index);
        final int valueCount = defaultValues.length;
        final Set<String> result = new HashSet<String>();
        
        for (int i = 0; i < valueCount; i++) {
            result.add(defaultValues[i].toString());
        }
        
        return result;
    }
    
    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setValues(restoreValue ? getPersistedStringSet(mValues) : (Set<String>) defaultValue);
    }
    
    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state
            return superState;
        }
        
        final SavedState myState = new SavedState(superState);
        myState.values = getValues();
        return myState;
    }
    
    private static class SavedState extends BaseSavedState {
        Set<String> values;
        
        public SavedState(Parcel source) {
            super(source);
            values = new HashSet<String>();
            // zhangxx 2013-06-29 test begin
            // String[] strings = source.readStringArray();
            String[] strings = new String[0];
            // zhangxx 2013-06-29 test end
            
            final int stringCount = strings.length;
            for (int i = 0; i < stringCount; i++) {
                values.add(strings[i]);
            }
        }
        
        public SavedState(Parcelable superState) {
            super(superState);
        }
        
        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeStringArray(values.toArray(new String[0]));
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
