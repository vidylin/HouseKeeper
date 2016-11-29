package com.hrsst.housekeeper.mvp.manualAddCamera;

import com.hrsst.housekeeper.ActivityScope;
import com.hrsst.housekeeper.AppComponent;

import dagger.Component;

/**
 * Created by Administrator on 2016/11/24.
 */
@ActivityScope
@Component(modules = ManualAddCameraModule.class,dependencies = AppComponent.class)
public interface ManualAddCameraComponent {
    ManualAddCameraActivity inject(ManualAddCameraActivity manualAddCameraActivity);
}
