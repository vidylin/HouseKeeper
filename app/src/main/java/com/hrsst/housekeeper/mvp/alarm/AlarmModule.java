package com.hrsst.housekeeper.mvp.alarm;

import com.hrsst.housekeeper.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/11/29.
 */
@Module
public class AlarmModule {
    private AlarmActivity alarmActivity;

    public AlarmModule(AlarmActivity alarmActivity){
        this.alarmActivity = alarmActivity;
    }

    @ActivityScope
    @Provides
    AlarmActivity provideAlarmActivity(){
        return alarmActivity;
    }

    @ActivityScope
    @Provides
    AlarmPresenter provideAlarmPresenter(AlarmActivity alarmActivity){
        return new AlarmPresenter(alarmActivity);
    }
}
