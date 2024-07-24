package com.example.utils.base;


import ohos.aafwk.content.Intent;

public abstract class BaseMvpFraction<T extends  BasePresenter>extends  BaseFraction implements  BaseView {

    protected T mPresenter;

    @Override
    protected void onStop() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onStop();
    }
}
