package com.tran.com.android.gc.update.activity.module;

import android.app.Activity;
import android.os.Bundle;

import com.tran.com.android.gc.update.R;
import com.tran.com.android.gc.update.util.Log;
import com.tran.com.android.gc.update.util.SystemUtils;

/**
 * Created by joy on 10/14/14.
 */
public class UpdateAlertActivity extends Activity {
    private int apkId = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity);

        apkId = getIntent().getIntExtra("apkId", 0);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Test", "UpdateAlertActivity onResume");
        SystemUtils.openForceUpDialog(this, apkId);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Test", "UpdateAlertActivity onStop");
        SystemUtils.killProcess(this, apkId);
        finish();
    }
}