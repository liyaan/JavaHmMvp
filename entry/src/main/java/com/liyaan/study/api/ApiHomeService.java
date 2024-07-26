package com.liyaan.study.api;

import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.*;
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


    @GET("harmony/index/json")
    Observable<BaseObjectBean<HosLinkJsonBean>> getHosIndexJson();


    @GET("article/list/{page}/json")
    Observable<BaseObjectBean<ArticleDataBean>> getTreeJsonArticleList(@Path("page")int page,@Query("cid") int cid);


    @GET("article/top/json")
    Observable<BaseObjectBean<List<ArticleListBean>>> getTopArticleList();

    @GET("navi/json")
    Observable<BaseObjectBean<List<NaviJsonBean>>> getNaviJson();
}