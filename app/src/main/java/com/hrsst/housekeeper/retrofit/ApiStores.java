package com.hrsst.housekeeper.retrofit;

import com.hrsst.housekeeper.entity.LoginModel;
import com.hrsst.housekeeper.entity.LoginServer;
import com.hrsst.housekeeper.entity.PostResult;
import com.hrsst.housekeeper.entity.RegisterModel;
import com.hrsst.housekeeper.entity.VersionXml;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiStores {
    //登录技威服务器
    @FormUrlEncoded
    @POST("Users/LoginCheck.ashx")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<LoginModel> loginYooSee(@Field("User") String User, @Field("Pwd") String Pwd,
                                       @Field("VersionFlag") String VersionFlag, @Field("AppOS") String AppOS,
                                       @Field("AppVersion") String AppVersion);
    //登录本地服务器
    @GET("login")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<LoginServer> loginServer(@Query("userId") String userId, @Query("phone") String phone, @Query("email") String email);

    //获取短信验证码
    @FormUrlEncoded
    @POST("Users/PhoneCheckCode.ashx")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<RegisterModel> getMesageCode(@Field("CountryCode") String countryCode, @Field("PhoneNO") String phoneNO
            , @Field("AppVersion") String appVersion);

    //检查短信验证码
    @FormUrlEncoded
    @POST("Users/PhoneVerifyCodeCheck.ashx")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<RegisterModel> verifyPhoneCode(@Field("CountryCode") String countryCode, @Field("PhoneNO") String phoneNO
            , @Field("VerifyCode") String verifyCode);

    //注册
    @FormUrlEncoded
    @POST("Users/RegisterCheck.ashx")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<RegisterModel> register(@Field("VersionFlag") String versionFlag, @Field("Email") String email
            , @Field("CountryCode") String countryCode, @Field("PhoneNO") String phoneNO
            , @Field("Pwd") String pwd, @Field("RePwd") String rePwd
            , @Field("VerifyCode") String verifyCode, @Field("IgnoreSafeWarning") String ignoreSafeWarning);

    @GET("update.xml")
    Observable<VersionXml> checkVersion();

    @FormUrlEncoded
    @POST("register")
    Observable<PostResult> registerServerIp(@Field("userId") String userId, @Field("userName") String userName
            , @Field("phone") String phone, @Field("email") String email, @Field("privilege") String pwd);
//    addCamera
//(cameraId=1&cameraName=2&cameraPwd=3&cameraAddress=4&longitude=5&latitude=6&principal1=7&principal1Phone=8&principal2=9&principal2Phone=10&areaId=11&placeTypeId=12)
    @FormUrlEncoded
    @POST("addCamera")
    Observable<PostResult> addCamera(@Field("cameraId") String cameraId, @Field("cameraName") String cameraName
            , @Field("cameraPwd") String cameraPwd, @Field("cameraAddress") String cameraAddress, @Field("longitude") String longitude
            ,@Field("latitude") String latitude, @Field("principal1") String principal1, @Field("principal1Phone") String principal1Phone,
             @Field("principal2") String principal2, @Field("principal2Phone") String principal2Phone, @Field("areaId") String areaId,
             @Field("placeTypeId") String placeTypeId);
}
