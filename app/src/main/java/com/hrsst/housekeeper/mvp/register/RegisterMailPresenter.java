package com.hrsst.housekeeper.mvp.register;

import android.content.Context;

import com.hrsst.housekeeper.common.basePresenter.BasePresenter;
import com.hrsst.housekeeper.common.utils.SharedPreferencesManager;
import com.hrsst.housekeeper.common.utils.Utils;
import com.hrsst.housekeeper.entity.PostResult;
import com.hrsst.housekeeper.entity.RegisterModel;
import com.hrsst.housekeeper.rxjava.ApiCallback;
import com.hrsst.housekeeper.rxjava.SubscriberCallBack;
import com.p2p.core.utils.MD5;

import java.util.Random;

import rx.Observable;

/**
 * Created by Administrator on 2016/11/7.
 */
public class RegisterMailPresenter extends BasePresenter<RegisterPhoneView>{

    public RegisterMailPresenter(RegisterEmailActivity registerPhoneActivity){
            attachView(registerPhoneActivity);
    }

    public void registerEmail(final String phoneNo, final String pwd, String rePwd, final Context mContext){
        boolean isEmail = Utils.isEmial(phoneNo);
        if(isEmail){
            mvpView.showLoading();
            MD5 md = new MD5();
            final String password = md.getMD5ofStr(pwd);
            final String rePassword = md.getMD5ofStr(rePwd);
            Random random = new Random();
            final int value = random.nextInt(4);
            Observable<RegisterModel> observable = apiStore[value].register("1",phoneNo,"","",password,rePassword,"","1");
            addSubscription(observable,new SubscriberCallBack<>(new ApiCallback<RegisterModel>() {
                @Override
                public void onSuccess(RegisterModel model) {
                    String errorCode = model.getError_code();
                    switch (errorCode){
                        case "0":
                            String userID = "0"+String.valueOf((Integer.parseInt(model.getUserID())&0x7fffffff));
                            SharedPreferencesManager.getInstance().putData(mContext,
                                    SharedPreferencesManager.SP_FILE_GWELL,
                                    SharedPreferencesManager.KEY_RECENTPASS,
                                    pwd);
                            SharedPreferencesManager.getInstance().putData(mContext,
                                    SharedPreferencesManager.SP_FILE_GWELL,
                                    SharedPreferencesManager.KEY_RECENTNAME_EMAIL,
                                    phoneNo);
                            SharedPreferencesManager.getInstance().putData(mContext,
                                    SharedPreferencesManager.SP_FILE_GWELL,
                                    SharedPreferencesManager.KEY_RECENTPASS_NUMBER, userID);
                            registerToServer(userID,"","",phoneNo,1+"");
                            break;
                        case "7":
                            mvpView.getDataFail("邮箱已被注册");
                            break;
                        case "4":
                            mvpView.getDataFail("邮箱格式不符合要求");
                            break;
                        case "10":
                            mvpView.getDataFail("两次输入的密码不一致");
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onFailure(int code, String msg) {
                    mvpView.getDataFail(msg);
                }

                @Override
                public void onCompleted() {

                }
            }));
        }else{
            mvpView.getDataFail("邮箱格式不正确");
        }
    }

    private void registerToServer(String userId,String userName,String phone,String email,String privilege){
        Observable<PostResult> observable = apiStoreServer.registerServerIp(userId,userName,phone,email,privilege);
        addSubscription(observable,new SubscriberCallBack<>(new ApiCallback<PostResult>() {
            @Override
            public void onSuccess(PostResult model) {
                int errorCode = model.getErrorCode();
                if(errorCode==0){
                    mvpView.register();
                }else{
                    mvpView.getDataFail("注册失败，请换个邮箱重新注册");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.getDataFail(msg);
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }
}
