package com.hrsst.housekeeper.mvp.addCamera;

import com.baidu.location.BDLocation;
import com.hrsst.housekeeper.entity.Area;
import com.hrsst.housekeeper.entity.ShopType;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/23.
 */
public interface AddCameraFourthView {
    void getLocationData(BDLocation location);
    void showLoading();
    void hideLoading();
    void addCameraResult(String msg);
    void errorMessage(String msg);
    void getShopType(ArrayList<Object> shopTypes);
    void getShopTypeFail(String msg);
    void getAreaType(ArrayList<Object> shopTypes);
    void getAreaTypeFail(String msg);
    void getChoiceArea(Area area);
    void getChoiceShop(ShopType shopType);
}
