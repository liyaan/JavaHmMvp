package com.liyaan.study.fraction;

import com.example.utils.base.BaseMvpFraction;
import com.example.utils.component.RoundImage;
import com.example.utils.component.timePicker.SingleDateAndTimePickerContainer;
import com.example.utils.component.timePicker.dialog.SingleDateAndTimePickerDialog;
import com.example.utils.log.UtilLog;
import com.liyaan.study.ResourceTable;
import com.liyaan.study.common.Consts;
import com.liyaan.study.demo.SelectDataTimeAbilitySlice;
import com.ryan.ohos.extension.TextUtils;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.liyaan.study.MyApplication.mPreferences;

public class OneSelfFraction extends BaseMvpFraction {
    private RoundImage mUserPhoto;
    private Text mUserName;
    private Text mSelectDataTime;

    private AbilitySlice mAbilitySlice;

    public OneSelfFraction(AbilitySlice abilitySlice) {
        this.mAbilitySlice = abilitySlice;
    }
    @Override
    public int getLayoutId() {
        return ResourceTable.Layout_fraction_oneself;
    }

    @Override
    public void initView(Component component) {
        mUserPhoto = (RoundImage) component.findComponentById(ResourceTable.Id_userPhoto);
        mUserName = (Text) component.findComponentById(ResourceTable.Id_userName);
        mSelectDataTime = (Text) component.findComponentById(ResourceTable.Id_selectDataTime);
        mUserPhoto.setPixelMapAndCircle(ResourceTable.Media_icon);
    }

    @Override
    public void initOnActive() {
        final String userName = mPreferences.getString(Consts.LOGIN_USERNAME,"");
        if (TextUtils.isEmpty(userName)){
            mUserName.setText("请去登陆");
        }else{
            mUserName.setText(userName);
        }
        mSelectDataTime.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent = new Intent();
                mAbilitySlice.present(new SelectDataTimeAbilitySlice(),intent);
            }
        });
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