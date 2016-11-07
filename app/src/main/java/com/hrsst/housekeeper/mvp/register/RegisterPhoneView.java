package com.hrsst.housekeeper.mvp.register;

/**
 * Created by Administrator on 2016/11/7.
 */
public interface RegisterPhoneView {
    void getDataFail(String msg);

    void showLoading();

    void hideLoading();

    void register();

    void getMessageSuccess();
}
