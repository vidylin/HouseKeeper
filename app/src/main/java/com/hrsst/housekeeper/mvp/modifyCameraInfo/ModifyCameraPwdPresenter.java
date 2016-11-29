package com.hrsst.housekeeper.mvp.modifyCameraInfo;

import com.hrsst.housekeeper.common.basePresenter.BasePresenter;
import com.hrsst.housekeeper.entity.PostResult;
import com.hrsst.housekeeper.rxjava.ApiCallback;
import com.hrsst.housekeeper.rxjava.SubscriberCallBack;
import com.p2p.core.P2PHandler;

import rx.Observable;

/**
 * Created by Administrator on 2016/11/24.
 */
public class ModifyCameraPwdPresenter extends BasePresenter<ModifyCameraPwdView>{

    public ModifyCameraPwdPresenter(ModifyCameraPwdActivity modifyCameraPwdActivity){
        attachView(modifyCameraPwdActivity);
    }

    public void ModifyCameraPwd(String cameraId,String exPwd,String newPwd,String newPwd2){
        if ("".equals(exPwd.trim())) {
            mvpView.errorMessage("请输入摄像机旧的密码");
            return;
        }
        if (exPwd.length() >10) {
            mvpView.errorMessage("原密码长度不能超过10个字符");
            return;
        }
        if ("".equals(newPwd.trim())) {
            mvpView.errorMessage("请输入新的密码");
            return;
        }
        if (newPwd.length() >10) {
            mvpView.errorMessage("新密码长度不能超过10个字符");
            return;
        }
        if ("".equals(newPwd2.trim())) {
            mvpView.errorMessage("请再输入一次密码");
            return;
        }
        if (!newPwd2.equals(newPwd)) {
            mvpView.errorMessage("您两次输入的密码不一致");
            return;
        }
        if (newPwd.length() < 6) {
            mvpView.errorMessage("密码太简单");
            return;
        }
        mvpView.showLoading();
        String pwdStr=P2PHandler.getInstance().EntryPassword(newPwd);
        String exPwdStr = P2PHandler.getInstance().EntryPassword(exPwd);
        P2PHandler.getInstance().setDevicePassword(cameraId, exPwdStr, pwdStr);
    }

    public void modifyCameraPwdToServer(String cameraId, final String pwd){
        Observable<PostResult> observable = apiStoreServer.changeCameraPwd(cameraId,pwd);
        addSubscription(observable,new SubscriberCallBack<>(new ApiCallback<PostResult>() {
            @Override
            public void onSuccess(PostResult model) {
                int errorCode = model.getErrorCode();
                if(errorCode==0){
                    mvpView.modifyCameraPwdResult("修改成功",pwd);
                }else{
                    mvpView.errorMessage("修改失败");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.errorMessage("网络错误");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }
}
