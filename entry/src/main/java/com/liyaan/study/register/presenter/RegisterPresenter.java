package com.liyaan.study.register.presenter;

import com.example.utils.base.BasePresenter;
import com.example.utils.net.RxScheduler;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.LoginBean;
import com.liyaan.study.login.contract.LoginContract;
import com.liyaan.study.login.model.LoginModel;
import com.liyaan.study.register.contract.RegisterContract;
import com.liyaan.study.register.model.RegisterModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {
    private RegisterContract.Model model;
    public RegisterPresenter() {
        model = new RegisterModel();
    }

    @Override
    public void register(String username, String password, String repassword) {
        if (!isViewAttached()) {
            return;
        }
        model.register(username,password,repassword)
                .compose(RxScheduler.Obs_io_main())
                .subscribe(new Observer<BaseObjectBean<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull BaseObjectBean<String> loginBeanBaseObjectBean) {
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
}