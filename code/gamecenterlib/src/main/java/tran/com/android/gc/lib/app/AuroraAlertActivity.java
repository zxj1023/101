package tran.com.android.gc.lib.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;

import tran.com.android.gc.lib.internal.app.AuroraAlertController;

import tran.com.android.gc.lib.R;

public abstract class AuroraAlertActivity extends Activity implements DialogInterface{
    /**
     * The model for the alert.
     * 
     * @see #mAlertParams
     */
    protected AuroraAlertController mAlert;
    
    /**
     * The parameters for the alert.
     */
    protected AuroraAlertController.AlertParams mAlertParams;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AuroraAlertDialogTheme);
        mAlert = new AuroraAlertController(this, this, getWindow());
        
        mAlertParams = new AuroraAlertController.AlertParams(this);        
    }

    public void cancel() {
        finish();
    }

    public void dismiss() {
        // This is called after the click, since we finish when handling the
        // click, don't do that again here.
        if (!isFinishing()) {
            finish();
        }
    }

    /**
     * Sets up the alert, including applying the parameters to the alert model,
     * and installing the alert's content.
     * 
     * @see #mAlert
     * @see #mAlertParams
     */
    protected void setupAlert() {
        mAlertParams.apply(mAlert);
        mAlert.installContent();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAlert.onKeyDown(keyCode, event)) return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mAlert.onKeyUp(keyCode, event)) return true;
        return super.onKeyUp(keyCode, event);
    }

    
}
