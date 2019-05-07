package tran.com.android.talpa.app_core.http;

import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import tran.com.android.talpa.app_core.log.LogPool;

/**
 * 说明：
 *
 * @auther lianbing.chen@itel-mobile.com
 * @date 2017/9/7 14:45
 */

public class HttpLogInterceptor implements Interceptor {
    private final String TAG  ="HttpLog";
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        okhttp3.Response response = chain.proceed(chain.request());
        long endTime = System.currentTimeMillis();
        long duration=endTime-startTime;
        okhttp3.MediaType mediaType = response.body().contentType();
        LogPool.d(TAG,"URL:"+request.url());
        LogPool.d(TAG,"Headers:"+request.headers());
        LogPool.d(TAG,"startTime:"+getTime(startTime)+"MS:"+startTime);
        LogPool.d(TAG,"endTime:"+getTime(endTime) +"MS:"+endTime);
        LogPool.d(TAG,"duration(MS):"+duration);
        String content = response.body().string();
        LogPool.d(TAG,"body:"+content);

        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }

    public String getTime(long date){
        return format.format(date);
    }


}
