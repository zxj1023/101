/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.transsion.ossdk.customeswitch;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.CompoundButton;

import com.transsion.ossdk.R;

/**
 * A Switch is a two-state toggle switch widget that can select between two
 * options. The user may drag the "thumb" back and forth to choose the selected option,
 * or simply tap to toggle as if it were a checkbox. The {@link #setText(CharSequence) text}
 * property controls the text displayed in the label for the switch, whereas the
 * {@link #setTextOff(CharSequence) off} and {@link #setTextOn(CharSequence) on} text
 * controls the text on the thumb. Similarly, the
 * {@link #setTextAppearance(Context, int) textAppearance} and the related
 * setTypeface() methods control the typeface and style of label text, whereas the
 * {@link #setSwitchTextAppearance(Context, int) switchTextAppearance} and
 * the related setSwitchTypeface() methods control that of the thumb.
 *
 * <p>See the <a href="{@docRoot}guide/topics/ui/controls/togglebutton.html">Toggle Buttons</a>
 * guide.</p>
 *
 * @attr ref android.R.styleable#Switch_textOn
 * @attr ref android.R.styleable#Switch_textOff
 * @attr ref android.R.styleable#Switch_switchMinWidth
 * @attr ref android.R.styleable#Switch_switchPadding
 * @attr ref android.R.styleable#Switch_switchTextAppearance
 * @attr ref android.R.styleable#Switch_thumb
 * @attr ref android.R.styleable#Switch_thumbTextPadding
 * @attr ref android.R.styleable#Switch_track
 */
public class Switch extends CompoundButton implements AnimHelper.ThumbPositionListener{
    private static final int THUMB_ANIMATION_DURATION = 256;

    private static final int TOUCH_MODE_IDLE = 0;
    private static final int TOUCH_MODE_DOWN = 1;
    private static final int TOUCH_MODE_DRAGGING = 2;

    // Enum for the "typeface" XML parameter.
    /** Begin: Ossdk for Switch Animation @{
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final int MONOSPACE = 3;
    * End: Ossdk for Switch Animation @}
    */

    private Drawable mThumbDrawable;
    private ColorStateList mThumbTintList = null;
    private PorterDuff.Mode mThumbTintMode = null;
    private boolean mHasThumbTint = false;
    private boolean mHasThumbTintMode = false;

    private Drawable mTrackDrawable;

    /**Begin: Ossdk for Switch Animation @{
    private ColorStateList mTrackTintList = null;
    private PorterDuff.Mode mTrackTintMode = null;
    private boolean mHasTrackTint = false;
    private boolean mHasTrackTintMode = false;
    * End: Ossdk for Switch Animation @}
    */
    private int mThumbTextPadding;
    private int mSwitchMinWidth;
    private int mSwitchPadding;
    private boolean mSplitTrack;
    private CharSequence mTextOn;
    private CharSequence mTextOff;
    private boolean mShowText;

    private int mTouchMode;
    private int mTouchSlop;
    private float mTouchX;
    private float mTouchY;
    private VelocityTracker mVelocityTracker = VelocityTracker.obtain();
    private int mMinFlingVelocity;

    private float mThumbPosition;

    /**
     * Width required to draw the switch track and thumb. Includes padding and
     * optical bounds for both the track and thumb.
     */
    private int mSwitchWidth;

    /**
     * Height required to draw the switch track and thumb. Includes padding and
     * optical bounds for both the track and thumb.
     */
    private int mSwitchHeight;

    /**
     * Width of the thumb's content region. Does not include padding or
     * optical bounds.
     */
    private int mThumbWidth;

    /** Left bound for drawing the switch track and thumb. */
    private int mSwitchLeft;

    /** Top bound for drawing the switch track and thumb. */
    private int mSwitchTop;

    /** Right bound for drawing the switch track and thumb. */
    private int mSwitchRight;

    /** Bottom bound for drawing the switch track and thumb. */
    private int mSwitchBottom;

    private TextPaint mTextPaint;

    private ColorStateList mTextColors;
    private Layout mOnLayout;
    private Layout mOffLayout;
    /**Begin: Ossdk for Switch Animation @{
    private TransformationMethod2 mSwitchTransformationMethod;
    private ObjectAnimator mPositionAnimator;
    * End: Ossdk for Switch Animation @}
    */

    //Begin: Ossdk for Switch Animation @{
    public CharSequence switchOn;
    public CharSequence switchOff;
    private static int[] sSwitchStyleableId;

    private Drawable mThumbOnDrawable;
    private Drawable mThumbOffDrawable;
    private float mThumbOnScaleCenterX;
    private float mThumbOffScaleCenterX;
    private float mThumbScaleCenterY;

    //thumb position
    private static final float THUMB_STATUS_CHANGE = 1f;

    private AnimHelper mAnimHelper;

    //trackon drawable
    private Drawable mTrackOnDrawable;

    //trackoff drawable
    private Drawable mTrackOffDrawable;
    private float mTrackOffScaleCenterX;
    private float mTrackScaleCenterY;

    //track bg
    private Paint mBgPaint;
    private Path mBgPath;
    //End: Ossdk for Switch Animation @}

    @SuppressWarnings("hiding")
    private final Rect mTempRect = new Rect();

    private static final int[] CHECKED_STATE_SET = {
        android.R.attr.state_checked
    };

    /**
     * Construct a new Switch with default styling.
     *
     * @param context The Context that will determine this widget's theming.
     */
    public Switch(Context context) {
        this(context, null);
    }

    /**
     * Construct a new Switch with default styling, overriding specific style
     * attributes as requested.
     *
     * @param context The Context that will determine this widget's theming.
     * @param attrs Specification of attributes that should deviate from default styling.
     */
    public Switch(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.Ossdk_Switch);
    }

    /**
     * Construct a new Switch with a default style determined by the given theme attribute,
     * overriding specific style attributes as requested.
     *
     * @param context The Context that will determine this widget's theming.
     * @param attrs Specification of attributes that should deviate from the default styling.
     * @param defStyleAttr An attribute in the current theme that contains a
     *        reference to a style resource that supplies default values for
     *        the view. Can be 0 to not look for defaults.
     */
    public Switch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        final Resources res = getResources();
        mTextPaint.density = res.getDisplayMetrics().density;
        /**Begin: Ossdk for Switch Animation @{
        //mTextPaint.setCompatibilityScaling(res.getCompatibilityInfo().applicationScale);
         End: Ossdk for Switch Animation @}
        */
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.TalpaOssdkSwitch, defStyleAttr, 0);
        mThumbDrawable = a.getDrawable(R.styleable.TalpaOssdkSwitch_talpaossdkThumb);
        if (mThumbDrawable != null) {
            mThumbDrawable.setCallback(this);
            //Begin: Ossdk for Switch Animation @{
            if(mThumbDrawable.getConstantState() != null){
                mThumbOnDrawable = mThumbDrawable.getConstantState().newDrawable().mutate();
                mThumbOnDrawable.setState(new int[]{android.R.attr.state_enabled, android.R.attr.state_checked});
                mThumbOffDrawable = mThumbDrawable.getConstantState().newDrawable().mutate();
                mThumbOffDrawable.setState(new int[]{android.R.attr.state_enabled, -android.R.attr.state_checked});
            }
            //End: Ossdk for Switch Animation @}
        }
        mTrackDrawable = a.getDrawable(R.styleable.TalpaOssdkSwitch_talpaossdkTrack);
        if (mTrackDrawable != null) {
            mTrackDrawable.setCallback(this);
            //Begin: Ossdk for Switch Animation @{
            if (mTrackDrawable.getConstantState() != null) {
                mTrackOnDrawable = mTrackDrawable.getConstantState().newDrawable().mutate();
                mTrackOnDrawable.setState(new int[]{android.R.attr.state_enabled, android.R.attr.state_checked});
                mTrackOffDrawable = mTrackDrawable.getConstantState().newDrawable().mutate();
                mTrackOffDrawable.setState(new int[]{android.R.attr.state_enabled, -android.R.attr.state_checked});
            }
            //End: Ossdk for Switch Animation @}
        }

        /* Ossdk for Switch Animation @{
        mTextOn = a.getText(com.android.internal.R.styleable.Switch_textOn);
        mTextOff = a.getText(com.android.internal.R.styleable.Switch_textOff);
        mShowText = a.getBoolean(com.android.internal.R.styleable.Switch_showText, true);
        mThumbTextPadding = a.getDimensionPixelSize(com.android.internal.R.styleable.Switch_thumbTextPadding, 0);
        End: Ossdk for Switch Animation @}
        */
        mSwitchMinWidth = a.getDimensionPixelSize(
                R.styleable.TalpaOssdkSwitch_talpaossdkSwitchMinWidth, 0);
        mSwitchPadding = a.getDimensionPixelSize(
                R.styleable.TalpaOssdkSwitch_talpaossdkSwitchPadding, 0);
        mSplitTrack = a.getBoolean(R.styleable.TalpaOssdkSwitch_talpaossdkSplitTrack, false);

        /* Ossdk for Switch Animation @{
        ColorStateList thumbTintList = a.getColorStateList(
                com.android.internal.R.styleable.Switch_thumbTint);
        if (thumbTintList != null) {
            mThumbTintList = thumbTintList;
            mHasThumbTint = true;
        }
        PorterDuff.Mode thumbTintMode = Drawable.parseTintMode(
                a.getInt(com.android.internal.R.styleable.Switch_thumbTintMode, -1), null);
        if (mThumbTintMode != thumbTintMode) {
            mThumbTintMode = thumbTintMode;
            mHasThumbTintMode = true;
        }
        if (mHasThumbTint || mHasThumbTintMode) {
            applyThumbTint();
        }

        ColorStateList trackTintList = a.getColorStateList(
                com.android.internal.R.styleable.Switch_trackTint);
        if (trackTintList != null) {
            mTrackTintList = trackTintList;
            mHasTrackTint = true;
        }
        PorterDuff.Mode trackTintMode = Drawable.parseTintMode(
                a.getInt(com.android.internal.R.styleable.Switch_trackTintMode, -1), null);
        if (mTrackTintMode != trackTintMode) {
            mTrackTintMode = trackTintMode;
            mHasTrackTintMode = true;
        }
        if (mHasTrackTint || mHasTrackTintMode) {
            applyTrackTint();
        }

        final int appearance = a.getResourceId(
                com.android.internal.R.styleable.Switch_switchTextAppearance, 0);
        if (appearance != 0) {
            setSwitchTextAppearance(context, appearance);
        }
        End: Ossdk for Switch Animation @}
        */
        a.recycle();

        //Ossdk for Switch Animation @{
        sSwitchStyleableId = new int[]{android.R.attr.textOn, android.R.attr.textOff};
        final TypedArray b = context.obtainStyledAttributes(attrs, sSwitchStyleableId, android.R.attr.switchStyle, 0);
        switchOn = b.getText(0);
        switchOff = b.getText(1);
        b.recycle();
        initBgDrawParams();
        //End: Ossdk for Switch Animation @}

        final ViewConfiguration config = ViewConfiguration.get(context);
        mTouchSlop = config.getScaledTouchSlop();
        mMinFlingVelocity = config.getScaledMinimumFlingVelocity();

        // Refresh display with current params
        refreshDrawableState();
        setChecked(isChecked());
    }

    /**
     * Construct a new Switch with a default style determined by the given theme
     * attribute or style resource, overriding specific style attributes as
     * requested.
     *
     * @param context The Context that will determine this widget's theming.
     * @param attrs Specification of attributes that should deviate from the
     *        default styling.
     * @param defStyleAttr An attribute in the current theme that contains a
     *        reference to a style resource that supplies default values for
     *        the view. Can be 0 to not look for defaults.
     * @param defStyleRes A resource identifier of a style resource that
     *        supplies default values for the view, used only if
     *        defStyleAttr is 0 or can not be found in the theme. Can be 0
     *        to not look for defaults.
     */
    /* Ossdk for Switch Animation @{
    public Switch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        final Resources res = getResources();
        mTextPaint.density = res.getDisplayMetrics().density;
        mTextPaint.setCompatibilityScaling(res.getCompatibilityInfo().applicationScale);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, com.android.internal.R.styleable.Switch, defStyleAttr, defStyleRes);
        mThumbDrawable = a.getDrawable(com.android.internal.R.styleable.Switch_thumb);
        if (mThumbDrawable != null) {
            mThumbDrawable.setCallback(this);
        }
        mTrackDrawable = a.getDrawable(com.android.internal.R.styleable.Switch_track);
        if (mTrackDrawable != null) {
            mTrackDrawable.setCallback(this);
        }
        mTextOn = a.getText(com.android.internal.R.styleable.Switch_textOn);
        mTextOff = a.getText(com.android.internal.R.styleable.Switch_textOff);
        mShowText = a.getBoolean(com.android.internal.R.styleable.Switch_showText, true);
        mThumbTextPadding = a.getDimensionPixelSize(
                com.android.internal.R.styleable.Switch_thumbTextPadding, 0);
        mSwitchMinWidth = a.getDimensionPixelSize(
                com.android.internal.R.styleable.Switch_switchMinWidth, 0);
        mSwitchPadding = a.getDimensionPixelSize(
                com.android.internal.R.styleable.Switch_switchPadding, 0);
        mSplitTrack = a.getBoolean(com.android.internal.R.styleable.Switch_splitTrack, false);

        ColorStateList thumbTintList = a.getColorStateList(
                com.android.internal.R.styleable.Switch_thumbTint);
        if (thumbTintList != null) {
            mThumbTintList = thumbTintList;
            mHasThumbTint = true;
        }
        PorterDuff.Mode thumbTintMode = Drawable.parseTintMode(
                a.getInt(com.android.internal.R.styleable.Switch_thumbTintMode, -1), null);
        if (mThumbTintMode != thumbTintMode) {
            mThumbTintMode = thumbTintMode;
            mHasThumbTintMode = true;
        }
        if (mHasThumbTint || mHasThumbTintMode) {
            applyThumbTint();
        }

        ColorStateList trackTintList = a.getColorStateList(
                com.android.internal.R.styleable.Switch_trackTint);
        if (trackTintList != null) {
            mTrackTintList = trackTintList;
            mHasTrackTint = true;
        }
        PorterDuff.Mode trackTintMode = Drawable.parseTintMode(
                a.getInt(com.android.internal.R.styleable.Switch_trackTintMode, -1), null);
        if (mTrackTintMode != trackTintMode) {
            mTrackTintMode = trackTintMode;
            mHasTrackTintMode = true;
        }
        if (mHasTrackTint || mHasTrackTintMode) {
            applyTrackTint();
        }

        final int appearance = a.getResourceId(
                com.android.internal.R.styleable.Switch_switchTextAppearance, 0);
        if (appearance != 0) {
            setSwitchTextAppearance(context, appearance);
        }
        a.recycle();

        final ViewConfiguration config = ViewConfiguration.get(context);
        mTouchSlop = config.getScaledTouchSlop();
        mMinFlingVelocity = config.getScaledMinimumFlingVelocity();

        // Refresh display with current params
        refreshDrawableState();
        setChecked(isChecked());
    }
    End: Ossdk for Switch Animation @}
    */

    /**
     * Sets the switch text color, size, style, hint color, and highlight color
     * from the specified TextAppearance resource.
     *
     * @attr ref android.R.styleable#Switch_switchTextAppearance
     */
    /* Begin: Ossdk for Switch Animation @{
    public void setSwitchTextAppearance(Context context, @StyleRes int resid) {
        TypedArray appearance =
                context.obtainStyledAttributes(resid,
                        com.android.internal.R.styleable.TextAppearance);

        ColorStateList colors;
        int ts;

        colors = appearance.getColorStateList(com.android.internal.R.styleable.
                TextAppearance_textColor);
        if (colors != null) {
            mTextColors = colors;
        } else {
            // If no color set in TextAppearance, default to the view's textColor
            mTextColors = getTextColors();
        }

        ts = appearance.getDimensionPixelSize(com.android.internal.R.styleable.
                TextAppearance_textSize, 0);
        if (ts != 0) {
            if (ts != mTextPaint.getTextSize()) {
                mTextPaint.setTextSize(ts);
                requestLayout();
            }
        }

        int typefaceIndex, styleIndex;

        typefaceIndex = appearance.getInt(com.android.internal.R.styleable.
                TextAppearance_typeface, -1);
        styleIndex = appearance.getInt(com.android.internal.R.styleable.
                TextAppearance_textStyle, -1);

        setSwitchTypefaceByIndex(typefaceIndex, styleIndex);

        boolean allCaps = appearance.getBoolean(com.android.internal.R.styleable.
                TextAppearance_textAllCaps, false);
        if (allCaps) {
            mSwitchTransformationMethod = new AllCapsTransformationMethod(getContext());
            mSwitchTransformationMethod.setLengthChangesAllowed(true);
        } else {
            mSwitchTransformationMethod = null;
        }

        appearance.recycle();
    }
    End: Ossdk for Switch Animation @}
    */

    /* Begin: Ossdk for Switch Animation @{
    private void setSwitchTypefaceByIndex(int typefaceIndex, int styleIndex) {
        Typeface tf = null;
        switch (typefaceIndex) {
            case SANS:
                tf = Typeface.SANS_SERIF;
                break;

            case SERIF:
                tf = Typeface.SERIF;
                break;

            case MONOSPACE:
                tf = Typeface.MONOSPACE;
                break;
        }

        setSwitchTypeface(tf, styleIndex);
    }
    End: Ossdk for Switch Animation @}
    */

    /**
     * Sets the typeface and style in which the text should be displayed on the
     * switch, and turns on the fake bold and italic bits in the Paint if the
     * Typeface that you provided does not have all the bits in the
     * style that you specified.
     */
    /* Begin: Ossdk for Switch Animation @{
    public void setSwitchTypeface(Typeface tf, int style) {
        if (style > 0) {
            if (tf == null) {
                tf = Typeface.defaultFromStyle(style);
            } else {
                tf = Typeface.create(tf, style);
            }

            setSwitchTypeface(tf);
            // now compute what (if any) algorithmic styling is needed
            int typefaceStyle = tf != null ? tf.getStyle() : 0;
            int need = style & ~typefaceStyle;
            mTextPaint.setFakeBoldText((need & Typeface.BOLD) != 0);
            mTextPaint.setTextSkewX((need & Typeface.ITALIC) != 0 ? -0.25f : 0);
        } else {
            mTextPaint.setFakeBoldText(false);
            mTextPaint.setTextSkewX(0);
            setSwitchTypeface(tf);
        }
    }
    End: Ossdk for Switch Animation @}
    */

    /**
     * Sets the typeface in which the text should be displayed on the switch.
     * Note that not all Typeface families actually have bold and italic
     * variants, so you may need to use
     * {@link #setSwitchTypeface(Typeface, int)} to get the appearance
     * that you actually want.
     *
     * @attr ref android.R.styleable#TextView_typeface
     * @attr ref android.R.styleable#TextView_textStyle
     */
    /* Begin: Ossdk for Switch Animation @{
    public void setSwitchTypeface(Typeface tf) {
        if (mTextPaint.getTypeface() != tf) {
            mTextPaint.setTypeface(tf);

            requestLayout();
            invalidate();
        }
    }
    End: Ossdk for Switch Animation @}
    */

    /**
     * Set the amount of horizontal padding between the switch and the associated text.
     *
     * @param pixels Amount of padding in pixels
     *
     * @attr ref android.R.styleable#Switch_switchPadding
     */
    public void setSwitchPadding(int pixels) {
        mSwitchPadding = pixels;
        requestLayout();
    }

    /**
     * Get the amount of horizontal padding between the switch and the associated text.
     *
     * @return Amount of padding in pixels
     *
     * @attr ref android.R.styleable#Switch_switchPadding
     */
    public int getSwitchPadding() {
        return mSwitchPadding;
    }

    /**
     * Set the minimum width of the switch in pixels. The switch's width will be the maximum
     * of this value and its measured width as determined by the switch drawables and text used.
     *
     * @param pixels Minimum width of the switch in pixels
     *
     * @attr ref android.R.styleable#Switch_switchMinWidth
     */
    public void setSwitchMinWidth(int pixels) {
        mSwitchMinWidth = pixels;
        requestLayout();
    }

    /**
     * Get the minimum width of the switch in pixels. The switch's width will be the maximum
     * of this value and its measured width as determined by the switch drawables and text used.
     *
     * @return Minimum width of the switch in pixels
     *
     * @attr ref android.R.styleable#Switch_switchMinWidth
     */
    public int getSwitchMinWidth() {
        return mSwitchMinWidth;
    }

    /**
     * Set the horizontal padding around the text drawn on the switch itself.
     *
     * @param pixels Horizontal padding for switch thumb text in pixels
     *
     * @attr ref android.R.styleable#Switch_thumbTextPadding
     */
    public void setThumbTextPadding(int pixels) {
        mThumbTextPadding = pixels;
        requestLayout();
    }

    /**
     * Get the horizontal padding around the text drawn on the switch itself.
     *
     * @return Horizontal padding for switch thumb text in pixels
     *
     * @attr ref android.R.styleable#Switch_thumbTextPadding
     */
    public int getThumbTextPadding() {
        return mThumbTextPadding;
    }

    /**
     * Set the drawable used for the track that the switch slides within.
     *
     * @param track Track drawable
     *
     * @attr ref android.R.styleable#Switch_track
     */
    public void setTrackDrawable(Drawable track) {
        if (mTrackDrawable != null) {
            mTrackDrawable.setCallback(null);
        }
        mTrackDrawable = track;
        if (track != null) {
            track.setCallback(this);
        }
        requestLayout();
    }

    /**
     * Set the drawable used for the track that the switch slides within.
     *
     * @param resId Resource ID of a track drawable
     *
     * @attr ref android.R.styleable#Switch_track
     */
    public void setTrackResource(int resId) {
        setTrackDrawable(getContext().getDrawable(resId));
    }

    /**
     * Get the drawable used for the track that the switch slides within.
     *
     * @return Track drawable
     *
     * @attr ref android.R.styleable#Switch_track
     */
    public Drawable getTrackDrawable() {
        return mTrackDrawable;
    }

    /**
     * Applies a tint to the track drawable. Does not modify the current
     * tint mode, which is {@link PorterDuff.Mode#SRC_IN} by default.
     * <p>
     * Subsequent calls to {@link #setTrackDrawable(Drawable)} will
     * automatically mutate the drawable and apply the specified tint and tint
     * mode using {@link Drawable#setTintList(ColorStateList)}.
     *
     * @param tint the tint to apply, may be {@code null} to clear tint
     *
     * @attr ref android.R.styleable#Switch_trackTint
     * @see #getTrackTintList()
     * @see Drawable#setTintList(ColorStateList)
     */
    /* Begin: Ossdk for Switch Animation @{
    public void setTrackTintList(ColorStateList tint) {
        mTrackTintList = tint;
        mHasTrackTint = true;

        applyTrackTint();
    }
    End: Ossdk for Switch Animation @}
    */

    /**
     * @return the tint applied to the track drawable
     * @attr ref android.R.styleable#Switch_trackTint
     * @see #setTrackTintList(ColorStateList)
     */
    /* Begin: Ossdk for Switch Animation @{
    public ColorStateList getTrackTintList() {
        return mTrackTintList;
    }
    End: Ossdk for Switch Animation @}
    */

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setTrackTintList(ColorStateList)}} to the track drawable.
     * The default mode is {@link PorterDuff.Mode#SRC_IN}.
     *
     * @param tintMode the blending mode used to apply the tint, may be
     *                 {@code null} to clear tint
     * @attr ref android.R.styleable#Switch_trackTintMode
     * @see #getTrackTintMode()
     * @see Drawable#setTintMode(PorterDuff.Mode)
     */
    /* Begin: Ossdk for Switch Animation @{
    public void setTrackTintMode(PorterDuff.Mode tintMode) {
        mTrackTintMode = tintMode;
        mHasTrackTintMode = true;

        applyTrackTint();
    }
    End: Ossdk for Switch Animation @}
    */

    /**
     * @return the blending mode used to apply the tint to the track
     *         drawable
     * @attr ref android.R.styleable#Switch_trackTintMode
     * @see #setTrackTintMode(PorterDuff.Mode)
     */
    /* Begin: Ossdk for Switch Animation @{
    public PorterDuff.Mode getTrackTintMode() {
        return mTrackTintMode;
    }
    End: Ossdk for Switch Animation @}
    */

    /* Begin: Ossdk for Switch Animation @{
    private void applyTrackTint() {
        if (mTrackDrawable != null && (mHasTrackTint || mHasTrackTintMode)) {
            mTrackDrawable = mTrackDrawable.mutate();

            if (mHasTrackTint) {
                mTrackDrawable.setTintList(mTrackTintList);
            }

            if (mHasTrackTintMode) {
                mTrackDrawable.setTintMode(mTrackTintMode);
            }

            // The drawable (or one of its children) may not have been
            // stateful before applying the tint, so let's try again.
            if (mTrackDrawable.isStateful()) {
                mTrackDrawable.setState(getDrawableState());
            }
        }
    }
    End: Ossdk for Switch Animation @}
    */

    /**
     * Set the drawable used for the switch "thumb" - the piece that the user
     * can physically touch and drag along the track.
     *
     * @param thumb Thumb drawable
     *
     * @attr ref android.R.styleable#Switch_thumb
     */
    public void setThumbDrawable(Drawable thumb) {
        if (mThumbDrawable != null) {
            mThumbDrawable.setCallback(null);
        }
        mThumbDrawable = thumb;
        if (thumb != null) {
            thumb.setCallback(this);
        }
        requestLayout();
    }

    /**
     * Set the drawable used for the switch "thumb" - the piece that the user
     * can physically touch and drag along the track.
     *
     * @param resId Resource ID of a thumb drawable
     *
     * @attr ref android.R.styleable#Switch_thumb
     */
    public void setThumbResource(int resId) {
        setThumbDrawable(getContext().getDrawable(resId));
    }

    /**
     * Get the drawable used for the switch "thumb" - the piece that the user
     * can physically touch and drag along the track.
     *
     * @return Thumb drawable
     *
     * @attr ref android.R.styleable#Switch_thumb
     */
    public Drawable getThumbDrawable() {
        return mThumbDrawable;
    }

    /**
     * Applies a tint to the thumb drawable. Does not modify the current
     * tint mode, which is {@link PorterDuff.Mode#SRC_IN} by default.
     * <p>
     * Subsequent calls to {@link #setThumbDrawable(Drawable)} will
     * automatically mutate the drawable and apply the specified tint and tint
     * mode using {@link Drawable#setTintList(ColorStateList)}.
     *
     * @param tint the tint to apply, may be {@code null} to clear tint
     *
     * @attr ref android.R.styleable#Switch_thumbTint
     * @see #getThumbTintList()
     * @see Drawable#setTintList(ColorStateList)
     */
    /*
    public void setThumbTintList(ColorStateList tint) {
        mThumbTintList = tint;
        mHasThumbTint = true;

        applyThumbTint();
    }
    */
    /**
     * @return the tint applied to the thumb drawable
     * @attr ref android.R.styleable#Switch_thumbTint
     * @see #setThumbTintList(ColorStateList)
     */
    /*
    public ColorStateList getThumbTintList() {
        return mThumbTintList;
    }
    */

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setThumbTintList(ColorStateList)}} to the thumb drawable.
     * The default mode is {@link PorterDuff.Mode#SRC_IN}.
     *
     * @param tintMode the blending mode used to apply the tint, may be
     *                 {@code null} to clear tint
     * @attr ref android.R.styleable#Switch_thumbTintMode
     * @see #getThumbTintMode()
     * @see Drawable#setTintMode(PorterDuff.Mode)
     */
    public void setThumbTintMode(PorterDuff.Mode tintMode) {
        mThumbTintMode = tintMode;
        mHasThumbTintMode = true;

        applyThumbTint();
    }

    /**
     * @return the blending mode used to apply the tint to the thumb
     *         drawable
     * @attr ref android.R.styleable#Switch_thumbTintMode
     * @see #setThumbTintMode(PorterDuff.Mode)
     */
    public PorterDuff.Mode getThumbTintMode() {
        return mThumbTintMode;
    }

    private void applyThumbTint() {
        if (mThumbDrawable != null && (mHasThumbTint || mHasThumbTintMode)) {
            mThumbDrawable = mThumbDrawable.mutate();

            if (mHasThumbTint) {
                mThumbDrawable.setTintList(mThumbTintList);
            }

            if (mHasThumbTintMode) {
                mThumbDrawable.setTintMode(mThumbTintMode);
            }

            // The drawable (or one of its children) may not have been
            // stateful before applying the tint, so let's try again.
            if (mThumbDrawable.isStateful()) {
                mThumbDrawable.setState(getDrawableState());
            }
        }
    }

    /**
     * Specifies whether the track should be split by the thumb. When true,
     * the thumb's optical bounds will be clipped out of the track drawable,
     * then the thumb will be drawn into the resulting gap.
     *
     * @param splitTrack Whether the track should be split by the thumb
     *
     * @attr ref android.R.styleable#Switch_splitTrack
     */
    public void setSplitTrack(boolean splitTrack) {
        mSplitTrack = splitTrack;
        invalidate();
    }

    /**
     * Returns whether the track should be split by the thumb.
     *
     * @attr ref android.R.styleable#Switch_splitTrack
     */
    public boolean getSplitTrack() {
        return mSplitTrack;
    }

    /**
     * Returns the text displayed when the button is in the checked state.
     *
     * @attr ref android.R.styleable#Switch_textOn
     */
    /* Begin: Ossdk for Switch Animation @{
    public CharSequence getTextOn() {
        return mTextOn;
    }
    End: Ossdk for Switch Animation @}
    */

    /**
     * Sets the text displayed when the button is in the checked state.
     *
     * @attr ref android.R.styleable#Switch_textOn
     */
    public void setTextOn(CharSequence textOn) {
        mTextOn = textOn;
        requestLayout();
    }

    /**
     * Returns the text displayed when the button is not in the checked state.
     *
     * @attr ref android.R.styleable#Switch_textOff
     */
    /* Begin: Ossdk for Switch Animation @{
    public CharSequence getTextOff() {
        return mTextOff;
    }
    End: Ossdk for Switch Animation @}
    */

    /**
     * Sets the text displayed when the button is not in the checked state.
     *
     * @attr ref android.R.styleable#Switch_textOff
     */
    public void setTextOff(CharSequence textOff) {
        mTextOff = textOff;
        requestLayout();
    }

    /**
     * Sets whether the on/off text should be displayed.
     *
     * @param showText {@code true} to display on/off text
     * @attr ref android.R.styleable#Switch_showText
     */
    /* Begin: Ossdk for Switch Animation @{
    public void setShowText(boolean showText) {
        if (mShowText != showText) {
            mShowText = showText;
            requestLayout();
        }
    }
    End: Ossdk for Switch Animation @}
    */

    /**
     * @return whether the on/off text should be displayed
     * @attr ref android.R.styleable#Switch_showText
     */
    /* EBegin: Ossdk for Switch Animation @{
    public boolean getShowText() {
        return mShowText;
    }
    End: Ossdk for Switch Animation @}
    */

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /* Begin: Ossdk for Switch Animation @{
        if (mShowText) {
            if (mOnLayout == null) {
                mOnLayout = makeLayout(mTextOn);
            }

            if (mOffLayout == null) {
                mOffLayout = makeLayout(mTextOff);
            }
        }
        End: Ossdk for Switch Animation @}
        */

        final Rect padding = mTempRect;
        final int thumbWidth;
        final int thumbHeight;
        if (mThumbDrawable != null) {
            // Cached thumb width does not include padding.
            mThumbDrawable.getPadding(padding);
            thumbWidth = mThumbDrawable.getIntrinsicWidth() - padding.left - padding.right;
            thumbHeight = mThumbDrawable.getIntrinsicHeight();
        } else {
            thumbWidth = 0;
            thumbHeight = 0;
        }
        /* Begin: Ossdk for Switch Animation @{
        final int maxTextWidth;
        if (mShowText) {
            maxTextWidth = Math.max(mOnLayout.getWidth(), mOffLayout.getWidth())
                    + mThumbTextPadding * 2;
        } else {
            maxTextWidth = 0;
        }
        End: Ossdk for Switch Animation @}
        */
        //Begin: Ossdk for Switch Animation @{
        //mThumbWidth = Math.max(maxTextWidth, thumbWidth); //old code
        mThumbWidth = thumbWidth;   //new code
        //End: Ossdk for Switch Animation @}

        final int trackHeight;
        final int trackWidth; //Ossdk for Switch Animation
        if (mTrackDrawable != null) {
            mTrackDrawable.getPadding(padding);
            trackHeight = mTrackDrawable.getIntrinsicHeight();
            trackWidth = mTrackDrawable.getIntrinsicWidth(); //Ossdk for Switch Animation
        } else {
            padding.setEmpty();
            trackHeight = 0;
            trackWidth = 0; //Ossdk for Switch Animation
        }

        // Adjust left and right padding to ensure there's enough room for the
        // thumb's padding (when present).
        int paddingLeft = padding.left;
        int paddingRight = padding.right;
        if (mThumbDrawable != null) {
            final Insets inset = Insets.NONE; //mThumbDrawable.getOpticalInsets(); //Ossdk for Switch Animation
            paddingLeft = Math.max(paddingLeft, inset.left);
            paddingRight = Math.max(paddingRight, inset.right);
        }

        //Begin: Ossdk for Switch Animation @{
        /**final int switchWidth = Math.max(mSwitchMinWidth,
                2 * mThumbWidth + paddingLeft + paddingRight);
        mSwitchWidth = switchWidth; //old code */
        mSwitchWidth = trackWidth;// new code
        //End: Ossdk for Switch Animation @}

        final int switchHeight = Math.max(trackHeight, thumbHeight);
        mSwitchHeight = switchHeight;

        //Begin: Ossdk for Switch Animation @{
        mTrackOffScaleCenterX = 52 + PADDING + DRAWABLE_PADDING;//(float)(mSwitchWidth - mThumbWidth / 2.0);
        mTrackScaleCenterY = (float)(mSwitchHeight / 2.0);
        //End: Ossdk for Switch Animation @}

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int measuredHeight = getMeasuredHeight();
        if (measuredHeight < switchHeight) {
            setMeasuredDimension(getMeasuredWidthAndState(), switchHeight);
        }
    }

    /** @hide */
    /* Begin: Ossdk for Switch Animation @{
    @Override
    public void onPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        super.onPopulateAccessibilityEventInternal(event);

        final CharSequence text = isChecked() ? mTextOn : mTextOff;
        if (text != null) {
            event.getText().add(text);
        }
    }
    End: Ossdk for Switch Animation @}
    */

    /* Begin: Ossdk for Switch Animation @{
    private Layout makeLayout(CharSequence text) {
        final CharSequence transformed = (mSwitchTransformationMethod != null)
                    ? mSwitchTransformationMethod.getTransformation(text, this)
                    : text;

        return new StaticLayout(transformed, mTextPaint,
                (int) Math.ceil(Layout.getDesiredWidth(transformed, mTextPaint)),
                Layout.Alignment.ALIGN_NORMAL, 1.f, 0, true);
    }
    End: Ossdk for Switch Animation @}
    */

    /**
     * @return true if (x, y) is within the target area of the switch thumb
     */
    /* Begin: Ossdk for Switch Animation @{
    private boolean hitThumb(float x, float y) {
        if (mThumbDrawable == null) {
            return false;
        }

        // Relies on mTempRect, MUST be called first!
        final int thumbOffset = getThumbOffset();

        mThumbDrawable.getPadding(mTempRect);
        final int thumbTop = mSwitchTop - mTouchSlop;
        final int thumbLeft = mSwitchLeft + thumbOffset - mTouchSlop;
        final int thumbRight = thumbLeft + mThumbWidth +
                mTempRect.left + mTempRect.right + mTouchSlop;
        final int thumbBottom = mSwitchBottom + mTouchSlop;
        return x > thumbLeft && x < thumbRight && y > thumbTop && y < thumbBottom;
    }
    End: Ossdk for Switch Animation @}
    */

    /**
     * @return true if (x, y) is within the target area of the switch thumb
     */
    //Begin: Ossdk for Switch Animation @{
    private boolean hitThumb(float x, float y) {
        return x > mSwitchLeft && x < mSwitchRight &&
                y > mSwitchTop && y < mSwitchBottom;
    }
    //End: Ossdk for Switch Animation @}

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mVelocityTracker.addMovement(ev);
        final int action = ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();
                if (isEnabled() && hitThumb(x, y)) {
                    mTouchMode = TOUCH_MODE_DOWN;
                    mTouchX = x;
                    mTouchY = y;
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                switch (mTouchMode) {
                    case TOUCH_MODE_IDLE:
                        // Didn't target the thumb, treat normally.
                        break;

                    case TOUCH_MODE_DOWN: {
                        final float x = ev.getX();
                        final float y = ev.getY();
                        if (Math.abs(x - mTouchX) > mTouchSlop ||
                                Math.abs(y - mTouchY) > mTouchSlop) {
                            mTouchMode = TOUCH_MODE_DRAGGING;
                            getParent().requestDisallowInterceptTouchEvent(true);
                            mTouchX = x;
                            mTouchY = y;
                            return true;
                        }
                        break;
                    }

                    case TOUCH_MODE_DRAGGING: {
                        final float x = ev.getX();
                        final int thumbScrollRange = getThumbScrollRange();
                        final float thumbScrollOffset = x - mTouchX;
                        float dPos;
                        if (thumbScrollRange != 0) {
                            dPos = thumbScrollOffset / thumbScrollRange;
                        } else {
                            // If the thumb scroll range is empty, just use the
                            // movement direction to snap on or off.
                            dPos = thumbScrollOffset > 0 ? 1 : -1;
                        }
                        if (isLayoutRtl()) {
                            dPos = -dPos;
                        }
                        final float newPos = MathUtils.constrain(mThumbPosition + dPos, 0, 1);
                        if (newPos != mThumbPosition) {
                            mTouchX = x;
                            setThumbPosition(newPos);
                        }
                        return true;
                    }
                }
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mTouchMode == TOUCH_MODE_DRAGGING) {
                    stopDrag(ev);
                    // Allow super class to handle pressed state, etc.
                    super.onTouchEvent(ev);
                    return true;
                }
                mTouchMode = TOUCH_MODE_IDLE;
                mVelocityTracker.clear();
                break;
            }
        }

        return super.onTouchEvent(ev);
    }

    private void cancelSuperTouch(MotionEvent ev) {
        MotionEvent cancel = MotionEvent.obtain(ev);
        cancel.setAction(MotionEvent.ACTION_CANCEL);
        super.onTouchEvent(cancel);
        cancel.recycle();
    }

    /**
     * Called from onTouchEvent to end a drag operation.
     *
     * @param ev Event that triggered the end of drag mode - ACTION_UP or ACTION_CANCEL
     */
    private void stopDrag(MotionEvent ev) {
        mTouchMode = TOUCH_MODE_IDLE;

        // Commit the change if the event is up and not canceled and the switch
        // has not been disabled during the drag.
        final boolean commitChange = ev.getAction() == MotionEvent.ACTION_UP && isEnabled();
        final boolean oldState = isChecked();
        final boolean newState;
        if (commitChange) {
            mVelocityTracker.computeCurrentVelocity(1000);
            final float xvel = mVelocityTracker.getXVelocity();
            if (Math.abs(xvel) > mMinFlingVelocity) {
                newState = isLayoutRtl() ? (xvel < 0) : (xvel > 0);
            } else {
                newState = getTargetCheckedState();
            }
        } else {
            newState = oldState;
        }

        if (newState != oldState) {
            playSoundEffect(SoundEffectConstants.CLICK);
        }
        // Always call setChecked so that the thumb is moved back to the correct edge
        setChecked(newState);
        cancelSuperTouch(ev);
    }

    /* Begin: Ossdk for Switch Animation @{
    private void animateThumbToCheckedState(boolean newCheckedState) {
        final float targetPosition = newCheckedState ? 1 : 0;
        mPositionAnimator = ObjectAnimator.ofFloat(this, THUMB_POS, targetPosition);
        mPositionAnimator.setDuration(THUMB_ANIMATION_DURATION);
        mPositionAnimator.setAutoCancel(true);
        mPositionAnimator.start();
    }
    End : Ossdk for Switch Animation @}
    */

    //Begin: Ossdk for Switch Animation @{
    private void animateThumbToCheckedState(boolean newCheckedState) {
        if(mAnimHelper != null){
            mAnimHelper.resetAllAnim();
        } else{
            mAnimHelper = new AnimHelper();
        }
        mAnimHelper.start(mThumbPosition, newCheckedState, this);
    }
    //end : Ossdk for Switch Animation @}

    private void cancelPositionAnimator() {
        if(mAnimHelper != null){
            mAnimHelper.cancel();
        }
    }

    //Begin: Ossdk for Switch Animation @{
    private boolean isPositionAnimatorRunning(){
        return mAnimHelper != null && mAnimHelper.isInAnim();
    }
    //end : Ossdk for Switch Animation @}

    private boolean getTargetCheckedState() {
        return mThumbPosition > 0.5f;
    }

    /**
     * Sets the thumb position as a decimal value between 0 (off) and 1 (on).
     *
     * @param position new position between [0,1]
     */
    private void setThumbPosition(float position) {
        mThumbPosition = position;
        invalidate();
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }

    @Override
    public void setChecked(boolean checked) {
        setChecked(checked, true);
    }

    public void setChecked(boolean checked, boolean useAnim) {
        //Begin: Ossdk for Switch Animation @{
        if (isChecked() == checked) {
            if (!isPositionAnimatorRunning()) {
                setThumbPosition(checked ? 1 : 0);
            }
            return;
        }
        //End: Ossdk for Switch Animation @}

        super.setChecked(checked);

        // Calling the super method may result in setChecked() getting called
        // recursively with a different value, so load the REAL value...
        checked = isChecked();

        if (useAnim && Build.VERSION.SDK_INT >= 19 && isAttachedToWindow() && isLaidOut()) {
            animateThumbToCheckedState(checked);
        } else {
            // Immediately move the thumb to the new position.
            cancelPositionAnimator();
            setThumbPosition(checked ? 1 : 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int opticalInsetLeft = 0;
        int opticalInsetRight = 0;
        if (mThumbDrawable != null) {
            final Rect trackPadding = mTempRect;
            if (mTrackDrawable != null) {
                mTrackDrawable.getPadding(trackPadding);
            } else {
                trackPadding.setEmpty();
            }

            final Insets insets = Insets.NONE; //mThumbDrawable.getOpticalInsets(); //Ossdk for Switch Animation
            opticalInsetLeft = Math.max(0, insets.left - trackPadding.left);
            opticalInsetRight = Math.max(0, insets.right - trackPadding.right);
        }

        final int switchRight;
        final int switchLeft;
        if (isLayoutRtl()) {
            switchLeft = getPaddingLeft() + opticalInsetLeft;
            switchRight = switchLeft + mSwitchWidth - opticalInsetLeft - opticalInsetRight;
        } else {
            switchRight = getWidth() - getPaddingRight() - opticalInsetRight;
            switchLeft = switchRight - mSwitchWidth + opticalInsetLeft + opticalInsetRight;
        }

        final int switchTop;
        final int switchBottom;
        switch (getGravity() & Gravity.VERTICAL_GRAVITY_MASK) {
            default:
            case Gravity.TOP:
                switchTop = getPaddingTop();
                switchBottom = switchTop + mSwitchHeight;
                break;

            case Gravity.CENTER_VERTICAL:
                switchTop = (getPaddingTop() + getHeight() - getPaddingBottom()) / 2 -
                        mSwitchHeight / 2;
                switchBottom = switchTop + mSwitchHeight;
                break;

            case Gravity.BOTTOM:
                switchBottom = getHeight() - getPaddingBottom();
                switchTop = switchBottom - mSwitchHeight;
                break;
        }

        mSwitchLeft = switchLeft;
        mSwitchTop = switchTop;
        mSwitchBottom = switchBottom;
        mSwitchRight = switchRight;
    }

    @Override
    public void draw(Canvas c) {
        final Rect padding = mTempRect;
        final int switchLeft = mSwitchLeft;
        final int switchTop = mSwitchTop;
        final int switchRight = mSwitchRight;
        final int switchBottom = mSwitchBottom;

        int thumbInitialLeft = switchLeft + getThumbOffset();
        final Insets thumbInsets;
        if (mThumbDrawable != null) {
            thumbInsets = Insets.NONE; //mThumbDrawable.getOpticalInsets(); //Ossdk for Switch Animation
        } else {
            thumbInsets = Insets.NONE;
        }
        boolean isInAnim = isPositionAnimatorRunning();
        // Layout the track.
        if (mTrackDrawable != null) {
            mTrackDrawable.getPadding(padding);

            // Adjust thumb position for track padding.
            thumbInitialLeft += padding.left;

            // If necessary, offset by the optical insets of the thumb asset.
            int trackLeft = switchLeft;
            int trackTop = switchTop;
            int trackRight = switchRight;
            int trackBottom = switchBottom;
            if (thumbInsets != Insets.NONE) {
                if (thumbInsets.left > padding.left) {
                    trackLeft += thumbInsets.left - padding.left;
                }
                if (thumbInsets.top > padding.top) {
                    trackTop += thumbInsets.top - padding.top;
                }
                if (thumbInsets.right > padding.right) {
                    trackRight -= thumbInsets.right - padding.right;
                }
                if (thumbInsets.bottom > padding.bottom) {
                    trackBottom -= thumbInsets.bottom - padding.bottom;
                }
            }
            mTrackDrawable.setBounds(trackLeft, trackTop, trackRight, trackBottom);
            //Begin: Ossdk for Switch Animation @{
            if(isInAnim){
                if(isChecked()) {
                    mTrackOffDrawable.setBounds(trackLeft, trackTop, trackRight, trackBottom);
                    mTrackDrawable.setAlpha(getTrackOnAlpha());
                } else {
                    mTrackOnDrawable.setBounds(trackLeft, trackTop, trackRight, trackBottom);
                    mTrackOnDrawable.setAlpha(getTrackOnAlpha());
                }
            } else {
                mTrackDrawable.setAlpha(255);
            }
            //End: Ossdk for Switch Animation @}
        }

        // Layout the thumb.
        if (mThumbDrawable != null) {
            mThumbDrawable.getPadding(padding);

            final int thumbLeft = thumbInitialLeft - padding.left;
            final int thumbRight = thumbInitialLeft + mThumbWidth + padding.right;
            mThumbDrawable.setBounds(thumbLeft, switchTop, thumbRight, switchBottom);
            //Begin: Ossdk for Switch Animation @{
            if(isInAnim){
                if(isChecked()){
                    mThumbOffDrawable.setAlpha(getGrayThumbAlpha());
                    mThumbOffDrawable.setBounds(thumbLeft, switchTop, thumbRight, switchBottom);
                    mThumbOffScaleCenterX = thumbRight - DRAWABLE_PADDING;
                    mThumbDrawable.setAlpha(getRedThumbAlpha());
                } else{
                    mThumbOnDrawable.setAlpha(getRedThumbAlpha());
                    mThumbOnDrawable.setBounds(thumbLeft, switchTop, thumbRight, switchBottom);
                    mThumbOnScaleCenterX = thumbLeft + DRAWABLE_PADDING;
                }
                mThumbScaleCenterY = (float) ((switchBottom - switchTop) / 2.0);
            } else{
                mThumbDrawable.setAlpha(255);
            }
            //End: Ossdk for Switch Animation @}
            final Drawable background = getBackground();
            if (background != null) {
                background.setHotspotBounds(thumbLeft, switchTop, thumbRight, switchBottom);
            }
        }

        // Draw the background.
        super.draw(c);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Begin: Ossdk for Switch Animation @{
        boolean isInAnim = isPositionAnimatorRunning();
        if(isInAnim){
            canvas.drawPath(mBgPath, mBgPaint);
            if(!isChecked()){
                final Drawable trackOnDrawable = mTrackOnDrawable;
                if(trackOnDrawable != null){
                    trackOnDrawable.draw(canvas);
                }
            }
        }
        //End: Ossdk for Switch Animation @}

        final Rect padding = mTempRect;
        final Drawable trackDrawable = mTrackDrawable;
        if (trackDrawable != null) {
            trackDrawable.getPadding(padding);
        } else {
            padding.setEmpty();
        }

        final int switchTop = mSwitchTop;
        final int switchBottom = mSwitchBottom;
        final int switchInnerTop = switchTop + padding.top;
        final int switchInnerBottom = switchBottom - padding.bottom;

        final Drawable thumbDrawable = mThumbDrawable;
        if (trackDrawable != null) {
            if (mSplitTrack && thumbDrawable != null) {
                final Insets insets = Insets.NONE;  //thumbDrawable.getOpticalInsets(); //Ossdk for Switch Animation
                thumbDrawable.copyBounds(padding);
                padding.left += insets.left;
                padding.right -= insets.right;

                final int saveCount = canvas.save();
                canvas.clipRect(padding, Op.DIFFERENCE);
                trackDrawable.draw(canvas);
                canvas.restoreToCount(saveCount);
            } else {
                if(isInAnim && !isChecked()){
                    final int saveCount = canvas.save();
                    Rect rect = new Rect(canvas.getClipBounds());
                    rect.left = getThumbOffset() + mThumbWidth / 2;
                    canvas.clipRect(rect);
                    trackDrawable.draw(canvas);
                    canvas.restoreToCount(saveCount);
                } else{
                    trackDrawable.draw(canvas);
                }
            }
        }

        //Begin: Ossdk for Switch Animation @{
        //open: Third draw
        if(isInAnim && isChecked()){
            final int saveCount0 = canvas.save();
            canvas.scale(getScaleBg(), getScaleBg(), mTrackOffScaleCenterX, mTrackScaleCenterY);
            final Drawable trackOffDrawable = mTrackOffDrawable;
            if(trackOffDrawable != null){
                trackOffDrawable.draw(canvas);
            }
            canvas.restoreToCount(saveCount0);
        }
        //End: Ossdk for Switch Animation @}

        if (thumbDrawable != null) {
            if(isInAnim){
                if(isChecked()){ //Begin: Ossdk for Switch Animation @{
                    final int saveCount = canvas.save();
                    canvas.scale(getThumbOffScale(), 1, mThumbOffScaleCenterX, mThumbScaleCenterY);
                    thumbDrawable.draw(canvas);

                    final Drawable thumbOffDrawable = mThumbOffDrawable;
                    thumbOffDrawable.draw(canvas);
                    canvas.restoreToCount(saveCount);
                } else{
                    final int saveCount = canvas.save();
                    canvas.scale(getThumbOffScale(), 1, mThumbOnScaleCenterX, mThumbScaleCenterY);
                    final Drawable thumbOffDrawable = mThumbOffDrawable;
                    thumbOffDrawable.draw(canvas);
                    thumbDrawable.draw(canvas);
                    canvas.restoreToCount(saveCount);
                }//End: Ossdk for Switch Animation @}
            } else {
                final int saveCount = canvas.save();
                thumbDrawable.draw(canvas);
                canvas.restoreToCount(saveCount);
            }
        }

        final int saveCount = canvas.save();
        final Layout switchText = getTargetCheckedState() ? mOnLayout : mOffLayout;
        if (switchText != null) {
            final int drawableState[] = getDrawableState();
            if (mTextColors != null) {
                mTextPaint.setColor(mTextColors.getColorForState(drawableState, 0));
            }
            mTextPaint.drawableState = drawableState;

            final int cX;
            if (thumbDrawable != null) {
                final Rect bounds = thumbDrawable.getBounds();
                cX = bounds.left + bounds.right;
            } else {
                cX = getWidth();
            }

            final int left = cX / 2 - switchText.getWidth() / 2;
            final int top = (switchInnerTop + switchInnerBottom) / 2 - switchText.getHeight() / 2;
            canvas.translate(left, top);
            switchText.draw(canvas);
        }

        canvas.restoreToCount(saveCount);
    }

    @Override
    public int getCompoundPaddingLeft() {
        if (!isLayoutRtl()) {
            return super.getCompoundPaddingLeft();
        }
        int padding = super.getCompoundPaddingLeft() + mSwitchWidth;
        if (!TextUtils.isEmpty(getText())) {
            padding += mSwitchPadding;
        }
        return padding;
    }

    @Override
    public int getCompoundPaddingRight() {
        if (isLayoutRtl()) {
            return super.getCompoundPaddingRight();
        }
        int padding = super.getCompoundPaddingRight() + mSwitchWidth;
        if (!TextUtils.isEmpty(getText())) {
            padding += mSwitchPadding;
        }
        return padding;
    }

    /**
     * @return
     */
    //Begin: Ossdk for Switch Animation @{
    public boolean isLayoutRtl() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return getLayoutDirection() == LAYOUT_DIRECTION_RTL;
        }
        return false;
    }
    //End: Ossdk for Switch Animation @}

    /**
     * Translates thumb position to offset according to current RTL setting and
     * thumb scroll range. Accounts for both track and thumb padding.
     *
     * @return thumb offset
     */
    private int getThumbOffset() {
        /* Begin: Ossdk for Switch Animation @{
        //old code
        final float thumbPosition;
        if (isLayoutRtl()) {
            thumbPosition = 1 - mThumbPosition;
        } else {
            thumbPosition = mThumbPosition;
        }
        return (int) (thumbPosition * getThumbScrollRange() + 0.5f);
        End: Ossdk for Switch Animation @}
        */
        //Begin: Ossdk for Switch Animation @{
        //new code
        if (!isThumbOn()){
            return (int) (getThumbValue() * getThumbScrollRange() + 0.5f);
        }
        else{
            return getThumbScrollRange();
        }
        //End: Ossdk for Switch Animation @}
    }

    //Begin: Ossdk for Switch Animation @{
    private float getThumbValue() {
        return mThumbPosition;
    }
    //End: Ossdk for Switch Animation @}

    //Begin: Ossdk for Switch Animation @{
    private boolean isThumbOn() {
        return mThumbPosition == THUMB_STATUS_CHANGE;
    }
    //End: Ossdk for Switch Animation @}

    private int getThumbScrollRange() {
        if (mTrackDrawable != null) {
            final Rect padding = mTempRect;
            mTrackDrawable.getPadding(padding);

            final Insets insets;
            if (mThumbDrawable != null) {
                insets = Insets.NONE; //mThumbDrawable.getOpticalInsets(); //Ossdk for Switch Animation
            } else {
                insets = Insets.NONE;
            }

            return mSwitchWidth - mThumbWidth - padding.left - padding.right
                    - insets.left - insets.right;
        } else {
            return 0;
        }
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final int[] state = getDrawableState();
        boolean changed = false;

        final Drawable thumbDrawable = mThumbDrawable;
        if (thumbDrawable != null && thumbDrawable.isStateful()) {
            changed |= thumbDrawable.setState(state);
        }

        final Drawable trackDrawable = mTrackDrawable;
        if (trackDrawable != null && trackDrawable.isStateful()) {
            changed |= trackDrawable.setState(state);
        }

        if (changed) {
            invalidate();
        }
    }

    @Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);

        if (mThumbDrawable != null) {
            mThumbDrawable.setHotspot(x, y);
        }

        if (mTrackDrawable != null) {
            mTrackDrawable.setHotspot(x, y);
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == mThumbDrawable || who == mTrackDrawable;
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (mThumbDrawable != null) {
            mThumbDrawable.jumpToCurrentState();
        }

        if (mTrackDrawable != null) {
            mTrackDrawable.jumpToCurrentState();
        }
        if(mAnimHelper != null){
            mAnimHelper.end();
            mAnimHelper = null;
        }
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return Switch.class.getName();
    }

    /* Begin: Ossdk for Switch Animation @{
    @Override
    public void onProvideStructure(ViewStructure structure) {
        super.onProvideStructure(structure);
        CharSequence switchText = isChecked() ? mTextOn : mTextOff;
        if (!TextUtils.isEmpty(switchText)) {
            CharSequence oldText = structure.getText();
            if (TextUtils.isEmpty(oldText)) {
                structure.setText(switchText);
            } else {
                StringBuilder newText = new StringBuilder();
                newText.append(oldText).append(' ').append(switchText);
                structure.setText(newText);
            }
            // The style of the label text is provided via the base TextView class. This is more
            // relevant than the style of the (optional) on/off text on the switch button itself,
            // so ignore the size/color/style stored this.mTextPaint.
        }
    }
    End: Ossdk for Switch Animation @}
    */

    /** @hide */
    /* Begin: Ossdk for Switch Animation @{
    @Override
    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        CharSequence switchText = isChecked() ? mTextOn : mTextOff;
        if (!TextUtils.isEmpty(switchText)) {
            CharSequence oldText = info.getText();
            if (TextUtils.isEmpty(oldText)) {
                info.setText(switchText);
            } else {
                StringBuilder newText = new StringBuilder();
                newText.append(oldText).append(' ').append(switchText);
                info.setText(newText);
            }
        }
    }
    End: Ossdk for Switch Animation @}
    */

    /* Begin: Ossdk for Switch Animation @{
    private static final FloatProperty<Switch> THUMB_POS = new FloatProperty<Switch>("thumbPos") {
        @Override
        public Float get(Switch object) {
            return object.mThumbPosition;
        }

        @Override
        public void setValue(Switch object, float value) {
            object.setThumbPosition(value);
        }
    };
    End: Ossdk for Switch Animation @}
    */

    //Begin: Ossdk for Switch Animation @{
    private static final int PADDING = 6;
    private static final int DRAWABLE_PADDING = 8;
    private void initBgDrawParams(){
        mBgPaint = new Paint();
        mBgPaint.setColor(this.getContext().getResources().getColor(R.color.talpaossdk_switch_bg_color));
        mBgPaint.setAntiAlias(true);

        mBgPath = new Path();
        int width = this.getContext().getResources().getDimensionPixelSize(R.dimen.talpaossdk_switch_bg_width);
        int height = this.getContext().getResources().getDimensionPixelSize(R.dimen.talpaossdk_switch_bg_height);
        RectF sRectF = new RectF(DRAWABLE_PADDING + PADDING, DRAWABLE_PADDING,
                height + DRAWABLE_PADDING + PADDING, height + DRAWABLE_PADDING); //8 is the track (drawable's blank padding / 2)
        mBgPath.arcTo(sRectF, 90, 180);
        sRectF.left = width - height + DRAWABLE_PADDING + PADDING;
        sRectF.right = width + DRAWABLE_PADDING + PADDING;
        mBgPath.arcTo(sRectF, 270, 180);
        mBgPath.close();
    }

    @Override
    public void updteThumbPosition(float thumbPosition) {
        setThumbPosition(thumbPosition);
    }

    private int getGrayThumbAlpha(){
        return mAnimHelper == null ? 1 : mAnimHelper.getGrayThumbAlpha();
    }

    private int getRedThumbAlpha(){
        return mAnimHelper == null ? 1 : mAnimHelper.getRedThumbAlpha();
    }

    private int getTrackOnAlpha(){
        return mAnimHelper == null ? 1 : mAnimHelper.getTrackOnAlpha();
    }

    private float getThumbOffScale(){
        return mAnimHelper == null ? 1 : mAnimHelper.getThumbOffScale();
    }

    private float getScaleBg(){
        return mAnimHelper == null ? 1: mAnimHelper.getScaleBg();
    }

    //End: Ossdk for Switch Animation @}
}
