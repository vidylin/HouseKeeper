package com.hrsst.housekeeper;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2016/10/19.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    Application getApplication();
}
