package com.hrsst.housekeeper.mvp.addCamera;

import com.baidu.location.BDLocation;

/**
 * Created by Administrator on 2016/11/23.
 */
public interface AddCameraFourthView {
    void getLocationData(BDLocation location);
    void showLoading();
    void hideLoading();
    void addCameraResult(String msg);
    void errorMessage(String msg);
}
