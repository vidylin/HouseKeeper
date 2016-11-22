package com.hrsst.housekeeper.mvp.main;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrsst.housekeeper.AppApplication;
import com.hrsst.housekeeper.AppComponent;
import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.common.baseActivity.BaseActivity;
import com.hrsst.housekeeper.common.utils.SharedPreferencesManager;
import com.hrsst.housekeeper.common.widget.NormalDialog;
import com.hrsst.housekeeper.common.yoosee.P2PListener;
import com.hrsst.housekeeper.common.yoosee.SettingListener;
import com.hrsst.housekeeper.mvp.addCamera.AddCameraFirstActivity;
import com.hrsst.housekeeper.mvp.fragment.AlarmMsg.AlarmMsgFragment;
import com.hrsst.housekeeper.mvp.fragment.DevFragment.DevFragment;
import com.hrsst.housekeeper.mvp.fragment.MapFragment.MapFragment;
import com.hrsst.housekeeper.mvp.fragment.OneKeyAlarmFragment.OneKeyAlarmFragment;
import com.hrsst.housekeeper.mvp.login.LoginActivity;
import com.hrsst.housekeeper.service.MainService;
import com.p2p.core.P2PHandler;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainView {

    @Inject
    MainPresenter mainPresenter;
    //    @Inject
//    MainThread mainThread;
    @Bind(R.id.tv_contact)
    TextView tvContact;
    @Bind(R.id.icon_contact)
    RelativeLayout iconContact;
    @Bind(R.id.tv_message)
    TextView tvMessage;
    @Bind(R.id.icon_keyboard)
    RelativeLayout iconKeyboard;
    @Bind(R.id.tv_image)
    TextView tvImage;
    @Bind(R.id.icon_discover)
    RelativeLayout iconDiscover;
    @Bind(R.id.tv_more)
    TextView tvMore;
    @Bind(R.id.icon_setting)
    RelativeLayout iconSetting;
    @Bind(R.id.tab_component)
    LinearLayout tabComponent;
    @Bind(R.id.fragment_layout)
    FrameLayout fragmentLayout;
    @Bind(R.id.one)
    ImageView one;
    @Bind(R.id.num_tv)
    TextView numTv;
    @Bind(R.id.info_num)
    RelativeLayout infoNum;
    @Bind(R.id.menu_title)
    RelativeLayout menuTitle;
    @Bind(R.id.menu_one)
    TextView menuOne;
    @Bind(R.id.menu_two)
    TextView menuTwo;
    @Bind(R.id.menu_three)
    TextView menuThree;
    @Bind(R.id.menu_four)
    TextView menuFour;
    @Bind(R.id.menu_five)
    TextView menuFive;
    @Bind(R.id.setting_lin)
    LinearLayout settingLin;
    @Bind(R.id.left)
    RelativeLayout left;
    @Bind(R.id.drawerlayout)
    DrawerLayout drawerlayout;
    @Bind(R.id.add_device)
    TextView addDevice;
    @Bind(R.id.menu)
    TextView menu;
    @Bind(R.id.layout_add)
    LinearLayout layoutAdd;
    @Bind(R.id.user_num)
    TextView userNum;
    @Bind(R.id.mailAdr)
    TextView mailAdr;
    private AlarmMsgFragment alarmMsgFragment;
    private DevFragment devFragment;
    private MapFragment mapFragment;
    private OneKeyAlarmFragment oneKeyAlarmFragment;
    public static final int FRAGMENT_ONE = 0;
    public static final int FRAGMENT_TWO = 1;
    public static final int FRAGMENT_THREE = 2;
    public static final int FRAGMENT_FOUR = 3;
    private FragmentManager fragmentManager;
    private Animation animation_out, animation_in;
    private Context mContext;
    public boolean isHideAdd = true;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainActivityComponent.builder()
                .appComponent(appComponent)
                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        P2PHandler.getInstance().p2pInit(this, new P2PListener(), new SettingListener());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        fragmentManager = getFragmentManager();
        showFragment(FRAGMENT_ONE);
        drawerlayout.setScrimColor(Color.argb(80, 80, 80, 80));
        getHightAndWight();
        startService(new Intent(MainActivity.this, MainService.class));
        mainPresenter.updateVersion(mContext, true);
        String userNumber = SharedPreferencesManager.getInstance().getData(mContext, SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTPASS_NUMBER);
        String userId = SharedPreferencesManager.getInstance().getData(mContext, SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        userNum.setText(userNumber);
        mailAdr.setText(userId);
    }

    void getHightAndWight() {
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        AppApplication.SCREENWIGHT = dm.widthPixels;
        AppApplication.SCREENHIGHT = dm.heightPixels;
        animation_out = AnimationUtils.loadAnimation(mContext,
                R.anim.scale_amplify);
        animation_in = AnimationUtils.loadAnimation(mContext,
                R.anim.scale_narrow);
    }

    @OnClick({R.id.icon_setting, R.id.icon_discover, R.id.icon_keyboard, R.id.icon_contact, R.id.add_device,
            R.id.menu, R.id.radar_add, R.id.manually_add, R.id.menu_two, R.id.setting_lin, R.id.menu_five})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icon_contact:
                showFragment(FRAGMENT_ONE);
                tabComponent.setBackgroundResource(R.mipmap.daohang1);
                addDevice.setVisibility(View.VISIBLE);
                break;
            case R.id.icon_keyboard:
                layoutAdd.setVisibility(View.GONE);
                isHideAdd = true;
                showFragment(FRAGMENT_TWO);
                tabComponent.setBackgroundResource(R.mipmap.daohang2);
                addDevice.setVisibility(View.GONE);
                break;
            case R.id.icon_discover:
                layoutAdd.setVisibility(View.GONE);
                isHideAdd = true;
                showFragment(FRAGMENT_THREE);
                tabComponent.setBackgroundResource(R.mipmap.daohang3);
                addDevice.setVisibility(View.GONE);
                break;
            case R.id.icon_setting:
                layoutAdd.setVisibility(View.GONE);
                isHideAdd = true;
                showFragment(FRAGMENT_FOUR);
                tabComponent.setBackgroundResource(R.mipmap.daohang4);
                addDevice.setVisibility(View.GONE);
                break;
            case R.id.add_device:
                if (isHideAdd == true) {
                    showAdd();
                } else {
                    hideAdd();
                }
                break;
            case R.id.radar_add:
                layoutAdd.setVisibility(View.GONE);
                isHideAdd = true;
                Intent intent = new Intent(this, AddCameraFirstActivity.class);
                startActivity(intent);
                break;
            case R.id.manually_add:
                layoutAdd.setVisibility(View.GONE);
                isHideAdd = true;
                break;
            case R.id.menu:
                drawerlayout.openDrawer(left);
                break;
            case R.id.menu_two:
                drawerlayout.closeDrawer(left);
                mainPresenter.updateVersion(mContext, false);
                break;
            case R.id.menu_five:
                SharedPreferencesManager.getInstance().putData(mContext,
                        SharedPreferencesManager.SP_FILE_GWELL,
                        SharedPreferencesManager.KEY_RECENTPASS,
                        "");
                Intent intent1 = new Intent(mContext, LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
            default:
                break;
        }
    }

    public void showFragment(int index) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        hideFragment(ft);
        //注意这里设置位置
        switch (index) {
            case FRAGMENT_TWO:
                if (mapFragment == null) {
                    mapFragment = new MapFragment();
                    ft.add(R.id.fragment_layout, mapFragment);
                } else {
                    ft.show(mapFragment);
                }
                break;
            case FRAGMENT_ONE:
                if (devFragment == null) {
                    devFragment = new DevFragment();
                    ft.add(R.id.fragment_layout, devFragment);
                } else {
                    ft.show(devFragment);
                }
                break;
            case FRAGMENT_THREE:
                if (oneKeyAlarmFragment == null) {
                    oneKeyAlarmFragment = new OneKeyAlarmFragment();
                    ft.add(R.id.fragment_layout, oneKeyAlarmFragment);
                } else {
                    ft.show(oneKeyAlarmFragment);
                }
                break;
            case FRAGMENT_FOUR:
                if (alarmMsgFragment == null) {
                    alarmMsgFragment = new AlarmMsgFragment();
                    ft.add(R.id.fragment_layout, alarmMsgFragment);
                } else {
                    ft.show(alarmMsgFragment);
                }
                break;
        }
        ft.commit();
    }

    public void hideFragment(FragmentTransaction ft) {
        //如果不为空，就先隐藏起来
        if (mapFragment != null) {
            ft.hide(mapFragment);
        }
        if (devFragment != null) {
            ft.hide(devFragment);
        }
        if (oneKeyAlarmFragment != null) {
            ft.hide(oneKeyAlarmFragment);
        }
        if (alarmMsgFragment != null) {
            ft.hide(alarmMsgFragment);
        }
    }

    public void hideAdd() {
        layoutAdd.startAnimation(animation_in);
        layoutAdd.setVisibility(LinearLayout.GONE);
        isHideAdd = true;
    }

    public void showAdd() {
        layoutAdd.setVisibility(LinearLayout.VISIBLE);
        layoutAdd.startAnimation(animation_out);
        isHideAdd = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapFragment != null) {
            mapFragment = null;
        }
        if (devFragment != null) {
            devFragment = null;
        }
        if (oneKeyAlarmFragment != null) {
            oneKeyAlarmFragment = null;
        }
        if (alarmMsgFragment != null) {
            alarmMsgFragment = null;
        }
    }

    @Override
    public void showUpdateDialog(String message, final String urlStr) {
        new NormalDialog(mContext, message, urlStr).showVersionDialog();
    }
}
