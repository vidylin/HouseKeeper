package com.hrsst.housekeeper.mvp.alarm;

import com.hrsst.housekeeper.ActivityScope;
import com.hrsst.housekeeper.AppComponent;

import dagger.Component;

/**
 * Created by Administrator on 2016/11/29.
 */
@ActivityScope
@Component(modules = AlarmModule.class,dependencies = AppComponent.class)
public interface AlarmComponent {
    AlarmActivity inject(AlarmActivity alarmActivity);
}
