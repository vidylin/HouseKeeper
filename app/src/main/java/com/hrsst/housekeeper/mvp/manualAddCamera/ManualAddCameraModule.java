package com.hrsst.housekeeper.mvp.manualAddCamera;

import com.hrsst.housekeeper.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/11/24.
 */
@Module
public class ManualAddCameraModule {
    private ManualAddCameraActivity manualAddCameraActivity;

    public ManualAddCameraModule(ManualAddCameraActivity manualAddCameraActivity){
        this.manualAddCameraActivity = manualAddCameraActivity;
    }

    @ActivityScope
    @Provides
    ManualAddCameraActivity providesManualAddCameraActivity(){
        return  manualAddCameraActivity;
    }

    @ActivityScope
    @Provides
    ManualAddCameraPresenter provideManualAddCameraPresenter(ManualAddCameraActivity manualAddCameraActivity){
        return new ManualAddCameraPresenter(manualAddCameraActivity);
    }
}
