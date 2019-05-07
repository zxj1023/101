package tran.com.android.talpa.app_core.http;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import tran.com.android.talpa.app_core.base.IApplication;
import tran.com.android.talpa.app_core.util.DeviceStateUtil;

/**
 * 说明：
 *
 * @auther lianbing.chen@itel-mobile.com
 * @date 2017/9/6 14:10
 */

public class CacheInterceptor implements Interceptor {
    private IApplication application;

    public CacheInterceptor(IApplication iApplication) {
        application = iApplication;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!DeviceStateUtil.isNetworkAvailable(application.getApplicationsContext())) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        if (DeviceStateUtil.isNetworkAvailable(application.getApplicationsContext())) {
            int maxAge = 0;
            // 有网络时 设置缓存超时时间0个小时
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .removeHeader("WuXiaolong")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build();
        } else {
            // 无网络时，设置超时为4周
            int maxStale = 60 * 60 * 24;
            response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("nyn")
                    .build();
        }
        return response;
    }
}
