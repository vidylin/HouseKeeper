package com.hrsst.housekeeper.service;

import android.content.Context;

import com.hrsst.housekeeper.AppApplication;
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
            System.out.print(dataJson);
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
}
