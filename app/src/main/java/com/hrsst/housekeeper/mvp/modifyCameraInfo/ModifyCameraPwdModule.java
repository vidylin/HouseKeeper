package com.hrsst.housekeeper.mvp.modifyCameraInfo;

import com.hrsst.housekeeper.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/11/24.
 */
@Module
public class ModifyCameraPwdModule {
    private ModifyCameraPwdActivity modifyCameraPwdActivity;

    public ModifyCameraPwdModule(ModifyCameraPwdActivity modifyCameraPwdActivity){
        this.modifyCameraPwdActivity = modifyCameraPwdActivity;
    }

    @ActivityScope
    @Provides
    ModifyCameraPwdActivity provideModifyCameraPwdActivity(){
        return modifyCameraPwdActivity;
    }

    @ActivityScope
    @Provides
    ModifyCameraPwdPresenter provideModifyCameraPwdPresenter(ModifyCameraPwdActivity modifyCameraPwdActivity){
        return new ModifyCameraPwdPresenter(modifyCameraPwdActivity);
    }
}
