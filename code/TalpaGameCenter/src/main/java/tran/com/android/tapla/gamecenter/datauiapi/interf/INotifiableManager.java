package tran.com.android.tapla.gamecenter.datauiapi.interf;

import tran.com.android.tapla.gamecenter.datauiapi.implement.Command;
import tran.com.android.tapla.gamecenter.datauiapi.implement.DataResponse;

public interface INotifiableManager {
	
	public void onFinish(DataResponse<?> response);
	public void onWrongConnectionState(int state, Command<?> cmd);
	public void onError(Exception e, Command<?> cmd);
	public void onMessage(String message);
	public void onMessage(int code, String message);
	//public void retryAll();
	public void retry();
	
}
