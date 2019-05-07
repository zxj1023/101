package tran.com.android.tapla.gamecenter.util;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

/**
 * Created by jiazhuo.ren on 2017/9/21.
 */

public  class Utils {
    /**
     * 单独设置文字的颜色
     * @param str
     * @param ch1
     * @param ch2
     * @param color
     * @return
     */
    public static SpannableStringBuilder setTVColor(String str , String ch1 , String ch2 , int color ){
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        try {
            int a = str.indexOf(ch1); //从字符ch1的下标开始
            int b = str.indexOf(ch2)+1; //到字符ch2的下标+1结束,因为SpannableStringBuilder的setSpan方法中区间为[ a,b )左闭右开
            builder.setSpan(new ForegroundColorSpan(color), a, b, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }catch (Exception e){
            Log.e("setTVColor","setTVColor error");
            return  builder;
        }
        return builder;
    }
    public static int formatSize(int target_size) {
        int MB=target_size/1024;
        return MB;
    }
}
