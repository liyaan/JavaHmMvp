package com.example.utils.base;


import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.ability.fraction.FractionAbility;
import ohos.aafwk.ability.fraction.FractionManager;
import ohos.aafwk.ability.fraction.FractionScheduler;
import ohos.aafwk.content.Intent;

public abstract class BaseMvpAbilitySlice<T extends  BasePresenter>extends  BaseAbilitySlice implements  BaseView {

    protected T mPresenter;
    private FractionManager mFractionManager;
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
    private FractionManager getFractionManager(){
        if (mFractionManager==null){
            mFractionManager = ((FractionAbility) getAbility()).getFractionManager();
        }
        return mFractionManager;
    }
    private FractionScheduler getFractionScheduler(){
        FractionManager mFractionManager = getFractionManager();
        FractionScheduler fractionScheduler = mFractionManager.startFractionScheduler();
        return fractionScheduler;
    }
    public void addFraction(int layoutId, Fraction fragment) {
        FractionScheduler fractionScheduler = getFractionScheduler();
        fractionScheduler.add(layoutId, fragment).show(fragment).submit();
    }
    public void hideFraction(Fraction fragment) {
        FractionScheduler fractionScheduler = getFractionScheduler();
        fractionScheduler.hide(fragment).submit();
    }

    public void showFraction(Fraction fragment) {
        FractionScheduler fractionScheduler = getFractionScheduler();
        fractionScheduler.show(fragment).submit();
    }
}
