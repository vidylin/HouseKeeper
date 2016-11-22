package com.hrsst.housekeeper.mvp.main;

import com.hrsst.housekeeper.ActivityScope;
import com.hrsst.housekeeper.mvp.interaction.MainViewInteraction;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/11/10.
 */
@Module
public class MainActivityModule {
    private MainView view;

    public MainActivityModule(MainView view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MainView provideMainView() {
        return view;
    }

    @ActivityScope
    @Provides
    MainPresenter provideMainPresenter(MainView mainView, MainViewInteraction mainViewInteraction){
        return new MainPresenter(view,mainViewInteraction);
    }

}
