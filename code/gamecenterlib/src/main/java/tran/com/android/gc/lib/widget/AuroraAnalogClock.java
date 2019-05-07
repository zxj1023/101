/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tran.com.android.gc.lib.widget;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.AttributeSet;

import java.util.TimeZone;

import android.widget.ImageView;
import android.widget.RemoteViews.RemoteView;

import tran.com.android.gc.lib.utils.DensityUtil;

import tran.com.android.gc.lib.R;

/**
 * This widget display an analogic clock with two hands for hours and
 * minutes.
 */
@RemoteView
public class AuroraAnalogClock extends ImageView {
    private Time mCalendar;

    // Gionee <baorui><2013-04-28> modify for CR00799490 begin
    /*
    private final Drawable mHourHand;
    private final Drawable mMinuteHand;
    private final Drawable mSecondHand;
    private final Drawable mDial;
    */
    private Drawable mHourHand;
    private Drawable mMinuteHand;
    private Drawable mSecondHand;
    private Drawable mDial;
    // Gionee <baorui><2013-04-28> modify for CR00799490 end

    private final int mDialWidth;
    private final int mDialHeight;

    private boolean mAttached;

    private final Handler mHandler = new Handler();
    private float mSeconds;
    private float mMinutes;
    private float mHour;
    private boolean mChanged;
    private final Context mContext;
    private String mTimeZoneId;
    private boolean mNoSeconds = false;
    
    //aurora add by tangjun 2014.1.8 start
    private boolean dialNotNeed;
    private boolean hourAndMinuteNotNeed;
    private int mCount;
    private boolean mNeedRotate;
    //aurora add by tangjun 2014.1.8 end
    
    public AuroraAnalogClock(Context context) {
        this(context, null);
    }

    public AuroraAnalogClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AuroraAnalogClock(Context context, AttributeSet attrs,
                       int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        Resources r = mContext.getResources();

        mDial = r.getDrawable(R.drawable.aurora_appwidget_clock_dial);
        mHourHand = r.getDrawable(R.drawable.aurora_appwidget_clock_hour);
        mMinuteHand = r.getDrawable(R.drawable.aurora_appwidget_clock_minute);
        mSecondHand = r.getDrawable(R.drawable.aurora_appwidget_clock_second);

        mCalendar = new Time();

        mDialWidth = mDial.getIntrinsicWidth();
        mDialHeight = mDial.getIntrinsicHeight();
    }
    
    /**
     * @param dialNeed is need dial
     */
    public void setDialNotNeed( boolean dialNotNeed ) {
    	this.dialNotNeed = dialNotNeed;
    }
    
    /**
     * @param dialNeed is need hour and minute
     */
    public void setHourAndMinuteNotNeed( boolean hourAndMinuteNotNeed ) {
    	this.hourAndMinuteNotNeed = hourAndMinuteNotNeed;
    }
    
    /**
     * @param count 设置时钟和分钟动画
     */
    public void setHourAndMinuteAnim(int count, boolean needRotate) {
    	mCount = count;
    	mNeedRotate = needRotate;
    	this.invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!mAttached) {
            mAttached = true;
            IntentFilter filter = new IntentFilter();

            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);

            getContext().registerReceiver(mIntentReceiver, filter, null, mHandler);
        }

        // NOTE: It's safe to do these after registering the receiver since the receiver always runs
        // in the main thread, therefore the receiver can't run before this method returns.

        // The time zone may have changed while the receiver wasn't registered, so update the Time
        mCalendar = new Time();

        // Make sure we update to the current time
        onTimeChanged();

        // tick the seconds
        post(mClockTick);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            getContext().unregisterReceiver(mIntentReceiver);
            removeCallbacks(mClockTick);
            mAttached = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize =  MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize =  MeasureSpec.getSize(heightMeasureSpec);

        float hScale = 1.0f;
        float vScale = 1.0f;

        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            hScale = (float) widthSize / (float) mDialWidth;
        }

        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            vScale = (float )heightSize / (float) mDialHeight;
        }

        float scale = Math.min(hScale, vScale);

        setMeasuredDimension(resolveSizeAndState((int) (mDialWidth * scale), widthMeasureSpec, 0),
                resolveSizeAndState((int) (mDialHeight * scale), heightMeasureSpec, 0));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChanged = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG)); 
        
        if ( dialNotNeed && hourAndMinuteNotNeed ) {
        	return;
        }

        boolean changed = mChanged;
        if (changed) {
            mChanged = false;
        }

        int availableWidth = getWidth();
        int availableHeight = getHeight();

        int x = availableWidth / 2;
        int y = availableHeight / 2;

        final Drawable dial = mDial;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();

        boolean scaled = false;
        
        int alpha = 0;
        if ( mNeedRotate ) {
        	alpha = (255 - mCount * 9) >= 0 ? (255 - mCount * 9) : 0;
        } else {
        	alpha = (255 - mCount * 30) >= 0 ? (255 - mCount * 30) : 0;
        }

        if (availableWidth < w || availableHeight < h) {
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) w,
                                   (float) availableHeight / (float) h);
            canvas.save();
            canvas.scale(scale, scale, x, y);
        }

        if ( !dialNotNeed ) {
	        if (changed) {
	            dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
	        }
	        dial.draw(canvas);
        }
        
        if (!mNoSeconds) {
            canvas.save();
            if ( mNeedRotate ) {
            	canvas.rotate(mSeconds / 60.0f * 360.0f - mCount * 2, x, y);
            } else {
            	canvas.rotate(mSeconds / 60.0f * 360.0f, x, y);
            }

            final Drawable secondHand = mSecondHand;
            if (changed) {
                w = secondHand.getIntrinsicWidth();
                h = secondHand.getIntrinsicHeight();
                secondHand.setBounds(x - (w / 2), y - h + DensityUtil.dip2px(mContext, 5), x + (w / 2), y + DensityUtil.dip2px(mContext, 5));
            }
            secondHand.setAlpha(alpha);
            secondHand.draw(canvas);
            canvas.restore();
        }
        
        canvas.save();
        if ( mNeedRotate ) {
        	canvas.rotate(mMinutes / 60.0f * 360.0f - mCount * 2, x, y);
        } else {
        	canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);
        }

        final Drawable minuteHand = mMinuteHand;
        if (changed) {
            w = minuteHand.getIntrinsicWidth();
            h = minuteHand.getIntrinsicHeight();
            minuteHand.setBounds(x - (w / 2), y - h + DensityUtil.dip2px(mContext, 5), x + (w / 2), y + DensityUtil.dip2px(mContext, 5));
        }
        minuteHand.setAlpha(alpha);
        minuteHand.draw(canvas);
        canvas.restore();

        canvas.save();
        if ( mNeedRotate ) {
        	canvas.rotate(mHour / 12.0f * 360.0f - mCount * 5, x, y);
        } else {
        	canvas.rotate(mHour / 12.0f * 360.0f, x, y);
        }
        final Drawable hourHand = mHourHand;
        if (changed) {
            w = hourHand.getIntrinsicWidth();
            h = hourHand.getIntrinsicHeight();
            hourHand.setBounds(x - (w / 2), y - h + DensityUtil.dip2px(mContext, 5), x + (w / 2), y + DensityUtil.dip2px(mContext, 5));
        }
        hourHand.setAlpha(alpha);
        hourHand.draw(canvas);
        canvas.restore();

        if (scaled) {
            canvas.restore();
        }
    }

    private void onTimeChanged() {
        mCalendar.setToNow();

        if (mTimeZoneId != null) {
            mCalendar.switchTimezone(mTimeZoneId);
        }

        int hour = mCalendar.hour;
        int minute = mCalendar.minute;
        int second = mCalendar.second;
  //      long millis = System.currentTimeMillis() % 1000;

        mSeconds = second;//(float) ((second * 1000 + millis) / 166.666);
        mMinutes = minute + second / 60.0f;
        mHour = hour + mMinutes / 60.0f;
        mChanged = true;

        updateContentDescription(mCalendar);
    }

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                String tz = intent.getStringExtra("time-zone");
                mCalendar = new Time(TimeZone.getTimeZone(tz).getID());
            }
            onTimeChanged();
            invalidate();
        }
    };

    private final Runnable mClockTick = new Runnable () {

        @Override
        public void run() {
            onTimeChanged();
            invalidate();
            AuroraAnalogClock.this.postDelayed(mClockTick, 500);
        }
    };

    private void updateContentDescription(Time time) {
        final int flags = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_24HOUR;
        String contentDescription = DateUtils.formatDateTime(mContext,
                time.toMillis(false), flags);
        setContentDescription(contentDescription);
    }

    public void setTimeZone(String id) {
        mTimeZoneId = id;
        onTimeChanged();
    }

    public void enableSeconds(boolean enable) {
        mNoSeconds = !enable;
    }

    // Gionee <baorui><2013-04-28> modify for CR00799490 begin
    public void setmHourHand(Drawable mHourHand) {
        this.mHourHand = mHourHand;
    }

    public void setmMinuteHand(Drawable mMinuteHand) {
        this.mMinuteHand = mMinuteHand;
    }

    public void setmSecondHand(Drawable mSecondHand) {
        this.mSecondHand = mSecondHand;
    }

    public void setmDial(Drawable mDial) {
        this.mDial = mDial;
    }
    // Gionee <baorui><2013-04-28> modify for CR00799490 end

}

