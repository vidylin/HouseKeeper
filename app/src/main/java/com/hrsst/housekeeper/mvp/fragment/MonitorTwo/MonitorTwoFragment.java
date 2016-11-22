package com.hrsst.housekeeper.mvp.fragment.MonitorTwo;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.common.global.Constants;
import com.hrsst.housekeeper.common.utils.T;
import com.hrsst.housekeeper.common.utils.Utils;
import com.hrsst.housekeeper.common.widget.SwitchPopwindow;
import com.hrsst.housekeeper.common.yoosee.FisheyeSetHandler;
import com.hrsst.housekeeper.entity.Sensor;
import com.hrsst.housekeeper.mvp.apmonitor.ApMonitorActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/11/8.
 */

public class MonitorTwoFragment extends Fragment{
    private Context mContext;
    ImageView iv_monitor_control;
    private List<Sensor> sensors = new ArrayList<Sensor>();
    boolean isRegFilter=false;
    private int SwitchPopState=0;//开关弹出窗状态 0还在获取，1获取结束
    private View view;
    private final static int SwitchH=117;
    boolean isSupport=true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view=inflater.inflate(R.layout.fragment_monitor_two, container, false);
        mContext=getActivity();
        iv_monitor_control=(ImageView)view.findViewById(R.id.iv_monitor_control);
        iv_monitor_control.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(!isSupport){
                    T.showShort(mContext, getResources().getString(R.string.not_support));
                    return;
                }
                showSwitchPop(sensors);
            }
        });
        regFilter();
        SwitchPopState=0;
        return view;
    }
    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.P2P.RET_GET_SENSOR_WORKMODE);
        filter.addAction(Constants.P2P.RET_GET_LAMPSTATE);
        filter.addAction(Constants.P2P.RET_DEVICE_NOT_SUPPORT);
        filter.addAction(Constants.Action.NEW_MONITOR);
        mContext.registerReceiver(mReceiver, filter);
        isRegFilter = true;
    }

    BroadcastReceiver mReceiver=new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            int iSrcID = intent.getIntExtra("iSrcID", 0);
            byte boption = intent.getByteExtra("boption", (byte) -1);
            byte[] data = intent.getByteArrayExtra("data");
            if(intent.getAction().equals(Constants.P2P.RET_GET_SENSOR_WORKMODE)){
                //获取传感器防护计划返回
                if (boption == Constants.FishBoption.MESG_GET_OK) {
                    int len1= Utils.bytesToInt(data, 5);
                    int len2=Utils.bytesToInt(data, 10);
                    paserSensorData(data, data[4], len1, data[9], len2);
                    getLampState((byte) 1);
                } else {
//	                    T.showLong(mContext, "获取错误");
                }
                SwitchPopState=1;
                if(switchPop!=null){
                    //展示获取的结果
                    switchPop.getSensored(SwitchPopState);
                }
            }else if(intent.getAction().equals(Constants.P2P.RET_GET_LAMPSTATE)){
                if (boption == Constants.FishBoption.MESG_GET_OK) {
                    Sensor sensor=getSensorByData(data,4);
                    if(sensor!=null){
                        sensor.setLampState(data[3]);
                        if(switchPop!=null){
                            switchPop.getSensored(SwitchPopState);
                            switchPop.updataswitch(getSensorPosition(sensor));
                        }else{
                            Log.e("dxsTest","switchPop为空");
                        }
                    }else{
                        Log.e("dxsTest","sensor为空");
                    }
                }
            }else if(intent.getAction().equals(Constants.P2P.RET_DEVICE_NOT_SUPPORT)){
                isSupport=false;
            }else if(intent.getAction().equals(Constants.Action.NEW_MONITOR)){
                isSupport=true;
                sensors.clear();
                getAllSensorData();
                if(switchPop!=null&&switchPop.isShowing()){
                    switchPop.dismiss();
                }
            }
        }
    };
    /**
     * 设置
     * @param sensors
     */
    SwitchPopwindow switchPop;
    private void showSwitchPop(List<Sensor> sensors){
        switchPop=new SwitchPopwindow(mContext, Utils.dip2px(mContext,SwitchH),sensors);
        switchPop.setOnSwitchListner(popwindowListner);
        switchPop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        switchPop.getSensored(SwitchPopState);
    }

    private SwitchPopwindow.OnSwitchListner popwindowListner=new SwitchPopwindow.OnSwitchListner() {
        @Override
        public void onSwitchClick(int position, Sensor sensor) {
            //带开关的传感器的开关点击
            if(sensor.isControlSensor()){
                if(sensor.getLampState()==1||sensor.getLampState()==3){
                    getLampState((byte) 3,sensor);
                }else if(sensor.getLampState()==2||sensor.getLampState()==4){
                    getLampState((byte) 2,sensor);
                }
                sensor.setLampState((byte) 0);
                switchPop.updataswitch(position);
            }
        }
    };
    /**
     * 获取所有的一般传感器
     */
    private void getAllSensorData() {
        if(ApMonitorActivity.mContact!=null){
            Log.e("leleTest", "callId="+ApMonitorActivity.callId+"--"+"password="+ ApMonitorActivity.password);
            FisheyeSetHandler.getInstance().sGetSenSorWorkMode(ApMonitorActivity.callId, ApMonitorActivity.password);
        }
    }

    /**
     * 解析指定传感器类型数据
     *
     * @param data
     * @param Controcons
     * @param controlen
     * @param Sensorcons
     * @param Sensorlen
     */
    private void paserSensorData(byte[] data, byte Controcons, int controlen, byte Sensorcons, int Sensorlen) {
        byte[] contro = new byte[21];
        for (int i = 0; i < Controcons; i++) {
            System.arraycopy(data, 14 + i * contro.length, contro, 0, contro.length);
            Sensor sensor = new Sensor(Sensor.SENSORCATEGORY_NORMAL, contro, contro[0]);
            //sensors.add(sensor);
        }
        if(data[3]==0){//没有防护计划
            byte[] sens = new byte[24];
            for (int i = 0; i < Sensorcons; i++) {
                System.arraycopy(data, 14 + controlen + i * sens.length, sens, 0, sens.length);
                Sensor sensor = new Sensor(Sensor.SENSORCATEGORY_NORMAL, sens, sens[0]);
                if(sensor.isControlSensor()){
                    sensors.add(sensor);
                }
            }
        }else if(data[3]==1){//有24位的防护计划
            byte[] sens = new byte[49];
            byte[] RealSens=new byte[24];
            for (int i = 0; i < Sensorcons; i++) {
                System.arraycopy(data, 14 + controlen + i * sens.length, sens, 0, sens.length);
                System.arraycopy(sens, 0, RealSens, 0, RealSens.length);
                Sensor sensor = new Sensor(Sensor.SENSORCATEGORY_NORMAL, RealSens, RealSens[0]);
                if(sensor.isControlSensor()){
                    sensors.add(sensor);
                }
            }
        }
    }

    /**
     * 获取所有插座状态
     * @param lamstate 1.查询  2.开  3.关闭
     */
    private void getLampState(byte lamstate){
        for (Sensor sensor:sensors){
            Log.e("leleTest", "name="+sensor.getName()+"--"+sensor.getSensorType());
            if(sensor.isControlSensor()){
                getLampState(lamstate,sensor);
            }
        }
    }

    /**
     * 获取或者设置插座状态
     * @param lamstate 0.查询  2.开  3.关闭
     * @param sensor
     */
    private void getLampState(byte lamstate,Sensor sensor){
        if(sensor.isControlSensor()){
            FisheyeSetHandler.getInstance().sGetLampStatu(ApMonitorActivity.callId, ApMonitorActivity.password, lamstate, sensor.getSensorData());
        }
    }
    /**
     * 通过特征码获取列表的传感器
     * @param data 原始数据
     * @param offset 数据偏移值
     * @return 不存在返回null
     */
    private Sensor getSensorByData(byte[] data,int offset){
        byte[] dataTemp=new byte[4];
        byte[] sensorTemp=new byte[4];
        System.arraycopy(data,offset,dataTemp,0,dataTemp.length);
        for (Sensor sensor: sensors) {
            byte[] sensorInfo=sensor.getSensorData();
            System.arraycopy(sensorInfo,0,sensorTemp,0,sensorTemp.length);
            if(Arrays.equals(sensorTemp, dataTemp)){
                return sensor;
            }
        }
        return null;
    }

    /**
     * 获取sensor在列表的位子
     * @param sensor
     * @return
     */
    private int getSensorPosition(Sensor sensor){
        return sensors.indexOf(sensor);
    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getAllSensorData();
    }

}

