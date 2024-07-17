package com.example.utils.base;


import ohos.aafwk.content.Intent;

public abstract class BaseMvpAbility <T extends  BasePresenter>extends  BaseAbility implements  BaseView {

    protected T mPresenter;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
    }

    @Override
    protected void onStop() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onStop();
    }
}
