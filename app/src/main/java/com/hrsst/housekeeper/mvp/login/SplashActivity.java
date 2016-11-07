package com.hrsst.housekeeper.mvp.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hrsst.housekeeper.AppComponent;
import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.common.baseActivity.BaseActivity;
import com.hrsst.housekeeper.common.utils.T;
import com.hrsst.housekeeper.entity.LoginModel;
import com.hrsst.housekeeper.mvp.main.MainActivity;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity implements LoginView{
    @Inject
    LoginPresenter loginPresenter;
    private Context mContext;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerLoginActivityComponent.builder()
                .appComponent(appComponent)
                .loginActivityModule(new LoginActivityModule(null,this))
                .build()
                .inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
        loginPresenter.autoLogin(this);
    }

    @Override
    public void getDataSuccess(LoginModel model) {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext,msg);
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void autoLogin(String userId, String pwd) {
        loginPresenter.loginYooSee(userId,pwd,mContext,0);
    }

    @Override
    public void autoLoginFail() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
