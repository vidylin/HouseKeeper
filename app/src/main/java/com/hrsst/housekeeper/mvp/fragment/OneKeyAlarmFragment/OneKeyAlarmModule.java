package com.hrsst.housekeeper.mvp.fragment.OneKeyAlarmFragment;

import com.hrsst.housekeeper.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/11/10.
 */
@Module
public class OneKeyAlarmModule {
    private OneKeyAlarmFragment oneKeyAlarmFragment;

    public OneKeyAlarmModule(OneKeyAlarmFragment oneKeyAlarmFragment) {
        this.oneKeyAlarmFragment = oneKeyAlarmFragment;
    }

    @ActivityScope
    @Provides
    OneKeyAlarmFragment provideOneKeyAlarmFragment() {
        return oneKeyAlarmFragment;
    }

    @ActivityScope
    @Provides
    OneKeyAlarmPresenter provideOneKeyAlarmPresenter(){
        return new OneKeyAlarmPresenter(oneKeyAlarmFragment);
    }
}
