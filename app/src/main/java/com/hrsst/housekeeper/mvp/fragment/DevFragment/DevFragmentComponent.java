package com.hrsst.housekeeper.mvp.fragment.DevFragment;

import com.hrsst.housekeeper.ActivityScope;
import com.hrsst.housekeeper.AppComponent;

import dagger.Component;

/**
 * Created by Administrator on 2016/11/18.
 */
@ActivityScope
@Component(dependencies = AppComponent.class,modules = DevFragmentModule.class)
public interface DevFragmentComponent {
    void inject(DevFragment devFragment);
    DevFragmentPresenter getDevPresenter();
}
