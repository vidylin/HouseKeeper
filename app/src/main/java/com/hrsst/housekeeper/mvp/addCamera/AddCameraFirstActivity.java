package com.hrsst.housekeeper.mvp.addCamera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.hrsst.housekeeper.R;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddCameraFirstActivity extends Activity {

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_camera_first);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.add_camera_action_one)
    public void onClick() {
        // TODO Auto-generated method stub
        Intent i = new Intent(AddCameraFirstActivity.this, AddCameraSecondActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
