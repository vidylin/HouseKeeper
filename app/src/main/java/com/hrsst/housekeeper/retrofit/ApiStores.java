package com.hrsst.housekeeper.retrofit;

import com.hrsst.housekeeper.entity.LoginModel;
import com.hrsst.housekeeper.entity.RegisterModel;
import com.hrsst.housekeeper.entity.VersionXml;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiStores {
    //登录技威服务器
    @POST("Users/LoginCheck.ashx")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<LoginModel> loginYooSee(@Query("User") String User, @Query("Pwd") String Pwd,
                                       @Query("VersionFlag") String VersionFlag, @Query("AppOS") String AppOS,
                                       @Query("AppVersion") String AppVersion);
    //登录本地服务器
    @GET("login")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<LoginModel> login(@Query("userId") String userId);

    //获取短信验证码
    @POST("Users/PhoneCheckCode.ashx")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<RegisterModel> getMesageCode(@Query("CountryCode") String countryCode, @Query("PhoneNO") String phoneNO
            , @Query("AppVersion") String appVersion);

    //检查短信验证码
    @POST("Users/PhoneVerifyCodeCheck.ashx")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<RegisterModel> verifyPhoneCode(@Query("CountryCode") String countryCode, @Query("PhoneNO") String phoneNO
            , @Query("VerifyCode") String verifyCode);

    //注册
    @POST("Users/RegisterCheck.ashx")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<RegisterModel> register(@Query("VersionFlag") String versionFlag, @Query("Email") String email
            , @Query("CountryCode") String countryCode, @Query("PhoneNO") String phoneNO
            , @Query("Pwd") String pwd, @Query("RePwd") String rePwd
            , @Query("VerifyCode") String verifyCode, @Query("IgnoreSafeWarning") String ignoreSafeWarning);

    @GET("update.xml")
    Observable<VersionXml> checkVersion();
}
