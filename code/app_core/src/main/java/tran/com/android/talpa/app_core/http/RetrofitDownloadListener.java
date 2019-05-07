package tran.com.android.talpa.app_core.http;

/**
 * 说明：
 *
 * @auther lianbing.chen@itel-mobile.com
 * @date 2017/9/8 16:20
 */

public interface RetrofitDownloadListener {

    void update(long bytesRead, long contentLength, boolean done);
}
