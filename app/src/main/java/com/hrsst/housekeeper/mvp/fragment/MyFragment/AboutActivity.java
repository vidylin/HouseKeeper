package com.hrsst.housekeeper.mvp.fragment.MyFragment;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.widget.TextView;

import com.hrsst.housekeeper.R;

public class AboutActivity extends Activity {

    private Context mContext;
    private TextView about_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mContext = this;
        about_version = (TextView) findViewById(R.id.about_version);
        int version =getlocalVersion();
        about_version.setText(version+".0");
    }

    private int getlocalVersion(){
        int localversion = 0;
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            localversion = info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localversion;
    }
}
