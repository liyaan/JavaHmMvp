package com.liyaan.study.login.contract;

import com.example.utils.base.BaseView;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.LoginBean;
import io.reactivex.rxjava3.core.Observable;

public interface LoginContract {
    interface Model {
        Observable<BaseObjectBean<LoginBean>> login(String username, String password);
        Observable<BaseObjectBean<String>> collect(int page);
    }

    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(String errMessage);

        void onSuccess(BaseObjectBean<LoginBean> bean);
        void onCollect(BaseObjectBean<String> bean);
    }

    interface Presenter {
        /**
         * 登陆
         *
         * @param username
         * @param password
         */
        void login(String username, String password);
        void collect(int page);
    }
}
