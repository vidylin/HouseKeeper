package com.hrsst.housekeeper.mvp.register;

import com.hrsst.housekeeper.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/11/7.
 */
@Module
public class RegisterPhoneActivityModule {
    private RegisterPhoneActivity registerPhoneActivity;
    public RegisterPhoneActivityModule(RegisterPhoneActivity registerPhoneActivity) {
        this.registerPhoneActivity = registerPhoneActivity;
    }

    @ActivityScope
    @Provides
    RegisterPhoneActivity provideRegisterPhoneActivity() {
        return registerPhoneActivity;
    }

    @ActivityScope
    @Provides
    RegisterPhonePresenter provideRegisterPhonePresenter(RegisterPhoneActivity registerPhoneActivity){
        return new RegisterPhonePresenter(registerPhoneActivity);
    }
}
