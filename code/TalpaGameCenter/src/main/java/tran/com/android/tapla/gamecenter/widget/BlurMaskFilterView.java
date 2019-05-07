package tran.com.android.tapla.gamecenter.widget;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import tran.com.android.tapla.gamecenter.util.DevicesUtil;


/**
 * 说明：
 *
 * @auther lianbing.chen@itel-mobile.com
 * @date 2017/9/21 20:01
 */

public class BlurMaskFilterView extends View {
    private Paint mPaint;
    private Paint whitePaint;

    private float redius = DevicesUtil.dip2px(getContext(),16);
    private float w = DevicesUtil.dip2px(getContext(),84);
    private float h = DevicesUtil.dip2px(getContext(),32);

    public BlurMaskFilterView(Context context) {
        super(context);
        init();
    }

    public BlurMaskFilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BlurMaskFilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BlurMaskFilterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null); //View级别关闭硬件加速
        mPaint = new Paint();
        //mPaint.setColor(Color.parseColor("#0D000000"));
        mPaint.setColor(Color.RED);
        mPaint.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.OUTER));

        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF r1 = new RectF();                         //Rect对象
        r1.left = 26;                                 //左边
        r1.top = 26;                                  //上边
        r1.right = w+20;                                   //右边
        r1.bottom = h+20;                              //下边
        canvas.drawRoundRect(r1, redius, redius, mPaint);                 //绘制矩形

        Rect r = new Rect();                         //Rect对象
        r.left = 0;                                 //左边
        r.top = 0;                                  //上边
        r.right = (int) (w+56);                                   //右边
        r.bottom = ((int) (h+56))/2;                              //下边
        canvas.drawRect(r, whitePaint);                 //绘制矩形

    }
}
