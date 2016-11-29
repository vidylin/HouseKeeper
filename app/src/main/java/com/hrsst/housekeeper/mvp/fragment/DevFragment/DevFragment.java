package com.hrsst.housekeeper.mvp.fragment.DevFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hrsst.housekeeper.AppComponent;
import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.adapter.ContactAdapter;
import com.hrsst.housekeeper.common.baseActivity.BaseFragment;
import com.hrsst.housekeeper.common.data.Contact;
import com.hrsst.housekeeper.common.global.Constants;
import com.hrsst.housekeeper.common.utils.SharedPreferencesManager;
import com.hrsst.housekeeper.common.utils.T;
import com.hrsst.housekeeper.common.widget.NormalDialog;
import com.hrsst.housekeeper.mvp.apmonitor.ApMonitorActivity;
import com.hrsst.housekeeper.mvp.modifyCameraInfo.ModifyCameraPwdActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8.
 */
public class DevFragment extends BaseFragment implements DevFragmentView{
    @Inject
    DevFragmentPresenter devFragmentPresenter;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    private LinearLayoutManager linearLayoutManager;
    private List<Contact> list;
    private Context mContext;
    private ContactAdapter contactAdapter;
    private  Map<String,Integer> stringMap;
    private String userNumber;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerDevFragmentComponent.builder()
                .appComponent(appComponent)
                .devFragmentModule(new DevFragmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_mydevice, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext=getActivity();
        userNumber = SharedPreferencesManager.getInstance().getData(mContext, SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTPASS_NUMBER);
        list = new ArrayList<>();
        stringMap = new HashMap<>();
        regFilter();
        refreshListView();
        devFragmentPresenter.getAllCamera(userNumber,"1","",false);
    }
    private void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.Action.GET_FRIENDS_STATE);
        filter.addAction(Constants.P2P.RET_GET_REMOTE_DEFENCE);
        filter.addAction(Constants.P2P.RET_SET_REMOTE_DEFENCE);
        filter.addAction(Constants.PUSH_CAMERA_DATA);
        mContext.registerReceiver(mReceiver, filter);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constants.PUSH_CAMERA_DATA)){
                devFragmentPresenter.getAllCamera(userNumber,"1","",true);
            }
            if(intent.getAction().equals(Constants.Action.GET_FRIENDS_STATE)){
                Map<String,Contact> contactMap = (Map<String, Contact>) intent.getSerializableExtra("contact");
                devFragmentPresenter.setFriendStatus(list,contactMap);
            }
            if(intent.getAction().equals(Constants.P2P.RET_GET_REMOTE_DEFENCE)){
                String id = intent.getExtras().getString("contactId");
                int state = intent.getExtras().getInt("state");
                stringMap.put(id,state);
                if(stringMap.size()==list.size()){
                    devFragmentPresenter.setDefenceState(list,stringMap);
                    stringMap.clear();
                }
            }
            if(intent.getAction().equals(Constants.P2P.RET_SET_REMOTE_DEFENCE)){
                String id = intent.getExtras().getString("contactId");
                int state = intent.getExtras().getInt("state");
                if(state==0){
                    devFragmentPresenter.getDefenceState(list);
                }
            }
        }
    };

    private void refreshListView() {
        //设置刷新时动画的颜色，可以设置4个
        swipereFreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipereFreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipereFreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        swipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                devFragmentPresenter.getAllCamera(userNumber,"1","",true);
            }
        });
    }

    @Override
    public String getFragmentName() {
        return "DevFragment";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mReceiver);
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setCameraStatus(List<Contact> lists)
    {
        notifyData(lists);
    }

    @Override
    public void setDefenceState(List<Contact> lists) {
        notifyData(lists);
    }

    @Override
    public void deleteUserIdCameraIdResult(String msg) {
        swipereFreshLayout.setRefreshing(false);
        T.showShort(mContext,msg);
    }

    @Override
    public void deleteUserIdCameraIdSuccess(Contact data, String msg) {
        T.showShort(mContext,msg);
        list.remove(data);
        contactAdapter.notifyDataSetChanged();
    }

    @Override
    public void getAllCameraResult(List<Contact> list1) {
        list.clear();
        list.addAll(list1);
        contactAdapter = new ContactAdapter(this,list,devFragmentPresenter);
        recyclerView.setAdapter(contactAdapter);
        devFragmentPresenter.getFriendStatus(list);
        devFragmentPresenter.getDefenceState(list);
        swipereFreshLayout.setRefreshing(false);
        contactAdapter.setOnItemClickListener(new ContactAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Contact contact) {
                Intent monitor = new Intent();
                monitor.setClass(mContext, ApMonitorActivity.class);
                monitor.putExtra("contact", contact);
                monitor.putExtra("connectType", Constants.ConnectType.P2PCONNECT);
                monitor.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(monitor);
            }
        });
        contactAdapter.setOnItemLongClickListener(new ContactAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final Contact data) {
                final String cameraId = data.contactId;
                NormalDialog normalDialog = new NormalDialog(mContext);
                normalDialog.setTitle("删除摄像机");
                normalDialog.setContentStr("从列表中删除"+cameraId+"?");
                normalDialog.setbtnStr1("是");
                normalDialog.setbtnStr2("否");
                normalDialog.showNormalDialog();
                normalDialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {
                    @Override
                    public void onClick() {
                        devFragmentPresenter.deleteUserIdCameraId(userNumber,data);
                    }
                });
            }
        });
    }

    @Override
    public void pushResult(List<Contact> contactList) {
        list.clear();
        list.addAll(contactList);
        swipereFreshLayout.setRefreshing(false);
        devFragmentPresenter.getFriendStatus(list);
        devFragmentPresenter.getDefenceState(list);
    }

    private void notifyData(List<Contact> lists){
        List<Contact> l = new ArrayList<>();
        l.addAll(lists);
        list.removeAll(list);
        list.addAll(l);
        contactAdapter.notifyDataSetChanged();
    }

    public void intentModify(Contact contact,int pos){
        Intent intent = new Intent(mContext, ModifyCameraPwdActivity.class);
        intent.putExtra("contact",contact);
        intent.putExtra("position",pos);
        startActivityForResult(intent,100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            Contact contact = (Contact) data.getExtras().getSerializable("contact");
            int pos = data.getExtras().getInt("position");
            list.set(pos, contact);
            contactAdapter.notifyDataSetChanged();
        }
    }

}
