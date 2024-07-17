package com.example.utils.net;

import com.example.utils.BuildConfig;
import io.reactivex.rxjava3.annotations.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.net.Proxy;
import java.net.ProxySelector;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {

//    private Map<String,Object> mMapHeader = new HashMap<>();
    private Retrofit mRetrofit;
    private OkHttpClient mOkHttpClient;
    private OkHttpClient.Builder mOkHttpClientBuilder;
    private static Builder mBuilder;
    private RetrofitClient(){
        if (mBuilder.mBuilderOkHttpClient==null){
            if (mOkHttpClient==null){
                mOkHttpClientBuilder = new OkHttpClient.Builder();
                mOkHttpClientBuilder.proxy(mBuilder.mProxy);
                mOkHttpClientBuilder.connectTimeout(mBuilder.timeout, TimeUnit.SECONDS);
                int size = mBuilder.mInterceptorList.size();
                if (size>0){
                    for (int i=0;i<size;i++){
                        mOkHttpClientBuilder.addInterceptor(mBuilder.mInterceptorList.get(i));
                    }
                }
                mOkHttpClient = mOkHttpClientBuilder.build();
            }
        }else{
            mOkHttpClient = mBuilder.mBuilderOkHttpClient;
        }
        setRetrofit(mOkHttpClient);
    }
    private void setRetrofit(OkHttpClient okHttpClient){
        if (mRetrofit==null){
            mRetrofit = new Retrofit
                    .Builder()
                    .baseUrl(mBuilder.baseUrl)
                    //设置数据解析器
                    .addConverterFactory(GsonConverterFactory.create())
                    //设置网络请求适配器，使其支持RxJava与RxAndroid
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
    }
    public Retrofit getRetrofit(){
        return mRetrofit;
    }
    public <T> T create(@NonNull Class<T> service) {
        return mRetrofit.create(service);
    }
//    public void addHeader(String key,Object obj){
//        mapHeader.put(key, obj);
//    }

    public static Builder newBuilder(){
        mBuilder = new Builder();
        return mBuilder;
    }

    public static class Builder{
        private static  RetrofitClient instance;

//        private APIService apiService;
        private String baseUrl ;
        private long timeout = 60;
        private long readTime = 20;
        private long writeTime = 20;
        private List<Interceptor> mInterceptorList = new ArrayList<>();
        private Proxy mProxy;

        private OkHttpClient mBuilderOkHttpClient;
        public  RetrofitClient builder(){
            if (instance == null) {
                instance = new RetrofitClient();
            }
            return instance;
        }
        public Builder addInterceptor(Interceptor interceptor){
            mInterceptorList.add(interceptor);
            return this;
        }
        public Builder setBaseUrl(String baseUrl){
            this.baseUrl = baseUrl;
            return this;
        }
        public Builder setTimeout(long timeout){
            this.timeout = timeout;
            return this;
        }

        public Builder setReadTime(long readTime){
            this.readTime = readTime;
            return this;
        }
        public Builder setWriteTime(long writeTime){
            this.writeTime = writeTime;
            return this;
        }
        public Builder setProxy(Proxy proxy){
            this.mProxy = proxy;
            return this;
        }
        public  Builder getOkhttp(OkHttpClient okHttpClient){
            this.mBuilderOkHttpClient = okHttpClient;
            return this;
        }
    }
}