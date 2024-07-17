package com.liyaan.study.slice;

import com.example.utils.base.BaseMvpAbilitySlice;
import com.example.utils.dialog.ToastUtils;
import com.liyaan.study.ResourceTable;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.LoginBean;
import com.liyaan.study.login.contract.LoginContract;
import com.liyaan.study.login.presenter.LoginPresenter;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.utils.TextAlignment;
import ohos.agp.window.dialog.ToastDialog;

public class LoginAbilitySlice extends BaseMvpAbilitySlice<LoginPresenter> implements LoginContract.View {
    private TextField mTextUsername, mTextPassword;
    private String mUsername, mPassword;
    private Button mLoginBtn;
    private LoginPresenter mPresenter;
    private Text mRegisterBtn;
    @Override
    public int getLayoutId() {
        return ResourceTable.Layout_activity_login;
    }

    @Override
    public void initView() {
        mTextUsername = (TextField) findComponentById(ResourceTable.Id_login_edit_account);
        mTextPassword = (TextField) findComponentById(ResourceTable.Id_login_edit_password);
        mLoginBtn = (Button) findComponentById(ResourceTable.Id_login_btn);
        mRegisterBtn = (Text) findComponentById(ResourceTable.Id_register_btn);
        mPresenter=new LoginPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void initOnActive() {
        mLoginBtn.setClickedListener(component -> {
            mUsername = mTextUsername.getText();
            mPassword = mTextPassword.getText();
            if (mUsername!=null && mPassword!=null){
                mPresenter.login(mUsername,mPassword);
            }else{
                ToastUtils.show(LoginAbilitySlice.this,"请输入账号密码");
            }
        });
        mRegisterBtn.setClickedListener(component -> {
//            ToastUtils.oneShow(LoginAbilitySlice.this,"注册功能", TextAlignment.BOTTOM);
            present(new RegisterAbilitySlice(),new Intent());
        });
    }

    @Override
    public void onSuccess(BaseObjectBean<LoginBean> bean) {
        ToastUtils.show(this,bean.getErrorCode()+" "+bean.getErrorMsg());
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
