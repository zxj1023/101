package com.transsion.ossdk;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;

import com.transsion.ossdk.drawable.TalpaOssdkCustomePressedDrawable;

/**
 * Created by liang.wu1 on 2017/7/24.
 */

public class TalpaOssdkViewBgHelper {
    public static TalpaOssdkCustomePressedDrawable create(View view){
        Resources resources = view.getContext().getResources();
        return new TalpaOssdkCustomePressedDrawable(view, TalpaOssdkCustomePressedDrawable.BG_RECT_FOREBG_RECT,
                -1,
                resources.getColor(R.color.talpaossdk_btn_bg_normal_color),
                Color.BLACK,
                resources.getInteger(R.integer.talpaossdk_operation_item_bg_animation_duration),
                resources.getInteger(R.integer.talpaossdk_operation_item_bg_animation_startalpha),
                resources.getInteger(R.integer.talpaossdk_operation_item_bg_animation_endalpha)
        );
    }

    public static TalpaOssdkCustomePressedDrawable createForOperationItem(View view){
        Resources resources = view.getContext().getResources();
        return new TalpaOssdkCustomePressedDrawable(view, TalpaOssdkCustomePressedDrawable.BG_RECT_FOREBG_CIRCLE,
                resources.getDimensionPixelSize(R.dimen.talpaossdk_operation_item_bg_radius),
                resources.getColor(R.color.talpaossdk_btn_bg_normal_color),
                resources.getColor(R.color.talpaossdk_operation_item_rippleColor),
                resources.getInteger(R.integer.talpaossdk_operation_item_bg_animation_duration),
                resources.getInteger(R.integer.talpaossdk_operation_item_bg_animation_startalpha),
                resources.getInteger(R.integer.talpaossdk_operation_item_bg_animation_endalpha)
        );
    }
}
