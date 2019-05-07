package tran.com.android.tapla.gamecenter.market.db;

import android.content.Context;


public class AutoUpdateDao extends AppDownloadDao {

	public AutoUpdateDao(Context context) {
		super(context, AppDownloadHelper.AUTOUPDATE_TABLE);
	}

}
