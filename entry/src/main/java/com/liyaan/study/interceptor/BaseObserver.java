package com.liyaan.study.interceptor;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public abstract class BaseObserver<BaseObjectBean> implements Observer<BaseObjectBean> {
    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(BaseObjectBean bean) {
        next(bean);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        error(e);
    }

    @Override
    public void onComplete() {

    }

    public abstract void next(BaseObjectBean bean);
    public abstract void error(@NonNull Throwable d);
}