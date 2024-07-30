package com.liyaan.study.fraction;

import com.example.utils.base.BaseMvpFraction;
import com.example.utils.component.RoundImage;
import com.example.utils.component.timePicker.SingleDateAndTimePickerContainer;
import com.example.utils.component.timePicker.dialog.SingleDateAndTimePickerDialog;
import com.example.utils.log.UtilLog;
import com.liyaan.selectpicker.MainAbility;
import com.liyaan.study.ResourceTable;
import com.liyaan.study.common.Consts;
import com.liyaan.study.demo.SelectDataTimeAbilitySlice;
import com.liyaan.study.slice.CollapseCalendarViewAbilitySlice;
import com.liyaan.study.slice.ScrollViewToListAbilitySlice;
import com.liyaan.study.slice.StickyScrollViewAbilitySlice;
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
    private Text mSelectPicker;
    private Text mSelectCalMw;
    private Text mStickyScrollView;
    private Text mScrollViewList;

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
        mSelectPicker = (Text) component.findComponentById(ResourceTable.Id_selectPicker);
        mSelectCalMw = (Text) component.findComponentById(ResourceTable.Id_selectCalMw);
        mStickyScrollView = (Text) component.findComponentById(ResourceTable.Id_stickyScrollView);
        mScrollViewList = (Text) component.findComponentById(ResourceTable.Id_scrollViewList);
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
        mSelectPicker.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent = new Intent();
                mAbilitySlice.present(new MainAbility(),intent);
            }
        });
        mSelectCalMw.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent = new Intent();
                mAbilitySlice.present(new CollapseCalendarViewAbilitySlice(),intent);
            }
        });
        mStickyScrollView.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent = new Intent();
                mAbilitySlice.present(new StickyScrollViewAbilitySlice(),intent);
            }
        });
        mScrollViewList.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent = new Intent();
                mAbilitySlice.present(new ScrollViewToListAbilitySlice(),intent);
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