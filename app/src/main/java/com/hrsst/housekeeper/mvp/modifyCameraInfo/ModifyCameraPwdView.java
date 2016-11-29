package com.hrsst.housekeeper.mvp.modifyCameraInfo;

/**
 * Created by Administrator on 2016/11/24.
 */
public interface ModifyCameraPwdView {
    void showLoading();
    void hideLoading();
    void modifyCameraPwdResult(String msg,String pwd);
    void errorMessage(String msg);
}
