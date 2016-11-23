package com.hrsst.housekeeper.mvp.addCamera;

import com.hrsst.housekeeper.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/11/23.
 */
@Module
public class AddCameraFourthModule {
    private AddCameraFourthActivity addCameraFourthActivity;
    public AddCameraFourthModule(AddCameraFourthActivity addCameraFourthActivity) {
        this.addCameraFourthActivity = addCameraFourthActivity;
    }

    @ActivityScope
    @Provides
    AddCameraFourthActivity provideAddCameraFourthActivity() {
        return addCameraFourthActivity;
    }

    @ActivityScope
    @Provides
    AddCameraFourthPresenter provideAddCameraFourthPresenter(AddCameraFourthActivity addCameraFourthActivity){
        return new AddCameraFourthPresenter(addCameraFourthActivity);
    }
}
