package tran.com.android.tapla.gamecenter.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 说明：
 *
 * @auther lianbing.chen@itel-mobile.com
 * @date 2017/9/28 14:12
 */

public class AnimTextView extends TextView {

    private boolean showAnim = false;

    public AnimTextView(Context context) {
        super(context);
    }

    public AnimTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AnimTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void showAnim() {
        showAnim = true;
        startAnim();
    }

    public void stopAnim() {
        showAnim = false;
    }


    private void startAnim() {
        final String text = (String) getText();
        post(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (showAnim) {
                    if(i==0){
                        setText(text+".");
                    }else if(i==1){
                        setText(text+"..");
                    }else{
                        setText(text+"...");
                        i=0;
                    }
                    try {
                        Thread.sleep(240);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
