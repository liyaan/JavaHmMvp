package com.liyaan.study.main.study.second.model;

import com.liyaan.study.MyApplication;
import com.liyaan.study.api.ApiHomeService;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.ArticleDataBean;
import com.liyaan.study.entity.resp.TreeJsonDataBean;
import com.liyaan.study.main.study.second.contract.StudySecondContract;
import io.reactivex.rxjava3.core.Observable;

import java.util.List;

/**
 * Description：
 */
public class StudySecondModel implements StudySecondContract.Model {
    private ApiHomeService mApiService;
    public StudySecondModel(){
        mApiService = MyApplication.mRetrofitClient.create(ApiHomeService.class);
    }


    @Override
    public Observable<BaseObjectBean<ArticleDataBean>> getTreeJsonArticleList(int page, int cid) {
        return mApiService.getTreeJsonArticleList(page,cid);
    }
}