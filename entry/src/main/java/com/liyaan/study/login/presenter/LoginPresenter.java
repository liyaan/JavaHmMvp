package com.liyaan.study.login.presenter;

import com.example.utils.base.BasePresenter;
import com.example.utils.net.RxScheduler;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.LoginBean;
import com.liyaan.study.interceptor.BaseObserver;
import com.liyaan.study.login.contract.LoginContract;
import com.liyaan.study.login.model.LoginModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    private LoginContract.Model model;
    public LoginPresenter() {
        model = new LoginModel();
    }
    @Override
    public void login(String username, String password) {
        if (!isViewAttached()) {
            return;
        }
        model.login(username,password)
                .compose(RxScheduler.Obs_io_main())
                .subscribe(new Observer<BaseObjectBean<LoginBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull BaseObjectBean<LoginBean> loginBeanBaseObjectBean) {
                        mView.onSuccess(loginBeanBaseObjectBean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.onError(e.getMessage());
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void collect(int page) {
        if (!isViewAttached()) {
            return;
        }
        model.collect(page)
                .compose(RxScheduler.Obs_io_main())
                .subscribe(new BaseObserver<BaseObjectBean<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }

                    @Override
                    public void next(BaseObjectBean<String> loginBeanBaseObjectBean) {
                        mView.onCollect(loginBeanBaseObjectBean);
                    }

                    @Override
                    public void error(Throwable e) {
                        mView.onError(e.getMessage());
                        mView.hideLoading();
                    }
                });
    }
}