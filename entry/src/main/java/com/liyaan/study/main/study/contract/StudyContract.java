package com.liyaan.study.main.study.contract;

import com.example.utils.base.BaseView;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.ArticleDataBean;
import com.liyaan.study.entity.resp.PageSliderBean;
import com.liyaan.study.entity.resp.TreeJsonDataBean;
import io.reactivex.rxjava3.core.Observable;

import java.util.List;

public interface StudyContract {
    interface Model {
        Observable<BaseObjectBean<List<TreeJsonDataBean>>> getTreeJson();
    }

    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(String errMessage);

        void onSuccess(BaseObjectBean<List<TreeJsonDataBean>> bean);

    }

    interface Presenter {
        void getTreeJson();

    }
}
