package com.tran.com.android.gc.update.db;

import android.content.Context;


public class AutoUpdateDao extends AppDownloadDao {

	public AutoUpdateDao(Context context) {
		super(context, AppDownloadHelper.AUTOUPDATE_TABLE);
	}

}
