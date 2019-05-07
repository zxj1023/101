package tran.com.android.gc.lib.preference;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import tran.com.android.gc.lib.utils.AuroraXmlUtils;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

/**
 * The {@link PreferenceInflater} is used to inflate preference hierarchies from
 * XML files.
 * <p>
 * Do not construct this directly, instead use
 * {@link Context#getSystemService(String)} with
 * {@link Context#PREFERENCE_INFLATER_SERVICE}.
 */
class AuroraPreferenceInflater extends AuroraGenericInflater<AuroraPreference, AuroraPreferenceGroup> {
    private static final String TAG = "PreferenceInflater";
    private static final String INTENT_TAG_NAME = "intent";
    private static final String EXTRA_TAG_NAME = "extra";

    private AuroraPreferenceManager mPreferenceManager;
    
    public AuroraPreferenceInflater(Context context, AuroraPreferenceManager preferenceManager) {
        super(context);
        init(preferenceManager);
    }

    AuroraPreferenceInflater(AuroraGenericInflater<AuroraPreference, AuroraPreferenceGroup> original, AuroraPreferenceManager preferenceManager, Context newContext) {
        super(original, newContext);
        init(preferenceManager);
    }

    @Override
    public AuroraGenericInflater<AuroraPreference, AuroraPreferenceGroup> cloneInContext(Context newContext) {
        return new AuroraPreferenceInflater(this, mPreferenceManager, newContext);
    }
    
    private void init(AuroraPreferenceManager preferenceManager) {
        mPreferenceManager = preferenceManager;
        setDefaultPackage("tran.com.android.gc.lib.preference.");
    }

    @Override
    protected boolean onCreateCustomFromTag(XmlPullParser parser, AuroraPreference parentPreference,
            AttributeSet attrs) throws XmlPullParserException {
        final String tag = parser.getName();
        
        if (tag.equals(INTENT_TAG_NAME)) {
            Intent intent = null;
            
            try {
                intent = Intent.parseIntent(getContext().getResources(), parser, attrs);
            } catch (IOException e) {
                XmlPullParserException ex = new XmlPullParserException(
                        "Error parsing preference");
                ex.initCause(e);
                throw ex;
            }
            
            if (intent != null) {
                parentPreference.setIntent(intent);
            }
            
            return true;
        } else if (tag.equals(EXTRA_TAG_NAME)) {
            getContext().getResources().parseBundleExtra(EXTRA_TAG_NAME, attrs,
                    parentPreference.getExtras());
            try {
                AuroraXmlUtils.skipCurrentTag(parser);
            } catch (IOException e) {
                XmlPullParserException ex = new XmlPullParserException(
                        "Error parsing preference");
                ex.initCause(e);
                throw ex;
            }
            return true;
        }
        
        return false;
    }

    @Override
    protected AuroraPreferenceGroup onMergeRoots(AuroraPreferenceGroup givenRoot, boolean attachToGivenRoot,
            AuroraPreferenceGroup xmlRoot) {
        // If we were given a Preferences, use it as the root (ignoring the root
        // Preferences from the XML file).
        if (givenRoot == null) {
            xmlRoot.onAttachedToHierarchy(mPreferenceManager);
            return xmlRoot;
        } else {
            return givenRoot;
        }
    }
    
}
