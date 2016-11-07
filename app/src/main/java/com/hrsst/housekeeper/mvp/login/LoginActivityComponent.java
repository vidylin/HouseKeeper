package com.hrsst.housekeeper.mvp.login;

import com.hrsst.housekeeper.ActivityScope;
import com.hrsst.housekeeper.AppComponent;

import dagger.Component;

/**
 * Created by Administrator on 2016/11/7.
 */
@ActivityScope
@Component(modules = LoginActivityModule.class,dependencies = AppComponent.class)
public interface LoginActivityComponent {
    LoginActivity inject(LoginActivity loginActivity);
    SplashActivity inject(SplashActivity splashActivity);
}
