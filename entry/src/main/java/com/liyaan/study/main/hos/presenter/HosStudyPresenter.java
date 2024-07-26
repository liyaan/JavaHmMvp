package com.liyaan.study.main.hos.presenter;

import com.example.utils.base.BasePresenter;
import com.example.utils.net.RxScheduler;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.HosIndexJsonBean;
import com.liyaan.study.entity.resp.HosLinkJsonBean;
import com.liyaan.study.entity.resp.TreeJsonDataBean;
import com.liyaan.study.main.hos.contract.HosStudyContract;
import com.liyaan.study.main.hos.model.HosStudyModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.List;


public class HosStudyPresenter extends BasePresenter<HosStudyContract.View> implements HosStudyContract.Presenter {
    private HosStudyContract.Model model;
    public HosStudyPresenter() {
        model = new HosStudyModel();
    }

    @Override
    public void getHosIndexJson() {
        if (!isViewAttached()) {
            return;
        }
        model.getHosIndexJson()
                .compose(RxScheduler.Obs_io_main())
                .subscribe(new Observer<BaseObjectBean<HosLinkJsonBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull BaseObjectBean<HosLinkJsonBean> loginBeanBaseObjectBean) {
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