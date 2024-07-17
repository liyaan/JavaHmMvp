package com.liyaan.study;

import com.example.utils.base.BaseAbility;
import com.liyaan.study.slice.WelComeAbilitySlice;


public class WelComeAbility extends BaseAbility {


    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initView() {
        super.setMainRoute(WelComeAbilitySlice.class.getName());
    }
}
