package tran.com.android.talpa.app_core.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 说明：
 *
 * @auther lianbing.chen@itel-mobile.com
 * @date 2017/9/8 16:22
 */

public class DownloadInterceptor implements Interceptor {
    private RetrofitDownloadListener listener;

    public DownloadInterceptor(RetrofitDownloadListener listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        return originalResponse.newBuilder()
                .body(new DownloadResponseBody(originalResponse.body(), listener))
                .build();
    }
}
