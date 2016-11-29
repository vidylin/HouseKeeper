package com.hrsst.housekeeper.mvp.defence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.hrsst.housekeeper.common.basePresenter.BasePresenter;
import com.hrsst.housekeeper.common.data.Contact;
import com.hrsst.housekeeper.common.global.Constants;
import com.hrsst.housekeeper.entity.Defence;
import com.hrsst.housekeeper.entity.PostResult;
import com.hrsst.housekeeper.rxjava.ApiCallback;
import com.hrsst.housekeeper.rxjava.SubscriberCallBack;
import com.p2p.core.P2PHandler;

import rx.Observable;

/**
 * Created by Administrator on 2016/11/28.
 */
public class DefencePresenter extends BasePresenter<DefenceView>{
    private DefenceActivity defenceActivity;
    private Defence defence;
    private String yzwNumber;
    private Contact contact;
    private String clickStr;
    private int current_type;

    public DefencePresenter(DefenceActivity defenceActivity){
        this.defenceActivity = defenceActivity;
        attachView(defenceActivity);
        regFilter();
    }

    public void regFilter(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.P2P.RET_SET_DEFENCE_AREA);
        filter.addAction(Constants.P2P.MESG_TYPE_RET_ALARM_TYPE_MOTOR_PRESET_POS);
        defenceActivity.registerReceiver(mReceiver, filter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constants.P2P.RET_SET_DEFENCE_AREA)){
                int result = intent.getIntExtra("result", -1);
                //学习成功
                if(result==Constants.P2P_SET.DEFENCE_AREA_SET.SETTING_SUCCESS){
                    if(current_type == Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_LEARN){
                        int areaId = defence.getArea();
                        String defenceId = areaId+""+defence.getChannel()+"";
                        if(areaId==1&&yzwNumber!=null&&yzwNumber.length()>0){
                            binderPreset(1,defence.getChannel(),Integer.parseInt(yzwNumber));
                        }else{
                            bindDefence(defenceId,"测试","1",contact.contactId);
                        }
                    }else{
                        mvpView.studyErrorResult("添加失败");
                    }

                }else{
                    mvpView.hideLoading();
                    mvpView.studyErrorResult("添加失败");
                }
            }
            if(intent.getAction().equals(Constants.P2P.MESG_TYPE_RET_ALARM_TYPE_MOTOR_PRESET_POS)){
                byte[] result = intent.getByteArrayExtra("result");
                if(0==(result[1]&0xff)){
                    int areaId = defence.getArea();
                    String defenceId = areaId+""+defence.getChannel()+"";
                    bindDefence(defenceId,"防区"+defenceId,clickStr,contact.contactId);
                }else{
                    failedBind(defence);
                }
            }
        }
    };

    public void setDefence(String yzwNumber,Contact contact,Defence defence,String clickStr){
        current_type = Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_LEARN;
        this.contact = contact;
        this.yzwNumber = yzwNumber;
        this.defence = defence;
        this.clickStr = clickStr;
        mvpView.showLoading();
        P2PHandler.getInstance().setDefenceAreaState(contact.contactId,
                contact.contactPassword,
                defence.getArea(), defence.getChannel() ,
                Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_LEARN);
    }

    private void binderPreset(int areaID,int channelID,int yzwNum){
        byte bPresetNum = (byte) (yzwNum-1);
        byte[] datas = new byte[7];
        datas[0] = 90;
        datas[1] = 0;
        datas[2] = 1;
        datas[3] = 0;
        datas[4] = (byte) (areaID-1);
        datas[5] = (byte) channelID;
        datas[6] = bPresetNum;
        P2PHandler.getInstance().sMesgSetAlarmPresetMotorPos(contact.contactId,contact.contactPassword,datas);
    }

    public void bindDefence(String defenceId,String defenceName,String sensorId,String cameraId){
        mvpView.showLoading();
        Observable<PostResult> observable = apiStoreServer.bindCameraSensor(defenceId,defenceName,sensorId,cameraId);
        addSubscription(observable,new SubscriberCallBack<>(new ApiCallback<PostResult>() {
            @Override
            public void onSuccess(PostResult model) {
                int errorCode = model.getErrorCode();
                if(errorCode==0){
                    mvpView.studySuccessResult("添加成功");
                }else{
                    failedBind(defence);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.studyErrorResult("网络错误，请稍后再试");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }

    private void failedBind(Defence defence){
        current_type=Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_CLEAR;
        P2PHandler.getInstance().setDefenceAreaState(contact.contactId,
                contact.contactPassword,
                defence.getArea(),
                defence.getChannel(),
                Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_CLEAR);
    }

    public void unReceiver(){
        if(mReceiver!=null){
            defenceActivity.unregisterReceiver(mReceiver);
            mReceiver=null;
        }
    }

}
