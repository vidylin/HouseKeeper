package com.hrsst.housekeeper.mvp.register;

import android.content.Context;

import com.hrsst.housekeeper.common.basePresenter.BasePresenter;
import com.hrsst.housekeeper.common.utils.SharedPreferencesManager;
import com.hrsst.housekeeper.entity.PostResult;
import com.hrsst.housekeeper.entity.RegisterModel;
import com.hrsst.housekeeper.rxjava.ApiCallback;
import com.hrsst.housekeeper.rxjava.SubscriberCallBack;
import com.p2p.core.utils.MD5;
import com.p2p.core.utils.MyUtils;

import java.util.Random;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/11/7.
 */
public class RegisterPhonePresenter extends BasePresenter<RegisterPhoneView>{

    public RegisterPhonePresenter(RegisterPhoneActivity registerPhoneActivity){
            attachView(registerPhoneActivity);
    }

    public void getMessageCode(String phoneNum){
        String AppVersion = MyUtils.getBitProcessingVersion();
        mvpView.showLoading();
        Random random = new Random();
        int value = random.nextInt(4);
        Observable<RegisterModel> observable = apiStore[value].getMesageCode("86", phoneNum,AppVersion);
        addSubscription(observable,
                new SubscriberCallBack<>(new ApiCallback<RegisterModel>() {
                    @Override
                    public void onSuccess(RegisterModel model) {
                        String errorCode = model.getError_code();
                        switch (errorCode){
                            case "0":
                                mvpView.getMessageSuccess();
                                break;
                            case "6":
                                mvpView.hideLoading();
                                mvpView.getDataFail("手机号已被注册");
                                break;
                            case "9":
                                mvpView.hideLoading();
                                mvpView.getDataFail("手机号码格式错误");
                                break;
                            case "27":
                                mvpView.hideLoading();
                                mvpView.getDataFail("获取手机验证码超时，请稍后再试");
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
                        mvpView.hideLoading();
                    }
                }));
    }

    public void register(final String phoneNo, final String pwd, String rePwd, final String code, final Context mContext){
        MD5 md = new MD5();
        final String password = md.getMD5ofStr(pwd);
        final String rePassword = md.getMD5ofStr(rePwd);
        mvpView.showLoading();
        Random random = new Random();
        final int value = random.nextInt(4);
        Observable<RegisterModel> observable = apiStore[value].verifyPhoneCode("86", phoneNo,code);
        twoSubscription(observable,new Func1<RegisterModel, Observable<RegisterModel>>(){
                    @Override
                    public Observable<RegisterModel> call(RegisterModel registerModel) {
                        return apiStore[value].register("1","","86",phoneNo,password,rePassword,code,"1");
                    }
                },
                new SubscriberCallBack<>(new ApiCallback<RegisterModel>() {
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
                                        SharedPreferencesManager.KEY_RECENTNAME,
                                        phoneNo);
                                SharedPreferencesManager.getInstance().putData(mContext,
                                        SharedPreferencesManager.SP_FILE_GWELL,
                                        SharedPreferencesManager.KEY_RECENTPASS_NUMBER, userID);
                                registerToServer(userID,"",phoneNo,pwd,"1");
                                break;
                            case "6":
                                mvpView.hideLoading();
                                mvpView.getDataFail("手机号已被注册");
                                break;
                            case "9":
                                mvpView.hideLoading();
                                mvpView.getDataFail("手机号码格式错误");
                                break;
                            case "18":
                                mvpView.hideLoading();
                                mvpView.getDataFail("验证码输入错误");
                            case "10":
                                mvpView.hideLoading();
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
                    mvpView.getDataFail("注册失败");
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
