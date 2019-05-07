package tran.com.android.tapla.gamecenter.market.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.RemotableViewMethod;
import android.view.TouchDelegate;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import tran.com.android.gc.lib.app.AuroraAlertDialog;
import tran.com.android.talpa.app_core.log.LogPool;
import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.market.util.SystemUtils;

public class ProgressBtn extends LinearLayout {

	private final String TAG = "ProgressBtn";
	public static final int STATUS_NORMAL = 1;  //正常下载状态
	public static final int STATUS_WAIT_DOWNLOAD = 2; //等待下载状态
	public static final int STATUS_PROGRESSING_DOWNLOAD = 3; //正在下载状态
	public static final int STATUS_WAIT_INSTALL = 4; //等待安装状态
	public static final int STATUS_PROGRESSING_INSTALLING = 5; //安装状态
	public static final int STATUS_FOUCE = 6;
	public static final int STATUS_FOUCE_NORMAL = 7;
	public static final int STATUS_DOWNLOAD_FAIL = 8;//下载失败，重试 Retry
	public static final int STAUTS_DOWNLOAD_CONTINUE = 9;
	public static final int STAUTS_DOWNLOAD_GO_ON = 10;//继续状态，GO_ON
	public static final int STAUTS_DOWNLOAD_PAUSE = 11;//暂停状态 Pause
	public static final int STAUTS_DOWNLOAD_UPDATE = 12;//更新 update
	private int status = STATUS_NORMAL;

	private int style = 0;


	private FrameLayout progressBtn;
	private ProgressBar progressBar;
	private TextView progressText;
	private View shadow;

	private int waitingColor = Color.parseColor("#CACACA");
	private int redColor = Color.parseColor("#FF535D");





	private int progress = 0;
	private boolean isRuningStartAnim = false;
	private boolean isRuningEndAnim = false;
	private OnClickListener onButtonClickListener = null;
	private OnClickListener onNormalClickListener = null;


	public ProgressBtn(Context context) {
		super(context);
		initView();
	}

	public ProgressBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	@Override
	@RemotableViewMethod
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);


	}
	private void initView() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.small_view_progressbtn, this);
		progressBtn = (FrameLayout) view.findViewById(R.id.progress_layout);
		progressBar = (ProgressBar) view.findViewById(R.id.download_progress);
		progressText = (TextView) view.findViewById(R.id.download_text);
		//shadow = view.findViewById(R.id.download_progess_shadow);

		progressBtn.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						if (!SystemUtils.isDownload(getContext())) {
							final View view = v;

							AuroraAlertDialog mWifiConDialog = new AuroraAlertDialog.Builder(
									getContext(), AuroraAlertDialog.THEME_AMIGO_FULLSCREEN)
									.setTitle(
											getContext().getResources().getString(
													R.string.dialog_prompt))
									.setMessage(
											getContext().getResources().getString(
													R.string.no_wifi_download_message))
									.setNegativeButton(android.R.string.cancel, null)
									.setPositiveButton(android.R.string.ok,
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {

													SharedPreferences sp = PreferenceManager
															.getDefaultSharedPreferences(getContext());
													SharedPreferences.Editor ed = sp.edit();
													ed.putBoolean("wifi_download_key", false);
													ed.commit();
													if (onButtonClickListener != null) {
														onButtonClickListener
																.onClick(view);
													}

													startBeginAnim();

													if (onNormalClickListener != null) {
														onNormalClickListener
																.onClick(view);
													}
												}

											}).create();
							mWifiConDialog.show();

						} else if (!SystemUtils.hasNetwork()) {
							Toast.makeText(getContext(), getContext()
									.getString(R.string.no_network_download_toast), Toast.LENGTH_SHORT).show();
						} else {
							if (onButtonClickListener != null) {
								onButtonClickListener.onClick(v);
							}

							startBeginAnim();

							if (onNormalClickListener != null) {
								onNormalClickListener.onClick(v);
							}
						}
					}
				});

		expandViewTouchDelegate(progressBtn);


	}

	/**
	 * @Title: startBeginAnim
	 * @Description: TODO 进度开始前动画
	 * @param
	 * @return void
	 * @throws
	 */
	public void startBeginAnim() {
		progressBar.setProgress(0);
	}

	/**
	 * @Title: startEndAnim
	 * @Description: TODO 结束进度后动画，按钮变为focus
	 * @param
	 * @return void
	 * @throws
	 */
	public void startEndAnim() {
		startEndAnim(true);
	}

	public void startEndAnim(final boolean fouce) {
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		LogPool.e(TAG,"Download Status:"+status);
		if (this.status == status) {
			return;
		}

		isRuningStartAnim = false;
		isRuningEndAnim = false;


		this.status = status;

		switch (status) {
			case STATUS_NORMAL:
				progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_small_download_btn_df));
				progressText.setTextColor(redColor);
				progressText.setText(getContext().getString(R.string.download_button_download));
				break;
			case STATUS_WAIT_DOWNLOAD:
				progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_small_download_btn_wait));
				if(progressText.getVisibility() == View.GONE){
					progressText.setVisibility(View.VISIBLE);

				}
				progressText.setTextColor(waitingColor);
				progressText.setText(getContext().getString(R.string.download_button_wait));
				break;
			case STATUS_PROGRESSING_DOWNLOAD:

				progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_small_download_btn_downloading));
				if(progressText.getVisibility() == View.GONE){
					progressText.setVisibility(View.VISIBLE);
				}
				progressText.setTextColor(Color.WHITE);
				progressText.setText(this.progress+getContext().getString(R.string.download_button_download_percent));

				break;
			case STATUS_WAIT_INSTALL:
				progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_small_download_btn_wait));
				if(progressText.getVisibility() == View.GONE){
					progressText.setVisibility(View.VISIBLE);
				}
				progressText.setTextColor(waitingColor);
				progressText.setText(getContext().getString(R.string.download_button_wait));

				break;
			case STATUS_PROGRESSING_INSTALLING:
				progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_small_download_btn_wait));
				if(progressText.getVisibility() == View.GONE){
					progressText.setVisibility(View.VISIBLE);
				}
				progressText.setTextColor(waitingColor);
				progressText.setText(getContext().getString(R.string.download_button_installing));
				break;
			case STATUS_FOUCE:
				progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_small_download_btn_df));
				progressText.setTextColor(redColor);
				progressText.setText(getContext().getString(R.string.download_button_open));
				break;
			case STATUS_FOUCE_NORMAL:
				progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_small_download_btn_df));
				if(progressText.getVisibility() == View.GONE){
					progressText.setVisibility(View.VISIBLE);
				}
				progressText.setTextColor(redColor);
				progressText.setText(getContext().getString(R.string.download_button_install));
				break;
			case STATUS_DOWNLOAD_FAIL:
				progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_small_download_btn_df));
				progressText.setTextColor(redColor);
				progressText.setText(getContext().getString(R.string.download_button_try));
				break;
			case STAUTS_DOWNLOAD_CONTINUE:
				progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_small_download_btn_pase));
				progressBar.setProgress(progress);
				if (progressText.getVisibility() == View.GONE) {
					progressText.setVisibility(View.VISIBLE);
				}
				progressText.setTextColor(Color.WHITE);
				progressText.setText(getContext().getString(R.string.download_button_continue));
				break;
			case STAUTS_DOWNLOAD_GO_ON:
				progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_small_download_btn_pase));
				progressText.setTextColor(redColor);
				progressText.setText(getContext().getString(R.string.download_button_continue));
				break;
			case STAUTS_DOWNLOAD_PAUSE:
				progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_small_download_btn_df));
				progressText.setTextColor(redColor);
				progressText.setText(getContext().getString(R.string.download_button_pause));
				break;
			case STAUTS_DOWNLOAD_UPDATE:
				progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_small_download_btn_df));
				progressText.setTextColor(redColor);
				progressText.setText(getContext().getString(R.string.download_button_update));
				break;
		}
	}

	public boolean isRuningStartAnim() {
		return isRuningStartAnim;
	}

	public boolean isRuningEndAnim() {
		return isRuningEndAnim;
	}

	public void setOnButtonClickListener(OnClickListener onButtonClickListener) {
		this.onButtonClickListener = onButtonClickListener;
	}

	public void setOnNormalClickListener(OnClickListener onNormalClickListener) {
		this.onNormalClickListener = onNormalClickListener;
	}

	public void setBtnText(String text) {
		progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_small_download_btn_df));
		if(progressText.getVisibility() == View.GONE){
			progressText.setVisibility(View.VISIBLE);
		}
		progressText.setTextColor(redColor);
		progressText.setText(text);

	}

	public void setFoucesBtnText(String text) {
		progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_small_download_btn_df));
		if(progressText.getVisibility() == View.GONE){
			progressText.setVisibility(View.VISIBLE);
		}
		progressText.setTextColor(redColor);
		progressText.setText(getContext().getString(R.string.download_button_install));

	}

	public void setOnFoucsClickListener(OnClickListener onFoucsClickListener) {
		progressBtn.setOnClickListener(onFoucsClickListener);
	}

	public void setOnProgressClickListener(
			OnClickListener onProgressClickListener) {
		progressBtn.setOnClickListener(onProgressClickListener);
	}

	public void setProgressBackground(int resid) {
		progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_small_download_btn_downloading));
	}

	public int getProgress() {
		return progress;
}
	public void updateProgress(int pros) {
		this.progress = pros;
	}
	public void setProgress(int pro) {
		this.progress = pro;
		if (status != STATUS_PROGRESSING_DOWNLOAD) {
			setStatus(STATUS_PROGRESSING_DOWNLOAD);
		}
		post(new Runnable() {
			@Override
			public void run() {
				progressBar.setProgress(progress);
				if(progressText.getVisibility() == View.GONE){
					progressText.setVisibility(View.VISIBLE);
				}
				progressText.setTextColor(Color.WHITE);
				progressText.setText(progress+getContext().getString(R.string.download_button_download_percent));
			}
		});

	}

	public void setProgressAnim(int pr) {
		this.progress = pr;
		if (status != STATUS_PROGRESSING_DOWNLOAD) {
			setStatus(STATUS_PROGRESSING_DOWNLOAD);
		}
		post(new Runnable() {
			@Override
			public void run() {
				progressBar.setProgress(progress);
				if(progressText.getVisibility() == View.GONE){
					progressText.setVisibility(View.VISIBLE);
				}
				progressText.setTextColor(Color.WHITE);
				progressText.setText(progress+getContext().getString(R.string.download_button_download_percent));
			}
		});


	}

	public void setOnBeginAnimListener(OnAnimListener onBeginAnimListener) {
//		this.onBeginAnimListener = onBeginAnimListener;
    }

    public void setFouceStyle() {
//		fouceBtn.setBackgroundResource(R.drawable.button_focus_selector);
//		fouceBtn.setTextColor(getResources().getColor(R.color.white));
//		fouceBtn_backup.setTextColor(getResources().getColor(R.color.white));
    }

    public void setFouceNormalStyle() {
//		fouceBtn.setBackgroundResource(R.drawable.button_default_selector);
//		fouceBtn.setTextColor(getResources().getColorStateList(R.color.normal_btn_text_color));
//		fouceBtn_backup.setTextColor(getResources().getColorStateList(R.color.normal_btn_text_color));
    }

    /**
     * @param @return
     * @return RotateAnimation
     * @throws
     * @Title: createRotateAnimation
     * @Description: 创建旋转动画
     */
    private RotateAnimation createRotateAnimation(boolean reverse) {
        RotateAnimation animation = null;
        if (!reverse) {
            animation = new RotateAnimation(0, 3600, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            animation = new RotateAnimation(0, -3600, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        animation.setInterpolator(new LinearInterpolator());
        animation.setFillAfter(true);
        animation.setDuration(10000);
        animation.setStartOffset(0);
        animation.setRepeatCount(1000);
        return animation;
    }

    //====================加大按钮点击区域start====================//

    private View lastDelegate;

    private void expandViewTouchDelegate(final View view) {
        post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                view.setEnabled(true);
                view.getHitRect(bounds);

                bounds.top -= 500;
                bounds.bottom += 500;
                bounds.left -= 500;
                bounds.right += 500;

                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(touchDelegate);
                }

                lastDelegate = view;
            }
        });
    }

    private void restoreViewTouchDelegate(final View view) {
        post(new Runnable() {
            @Override
            public void run() {
                if (view != null) {
                    Rect bounds = new Rect();
                    bounds.setEmpty();
                    TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                    if (View.class.isInstance(view.getParent())) {
                        ((View) view.getParent()).setTouchDelegate(touchDelegate);
                    }
                }

            }
        });
    }

    //====================加大按钮点击区域end====================//

    public interface OnAnimListener {
        public void onEnd(ProgressBtn view);
    }

}
