package com.hrsst.housekeeper.mvp.defence;

/**
 * Created by Administrator on 2016/11/25.
 */
public interface DefenceView {
    void showLoading();
    void hideLoading();
    void studyErrorResult(String msg);
    void studySuccessResult(String msg);
}
