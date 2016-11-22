package com.hrsst.housekeeper.mvp.fragment.DevFragment;

import com.hrsst.housekeeper.common.data.Contact;

import java.util.List;

/**
 * Created by Administrator on 2016/11/18.
 */
public interface DevFragmentView {
    void setCameraStatus(List<Contact> lists);
    void setDefenceState(List<Contact> lists);
}
