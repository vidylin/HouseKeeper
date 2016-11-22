package com.hrsst.housekeeper.mvp.interaction;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.hrsst.housekeeper.common.utils.CompareVersion;
import com.hrsst.housekeeper.entity.UpdateXml;
import com.hrsst.housekeeper.entity.VersionXml;
import com.hrsst.housekeeper.mvp.listener.MainPresenterListener;
import com.hrsst.housekeeper.retrofit.ApiStores;
import com.hrsst.housekeeper.rxjava.ApiCallback;
import com.hrsst.housekeeper.rxjava.SubscriberCallBack;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/11.
 */
public class MainViewInteraction {
    private ApiStores api;

    @Inject
    public MainViewInteraction(ApiStores myApi) {
        this.api = myApi;
    }

    public void checkVersion(final String localVersion,final MainPresenterListener mainPresenterListener){
        Observable<VersionXml> observable =  api.checkVersion();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberCallBack<>(new ApiCallback<VersionXml>() {
                    @Override
                    public void onSuccess(VersionXml model) {
                        UpdateXml mUpdateXml = model.getList().get(0);
                        String serverCode = mUpdateXml.getVersionName();
                        int result;
                        try {
                            result = CompareVersion.compareVersion(serverCode,localVersion);
                            if (result==1) {
                                mainPresenterListener.onFinished(mUpdateXml.getMsg(),mUpdateXml.getUrl());
                            }
                            if(result<1){
                                mainPresenterListener.onFinished(mUpdateXml.getMsg(),null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                    }

                    @Override
                    public void onCompleted() {
                    }
                }));
    }

    public String getLocalVersion(Context mContext){
        String localVersion = null;
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            localVersion = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

}
