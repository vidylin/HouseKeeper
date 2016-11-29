package com.hrsst.housekeeper.mvp.fragment.AlarmMsg;

import com.hrsst.housekeeper.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/11/29.
 */
@Module
public class AlarmMsgModule {
    private AlarmMsgFragment alarmMsgFragment;

    public AlarmMsgModule(AlarmMsgFragment alarmMsgFragment){
        this.alarmMsgFragment = alarmMsgFragment;
    }

    @ActivityScope
    @Provides
    AlarmMsgFragment provideAlarmMsgFragment(){
        return alarmMsgFragment;
    }

    @ActivityScope
    @Provides
    AlarmMsgPresenter provideAlarmMsgPresenter(AlarmMsgFragment alarmMsgFragment){
        return new AlarmMsgPresenter(alarmMsgFragment);
    }
}
