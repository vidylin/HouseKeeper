package com.hrsst.housekeeper.mvp.defence;

import com.hrsst.housekeeper.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/11/28.
 */
@Module
public class DefenceModule {
    private DefenceActivity defenceActivity;

    public DefenceModule(DefenceActivity defenceActivity){
        this.defenceActivity = defenceActivity;
    }

    @ActivityScope
    @Provides
    DefenceActivity provideDefenceActivity(){
        return defenceActivity;
    }

    @ActivityScope
    @Provides
    DefencePresenter provideDefencePresenter(DefenceActivity defenceActivity){
        return new DefencePresenter(defenceActivity);
    }
}
