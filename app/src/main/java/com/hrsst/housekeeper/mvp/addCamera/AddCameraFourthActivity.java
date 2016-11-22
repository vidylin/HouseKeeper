package com.hrsst.housekeeper.mvp.addCamera;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.hrsst.housekeeper.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddCameraFourthActivity extends Activity {
    private String contactId;
    private Context mContext;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_camera_four);
        ButterKnife.bind(this);
        mContext = this;
        contactId = getIntent().getExtras().getString("contactId");
    }

    @OnClick(R.id.add_camera_action_four)
    public void onClick(){
        finish();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}
