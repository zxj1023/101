package com.tran.com.android.gc.update.datauiapi.interf;

import com.tran.com.android.gc.update.datauiapi.implement.Command;
import com.tran.com.android.gc.update.datauiapi.implement.DataResponse;

public interface INotifiableManager {
	
	public void onFinish(DataResponse<?> response);
	public void onWrongConnectionState(int state, Command<?> cmd);
	public void onError(Exception e, Command<?> cmd);
	public void onMessage(String message);
	public void onMessage(int code, String message);
	//public void retryAll();
	public void retry();
	
}
