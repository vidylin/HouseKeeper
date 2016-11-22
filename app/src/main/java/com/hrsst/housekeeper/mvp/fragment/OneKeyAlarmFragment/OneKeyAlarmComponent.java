package com.hrsst.housekeeper.mvp.fragment.OneKeyAlarmFragment;

import com.hrsst.housekeeper.ActivityScope;
import com.hrsst.housekeeper.AppComponent;

import dagger.Component;

/**
 * Created by Administrator on 2016/11/10.
 */
@ActivityScope
@Component(modules = OneKeyAlarmModule.class,dependencies = AppComponent.class)
public interface OneKeyAlarmComponent {
    OneKeyAlarmFragment inject(OneKeyAlarmFragment oneKeyAlarmFragment);
}
