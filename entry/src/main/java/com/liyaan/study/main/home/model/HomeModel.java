package com.liyaan.study.main.home.model;

import com.liyaan.study.MyApplication;
import com.liyaan.study.api.APIService;
import com.liyaan.study.api.ApiHomeService;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.ArticleDataBean;
import com.liyaan.study.entity.resp.ArticleListBean;
import com.liyaan.study.entity.resp.LoginBean;
import com.liyaan.study.entity.resp.PageSliderBean;
import com.liyaan.study.main.home.contract.HomeContract;
import io.reactivex.rxjava3.core.Observable;

import java.util.List;

/**
 * Description：
 */
public class HomeModel implements HomeContract.Model {
    private ApiHomeService mApiService;
    public HomeModel(){
        mApiService = MyApplication.mRetrofitClient.create(ApiHomeService.class);
    }


    @Override
    public Observable<BaseObjectBean<List<PageSliderBean>>> banner() {
        return mApiService.banner();
    }

    @Override
    public Observable<BaseObjectBean<ArticleDataBean>> getArticleList(int page) {
        return mApiService.getArticleList(page);
    }
}