/* 
 * Copyright (C) 2008 Google Inc.
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

package tran.com.android.gc.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Used by dialogs to change the font size and number of lines to try to fit
 * the text to the available space.
 */
public class AuroraDialogTitle extends TextView {
    
    public AuroraDialogTitle(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        initTextAppearance();
    }

    public AuroraDialogTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTextAppearance();
    }

    public AuroraDialogTitle(Context context) {
        super(context);
        initTextAppearance();
    }
    
    private void initTextAppearance(){
    	getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
         getPaint().setStrokeWidth(0.6f);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final Layout layout = getLayout();
        if (layout != null) {
            final int lineCount = layout.getLineCount();
            if (lineCount > 0) {
                final int ellipsisCount = layout.getEllipsisCount(lineCount - 1);
                if (ellipsisCount > 0) {
                    setSingleLine(false);
                    setMaxLines(2);

                    final TypedArray a = mContext.obtainStyledAttributes(null,
                            android.R.styleable.TextAppearance, android.R.attr.textAppearanceMedium,
                            android.R.style.TextAppearance_Medium);
                    final int textSize = a.getDimensionPixelSize(
                            android.R.styleable.TextAppearance_textSize, 0);
                    if (textSize != 0) {
                        // textSize is already expressed in pixels
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    }
                    a.recycle();

                    super.onMeasure(widthMeasureSpec, heightMeasureSpec);      
                }
            }
        }
    }
}
