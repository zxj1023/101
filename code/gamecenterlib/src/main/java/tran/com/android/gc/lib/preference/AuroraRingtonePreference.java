package tran.com.android.gc.lib.preference;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.util.AttributeSet;

import tran.com.android.gc.lib.R;


/**
 * A {@link Preference} that allows the user to choose a ringtone from those on the device. 
 * The chosen ringtone's URI will be persisted as a string.
 * <p>
 * If the user chooses the "Default" item, the saved string will be one of
 * {@link System#DEFAULT_RINGTONE_URI},
 * {@link System#DEFAULT_NOTIFICATION_URI}, or
 * {@link System#DEFAULT_ALARM_ALERT_URI}. If the user chooses the "Silent"
 * item, the saved string will be an empty string.
 * 
 * @attr ref android.R.styleable#RingtonePreference_ringtoneType
 * @attr ref android.R.styleable#RingtonePreference_showDefault
 * @attr ref android.R.styleable#RingtonePreference_showSilent
 */
public class AuroraRingtonePreference extends AuroraPreference implements
AuroraPreferenceManager.OnActivityResultListener {

    private static final String TAG = "RingtonePreference";
    private static final String RINGTONE_PICKER = "gn.com.android.audioprofile.action.RINGTONE_PICKER";
    private int mRingtoneType;
    private boolean mShowDefault;
    private boolean mShowSilent;
    
    private int mRequestCode;

    public AuroraRingtonePreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        // Gionee <zhangxx> <2013-08-14> modify for CR00857086 begin
        if (NativePreferenceManager.getAnalyzeNativePreferenceXml() && attrs != null) {
            int i;
            String mTempStr;
            for (i=0; i<attrs.getAttributeCount(); i++) {
                int id = attrs.getAttributeNameResource(i);
                switch (id) {
                    case android.R.attr.ringtoneType:
                        mRingtoneType = attrs.getAttributeIntValue(i, RingtoneManager.TYPE_RINGTONE);
                        break;
                    case android.R.attr.showDefault:
                        mShowDefault = attrs.getAttributeBooleanValue(i, true);
                        break;
                    case android.R.attr.showSilent:
                        mShowSilent = attrs.getAttributeBooleanValue(i, true);
                        break;
                }
            }
        } else {
        // Gionee <zhangxx> <2013-08-14> modify for CR00857086 end
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.AuroraRingtonePreference, defStyle, 0);
            mRingtoneType = a.getInt(R.styleable.AuroraRingtonePreference_auroraringtoneType,
                    RingtoneManager.TYPE_RINGTONE);
            mShowDefault = a.getBoolean(R.styleable.AuroraRingtonePreference_aurorashowDefault,
                    true);
            mShowSilent = a.getBoolean(R.styleable.AuroraRingtonePreference_aurorashowSilent,
                    true);
            a.recycle();
        // Gionee <zhangxx> <2013-08-14> modify for CR00857086 begin
        }
        // Gionee <zhangxx> <2013-08-14> modify for CR00857086 end
    }

    public AuroraRingtonePreference(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.ringtonePreferenceStyle);
    }
    
    public AuroraRingtonePreference(Context context) {
        this(context, null);
    }

    /**
     * Returns the sound type(s) that are shown in the picker.
     * 
     * @return The sound type(s) that are shown in the picker.
     * @see #setRingtoneType(int)
     */
    public int getRingtoneType() {
        return mRingtoneType;
    }

    /**
     * Sets the sound type(s) that are shown in the picker.
     * 
     * @param type The sound type(s) that are shown in the picker.
     * @see RingtoneManager#EXTRA_RINGTONE_TYPE
     */
    public void setRingtoneType(int type) {
        mRingtoneType = type;
    }

    /**
     * Returns whether to a show an item for the default sound/ringtone.
     * 
     * @return Whether to show an item for the default sound/ringtone.
     */
    public boolean getShowDefault() {
        return mShowDefault;
    }

    /**
     * Sets whether to show an item for the default sound/ringtone. The default
     * to use will be deduced from the sound type(s) being shown.
     * 
     * @param showDefault Whether to show the default or not.
     * @see RingtoneManager#EXTRA_RINGTONE_SHOW_DEFAULT
     */
    public void setShowDefault(boolean showDefault) {
        mShowDefault = showDefault;
    }

    /**
     * Returns whether to a show an item for 'Silent'.
     * 
     * @return Whether to show an item for 'Silent'.
     */
    public boolean getShowSilent() {
        return mShowSilent;
    }

    /**
     * Sets whether to show an item for 'Silent'.
     * 
     * @param showSilent Whether to show 'Silent'.
     * @see RingtoneManager#EXTRA_RINGTONE_SHOW_SILENT
     */
    public void setShowSilent(boolean showSilent) {
        mShowSilent = showSilent;
    }

    @Override
    protected void onClick() {
        // Launch the ringtone picker
//        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        Intent intent = new Intent(RINGTONE_PICKER);
        onPrepareRingtonePickerIntent(intent);
        AuroraPreferenceFragment owningFragment = getPreferenceManager().getFragment();
        if (owningFragment != null) {
            owningFragment.startActivityForResult(intent, mRequestCode);
        } else {
            getPreferenceManager().getActivity().startActivityForResult(intent, mRequestCode);
        }
    }

    /**
     * Prepares the intent to launch the ringtone picker. This can be modified
     * to adjust the parameters of the ringtone picker.
     * 
     * @param ringtonePickerIntent The ringtone picker intent that can be
     *            modified by putting extras.
     */
    protected void onPrepareRingtonePickerIntent(Intent ringtonePickerIntent) {

        ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
                onRestoreRingtone());
        
        ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, mShowDefault);
        if (mShowDefault) {
            ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI,
                    RingtoneManager.getDefaultUri(getRingtoneType()));
        }

        ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, mShowSilent);
        ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, mRingtoneType);
        ringtonePickerIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, getTitle());
    }
    
    /**
     * Called when a ringtone is chosen.
     * <p>
     * By default, this saves the ringtone URI to the persistent storage as a
     * string.
     * 
     * @param ringtoneUri The chosen ringtone's {@link Uri}. Can be null.
     */
    protected void onSaveRingtone(Uri ringtoneUri) {
        persistString(ringtoneUri != null ? ringtoneUri.toString() : "");
    }

    /**
     * Called when the chooser is about to be shown and the current ringtone
     * should be marked. Can return null to not mark any ringtone.
     * <p>
     * By default, this restores the previous ringtone URI from the persistent
     * storage.
     * 
     * @return The ringtone to be marked as the current ringtone.
     */
    protected Uri onRestoreRingtone() {
        final String uriString = getPersistedString(null);
        return !TextUtils.isEmpty(uriString) ? Uri.parse(uriString) : null;
    }
    
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValueObj) {
        String defaultValue = (String) defaultValueObj;
        
        /*
         * This method is normally to make sure the internal state and UI
         * matches either the persisted value or the default value. Since we
         * don't show the current value in the UI (until the dialog is opened)
         * and we don't keep local state, if we are restoring the persisted
         * value we don't need to do anything.
         */
        if (restorePersistedValue) {
            return;
        }
        
        // If we are setting to the default value, we should persist it.
        if (!TextUtils.isEmpty(defaultValue)) {
            onSaveRingtone(Uri.parse(defaultValue));
        }
    }

    @Override
    protected void onAttachedToHierarchy(AuroraPreferenceManager preferenceManager) {
        super.onAttachedToHierarchy(preferenceManager);
        
        preferenceManager.registerOnActivityResultListener(this);
        mRequestCode = preferenceManager.getNextRequestCode();
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        
        if (requestCode == mRequestCode) {
            
            if (data != null) {
                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                
                if (callChangeListener(uri != null ? uri.toString() : "")) {
                    onSaveRingtone(uri);
                }
            }
            
            return true;
        }
        
        return false;
    }

}
