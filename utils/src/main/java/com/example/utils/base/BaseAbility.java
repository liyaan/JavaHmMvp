package com.example.utils.base;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public abstract class BaseAbility  extends Ability {

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        if (getLayoutId()!=0){
            super.setUIContent(getLayoutId());
        }
        initView();

    }


    @Override
    protected void onStop() {
        super.onStop();
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
}
