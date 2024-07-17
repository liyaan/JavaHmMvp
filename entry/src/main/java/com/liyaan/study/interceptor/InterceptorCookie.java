package com.liyaan.study.interceptor;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Set;

public class InterceptorCookie implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        // 发送请求
        Response response = chain.proceed(chain.request());

        // 从响应中获取所有cookie
        String cookies = response.header("Set-Cookie");
        if (cookies != null) {
            // 处理cookie，例如保存到SharedPreferences或数据库中
            // 这里仅为示例，打印出来
            System.out.println("Cookies: " + cookies);
        }

        return response;
    }
}