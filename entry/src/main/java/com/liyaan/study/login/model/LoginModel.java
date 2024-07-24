package com.liyaan.study.login.model;

import com.liyaan.study.MyApplication;
import com.liyaan.study.api.APIService;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.LoginBean;
import com.liyaan.study.login.contract.LoginContract;
import io.reactivex.rxjava3.core.Observable;

/**
 * Description：
 */
public class LoginModel implements LoginContract.Model {
    private APIService mApiService;
    public LoginModel(){
        mApiService = MyApplication.mRetrofitClient.create(APIService.class);
    }
    @Override
    public Observable<BaseObjectBean<LoginBean>> login(String username, String password) {
        System.out.println("MainModel login 被调用");
        return mApiService.login(username,password);
    }

    @Override
    public Observable<BaseObjectBean<String>> collect(int page) {
        System.out.println("MainModel login 被调用");
        return mApiService.collect(page);
    }

}