package com.hrsst.housekeeper.mvp.addCamera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.hrsst.housekeeper.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddCameraSecondActivity extends Activity {

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_camera_second);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.add_camera_action_two)
    public void onClick() {
        Intent i = new Intent(AddCameraSecondActivity.this, AddCameraThirdActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
