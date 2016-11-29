package com.hrsst.housekeeper.mvp.defenceList;

import com.hrsst.housekeeper.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/11/28.
 */
@Module
public class DefenceListModule {
    private DefenceListActivity defenceListActivity;

    public DefenceListModule(DefenceListActivity defenceListActivity){
        this.defenceListActivity = defenceListActivity;
    }

    @ActivityScope
    @Provides
    DefenceListActivity provideDefenceListActivity(){
        return defenceListActivity;
    }

    @ActivityScope
    @Provides
    DefenceListPresenter provideDefenceListPresenter(DefenceListActivity defenceListActivity){
        return new DefenceListPresenter(defenceListActivity);
    }
}
