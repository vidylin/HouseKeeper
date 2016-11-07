package com.hrsst.housekeeper.mvp.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hrsst.housekeeper.AppComponent;
import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.common.baseActivity.BaseActivity;
import com.hrsst.housekeeper.common.utils.T;
import com.hrsst.housekeeper.mvp.login.SplashActivity;
import com.hrsst.housekeeper.mvp.login.LoginActivity;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class RegisterPhoneActivity extends BaseActivity implements RegisterPhoneView {
    @Inject
    RegisterPhonePresenter registerPhonePresenter;
    @Bind(R.id.register_user)
    EditText registerUser;
    @Bind(R.id.register_pwd)
    EditText registerPwd;
    @Bind(R.id.register_comfire_pwd)
    EditText registerComfirePwd;
    @Bind(R.id.register_code)
    EditText registerCode;
    @Bind(R.id.register_get_code)
    Button registerGetCode;
    @Bind(R.id.register_btn_phone)
    Button registerBtnPhone;
    @Bind(R.id.register_old_user_tv)
    TextView registerOldUserTv;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    private Context mContext;
    private String phoneNO;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerRegisterPhoneActivityComponent.builder()
                .appComponent(appComponent)
                .registerPhoneActivityModule(new RegisterPhoneActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phone);
        ButterKnife.bind(this);
        mContext = this;
        doAction();
    }

    private void doAction() {
        RxView.clicks(registerGetCode).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        phoneNO = registerUser.getText().toString().trim();
                        registerPhonePresenter.getMessageCode(phoneNO);
                    }
                });
        RxView.clicks(registerBtnPhone).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String phoneNO = registerUser.getText().toString().trim();
                        String pwd = registerPwd.getText().toString().trim();
                        String rePwd = registerComfirePwd.getText().toString().trim();
                        String code = registerCode.getText().toString().trim();
                        registerPhonePresenter.register(phoneNO,pwd,rePwd,code,mContext);
                    }
                });
        RxView.clicks(registerOldUserTv).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        //跳转到登录界面
                        Intent intent1 = new Intent(mContext,LoginActivity.class);
                        startActivity(intent1);
                        finish();
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }
                });
    }

    @Override
    public void getDataFail(String msg) {
        T.showShort(mContext,msg);
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void register() {
        T.showShort(mContext,"注册成功,正在登陆");
        Intent login = new Intent(mContext, SplashActivity.class);
        startActivity(login);
        finish();
    }

    @Override
    public void getMessageSuccess() {
        T.showShort(mContext,"获取验证码成功");
    }
}
