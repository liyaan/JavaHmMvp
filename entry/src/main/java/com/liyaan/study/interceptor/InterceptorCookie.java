package com.liyaan.study.interceptor;

import com.liyaan.study.common.Consts;
import ohos.data.preferences.Preferences;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static com.liyaan.study.MyApplication.mPreferences;

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
            settingCookie(cookies);
        }

        return response;
    }
    private void settingCookie(String cookie){
        //token_pass_wanandroid_com=08937b953a34ae42f65e461149b23129;
        // Domain=wanandroid.com; Expires=Fri, 16-Aug-2024 03:08:05 GMT; Path=/
        String[] cookies = cookie.split(";");
        String[] tokens = cookies[0].split("=");
        //token_pass_wanandroid_com  08937b953a34ae42f65e461149b23129
        if (tokens[0].contains("token_pass")){
            System.out.println("Cookies: " + tokens[0]+"  "+tokens[1]);
            mPreferences.putString(Consts.TOKEN_KEY, tokens[0]);
            mPreferences.putString(Consts.TOKEN_VALUE, tokens[1]);
        }

    }
}