package com.hrsst.housekeeper.mvp.defenceList;

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

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by Administrator on 2016/11/28.
 */

public class DefenceListPresenter extends BasePresenter<DefenceListView>{
    private DefenceListActivity defenceListActivity;
    private Contact contact;
    private Defence.DefenceBean defenceBean;
    private String newName;

    public DefenceListPresenter(DefenceListActivity defenceListActivity){
        this.defenceListActivity = defenceListActivity;
        attachView(defenceListActivity);
        regFilter();
    }

    public void getDefenceArea(){
        P2PHandler.getInstance().getDefenceArea(contact.contactId, contact.contactPassword);
    }

    public void regFilter(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.P2P.RET_GET_DEFENCE_AREA);
        filter.addAction(Constants.P2P.RET_SET_DEFENCE_AREA);
        defenceListActivity.registerReceiver(mReceiver, filter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constants.P2P.RET_GET_DEFENCE_AREA)){
                @SuppressWarnings("unchecked")
                ArrayList<int[]> data = (ArrayList<int[]>) intent.getSerializableExtra("data");
                Defence defence = initData(data);
                mvpView.getDefenceArea(defence);
            }

            if(intent.getAction().equals(Constants.P2P.RET_SET_DEFENCE_AREA)){
                int result = intent.getIntExtra("result", -1);
                //学习成功
                if(result==Constants.P2P_SET.DEFENCE_AREA_SET.SETTING_SUCCESS){
                    deleteDefence();
                }else{
                    mvpView.errorMessage("删除失败，请重新再试");
                    mvpView.hideLoading();
                }
            }
        }
    };

    private Defence initData(ArrayList<int[]> data){
        Defence defence = new Defence();
        outTerLoop:for(int i=1;i<9;i++){
            int [] ints = data.get(i);
            for(int j=0;j<9;j++){
                int state = ints[j];
                if(state==1){
                    defence.setArea(i);
                    defence.setChannel(j);
                    break outTerLoop;
                }
            }
        }
        return defence;
    }

    public void getDefenceFromServer(Contact contact, String page, final boolean refresh){
        this.contact = contact;
        if(!refresh){
            mvpView.showLoading();
        }
        Observable<Defence> observable = apiStoreServer.getAllDefence(contact.contactId,page);
        addSubscription(observable,new SubscriberCallBack<>(new ApiCallback<Defence>() {
            @Override
            public void onSuccess(Defence model) {
                int errorCode = model.getErrorCode();
                if(errorCode==0){
                    if(refresh){
                        mvpView.refresh(model);
                    }else{
                        mvpView.getDefenceResult("",model);
                    }

                }else{
                    Defence defence = new Defence();
                    mvpView.getDefenceResult("",defence);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.errorMessage("网络错误，请稍后再试");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }

    public void unReceiver(){
        if(mReceiver!=null){
            defenceListActivity.unregisterReceiver(mReceiver);
            mReceiver=null;
        }
    }

    private void deleteDefence(){
        Observable<PostResult> observable = apiStoreServer.deleteCameraSensor(defenceBean.getDefenceId(),contact.contactId);
        addSubscription(observable,new SubscriberCallBack<>(new ApiCallback<PostResult>() {
            @Override
            public void onSuccess(PostResult model) {
                int errorCode = model.getErrorCode();
                if(errorCode==0){
                    mvpView.deleteDefenceResult(defenceBean);
                    mvpView.errorMessage("删除成功");
                }else{
                    mvpView.errorMessage("删除失败");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.errorMessage("网络错误，请稍后再试");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }

    public void deleteDefenceDefence(Defence.DefenceBean defenceBean){
        mvpView.showLoading();
        this.defenceBean = defenceBean;
        int areaId = Integer.parseInt(defenceBean.getDefenceId().substring(0,1));
        int channel = Integer.parseInt(defenceBean.getDefenceId().substring(1,2));
        P2PHandler.getInstance().setDefenceAreaState(contact.contactId,
                contact.contactPassword,
                areaId,
                channel,
                Constants.P2P_SET.DEFENCE_AREA_SET.DEFENCE_AREA_TYPE_CLEAR);
    }

    public void getNewName(String newName){
        this.newName = newName;
    }

    public void modifyDefenceName(final Defence.DefenceBean defenceBean){
        if(newName==null||newName.length()<=0){
            mvpView.errorMessage("请输入新的防区名称");
            return;
        }
        mvpView.showLoading();
        Observable<PostResult> observable = apiStoreServer.changeDefenceName(contact.contactId,defenceBean.getDefenceId(),newName);
        addSubscription(observable,new SubscriberCallBack<>(new ApiCallback<PostResult>() {
            @Override
            public void onSuccess(PostResult model) {
                int errorCode = model.getErrorCode();
                if(errorCode==0){
                    defenceBean.setDefenceName(newName);
                    mvpView.modifyDefenceNameResult(defenceBean);
                    mvpView.errorMessage("修改成功");
                }else{
                    mvpView.errorMessage("修改失败");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.errorMessage("网络错误，请稍后再试");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }
}
