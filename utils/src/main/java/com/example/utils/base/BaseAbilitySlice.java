package com.example.utils.base;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

public abstract class BaseAbilitySlice extends AbilitySlice {

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        if (getLayoutId()!=0){
            super.setUIContent(getLayoutId());
        }
        initView();
        onStartIntent(intent);
    }

    @Override
    public void onActive() {
        super.onActive();
        initOnActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
    @Override
    protected void onStop() {
        super.onStop();
        onBaseStop();
    }

    public void onStartIntent(Intent intent) {

    }
    public void onBaseStop() {

    }
    /**
     * 设置布局
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化视图
     */
    public abstract void initView();

    /**
     * 数据
     */
    public abstract void initOnActive();

}
