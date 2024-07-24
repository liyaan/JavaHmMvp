package com.example.utils.base;
import com.example.utils.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;

public abstract class BaseFraction extends Fraction {
//启动fraction
//依次调用onComponentAttached、onStart、onActive
//按下home键进入后台
//依次调用onInactive、onBackground
//重新回到前台
//依次调用onForeground、onActive
//退出fraction
//依次调用onInactive、onBackground、onStop、onComponentDetach

    private Component mComponent;
    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {

        if (getLayoutId()!=0){
            mComponent = scatter.parse(getLayoutId(), container, false);
        }else{
            mComponent = scatter.parse(ResourceTable.Layout_fraction, container, false);
        }
        return mComponent;
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        initView(mComponent);
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
    public abstract void initView( Component component);

    /**
     * 数据
     */
    public abstract void initOnActive();

}
