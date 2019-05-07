package tran.com.android.talpa.app_core.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import tran.com.android.talpa.app_core.base.IApplication;
import tran.com.android.talpa.app_core.util.Constant;

/**
 * 说明：
 *
 * @auther lianbing.chen@itel-mobile.com
 * @date 2017/9/6 13:56
 */

public class RetrofitHelper {
    private final Gson mGson;
    private static  IApplication iApplication;

    public RetrofitHelper() {
        mGson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
    }

    private static class SingleOnHolder {
        private static final RetrofitHelper INSTANCE = new RetrofitHelper();
    }

    public static RetrofitHelper getInstance(IApplication application) {
        iApplication =  application;
        return SingleOnHolder.INSTANCE;
    }

    public <S> S createService(Class<S> serviceClass) {
        String baseUrl = Constant.BASE_URL;
        try {
            Field field = serviceClass.getField("BASE_URL");
            try {
                baseUrl = (String) field.get(field);
            } catch (IllegalAccessException e) {
               // e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
           // e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttp())
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }

    private final static long DEFAULT_TIMEOUT = 10;

    private OkHttpClient getOkHttp() {
        OkHttpClient.Builder okhttp = new OkHttpClient.Builder();
        okhttp.addInterceptor(new HttpLogInterceptor());
        okhttp.addNetworkInterceptor(new CacheInterceptor(iApplication));
        okhttp.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        okhttp.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        okhttp.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        okhttp.cache(new Cache(new File(iApplication.getApplicationsContext().getExternalCacheDir(), Constant.NET_CACHE), 10 * 1024 * 1024));
        return okhttp.build();
    }
}
