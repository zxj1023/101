package tran.com.android.gc.lib.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

import tran.com.android.gc.lib.widget.AuroraCheckBox;
import tran.com.android.gc.lib.R;


/**
 * A {@link Preference} that provides checkbox widget
 * functionality.
 * <p>
 * This preference will store a boolean into the SharedPreferences.
 * 
 * @attr ref android.R.styleable#CheckBoxPreference_summaryOff
 * @attr ref android.R.styleable#CheckBoxPreference_summaryOn
 * @attr ref android.R.styleable#CheckBoxPreference_disableDependentsState
 */
public class AuroraCheckBoxPreference extends AuroraTwoStatePreference {
    
    private boolean mIsClicked = false;
    
    private boolean mAttached;

    public AuroraCheckBoxPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Gionee <zhangxx> <2013-08-14> add for CR00857086 begin
        if (NativePreferenceManager.getAnalyzeNativePreferenceXml() && attrs != null) {
            int i;
            for (i=0; i<attrs.getAttributeCount(); i++) {
                int id = attrs.getAttributeNameResource(i);
                switch (id) {
                    case android.R.attr.summaryOn:
                        setSummaryOn(NativePreferenceManager.getAttributeStringValue(context, attrs, i));
                        break;
                    case android.R.attr.summaryOff:
                        setSummaryOff(NativePreferenceManager.getAttributeStringValue(context, attrs, i));
                        break;
                    case android.R.attr.disableDependentsState:
                        setDisableDependentsState(attrs.getAttributeBooleanValue(i, false));
                        break;
                }
            }
            setWidgetLayoutResource(R.layout.aurora_preference_widget_checkbox);
        } else {
        // Gionee <zhangxx> <2013-08-14> add for CR00857086 end
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.AuroraCheckBoxPreference, defStyle, 0);
            setSummaryOn(a.getString(R.styleable.AuroraCheckBoxPreference_aurorasummaryOn));
            setSummaryOff(a.getString(R.styleable.AuroraCheckBoxPreference_aurorasummaryOff));
            setDisableDependentsState(a.getBoolean(
                    R.styleable.AuroraCheckBoxPreference_auroradisableDependentsState, false));
            a.recycle();
        // Gionee <zhangxx> <2013-08-14> add for CR00857086 begin
        }
        // Gionee <zhangxx> <2013-08-14> add for CR00857086 end
    }

    public AuroraCheckBoxPreference(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.checkBoxPreferenceStyle);
    }

    public AuroraCheckBoxPreference(Context context) {
        this(context, null);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        // TODO Auto-generated method stub
        return super.onCreateView(parent);
    }
    
    
    @Override
    protected void onAttachedToActivity() {
        // TODO Auto-generated method stub
        Log.e("attach", "onAttachedToActivity");
        mAttached = true;
        super.onAttachedToActivity();
    }
    
    @Override
    protected void onClick() {
        // TODO Auto-generated method stub
        mIsClicked = true;
        super.onClick();
    }
    
    
    
    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        AuroraCheckBox checkboxView = (AuroraCheckBox) view.findViewById(android.R.id.checkbox);
        if (checkboxView != null && checkboxView instanceof Checkable) {
//            if(checkboxView instanceof CheckBox){
//                ((Checkable) checkboxView).setChecked(mChecked);
//            }
            int[] location = new int[2];
//            checkboxView.getLocationOnScreen(mHistoryLocation);
//            Log.e("yy", "scrollY:"+mHistoryLocation[1]);
            if(checkboxView instanceof AuroraCheckBox){
                if(mAttached){
                    checkboxView.getLocationOnScreen(mHistoryLocation);
                    checkboxView.setChecked(mChecked);
                    mAttached = false;
                }else{
                    if(mIsClicked){
                        checkboxView.auroraSetChecked(mChecked,true);
                        mIsClicked = false;
                    }else{
                        checkboxView.setChecked(mChecked);
                    }
//                    checkboxView.getLocationOnScreen(location);
//                    if(location[1] < 0){
//                        checkboxView.setChecked(mChecked);
//                    }else{
//                        checkboxView.auroraSetChecked(mChecked,true);
//                    }
                 
                }
            }
            sendAccessibilityEvent(checkboxView);
        }

        syncSummaryView(view);
    }
    
    int[] mHistoryLocation = new int[2];
}
