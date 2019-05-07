package tran.com.android.tapla.gamecenter.market.util;

import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import tran.com.android.gc.lib.utils.LogUtils;
import tran.com.android.tapla.gamecenter.R;

public class LoadingPageUtil implements OnClickListener {

    public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    private static final int STATUS_NO_NETWORK = 1;
    private static final int STATUS_NETWORK_ERROR = 2;

    private Context context;

    private LinearLayout ll_load_page;
    //private ProgressBar iv_loading;
    private LinearLayout ll_error;
    private ImageView iv_icon;
    private TextView tv_text;
    private Button btn;

    private int status;

    private OnShowListener onShowListener;
    private OnHideListener onHideListener;
    private OnRetryListener onRetryListener;

    private NetWorkReceiver netWorkReceiver;

    private LottieAnimationView iv_loading, iv_loadingend;

    public void init(Context context, View view) {
        this.context = context;
        ll_load_page = (LinearLayout) view.findViewById(R.id.ll_load_page);
        iv_loading = (LottieAnimationView) view.findViewById(R.id.animation_view);
        iv_loadingend = (LottieAnimationView) view.findViewById(R.id.animation_view_end);
        ll_error = (LinearLayout) view.findViewById(R.id.ll_error);
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        tv_text = (TextView) view.findViewById(R.id.tv_text);
        btn = (Button) view.findViewById(R.id.btn);
        btn.setOnClickListener(this);
    }

    public boolean isShowing() {
        if (ll_load_page.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }

    public void showLoadPage() {
        ll_load_page.setVisibility(View.VISIBLE);
        if (onShowListener != null) {
            onShowListener.onShow();
        }
    }

    public void hideLoadPage() {
        ll_load_page.setVisibility(View.GONE);
        if (onHideListener != null) {
            onHideListener.onHide();
        }
    }


    public void hideLoadPageFirstLoad(final Animator.AnimatorListener listener) {
        Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                iv_loading.setVisibility(View.GONE);
                iv_loading.cancelAnimation();
                playendAnimation(listener);
            }
        };
        iv_loading.addAnimatorListener(animatorListener);
        iv_loading.playAnimation();
    }


    public void playendAnimation(Animator.AnimatorListener listener) {
        iv_loadingend.setVisibility(View.VISIBLE);
        iv_loadingend.setAnimation("loading140_130dp.json");
        iv_loadingend.loop(false);
        iv_loadingend.addAnimatorListener(listener);
        iv_loadingend.playAnimation();
    }

    public void showLoading() {
        unRegister();

        ll_error.setVisibility(View.GONE);
        iv_loading.setVisibility(View.VISIBLE);
        //iv_loading.clearAnimation();
        //iv_loading.startAnimation(createRotateAnimation());
    }

    public void showNoNetWork() {
        register();

        status = STATUS_NO_NETWORK;
        ll_error.setVisibility(View.VISIBLE);
        //iv_loading.clearAnimation();
        iv_loading.setVisibility(View.GONE);

        iv_icon.setImageResource(R.drawable.icon_no_network);
        tv_text.setText(context.getString(R.string.page_loading_no_network));
        btn.setText(context.getString(R.string.page_loading_set_network));
    }

    public void showNetworkError() {
        unRegister();

        status = STATUS_NETWORK_ERROR;
        ll_error.setVisibility(View.VISIBLE);
        //iv_loading.clearAnimation();
        iv_loading.setVisibility(View.GONE);

        iv_icon.setImageResource(R.drawable.icon_network_error);
        tv_text.setText(context.getString(R.string.page_loading_network_error));
        btn.setText(context.getString(R.string.page_loading_retry));
    }

    public void setOnShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
    }

    public void setOnHideListener(OnHideListener onHideListener) {
        this.onHideListener = onHideListener;
    }

    public void setOnRetryListener(OnRetryListener onRetryListener) {
        this.onRetryListener = onRetryListener;
    }

    public interface OnShowListener {
        public void onShow();
    }

    public interface OnHideListener {
        public void onHide();
    }

    public interface OnRetryListener {
        public void retry();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                if (status == STATUS_NO_NETWORK) {
                    // open setting
                    Intent intent = new Intent(
                            android.provider.Settings.ACTION_SETTINGS);
                    context.startActivity(intent);
                } else if (status == STATUS_NETWORK_ERROR) {
                    showLoading();
                    if (onRetryListener != null) {
                        onRetryListener.retry();
                    }
                }
                break;
        }
    }

    /**
     * @param @return
     * @return RotateAnimation
     * @throws
     * @Title: createRotateAnimation
     * @Description: 创建旋转动画
     */
    private RotateAnimation createRotateAnimation() {
        RotateAnimation animation = null;
        animation = new RotateAnimation(0, 3600, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setFillAfter(true);
        animation.setDuration(10000);
        animation.setStartOffset(0);
        animation.setRepeatCount(1000);
        return animation;
    }

    private void register() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CONNECTIVITY_CHANGE_ACTION);
        if (netWorkReceiver == null) {
            netWorkReceiver = new NetWorkReceiver();
            context.registerReceiver(netWorkReceiver, filter);
        }
    }

    private void unRegister() {
        if (netWorkReceiver != null) {
            context.unregisterReceiver(netWorkReceiver);
            netWorkReceiver = null;
        }
    }

    private class NetWorkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SystemUtils.hasNetwork()) {
                showLoading();
                if (onRetryListener != null) {
                    onRetryListener.retry();
                }
            }
        }
    }

}
