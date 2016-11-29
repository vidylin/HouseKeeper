package com.hrsst.housekeeper.mvp.modifyCameraInfo;

import com.hrsst.housekeeper.ActivityScope;
import com.hrsst.housekeeper.AppComponent;

import dagger.Component;

/**
 * Created by Administrator on 2016/11/24.
 */
@ActivityScope
@Component(modules = ModifyCameraPwdModule.class,dependencies = AppComponent.class)
public interface ModifyCameraPwdComponent {
    ModifyCameraPwdActivity inject(ModifyCameraPwdActivity modifyCameraPwdActivity);
}
