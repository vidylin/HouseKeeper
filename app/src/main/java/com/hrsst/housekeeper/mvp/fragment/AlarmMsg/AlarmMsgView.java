package com.hrsst.housekeeper.mvp.fragment.AlarmMsg;

import com.hrsst.housekeeper.entity.AlarmMsg;

import java.util.List;

/**
 * Created by Administrator on 2016/11/29.
 */
public interface AlarmMsgView {
    void showLoading();
    void hideLoading();
    void errorMsg(String msg);
    void getAllMsg(List<AlarmMsg.AlarmBean> alarmBeen);
}
