package com.liyaan.study.slice;

import com.example.utils.base.BaseMvpAbilitySlice;
import com.example.utils.dialog.ToastUtils;
import com.liyaan.study.ResourceTable;
import com.liyaan.study.common.Consts;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.LoginBean;
import com.liyaan.study.fraction.*;
import com.liyaan.study.login.contract.LoginContract;
import com.liyaan.study.login.presenter.LoginPresenter;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.ability.fraction.FractionAbility;
import ohos.aafwk.ability.fraction.FractionManager;
import ohos.aafwk.ability.fraction.FractionScheduler;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ElementContainer;

import static com.liyaan.study.MyApplication.mPreferences;

public class MainAbilitySlice extends BaseMvpAbilitySlice<LoginPresenter> implements LoginContract.View {

    private TabList mTablist;
    private RadioContainer mRadioContainer;

    private Fraction mHomeFraction;
    private Fraction mHdFraction;
    private Fraction mStudyFraction;
    private Fraction mDiscoverFraction;
    private Fraction mOneSelfFraction;
    @Override
    public int getLayoutId() {
        return ResourceTable.Layout_ability_main_slice;
    }

    @Override
    public void initView() {
//        mTablist = (TabList) findComponentById(ResourceTable.Id_tabList);
        mRadioContainer = (RadioContainer) findComponentById(ResourceTable.Id_radioContainer);
//        mRadioContainer.mark(0);
        homeFraction();
        mRadioContainer.setMarkChangedListener((radioContainer, i) -> {
            switch (i){
                case 0:
                    homeFraction();
                    break;
                case 1:
                    hdFraction();
                    break;
                case 2:
                    studyFraction();
                    break;
                case 3:
                    discoverFraction();
                    break;
                case 4:
                    oneSelfFraction();
                    break;
            }
//            mRadioContainer.mark(i);
        });
    }
    //首页
    private void homeFraction() {
        hideAll();
        if (mHomeFraction == null) {
            mHomeFraction = new HomeFraction();
            addFraction(ResourceTable.Id_stackLayout,mHomeFraction);
        } else {
            showFraction(mHomeFraction);
        }

    }
    //家校互动
    private void hdFraction() {
        hideAll();
        if (mHdFraction == null) {
            mHdFraction = new SchoolHdFraction();
            addFraction(ResourceTable.Id_stackLayout,mHdFraction);
        } else {
            showFraction(mHdFraction);
        }
    }
    //学习中心
    private void studyFraction() {
        hideAll();
        if (mStudyFraction == null) {
            mStudyFraction = new StudyFraction();
            addFraction(ResourceTable.Id_stackLayout,mStudyFraction);
        } else {
            showFraction(mStudyFraction);
        }
    }
    //发现
    private void discoverFraction() {
        hideAll();
        if (mDiscoverFraction == null) {
            mDiscoverFraction = new DiscoverFraction();
            addFraction(ResourceTable.Id_stackLayout,mDiscoverFraction);
        } else {
            showFraction(mDiscoverFraction);
        }
    }
    //我的
    private void oneSelfFraction() {
        hideAll();
        if (mOneSelfFraction == null) {
            mOneSelfFraction = new OneSelfFraction();
            addFraction(ResourceTable.Id_stackLayout,mOneSelfFraction);
        } else {
            showFraction(mOneSelfFraction);
        }
    }
    private void hideAll(){
        if (mHomeFraction!=null) hideFraction(mHomeFraction);
        if (mHdFraction!=null) hideFraction(mHdFraction);
        if (mStudyFraction!=null) hideFraction(mStudyFraction);
        if (mDiscoverFraction!=null) hideFraction(mDiscoverFraction);
        if (mOneSelfFraction!=null) hideFraction(mOneSelfFraction);
    }
    @Override
    public void initOnActive() {

    }

    @Override
    public void onSuccess(BaseObjectBean<LoginBean> bean) {

    }

    @Override
    public void onCollect(BaseObjectBean<String> bean) {
        System.out.println("Cookies ==== "+bean.getErrorCode());
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(String errMessage) {

    }
}
