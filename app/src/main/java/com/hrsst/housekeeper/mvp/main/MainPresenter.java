package com.hrsst.housekeeper.mvp.main;

import android.content.Context;

import com.hrsst.housekeeper.common.basePresenter.BasePresenter;
import com.hrsst.housekeeper.common.utils.SharedPreferencesManager;
import com.hrsst.housekeeper.mvp.interaction.MainViewInteraction;
import com.hrsst.housekeeper.mvp.listener.MainPresenterListener;

/**
 * Created by Administrator on 2016/11/8.
 */
public class MainPresenter extends BasePresenter<MainView> implements MainPresenterListener {
    private MainView view;
    MainViewInteraction mainViewInteraction;

    public MainPresenter(MainView view,MainViewInteraction mainViewInteraction){
        this.view = view;
        this.mainViewInteraction = mainViewInteraction;
    }

    public void updateVersion(Context mContext,boolean autoCheck){
        long last_check_update_time = SharedPreferencesManager.getInstance().getLastAutoCheckUpdateTime(mContext);
        long now_time = System.currentTimeMillis();
        if (((now_time - last_check_update_time) > 1000 * 60 * 60 * 12&&autoCheck==true)||autoCheck==false) {
            SharedPreferencesManager.getInstance().putLastAutoCheckUpdateTime(now_time, mContext);
            String localVersion = mainViewInteraction.getLocalVersion(mContext);
            mainViewInteraction.checkVersion(localVersion,this);
        }
    }

    @Override
    public void onFinished(String message, String urlStr) {
        view.showUpdateDialog(message,urlStr);
    }

}
