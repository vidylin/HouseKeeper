package com.hrsst.housekeeper.mvp.register;

import com.hrsst.housekeeper.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/11/7.
 */
@Module
public class RegisterEmailActivityModule {
    private RegisterEmailActivity registerEmailActivity;
    public RegisterEmailActivityModule(RegisterEmailActivity registerEmailActivity) {
        this.registerEmailActivity = registerEmailActivity;
    }

    @ActivityScope
    @Provides
    RegisterEmailActivity provideRegisterEmailActivity() {
        return registerEmailActivity;
    }

    @ActivityScope
    @Provides
    RegisterMailPresenter provideRegisterMailPresenter(RegisterEmailActivity registerEmailActivity){
        return new RegisterMailPresenter(registerEmailActivity);
    }
}
