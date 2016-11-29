package com.hrsst.housekeeper.mvp.fragment.AlarmMsg;

import com.hrsst.housekeeper.common.basePresenter.BasePresenter;
import com.hrsst.housekeeper.entity.AlarmMsg;
import com.hrsst.housekeeper.rxjava.ApiCallback;
import com.hrsst.housekeeper.rxjava.SubscriberCallBack;

import java.util.List;

import rx.Observable;

/**
 * Created by Administrator on 2016/11/29.
 */
public class AlarmMsgPresenter extends BasePresenter<AlarmMsgView>{

    public AlarmMsgPresenter(AlarmMsgFragment alarmMsgFragment){
        attachView(alarmMsgFragment);
    }

    public void getAllAlarmMsg(String userId,String privilege,String page,boolean loadMore){
        if(!loadMore){
            mvpView.showLoading();
        }
        Observable<AlarmMsg> observable = apiStoreServer.getAllAlarm(userId,privilege,page);
        addSubscription(observable,new SubscriberCallBack<>(new ApiCallback<AlarmMsg>() {
            @Override
            public void onSuccess(AlarmMsg model) {
                int errorCode = model.getErrorCode();
                if(errorCode==0){
                    List<AlarmMsg.AlarmBean> alarmBeanList = model.getAlarm();
                    mvpView.getAllMsg(alarmBeanList);
                }else{
                    mvpView.errorMsg("无数据");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.errorMsg("网络错误，请稍后再试");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }
}
