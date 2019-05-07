package tran.com.android.gc.lib.widget;

/**
 * @author huming
 * @description the interface is implemented in the widget which contain complex
 *              view ,which can be called by launcher
 */
public abstract interface IAuroraWidget {

	// allows you to add the maximum number of widget
	public abstract int getPermittedCount();

	// set the upper-left corner of coordinates of widget
	public abstract int[] setWigetPoi(int[] pos);

	// when widget is removed from launcher,call the method
	public abstract void onDestroy();

	// whether the widget can add onto launcher
	boolean canAddToGioneeLauncher();

	// whether the widget upto maxinum number
	void showUpToLimit();

	// add widget to launcher,call the method
	void onAddToGioneeLauncher();

	// set the screen which the widget display
	public abstract void setScreen(int curScreen);

	// when the widget start drag,call the method
	public abstract void onStartDrag();

	// when the widget stop drag,call the method
	public abstract void onStopDrag();

	// when the launcher screen start scroll,call the method
	public abstract boolean onScrollStart();

	// when the launcher screen stop scroll,call the method
	public abstract void onScrollEnd(int curScreen);

	// when the launcher screen is scrolling,call the method
	public abstract void onScroll(float factor);

	// when the floader is opening,call the method
	public abstract void startCovered(int screenConvered);

	// when the floader is closing,call the method
	public abstract void stopCovered(int screenConvered);

	// when the launcher activity onPause,call the method
	public abstract void onPauseWhenShown(int curScreen);

	// when the launcher activity onResume,call the method
	public abstract void onResumeWhenShown(int curScreen);

	// bind date on bundle object,update the view
	public abstract void updateView(android.os.Bundle arg0);
}
