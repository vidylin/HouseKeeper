package com.hrsst.housekeeper;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.hrsst.housekeeper.common.utils.SharedPreferencesManager;
import com.hrsst.housekeeper.service.LocationService;
import com.hrsst.housekeeper.ui.ForwardDownActivity;
import com.p2p.core.update.UpdateManager;

/**
 * Created by Administrator on 2016/10/19.
 */
public class AppApplication extends Application{
    public static String privilege;
    public static AppApplication context;
    public static final String MAIN_SERVICE_START = "com.hrsst.housekeeper.service.MAINSERVICE";
    private AppComponent appComponent;
    public static int SCREENWIGHT;
    public static int SCREENHIGHT;
    private NotificationManager mNotificationManager;
    private Notification mNotification;
    public static final int NOTIFICATION_DOWN_ID = 0x53256562;
    private RemoteViews cur_down_view;
    public LocationService locationService;

    public static AppApplication get(Context context){
        return (AppApplication)context.getApplicationContext();
    }

    public void setPrivilege(int privilege){
        this.privilege = privilege+"";
    }

    public String getPrivilege(){
        return privilege;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        locationService = new LocationService(getApplicationContext());
        appComponent=DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public NotificationManager getNotificationManager() {
        if (mNotificationManager == null)
            mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        return mNotificationManager;
    }

    /**
     * 创建下载图标
     */
    @SuppressWarnings("deprecation")
    public void showDownNotification(int state,int value) {
        boolean isShowNotify = SharedPreferencesManager.getInstance().getIsShowNotify(this);
        if(isShowNotify){

            mNotificationManager = getNotificationManager();
            mNotification = new Notification();
            long when = System.currentTimeMillis();
            mNotification = new Notification(
                    R.mipmap.ic_launcher,
                    this.getResources().getString(R.string.app_name),
                    when);
            // 放置在"正在运行"栏目中
            mNotification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_AUTO_CANCEL;

            RemoteViews contentView = new RemoteViews(getPackageName(),
                    R.layout.notify_down_bar);
            cur_down_view = contentView;
            contentView.setImageViewResource(R.id.icon,
                    R.mipmap.ic_launcher);

            Intent intent = new Intent(this,ForwardDownActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            switch(state){
                case UpdateManager.HANDLE_MSG_DOWN_SUCCESS:
                    cur_down_view.setTextViewText(R.id.down_complete_text, this.getResources().getString(R.string.down_complete_click));
                    cur_down_view.setTextViewText(R.id.progress_value,"100%");
                    contentView.setProgressBar(R.id.progress_bar, 100, 100, false);
                    intent.putExtra("state", UpdateManager.HANDLE_MSG_DOWN_SUCCESS);
                    break;
                case UpdateManager.HANDLE_MSG_DOWNING:
                    cur_down_view.setTextViewText(R.id.down_complete_text, this.getResources().getString(R.string.down_londing_click));
                    cur_down_view.setTextViewText(R.id.progress_value,value+"%");
                    contentView.setProgressBar(R.id.progress_bar, 100, value, false);
                    intent.putExtra("state", UpdateManager.HANDLE_MSG_DOWNING);
                    break;
                case UpdateManager.HANDLE_MSG_DOWN_FAULT:
                    cur_down_view.setTextViewText(R.id.down_complete_text, this.getResources().getString(R.string.down_fault_click));
                    cur_down_view.setTextViewText(R.id.progress_value,value+"%");
                    contentView.setProgressBar(R.id.progress_bar, 100, value, false);
                    intent.putExtra("state", UpdateManager.HANDLE_MSG_DOWN_FAULT);
                    break;
            }
            mNotification.contentView = contentView;
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mNotification.contentIntent = contentIntent;

            mNotificationManager.notify(NOTIFICATION_DOWN_ID,
                    mNotification);
        }
    }

    public void hideDownNotification(){
        mNotificationManager = getNotificationManager();
        mNotificationManager.cancel(NOTIFICATION_DOWN_ID);

    }
}
