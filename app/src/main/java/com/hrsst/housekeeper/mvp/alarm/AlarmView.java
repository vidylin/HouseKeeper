package com.hrsst.housekeeper.mvp.alarm;

import com.hrsst.housekeeper.entity.AlarmCameraInfo;

/**
 * Created by Administrator on 2016/11/29.
 */
public interface AlarmView {
    void getAlarmCameraResult(AlarmCameraInfo.CameraBean cameraBean);
}
