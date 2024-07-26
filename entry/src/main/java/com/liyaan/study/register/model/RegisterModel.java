package com.liyaan.study.register.model;

import com.liyaan.study.MyApplication;
import com.liyaan.study.api.APIService;
import com.liyaan.study.api.ApiRegisterService;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.LoginBean;
import com.liyaan.study.login.contract.LoginContract;
import com.liyaan.study.register.contract.RegisterContract;
import io.reactivex.rxjava3.core.Observable;

/**
 * Description：
 */
public class RegisterModel implements RegisterContract.Model {
    private ApiRegisterService mApiService;
    public RegisterModel(){
        mApiService = MyApplication.mRetrofitClient.create(ApiRegisterService.class);
    }


    @Override
    public Observable<BaseObjectBean<LoginBean>> register(String username, String password, String repassword) {
        System.out.println("MainModel login 被调用");
        return mApiService.register(username,password,repassword);
    }
}