package com.hrsst.housekeeper.mvp.login;

import android.app.Activity;
import android.content.Context;

import com.hrsst.housekeeper.common.basePresenter.BasePresenter;
import com.hrsst.housekeeper.common.global.AccountPersist;
import com.hrsst.housekeeper.common.global.NpcCommon;
import com.hrsst.housekeeper.common.utils.SharedPreferencesManager;
import com.hrsst.housekeeper.common.utils.T;
import com.hrsst.housekeeper.common.utils.Utils;
import com.hrsst.housekeeper.entity.Account;
import com.hrsst.housekeeper.entity.LoginModel;
import com.hrsst.housekeeper.rxjava.ApiCallback;
import com.hrsst.housekeeper.rxjava.SubscriberCallBack;
import com.p2p.core.utils.MD5;
import com.p2p.core.utils.MyUtils;

import java.util.Random;

import rx.Observable;

/**
 * Created by Administrator on 2016/11/7.
 */
public class LoginPresenter extends BasePresenter<LoginView>{

    public LoginPresenter(Activity loginActivity,Activity splashActivity){
        if(loginActivity!=null){
            attachView((LoginActivity)loginActivity);
        }
        if(splashActivity!=null){
            attachView((SplashActivity)splashActivity);
        }
    }

    public void loginYooSee(final String User, final String Pwd, final Context context, final int type) {
        String AppVersion = MyUtils.getBitProcessingVersion();
        String userId = "+86-"+User;
        MD5 md = new MD5();
        String password = md.getMD5ofStr(Pwd);
        if(type==1){
            mvpView.showLoading();
        }
        Random random = new Random();
        int value = random.nextInt(4);
        Observable<LoginModel> observable = apiStore[value].loginYooSee(userId, password, "1", "3", AppVersion);
        addSubscription(observable,new SubscriberCallBack<>(new ApiCallback<LoginModel>() {
            @Override
            public void onSuccess(LoginModel model) {
                String errorCode = model.getError_code();
                if(errorCode.equals("0")){
                    editSharePreference(context,model,User,Pwd);
                    mvpView.getDataSuccess(model);
                }else{
                    switch (errorCode){
                        case "2":
                            T.showShort(context,"用户不存在");
                            break;
                        case "3":
                            T.showShort(context,"密码错误");
                            break;
                        case "9":
                            T.showShort(context,"用户名不能为空");
                            break;
                        default:
                            break;
                    }
                    if(type==0){
                        mvpView.autoLoginFail();
                    }
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.getDataFail("网络错误，请检查网络");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
//        final boolean[] result = {false};
//        twoSubscription(observable1, new Func1<LoginModel,Observable<LoginModel>>() {
//                    @Override
//                    public Observable<LoginModel> call(LoginModel loginModel) {
//                        if(loginModel.getError_code().equals("0")&&result[0]==false){
//                            result[0] =true;
//                            editSharePreference(context,loginModel,User,Pwd);
//                            return observable;
//                        }else{
//                            return null;
//                        }
//                    }
//                },
//                new SubscriberCallBack<>(new ApiCallback<LoginModel>() {
//                    @Override
//                    public void onSuccess(LoginModel model) {
//                        if(model!=null){
//                            String error_code = model.getError_code();
//                            switch (error_code){
//                                case "0":
//                                    mvpView.getDataSuccess(model);
//                                    break;
//                                case "2":
//                                    T.showShort(context,"用户不存在");
//                                    break;
//                                case "3":
//                                    T.showShort(context,"密码错误");
//                                    break;
//                                case "9":
//                                    T.showShort(context,"用户名不能为空");
//                                    break;
//                                default:
//                                    break;
//                            }
//                        }
//                    }
//                    @Override
//                    public void onFailure(int code, String msg) {
//                    }
//                    @Override
//                    public void onCompleted() {
//                        mvpView.hideLoading();
//                        if(result[0]==false){
//                            mvpView.getDataFail("网络错误，请检查网络");
//                        }
//                    }
//                }));
    }


    private void editSharePreference(Context mContext, LoginModel object, String userId, String userPwd){
        String userID = "0"+String.valueOf((Integer.parseInt(object.getUserID())&0x7fffffff));
        SharedPreferencesManager.getInstance().putData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTPASS,
                userPwd);
        SharedPreferencesManager.getInstance().putData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME,
                userId);
        SharedPreferencesManager.getInstance().putData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTPASS_NUMBER, userID);
        String codeStr1 = String.valueOf(Long.parseLong(object.getP2PVerifyCode1()));
        String codeStr2 = String.valueOf(Long.parseLong(object.getP2PVerifyCode2()));
        Account account = AccountPersist.getInstance()
                .getActiveAccountInfo(mContext);
        if (null == account) {
            account = new Account();
        }
        account.three_number = userID;
        account.phone = object.getPhoneNO();
        account.email = object.getEmail();
        account.sessionId = object.getSessionID();
        account.rCode1 = codeStr1;
        account.rCode2 = codeStr2;
        account.countryCode = object.getCountryCode();
        AccountPersist.getInstance()
                .setActiveAccount(mContext, account);
        NpcCommon.mThreeNum = AccountPersist.getInstance()
                .getActiveAccountInfo(mContext).three_number;
    }

    public void autoLogin(Activity activity){
        if(Utils.isNetworkAvailable(activity)){
            String userId = SharedPreferencesManager.getInstance().getData(activity, SharedPreferencesManager.SP_FILE_GWELL, SharedPreferencesManager.KEY_RECENTNAME);
            String userPwd = SharedPreferencesManager.getInstance().getData(activity, SharedPreferencesManager.SP_FILE_GWELL, SharedPreferencesManager.KEY_RECENTPASS);
            mvpView.autoLogin(userId,userPwd);
        }else {
            mvpView.autoLoginFail();
        }
    }
}
