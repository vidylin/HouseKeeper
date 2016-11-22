package com.hrsst.housekeeper;

import android.app.Application;

import com.hrsst.housekeeper.mvp.interaction.MainViewInteraction;
import com.hrsst.housekeeper.retrofit.AppClientModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2016/10/19.
 */
@Singleton
@Component(modules = {AppModule.class, AppClientModule.class})
public interface AppComponent {
    Application getApplication();
    MainViewInteraction getWeatherInteractor();
}
