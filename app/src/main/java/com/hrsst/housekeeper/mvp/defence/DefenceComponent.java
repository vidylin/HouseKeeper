package com.hrsst.housekeeper.mvp.defence;

import com.hrsst.housekeeper.ActivityScope;
import com.hrsst.housekeeper.AppComponent;

import dagger.Component;

/**
 * Created by Administrator on 2016/11/28.
 */
@ActivityScope
@Component(modules = DefenceModule.class,dependencies = AppComponent.class)
public interface DefenceComponent {
    DefenceActivity inject(DefenceActivity defenceActivity);
}
