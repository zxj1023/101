package tran.com.android.gc.lib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import tran.com.android.gc.lib.R;

public class AuroraActionBarIcon extends AuroraAnimationImageView{

    
    
    
    public AuroraActionBarIcon(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AuroraActionBarIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        mAnimationBG = getDrawable(R.drawable.aurora_action_bar_icon_right_anim);
    }
    
    private Drawable getDrawable(int resId){
        
        return getResources().getDrawable(resId);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        if ((mAnimationBG != null) && mEnable) {
            canvas.save();
            final int w = getWidth();
            final int h = getHeight();
            ViewGroup parent = (ViewGroup) this.getParent();
            if (parent != null) {
                parent.setClipChildren(false);
                parent.setClipToPadding(false);
            }

            final float aspect = (float) mAnimationWidth / mAnimationHeight;
            final int drawW = (int) (h * aspect);
            final int drawH = h;
            final int margin = (drawW - w) / 2;

            mAnimationBG.setBounds(-margin, 0, drawW - margin, drawH);
            mAnimationBG.setAlpha((int) (mDrawingAlpha * mAlpha * 255));
            mAnimationBG.draw(canvas);
            canvas.restore();
            mRect.right = w;
            mRect.bottom = h;
        }
        super.onDraw(canvas);
    }
    
    
    
    
    
    
}
