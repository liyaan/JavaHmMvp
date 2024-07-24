package com.liyaan.study.api;

import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.ArticleDataBean;
import com.liyaan.study.entity.resp.ArticleListBean;
import com.liyaan.study.entity.resp.PageSliderBean;
import com.liyaan.study.entity.resp.TreeJsonDataBean;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface ApiHomeService {

    @GET("banner/json")
    Observable<BaseObjectBean<List<PageSliderBean>>> banner();
    @GET("article/list/{page}/json")
    Observable<BaseObjectBean<ArticleDataBean>> getArticleList(@Path("page")int page);


    @GET("tree/json")
    Observable<BaseObjectBean<List<TreeJsonDataBean>>> getTreeJson();
}