package com.hrsst.housekeeper.mvp.manualAddCamera;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hrsst.housekeeper.AppComponent;
import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.common.baseActivity.BaseActivity;
import com.hrsst.housekeeper.common.global.Constants;
import com.hrsst.housekeeper.common.utils.T;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class ManualAddCameraActivity extends BaseActivity implements ManualAddCameraView {
    @Inject
    ManualAddCameraPresenter manualAddCameraPresenter;
    @Bind(R.id.camera_id)
    EditText cameraId;
    @Bind(R.id.camera_pwd)
    EditText cameraPwd;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.add_camera)
    RelativeLayout addCamera;
    private Context mContext;
    private String cameraID;
    private String pwd;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerManualAddCameraComponent.builder()
                .appComponent(appComponent)
                .manualAddCameraModule(new ManualAddCameraModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_add_camera);
        ButterKnife.bind(this);
        mContext = this;

        RxView.clicks(addCamera).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        cameraID = cameraId.getText().toString().trim();
                        pwd = cameraPwd.getText().toString().trim();
                        manualAddCameraPresenter.checkPassword(cameraID, pwd);
                    }
                });
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
    public void addCameraResult(String msg) {
        manualAddCameraPresenter.getAlarmId(cameraID,pwd);
    }

    @Override
    public void errorMessage(String msg) {
        T.showShort(mContext, msg);
    }

    @Override
    public void bindAlarm(String[] new_data) {
        manualAddCameraPresenter.bindAlarmId(new_data,cameraID,pwd);
    }

    @Override
    protected void onDestroy() {
        manualAddCameraPresenter.unRegisterReceiver();
        super.onDestroy();
    }

    @Override
    public void finishActivity(){
        T.showShort(mContext, "添加成功");
        Intent intent = new Intent();
        intent.setAction(Constants.PUSH_CAMERA_DATA);
        sendBroadcast(intent);
        finish();
    }
}
