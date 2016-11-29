package com.hrsst.housekeeper.mvp.defenceList;

import com.hrsst.housekeeper.ActivityScope;
import com.hrsst.housekeeper.AppComponent;

import dagger.Component;

/**
 * Created by Administrator on 2016/11/28.
 */
@ActivityScope
@Component(modules = DefenceListModule.class,dependencies = AppComponent.class)
public interface DefenceListComponent {
    DefenceListActivity inject(DefenceListActivity defenceListActivity);
}
