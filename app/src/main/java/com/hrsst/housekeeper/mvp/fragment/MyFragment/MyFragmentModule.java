package com.hrsst.housekeeper.mvp.fragment.MyFragment;

import com.hrsst.housekeeper.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/12/2.
 */
@Module
public class MyFragmentModule {
    private MyFragment myFragment;

    public MyFragmentModule(MyFragment myFragment){
        this.myFragment = myFragment;
    }

    @ActivityScope
    @Provides
    MyFragment provideMyFragment(){
        return myFragment;
    }

    @ActivityScope
    @Provides
    MyFragmentPresenter provideMyFragmentPresenter(MyFragment myFragment){
        return new MyFragmentPresenter(myFragment);
    }
}
