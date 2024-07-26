package com.liyaan.study.main.study.second.contract;

import com.example.utils.base.BaseView;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.ArticleDataBean;
import com.liyaan.study.entity.resp.TreeJsonDataBean;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface StudySecondContract {
    interface Model {
        Observable<BaseObjectBean<ArticleDataBean>> getTreeJsonArticleList(int page, int cid);
    }

    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(String errMessage);

        void onSuccess(BaseObjectBean<ArticleDataBean> bean);

    }

    interface Presenter {
        void getTreeJsonArticleList(int page, int cid);

    }
}
