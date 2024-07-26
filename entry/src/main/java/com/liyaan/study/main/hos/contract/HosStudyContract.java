package com.liyaan.study.main.hos.contract;

import com.example.utils.base.BaseView;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.HosIndexJsonBean;
import com.liyaan.study.entity.resp.HosLinkJsonBean;
import com.liyaan.study.entity.resp.TreeJsonDataBean;
import io.reactivex.rxjava3.core.Observable;

import java.util.List;

public interface HosStudyContract {
    interface Model {
        Observable<BaseObjectBean<HosLinkJsonBean>> getHosIndexJson();
    }

    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(String errMessage);

        void onSuccess(BaseObjectBean<HosLinkJsonBean> bean);

    }

    interface Presenter {
        void getHosIndexJson();

    }
}
