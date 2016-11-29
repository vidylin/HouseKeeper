package com.hrsst.housekeeper.mvp.modifyCameraInfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hrsst.housekeeper.AppComponent;
import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.common.baseActivity.BaseActivity;
import com.hrsst.housekeeper.common.data.Contact;
import com.hrsst.housekeeper.common.global.Constants;
import com.hrsst.housekeeper.common.utils.T;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyCameraPwdActivity extends BaseActivity implements ModifyCameraPwdView {

    @Inject
    ModifyCameraPwdPresenter modifyCameraPwdPresenter;
    @Bind(R.id.camera_ex_pwd)
    EditText cameraExPwd;
    @Bind(R.id.camera_pwd_now)
    EditText cameraPwdNow;
    @Bind(R.id.camera_pwd_now2)
    EditText cameraPwdNow2;
    @Bind(R.id.modify_camera_pwd)
    RelativeLayout modifyCameraPwd;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    private Context mContext;
    private Contact contact;
    private String newPwd;
    private int pos;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerModifyCameraPwdComponent.builder()
                .appComponent(appComponent)
                .modifyCameraPwdModule(new ModifyCameraPwdModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_camera_pwd);
        ButterKnife.bind(this);
        mContext = this;
        contact = (Contact) getIntent().getExtras().getSerializable("contact");
        pos = getIntent().getExtras().getInt("position");
        regFilter();
    }

    private void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.P2P.ACK_RET_SET_DEVICE_PASSWORD);
        filter.addAction(Constants.P2P.RET_SET_DEVICE_PASSWORD);
        filter.addAction(Constants.P2P.RET_DEVICE_NOT_SUPPORT);
        mContext.registerReceiver(mReceiver, filter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (intent.getAction().equals(Constants.P2P.RET_SET_DEVICE_PASSWORD)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.DEVICE_PASSWORD_SET.SETTING_SUCCESS) {
                    modifyCameraPwdPresenter.modifyCameraPwdToServer(contact.contactId,newPwd);
                } else {
                    hideLoading();
                    T.showShort(mContext, R.string.operator_error);
                }
            } else if (intent.getAction().equals(Constants.P2P.ACK_RET_SET_DEVICE_PASSWORD)) {
                int result = intent.getIntExtra("result", -1);
                if (result == Constants.P2P_SET.ACK_RESULT.ACK_PWD_ERROR) {
                    hideLoading();
                    T.showShort(mContext, R.string.old_pwd_error);
                } else if (result == Constants.P2P_SET.ACK_RESULT.ACK_NET_ERROR) {
                    hideLoading();
                    T.showShort(mContext, R.string.net_error_operator_fault);
                }
            } else if (intent.getAction().equals(Constants.P2P.RET_DEVICE_NOT_SUPPORT)) {
                hideLoading();
                finish();
                T.showShort(mContext, R.string.not_support);
            }
        }
    };

    @OnClick(R.id.modify_camera_pwd)
    public void onClick(){
        String oldPwd = cameraExPwd.getText().toString().trim();
        newPwd = cameraPwdNow.getText().toString().trim();
        String newPwd2 = cameraPwdNow2.getText().toString().trim();
        modifyCameraPwdPresenter.ModifyCameraPwd(contact.contactId,oldPwd,newPwd,newPwd2);
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
    public void modifyCameraPwdResult(String msg,String pwd) {
        T.showShort(mContext,msg);
        contact.contactPassword = pwd;
        Intent in = new Intent();
        in.putExtra("contact",contact);
        in.putExtra("position",pos);
        setResult(100,in);
        finish();
    }

    @Override
    public void errorMessage(String msg) {
        T.showShort(mContext,msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
