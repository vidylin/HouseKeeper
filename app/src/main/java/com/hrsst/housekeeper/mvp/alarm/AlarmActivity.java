package com.hrsst.housekeeper.mvp.alarm;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hrsst.housekeeper.AppComponent;
import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.common.baseActivity.BaseActivity;
import com.hrsst.housekeeper.common.data.Contact;
import com.hrsst.housekeeper.common.global.Constants;
import com.hrsst.housekeeper.common.utils.MusicManger;
import com.hrsst.housekeeper.common.utils.Utils;
import com.hrsst.housekeeper.entity.AlarmCameraInfo;
import com.hrsst.housekeeper.mvp.apmonitor.ApMonitorActivity;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlarmActivity extends BaseActivity implements AlarmView {
    @Inject
    AlarmPresenter alarmPresenter;
    Context mContext;
    @Bind(R.id.alarm_fk_img)
    ImageView alarm_fk_img;
    @Bind(R.id.alarm_msg_tv)
    TextView alarm_msg_tv;
    @Bind(R.id.alarm_msg_time_tv)
    TextView alarm_msg_time_tv;
    @Bind(R.id.alarm_back)
    ImageView alarm_back;
    @Bind(R.id.watch_video_image)
    ImageView watch_video_image;
    private int TIME_OUT = 20 * 1000;
    boolean isAlarm;
    private Contact mContact;
    private String cameraId;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAlarmComponent.builder()
                .appComponent(appComponent)
                .alarmModule(new AlarmModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int mCurrentOri = getResources().getConfiguration().orientation;
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        mContext = this;
        setContentView(R.layout.activity_firelink_alarm);
        ButterKnife.bind(this);
        mContact = new Contact();
        cameraId = getIntent().getExtras().getString("cameraId");
        long alarmTime = Long.parseLong(getIntent().getExtras().getString("alarmTime"));
        alarmPresenter.getCameraInfo(cameraId);
        String alarmTimeStr = Utils.ConvertTimeByLong(alarmTime);
        alarm_msg_time_tv.setText("报警时间:"+alarmTimeStr);
        alarmInit();
        excuteTimeOutTimer();
    }

    @OnClick({R.id.alarm_back,R.id.watch_video_image})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.alarm_back:
                finish();
                break;
            case R.id.watch_video_image:
                if (null != mContact) {
                    mContact.contactType = 0;
                    mContact.apModeState = 1;
                    Intent monitor = new Intent();
                    monitor.setClass(mContext, ApMonitorActivity.class);
                    monitor.putExtra("contact", mContact);
                    monitor.putExtra("connectType", Constants.ConnectType.P2PCONNECT);
                    monitor.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(monitor);
                    finish();
                } else {
                    watch_video_image.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    private void alarmInit() {
        final AnimationDrawable anim = (AnimationDrawable) alarm_fk_img
                .getBackground();
        OnPreDrawListener opdl = new OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                anim.start();
                return true;
            }

        };
        alarm_fk_img.getViewTreeObserver().addOnPreDrawListener(opdl);
    }

    boolean viewed = false;
    Timer timeOutTimer;
    public static final int USER_HASNOTVIEWED = 3;

    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case USER_HASNOTVIEWED:
                    finish();
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    // 超时计时器
    public void excuteTimeOutTimer() {
        timeOutTimer = new Timer();
        TimerTask mTask = new TimerTask() {
            @Override
            public void run() {
                // 弹出一个对话框
                if (!viewed) {
                    Message message = new Message();
                    message.what = USER_HASNOTVIEWED;
                    mHandler.sendMessage(message);
                }
            }
        };
        timeOutTimer.schedule(mTask, TIME_OUT);
    }

    // 报警声音
    public void loadMusicAndVibrate() {
        isAlarm = true;
        MusicManger.getInstance().playAlarmMusic();
        new Thread() {
            public void run() {
                while (isAlarm) {
                    MusicManger.getInstance().Vibrate();
                    Utils.sleepThread(100);
                }
                MusicManger.getInstance().stopVibrate();
            }
        }.start();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        isAlarm = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMusicAndVibrate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicManger.getInstance().stop();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeOutTimer != null) {
            timeOutTimer.cancel();
            timeOutTimer = null;
        }

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
    }

    @Override
    public void getAlarmCameraResult(AlarmCameraInfo.CameraBean cameraBean) {
        mContact.contactName = cameraBean.getCameraName();
        mContact.contactPassword = cameraBean.getCameraPwd();
        mContact.contactId = cameraId;
        alarm_msg_tv.setText(cameraBean.getCameraAddress()+"发生报警");
    }
}

