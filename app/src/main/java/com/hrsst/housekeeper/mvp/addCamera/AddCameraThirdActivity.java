package com.hrsst.housekeeper.mvp.addCamera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.common.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddCameraThirdActivity extends Activity{
    @Bind(R.id.camera_wifi_name)
    TextView cameraWifiName;
    @Bind(R.id.camera_wifi_pwd)
    EditText cameraWifiPwd;
    private Context mContext;
    private String ssid;
    private int mLocalIp;
    boolean bool1, bool2, bool3, bool4;
    private byte mAuthMode;
    private byte AuthModeOpen = 0;
    private byte AuthModeWPA = 3;
    private byte AuthModeWPA1PSKWPA2PSK = 9;
    private byte AuthModeWPA1WPA2 = 8;
    private byte AuthModeWPA2 = 6;
    private byte AuthModeWPA2PSK = 7;
    private byte AuthModeWPAPSK = 4;
    private boolean isWifiOpen = false;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_camera_third);
        ButterKnife.bind(this);
        mContext = this;
        currentWifi();
    }

    @OnClick(R.id.next_add_camera_second_btn)
    public void onClick() {
        // TODO Auto-generated method stub
        InputMethodManager manager = (InputMethodManager) getSystemService(mContext.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(cameraWifiPwd.getWindowToken(), 0);
        }
        String wifiPwd = cameraWifiPwd.getText().toString();
        if (ssid == null || ssid.equals("")) {
            Toast.makeText(mContext, "请先将手机连接无线网络", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ssid.equals("<unknown ssid>")) {
            Toast.makeText(mContext, "请先将手机连接无线网络", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isWifiOpen) {
            if (null == wifiPwd || wifiPwd.length() <= 0
                    && (type == 1 || type == 2)) {
                Toast.makeText(mContext, "请输入Wi-Fi密码", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Intent device_network = new Intent(mContext, AddWaitActicity.class);
        device_network.putExtra("ssidname", ssid);
        device_network.putExtra("wifiPwd", wifiPwd);
        device_network.putExtra("type", mAuthMode);
        device_network.putExtra("LocalIp", mLocalIp);
        device_network.putExtra("isNeedSendWifi", true);
        startActivity(device_network);
        finish();
    }

    private void currentWifi() {
        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (!manager.isWifiEnabled())
            return;
        WifiInfo info = manager.getConnectionInfo();
        ssid = info.getSSID();
        mLocalIp = info.getIpAddress();
        List<ScanResult> datas = new ArrayList<>();
        if (!manager.isWifiEnabled())
            return;
        manager.startScan();
        datas = manager.getScanResults();
        if (ssid == null) {
            return;
        }
        if (ssid.equals("")) {
            return;
        }
        int a = ssid.charAt(0);
        if (a == 34) {
            ssid = ssid.substring(1, ssid.length() - 1);
        }
        if (!ssid.equals("<unknown ssid>") && !ssid.equals("0x")) {
            cameraWifiName.setText(ssid);
        }
        for (int i = 0; i < datas.size(); i++) {
            ScanResult result = datas.get(i);
            if (!result.SSID.equals(ssid)) {
                continue;
            }
            if (Utils.isWifiOpen(result)) {
                type = 0;
                isWifiOpen = true;
            } else {
                type = 1;
                isWifiOpen = false;
            }
            bool1 = result.capabilities.contains("WPA-PSK");
            bool2 = result.capabilities.contains("WPA2-PSK");
            bool3 = result.capabilities.contains("WPA-EAP");
            bool4 = result.capabilities.contains("WPA2-EAP");
            if (result.capabilities.contains("WEP")) {
                this.mAuthMode = this.AuthModeOpen;
            }
            if ((bool1) && (bool2)) {
                mAuthMode = AuthModeWPA1PSKWPA2PSK;
            } else if (bool2) {
                this.mAuthMode = this.AuthModeWPA2PSK;
            } else if (bool1) {
                this.mAuthMode = this.AuthModeWPAPSK;
            } else if ((bool3) && (bool4)) {
                this.mAuthMode = this.AuthModeWPA1WPA2;
            } else if (bool4) {
                this.mAuthMode = this.AuthModeWPA2;
            } else {
                if (!bool3)
                    break;
                this.mAuthMode = this.AuthModeWPA;
            }
        }
    }

}

