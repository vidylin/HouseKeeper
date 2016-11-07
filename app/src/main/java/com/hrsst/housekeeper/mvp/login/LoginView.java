package com.hrsst.housekeeper.mvp.login;

import com.hrsst.housekeeper.entity.LoginModel;

/**
 * Created by Administrator on 2016/11/7.
 */
public interface LoginView {
    void getDataSuccess(LoginModel model);

    void getDataFail(String msg);

    void showLoading();

    void hideLoading();

    void autoLogin(String userId,String pwd);

    void autoLoginFail();
}
