package com.liyaan.study.main.discover.model;

import com.liyaan.study.MyApplication;
import com.liyaan.study.api.ApiHomeService;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.ArticleDataBean;
import com.liyaan.study.entity.resp.ArticleListBean;
import com.liyaan.study.entity.resp.NaviJsonBean;
import com.liyaan.study.entity.resp.PageSliderBean;
import com.liyaan.study.main.discover.contract.DiscoverContract;
import io.reactivex.rxjava3.core.Observable;

import java.util.List;

/**
 * Description：
 */
public class DiscoverModel implements DiscoverContract.Model {
    private ApiHomeService mApiService;
    public DiscoverModel(){
        mApiService = MyApplication.mRetrofitClient.create(ApiHomeService.class);
    }


    @Override
    public Observable<BaseObjectBean<List<ArticleListBean>>> getTopArticleList() {
        return mApiService.getTopArticleList();
    }

    @Override
    public Observable<BaseObjectBean<List<NaviJsonBean>>> getNaviJson() {
        return mApiService.getNaviJson();
    }
}