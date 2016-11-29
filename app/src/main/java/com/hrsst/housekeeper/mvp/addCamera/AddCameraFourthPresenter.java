package com.hrsst.housekeeper.mvp.addCamera;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.hrsst.housekeeper.AppApplication;
import com.hrsst.housekeeper.common.basePresenter.BasePresenter;
import com.hrsst.housekeeper.entity.Area;
import com.hrsst.housekeeper.entity.HttpAreaResult;
import com.hrsst.housekeeper.entity.HttpError;
import com.hrsst.housekeeper.entity.PostResult;
import com.hrsst.housekeeper.entity.ShopType;
import com.hrsst.housekeeper.rxjava.ApiCallback;
import com.hrsst.housekeeper.rxjava.SubscriberCallBack;
import com.hrsst.housekeeper.service.LocationService;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/11/23.
 */
public class AddCameraFourthPresenter extends BasePresenter<AddCameraFourthView> {
    private LocationService locationService;

    public AddCameraFourthPresenter(AddCameraFourthActivity addCameraFourthActivity){
        attachView(addCameraFourthActivity);
    }

    public void initLocation(){
        locationService = AppApplication.context.locationService;
        locationService.registerListener(mListener);
    }

    public void startLocation(){
        locationService.start();
        mvpView.showLoading();
    }

    public void stopLocation(){
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                int result = location.getLocType();
                switch (result){
                    case 61:
                        mvpView.getLocationData(location);
                        break;
                    case 161:
                        mvpView.getLocationData(location);
                        break;
                }
                locationService.stop();
                mvpView.hideLoading();
            }
        }
    };

    //(cameraId=1&cameraName=2&cameraPwd=3&cameraAddress=4&longitude=5&latitude=6&principal1=7&principal1Phone=8&principal2=9&principal2Phone=10&areaId=11&placeTypeId=12)
    public void addCamera(String cameraId,String cameraName,String cameraPwd,String cameraAddress,String longitude,
                          String latitude,String principal1,String principal1Phone,String principal2,String principal2Phone,
                          String areaId,String placeTypeId){
        if(longitude.length()==0||latitude.length()==0){
            mvpView.errorMessage("请获取经纬度");
            return;
        }
        if(cameraPwd==null||cameraPwd.length()==0){
            mvpView.errorMessage("请填写摄像机密码");
            return;
        }
//        if(areaId==null||areaId.length()==0){
//            mvpView.errorMessage("请填选择区域");
//            return;
//        }
        mvpView.showLoading();
        Observable<PostResult> mObservable = apiStoreServer.addCamera(cameraId,cameraName,cameraPwd,cameraAddress,longitude,
                latitude,principal1,principal1Phone,principal2,principal2Phone,
                areaId,placeTypeId);
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<PostResult>() {
            @Override
            public void onSuccess(PostResult model) {
                int result = model.getErrorCode();
                if(result==0){
                    mvpView.addCameraResult("添加成功");
                }else{
                    mvpView.errorMessage("添加失败");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.errorMessage("添加失败");
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }

    //type:1表示查询商铺类型，2表示查询区域类型
    public void getPlaceTypeId(String userId, String privilege, final int type){
        Observable mObservable = null;
        if(type==1){
            mObservable= apiStoreServer.getPlaceTypeId(userId,privilege,"").map(new Func1<HttpError,ArrayList<Object>>() {
                @Override
                public ArrayList<Object> call(HttpError o) {
                    return o.getPlaceType();
                }
            });
        }else{
            mObservable= apiStoreServer.getAreaId(userId,privilege,"").map(new Func1<HttpAreaResult,ArrayList<Object>>() {
                @Override
                public ArrayList<Object> call(HttpAreaResult o) {
                    return o.getSmoke();
                }
            });
        }
        addSubscription(mObservable,new SubscriberCallBack<>(new ApiCallback<ArrayList<Object>>() {
            @Override
            public void onSuccess(ArrayList<Object> model) {
                if(type==1){
                    if(model!=null&&model.size()>0){
                        mvpView.getShopType(model);
                    }else{
                        mvpView.getShopTypeFail("无数据");
                    }
                }else{
                    if(model!=null&&model.size()>0){
                        mvpView.getAreaType(model);
                    }else{
                        mvpView.getAreaTypeFail("无数据");
                    }
                }
            }
            @Override
            public void onFailure(int code, String msg) {
                mvpView.errorMessage("网络错误");
            }
            @Override
            public void onCompleted() {
            }
        }));
    }

    @Override
    public void getArea(Area area) {
        mvpView.getChoiceArea(area);
    }

    @Override
    public void getShop(ShopType shopType) {
        mvpView.getChoiceShop(shopType);
    }
}
