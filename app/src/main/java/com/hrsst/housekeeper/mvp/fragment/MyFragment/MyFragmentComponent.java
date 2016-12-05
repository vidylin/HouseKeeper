package com.hrsst.housekeeper.mvp.fragment.MyFragment;

import com.hrsst.housekeeper.ActivityScope;
import com.hrsst.housekeeper.AppComponent;

import dagger.Component;

/**
 * Created by Administrator on 2016/12/2.
 */
@ActivityScope
@Component(modules = MyFragmentModule.class,dependencies = AppComponent.class)
public interface MyFragmentComponent {
    MyFragment inject(MyFragment myFragment);
}
