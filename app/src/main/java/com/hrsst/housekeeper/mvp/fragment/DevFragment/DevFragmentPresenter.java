package com.hrsst.housekeeper.mvp.fragment.DevFragment;

import android.content.Context;

import com.hrsst.housekeeper.common.data.Contact;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/18.
 */
public interface DevFragmentPresenter {
    void setFriendStatus(List<Contact> lists,Map<String,Contact> contactMap);
    void setDefence(Context mContext, String callId, String password, int defenceState);
    void getFriendStatus(List<Contact> lists);
    void getDefenceState(List<Contact> contact);
    void setDefenceState(List<Contact> lists,Map<String,Integer> map);
}
