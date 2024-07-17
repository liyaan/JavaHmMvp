package com.example.utils.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.reactivex.rxjava3.annotations.NonNull;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RetrofitApiUtils {
    private Retrofit mRetrofit;

    public RetrofitApiUtils(Retrofit retrofit) {
        this.mRetrofit = retrofit;
    }

    /**
     * 创建api
     *
     * @param service
     * @param <T>
     * @return
     */
    public <T> T create(@NonNull Class<T> service) {
        return mRetrofit.create(service);
    }
    /***
     * 创建RequestBody，用于JSON字符串请求
     *
     * @param object 参数对象
     * @return
     */
    public static RequestBody createRequestBody(Object object) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(object));
    }

    public static RequestBody createRequestNewBody(Object object) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(object));
    }
}