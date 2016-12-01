package com.hrsst.housekeeper.mvp.alarm;

import com.hrsst.housekeeper.common.basePresenter.BasePresenter;
import com.hrsst.housekeeper.entity.AlarmCameraInfo;
import com.hrsst.housekeeper.rxjava.ApiCallback;
import com.hrsst.housekeeper.rxjava.SubscriberCallBack;

import rx.Observable;

/**
 * Created by Administrator on 2016/11/29.
 */
public class AlarmPresenter extends BasePresenter<AlarmView>{

    public AlarmPresenter(AlarmActivity alarmActivity){
        attachView(alarmActivity);
    }

    public void getCameraInfo(String cameraId){
        Observable<AlarmCameraInfo> observable = apiStoreServer.getOneCamera(cameraId);
        addSubscription(observable,new SubscriberCallBack<>(new ApiCallback<AlarmCameraInfo>() {
            @Override
            public void onSuccess(AlarmCameraInfo model) {
                int errorCode = model.getErrorCode();
                if(errorCode==0){
                    AlarmCameraInfo.CameraBean cameraBean = model.getCamera();
                    mvpView.getAlarmCameraResult(cameraBean);
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
}
