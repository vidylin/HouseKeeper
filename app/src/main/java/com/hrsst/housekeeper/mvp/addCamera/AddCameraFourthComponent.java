package com.hrsst.housekeeper.mvp.addCamera;

import com.hrsst.housekeeper.ActivityScope;
import com.hrsst.housekeeper.AppComponent;

import dagger.Component;

/**
 * Created by Administrator on 2016/11/23.
 */
@ActivityScope
@Component(modules = AddCameraFourthModule.class,dependencies = AppComponent.class)
public interface AddCameraFourthComponent {
    AddCameraFourthActivity inject(AddCameraFourthActivity addCameraFourthActivity);
}
