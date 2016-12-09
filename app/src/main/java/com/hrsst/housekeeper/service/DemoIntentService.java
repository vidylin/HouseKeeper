package com.hrsst.housekeeper.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.hrsst.housekeeper.AppApplication;
import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.common.global.Constants;
import com.hrsst.housekeeper.common.utils.SharedPreferencesManager;
import com.hrsst.housekeeper.entity.HttpError;
import com.hrsst.housekeeper.retrofit.ApiStores;
import com.hrsst.housekeeper.retrofit.AppClient;
import com.hrsst.housekeeper.rxjava.ApiCallback;
import com.hrsst.housekeeper.rxjava.SubscriberCallBack;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Random;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/12/2.
 */
public class DemoIntentService extends GTIntentService {
    private CompositeSubscription mCompositeSubscription;
    @Override
    public void onReceiveServicePid(Context context, int i) {

    }

    @Override
    public void onReceiveClientId(Context context, String cid) {
        String userNumber = SharedPreferencesManager.getInstance().getData(context, SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTPASS_NUMBER);
        PushManager.getInstance().bindAlias(this.getApplicationContext(),userNumber);
        goToServer(cid,userNumber);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        String msg = new String(gtTransmitMessage.getPayload());
        try {
            JSONObject dataJson = new JSONObject(msg);
            DisposeAlarm disposeAlarm = new DisposeAlarm();
            disposeAlarm.setAlarmType(dataJson.getInt("alarmType"));
            disposeAlarm.setPolice(dataJson.getString("police"));
            disposeAlarm.setTime(dataJson.getString("time"));
            disposeAlarm.setPoliceName(dataJson.getString("policeName"));
            Random random4 = new Random();
            showDownNotification(context,disposeAlarm.getPoliceName()+"警员已处理您的消息",null,random4.nextInt(),null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {
        System.out.print(b);
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        System.out.print(gtCmdMessage);
    }

    private void goToServer(String cid,String userId){
        ApiStores apiStores = AppClient.retrofit(Constants.SERVER_PUSH).create(ApiStores.class);
        Observable observable = apiStores.bindAlias( userId,cid,"house");
        addSubscription(observable,new SubscriberCallBack<>(new ApiCallback<HttpError>() {
            @Override
            public void onSuccess(HttpError model) {
                AppApplication.context.setState(model.getState());
            }

            @Override
            public void onFailure(int code, String msg) {
            }

            @Override
            public void onCompleted() {
                stopSelf();
            }
        }));
    }

    private void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @SuppressWarnings("deprecation")
    private void showDownNotification(Context context, String message, Serializable mPushAlarmMsg, int id, Class clazz){
        NotificationCompat.Builder m_builder = new NotificationCompat.Builder(context);
        m_builder.setContentTitle(message); // 主标题

        //从系统服务中获得通知管理器
        NotificationManager nm=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        //具体的通知内容

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher); // 将PNG图片转
        m_builder.setLargeIcon(icon);

        m_builder.setSmallIcon(R.mipmap.ic_launcher); //设置小图标
        m_builder.setWhen(System.currentTimeMillis());
        m_builder.setAutoCancel(true);
        if(clazz!=null){
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);//设置提示音
            m_builder.setSound(uri);
            m_builder.setContentText("点击查看详情"); //设置主要内容
            //通知消息与Intent关联
            Intent it=new Intent(context,clazz);
            it.putExtra("mPushAlarmMsg",mPushAlarmMsg);
            it.putExtra("alarmMsg",message);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent contentIntent=PendingIntent.getActivity(context, id, it, PendingIntent.FLAG_CANCEL_CURRENT);
            m_builder.setContentIntent(contentIntent);
        }
        //执行通知
        nm.notify(id, m_builder.build());
    }
}
