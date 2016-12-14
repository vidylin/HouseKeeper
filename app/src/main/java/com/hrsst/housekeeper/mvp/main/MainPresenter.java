package com.hrsst.housekeeper.mvp.main;

import android.content.Context;

import com.hrsst.housekeeper.common.basePresenter.BasePresenter;
import com.hrsst.housekeeper.common.utils.SharedPreferencesManager;
import com.hrsst.housekeeper.common.utils.T;
import com.hrsst.housekeeper.mvp.interaction.MainViewInteraction;
import com.hrsst.housekeeper.mvp.listener.MainPresenterListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/11/8.
 */
public class MainPresenter extends BasePresenter<MainView> implements MainPresenterListener {
    private MainView view;
    MainViewInteraction mainViewInteraction;
    private boolean autoCheck;

    public MainPresenter(MainView view,MainViewInteraction mainViewInteraction){
        this.view = view;
        this.mainViewInteraction = mainViewInteraction;
    }

    public void updateVersion(Context mContext,boolean autoCheck){
        this.autoCheck = autoCheck;
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
        if(autoCheck==false||urlStr!=null){
            view.showUpdateDialog(message,urlStr);
        }
    }
    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    public void exitBy2Click(Context mContext) {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            T.showShort(mContext,"再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            view.exitBy2Click(isExit);
        }
    }
}
