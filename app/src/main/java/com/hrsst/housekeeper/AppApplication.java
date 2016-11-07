package com.hrsst.housekeeper;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2016/10/19.
 */
public class AppApplication extends Application{
    public static Context context;
    private AppComponent appComponent;

    public static AppApplication get(Context context){
        return (AppApplication)context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        appComponent=DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
