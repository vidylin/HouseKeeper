package com.hrsst.housekeeper.mvp.fragment.OneKeyAlarmFragment;

import android.os.Handler;
import android.os.Message;

import com.hrsst.housekeeper.common.basePresenter.BasePresenter;
import com.hrsst.housekeeper.common.data.Contact;
import com.hrsst.housekeeper.entity.Camera;
import com.hrsst.housekeeper.entity.PostResult;
import com.hrsst.housekeeper.rxjava.ApiCallback;
import com.hrsst.housekeeper.rxjava.SubscriberCallBack;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;

/**
 * Created by Administrator on 2016/11/10.
 */
public class OneKeyAlarmPresenter extends BasePresenter<OneKeyAlarmView>{
    private Timer timer;
    private String userId;
    private String privilege;
    private String cameraId;
    private String info;

    public OneKeyAlarmPresenter(OneKeyAlarmFragment oneKeyAlarmFragment){
        attachView(oneKeyAlarmFragment);
    }

    int countNum=0;
    public void startTimer(String userId,String privilege,String cameraId,String info){
        this.userId = userId;
        this.privilege = privilege;
        this.cameraId = cameraId;
        this.info = info;
        if(timer==null){
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                countNum = countNum+1;
                Message message = new Message();
                message.what = 1;
                message.obj =countNum;
                handler.sendMessage(message);//
            }
        },1,50);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int obj = (int) msg.obj;
            if(obj<=100){
                mvpView.getCurrentTime(obj);
            }else{
                countNum=0;
                if(timer!=null){
                    timer.cancel();
                    timer=null;
                }
                onKeyAlarm(userId,privilege,cameraId,info);
            }
        }
    };

    public void stopTimer(){
        if(timer!=null){
            timer.cancel();
            timer=null;
            countNum=0;
        }
    }

    public void getAllCamera(String userId, String privilege, String page, final boolean refresh){
        Observable<Camera> mObservable = apiStoreServer.ordinaryUserGetAllCamera(userId,privilege,page);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<Camera>() {
            @Override
            public void onSuccess(Camera model) {
                int errorCode = model.getErrorCode();
                if(errorCode==0){
                    List<Contact> contactList = new ArrayList<>();
                    List<Camera.CameraBean> list = model.getCamera();
                    for(Camera.CameraBean cameraBean : list){
                        Contact contact = new Contact();
                        contact.contactId = cameraBean.getCameraId();
                        contact.contactPassword = cameraBean.getCameraPwd();
                        contact.contactName = cameraBean.getCameraName();
                        contact.activeUser = cameraBean.getCameraAddress();
                        contactList.add(contact);
                    }
                    if(refresh){
                        mvpView.getDataRefresh(contactList);
                    }else{
                        mvpView.getDataSuccess(contactList);
                    }

                }
            }

            @Override
            public void onFailure(int code, String msg) {
            }

            @Override
            public void onCompleted() {
                mvpView.stopAnim();
            }
        }));
    }

    private void onKeyAlarm(String userId,String privilege,String cameraId,String info){
        Observable<PostResult> mObservable = apiStoreServer.textAlarm(userId,privilege,cameraId,info);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<PostResult>() {
            @Override
            public void onSuccess(PostResult model) {
                int errorCode = model.getErrorCode();
                if(errorCode==0){
                }
            }

            @Override
            public void onFailure(int code, String msg) {
            }

            @Override
            public void onCompleted() {
                mvpView.sendAlarmMessage("报警已发送");
            }
        }));
    }
}
