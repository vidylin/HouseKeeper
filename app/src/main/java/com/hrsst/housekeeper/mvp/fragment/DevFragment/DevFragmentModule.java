package com.hrsst.housekeeper.mvp.fragment.DevFragment;

import com.hrsst.housekeeper.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/11/18.
 */
@Module
public class DevFragmentModule {
    private DevFragmentView devFragmentView;
    public DevFragmentModule(DevFragmentView devFragmentView) {
        this.devFragmentView = devFragmentView;
    }

    @ActivityScope
    @Provides
    public DevFragmentView provideDevFragmentView() {
        return devFragmentView;
    }

    @ActivityScope
    @Provides
    public DevFragmentPresenter provideDevFragmentPresenter(DevFragmentView devFragmentView) {
        return new DevFragmentPresenterImpl(devFragmentView);
    }
}
