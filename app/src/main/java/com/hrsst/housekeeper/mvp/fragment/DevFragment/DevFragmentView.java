package com.hrsst.housekeeper.mvp.fragment.DevFragment;

import com.hrsst.housekeeper.common.data.Contact;

import java.util.List;

/**
 * Created by Administrator on 2016/11/18.
 */
public interface DevFragmentView {
    void showLoading();
    void hideLoading();
    void setCameraStatus(List<Contact> lists);
    void setDefenceState(List<Contact> lists);
    void deleteUserIdCameraIdResult(String msg);
    void deleteUserIdCameraIdSuccess(Contact data,String msg);
    void getAllCameraResult(List<Contact> contactList);
    void pushResult(List<Contact> contactList);
}
