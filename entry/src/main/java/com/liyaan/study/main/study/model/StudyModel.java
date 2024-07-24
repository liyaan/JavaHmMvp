package com.liyaan.study.main.study.model;

import com.liyaan.study.MyApplication;
import com.liyaan.study.api.ApiHomeService;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.ArticleDataBean;
import com.liyaan.study.entity.resp.PageSliderBean;
import com.liyaan.study.entity.resp.TreeJsonDataBean;
import com.liyaan.study.main.study.contract.StudyContract;
import io.reactivex.rxjava3.core.Observable;

import java.util.List;

/**
 * Description：
 */
public class StudyModel implements StudyContract.Model {
    private ApiHomeService mApiService;
    public StudyModel(){
        mApiService = MyApplication.mRetrofitClient.create(ApiHomeService.class);
    }


    @Override
    public Observable<BaseObjectBean<List<TreeJsonDataBean>>> getTreeJson() {
        return mApiService.getTreeJson();
    }
}