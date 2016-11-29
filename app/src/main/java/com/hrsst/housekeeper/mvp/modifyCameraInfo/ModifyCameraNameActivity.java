package com.hrsst.housekeeper.mvp.modifyCameraInfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hrsst.housekeeper.AppComponent;
import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.common.baseActivity.BaseActivity;
import com.hrsst.housekeeper.common.data.Contact;
import com.hrsst.housekeeper.common.utils.T;
import com.hrsst.housekeeper.mvp.apmonitor.ApMonitorActivity;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class ModifyCameraNameActivity extends BaseActivity implements ModifyCameraPwdView{
    private ModifyCameraNamePresenter modifyCameraNamePresenter;
    @Bind(R.id.camera_ex_pwd)
    EditText cameraExPwd;
    @Bind(R.id.modify_camera_pwd)
    RelativeLayout modifyCameraPwd;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    private Context mContext;
    private Contact contact;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        modifyCameraNamePresenter = new ModifyCameraNamePresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_camera_name);
        ButterKnife.bind(this);
        mContext= this;
        contact = (Contact) getIntent().getExtras().getSerializable("contact");
        cameraExPwd.setText(contact.contactName);
        RxView.clicks(modifyCameraPwd).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String cameraName = cameraExPwd.getText().toString().trim();
                        modifyCameraNamePresenter.modifyName(cameraName,contact.contactId);
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
    public void modifyCameraPwdResult(String msg, String pwd) {
        contact.contactName = pwd;
        T.showShort(mContext,msg);
        Intent in = new Intent(mContext,ApMonitorActivity.class);
        in.putExtra("contact",contact);
        startActivity(in);
        finish();
    }

    @Override
    public void errorMessage(String msg) {
        T.showShort(mContext,msg);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(mContext, ApMonitorActivity.class);
            i.putExtra("contact", contact);
            startActivity(i);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
