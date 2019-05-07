package tran.com.android.tapla.gamecenter.net;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import tran.com.android.tapla.gamecenter.datauiapi.bean.MarketListObject;
import tran.com.android.tapla.gamecenter.entity.BaseEntity;

/**
 * 说明：
 *
 * @auther lianbing.chen@itel-mobile.com
 * @date 2017/9/6 13:46
 */

public interface GameNetInterface {

    /**
     * Get网络请求
     * @param json
     * @return
     */
    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @GET("list")
    Call<MarketListObject> getMainPage(@Query("parmJson") String json);

    /**
     * Get网络请求
     * @param json
     * @return
     */
    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @GET("download")
    Call<BaseEntity> collectDownloadCounts(@Query("parmJson") String json);

    /**
     * 下载
     * @param url
     * @return
     */
    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url);


}
