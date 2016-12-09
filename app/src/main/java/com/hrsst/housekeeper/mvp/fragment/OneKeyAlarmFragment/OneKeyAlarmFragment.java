package com.hrsst.housekeeper.mvp.fragment.OneKeyAlarmFragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrsst.housekeeper.AppApplication;
import com.hrsst.housekeeper.AppComponent;
import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.adapter.ViewPagerAdapter;
import com.hrsst.housekeeper.common.baseActivity.BaseFragment;
import com.hrsst.housekeeper.common.data.Contact;
import com.hrsst.housekeeper.common.utils.SharedPreferencesManager;
import com.hrsst.housekeeper.common.utils.T;
import com.hrsst.housekeeper.common.widget.CircleTextProgressbar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;

/**
 * Created by Administrator on 2016/11/8.
 */
public class OneKeyAlarmFragment extends BaseFragment implements OneKeyAlarmView {
    @Inject
    OneKeyAlarmPresenter oneKeyAlarmPresenter;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.viewGroup)
    LinearLayout viewGroup;
    @Bind(R.id.tv_red_circle_color)
    CircleTextProgressbar tvRedCircleColor;
    @Bind(R.id.start_image)
    ImageView startImage;
    @Bind(R.id.one_key_alarm_bg)
    RelativeLayout oneKeyAlarmBg;
    @Bind(R.id.m_tv)
    TextView mTv;
    @Bind(R.id.send_tv)
    TextView sendTv;
    @Bind(R.id.infoOperating)
    ImageView infoOperating;
    private Context mContext;
    private ViewPagerAdapter mViewPagerAdapter;
    private List<View> views;
    private ImageView[] mTips = null;
    private List<Contact> contacts;
    private Contact contact;
    private String userID;
    private int progress;
    private Animation operatingAnim;
    private String privilege;

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
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_one_key_alarm, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        userID = SharedPreferencesManager.getInstance().getData(mContext,
                SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTPASS_NUMBER);
        contacts = new ArrayList<>();
        privilege = AppApplication.privilege;
        oneKeyAlarmPresenter.getAllCamera(userID, privilege, "",false);
        operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        tvRedCircleColor.setProgressType(CircleTextProgressbar.ProgressType.COUNT);
        startImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sendTv.setVisibility(View.GONE);
                        mTv.setText("长按5s后报警");
                        oneKeyAlarmBg.setBackgroundResource(R.color.progress_bg);
                        if(contact!=null){
                            oneKeyAlarmPresenter.startTimer(userID,privilege,contact.contactId,userID+"SOS");
                        }
                        startImage.setImageResource(R.mipmap.a_bj_an);
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (progress < 100) {
                            startImage.setImageResource(R.mipmap.a_bj);
                            oneKeyAlarmPresenter.stopTimer();
                            tvRedCircleColor.setProgress(0);
                        }
                        progress = 0;
                        return true;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.infoOperating)
    public void onClick(){
        if(operatingAnim!=null){
            infoOperating.startAnimation(operatingAnim);
            oneKeyAlarmPresenter.getAllCamera(userID, privilege, "",true);
        }
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
    public void getCurrentTime(int time) {
        progress = time;
        tvRedCircleColor.setProgress(time);
    }

    @Override
    public void sendAlarmMessage(String result) {
        T.showShort(mContext, result);
        tvRedCircleColor.setProgress(0);
        startImage.setImageResource(R.mipmap.a_bj_fs);
        oneKeyAlarmBg.setBackgroundResource(R.color.progress_bg_send);
        mTv.setText("再报一次");
        sendTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void getDataRefresh(List<Contact> contactList) {
        contacts.clear();
        contacts.addAll(contactList);
        contact = contacts.get(0);
        if(mViewPagerAdapter==null){
            initViews(contacts);
            initDots();
        }else{
            mViewPagerAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void getDataSuccess(List<Contact> contactList) {
        contacts.clear();
        contacts.addAll(contactList);
        contact = contacts.get(0);
        initViews(contacts);
        initDots();
    }

    @Override
    public void stopAnim() {
        infoOperating.clearAnimation();
    }

    private void initViews(List<Contact> list) {
        int len = list.size();
        if (len > 0) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            views = new ArrayList<>();
            // 初始化引导图片列表
            for (int i = 0; i < len; i++) {
                views.add(inflater.inflate(R.layout.view_pager_one, null));
            }
            // 初始化Adapter
            mViewPagerAdapter = new ViewPagerAdapter(views, getActivity(), contacts);
            viewPager.setAdapter(mViewPagerAdapter);
        }
    }

    @OnPageChange(R.id.view_pager)
    public void onPageChange(int position) {
        for (int i = 0; i < mTips.length; i++) {
            if (position == i) {
                mTips[i].setImageResource(R.mipmap.bj_qh_h);
            } else {
                mTips[i].setImageResource(R.mipmap.bj_qh_b);
            }
        }
        if (contacts != null) {
            contact = contacts.get(position);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initDots() {
        mTips = new ImageView[views.size()];
        int len = mTips.length;
        // 循环取得小点图片
        if (len == 1) {
            return;
        } else {
            for (int i = 0; i < len; i++) {
                ImageView iv = new ImageView(mContext);
                iv.setLayoutParams(new ViewGroup.LayoutParams(3, 3));
                mTips[i] = iv;
                if (i == 0) {
                    iv.setImageResource(R.mipmap.bj_qh_h);
                } else {
                    iv.setImageResource(R.mipmap.bj_qh_b);
                }
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(15, 15));
                lp.leftMargin = 5;
                lp.rightMargin = 5;
                viewGroup.addView(iv, lp);
            }
        }
    }

    @Override
    public String getFragmentName() {
        return "OneKeyAlarmFragment";
    }

}
