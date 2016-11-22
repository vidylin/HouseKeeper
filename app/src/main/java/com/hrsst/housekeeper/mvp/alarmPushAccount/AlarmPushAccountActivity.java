package com.hrsst.housekeeper.mvp.alarmPushAccount;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.common.global.Constants;
import com.hrsst.housekeeper.common.utils.T;
import com.hrsst.housekeeper.common.utils.Utils;
import com.hrsst.housekeeper.common.widget.NormalDialog;
import com.lib.addBar.AddBar;
import com.lib.addBar.OnItemChangeListener;
import com.lib.addBar.OnLeftIconClickListener;
import com.p2p.core.P2PHandler;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlarmPushAccountActivity extends AppCompatActivity {

    @Bind(R.id.progressBar_alarmId)
    ProgressBar progressBar_alarmId;
    @Bind(R.id.add_alarm_item)
    RelativeLayout add_alarm_item;
    private Context mContext;
    String contactPassword;
    String contactId;
    boolean isRegFilter = false;
    AddBar addbar;
    String[] last_bind_data;
    NormalDialog dialog_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_push_account);
        ButterKnife.bind(this);
        mContext = this;
        contactPassword = getIntent().getStringExtra("contactPassword");
        contactId = getIntent().getStringExtra("contactId");
        initComponent();
        regFilter();
        P2PHandler.getInstance().getBindAlarmId(contactId, contactPassword);
    }

    public void initComponent() {
        addbar = (AddBar) findViewById(R.id.add_bar);
        progressBar_alarmId = (ProgressBar) findViewById(R.id.progressBar_alarmId);
        add_alarm_item = (RelativeLayout) findViewById(R.id.add_alarm_item);
        addbar.setOnItemChangeListener(new OnItemChangeListener() {

            @Override
            public void onChange(int item) {
                // TODO Auto-generated method stub
                if (item > 0) {
                    add_alarm_item.setBackgroundResource(R.drawable.tiao_bg_up);
                } else {
                    add_alarm_item.setBackgroundResource(R.drawable.tiao_bg_single);
                }
            }

        });
        addbar.setOnLeftIconClickListener(new OnLeftIconClickListener() {

            @Override
            public void onClick(View icon, final int position) {
                // TODO Auto-generated method stub
                NormalDialog dialog = new NormalDialog(mContext, mContext
                        .getResources().getString(R.string.delete_alarm_id),
                        mContext.getResources().getString(
                                R.string.ensure_delete)
                                + last_bind_data[position] + "?", mContext
                        .getResources().getString(R.string.ensure),
                        mContext.getResources().getString(R.string.cancel));
                dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

                    @Override
                    public void onClick() {
                        // TODO Auto-generated method stub

                        if (null == dialog_loading) {
                            dialog_loading = new NormalDialog(mContext,
                                    mContext.getResources().getString(
                                            R.string.verification), "", "", "");
                            dialog_loading
                                    .setStyle(NormalDialog.DIALOG_STYLE_LOADING);
                        }
                        dialog_loading.showDialog();

                        String[] data = Utils.getDeleteAlarmIdArray(
                                last_bind_data, position);
                        last_bind_data = data;
                        P2PHandler.getInstance().setBindAlarmId(contactId,
                                contactPassword, data.length, data);
                    }
                });
                dialog.showDialog();
            }
        });

    }

    public void regFilter() {
        isRegFilter = true;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.P2P.RET_GET_BIND_ALARM_ID);
        filter.addAction(Constants.P2P.RET_SET_BIND_ALARM_ID);
        filter.addAction(Constants.P2P.ACK_RET_SET_BIND_ALARM_ID);
        filter.addAction(Constants.P2P.ACK_RET_GET_BIND_ALARM_ID);
        mContext.registerReceiver(br, filter);
    }

    BroadcastReceiver br = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (intent.getAction().equals(Constants.P2P.RET_GET_BIND_ALARM_ID)) {
                String[] data = intent.getStringArrayExtra("data");
                last_bind_data = data;
                int max_count = intent.getIntExtra("max_count", 0);
                addbar.removeAll();
                addbar.setMax_count(max_count);
                for (int i = 0; i < data.length; i++) {
                    addbar.addItem(data[i]);
                }
                showAlarmIdState();
            } else if (intent.getAction().equals(
                    Constants.P2P.RET_SET_BIND_ALARM_ID)) {
                int result = intent.getIntExtra("result", -1);
                if (null != dialog_loading && dialog_loading.isShowing()) {
                    dialog_loading.dismiss();
                    dialog_loading = null;
                }
                if (result == Constants.P2P_SET.BIND_ALARM_ID_SET.SETTING_SUCCESS) {
                    addbar.removeAll();
                    P2PHandler.getInstance().getBindAlarmId(contactId,
                            contactPassword);
                    T.showShort(mContext, R.string.modify_success);
                } else {
                    T.showShort(mContext, R.string.operator_error);
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
                    P2PHandler.getInstance().setBindAlarmId(contactId,
                            contactPassword, last_bind_data.length,
                            last_bind_data);
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
                    P2PHandler.getInstance().getBindAlarmId(contactId,
                            contactPassword);
                }
            }
        }
    };

    @OnClick(R.id.back_btn)
    public void onClick(View v) {
        if (v.getId() == R.id.back_btn) {
            finish();
        }
    }

    public void showAlarmIdState() {
        progressBar_alarmId.setVisibility(RelativeLayout.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegFilter == true) {
            mContext.unregisterReceiver(br);
            isRegFilter = false;
        }
    }
}
