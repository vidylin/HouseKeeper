package com.hrsst.housekeeper.mvp.alarmSetting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.common.data.Contact;
import com.hrsst.housekeeper.common.global.Constants;
import com.hrsst.housekeeper.common.global.NpcCommon;
import com.hrsst.housekeeper.common.utils.T;
import com.hrsst.housekeeper.common.widget.NormalDialog;
import com.hrsst.housekeeper.mvp.alarmPushAccount.AlarmPushAccountActivity;
import com.hrsst.housekeeper.mvp.apmonitor.ApMonitorActivity;
import com.hrsst.housekeeper.mvp.modifyBoundEmail.ModifyBoundEmailActivity;
import com.lib.addBar.AddBar;
import com.p2p.core.P2PHandler;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlarmSettingActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.img_receive_alarm)
    ImageView img_receive_alarm;
    @Bind(R.id.progressBar_receive_alarm)
    ProgressBar progressBar_receive_alarm;
    @Bind(R.id.layout_alarm_switch)
    RelativeLayout layout_alarm_switch;
    @Bind(R.id.add_alarm_item)
    RelativeLayout add_alarm_item;
    @Bind(R.id.email_text)
    TextView email_text;
    @Bind(R.id.progressBar_email)
    ProgressBar progressBar_email;
    @Bind(R.id.change_email)
    RelativeLayout change_email;
    @Bind(R.id.motion_img)
    ImageView motion_img;
    @Bind(R.id.progressBar_motion)
    ProgressBar progressBar_motion;
    @Bind(R.id.change_motion)
    RelativeLayout change_motion;
    @Bind(R.id.buzzer_img)
    ImageView buzzer_img;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.change_buzzer)
    RelativeLayout change_buzzer;
    @Bind(R.id.radio_one)
    RadioButton radio_one;
    @Bind(R.id.radio_two)
    RadioButton radio_two;
    @Bind(R.id.radio_three)
    RadioButton radio_three;
    @Bind(R.id.buzzer_time)
    LinearLayout buzzer_time;
    @Bind(R.id.pir_img)
    ImageView pir_img;
    @Bind(R.id.progressBar_pir)
    ProgressBar progressBar_pir;
    @Bind(R.id.change_pir)
    RelativeLayout change_pir;
    @Bind(R.id.img_alarm_input)
    ImageView img_alarm_input;
    @Bind(R.id.progressBar_alarm_input)
    ProgressBar progressBar_alarm_input;
    @Bind(R.id.alarm_input_switch)
    RelativeLayout alarm_input_switch;
    @Bind(R.id.img_alarm_out)
    ImageView img_alarm_out;
    @Bind(R.id.progressBar_alarm_out)
    ProgressBar progressBar_alarm_out;
    @Bind(R.id.alarm_out_switch)
    RelativeLayout alarm_out_switch;
    private Context mContext;
    private Contact contact;
    AddBar addBar;
    int buzzer_switch;
    int motion_switch;
    private boolean isRegFilter = false;

    NormalDialog dialog_loading;
    String[] last_bind_data;
    int cur_modify_buzzer_state;
    int cur_modify_motion_state;
    int infrared_switch;
    int modify_infrared_state;
    boolean current_infrared_state;
    boolean isOpenWriedAlarmInput;
    boolean isOpenWriedAlarmOut;
    boolean isReceiveAlarm = true;
    String[] new_data;
    int max_alarm_count;
    int lamp_switch;
    int cur_modify_lamp_state;
    String sendEmail = "";
    String emailRobot = "";
    String emailPwd = "";
    boolean isSurportSMTP = false;
    boolean isEmailLegal = true;
    boolean isEmailChecked = false;
    String idOrIp;// 如果是局域网用ip，不是局域网用id
    // boolean isRET=false;
    String senderEmail;
    int encrypt;
    int smtpport;
    boolean isSupportManual = false;
    private int connectType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);
        ButterKnife.bind(this);
        mContext = this;
        contact = (Contact) getIntent().getSerializableExtra("contact");
        connectType = getIntent().getIntExtra("connectType", 0);
        initComponent();
        showProgress();
        showProgress_motion();
        regFilter();
        idOrIp = contact.contactId;
        if (contact.ipadressAddress != null) {
            String mark = contact.ipadressAddress.getHostAddress();
            String ip = mark.substring(mark.lastIndexOf(".") + 1, mark.length());
            if (!ip.equals("") && ip != null) {
                idOrIp = ip;
            }
        }
        P2PHandler.getInstance().getNpcSettings(idOrIp, contact.contactPassword);
        P2PHandler.getInstance().getBindAlarmId(idOrIp, contact.contactPassword);
    }

    @Override
    public void onResume() {
        super.onResume();
        showProgress_email();
        P2PHandler.getInstance().getAlarmEmail(idOrIp,
                contact.contactPassword);
    }

    public void initComponent() {
        layout_alarm_switch.setClickable(false);
        add_alarm_item.setClickable(false);

        // AP模式部分功能隐藏
        if (connectType == 0) {
            layout_alarm_switch.setVisibility(View.VISIBLE);
            add_alarm_item.setVisibility(View.VISIBLE);
            change_email.setVisibility(View.VISIBLE);
        } else {
            layout_alarm_switch.setVisibility(View.GONE);
            add_alarm_item.setVisibility(View.GONE);
            change_email.setVisibility(View.GONE);
        }
        if (contact.isSmartHomeContatct()) {
            change_motion.setVisibility(View.GONE);
        } else {
            change_motion.setVisibility(View.VISIBLE);
        }
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.P2P.ACK_RET_GET_NPC_SETTINGS);

        filter.addAction(Constants.P2P.ACK_RET_SET_BIND_ALARM_ID);
        filter.addAction(Constants.P2P.ACK_RET_GET_BIND_ALARM_ID);
        filter.addAction(Constants.P2P.RET_SET_BIND_ALARM_ID);
        filter.addAction(Constants.P2P.RET_GET_BIND_ALARM_ID);
        filter.addAction(Constants.P2P.ACK_RET_GET_ALARM_EMAIL);
        filter.addAction(Constants.P2P.RET_GET_ALARM_EMAIL_WITHSMTP);

        filter.addAction(Constants.P2P.ACK_RET_SET_MOTION);
        filter.addAction(Constants.P2P.RET_SET_MOTION);
        filter.addAction(Constants.P2P.RET_GET_MOTION);

        filter.addAction(Constants.P2P.ACK_RET_SET_BUZZER);
        filter.addAction(Constants.P2P.RET_SET_BUZZER);
        filter.addAction(Constants.P2P.RET_GET_BUZZER);
        filter.addAction(Constants.P2P.RET_GET_INFRARED_SWITCH);
        filter.addAction(Constants.P2P.ACK_RET_SET_INFRARED_SWITCH);
        filter.addAction(Constants.P2P.RET_GET_WIRED_ALARM_INPUT);
        filter.addAction(Constants.P2P.RET_GET_WIRED_ALARM_OUT);
        filter.addAction(Constants.P2P.ACK_RET_SET_WIRED_ALARM_INPUT);
        filter.addAction(Constants.P2P.ACK_RET_SET_WIRED_ALARM_OUT);

        mContext.registerReceiver(mReceiver, filter);
        isRegFilter = true;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (intent.getAction().equals(Constants.P2P.RET_GET_BIND_ALARM_ID)) {
                showImg_receive_alarm();
                String[] data = intent.getStringArrayExtra("data");
                int max_count = intent.getIntExtra("max_count", 0);
                last_bind_data = data;
                max_alarm_count = max_count;
                layout_alarm_switch.setClickable(true);
                add_alarm_item.setClickable(true);
                int count = 0;
                for (int i = 0; i < data.length; i++) {
                    if (data[i].equals(NpcCommon.mThreeNum)) {
                        img_receive_alarm
                                .setBackgroundResource(R.mipmap.ic_checkbox_on);
                        isReceiveAlarm = false;
                        count = count + 1;
                        return;
                    }
                }
                if (count == 0) {
                    img_receive_alarm
                            .setBackgroundResource(R.mipmap.ic_checkbox_off);
                    isReceiveAlarm = true;
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_SET_BIND_ALARM_ID)) {
                int result = intent.getIntExtra("result", -1);
                if (null != dialog_loading && dialog_loading.isShowing()) {
                    dialog_loading.dismiss();
                    dialog_loading = null;
                }
                if (result == Constants.P2P_SET.BIND_ALARM_ID_SET.SETTING_SUCCESS) {
                    P2PHandler.getInstance().getBindAlarmId(idOrIp,
                            contact.contactPassword);
                    T.showShort(mContext, R.string.modify_success);
                } else {
                    T.showShort(mContext, R.string.operator_error);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_ALARM_EMAIL)) {
                // isRET=true;
                String email = intent.getStringExtra("email");
                int result = intent.getIntExtra("result", 0);
                getSMTPMessage(result);
                if (email.equals("") || email.equals("0")) {
                    email_text.setText(R.string.unbound);
                } else {
                    email_text.setText(email);
                }
                showEmailState();
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_ALARM_EMAIL_WITHSMTP)) {
                // isRET=true;
                String contectid = intent.getStringExtra("contectid");
                Log.i("dxsemail", "contectid-->" + contectid);
                if (contectid != null && contectid.equals(idOrIp)) {
                    String email = intent.getStringExtra("email");
                    encrypt = intent.getIntExtra("encrypt", -1);
                    String[] SmptMessage = intent
                            .getStringArrayExtra("SmptMessage");
                    int result = intent.getIntExtra("result", 0);
                    int isSupport = intent.getIntExtra("isSupport", -1);
                    if (isSupport == 1) {
                        isSupportManual = true;
                    } else {
                        isSupportManual = false;
                    }
                    getSMTPMessage(result);
                    // sendEmail = SmptMessage[1];
                    sendEmail = email;
                    emailRobot = SmptMessage[0];
                    emailPwd = SmptMessage[2];
                    senderEmail = SmptMessage[1];
                    smtpport = intent.getIntExtra("smtpport", -1);
                    if (email.equals("") || email.equals("0")) {
                        email_text.setText(R.string.unbound);
                    } else {
                        email_text.setText(email);
                    }
                    showEmailState();
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_SET_ALARM_EMAIL)) {

            } else if (intent.getAction().equals(Constants.P2P.RET_GET_MOTION)) {
                int state = intent.getIntExtra("motionState", -1);
                if (state == Constants.P2P_SET.MOTION_SET.MOTION_DECT_ON) {
                    motion_switch = Constants.P2P_SET.MOTION_SET.MOTION_DECT_ON;
                    motion_img.setBackgroundResource(R.mipmap.ic_checkbox_on);
                } else {
                    motion_switch = Constants.P2P_SET.MOTION_SET.MOTION_DECT_OFF;
                    motion_img
                            .setBackgroundResource(R.mipmap.ic_checkbox_off);
                }
                showMotionState();
            } else if (intent.getAction().equals(Constants.P2P.RET_SET_MOTION)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.MOTION_SET.SETTING_SUCCESS) {
                    if (cur_modify_motion_state == Constants.P2P_SET.MOTION_SET.MOTION_DECT_ON) {
                        motion_switch = Constants.P2P_SET.MOTION_SET.MOTION_DECT_ON;
                        motion_img
                                .setBackgroundResource(R.mipmap.ic_checkbox_on);
                    } else {
                        motion_switch = Constants.P2P_SET.MOTION_SET.MOTION_DECT_OFF;
                        motion_img
                                .setBackgroundResource(R.mipmap.ic_checkbox_off);
                    }
                    showMotionState();
                    T.showShort(mContext, R.string.modify_success);
                } else {
                    showMotionState();
                    T.showShort(mContext, R.string.operator_error);
                }
            } else if (intent.getAction().equals(Constants.P2P.RET_GET_BUZZER)) {
                int state = intent.getIntExtra("buzzerState", -1);
                updateBuzzer(state);
                showBuzzerTime();
            } else if (intent.getAction().equals(Constants.P2P.RET_SET_BUZZER)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.BUZZER_SET.SETTING_SUCCESS) {
                    updateBuzzer(cur_modify_buzzer_state);
                    showBuzzerTime();
                    T.showShort(mContext, R.string.modify_success);
                } else {
                    showBuzzerTime();
                    T.showShort(mContext, R.string.operator_error);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_GET_NPC_SETTINGS)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:get npc settings");
                    P2PHandler.getInstance().getNpcSettings(idOrIp,
                            contact.contactPassword);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_SET_BIND_ALARM_ID)) {
                int result = intent.getIntExtra("result", -1);

                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    if (null != dialog_loading && dialog_loading.isShowing()) {
                        dialog_loading.dismiss();
                        dialog_loading = null;
                    }
                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:set alarm bind id");
                    P2PHandler.getInstance().setBindAlarmId(idOrIp,
                            contact.contactPassword, new_data.length, new_data);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_GET_BIND_ALARM_ID)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:get alarm bind id");
                    P2PHandler.getInstance().getBindAlarmId(idOrIp,
                            contact.contactPassword);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_GET_ALARM_EMAIL)) {
                int result = intent.getIntExtra("result", -1);
                // if(isRET){
                // //如果接收到数据则不处理此处数据，原因是运行过程中运行异常，收到邮箱数据之后任然返回错误，导致不停GET
                // return;
                // }
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:get alarm email");
                    P2PHandler.getInstance().getAlarmEmail(idOrIp,
                            contact.contactPassword);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_SET_MOTION)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:set npc settings motion");
                    P2PHandler.getInstance().setMotion(idOrIp,
                            contact.contactPassword, cur_modify_motion_state);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_SET_BUZZER)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    Intent i = new Intent();
                    i.setAction(Constants.Action.CONTROL_SETTING_PWD_ERROR);
                    mContext.sendBroadcast(i);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    Log.e("my", "net error resend:set npc settings buzzer");
                    P2PHandler.getInstance().setBuzzer(idOrIp,
                            contact.contactPassword, cur_modify_buzzer_state);
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_INFRARED_SWITCH)) {
                int state = intent.getIntExtra("state", -1);
                if (state == Constants.P2P_SET.INFRARED_SWITCH.INFRARED_SWITCH_ON) {
                    change_pir.setVisibility(RelativeLayout.VISIBLE);
                    current_infrared_state = false;
                    pir_img.setBackgroundResource(R.mipmap.ic_checkbox_on);
                } else if (state == Constants.P2P_SET.INFRARED_SWITCH.INFRARED_SWITCH_OFF) {
                    change_pir.setVisibility(RelativeLayout.VISIBLE);
                    current_infrared_state = true;
                    pir_img.setBackgroundResource(R.mipmap.ic_checkbox_off);
                }
                showImg_infrared_switch();
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_SET_INFRARED_SWITCH)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    if (current_infrared_state == true) {
                        P2PHandler.getInstance().setInfraredSwitch(idOrIp,
                                contact.contactPassword, 1);
                    } else {
                        P2PHandler.getInstance().setInfraredSwitch(idOrIp,
                                contact.contactPassword, 0);
                    }
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_SUCCESS) {
                    if (current_infrared_state == true) {
                        current_infrared_state = false;
                        pir_img.setBackgroundResource(R.mipmap.ic_checkbox_on);
                    } else {
                        current_infrared_state = true;
                        pir_img.setBackgroundResource(R.mipmap.ic_checkbox_off);
                    }
                    showImg_infrared_switch();
                }

            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_WIRED_ALARM_INPUT)) {
                int state = intent.getIntExtra("state", -1);
                if (state == Constants.P2P_SET.WIRED_ALARM_INPUT.ALARM_INPUT_ON) {
                    alarm_input_switch.setVisibility(RelativeLayout.VISIBLE);
                    isOpenWriedAlarmInput = false;
                    img_alarm_input
                            .setBackgroundResource(R.mipmap.ic_checkbox_on);
                } else if (state == Constants.P2P_SET.WIRED_ALARM_INPUT.ALARM_INPUT_OFF) {
                    alarm_input_switch.setVisibility(RelativeLayout.VISIBLE);
                    isOpenWriedAlarmInput = true;
                    img_alarm_input
                            .setBackgroundResource(R.mipmap.ic_checkbox_off);
                }
                showImg_wired_alarm_input();
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_GET_WIRED_ALARM_OUT)) {
                int state = intent.getIntExtra("state", -1);
                if (state == Constants.P2P_SET.WIRED_ALARM_OUT.ALARM_OUT_ON) {
                    alarm_out_switch.setVisibility(RelativeLayout.VISIBLE);
                    isOpenWriedAlarmOut = false;
                    img_alarm_out
                            .setBackgroundResource(R.mipmap.ic_checkbox_on);
                } else if (state == Constants.P2P_SET.WIRED_ALARM_OUT.ALARM_OUT_OFF) {
                    alarm_out_switch.setVisibility(RelativeLayout.VISIBLE);
                    isOpenWriedAlarmOut = true;
                    img_alarm_out
                            .setBackgroundResource(R.mipmap.ic_checkbox_off);
                }
                showImg_wired_alarm_out();
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_SET_WIRED_ALARM_INPUT)) {
                int result = intent.getIntExtra("state", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    if (isOpenWriedAlarmInput == true) {
                        P2PHandler.getInstance().setWiredAlarmInput(idOrIp,
                                contact.contactPassword, 1);
                    } else {
                        P2PHandler.getInstance().setWiredAlarmInput(idOrIp,
                                contact.contactPassword, 0);
                    }

                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_SUCCESS) {
                    if (isOpenWriedAlarmInput == true) {
                        isOpenWriedAlarmInput = false;
                        img_alarm_input
                                .setBackgroundResource(R.mipmap.ic_checkbox_on);
                    } else {
                        isOpenWriedAlarmInput = true;
                        img_alarm_input
                                .setBackgroundResource(R.mipmap.ic_checkbox_off);
                    }
                    showImg_wired_alarm_input();
                }
            } else if (intent.getAction().equals(
                    Constants.P2P.ACK_RET_SET_WIRED_ALARM_OUT)) {
                int result = intent.getIntExtra("state", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    if (isOpenWriedAlarmOut == true) {
                        P2PHandler.getInstance().setWiredAlarmOut(idOrIp,
                                contact.contactPassword, 1);
                    } else {
                        P2PHandler.getInstance().setWiredAlarmOut(idOrIp,
                                contact.contactPassword, 0);
                    }

                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_SUCCESS) {
                    if (isOpenWriedAlarmOut == true) {
                        isOpenWriedAlarmOut = false;
                        img_alarm_out
                                .setBackgroundResource(R.mipmap.ic_checkbox_on);
                    } else {
                        isOpenWriedAlarmOut = true;
                        img_alarm_out
                                .setBackgroundResource(R.mipmap.ic_checkbox_off);
                    }
                    showImg_wired_alarm_out();
                }
            }
        }
    };

    private void getSMTPMessage(int result) {
        Log.i("dxsalarm", "result------>" + result);
        if ((byte) ((result >> 1) & (0x1)) == 0) {
            isSurportSMTP = false;
        } else {
            isSurportSMTP = true;
            if ((byte) ((result >> 4) & (0x1)) == 0) {
                isEmailChecked = true;
                if ((byte) ((result >> 2) & (0x1)) == 0) {
                    isEmailLegal = false;
                } else {
                    isEmailLegal = true;
                }
            } else {
                isEmailChecked = false;
            }

        }
    }

    @OnClick({R.id.change_email,R.id.add_alarm_item,R.id.change_buzzer,R.id.change_motion,R.id.radio_one,R.id.radio_two,R.id.radio_three,R.id.change_pir,
            R.id.alarm_input_switch,R.id.alarm_out_switch,R.id.layout_alarm_switch})
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.change_email:
                Intent modify_email = new Intent(mContext,
                        ModifyBoundEmailActivity.class);
                modify_email.putExtra("contact", contact);
                modify_email.putExtra("email", email_text.getText().toString());
                modify_email.putExtra("sendEmail", sendEmail);
                modify_email.putExtra("emailRoot", emailRobot);
                modify_email.putExtra("emailPwd", emailPwd);
                modify_email.putExtra("isEmailLegal", isEmailLegal);
                modify_email.putExtra("isSurportSMTP", isSurportSMTP);
                modify_email.putExtra("isEmailChecked", isEmailChecked);
                modify_email.putExtra("senderEmail", senderEmail);
                modify_email.putExtra("encrypt", encrypt);
                modify_email.putExtra("smtpport", smtpport);
                modify_email.putExtra("isSupportManual", isSupportManual);
                mContext.startActivity(modify_email);
                break;
            case R.id.add_alarm_item:
                if (last_bind_data.length <= 0 || (last_bind_data[0].equals("0") && last_bind_data.length == 1)) {
                    T.showShort(mContext, R.string.no_alarm_account);
                } else {
                    Intent it = new Intent(mContext, AlarmPushAccountActivity.class);
                    it.putExtra("contactId", contact.contactId);
                    it.putExtra("contactPassword", contact.contactPassword);
                    startActivity(it);
                }
                break;
            case R.id.change_buzzer:
                showProgress();
                if (buzzer_switch != Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_OFF) {
                    cur_modify_buzzer_state = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_OFF;
                } else {
                    cur_modify_buzzer_state = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_ONE_MINUTE;
                }
                P2PHandler.getInstance().setBuzzer(idOrIp, contact.contactPassword,
                        cur_modify_buzzer_state);
                break;
            case R.id.change_motion:
                showProgress_motion();
                if (motion_switch != Constants.P2P_SET.MOTION_SET.MOTION_DECT_OFF) {
                    cur_modify_motion_state = Constants.P2P_SET.MOTION_SET.MOTION_DECT_OFF;
                    P2PHandler.getInstance().setMotion(idOrIp,
                            contact.contactPassword, cur_modify_motion_state);
                } else {
                    cur_modify_motion_state = Constants.P2P_SET.MOTION_SET.MOTION_DECT_ON;
                    P2PHandler.getInstance().setMotion(idOrIp,
                            contact.contactPassword, cur_modify_motion_state);
                }
                break;
            case R.id.radio_one:
                showProgress();
                cur_modify_buzzer_state = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_ONE_MINUTE;
                P2PHandler.getInstance().setBuzzer(idOrIp, contact.contactPassword,
                        cur_modify_buzzer_state);
                break;
            case R.id.radio_two:
                showProgress();
                cur_modify_buzzer_state = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_TWO_MINUTE;
                P2PHandler.getInstance().setBuzzer(idOrIp, contact.contactPassword,
                        cur_modify_buzzer_state);
                break;
            case R.id.radio_three:
                showProgress();
                cur_modify_buzzer_state = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_THREE_MINUTE;
                P2PHandler.getInstance().setBuzzer(idOrIp, contact.contactPassword,
                        cur_modify_buzzer_state);
                break;
            case R.id.change_pir:
                showProgress_infrares_switch();
                if (current_infrared_state == true) {
                    modify_infrared_state = Constants.P2P_SET.INFRARED_SWITCH.INFRARED_SWITCH_ON;
                    P2PHandler.getInstance().setInfraredSwitch(idOrIp,
                            contact.contactPassword, modify_infrared_state);
                } else {
                    modify_infrared_state = Constants.P2P_SET.INFRARED_SWITCH.INFRARED_SWITCH_OFF;
                    P2PHandler.getInstance().setInfraredSwitch(idOrIp,
                            contact.contactPassword, modify_infrared_state);
                }
                break;
            case R.id.alarm_input_switch:
                showProgress_wired_alarm_input();
                if (isOpenWriedAlarmInput == true) {
                    P2PHandler.getInstance().setWiredAlarmInput(idOrIp,
                            contact.contactPassword, 1);
                } else {
                    P2PHandler.getInstance().setWiredAlarmInput(idOrIp,
                            contact.contactPassword, 0);
                }
                break;
            case R.id.alarm_out_switch:
                showProgress_wired_alarm_out();
                if (isOpenWriedAlarmOut == true) {
                    P2PHandler.getInstance().setWiredAlarmOut(idOrIp,
                            contact.contactPassword, 1);
                } else {
                    P2PHandler.getInstance().setWiredAlarmOut(idOrIp,
                            contact.contactPassword, 0);
                }
                break;
            case R.id.layout_alarm_switch:
                showProgress_receive_alarm();
                if (isReceiveAlarm == true) {
                    if (last_bind_data.length >= max_alarm_count) {
                        T.showShort(mContext, R.string.alarm_push_limit);
                        showImg_receive_alarm();
                        return;
                    }
                    new_data = new String[last_bind_data.length + 1];
                    for (int i = 0; i < last_bind_data.length; i++) {
                        new_data[i] = last_bind_data[i];
                    }
                    new_data[new_data.length - 1] = NpcCommon.mThreeNum;
                    // last_bind_data=new_data;
                    P2PHandler.getInstance().setBindAlarmId(idOrIp,
                            contact.contactPassword, new_data.length, new_data);
                } else {
                    new_data = new String[last_bind_data.length - 1];
                    int count = 0;
                    for (int i = 0; i < last_bind_data.length; i++) {
                        if (!last_bind_data[i].equals(NpcCommon.mThreeNum)) {
                            new_data[count] = last_bind_data[i];
                            count++;
                        }
                    }
                    if (new_data.length == 0) {
                        new_data = new String[]{"0"};
                    }
                    P2PHandler.getInstance().setBindAlarmId(idOrIp,
                            contact.contactPassword, new_data.length, new_data);
                }
                break;
        }
    }

    public void updateBuzzer(int state) {
        if (state == Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_ONE_MINUTE) {
            buzzer_switch = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_ONE_MINUTE;
            buzzer_img.setBackgroundResource(R.mipmap.ic_checkbox_on);
            change_buzzer.setBackgroundResource(R.drawable.tiao_bg_up);
            buzzer_time.setVisibility(RelativeLayout.VISIBLE);
            radio_one.setChecked(true);
        } else if (state == Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_TWO_MINUTE) {
            buzzer_switch = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_TWO_MINUTE;
            buzzer_img.setBackgroundResource(R.mipmap.ic_checkbox_on);
            change_buzzer.setBackgroundResource(R.drawable.tiao_bg_up);
            buzzer_time.setVisibility(RelativeLayout.VISIBLE);
            radio_two.setChecked(true);
        } else if (state == Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_THREE_MINUTE) {
            buzzer_switch = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_ON_THREE_MINUTE;
            buzzer_img.setBackgroundResource(R.mipmap.ic_checkbox_on);
            change_buzzer.setBackgroundResource(R.drawable.tiao_bg_up);
            buzzer_time.setVisibility(RelativeLayout.VISIBLE);
            radio_three.setChecked(true);
        } else {
            buzzer_switch = Constants.P2P_SET.BUZZER_SET.BUZZER_SWITCH_OFF;
            buzzer_img.setBackgroundResource(R.mipmap.ic_checkbox_off);
            change_buzzer.setBackgroundResource(R.drawable.tiao_bg_single);
            buzzer_time.setVisibility(RelativeLayout.GONE);
        }
    }

    public void showProgress() {
        progressBar.setVisibility(RelativeLayout.VISIBLE);
        buzzer_img.setVisibility(RelativeLayout.GONE);
        change_buzzer.setEnabled(false);
        radio_one.setEnabled(false);
        radio_two.setEnabled(false);
        radio_three.setEnabled(false);
    }

    public void showBuzzerTime() {
        progressBar.setVisibility(RelativeLayout.GONE);
        buzzer_img.setVisibility(RelativeLayout.VISIBLE);
        change_buzzer.setEnabled(true);
        radio_one.setEnabled(true);
        radio_two.setEnabled(true);
        radio_three.setEnabled(true);
    }

    public void showMotionState() {
        progressBar_motion.setVisibility(RelativeLayout.GONE);
        motion_img.setVisibility(RelativeLayout.VISIBLE);
        change_motion.setEnabled(true);
    }

    public void showProgress_motion() {
        progressBar_motion.setVisibility(RelativeLayout.VISIBLE);
        motion_img.setVisibility(RelativeLayout.GONE);
        change_motion.setEnabled(false);
    }


    public void showEmailState() {
        progressBar_email.setVisibility(RelativeLayout.GONE);
        email_text.setVisibility(RelativeLayout.VISIBLE);
        change_email.setEnabled(true);
    }

    public void showProgress_email() {
        progressBar_email.setVisibility(RelativeLayout.VISIBLE);
        email_text.setVisibility(RelativeLayout.GONE);
        change_email.setEnabled(false);
    }

    public void showProgress_infrares_switch() {
        progressBar_pir.setVisibility(ProgressBar.VISIBLE);
        pir_img.setVisibility(ImageView.GONE);
    }

    public void showImg_infrared_switch() {
        progressBar_pir.setVisibility(progressBar.GONE);
        pir_img.setVisibility(ImageView.VISIBLE);
    }

    public void showProgress_wired_alarm_input() {
        progressBar_alarm_input.setVisibility(ProgressBar.VISIBLE);
        img_alarm_input.setVisibility(ImageView.GONE);
    }

    public void showImg_wired_alarm_input() {
        progressBar_alarm_input.setVisibility(ProgressBar.GONE);
        img_alarm_input.setVisibility(ImageView.VISIBLE);
    }

    public void showProgress_wired_alarm_out() {
        progressBar_alarm_out.setVisibility(ProgressBar.VISIBLE);
        img_alarm_out.setVisibility(ImageView.GONE);
    }

    public void showImg_wired_alarm_out() {
        progressBar_alarm_out.setVisibility(ProgressBar.GONE);
        img_alarm_out.setVisibility(ImageView.VISIBLE);
    }

    public void showProgress_receive_alarm() {
        progressBar_receive_alarm.setVisibility(ProgressBar.VISIBLE);
        img_receive_alarm.setVisibility(progressBar.GONE);
    }

    public void showImg_receive_alarm() {
        progressBar_receive_alarm.setVisibility(ProgressBar.GONE);
        img_receive_alarm.setVisibility(progressBar.VISIBLE);
    }

    @Override
    public void onDestroy() {
        if (isRegFilter) {
            mContext.unregisterReceiver(mReceiver);
            isRegFilter = false;
        }
        Intent it = new Intent();
        it.setAction(Constants.Action.CONTROL_BACK);
        mContext.sendBroadcast(it);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent i = new Intent(mContext,ApMonitorActivity.class);
            i.putExtra("contact",contact);
            startActivity(i);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
