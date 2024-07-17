package com.liyaan.study;

import com.example.utils.base.BaseApplication;
import com.example.utils.interceptor.InterceptorLog;
import com.example.utils.net.RetrofitClient;
import com.liyaan.study.interceptor.InterceptorCookie;
import ohos.aafwk.ability.AbilityPackage;

public class MyApplication extends BaseApplication {
    public static RetrofitClient mRetrofitClient;
    @Override
    public void onInitialize() {
        super.onInitialize();
        mRetrofitClient = RetrofitClient.newBuilder()
                .setBaseUrl("https://www.wanandroid.com")
                .addInterceptor(InterceptorLog.getInterceptor())
                .addInterceptor(new InterceptorCookie())
                .setTimeout(60)
                .builder();
    }

}
