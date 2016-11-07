package com.hrsst.housekeeper.mvp.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrsst.housekeeper.AppComponent;
import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.common.baseActivity.BaseActivity;
import com.hrsst.housekeeper.common.global.Constants;
import com.hrsst.housekeeper.common.utils.T;
import com.hrsst.housekeeper.entity.LoginModel;
import com.hrsst.housekeeper.mvp.main.MainActivity;
import com.hrsst.housekeeper.mvp.register.RegisterPhoneActivity;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class LoginActivity extends BaseActivity implements LoginView {
    @Inject
    LoginPresenter loginPresenter;
    @Bind(R.id.denglu_image)
    ImageView dengluImage;
    @Bind(R.id.user_id)
    EditText userId;
    @Bind(R.id.user_id_rela)
    RelativeLayout userIdRela;
    @Bind(R.id.user_pwd)
    EditText userPwd;
    @Bind(R.id.user_pwd_rela)
    RelativeLayout userPwdRela;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.tv_new_user)
    TextView tvNewUser;
    @Bind(R.id.tv_forget_pwd)
    TextView tvForgetPwd;
    @Bind(R.id.new_relative)
    RelativeLayout newRelative;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    private Context mContext;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    private void initView() {
        RxView.clicks(btnLogin).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        userID = userId.getText().toString().trim();
                        String pwd = userPwd.getText().toString().trim();
                        loginPresenter.loginYooSee(userID, pwd, mContext, 1);
                    }
                });
        RxView.clicks(tvNewUser).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(mContext, RegisterPhoneActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }
                });
        RxView.clicks(tvForgetPwd).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Uri uri = Uri.parse(Constants.FORGET_PASSWORD_URL);
                        Intent open_web = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(open_web);
                    }
                });
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerLoginActivityComponent.builder()
                .appComponent(appComponent)
                .loginActivityModule(new LoginActivityModule(this,null))
                .build()
                .inject(this);
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
    public void autoLogin(String userId, String pwd) {

    }

    @Override
    public void autoLoginFail() {

    }
}
