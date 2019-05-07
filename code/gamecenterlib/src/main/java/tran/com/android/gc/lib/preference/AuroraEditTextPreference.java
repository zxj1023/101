package tran.com.android.gc.lib.preference;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import tran.com.android.gc.lib.widget.AuroraEditText;
import tran.com.android.gc.lib.R;

/**
 * A {@link Preference} that allows for string
 * input.
 * <p>
 * It is a subclass of {@link DialogPreference} and shows the {@link AuroraEditText}
 * in a dialog. This {@link AuroraEditText} can be modified either programmatically
 * via {@link #getEditText()}, or through XML by setting any AuroraEditText
 * attributes on the EditTextPreference.
 * <p>
 * This preference will store a string into the SharedPreferences.
 * <p>
 * See {@link android.R.styleable#AuroraEditText AuroraEditText Attributes}.
 */
public class AuroraEditTextPreference extends AuroraDialogPreference {
    /**
     * The edit text shown in the dialog.
     */
    private AuroraEditText mEditText;
    
    private String mText;
    
    private float mEditTextTextSize = 15f;
    
    public AuroraEditTextPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        mEditText = new AuroraEditText(context, attrs);
        
        // Give it an ID so it can be saved/restored
        mEditText.setId(android.R.id.edit);
        
        /*
         * The preference framework and view framework both have an 'enabled'
         * attribute. Most likely, the 'enabled' specified in this XML is for
         * the preference framework, but it was also given to the view framework.
         * We reset the enabled state.
         */
        mEditText.setEnabled(true);
    }

    public AuroraEditTextPreference(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextPreferenceStyle);
    }

    public AuroraEditTextPreference(Context context) {
        this(context, null);
    }
    
    /**
     * Saves the text to the {@link SharedPreferences}.
     * 
     * @param text The text to save
     */
    public void setText(String text) {
        final boolean wasBlocking = shouldDisableDependents();
        
        mText = text;
        
        persistString(text);
        
        final boolean isBlocking = shouldDisableDependents(); 
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking);
        }
    }
    
    /**
     * Gets the text from the {@link SharedPreferences}.
     * 
     * @return The current preference value.
     */
    public String getText() {
        return mText;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        AuroraEditText editText = mEditText;
        editText.setText(getText());
        
        ViewParent oldParent = editText.getParent();
        if (oldParent != view) {
            if (oldParent != null) {
                ((ViewGroup) oldParent).removeView(editText);
            }
            onAddEditTextToDialogView(view, editText);
        }
    }

    /**
     * Adds the AuroraEditText widget of this preference to the dialog's view.
     * 
     * @param dialogView The dialog view.
     */
    protected void onAddEditTextToDialogView(View dialogView, AuroraEditText editText) {
        ViewGroup container = (ViewGroup) dialogView
                .findViewById(R.id.aurora_edittext_container);
        if (container != null) {
        	editText.setTextSize(mEditTextTextSize);
            container.addView(editText, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
    
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        
        if (positiveResult) {
            String value = mEditText.getText().toString();
            if (callChangeListener(value)) {
                setText(value);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setText(restoreValue ? getPersistedString(mText) : (String) defaultValue);
    }

    @Override
    public boolean shouldDisableDependents() {
        return TextUtils.isEmpty(mText) || super.shouldDisableDependents();
    }

    /**
     * Returns the {@link AuroraEditText} widget that will be shown in the dialog.
     * 
     * @return The {@link AuroraEditText} widget that will be shown in the dialog.
     */
    public AuroraEditText getEditText() {
        return mEditText;
    }

    /** @hide */
    @Override
    protected boolean needInputMethod() {
        // We want the input method to show, if possible, when dialog is displayed
        return true;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }
        
        final SavedState myState = new SavedState(superState);
        myState.text = getText();
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
        setText(myState.text);
    }
    
    private static class SavedState extends BaseSavedState {
        String text;
        
        public SavedState(Parcel source) {
            super(source);
            text = source.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(text);
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
