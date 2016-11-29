package com.hrsst.housekeeper.entity;

/**
 * Created by Administrator on 2016/11/29.
 */
public class AlarmCameraInfo {

    /**
     * areaName :
     * cameraAddress : %E4%B8%AD%E5%9B%BD%E5%B9%BF%E4%B8%9C%E7%9C%81%E5%B9%BF%E5%B7%9E%E5%B8%82%E5%A4%A9%E6%B2%B3%E5%8C%BA%E9%BB%84%E5%9F%94%E5%A4%A7%E9%81%93%E8%A5%BF542%E5%8F%B7
     * cameraName : Yuty
     * cameraPwd : 123
     * ifDealAlarm : 1
     * placeType :
     * principal1 :
     * principal1Phone :
     * principal2 :
     * principal2Phone :
     */

    private CameraBean camera;
    /**
     * camera : {"areaName":"","cameraAddress":"%E4%B8%AD%E5%9B%BD%E5%B9%BF%E4%B8%9C%E7%9C%81%E5%B9%BF%E5%B7%9E%E5%B8%82%E5%A4%A9%E6%B2%B3%E5%8C%BA%E9%BB%84%E5%9F%94%E5%A4%A7%E9%81%93%E8%A5%BF542%E5%8F%B7","cameraName":"Yuty","cameraPwd":"123","ifDealAlarm":1,"placeType":"","principal1":"","principal1Phone":"","principal2":"","principal2Phone":""}
     * error : 获取摄像头成功
     * errorCode : 0
     */

    private String error;
    private int errorCode;

    public CameraBean getCamera() {
        return camera;
    }

    public void setCamera(CameraBean camera) {
        this.camera = camera;
    }

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

    public static class CameraBean {
        private String areaName;
        private String cameraAddress;
        private String cameraName;
        private String cameraPwd;
        private int ifDealAlarm;
        private String placeType;
        private String principal1;
        private String principal1Phone;
        private String principal2;
        private String principal2Phone;

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

        public String getPrincipal1() {
            return principal1;
        }

        public void setPrincipal1(String principal1) {
            this.principal1 = principal1;
        }

        public String getPrincipal1Phone() {
            return principal1Phone;
        }

        public void setPrincipal1Phone(String principal1Phone) {
            this.principal1Phone = principal1Phone;
        }

        public String getPrincipal2() {
            return principal2;
        }

        public void setPrincipal2(String principal2) {
            this.principal2 = principal2;
        }

        public String getPrincipal2Phone() {
            return principal2Phone;
        }

        public void setPrincipal2Phone(String principal2Phone) {
            this.principal2Phone = principal2Phone;
        }
    }
}
