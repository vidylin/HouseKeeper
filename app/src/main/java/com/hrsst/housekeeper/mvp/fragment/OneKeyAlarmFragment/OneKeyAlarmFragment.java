package com.hrsst.housekeeper.mvp.fragment.OneKeyAlarmFragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.test.suitebuilder.annotation.Smoke;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrsst.housekeeper.AppComponent;
import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.adapter.ViewPagerAdapter;
import com.hrsst.housekeeper.common.baseActivity.BaseFragment;
import com.hrsst.housekeeper.common.data.Contact;
import com.hrsst.housekeeper.common.utils.SharedPreferencesManager;
import com.hrsst.housekeeper.common.utils.T;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.OnPageChange;

/**
 * Created by Administrator on 2016/11/8.
 */
public class OneKeyAlarmFragment extends BaseFragment implements OneKeyAlarmView{
    @Inject
    OneKeyAlarmPresenter oneKeyAlarmPresenter;
    @Bind(R.id.alarm_time)
    TextView alarmTime;
    @Bind(R.id.cancel_alarm)
    RelativeLayout cancelAlarm;
    @Bind(R.id.call_alarm_image)
    ImageView callAlarmImage;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.viewGroup)
    LinearLayout viewGroup;
    private Context mContext;
    private ViewPagerAdapter mViewPagerAdapter;
    private List<View> views;
    private ImageView[] mTips = null;
    private List<Contact> contacts;
    private Contact contact;
    private String userID;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerOneKeyAlarmComponent.builder()
                .appComponent(appComponent)
                .oneKeyAlarmModule(new OneKeyAlarmModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_one_key_alarm,null);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnLongClick({R.id.call_alarm_image})
    public boolean OnLongClick(View view) {
        switch (view.getId()) {
            case R.id.call_alarm_image:

                break;
            default:
                break;
        }
        return true;
    }

    @OnClick({R.id.cancel_alarm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_alarm:
                oneKeyAlarmPresenter.stopCountDown();
                break;
            default:
                break;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        cancelAlarm.setClickable(false);
    }

    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void getCurrentTime(String time) {
        cancelAlarm.setVisibility(View.VISIBLE);
        alarmTime.setText(time + "(s)");
    }

    @Override
    public void stopCountDown(String msg) {
        T.showShort(mContext, msg);
        callAlarmImage.setLongClickable(true);
        cancelAlarm.setClickable(false);
        cancelAlarm.setBackgroundResource(R.drawable.cancel_alarm_btn_an);
        alarmTime.setText("");
        alarmTime.setVisibility(View.GONE);
    }

    @Override
    public void sendAlarmMessage(String result) {
        T.showShort(mContext, result);
        callAlarmImage.setLongClickable(true);
        cancelAlarm.setClickable(false);
        cancelAlarm.setBackgroundResource(R.drawable.cancel_alarm_btn_an);
        alarmTime.setText("");
        alarmTime.setVisibility(View.GONE);
    }

    @Override
    public void getDataResult(String result) {
        T.showShort(mContext,result);
    }

    @Override
    public void getDataSuccess(List<Contact> contacts) {
    }

    private void initViews(List<Smoke> list) {
        int len = list.size();
        if(len>0){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            views = new ArrayList<>();
            // 初始化引导图片列表
            for(int i=0;i<len;i++){
                views.add(inflater.inflate(R.layout.view_pager_one, null));
            }
            // 初始化Adapter
            mViewPagerAdapter = new ViewPagerAdapter(views, getActivity(),contacts);
            viewPager.setAdapter(mViewPagerAdapter);
        }
    }

    @OnPageChange(R.id.view_pager)
    public void onPageChange(int position){
        for(int i = 0; i < mTips.length; i++) {
            if(position == i) {
                mTips[i].setImageResource(R.mipmap.bj_qh_h);
            }else {
                mTips[i].setImageResource(R.mipmap.bj_qh_b);
            }
        }
        if(contacts!=null){
            contact = contacts.get(position);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initDots() {
        mTips = new ImageView[views.size()];
        int len = mTips.length;
        // 循环取得小点图片
        if(len==1){
            return;
        }else{
            for(int i = 0; i < len; i++) {
                ImageView iv = new ImageView(mContext);
                iv.setLayoutParams(new ViewGroup.LayoutParams(3,3));
                mTips[i] = iv;
                if(i == 0) {
                    iv.setImageResource(R.mipmap.bj_qh_h);
                }else {
                    iv.setImageResource(R.mipmap.bj_qh_b);
                }
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(15, 15));
                lp.leftMargin = 5;
                lp.rightMargin = 5;
                viewGroup.addView(iv,lp);
            }
        }
    }

    @Override
    public String getFragmentName() {
        return "OneKeyAlarmFragment";
    }

}
