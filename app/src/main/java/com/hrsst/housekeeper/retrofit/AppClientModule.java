package com.hrsst.housekeeper.retrofit;

import com.hrsst.housekeeper.mvp.interaction.MainViewInteraction;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2016/11/11.
 */
@Module
public class AppClientModule {

    @Provides
    @Singleton
    public Retrofit provideAppClientModule(){
        return AppClient.getRetrofit();
    }

    @Provides
    @Singleton
    public ApiStores provideApiStores(Retrofit restAdapter){
        return restAdapter.create(ApiStores.class);
    }

    @Provides
    public MainViewInteraction provideMainViewInteraction(ApiStores apiStores) {
        return new MainViewInteraction(apiStores);
    }
}
