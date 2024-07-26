package com.liyaan.study.main.study.second.presenter;

import com.example.utils.base.BasePresenter;
import com.example.utils.net.RxScheduler;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.ArticleDataBean;
import com.liyaan.study.entity.resp.TreeJsonDataBean;
import com.liyaan.study.main.study.second.contract.StudySecondContract;
import com.liyaan.study.main.study.second.model.StudySecondModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.List;


public class StudySecondPresenter extends BasePresenter<StudySecondContract.View> implements StudySecondContract.Presenter {
    private StudySecondContract.Model model;
    public StudySecondPresenter() {
        model = new StudySecondModel();
    }


    @Override
    public void getTreeJsonArticleList(int page, int cid) {
        if (!isViewAttached()) {
            return;
        }
        model.getTreeJsonArticleList(page,cid)
                .compose(RxScheduler.Obs_io_main())
                .subscribe(new Observer<BaseObjectBean<ArticleDataBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull BaseObjectBean<ArticleDataBean> loginBeanBaseObjectBean) {
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