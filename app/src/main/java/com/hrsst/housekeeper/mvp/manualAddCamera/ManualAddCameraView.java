package com.hrsst.housekeeper.mvp.manualAddCamera;

/**
 * Created by Administrator on 2016/11/24.
 */
public interface ManualAddCameraView {
    void showLoading();
    void hideLoading();
    void addCameraResult(String msg);
    void errorMessage(String msg);
    void bindAlarm(String[] new_data);
    void finishActivity();
}
