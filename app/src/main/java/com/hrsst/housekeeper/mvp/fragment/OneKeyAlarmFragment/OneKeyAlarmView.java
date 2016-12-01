package com.hrsst.housekeeper.mvp.fragment.OneKeyAlarmFragment;

import com.hrsst.housekeeper.common.data.Contact;

import java.util.List;

/**
 * Created by Administrator on 2016/11/10.
 */
public interface OneKeyAlarmView {
    void getCurrentTime(int time);
    void stopCountDown(String msg);
    void sendAlarmMessage(String result);
    void getDataResult(String result);
    void getDataSuccess(List<Contact> contacts);
}
