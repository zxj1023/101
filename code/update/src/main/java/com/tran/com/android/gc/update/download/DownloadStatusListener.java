package com.tran.com.android.gc.update.download;

/**
 * 下载状态监听 
 */
public interface DownloadStatusListener {
	
	public void onDownload(int appId, int status, long downloadSize, long fileSize);
	
}
