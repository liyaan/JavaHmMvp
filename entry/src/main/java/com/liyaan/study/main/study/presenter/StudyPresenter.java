package com.liyaan.study.main.study.presenter;

import com.example.utils.base.BasePresenter;
import com.example.utils.net.RxScheduler;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.ArticleDataBean;
import com.liyaan.study.entity.resp.PageSliderBean;
import com.liyaan.study.entity.resp.TreeJsonDataBean;
import com.liyaan.study.main.study.contract.StudyContract;
import com.liyaan.study.main.study.model.StudyModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.List;


public class StudyPresenter extends BasePresenter<StudyContract.View> implements StudyContract.Presenter {
    private StudyContract.Model model;
    public StudyPresenter() {
        model = new StudyModel();
    }

    @Override
    public void getTreeJson() {
        if (!isViewAttached()) {
            return;
        }
        model.getTreeJson()
                .compose(RxScheduler.Obs_io_main())
                .subscribe(new Observer<BaseObjectBean<List<TreeJsonDataBean>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull BaseObjectBean<List<TreeJsonDataBean>> loginBeanBaseObjectBean) {
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