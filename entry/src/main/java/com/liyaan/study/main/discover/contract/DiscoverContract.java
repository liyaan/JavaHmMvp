package com.liyaan.study.main.discover.contract;

import com.example.utils.base.BaseView;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.ArticleDataBean;
import com.liyaan.study.entity.resp.ArticleListBean;
import com.liyaan.study.entity.resp.NaviJsonBean;
import com.liyaan.study.entity.resp.PageSliderBean;
import io.reactivex.rxjava3.core.Observable;

import java.util.List;

public interface DiscoverContract {
    interface Model {
        Observable<BaseObjectBean<List<ArticleListBean>>> getTopArticleList();
        Observable<BaseObjectBean<List<NaviJsonBean>>> getNaviJson();
    }

    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(String errMessage);

        void onSuccess(BaseObjectBean<List<ArticleListBean>> bean);
        void onSuccessNavi(BaseObjectBean<List<NaviJsonBean>> bean);
    }

    interface Presenter {
        void getTopArticleList();
        void getNaviJson();
    }
}
