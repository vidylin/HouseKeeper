package com.hrsst.housekeeper.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/11/24.
 */
public class Camera {


    /**
     * camera : [{"areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西540号","cameraId":"2726057","cameraName":"微微057","cameraPwd":"123","ifDealAlarm":1,"placeType":"其它店","setTime":"2016-12-08 14:40:38"},{"areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西534号","cameraId":"3121638","cameraName":"丁香蕉","cameraPwd":"123","ifDealAlarm":1,"placeType":"其它店","setTime":"1"}]
     * error : 普通用户获取摄像头成功
     * errorCode : 0
     */

    private String error;
    private int errorCode;
    /**
     * areaName : 测试区
     * cameraAddress : 中国广东省广州市天河区黄埔大道西540号
     * cameraId : 2726057
     * cameraName : 微微057
     * cameraPwd : 123
     * ifDealAlarm : 1
     * placeType : 其它店
     * setTime : 2016-12-08 14:40:38
     */

    private List<CameraBean> camera;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<CameraBean> getCamera() {
        return camera;
    }

    public void setCamera(List<CameraBean> camera) {
        this.camera = camera;
    }

    public static class CameraBean {
        private String areaName;
        private String cameraAddress;
        private String cameraId;
        private String cameraName;
        private String cameraPwd;
        private int ifDealAlarm;
        private String placeType;
        private String setTime;

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getCameraAddress() {
            return cameraAddress;
        }

        public void setCameraAddress(String cameraAddress) {
            this.cameraAddress = cameraAddress;
        }

        public String getCameraId() {
            return cameraId;
        }

        public void setCameraId(String cameraId) {
            this.cameraId = cameraId;
        }

        public String getCameraName() {
            return cameraName;
        }

        public void setCameraName(String cameraName) {
            this.cameraName = cameraName;
        }

        public String getCameraPwd() {
            return cameraPwd;
        }

        public void setCameraPwd(String cameraPwd) {
            this.cameraPwd = cameraPwd;
        }

        public int getIfDealAlarm() {
            return ifDealAlarm;
        }

        public void setIfDealAlarm(int ifDealAlarm) {
            this.ifDealAlarm = ifDealAlarm;
        }

        public String getPlaceType() {
            return placeType;
        }

        public void setPlaceType(String placeType) {
            this.placeType = placeType;
        }

        public String getSetTime() {
            return setTime;
        }

        public void setSetTime(String setTime) {
            this.setTime = setTime;
        }
    }
}
