package com.hrsst.housekeeper.mvp.register;

import com.hrsst.housekeeper.ActivityScope;
import com.hrsst.housekeeper.AppComponent;

import dagger.Component;

/**
 * Created by Administrator on 2016/11/7.
 */
@ActivityScope
@Component(modules = RegisterEmailActivityModule.class,dependencies = AppComponent.class)
public interface RegisterEmailActivityComponent {
    RegisterEmailActivity inject(RegisterEmailActivity registerEmailActivity);
}
