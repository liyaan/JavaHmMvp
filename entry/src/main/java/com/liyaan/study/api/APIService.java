package com.liyaan.study.api;

import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.LoginBean;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

public interface APIService {

    /**
     * 登陆
     *
     * @param username 账号
     * @param password 密码
     * @return
     */
    @FormUrlEncoded
    @POST("user/login")
    Observable<BaseObjectBean<LoginBean>> login(@Field("username") String username,
                                                @Field("password") String password);


    @GET("lg/collect/list/{page}/json")
    Observable<BaseObjectBean<String>> collect(@Path("page") int page);

}