package com.liyaan.study.main.home.presenter;

import com.example.utils.base.BasePresenter;
import com.example.utils.net.RxScheduler;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.ArticleDataBean;
import com.liyaan.study.entity.resp.ArticleListBean;
import com.liyaan.study.entity.resp.LoginBean;
import com.liyaan.study.entity.resp.PageSliderBean;
import com.liyaan.study.interceptor.BaseObserver;
import com.liyaan.study.main.home.contract.HomeContract;
import com.liyaan.study.main.home.model.HomeModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.List;


public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {
    private HomeContract.Model model;
    public HomePresenter() {
        model = new HomeModel();
    }

    @Override
    public void banner() {
        if (!isViewAttached()) {
            return;
        }
        model.banner()
                .compose(RxScheduler.Obs_io_main())
                .subscribe(new Observer<BaseObjectBean<List<PageSliderBean>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull BaseObjectBean<List<PageSliderBean>> loginBeanBaseObjectBean) {
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
    public void getArticleList(int page) {
        if (!isViewAttached()) {
            return;
        }
        model.getArticleList(page)
                .compose(RxScheduler.Obs_io_main())
                .subscribe(new Observer<BaseObjectBean<ArticleDataBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull BaseObjectBean<ArticleDataBean> loginBeanBaseObjectBean) {
                        mView.onSuccessArticleList(loginBeanBaseObjectBean);
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