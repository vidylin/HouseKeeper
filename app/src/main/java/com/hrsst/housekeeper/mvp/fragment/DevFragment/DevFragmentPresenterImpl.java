package com.hrsst.housekeeper.mvp.fragment.DevFragment;

import android.content.Context;

import com.hrsst.housekeeper.common.basePresenter.BasePresenter;
import com.hrsst.housekeeper.common.data.Contact;
import com.hrsst.housekeeper.common.global.Constants;
import com.hrsst.housekeeper.entity.Camera;
import com.hrsst.housekeeper.entity.PostResult;
import com.hrsst.housekeeper.rxjava.ApiCallback;
import com.hrsst.housekeeper.rxjava.SubscriberCallBack;
import com.p2p.core.P2PHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/11/18.
 */
public class DevFragmentPresenterImpl extends BasePresenter<DevFragmentView> implements DevFragmentPresenter{
    private DevFragmentView devFragmentView;

    public DevFragmentPresenterImpl(DevFragmentView devFragmentView) {
        this.devFragmentView = devFragmentView;
    }

    @Override
    public void setFriendStatus(List<Contact> lists, Map<String, Contact> contactMap) {
        List<Contact> contactList = new ArrayList<>();
        contactList.addAll(lists);
        if(contactMap!=null&&lists.size()==contactMap.size()){
            for(int i=0;i<contactMap.size();i++){
                Contact c = contactList.get(i);
                String id = c.contactId;
                Contact contact = contactMap.get(id);
                if(contact!=null){
                    c.onLineState = contact.onLineState;
                    c.contactType = contact.contactType;
                    lists.remove(i);
                    lists.add(i,c);
                }
            }
            devFragmentView.setCameraStatus(lists);
        }
    }

    @Override
    public void setDefence(Context mContext,String callId,String password,int defenceState) {
        // 设置布防
        if (defenceState == Constants.DefenceState.DEFENCE_STATE_ON) {
            P2PHandler.getInstance().setRemoteDefence(callId, password,
                    Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_OFF);

        } else if (defenceState == Constants.DefenceState.DEFENCE_STATE_OFF) {
            P2PHandler.getInstance().setRemoteDefence(callId, password,
                    Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_ON);
        }
    }

    @Override
    public void getFriendStatus(List<Contact> lists) {
        Observable.just(lists).map(new Func1<List<Contact>, Object>() {
            @Override
            public Object call(List<Contact> contacts) {
                String[] contactIds = new String[contacts.size()];
                int i = 0;
                for (Contact contact : contacts) {
                    contactIds[i] = contact.contactId;
                    i++;
                }
                P2PHandler.getInstance().getFriendStatus(contactIds);
                return null;
            }
        }).subscribe();
    }

    @Override
    public void getDefenceState(List<Contact> lists) {
        Observable.just(lists).map(new Func1<List<Contact>, Object>() {
            @Override
            public Object call(List<Contact> contacts) {
                for (Contact contact : contacts) {
                    P2PHandler.getInstance().getDefenceStates(contact.contactId, contact.contactPassword);
                }
                return null;
            }
        }).subscribe();
    }

    @Override
    public void setDefenceState(List<Contact> lists, Map<String, Integer> map) {
        List<Contact> contactList = new ArrayList<>();
        contactList.addAll(lists);
        if(map!=null&&lists.size()==map.size()){
            for(int i=0;i<map.size();i++){
                Contact c = contactList.get(i);
                String id = c.contactId;
                boolean containKey = map.containsKey(id);
                if(containKey){
                    int state = map.get(id);
                    c.defenceState = state;
                    lists.remove(i);
                    lists.add(i,c);
                }

            }
            devFragmentView.setDefenceState(lists);
        }
    }

    @Override
    public void deleteUserIdCameraId(String userId, final Contact data) {
        devFragmentView.showLoading();
        Observable<PostResult> mObservable = apiStoreServer.deleteUserIdCameraId(userId,data.contactId);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<PostResult>() {
            @Override
            public void onSuccess(PostResult model) {
                int errorCode = model.getErrorCode();
                if(errorCode==0){
                    devFragmentView.deleteUserIdCameraIdSuccess(data,"删除成功");
                }else{
                    devFragmentView.deleteUserIdCameraIdResult("删除失败");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                devFragmentView.deleteUserIdCameraIdResult("网络错误");
            }

            @Override
            public void onCompleted() {
                devFragmentView.hideLoading();
            }
        }));
    }

    @Override
    public void getAllCamera(String userId, String privilege, String page, final boolean push){
        if(!push){
            devFragmentView.showLoading();
        }
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
                    if(!push){
                        devFragmentView.getAllCameraResult(contactList);
                    }else{
                        devFragmentView.pushResult(contactList);
                    }
                }else{
                    List<Contact> contactList = new ArrayList<>();
                    devFragmentView.getAllCameraResult(contactList);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                devFragmentView.deleteUserIdCameraIdResult("网络错误");
            }

            @Override
            public void onCompleted() {
                devFragmentView.hideLoading();
            }
        }));
    }

}
