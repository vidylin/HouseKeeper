package com.hrsst.housekeeper.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/11/24.
 */
public class Camera {

    /**
     * camera : [{"areaName":"???","cameraId":"3121164","cameraName":"camera_3121164","cameraPwd":"npwd","ifDealAlarm":0,"placeType":"???","setTime":"1"},{"areaName":"???","cameraId":"3121638","cameraName":"???987","cameraPwd":"123","ifDealAlarm":1,"placeType":"??","setTime":"1"}]
     * error : 普通用户获取摄像头成功
     * errorCode : 0
     */

    private String error;
    private int errorCode;
    /**
     * areaName : ???
     * cameraId : 3121164
     * cameraName : camera_3121164
     * cameraPwd : npwd
     * ifDealAlarm : 0
     * placeType : ???
     * setTime : 1
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
