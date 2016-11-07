package com.hrsst.housekeeper.mvp.login;

import com.hrsst.housekeeper.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/11/7.
 */
@Module
public class LoginActivityModule {
    private LoginActivity loginActivity;
    private SplashActivity splashActivity;

    public LoginActivityModule(LoginActivity loginActivity,SplashActivity splashActivity) {
        this.loginActivity = loginActivity;
        this.splashActivity = splashActivity;
    }

    @ActivityScope
    @Provides
    LoginActivity provideLoginActivity() {
        return loginActivity;
    }

    @ActivityScope
    @Provides
    SplashActivity provideSplashActivity() {
        return splashActivity;
    }

    @ActivityScope
    @Provides
    LoginPresenter provideLoginPresenter(){
        return new LoginPresenter(loginActivity,splashActivity);
    }

}
