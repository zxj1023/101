package tran.com.android.tapla.gamecenter.market.util;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.io.File;

import tran.com.android.gc.lib.app.AuroraAlertDialog;
import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.download.AppDownloadService;
import tran.com.android.tapla.gamecenter.install.AppInstallService;
import tran.com.android.tapla.gamecenter.market.download.ApkUtil;
import tran.com.android.tapla.gamecenter.market.download.FileDownloader;
import tran.com.android.tapla.gamecenter.market.install.InstallAppManager;
import tran.com.android.tapla.gamecenter.market.model.DownloadData;
import tran.com.android.tapla.gamecenter.market.model.InstalledAppInfo;
import tran.com.android.tapla.gamecenter.market.widget.MiddleProgressBtn;

public class MiddleProgressBtnUtil {
	
	public static final String TAG = "ProgressBtnUtil";
	
	public void updateProgressBtn(MiddleProgressBtn btn, DownloadData data) {
		updateProgressBtn(btn, data, null);
	}
	
	public void updateProgressBtn(MiddleProgressBtn btn, DownloadData data, OnClickListener onClickListener) {
		// 检测是否安装
		InstalledAppInfo installedAppInfo = InstallAppManager
				.getInstalledAppInfo(btn.getContext(),
						data.getPackageName());

		// 未安装的情况
		if (installedAppInfo == null) {
			FileDownloader downloader = AppDownloadService.getDownloaders()
					.get(data.getApkId());
			// 如果下载器任务存在, 显示各状态信息
			if (downloader != null) {
				int status = downloader.getStatus();
				if (status == FileDownloader.STATUS_PAUSE
						||status == FileDownloader.STATUS_PAUSE_NEED_CONTINUE) {
					showOperationContinue(btn, downloader);
				} else if (status == FileDownloader.STATUS_DOWNLOADING
						|| status == FileDownloader.STATUS_CONNECTING
						|| status == FileDownloader.STATUS_NO_NETWORK
						|| status == FileDownloader.STATUS_WAIT) {
					showOperationDownloading(btn, downloader);
				} else if (status == FileDownloader.STATUS_FAIL) {
					showOperationRetry(btn, downloader);
				} else if (status == FileDownloader.STATUS_INSTALL_WAIT) {
					showWaitInstall(btn, data);
				} else {
					if (status < FileDownloader.STATUS_INSTALL_WAIT) {
						showOperationDownload(btn, data);
					}
				}
			} else { // 任务完成或者没有记录
				DownloadData tempData = AppDownloadService.getAppDownloadDao()
						.getDownloadData(data.getApkId());
				if (tempData == null) {
					showOperationDownload(btn, data);
				} else {
					if (tempData.getVersionCode() == data.getVersionCode()) {
						int status = tempData.getStatus();
						if (status == FileDownloader.STATUS_INSTALL_WAIT) {		// 等待安装
							showWaitInstall(btn, data);
						} else if (status == FileDownloader.STATUS_INSTALLING) {	// 安装中
							showInstalling(btn, data);
						} else if (status == FileDownloader.STATUS_INSTALLFAILED
								|| status == FileDownloader.STATUS_INSTALLED) {	// 安装成功或者安装失败
							
							int id = btn.getTag() == null ? 0 : (Integer) btn.getTag();
							if (id == tempData.getApkId() && status == FileDownloader.STATUS_INSTALLED
									&& btn.getStatus() == MiddleProgressBtn.STATUS_PROGRESSING_INSTALLING) {
								return;
							}
							
							String fileDir = tempData.getFileDir();
							fileDir = fileDir == null ? "" : fileDir;
							String fileName = tempData.getFileName();
							fileName = fileName == null ? "" : fileName;
							final File file = new File(fileDir, fileName);
							if (file.exists()) {	// 查看数据库中该任务状态是否为完成, 并且文件是存在的
								InstalledAppInfo info = InstallAppManager
									.getInstalledAppInfo(btn.getContext(),
											data.getPackageName());
								if (info == null) {
									showOperationInstall(btn, data, file);
								} else {
									showOperationOpen(btn, data);
								}
							} else {
								showOperationDownload(btn, data);
							}
						} else {	// 条件不符合则显示下载
							showOperationDownload(btn, data);
						}
					} else {
						showOperationDownload(btn, data);
					}
				}
			}
		} else {
			// 这里判断是否为最新版本
			if (data.getVersionCode() > installedAppInfo.getVersionCode()) { // 不是最新版本
				FileDownloader downloader = AppDownloadService.getDownloaders()
						.get(data.getApkId());
				// 如果下载器任务存在, 显示各状态信息
				if (downloader != null) {
					int status = downloader.getStatus();
					if (status == FileDownloader.STATUS_PAUSE
							||status == FileDownloader.STATUS_PAUSE_NEED_CONTINUE) {
						showOperationContinue(btn, downloader);
					} else if (status == FileDownloader.STATUS_DOWNLOADING
							|| status == FileDownloader.STATUS_CONNECTING
							|| status == FileDownloader.STATUS_NO_NETWORK
							|| status == FileDownloader.STATUS_WAIT) {
						showOperationDownloading(btn, downloader);
					} else if (status == FileDownloader.STATUS_FAIL) {
						showOperationRetry(btn, downloader);
					} else {
						if (status < FileDownloader.STATUS_INSTALL_WAIT) {
							showOperationUpdate(btn, data, onClickListener);
						}
					}
				} else { // 任务完成或者没有记录
					DownloadData tempData = AppDownloadService.getAppDownloadDao()
							.getDownloadData(data.getApkId());
					if (tempData == null) {
						showOperationUpdate(btn, data, onClickListener);
					} else {
						if (tempData.getVersionCode() == data.getVersionCode()) {
							int status = tempData.getStatus();
							if (status == FileDownloader.STATUS_INSTALL_WAIT) {		// 等待安装
								showWaitInstall(btn, data);
							} else if (status == FileDownloader.STATUS_INSTALLING) {	// 安装中
								showInstalling(btn, data);
							}/* else if (status == FileDownloader.STATUS_INSTALLED) {	// 安装成功
								showOperationOpen(btn, data);
							} else if (status == FileDownloader.STATUS_INSTALLFAILED) {	// 安装失败
								String fileDir = tempData.getFileDir();
								fileDir = fileDir == null ? "" : fileDir;
								String fileName = tempData.getFileName();
								fileName = fileName == null ? "" : fileName;
								final File file = new File(fileDir, fileName);
								if (file.exists()) {
									showOperationInstall(btn, data, file);
								} else {
									showOperationUpdate(btn, data, onClickListener);
								}
							}*/ else if (status == FileDownloader.STATUS_INSTALLFAILED
									|| status == FileDownloader.STATUS_INSTALLED) {	// 安装成功或者安装失败
								
								String fileDir = tempData.getFileDir();
								fileDir = fileDir == null ? "" : fileDir;
								String fileName = tempData.getFileName();
								fileName = fileName == null ? "" : fileName;
								final File file = new File(fileDir, fileName);
								if (file.exists()) {	// 查看数据库中该任务状态是否为完成, 并且文件是存在的
									InstalledAppInfo info = InstallAppManager
										.getInstalledAppInfo(btn.getContext(),
												data.getPackageName());
									if (info != null && data.getVersionCode() > info.getVersionCode()) {
										showOperationInstall(btn, data, file);
									} else {
										showOperationOpen(btn, data);
									}
								} else {
									showOperationUpdate(btn, data, onClickListener);
								}
							} else {	// 条件不符合则显示更新
								showOperationUpdate(btn, data, onClickListener);
							}
						} else {
							showOperationUpdate(btn, data, onClickListener);
						}
					}
				}
			} else { // 如果是最新版本
				showOperationOpen(btn, data);
			}
		}
		
		btn.setTag(data.getApkId());
	}
	
	public void updateFinishProgressBtn(MiddleProgressBtn btn, DownloadData data) {
		// 检测是否安装
		InstalledAppInfo installedAppInfo = InstallAppManager
				.getInstalledAppInfo(btn.getContext(),
						data.getPackageName());

		// 未安装的情况
		if (installedAppInfo == null) {
			DownloadData tempData = AppDownloadService.getAppDownloadDao()
					.getDownloadData(data.getApkId());
			if (tempData == null) {
				showOperationDownload(btn, data);
			} else {
				if (tempData.getVersionCode() == data.getVersionCode()) {
					int status = tempData.getStatus();
					if (status == FileDownloader.STATUS_INSTALL_WAIT) {		// 等待安装
						showWaitInstall(btn, data);
					} else if (status == FileDownloader.STATUS_INSTALLING) {	// 安装中
						showInstalling(btn, data);
					} else if (status == FileDownloader.STATUS_INSTALLFAILED
							|| status == FileDownloader.STATUS_INSTALLED) {	// 安装成功或者安装失败
						
						int id = btn.getTag() == null ? 0 : (Integer) btn.getTag();
						if (id == tempData.getApkId() && status == FileDownloader.STATUS_INSTALLED
								&& btn.getStatus() == MiddleProgressBtn.STATUS_PROGRESSING_INSTALLING) {
							return;
						}
						
						String fileDir = tempData.getFileDir();
						fileDir = fileDir == null ? "" : fileDir;
						String fileName = tempData.getFileName();
						fileName = fileName == null ? "" : fileName;
						final File file = new File(fileDir, fileName);
						showOperationInstall(btn, data, file);
					} else {	// 条件不符合则显示安装
						String fileDir = tempData.getFileDir();
						fileDir = fileDir == null ? "" : fileDir;
						String fileName = tempData.getFileName();
						fileName = fileName == null ? "" : fileName;
						final File file = new File(fileDir, fileName);
						showOperationInstall(btn, data, file);
					}
				} else {
					String fileDir = tempData.getFileDir();
					fileDir = fileDir == null ? "" : fileDir;
					String fileName = tempData.getFileName();
					fileName = fileName == null ? "" : fileName;
					final File file = new File(fileDir, fileName);
					showOperationInstall(btn, data, file);
				}
			}
		} else {
			// 这里判断是否为最新版本
			if (data.getVersionCode() > installedAppInfo.getVersionCode()) { // 不是最新版本
				DownloadData tempData = AppDownloadService.getAppDownloadDao()
						.getDownloadData(data.getApkId());
				if (tempData == null) {
					showOperationOpen(btn, data);
				} else {
					if (tempData.getVersionCode() == data.getVersionCode()) {
						int status = tempData.getStatus();
						if (status == FileDownloader.STATUS_INSTALL_WAIT) {		// 等待安装
							showWaitInstall(btn, data);
						} else if (status == FileDownloader.STATUS_INSTALLING) {	// 安装中
							showInstalling(btn, data);
						} else if (status == FileDownloader.STATUS_INSTALLFAILED
								|| status == FileDownloader.STATUS_INSTALLED) {	// 安装成功或者安装失败
							
							int id = btn.getTag() == null ? 0 : (Integer) btn.getTag();
							if (id == tempData.getApkId() && status == FileDownloader.STATUS_INSTALLED
									&& btn.getStatus() == MiddleProgressBtn.STATUS_PROGRESSING_INSTALLING) {
								return;
							}
							
							String fileDir = tempData.getFileDir();
							fileDir = fileDir == null ? "" : fileDir;
							String fileName = tempData.getFileName();
							fileName = fileName == null ? "" : fileName;
							final File file = new File(fileDir, fileName);
							showOperationInstall(btn, data, file);
						} else {	// 条件不符合则显示更新
							showOperationOpen(btn, data);
						}
					} else {
						showOperationOpen(btn, data);
					}
				}
			} else { // 如果是最新版本
				showOperationOpen(btn, data);
			}
		}
		
		btn.setTag(data.getApkId());
	}
	
	
	/**
	 * 显示下载操作
	 *
	 */
	private void showOperationDownload(final MiddleProgressBtn progressBtn,
			final DownloadData downloadData) {
		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppDownloadService.startDownload(progressBtn.getContext(),
						downloadData);
			}
		};
		
		// ProgressBtn
		progressBtn.setBtnText(progressBtn.getResources().getString(R.string.download_button_download));
		progressBtn.setStatus(MiddleProgressBtn.STATUS_NORMAL);
		progressBtn.setOnNormalClickListener(clickListener);
		final int apkId = downloadData.getApkId();
		progressBtn.setOnBeginAnimListener(new MiddleProgressBtn.OnAnimListener() {
			@Override
			public void onEnd(MiddleProgressBtn view) {
				FileDownloader downloader = AppDownloadService.getDownloaders()
						.get(apkId);
				if (downloader != null) {
					int status = downloader.getStatus();
					if (status == FileDownloader.STATUS_CONNECTING
							|| status == FileDownloader.STATUS_DOWNLOADING) {
						view.setStatus(MiddleProgressBtn.STATUS_PROGRESSING_DOWNLOAD);
					}
				}
			}
		});
	}

	/**
	 * 显示更新操作
	 *
	 */
	private void showOperationUpdate(final MiddleProgressBtn progressBtn,
			final DownloadData downloadData, OnClickListener onClickListener) {
		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppDownloadService.startDownload(progressBtn.getContext(),
						downloadData);
			}
		};
		
		// ProgressBtn
//		progressBtn.setBtnText(progressBtn.getResources().getString(R.string.download_button_update));
		progressBtn.setStatus(MiddleProgressBtn.STAUTS_DOWNLOAD_UPDATE);
		progressBtn.setOnNormalClickListener(clickListener);
		/*if (onClickListener != null) {
			progressBtn.setOnButtonClickListener(onClickListener);
		} else {
			progressBtn.setOnButtonClickListener(null);
		}*/
		final int apkId = downloadData.getApkId();
		progressBtn.setOnBeginAnimListener(new MiddleProgressBtn.OnAnimListener() {
			@Override
			public void onEnd(MiddleProgressBtn view) {
				FileDownloader downloader = AppDownloadService.getDownloaders()
						.get(apkId);
				if (downloader != null) {
					int status = downloader.getStatus();
					if (status == FileDownloader.STATUS_CONNECTING
							|| status == FileDownloader.STATUS_DOWNLOADING) {
						view.setStatus(MiddleProgressBtn.STATUS_PROGRESSING_DOWNLOAD);
					}
				}
			}
		});
	}

	/**
	 * 显示正在下载
	 * 
	 * @param downloader
	 */
	private void showOperationDownloading(final MiddleProgressBtn progressBtn,
			final FileDownloader downloader) {
		int status = downloader.getStatus();
		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppDownloadService.pauseOrContinueDownload(
						progressBtn.getContext(), downloader.getDownloadData());
			}
		};
		
		// ProgressBtn
		if (status == FileDownloader.STATUS_WAIT) {
			if (!progressBtn.isRuningStartAnim()) {
				progressBtn.setStatus(MiddleProgressBtn.STATUS_WAIT_DOWNLOAD);
			}
		} else {
			if (!progressBtn.isRuningStartAnim()) {
				progressBtn.setStatus(MiddleProgressBtn.STATUS_PROGRESSING_DOWNLOAD);
			}
			long downloadSize = downloader.getDownloadSize();
			long fileSize = downloader.getFileSize();
			int progress = 0;
			if (fileSize != 0) {
				progress = (int) ((downloadSize * 1.0) / fileSize * 100);
			}
			int id =  progressBtn.getTag() == null ? 0 : (Integer) progressBtn.getTag();
			if (!progressBtn.isRuningStartAnim()) {
				if (id == downloader.getDownloadData().getApkId()) {
					progressBtn.setProgressAnim(progress);
				} else {
					progressBtn.setProgress(progress);
				}
			}
			progressBtn.setOnProgressClickListener(clickListener);
			progressBtn.setProgressBackground(R.drawable.button_stop_selector);
		}
	}

	/**
	 * 显示继续操作
	 *
	 */
	private void showOperationContinue(final MiddleProgressBtn progressBtn,
			final FileDownloader downloader) {
		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!SystemUtils.isDownload(progressBtn.getContext())) {
					AuroraAlertDialog mWifiConDialog = new AuroraAlertDialog.Builder(
							progressBtn.getContext(), AuroraAlertDialog.THEME_AMIGO_FULLSCREEN)
							.setTitle(
									progressBtn.getContext().getResources().getString(
											R.string.dialog_prompt))
							.setMessage(
									progressBtn.getContext().getResources().getString(
											R.string.no_wifi_download_message))
							.setNegativeButton(android.R.string.cancel, null)
							.setPositiveButton(android.R.string.ok,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											
											SharedPreferences sp = PreferenceManager
													.getDefaultSharedPreferences(progressBtn.getContext());
											Editor ed = sp.edit();
											ed.putBoolean("wifi_download_key", false);
											ed.commit();
											doOperationContinue(progressBtn,downloader);
										}

									}).create();
					mWifiConDialog.show();

				} else if (!SystemUtils.hasNetwork()) {
					Toast.makeText(progressBtn.getContext(), progressBtn.getContext()
							.getString(R.string.no_network_download_toast), Toast.LENGTH_SHORT).show();
				} else {
					doOperationContinue(progressBtn, downloader);
				}
			}
		};
		
		// ProgressBtn
		long downloadSize = downloader.getDownloadSize();
		long fileSize = downloader.getFileSize();
		int progress = 0;
		if (fileSize != 0) {
			progress = (int) ((downloadSize * 1.0) / fileSize * 100);
		}
		progressBtn.updateProgress(progress);
		progressBtn.setStatus(MiddleProgressBtn.STAUTS_DOWNLOAD_CONTINUE);
		progressBtn.setOnProgressClickListener(clickListener);
	}

	private void doOperationContinue(final MiddleProgressBtn progressBtn,
			final FileDownloader downloader)
	{
		int status = downloader.getStatus();
		if (status == FileDownloader.STATUS_PAUSE_NEED_CONTINUE) {
			AuroraAlertDialog mWifiConDialog = new AuroraAlertDialog.Builder(
					progressBtn.getContext(), AuroraAlertDialog.THEME_AMIGO_FULLSCREEN)
					.setTitle(progressBtn.getContext().getResources().getString(R.string.dialog_prompt))
					.setMessage(progressBtn.getContext().getResources().getString(
									R.string.downloadman_continue_download_by_mobile))
					.setNegativeButton(android.R.string.cancel, null)
					.setPositiveButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {
									
									AppDownloadService.pauseOrContinueDownload(progressBtn.getContext(),
											downloader.getDownloadData());
								}

							}).create();
			mWifiConDialog.show();
		} else {
			AppDownloadService.pauseOrContinueDownload(progressBtn.getContext(),downloader.getDownloadData());
		}
	}
	
	
	/**
	 * 显示重试操作
	 *
	 */
	private void showOperationRetry(final MiddleProgressBtn progressBtn,
			final FileDownloader downloader) {
		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!SystemUtils.hasNetwork()) {
					Toast.makeText(progressBtn.getContext(), progressBtn.getContext()
							.getString(R.string.no_network_download_toast), Toast.LENGTH_SHORT).show();
				} else {
					AppDownloadService.pauseOrContinueDownload(
							progressBtn.getContext(), downloader.getDownloadData());
				}
			}
		};
		
		// ProgressBtn
		long downloadSize = downloader.getDownloadSize();
		long fileSize = downloader.getFileSize();
		int progress = 0;
		if (fileSize != 0) {
			progress = (int) ((downloadSize * 1.0) / fileSize * 100);
		}
		progressBtn.setStatus(MiddleProgressBtn.STATUS_DOWNLOAD_FAIL);
		progressBtn.setProgress(progress);
		progressBtn.setOnProgressClickListener(clickListener);
		progressBtn.setProgressBackground(R.drawable.button_goon_selector);
	}

	/**
	 * 显示安装操作
	 * 
	 * @param downloadData
	 */
	private void showOperationInstall(final MiddleProgressBtn progressBtn,
			final DownloadData downloadData, final File file) {
		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (file == null || !file.exists()) {
//					AuroraAlertDialog dialog = new AuroraAlertDialog.Builder(
//							progressBtn.getContext(), AuroraAlertDialog.THEME_AMIGO_FULLSCREEN)
//							.setTitle(progressBtn.getContext().getResources().getString(R.string.dialog_prompt))
//							.setMessage(progressBtn.getContext().getResources().getString(
//											R.string.downloadman_noakkfile_redownload))
//							.setNegativeButton(android.R.string.cancel, null)
//							.setPositiveButton(android.R.string.ok,
//									new DialogInterface.OnClickListener() {
//										@Override
//										public void onClick(DialogInterface dialog, int which) {
//											AppDownloadService.getAppDownloadDao().delete(downloadData.getApkId());
//											AppDownloadService.startDownload(progressBtn.getContext(), downloadData);
//										}
//
//									}).create();
//					dialog.show();
					//不做对话框提示，直接重新下载。
					AppDownloadService.getAppDownloadDao().delete(downloadData.getApkId());
					AppDownloadService.startDownload(progressBtn.getContext(), downloadData);
					return;
				}
				
				downloadData.setStatus(FileDownloader.STATUS_INSTALL_WAIT);
				AppDownloadService.getAppDownloadDao().updateStatus(downloadData.getApkId(), 
						FileDownloader.STATUS_INSTALL_WAIT);
				DownloadData d = AppDownloadService.getAppDownloadDao().getDownloadData(downloadData.getApkId());
				AppInstallService.startInstall(progressBtn.getContext(),
						d, AppInstallService.TYPE_NORMAL);
				AppDownloadService.updateDownloadProgress();
			}
		};
		
		// ProgressBtn
		progressBtn.setFoucesBtnText(progressBtn.getResources().getString(R.string.download_button_install));
		progressBtn.setFouceNormalStyle();
		int id =  progressBtn.getTag() == null ? 0 : (Integer) progressBtn.getTag();
		if (id == downloadData.getApkId()) {
			if (!progressBtn.isRuningEndAnim()) {
				if (progressBtn.getStatus() == MiddleProgressBtn.STATUS_PROGRESSING_INSTALLING
						|| progressBtn.getStatus() == MiddleProgressBtn.STATUS_WAIT_INSTALL) {
					progressBtn.startEndAnim(false);
				} else {
					progressBtn.setStatus(MiddleProgressBtn.STATUS_FOUCE_NORMAL);
				}
			}
		} else {
			progressBtn.setStatus(MiddleProgressBtn.STATUS_FOUCE_NORMAL);
		}
		progressBtn.setOnFoucsClickListener(clickListener);
	}
	
	/**
	 * 显示等待安装操作
	 * 
	 * @param downloadData
	 */
	private void showWaitInstall(final MiddleProgressBtn progressBtn,
			final DownloadData downloadData) {
		// ProgressBtn
		progressBtn.setStatus(MiddleProgressBtn.STATUS_WAIT_INSTALL);
	}
	
	/**
	 * 显示正在安装操作
	 * 
	 * @param downloadData
	 */
	private void showInstalling(final MiddleProgressBtn progressBtn,
			final DownloadData downloadData) {
		Log.i(TAG, downloadData.getApkName() + " showInstalling");
		
		// ProgressBtn
		progressBtn.setStatus(MiddleProgressBtn.STATUS_PROGRESSING_INSTALLING);
	}

	/**
	 * 显示打开操作
	 * 
	 * @param downloadData
	 */
	private void showOperationOpen(final MiddleProgressBtn progressBtn,
			final DownloadData downloadData) {
		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				ApkUtil.openApp(progressBtn.getContext(),
						downloadData.getPackageName());
			}
		};
		
		// ProgressBtn
		progressBtn.setBtnText(progressBtn.getResources().getString(R.string.download_button_open));
		progressBtn.setFouceStyle();
		int id =  progressBtn.getTag() == null ? 0 : (Integer) progressBtn.getTag();
		if (id == downloadData.getApkId()) {
			if (!progressBtn.isRuningEndAnim()) {
				if (progressBtn.getStatus() == MiddleProgressBtn.STATUS_PROGRESSING_INSTALLING
						|| progressBtn.getStatus() == MiddleProgressBtn.STATUS_WAIT_INSTALL) {
					progressBtn.startEndAnim(true);
				} else {
					progressBtn.setStatus(MiddleProgressBtn.STATUS_FOUCE);
				}
			}
		} else {
			progressBtn.setStatus(MiddleProgressBtn.STATUS_FOUCE);
		}
		progressBtn.setOnFoucsClickListener(clickListener);
	}

}
