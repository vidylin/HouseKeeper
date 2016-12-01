package com.hrsst.housekeeper.mvp.fragment.OneKeyAlarmFragment;

import android.os.Handler;
import android.os.Message;

import com.hrsst.housekeeper.common.basePresenter.BasePresenter;
import com.hrsst.housekeeper.common.data.Contact;
import com.hrsst.housekeeper.entity.Camera;
import com.hrsst.housekeeper.rxjava.ApiCallback;
import com.hrsst.housekeeper.rxjava.SubscriberCallBack;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;
import rx.Subscription;

/**
 * Created by Administrator on 2016/11/10.
 */
public class OneKeyAlarmPresenter extends BasePresenter<OneKeyAlarmView>{
    private Subscription mSubscription;
    private Timer timer;

    public OneKeyAlarmPresenter(OneKeyAlarmFragment oneKeyAlarmFragment){
        attachView(oneKeyAlarmFragment);
    }

    int countNum=0;
    public void startTimer(){
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

    public void stopCountDown(){
        if(mSubscription!=null){
            if(!mSubscription.isUnsubscribed()){
                mSubscription.unsubscribe();
                mSubscription=null;
                mvpView.stopCountDown("已取消报警");
            }
        }
    }

    public void getAllCamera(String userId, String privilege, String page){
        Observable<Camera> mObservable = apiStoreServer.ordinaryUserGetAllCamera(userId,privilege,page);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<Camera>() {
            @Override
            public void onSuccess(Camera model) {
                int errorCode = model.getErrorCode();
                if(errorCode==0){
                    List<Contact> contactList = new ArrayList<Contact>();
                    List<Camera.CameraBean> list = model.getCamera();
                    for(Camera.CameraBean cameraBean : list){
                        Contact contact = new Contact();
                        contact.contactId = cameraBean.getCameraId();
                        contact.contactPassword = cameraBean.getCameraPwd();
                        contact.contactName = cameraBean.getCameraName();
                        contactList.add(contact);
                    }
                    mvpView.getDataSuccess(contactList);
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
