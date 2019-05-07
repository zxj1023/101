package tran.com.android.gc.lib.preference;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Used to group {@link Preference} objects
 * and provide a disabled title above the group.
 * 
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For information about building a settings UI with Preferences,
 * read the <a href="{@docRoot}guide/topics/ui/settings.html">Settings</a>
 * guide.</p>
 * </div>
 */
public class AuroraPreferenceCategory extends AuroraPreferenceGroup {
    private static final String TAG = "PreferenceCategory";
    public static final int CATEGORY_ZERO_HEIGHT = 1;
    public AuroraPreferenceCategory(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AuroraPreferenceCategory(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.preferenceCategoryStyle);
    }

    public AuroraPreferenceCategory(Context context) {
        this(context, null);
    }
    
    @Override
    protected boolean onPrepareAddPreference(AuroraPreference preference) {
        if (preference instanceof AuroraPreferenceCategory) {
            throw new IllegalArgumentException(
                    "Cannot add a " + TAG + " directly to a " + TAG);
        }
        
        return super.onPrepareAddPreference(preference);
    }

    public void setHeight(int height){
        mPreferenceHeight = height;
    }
    
    
    @Override
    public boolean isEnabled() {
        return false;
    }
    
}
