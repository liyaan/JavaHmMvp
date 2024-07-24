package com.liyaan.study.main.home.contract;

import com.example.utils.base.BaseView;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.ArticleDataBean;
import com.liyaan.study.entity.resp.ArticleListBean;
import com.liyaan.study.entity.resp.LoginBean;
import com.liyaan.study.entity.resp.PageSliderBean;
import io.reactivex.rxjava3.core.Observable;

import java.util.List;

public interface HomeContract {
    interface Model {
        Observable<BaseObjectBean<List<PageSliderBean>>> banner();
        Observable<BaseObjectBean<ArticleDataBean>> getArticleList(int page);
    }

    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(String errMessage);

        void onSuccess(BaseObjectBean<List<PageSliderBean>> bean);
        void onSuccessArticleList(BaseObjectBean<ArticleDataBean> bean);
    }

    interface Presenter {
        void banner();
        void getArticleList(int page);
    }
}
