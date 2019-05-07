package tran.com.android.gc.lib.preference;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import tran.com.android.gc.lib.R;


public class AuroraSeekBarDialogPreference extends AuroraDialogPreference {
    private static final String TAG = "SeekBarDialogPreference";

    private Drawable mMyIcon;

    public AuroraSeekBarDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.aurora_seekbar_dialog);
        createActionButtons();

        // Steal the XML dialogIcon attribute's value
        mMyIcon = getDialogIcon();
        setDialogIcon(null);
    }

    // Allow subclasses to override the action buttons
    public void createActionButtons() {
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        final ImageView iconView = (ImageView) view.findViewById(android.R.id.icon);
        if (mMyIcon != null) {
            iconView.setImageDrawable(mMyIcon);
        } else {
            iconView.setVisibility(View.GONE);
        }
    }

    protected static SeekBar getSeekBar(View dialogView) {
        return (SeekBar) dialogView.findViewById(R.id.aurora_seekbar);
    }
}
