package com.hrsst.housekeeper.mvp.fragment.DevFragment;

import android.content.Context;

import com.hrsst.housekeeper.common.data.Contact;
import com.hrsst.housekeeper.common.global.Constants;
import com.p2p.core.P2PHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/11/18.
 */
public class DevFragmentPresenterImpl implements DevFragmentPresenter,DevFragmentListener{
    private DevFragmentView devFragmentView;

    public DevFragmentPresenterImpl(DevFragmentView devFragmentView) {
        this.devFragmentView = devFragmentView;
    }

    @Override
    public void setFriendStatus(List<Contact> lists, Map<String, Contact> contactMap) {
        List<Contact> contactList = new ArrayList<>();
        contactList.addAll(lists);
        if(contactMap!=null&&lists.size()==contactMap.size()){
            for(int i=0;i<contactMap.size();i++){
                Contact c = contactList.get(i);
                String id = c.contactId;
                Contact contact = contactMap.get(id);
                if(contact!=null){
                    c.onLineState = contact.onLineState;
                    c.contactType = contact.contactType;
                    lists.remove(i);
                    lists.add(i,c);
                }
            }
            devFragmentView.setCameraStatus(lists);
        }
    }

    @Override
    public void setDefence(Context mContext,String callId,String password,int defenceState) {
        // 设置布防
        if (defenceState == Constants.DefenceState.DEFENCE_STATE_ON) {
            P2PHandler.getInstance().setRemoteDefence(callId, password,
                    Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_OFF);

        } else if (defenceState == Constants.DefenceState.DEFENCE_STATE_OFF) {
            P2PHandler.getInstance().setRemoteDefence(callId, password,
                    Constants.P2P_SET.REMOTE_DEFENCE_SET.ALARM_SWITCH_ON);
        }
    }

    @Override
    public void getFriendStatus(List<Contact> lists) {
        Observable.just(lists).map(new Func1<List<Contact>, Object>() {
            @Override
            public Object call(List<Contact> contacts) {
                String[] contactIds = new String[contacts.size()];
                int i = 0;
                for (Contact contact : contacts) {
                    contactIds[i] = contact.contactId;
                    i++;
                }
                P2PHandler.getInstance().getFriendStatus(contactIds);
                return null;
            }
        }).subscribe();
    }

    @Override
    public void getDefenceState(List<Contact> lists) {
        Observable.just(lists).map(new Func1<List<Contact>, Object>() {
            @Override
            public Object call(List<Contact> contacts) {
                for (Contact contact : contacts) {
                    P2PHandler.getInstance().getDefenceStates(contact.contactId, contact.contactPassword);
                }
                return null;
            }
        }).subscribe();
    }

    @Override
    public void setDefenceState(List<Contact> lists, Map<String, Integer> map) {
        List<Contact> contactList = new ArrayList<>();
        contactList.addAll(lists);
        if(map!=null&&lists.size()==map.size()){
            for(int i=0;i<map.size();i++){
                Contact c = contactList.get(i);
                String id = c.contactId;
                int state = map.get(id);
                c.defenceState = state;
                lists.remove(i);
                lists.add(i,c);
            }
            devFragmentView.setDefenceState(lists);
        }
    }

}
