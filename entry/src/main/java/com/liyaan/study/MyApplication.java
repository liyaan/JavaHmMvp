package com.liyaan.study;

import com.example.utils.base.BaseApplication;
import com.example.utils.data.PreferencesUtils;
import com.example.utils.interceptor.InterceptorLog;
import com.example.utils.net.RetrofitClient;
import com.liyaan.study.interceptor.InterceptorAddHeader;
import com.liyaan.study.interceptor.InterceptorCookie;
import ohos.aafwk.ability.AbilityPackage;
import ohos.data.preferences.Preferences;

public class MyApplication extends BaseApplication {
    public static RetrofitClient mRetrofitClient;
    public static Preferences mPreferences;
    @Override
    public void onInitialize() {
        super.onInitialize();
        mRetrofitClient = RetrofitClient.newBuilder()
                .setBaseUrl("https://www.wanandroid.com")
                .addInterceptor(InterceptorLog.getInterceptor())
                .addInterceptor(new InterceptorCookie())
                .addInterceptor(new InterceptorAddHeader())
                .setTimeout(60)
                .builder();
        mPreferences = PreferencesUtils.getPreferencesUtils(this).init("fileName");
    }

}
