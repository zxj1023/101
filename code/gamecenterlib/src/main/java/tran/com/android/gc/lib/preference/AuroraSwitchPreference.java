
package tran.com.android.gc.lib.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.CompoundButton;
import tran.com.android.gc.lib.widget.AuroraSwitch;
import tran.com.android.gc.lib.R;

import tran.com.android.gc.lib.utils.Constants;

import android.util.Log;

/**
 * A {@link Preference} that provides a two-state toggleable option.
 * <p>
 * This preference will store a boolean into the SharedPreferences.
 * 
 * @attr ref android.R.styleable#SwitchPreference_summaryOff
 * @attr ref android.R.styleable#SwitchPreference_summaryOn
 * @attr ref android.R.styleable#SwitchPreference_switchTextOff
 * @attr ref android.R.styleable#SwitchPreference_switchTextOn
 * @attr ref android.R.styleable#SwitchPreference_disableDependentsState
 */
public class AuroraSwitchPreference extends AuroraTwoStatePreference {
    // Switch text for on and off states
    private CharSequence mSwitchOn;
    private CharSequence mSwitchOff;
    private final Listener mListener = new Listener();
    private boolean isUserListener = true;
//    private View checkableView;

    private class Listener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!callChangeListener(isChecked)) {
                // Listener didn't like it, change it back.
                // CompoundButton will make sure we don't recurse.
                buttonView.setChecked(!isChecked);
                Log.i("luofu", "checked:"+isChecked);
                return;
            }

            AuroraSwitchPreference.this.setChecked(isChecked);
        }
    }

    /**
     * Construct a new SwitchPreference with the given style options.
     * 
     * @param context The Context that will style this preference
     * @param attrs Style attributes that differ from the default
     * @param defStyle Theme attribute defining the default style options
     */
    public AuroraSwitchPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Gionee <zhangxx> <2013-08-14> modify for CR00857086 begin
        if (NativePreferenceManager.getAnalyzeNativePreferenceXml() && attrs != null) {
            int i;
            for (i = 0; i < attrs.getAttributeCount(); i++) {
                int id = attrs.getAttributeNameResource(i);
                switch (id) {
                    case android.R.attr.summaryOn:
                        setSummaryOn(NativePreferenceManager.getAttributeStringValue(context,
                                attrs, i));
                        break;
                    case android.R.attr.summaryOff:
                        setSummaryOff(NativePreferenceManager.getAttributeStringValue(context,
                                attrs, i));
                        break;
                    case android.R.attr.disableDependentsState:
                        setDisableDependentsState(attrs.getAttributeBooleanValue(i, false));
                        break;
                }
            }
            setSwitchTextOn(R.string.aurora_capital_on);
            setSwitchTextOff(R.string.aurora_capital_off);
            setWidgetLayoutResource(R.layout.aurora_preference_widget_switch);
        } else {
            // Gionee <zhangxx> <2013-08-14> modify for CR00857086 end
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.AuroraSwitchPreference, defStyle, 0);
            setSummaryOn(a
                    .getString(R.styleable.AuroraSwitchPreference_aurorasummaryOn));
            setSummaryOff(a
                    .getString(R.styleable.AuroraSwitchPreference_aurorasummaryOff));
            setSwitchTextOn(a.getString(
                    R.styleable.AuroraSwitchPreference_auroraswitchTextOn));
            setSwitchTextOff(a.getString(
                    R.styleable.AuroraSwitchPreference_auroraswitchTextOff));
            setDisableDependentsState(a
                    .getBoolean(
                            R.styleable.AuroraSwitchPreference_auroradisableDependentsState,
                            false));
            a.recycle();
            // Gionee <zhangxx> <2013-08-14> modify for CR00857086 begin
        }
        setSelectable(false);
        // Gionee <zhangxx> <2013-08-14> modify for CR00857086 end
    }

    /**
     * Construct a new SwitchPreference with the given style options.
     * 
     * @param context The Context that will style this preference
     * @param attrs Style attributes that differ from the default
     */
    public AuroraSwitchPreference(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.switchPreferenceStyle);
    }

    /**
     * Construct a new SwitchPreference with default style options.
     * 
     * @param context The Context that will style this preference
     */
    public AuroraSwitchPreference(Context context) {
        this(context, null);
    }
    
    public void useSystemListener(boolean flag){
        isUserListener = flag;
    }
    
    @Override
    protected void onAttachedToActivity() {
        // TODO Auto-generated method stub
        Constants.PREFERENCE_ATTACHED = true;
        super.onAttachedToActivity();
    }
    

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        if(Constants.PREFERENCE_ATTACHED){
            bind(view);
        }else{
        View checkableView = view.findViewById(R.id.aurora_switchWidget);
        if (checkableView != null && checkableView instanceof Checkable) {
            if (checkableView instanceof AuroraSwitch) {
                final AuroraSwitch switchView = (AuroraSwitch) checkableView;
                switchView.setTextOn(mSwitchOn);
                switchView.setTextOff(mSwitchOff);
                if(isUserListener){
                    switchView.setOnCheckedChangeListener(mListener);
                }
                
            }
            ((Checkable) checkableView).setChecked(mChecked);

            sendAccessibilityEvent(checkableView);

           
        }

        syncSummaryView(view);
        }
    }

    private void bind(View view){
        View checkableView = view.findViewById(R.id.aurora_switchWidget);
        if (checkableView != null && checkableView instanceof Checkable) {
            ((Checkable) checkableView).setChecked(mChecked);
            if (checkableView instanceof AuroraSwitch) {
                final AuroraSwitch switchView = (AuroraSwitch) checkableView;
                switchView.setTextOn(mSwitchOn);
                switchView.setTextOff(mSwitchOff);
                if(isUserListener){
                    switchView.setOnCheckedChangeListener(mListener);
                }
                
            }
            

            sendAccessibilityEvent(checkableView);

           
        }

        syncSummaryView(view);
    }
    
    // add by <flash> 2013.10.15 start
//    @Override
//    protected void onClick() {
//        final AuroraSwitch switchView = (AuroraSwitch) checkableView;
//        if (switchView != null)
//        {
//            // Log.d("liuwei","switchView != null !!!");
//            switchView.performClick();
//        }
//    }

    // end

    /**
     * Set the text displayed on the switch widget in the on state. This should
     * be a very short string; one word if possible.
     * 
     * @param onText Text to display in the on state
     */
    public void setSwitchTextOn(CharSequence onText) {
        mSwitchOn = onText;
        notifyChanged();
    }

    /**
     * Set the text displayed on the switch widget in the off state. This should
     * be a very short string; one word if possible.
     * 
     * @param offText Text to display in the off state
     */
    public void setSwitchTextOff(CharSequence offText) {
        mSwitchOff = offText;
        notifyChanged();
    }

    /**
     * Set the text displayed on the switch widget in the on state. This should
     * be a very short string; one word if possible.
     * 
     * @param resId The text as a string resource ID
     */
    public void setSwitchTextOn(int resId) {
        setSwitchTextOn(getContext().getString(resId));
    }

    /**
     * Set the text displayed on the switch widget in the off state. This should
     * be a very short string; one word if possible.
     * 
     * @param resId The text as a string resource ID
     */
    public void setSwitchTextOff(int resId) {
        setSwitchTextOff(getContext().getString(resId));
    }

    /**
     * @return The text that will be displayed on the switch widget in the on
     *         state
     */
    public CharSequence getSwitchTextOn() {
        return mSwitchOn;
    }

    /**
     * @return The text that will be displayed on the switch widget in the off
     *         state
     */
    public CharSequence getSwitchTextOff() {
        return mSwitchOff;
    }
}
