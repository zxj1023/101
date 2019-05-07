/*
 * Copyright (C) 2007 The Android Open Source Project
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

import android.annotation.Widget;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.SpinnerAdapter;

import tran.com.android.gc.lib.utils.DensityUtil;

import tran.com.android.gc.lib.app.AuroraAlertDialog;
import tran.com.android.gc.lib.app.AuroraDialog;
import tran.com.android.gc.lib.R;

/**
 * A view that displays one child at a time and lets the user pick among them.
 * The items in the Spinner come from the {@link Adapter} associated with this
 * view.
 * <p>
 * See the <a href="{@docRoot}
 * guide/topics/ui/controls/spinner.html">Spinners</a> guide.
 * </p>
 * 
 * @attr ref android.R.styleable#Spinner_dropDownHorizontalOffset
 * @attr ref android.R.styleable#Spinner_dropDownSelector
 * @attr ref android.R.styleable#Spinner_dropDownVerticalOffset
 * @attr ref android.R.styleable#Spinner_dropDownWidth
 * @attr ref android.R.styleable#Spinner_gravity
 * @attr ref android.R.styleable#Spinner_popupBackground
 * @attr ref android.R.styleable#Spinner_prompt
 * @attr ref android.R.styleable#Spinner_spinnerMode
 */
@Widget
public class AuroraSpinner extends AuroraAbsSpinner implements OnClickListener {
    private static final String TAG = "AuroraSpinner";

    private static final int MSG_ARROW_DOWN = 100;
    private static final int MSG_ARROW_UP = 101;
    
    public static final int POSITION_LEFT_TOP = 0;
    public static final int POSITION_LEFT_NORMAL=1;
    public static final int POSITION_LEFT_BOTTOM = 2;
    
    public static final int POSITION_RIGHT_TOP = 3;
    public static final int POSITION_RIGHT_NORMAL=4;
    public static final int POSITION_RIGHT_BOTTOM = 5;
    
    public static final int POSITION_NORMAL = 6;
    
    // Only measure this many items to get a decent max width.
    private static final int MAX_ITEMS_MEASURED = 15;

    /**
     * Use a dialog window for selecting spinner options.
     */
    public static final int MODE_DIALOG = 0;

    /**
     * Use a dropdown anchored to the Spinner for selecting spinner options.
     */
    public static final int MODE_DROPDOWN = 1;

    /**
     * Use the theme-supplied value to select the dropdown mode.
     */
    private static final int MODE_THEME = -1;

    private SpinnerPopup mPopup;
    private DropDownAdapter mTempAdapter;
    int mDropDownWidth;

    private int mGravity;
    private boolean mDisableChildrenWhenDisabled;

    private Rect mTempRect = new Rect();
    
    
    private boolean mPopupWindowDismiss = true;
    private boolean mChangeArrow = true;
    
    
    private Handler mChangeArrowHandler;

    
    private ItemClickListener mItemClickListener;
    
    private InputMethodManager imm;
    
    private int position = POSITION_NORMAL;
    
    private int mRight = 0;
    
    
    private int mBackgroundRes;
    
    public interface ItemClickListener{
        
        void itemClick(AdapterView<?> parent,View child,int position,long id);
        
    }
    /**
     * Construct a new spinner with the given context's theme.
     * 
     * @param context The Context the view is running in, through which it can
     *            access the current theme, resources, etc.
     */
    public AuroraSpinner(Context context) {
        this(context, null);
    }

    /**
     * Construct a new spinner with the given context's theme and the supplied
     * mode of displaying choices. <code>mode</code> may be one of
     * {@link #MODE_DIALOG} or {@link #MODE_DROPDOWN}.
     * 
     * @param context The Context the view is running in, through which it can
     *            access the current theme, resources, etc.
     * @param mode Constant describing how the user will select choices from the
     *            spinner.
     * @see #MODE_DIALOG
     * @see #MODE_DROPDOWN
     */
    public AuroraSpinner(Context context, int mode) {
        this(context, null, com.android.internal.R.attr.spinnerStyle, mode);
    }

    /**
     * Construct a new spinner with the given context's theme and the supplied
     * attribute set.
     * 
     * @param context The Context the view is running in, through which it can
     *            access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     */
    public AuroraSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.spinnerStyle);
    }

    /**
     * Construct a new spinner with the given context's theme, the supplied
     * attribute set, and default style.
     * 
     * @param context The Context the view is running in, through which it can
     *            access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyle The default style to apply to this view. If 0, no style
     *            will be applied (beyond what is included in the theme). This
     *            may either be an attribute resource, whose value will be
     *            retrieved from the current theme, or an explicit style
     *            resource.
     */
    public AuroraSpinner(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, MODE_THEME);
    }

    /**
     * Construct a new spinner with the given context's theme, the supplied
     * attribute set, and default style. <code>mode</code> may be one of
     * {@link #MODE_DIALOG} or {@link #MODE_DROPDOWN} and determines how the
     * user will select choices from the spinner.
     * 
     * @param context The Context the view is running in, through which it can
     *            access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyle The default style to apply to this view. If 0, no style
     *            will be applied (beyond what is included in the theme). This
     *            may either be an attribute resource, whose value will be
     *            retrieved from the current theme, or an explicit style
     *            resource.
     * @param mode Constant describing how the user will select choices from the
     *            spinner.
     * @see #MODE_DIALOG
     * @see #MODE_DROPDOWN
     */
    public AuroraSpinner(Context context, AttributeSet attrs, int defStyle, int mode) {
        super(context, attrs, defStyle);
        mChangeArrowHandler = new ChangeArrowHandler();
        TypedArray a = context.obtainStyledAttributes(attrs,
                com.android.internal.R.styleable.Spinner, defStyle, 0);

        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        TypedArray auroraA = context.obtainStyledAttributes(attrs,
                R.styleable.AuroraSpinner, defStyle, 0);
        
        if (mode == MODE_THEME) {
            mode = auroraA.getInt(R.styleable.AuroraSpinner_auroraspinnerMode,
                    MODE_DROPDOWN);
        }
        position = auroraA.getInteger(R.styleable.AuroraSpinner_spinnerPosition, POSITION_NORMAL);
        auroraA.recycle();
//        switch (mode) {
//            case MODE_DIALOG: //{
////                mPopup = new DialogPopup();AuroraDialogPopup
                mPopup = new AuroraDialogPopup(context);
//               // break;
//            //}
//
//            case MODE_DROPDOWN: {
//                DropdownPopup popup = new DropdownPopup(context, attrs, defStyle);
//
//                mDropDownWidth = a.getLayoutDimension(
//                        com.android.internal.R.styleable.Spinner_dropDownWidth,
//                        ViewGroup.LayoutParams.WRAP_CONTENT);
//                popup.setBackgroundDrawable(a.getDrawable(
//                        com.android.internal.R.styleable.Spinner_popupBackground));
//                final int verticalOffset = a.getDimensionPixelOffset(
//                        com.android.internal.R.styleable.Spinner_dropDownVerticalOffset, 0);
//                if (verticalOffset != 0) {
//                    popup.setVerticalOffset(verticalOffset);
//                }
//
//                final int horizontalOffset = a.getDimensionPixelOffset(
//                        com.android.internal.R.styleable.Spinner_dropDownHorizontalOffset, 0);
//                if (horizontalOffset != 0) {
//                    popup.setHorizontalOffset(horizontalOffset);
//                }
//
//                mPopup = popup;
//                break;
//            }
//        }

        mGravity = a.getInt(com.android.internal.R.styleable.Spinner_gravity, Gravity.CENTER);

        mPopup.setPromptText(a.getString(com.android.internal.R.styleable.Spinner_prompt));

        mDisableChildrenWhenDisabled = a.getBoolean(
                com.android.internal.R.styleable.Spinner_disableChildrenWhenDisabled, false);

        a.recycle();
        
        a = context.obtainStyledAttributes(attrs,
                R.styleable.AuroraSpinner, defStyle, 0);
//        mChangeArrow = true;//a.getBoolean(R.styleable.AuroraSpinner_changeArrowDirection, false);
        a.recycle();
        // Base constructor can call setAdapter before we initialize mPopup.
        // Finish setting things up if this happened.
        if (mTempAdapter != null) {
            mPopup.setAdapter(mTempAdapter);
            mTempAdapter = null;
        }
       // setBackgroundResource(R.drawable.aurora_spinner_background_normal);
        //IsShowThread.start();
        setBackgroundInternal(position);
        mRight = mPaddingRight;
    }

    private void setBackgroundInternal(int position){
        switch (position) {
            case POSITION_LEFT_BOTTOM:
                setBackground(R.drawable.aurora_spinner_left_backgound_bottom);
                break;
            case POSITION_LEFT_NORMAL:
                setBackground(R.drawable.aurora_spinner_left_backgound_normal);
                break;
            case POSITION_LEFT_TOP:
                setBackground(R.drawable.aurora_spinner_left_backgound_top);
                break;
            case POSITION_NORMAL:
                setBackground(R.drawable.aurora_spinner_middle_backgound);
                break;
            case POSITION_RIGHT_BOTTOM:
                setBackground(R.drawable.aurora_spinner_right_backgound_bottom);
                break;
            case POSITION_RIGHT_NORMAL:
                setBackground(R.drawable.aurora_spinner_right_backgound_normal);
                break;
            case POSITION_RIGHT_TOP:
                setBackground(R.drawable.aurora_spinner_right_backgound_top);
                break;

            default:
                setBackground(R.drawable.aurora_spinner_middle_backgound);
                break;
        }
    }
    
    public void changeDirection(boolean changed){
        mChangeArrow = changed;
    }
    public boolean getDirection(){
        
        return mChangeArrow;
    }
    
    public void setPosition(int position){
        setBackgroundInternal(position);
        postInvalidate();
    }
    public int getPosition(){
        return position;
    }
    /**
     * Set the background drawable for the spinner's popup window of choices.
     * Only valid in {@link #MODE_DROPDOWN}; this method is a no-op in other
     * modes.
     * 
     * @param background Background drawable
     * @attr ref android.R.styleable#Spinner_popupBackground
     */
    public void setPopupBackgroundDrawable(Drawable background) {
        if (!(mPopup instanceof DropdownPopup)) {
            Log.e(TAG, "setPopupBackgroundDrawable: incompatible spinner mode; ignoring...");
            return;
        }
        ((DropdownPopup) mPopup).setBackgroundDrawable(background);
    }

    /**
     * Set the background drawable for the spinner's popup window of choices.
     * Only valid in {@link #MODE_DROPDOWN}; this method is a no-op in other
     * modes.
     * 
     * @param resId Resource ID of a background drawable
     * @attr ref android.R.styleable#Spinner_popupBackground
     */
    public void setPopupBackgroundResource(int resId) {
        setPopupBackgroundDrawable(getContext().getResources().getDrawable(resId));
    }

    /**
     * Get the background drawable for the spinner's popup window of choices.
     * Only valid in {@link #MODE_DROPDOWN}; other modes will return null.
     * 
     * @return background Background drawable
     * @attr ref android.R.styleable#Spinner_popupBackground
     */
    public Drawable getPopupBackground() {
        return mPopup.getBackground();
    }

    /**
     * Set a vertical offset in pixels for the spinner's popup window of
     * choices. Only valid in {@link #MODE_DROPDOWN}; this method is a no-op in
     * other modes.
     * 
     * @param pixels Vertical offset in pixels
     * @attr ref android.R.styleable#Spinner_dropDownVerticalOffset
     */
    public void setDropDownVerticalOffset(int pixels) {
        mPopup.setVerticalOffset(pixels);
    }

    /**
     * Get the configured vertical offset in pixels for the spinner's popup
     * window of choices. Only valid in {@link #MODE_DROPDOWN}; other modes will
     * return 0.
     * 
     * @return Vertical offset in pixels
     * @attr ref android.R.styleable#Spinner_dropDownVerticalOffset
     */
    public int getDropDownVerticalOffset() {
        return mPopup.getVerticalOffset();
    }

    /**
     * Set a horizontal offset in pixels for the spinner's popup window of
     * choices. Only valid in {@link #MODE_DROPDOWN}; this method is a no-op in
     * other modes.
     * 
     * @param pixels Horizontal offset in pixels
     * @attr ref android.R.styleable#Spinner_dropDownHorizontalOffset
     */
    public void setDropDownHorizontalOffset(int pixels) {
        mPopup.setHorizontalOffset(pixels);
    }

    /**
     * Get the configured horizontal offset in pixels for the spinner's popup
     * window of choices. Only valid in {@link #MODE_DROPDOWN}; other modes will
     * return 0.
     * 
     * @return Horizontal offset in pixels
     * @attr ref android.R.styleable#Spinner_dropDownHorizontalOffset
     */
    public int getDropDownHorizontalOffset() {
        return mPopup.getHorizontalOffset();
    }

    /**
     * Set the width of the spinner's popup window of choices in pixels. This
     * value may also be set to
     * {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT} to match the
     * width of the Spinner itself, or
     * {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT} to wrap to the
     * measured size of contained dropdown list items.
     * <p>
     * Only valid in {@link #MODE_DROPDOWN}; this method is a no-op in other
     * modes.
     * </p>
     * 
     * @param pixels Width in pixels, WRAP_CONTENT, or MATCH_PARENT
     * @attr ref android.R.styleable#Spinner_dropDownWidth
     */
    public void setDropDownWidth(int pixels) {
        if (!(mPopup instanceof DropdownPopup)) {
            Log.e(TAG, "Cannot set dropdown width for MODE_DIALOG, ignoring");
            return;
        }
        mDropDownWidth = pixels;
    }

    /**
     * Get the configured width of the spinner's popup window of choices in
     * pixels. The returned value may also be
     * {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT} meaning the
     * popup window will match the width of the Spinner itself, or
     * {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT} to wrap to the
     * measured size of contained dropdown list items.
     * 
     * @return Width in pixels, WRAP_CONTENT, or MATCH_PARENT
     * @attr ref android.R.styleable#Spinner_dropDownWidth
     */
    public int getDropDownWidth() {
        return mDropDownWidth;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (mDisableChildrenWhenDisabled) {
            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                getChildAt(i).setEnabled(enabled);
            }
        }
    }

    /**
     * Describes how the selected item view is positioned. Currently only the
     * horizontal component is used. The default is determined by the current
     * theme.
     * 
     * @param gravity See {@link android.view.Gravity}
     * @attr ref android.R.styleable#Spinner_gravity
     */
    public void setGravity(int gravity) {
        if (mGravity != gravity) {
            if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == 0) {
                gravity |= Gravity.START;
            }
            mGravity = gravity;
            requestLayout();
        }
    }

    /**
     * Describes how the selected item view is positioned. The default is
     * determined by the current theme.
     * 
     * @return A {@link android.view.Gravity Gravity} value
     */
    public int getGravity() {
        return mGravity;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        super.setAdapter(adapter);

        if (mPopup != null) {
            mPopup.setAdapter(new DropDownAdapter(adapter));
        } else {
            mTempAdapter = new DropDownAdapter(adapter);
        }
    }
    
    public void setNotice(CharSequence notice){
        
    }
    public void setNotice(int noticeResId){
        
    }

    @Override
    public int getBaseline() {
        View child = null;

        if (getChildCount() > 0) {
            child = getChildAt(0);
        } else if (mAdapter != null && mAdapter.getCount() > 0) {
            child = makeAndAddView(0);
            mRecycler.put(0, child);
            removeAllViewsInLayout();
        }

        if (child != null) {
            final int childBaseline = child.getBaseline();
            return childBaseline >= 0 ? child.getTop() + childBaseline : -1;
        } else {
            return -1;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mPopup != null && mPopup.isDialogShowing()) {
            mPopup.dismiss();
        }
    }

    /**
     * <p>
     * A spinner does not support item click events. Calling this method will
     * raise an exception.
     * </p>
     * <p>
     * Instead use {@link AdapterView#setOnItemSelectedListener}.
     * 
     * @param l this listener will be ignored
     */
    public void setAuroraOnItemClickListener(ItemClickListener l) {
        mItemClickListener = l;
    }

    
    /**
     * @hide internal use only
     */
    public void setOnItemClickListenerInt(OnItemClickListener l) {
        super.setOnItemClickListener(l);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mPopup != null && MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            final int measuredWidth = getMeasuredWidth();
            setMeasuredDimension(Math.min(Math.max(measuredWidth,measureContentWidth(getAdapter(), getBackground())),
                    MeasureSpec.getSize(widthMeasureSpec)),
                    getMeasuredHeight());
            
//            Log.e("spinner", "Min:"+Math.min(Math.max(measuredWidth,measureContentWidth(getAdapter(), getBackground())),
//                    MeasureSpec.getSize(widthMeasureSpec)));
//            Log.e("spinner", "measuredWidth:"+measuredWidth);
//            Log.e("spinner", "measureContentWidth:"+measureContentWidth(getAdapter(), getBackground()));
//            Log.e("spinner", "MeasureSpec.Size:"+MeasureSpec.getSize(widthMeasureSpec));
        }
    }

    /**
     * @see android.view.View#onLayout(boolean,int,int,int,int) Creates and
     *      positions all views
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mInLayout = true;
        layout(0, false);
        mInLayout = false;
    }
    public void setBackground(int resID){
        this.mBackgroundRes = resID;
        setBackgroundResource(mBackgroundRes);
    }
    
    public void setOnItemClickListener(ItemClickListener listener){
        this.mItemClickListener = listener;
    }
    
    /**
     * Creates and positions all views for this Spinner.
     * 
     * @param delta Change in the selected position. +1 means selection is
     *            moving to the right, so views are scrolling to the left. -1
     *            means selection is moving to the left.
     */
    @Override
    void layout(int delta, boolean animate) {
        int childrenLeft = mSpinnerPadding.left;
        int childrenWidth = mRight - mLeft - mSpinnerPadding.left - mSpinnerPadding.right;

        
        if (mDataChanged) {
            handleDataChanged();
        }

        // Handle the empty set by removing all views
        if (mItemCount == 0) {
            resetList();
            return;
        }

        if (mNextSelectedPosition >= 0) {
            setSelectedPositionInt(mNextSelectedPosition);
        }

        recycleAllViews();

        // Clear out old views
        removeAllViewsInLayout();

        // Make selected view and position it
        mFirstPosition = mSelectedPosition;
        View sel = makeAndAddView(mSelectedPosition);
        int width = sel.getMeasuredWidth();
        int selectedOffset = childrenLeft;
        final int layoutDirection = getLayoutDirection();
        final int absoluteGravity = Gravity.getAbsoluteGravity(mGravity, layoutDirection);
        switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.CENTER_HORIZONTAL:
                selectedOffset = childrenLeft + (childrenWidth / 2) - (width / 2);
                break;
            case Gravity.RIGHT:
                selectedOffset = childrenLeft + childrenWidth - width;
                break;
        }
        sel.offsetLeftAndRight(selectedOffset);

        // Flush any cached views that did not get reused above
        mRecycler.clear();

        invalidate();

        checkSelectionChanged();

        mDataChanged = false;
        mNeedSync = false;
        setNextSelectedPositionInt(mSelectedPosition);
    }

    /**
     * Obtain a view, either by pulling an existing view from the recycler or by
     * getting a new one from the adapter. If we are animating, make sure there
     * is enough information in the view's layout parameters to animate from the
     * old to new positions.
     * 
     * @param position Position in the spinner for the view to obtain
     * @return A view that has been added to the spinner
     */
    private View makeAndAddView(int position) {

        View child;

        if (!mDataChanged) {
            child = mRecycler.get(position);
            if (child != null) {
                // Position the view
                setUpChild(child);

                return child;
            }
        }

        // Nothing found in the recycler -- ask the adapter for a view
        child = mAdapter.getView(position, null, this);

        // Position the view
        setUpChild(child);

        return child;
    }

    /**
     * Helper for makeAndAddView to set the position of a view and fill out its
     * layout paramters.
     * 
     * @param child The view to position
     */
    private void setUpChild(View child) {

        // Respect layout params that are already in the view. Otherwise
        // make some up...
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        if (lp == null) {
            lp = generateDefaultLayoutParams();
        }

        addViewInLayout(child, 0, lp);

        child.setSelected(hasFocus());
        if (mDisableChildrenWhenDisabled) {
            child.setEnabled(isEnabled());
        }

        // Get measure specs
        int childHeightSpec = ViewGroup.getChildMeasureSpec(mHeightMeasureSpec,
                mSpinnerPadding.top + mSpinnerPadding.bottom, lp.height);
        int childWidthSpec = ViewGroup.getChildMeasureSpec(mWidthMeasureSpec,
                mSpinnerPadding.left + mSpinnerPadding.right, lp.width);

        // Measure child
        child.measure(childWidthSpec, childHeightSpec);

        int childLeft;
        int childRight;

        // Position vertically based on gravity setting
        int childTop = mSpinnerPadding.top
                + ((getMeasuredHeight() - mSpinnerPadding.bottom -
                        mSpinnerPadding.top - child.getMeasuredHeight()) / 2);
        int childBottom = childTop + child.getMeasuredHeight();

        int width = child.getMeasuredWidth();
        childLeft = 0;
        childRight = childLeft + width;
        if(mDropDownWidth == WindowManager.LayoutParams.MATCH_PARENT){
            
        }
        child.layout(childLeft, childTop, childRight, childBottom);
    }

    @Override
    public boolean performClick() {
        if(imm != null){
            imm.hideSoftInputFromWindow(this.getWindowToken(),0);
        }
//        mSpinnerPadding.right = mRight;
        boolean handled = super.performClick();
       
        if (!handled) {
            handled = true;

            if (!mPopup.isDialogShowing()) {
                mPopup.show();
                
            }
        }

        return handled;
    }

    public void onClick(DialogInterface dialog, int which) {
        setSelection(which);
        dialog.dismiss();
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(AuroraSpinner.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(AuroraSpinner.class.getName());
    }

    /**
     * Sets the prompt to display when the dialog is shown.
     * 
     * @param prompt the prompt to set
     */
    public void setPrompt(CharSequence prompt) {
        mPopup.setPromptText(prompt);
    }

    /**
     * Sets the prompt to display when the dialog is shown.
     * 
     * @param promptId the resource ID of the prompt to display when the dialog
     *            is shown
     */
    public void setPromptId(int promptId) {
        setPrompt(getContext().getText(promptId));
    }

    /**
     * @return The prompt to display when the dialog is shown
     */
    public CharSequence getPrompt() {
        return mPopup.getHintText();
    }

    int measureContentWidth(SpinnerAdapter adapter, Drawable background) {
        if (adapter == null) {
            return 0;
        }

        int width = 0;
        View itemView = null;
        int itemType = 0;
        final int widthMeasureSpec =
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        final int heightMeasureSpec =
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        // Make sure the number of items we'll measure is capped. If it's a huge
        // data set
        // with wildly varying sizes, oh well.
        int start = Math.max(0, getSelectedItemPosition());
        final int end = Math.min(adapter.getCount(), start + MAX_ITEMS_MEASURED);
        final int count = end - start;
        start = Math.max(0, start - (MAX_ITEMS_MEASURED - count));
        for (int i = start; i < end; i++) {
            final int positionType = adapter.getItemViewType(i);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }
            itemView = adapter.getView(i, itemView, this);
            if (itemView.getLayoutParams() == null) {
                itemView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            itemView.measure(widthMeasureSpec, heightMeasureSpec);
            width = Math.max(width, itemView.getMeasuredWidth());
        }

        // Add background padding to measured width
        if (background != null) {
            background.getPadding(mTempRect);
            width += mTempRect.left + mTempRect.right;
        }

        return width;
    }

    /**
     * <p>
     * Wrapper class for an Adapter. Transforms the embedded Adapter instance
     * into a ListAdapter.
     * </p>
     */
    private static class DropDownAdapter implements ListAdapter, SpinnerAdapter {
        private SpinnerAdapter mAdapter;
        private ListAdapter mListAdapter;

        /**
         * <p>
         * Creates a new ListAdapter wrapper for the specified adapter.
         * </p>
         * 
         * @param adapter the Adapter to transform into a ListAdapter
         */
        public DropDownAdapter(SpinnerAdapter adapter) {
            this.mAdapter = adapter;
            if (adapter instanceof ListAdapter) {
                this.mListAdapter = (ListAdapter) adapter;
            }
        }

        public int getCount() {
            return mAdapter == null ? 0 : mAdapter.getCount();
        }

        public Object getItem(int position) {
            return mAdapter == null ? null : mAdapter.getItem(position);
        }

        public long getItemId(int position) {
            return mAdapter == null ? -1 : mAdapter.getItemId(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            return getDropDownView(position, convertView, parent);
        }

        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return mAdapter == null ? null :
                    mAdapter.getDropDownView(position, convertView, parent);
        }

        public boolean hasStableIds() {
            return mAdapter != null && mAdapter.hasStableIds();
        }

        public void registerDataSetObserver(DataSetObserver observer) {
            if (mAdapter != null) {
                mAdapter.registerDataSetObserver(observer);
            }
        }

        public void unregisterDataSetObserver(DataSetObserver observer) {
            if (mAdapter != null) {
                mAdapter.unregisterDataSetObserver(observer);
            }
        }

        /**
         * If the wrapped SpinnerAdapter is also a ListAdapter, delegate this
         * call. Otherwise, return true.
         */
        public boolean areAllItemsEnabled() {
            final ListAdapter adapter = mListAdapter;
            if (adapter != null) {
                return adapter.areAllItemsEnabled();
            } else {
                return true;
            }
        }

        /**
         * If the wrapped SpinnerAdapter is also a ListAdapter, delegate this
         * call. Otherwise, return true.
         */
        public boolean isEnabled(int position) {
            final ListAdapter adapter = mListAdapter;
            if (adapter != null) {
                return adapter.isEnabled(position);
            } else {
                return true;
            }
        }

        public int getItemViewType(int position) {
            return 0;
        }

        public int getViewTypeCount() {
            return 1;
        }

        public boolean isEmpty() {
            return getCount() == 0;
        }
    }

    /**
     * Implements some sort of popup selection interface for selecting a spinner
     * option. Allows for different spinner modes.
     */
    private interface SpinnerPopup {
        public void setAdapter(ListAdapter adapter);

        /**
         * Show the popup
         */
        public void show();

        /**
         * Dismiss the popup
         */
        public void dismiss();

        /**
         * @return true if the popup is showing, false otherwise.
         */
        public boolean isDialogShowing();

        /**
         * Set hint text to be displayed to the user. This should provide a
         * description of the choice being made.
         * 
         * @param hintText Hint text to set.
         */
        public void setPromptText(CharSequence hintText);

        public CharSequence getHintText();

        public void setBackgroundDrawable(Drawable bg);

        public void setVerticalOffset(int px);

        public void setHorizontalOffset(int px);

        public Drawable getBackground();

        public int getVerticalOffset();

        public int getHorizontalOffset();
    }

    void setDropdownWidth(int width){
        mDropDownWidth = width;
    }
    void computeContentWidth() {
//        final Drawable background = getBackground();
//        int hOffset = 0;
//        if (background != null) {
//            background.getPadding(mTempRect);
//            hOffset = isLayoutRtl() ? mTempRect.right : -mTempRect.left;
//        } else {
//            mTempRect.left = mTempRect.right = 0;
//        }
//
//        final int spinnerPaddingLeft = AuroraSpinner.this.getPaddingLeft();
//        final int spinnerPaddingRight = AuroraSpinner.this.getPaddingRight();
//        final int spinnerWidth = AuroraSpinner.this.getWidth();
//
//        if (mDropDownWidth == WRAP_CONTENT) {
//            int contentWidth =  measureContentWidth(
//                    (SpinnerAdapter) mAdapter, getBackground());
//            final int contentWidthLimit = getResources()
//                    .getDisplayMetrics().widthPixels - mTempRect.left - mTempRect.right;
//            if (contentWidth > contentWidthLimit) {
//                contentWidth = contentWidthLimit;
//            }
//            setContentWidth(Math.max(
//                   contentWidth, spinnerWidth - spinnerPaddingLeft - spinnerPaddingRight));
//        } else if (mDropDownWidth == MATCH_PARENT) {
//            setContentWidth(spinnerWidth - spinnerPaddingLeft - spinnerPaddingRight);
//        } else {
//            setContentWidth(mDropDownWidth);
//        }
//
//        if (isLayoutRtl()) {
//            hOffset += spinnerWidth - spinnerPaddingRight - getWidth();
//        } else {
//            hOffset += spinnerPaddingLeft;
//        }
       
    }

    private class AuroraDialogPopup extends AuroraDialog implements SpinnerPopup,
            DialogInterface.OnClickListener {
        private ListView mList;
        private ListAdapter mListAdapter;
        private CharSequence mPrompt;
        private int gravity = Gravity.BOTTOM;

        private View mContentView;
        
        private AdapterView<?> mParent;
        private View mChild;
        private int mPosition;
        private long mId;

        public AuroraDialogPopup(Context context) {
            super(context);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mContentView = inflater.inflate(
                    R.layout.aurora_spinner_dialog, null);
            super.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            setContentView(mContentView);
            mList = (ListView) mContentView
                    .findViewById(R.id.aurora_spinner_dialog_list);
            ImageView greyBg = (ImageView) mContentView
                    .findViewById(R.id.aurora_spinner_grey_bg);
            greyBg.setVisibility(View.GONE);
            setCanceledOnTouchOutside(true);

            mList.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                        int position, long id) {
                    // TODO Auto-generated method stub
                    AuroraSpinner.this.setSelection(position);
                    AuroraSpinner.this.performItemClick(v, position, id);
                    mParent = parent;
                    mPosition = position;
                    mChild = v;
                    mId = id;
                    if(mItemClickListener != null){
                        Log.e("spinner", "mItemClickListener1");
                        mItemClickListener.itemClick(mParent, mChild , mPosition, mId);
                    }
                    dismiss();
                }

            });

            mList.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View v,
                        int position, long id) {
                    // TODO Auto-generated method stub
                    AuroraSpinner.this.setSelection(position);
                    AuroraSpinner.this.performItemClick(v, position, id);
                    mParent = parent;
                    mPosition = position;
                    mChild = v;
                    mId = id;
                    if(mItemClickListener != null){
                        Log.e("spinner", "mItemClickListener1");
                        mItemClickListener.itemClick(mParent, mChild , mPosition, mId);
                    }
                    dismiss();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });

        }


      
        public void dismiss() {
            if(mChangeArrow){
                mChangeArrowHandler.sendEmptyMessage(MSG_ARROW_DOWN);
            }
            
            super.dismiss();
        }

        public void setAdapter(ListAdapter adapter) {
            mListAdapter = adapter;
            if (mList != null) {
                mList.setAdapter(adapter);
            }

        }

        public void setPromptText(CharSequence hintText) {
            mPrompt = hintText;
        }


        public CharSequence getHintText() {
            return mPrompt;
        }

        public void show() {
            Window window = super.getWindow();
            window.getAttributes().gravity = Gravity.BOTTOM;
            window.getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
            window.getAttributes().height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.getAttributes().windowAnimations = R.style.AuroraSpinnerAnimationStyle;
           AuroraSpinner.this.setDropdownWidth(window.getAttributes().width);
            ListAdapter adapter = mList.getAdapter();
            if (adapter == null) {
                return;
            }
            int itemCount = adapter.getCount();
            ViewGroup.MarginLayoutParams lp;
            lp = (ViewGroup.MarginLayoutParams) mList.getLayoutParams();
            if (itemCount > 5) {
                if (lp.height == getDisplay().getHeight()) {
                    lp.height = DensityUtil.dip2px(getContext(), 422);
                }

            } else {
                lp.height = lp.WRAP_CONTENT;
            }
            mList.setLayoutParams(lp);
//            mList.setSelection(0);
            if(mChangeArrow){
                mChangeArrowHandler.sendEmptyMessage(MSG_ARROW_UP);
            }
            
            super.show();
            final ViewTreeObserver vto = getViewTreeObserver();
            if (vto != null) {
                final OnGlobalLayoutListener layoutListener = new OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (!AuroraSpinner.this.isVisibleToUser()) {
                            dismiss();
                        }
                    }
                };
                vto.addOnGlobalLayoutListener(layoutListener);
            }

        }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dismiss();
            }
            return super.onKeyDown(keyCode, event);
        }

        public void onClick(DialogInterface dialog, int which) {
            setSelection(which);
            dismiss();
        }

        @Override
        public void setBackgroundDrawable(Drawable bg) {
            if (mList != null && bg != null) {
                mList.setBackgroundDrawable(bg);
            }

        }

        @Override
        public void setVerticalOffset(int px) {

        }

        @Override
        public void setHorizontalOffset(int px) {

        }

        @Override
        public Drawable getBackground() {
            return mList.getBackground();
        }

        @Override
        public int getVerticalOffset() {
            return 0;
        }

        @Override
        public int getHorizontalOffset() {
            return 0;
        }

        @Override
        public boolean isDialogShowing() {
            // TODO Auto-generated method stub
            return super.isShowing();
        }
    }

    private class DialogPopup implements SpinnerPopup, DialogInterface.OnClickListener {
        private AuroraAlertDialog mPopup;
        private ListAdapter mListAdapter;
        private CharSequence mPrompt;

        public void dismiss() {
            mPopup.dismiss();
            mPopup = null;
        }

        public boolean isDialogShowing() {
            return mPopup != null ? mPopup.isShowing() : false;
        }

        public void setAdapter(ListAdapter adapter) {
            mListAdapter = adapter;
        }

        public void setPromptText(CharSequence hintText) {
            mPrompt = hintText;
        }

        public CharSequence getHintText() {
            return mPrompt;
        }

        public void show() {
            AuroraAlertDialog.Builder builder = new AuroraAlertDialog.Builder(getContext());
            if (mPrompt != null) {
                builder.setTitle(mPrompt);
            }
            mPopup = builder.setSingleChoiceItems(mListAdapter,
                    getSelectedItemPosition(), this).show();
        }

        public void onClick(DialogInterface dialog, int which) {
            setSelection(which);
            if (mOnItemClickListener != null) {
                performItemClick(null, which, mListAdapter.getItemId(which));
            }
            dismiss();
        }

        @Override
        public void setBackgroundDrawable(Drawable bg) {
            Log.e(TAG, "Cannot set popup background for MODE_DIALOG, ignoring");
        }

        @Override
        public void setVerticalOffset(int px) {
            Log.e(TAG, "Cannot set vertical offset for MODE_DIALOG, ignoring");
        }

        @Override
        public void setHorizontalOffset(int px) {
            Log.e(TAG, "Cannot set horizontal offset for MODE_DIALOG, ignoring");
        }

        @Override
        public Drawable getBackground() {
            return null;
        }

        @Override
        public int getVerticalOffset() {
            return 0;
        }

        @Override
        public int getHorizontalOffset() {
            return 0;
        }
    }

    private class DropdownPopup extends ListPopupWindow implements SpinnerPopup {
        private CharSequence mHintText;
        private ListAdapter mAdapter;
        private Context mContext;
        public DropdownPopup(Context context, AttributeSet attrs, int defStyleRes) {
            super(context, attrs, 0, defStyleRes);
            mContext = context;
            setAnchorView(AuroraSpinner.this);
            setModal(true);
            setPromptPosition(POSITION_PROMPT_ABOVE);
            setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    AuroraSpinner.this.setSelection(position);
                    if (mOnItemClickListener != null) {
                        AuroraSpinner.this.performItemClick(view, position, mAdapter.getItemId(position));
                    }
                    dismiss();
                }

            });
        }
        @Override
        public boolean isDialogShowing() {
            // TODO Auto-generated method stub
            return super.isShowing();
        }

        @Override
        public void setAdapter(ListAdapter adapter) {
            super.setAdapter(adapter);
            mAdapter = adapter;
        }

        public CharSequence getHintText() {
            return mHintText;
        }

        public void setPromptText(CharSequence hintText) {
            // Hint text is ignored for dropdowns, but maintain it here.
            mHintText = hintText;
        }

        @Override
        public void show() {
            final Drawable background = getBackground();
            int hOffset = 0;
            if (background != null) {
                background.getPadding(mTempRect);
                hOffset = isLayoutRtl() ? mTempRect.right : -mTempRect.left;
            } else {
                mTempRect.left = mTempRect.right = 0;
            }

            final int spinnerPaddingLeft = AuroraSpinner.this.getPaddingLeft();
            final int spinnerPaddingRight = AuroraSpinner.this.getPaddingRight();
            final int spinnerWidth = AuroraSpinner.this.getWidth();
//            if (mDropDownWidth == WRAP_CONTENT) {
//                int contentWidth = measureContentWidth(
//                        (SpinnerAdapter) mAdapter, getBackground());
//                final int contentWidthLimit = mContext.getResources()
//                        .getDisplayMetrics().widthPixels - mTempRect.left - mTempRect.right;
//                if (contentWidth > contentWidthLimit) {
//                    contentWidth = contentWidthLimit;
//                }
//                setContentWidth(Math.max(
//                        contentWidth, spinnerWidth - spinnerPaddingLeft - spinnerPaddingRight));
//            } else if (mDropDownWidth == MATCH_PARENT) {
//                setContentWidth(spinnerWidth - spinnerPaddingLeft - spinnerPaddingRight);
//            } else {
//                setContentWidth(mDropDownWidth);
//                
//            }
            setContentWidth(spinnerWidth);

            if (isLayoutRtl()) {
                hOffset += spinnerWidth - spinnerPaddingRight - getWidth();
            } else {
                hOffset += spinnerPaddingLeft;
            }
            setHorizontalOffset(hOffset);
            setInputMethodMode(ListPopupWindow.INPUT_METHOD_NOT_NEEDED);
            super.show();
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            setSelection(AuroraSpinner.this.getSelectedItemPosition());

            // Make sure we hide if our anchor goes away.
            // TODO: This might be appropriate to push all the way down to
            // PopupWindow,
            // but it may have other side effects to investigate first. (Text
            // editing handles, etc.)
            final ViewTreeObserver vto = getViewTreeObserver();
            if (vto != null) {
                final OnGlobalLayoutListener layoutListener = new OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (!AuroraSpinner.this.isVisibleToUser()) {
                            dismiss();
                        }
                    }
                };
                vto.addOnGlobalLayoutListener(layoutListener);
                setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        if(mChangeArrow){
                            mChangeArrowHandler.sendEmptyMessage(MSG_ARROW_DOWN);
                        }
                        final ViewTreeObserver vto = getViewTreeObserver();
                        if (vto != null) {
                            vto.removeOnGlobalLayoutListener(layoutListener);
                        }
                    }
                });
            }
            if(mChangeArrow){
                mChangeArrowHandler.sendEmptyMessage(MSG_ARROW_UP);
            }
            
        }
//        @Override
//        public void dismiss() {
//            // TODO Auto-generated method stub
//            
//            super.dismiss();
//        }
    }

    /**
     * M: Dismiss popup window by application's request.
     * 
     * @hide
     */
    public void dismissPopup() {
        if (mPopup != null && mPopup.isDialogShowing()) {
            mPopup.dismiss();
        }
    }

    /**
     * M: Judge whether the popup window is showing.
     * 
     * @hide
     */
    public boolean isPopupShowing() {
        return (mPopup != null && mPopup.isDialogShowing());
    }

    
    class ChangeArrowHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            
            switch (msg.what) {
                case MSG_ARROW_DOWN:
//                    AuroraSpinner.this.setBackgroundInternal(AuroraSpinner.this.getPosition());
                    setBackgroundResource(mBackgroundRes);
                    break;
                case MSG_ARROW_UP:
                    Log.e("spinner", "mChangeArrow:"+mChangeArrow);
                    if(mChangeArrow){
                       AuroraSpinner.this.setBackgroundResource(R.drawable.aurora_spinner_arrow_up_backgound);
                    }
                    break;

                default:
                    break;
            }
        }
        
        
     
        
    }
    

}