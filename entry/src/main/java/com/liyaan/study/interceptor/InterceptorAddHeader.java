package com.liyaan.study.interceptor;

import com.liyaan.study.common.Consts;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static com.liyaan.study.MyApplication.mPreferences;

public class InterceptorAddHeader implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder();
        //添加Token    如果需要添加请求头可以统一在这里添加
        // mPreferences.putString("token_key", tokens[0]);
        //        mPreferences.putString("token_value", tokens[1]);
        // Request.Builder requestBuilder = original.newBuilder().header("token", "");
//        Request.Builder requestBuilder = original.newBuilder().header("token", "");
            requestBuilder.addHeader(mPreferences.getString(Consts.TOKEN_KEY,"Cookie"),
                    mPreferences.getString(Consts.TOKEN_VALUE,""));
        requestBuilder.addHeader("Cookie","loginUserName="+
                mPreferences.getString(Consts.LOGIN_USERNAME,""));
        requestBuilder.addHeader("Cookie","loginUserPassword="+
                mPreferences.getString(Consts.LOGIN_PASSWORD,""));
        requestBuilder.addHeader("loginUserName",
                mPreferences.getString(Consts.LOGIN_USERNAME,""));
        System.out.println("Cookies "+mPreferences.getString(Consts.LOGIN_USERNAME,"zzzzz u"));
        requestBuilder.addHeader("loginUserPassword",
                mPreferences.getString(Consts.LOGIN_PASSWORD,""));
        System.out.println("Cookies "+mPreferences.getString(Consts.LOGIN_PASSWORD,"zzzzz p"));
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}