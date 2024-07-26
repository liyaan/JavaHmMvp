package com.liyaan.study.main.hos.model;

import com.liyaan.study.MyApplication;
import com.liyaan.study.api.ApiHomeService;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.HosIndexJsonBean;
import com.liyaan.study.entity.resp.HosLinkJsonBean;
import com.liyaan.study.entity.resp.TreeJsonDataBean;
import com.liyaan.study.main.hos.contract.HosStudyContract;
import io.reactivex.rxjava3.core.Observable;

import java.util.List;

/**
 * Description：
 */
public class HosStudyModel implements HosStudyContract.Model {
    private ApiHomeService mApiService;
    public HosStudyModel(){
        mApiService = MyApplication.mRetrofitClient.create(ApiHomeService.class);
    }


    @Override
    public Observable<BaseObjectBean<HosLinkJsonBean>> getHosIndexJson() {
        return mApiService.getHosIndexJson();
    }
}