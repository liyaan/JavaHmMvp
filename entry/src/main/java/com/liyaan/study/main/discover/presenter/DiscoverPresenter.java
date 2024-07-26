package com.liyaan.study.main.discover.presenter;

import com.example.utils.base.BasePresenter;
import com.example.utils.net.RxScheduler;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.ArticleDataBean;
import com.liyaan.study.entity.resp.ArticleListBean;
import com.liyaan.study.entity.resp.NaviJsonBean;
import com.liyaan.study.entity.resp.PageSliderBean;
import com.liyaan.study.main.discover.contract.DiscoverContract;
import com.liyaan.study.main.discover.model.DiscoverModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.List;


public class DiscoverPresenter extends BasePresenter<DiscoverContract.View> implements DiscoverContract.Presenter {
    private DiscoverContract.Model model;
    public DiscoverPresenter() {
        model = new DiscoverModel();
    }

    @Override
    public void getTopArticleList() {
        if (!isViewAttached()) {
            return;
        }
        model.getTopArticleList()
                .compose(RxScheduler.Obs_io_main())
                .subscribe(new Observer<BaseObjectBean<List<ArticleListBean>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull BaseObjectBean<List<ArticleListBean>> loginBeanBaseObjectBean) {
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
    public void getNaviJson() {
        if (!isViewAttached()) {
            return;
        }
        model.getNaviJson()
                .compose(RxScheduler.Obs_io_main())
                .subscribe(new Observer<BaseObjectBean<List<NaviJsonBean>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull BaseObjectBean<List<NaviJsonBean>> loginBeanBaseObjectBean) {
                        mView.onSuccessNavi(loginBeanBaseObjectBean);
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