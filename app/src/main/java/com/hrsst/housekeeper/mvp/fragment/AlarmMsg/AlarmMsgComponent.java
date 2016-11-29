package com.hrsst.housekeeper.mvp.fragment.AlarmMsg;

import com.hrsst.housekeeper.ActivityScope;
import com.hrsst.housekeeper.AppComponent;

import dagger.Component;

/**
 * Created by Administrator on 2016/11/29.
 */
@ActivityScope
@Component(modules = AlarmMsgModule.class,dependencies = AppComponent.class)
public interface AlarmMsgComponent {
    AlarmMsgFragment inject(AlarmMsgFragment alarmMsgFragment);
}
