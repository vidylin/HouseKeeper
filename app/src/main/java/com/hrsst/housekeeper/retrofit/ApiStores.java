package com.hrsst.housekeeper.retrofit;

import com.hrsst.housekeeper.entity.AlarmCameraInfo;
import com.hrsst.housekeeper.entity.AlarmMsg;
import com.hrsst.housekeeper.entity.Camera;
import com.hrsst.housekeeper.entity.Defence;
import com.hrsst.housekeeper.entity.HttpAreaResult;
import com.hrsst.housekeeper.entity.HttpError;
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

    @GET("update_bees_normal.xml")
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

    //bindUserIdCameraId  userId=zhenweihao&cameraId=1)
    @FormUrlEncoded
    @POST("bindUserIdCameraId")
    Observable<PostResult> bindUserIdCameraId(@Field("userId") String userId, @Field("cameraId") String cameraId);

    //deleteUserIdCameraId 用户摄像头解绑 userId=zhenweihao&cameraId=1)
    @FormUrlEncoded
    @POST("deleteUserIdCameraId")
    Observable<PostResult> deleteUserIdCameraId(@Field("userId") String userId, @Field("cameraId") String cameraId);

    //    普通用户获取摄像头 ordinaryUserGetAllCamera?userId=13428282520&privilege=1&page=1
    @GET("ordinaryUserGetAllCamera")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<Camera> ordinaryUserGetAllCamera(@Query("userId") String userId, @Query("privilege") String privilege, @Query("page") String page);

//    changeCameraPwd （cameraId=3121164&cameraPwd=npwd)
    @FormUrlEncoded
    @POST("changeCameraPwd")
    Observable<PostResult> changeCameraPwd(@Field("cameraId") String cameraId, @Field("cameraPwd") String cameraPwd);

    //获取所有的店铺类型
    @GET("getPlaceTypeId")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> getPlaceTypeId(@Query("userId") String userId, @Query("privilege") String privilege, @Query("page") String page);

    //获取所有的区域类型
    @GET("getAreaId")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpAreaResult> getAreaId(@Query("userId") String userId, @Query("privilege") String privilege, @Query("page") String page);

//    changeCameraName （cameraId=3121164&cameraName=camera_3121164)
    @FormUrlEncoded
    @POST("changeCameraName")
    Observable<PostResult> changeCameraName(@Field("cameraId") String cameraId, @Field("cameraName") String cameraName);
//    http://192.168.4.111:51091/camera/getAllDefence?cameraId=121164&page=
    @GET("getAllDefence")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<Defence> getAllDefence(@Query("cameraId") String cameraId, @Query("page") String page);
//    post   http://192.168.4.111:51091/camera/bindCameraSensor
//            （defenceId=5&defenceName=dnm40&sensorId=30&cameraId=3121164)
    @FormUrlEncoded
    @POST("bindCameraSensor")
    Observable<PostResult> bindCameraSensor(@Field("defenceId") String defenceId, @Field("defenceName") String defenceName,
                                            @Field("sensorId") String sensorId, @Field("cameraId") String cameraId);
//    post   http://192.168.4.111:51091/camera/deleteCameraSensor
//            （defenceId=5&cameraId=3121164)
    @FormUrlEncoded
    @POST("deleteCameraSensor")
    Observable<PostResult> deleteCameraSensor(@Field("defenceId") String defenceId, @Field("cameraId") String cameraId);

//    16 	修改防区名称
//    16  post:  http://192.168.4.111:51091/camera/changeDefenceName
//            （cameraId=3121164&defenceId=5&defenceName=ndn)
    @FormUrlEncoded
    @POST("changeDefenceName")
    Observable<PostResult> changeDefenceName(@Field("cameraId") String cameraId, @Field("defenceId") String defenceId,
                                             @Field("defenceName") String defenceName);
//    http://192.168.4.111:51091/camera/getAllAlarm?userId=13428282520&privilege=1&page=
    @GET("getAllAlarm")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<AlarmMsg> getAllAlarm(@Query("userId") String userId, @Query("privilege") String privilege,@Query("page") String page);
//
//    9 获取摄像头信息
//    9 get:	  http://192.168.4.111:51091/camera/getOneCamera?cameraId=3121164
    @GET("getOneCamera")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<AlarmCameraInfo> getOneCamera(@Query("cameraId") String cameraId);

    @GET("getCid")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    Observable<HttpError> bindAlias(@Query("alias") String alias, @Query("cid") String cid,@Query("projectName") String projectName);

    @FormUrlEncoded
    @POST("textAlarm")
    Observable<PostResult> textAlarm(@Field("userId") String userId, @Field("privilege") String privilege,
                                             @Field("cameraId") String cameraId,@Field("info") String info);
}
