package com.hrsst.housekeeper.mvp.manualAddCamera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.hrsst.housekeeper.common.basePresenter.BasePresenter;
import com.hrsst.housekeeper.common.data.Contact;
import com.hrsst.housekeeper.common.global.Constants;
import com.hrsst.housekeeper.common.global.NpcCommon;
import com.hrsst.housekeeper.common.utils.SharedPreferencesManager;
import com.hrsst.housekeeper.entity.PostResult;
import com.hrsst.housekeeper.rxjava.ApiCallback;
import com.hrsst.housekeeper.rxjava.SubscriberCallBack;
import com.p2p.core.P2PHandler;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/11/24.
 */
public class ManualAddCameraPresenter extends BasePresenter<ManualAddCameraView>{
    private ManualAddCameraActivity manualAddCameraActivity;

    public ManualAddCameraPresenter(ManualAddCameraActivity manualAddCameraActivity){
        this.manualAddCameraActivity = manualAddCameraActivity;
    }

    public void checkPassword(String cameraId,String pwd){
        if(cameraId==null||cameraId.length()==0||pwd==null||pwd.length()==0){
            return;
        }
        Contact contact = new Contact();
        contact.contactId = cameraId;
        contact.contactPassword = pwd;
        manualAddCameraActivity.showLoading();
        Observable.just(contact).map(new Func1<Contact, Object>() {
            @Override
            public Object call(Contact mContact) {
                P2PHandler.getInstance().getDefenceStates(mContact.contactId, mContact.contactPassword);
                regFilter();
                return null;
            }
        }).subscribe();
    }

    private void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.P2P.RET_GET_REMOTE_DEFENCE);
        filter.addAction(Constants.P2P.RET_GET_BIND_ALARM_ID);
        filter.addAction(Constants.P2P.ACK_RET_SET_BIND_ALARM_ID);
        manualAddCameraActivity.registerReceiver(mReceiver, filter);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constants.P2P.ACK_RET_SET_BIND_ALARM_ID)){
                if(mReceiver!=null){
                    manualAddCameraActivity.unregisterReceiver(mReceiver);
                    mReceiver=null;
                }
                manualAddCameraActivity.hideLoading();
                manualAddCameraActivity.finishActivity();
            }
            if(intent.getAction().equals(Constants.P2P.RET_GET_BIND_ALARM_ID)){
                String[] data = intent.getStringArrayExtra("data");
                int max_count = intent.getIntExtra("max_count", 0);
                String[] last_bind_data = data;
                int max_alarm_count = max_count;
                int count = 0;
                for (int i = 0; i < data.length; i++) {
                    if (data[i].equals(NpcCommon.mThreeNum)) {
                        count = count + 1;
                        if(mReceiver!=null){
                            manualAddCameraActivity.unregisterReceiver(mReceiver);
                            mReceiver=null;
                        }
                        manualAddCameraActivity.hideLoading();
                        manualAddCameraActivity.finishActivity();
                        return;
                    }
                }
                if (count == 0) {
                    if (last_bind_data.length >= max_alarm_count) {
                        manualAddCameraActivity.errorMessage("报警接收人数已达上限");
                        if(mReceiver!=null){
                            manualAddCameraActivity.unregisterReceiver(mReceiver);
                            mReceiver=null;
                        }
                        manualAddCameraActivity.hideLoading();
                        manualAddCameraActivity.finishActivity();
                        return;
                    }
                    String[] new_data = new String[last_bind_data.length + 1];
                    for (int i = 0; i < last_bind_data.length; i++) {
                        new_data[i] = last_bind_data[i];
                    }
                    new_data[new_data.length - 1] = NpcCommon.mThreeNum;
                    manualAddCameraActivity.bindAlarm(new_data);
                }
            }
            if (intent.getAction().equals(Constants.P2P.RET_GET_REMOTE_DEFENCE)) {
                String id = intent.getExtras().getString("contactId");
                int state = intent.getExtras().getInt("state");
                switch (state){
                    case 3:
                        manualAddCameraActivity.errorMessage("摄像机密码错误，请重新输入密码");
                        manualAddCameraActivity.hideLoading();
                        break;
                    case 4:
                        manualAddCameraActivity.errorMessage("摄像机处于离线状态，请把摄像机联网");
                        manualAddCameraActivity.hideLoading();
                        break;
                    default:
                        String userNumber = SharedPreferencesManager.getInstance().getData(manualAddCameraActivity, SharedPreferencesManager.SP_FILE_GWELL,
                                SharedPreferencesManager.KEY_RECENTPASS_NUMBER);
                        addCamera(userNumber, id);
                        break;
                }
            }
        }
    };

    private void addCamera(String userId,String cameraId){
        Observable<PostResult> mObservable = apiStoreServer.bindUserIdCameraId(userId,cameraId);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<PostResult>() {
            @Override
            public void onSuccess(PostResult model) {
                int errorCode = model.getErrorCode();
                if(errorCode==0){
                    manualAddCameraActivity.addCameraResult("添加成功");
                }else if(errorCode==3){
                    manualAddCameraActivity.hideLoading();
                    manualAddCameraActivity.errorMessage("摄像机已存在，请勿重复添加");
                }else{
                    manualAddCameraActivity.hideLoading();
                    manualAddCameraActivity.errorMessage("添加失败，请重新添加");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                manualAddCameraActivity.hideLoading();
                manualAddCameraActivity.errorMessage("网络错误，请重新添加");
            }

            @Override
            public void onCompleted() {
            }
        }));
    }

    public void getAlarmId(String contactId,String contactPassword){
        P2PHandler.getInstance().getBindAlarmId(contactId,contactPassword);
    }
    
    public void bindAlarmId(String[] new_data,String cameraID,String pwd){
        P2PHandler.getInstance().setBindAlarmId(cameraID, pwd, new_data.length, new_data);
    }

    public void unRegisterReceiver(){
        if(mReceiver!=null){
            manualAddCameraActivity.unregisterReceiver(mReceiver);
            mReceiver=null;
        }
    }
}
