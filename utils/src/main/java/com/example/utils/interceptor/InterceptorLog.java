package com.example.utils.interceptor;

import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;

public class InterceptorLog {
    public static  Interceptor getInterceptor() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //显示日志
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return interceptor;
    }
}