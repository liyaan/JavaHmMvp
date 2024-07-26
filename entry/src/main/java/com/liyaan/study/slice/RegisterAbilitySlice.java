package com.liyaan.study.slice;

import com.example.utils.base.BaseMvpAbilitySlice;
import com.example.utils.dialog.ToastUtils;
import com.liyaan.study.ResourceTable;
import com.liyaan.study.entity.BaseObjectBean;
import com.liyaan.study.entity.resp.LoginBean;
import com.liyaan.study.login.contract.LoginContract;
import com.liyaan.study.login.presenter.LoginPresenter;
import com.liyaan.study.register.contract.RegisterContract;
import com.liyaan.study.register.presenter.RegisterPresenter;
import ohos.agp.components.Button;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.agp.utils.TextAlignment;

public class RegisterAbilitySlice extends BaseMvpAbilitySlice<RegisterPresenter> implements RegisterContract.View {
    private TextField mTextUsername, mTextPassword;
    private String mUsername, mPassword;
    private Button mRegisterBtn;
    private RegisterPresenter mPresenter;
    @Override
    public int getLayoutId() {
        return ResourceTable.Layout_activity_register;
    }

    @Override
    public void initView() {
        mTextUsername = (TextField) findComponentById(ResourceTable.Id_register_edit_account);
        mTextPassword = (TextField) findComponentById(ResourceTable.Id_register_edit_password);
        mRegisterBtn = (Button) findComponentById(ResourceTable.Id_register_btn);

        mPresenter=new RegisterPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void initOnActive() {
        mRegisterBtn.setClickedListener(component -> {
            mUsername = mTextUsername.getText();
            mPassword = mTextPassword.getText();
            if (mUsername!=null && mPassword!=null){
                mPresenter.register(mUsername,mPassword,mPassword);
            }else{
                ToastUtils.show(RegisterAbilitySlice.this,"请输入账号密码");
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

    @Override
    public void onSuccess(BaseObjectBean<LoginBean> bean) {

        if (bean.getErrorCode()==0){
            ToastUtils.oneShow(this,"注册成功",TextAlignment.CENTER);
            terminate();
        }else{
            ToastUtils.oneShow(this,bean.getErrorMsg(),TextAlignment.CENTER);
        }
    }
}
