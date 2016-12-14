package com.hrsst.housekeeper.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/11/29.
 */
public class AlarmMsg {

    /**
     * Alarm : [{"alarmTime":"4","alarmType":127,"camera":{"addrcameraNameess":"君","areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西536号","cameraId":"3121638","cameraPwd":"123","latitude":"23.131779","longitude":"113.350168","placeType":"其它店","principal1":"break","principal1Phone":"123456789","principal2":"","principal2Phone":""},"dealTime":"2016-12-09 10:36:23","dealUser":{"email":"fsyhrsst@126.com","phone":"","userId":"05501386","userName":"iOS"},"ifDealAlarm":0},{"alarmTime":"2016-12-13 09:49:34","alarmType":2,"camera":{"addrcameraNameess":"微微057","areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西540号","cameraId":"2726057","cameraPwd":"123","latitude":"23.131768","longitude":"113.350206","placeType":"其它店","principal1":"微微","principal1Phone":"","principal2":"","principal2Phone":""},"dealTime":"","dealUser":null,"ifDealAlarm":0},{"alarmTime":"2016-12-13 09:44:55","alarmType":2,"camera":{"addrcameraNameess":"微微057","areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西540号","cameraId":"2726057","cameraPwd":"123","latitude":"23.131768","longitude":"113.350206","placeType":"其它店","principal1":"微微","principal1Phone":"","principal2":"","principal2Phone":""},"dealTime":"","dealUser":null,"ifDealAlarm":0},{"alarmTime":"2016-12-13 09:43:22","alarmType":2,"camera":{"addrcameraNameess":"微微057","areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西540号","cameraId":"2726057","cameraPwd":"123","latitude":"23.131768","longitude":"113.350206","placeType":"其它店","principal1":"微微","principal1Phone":"","principal2":"","principal2Phone":""},"dealTime":"","dealUser":null,"ifDealAlarm":0},{"alarmTime":"2016-12-13 09:42:05","alarmType":2,"camera":{"addrcameraNameess":"微微057","areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西540号","cameraId":"2726057","cameraPwd":"123","latitude":"23.131768","longitude":"113.350206","placeType":"其它店","principal1":"微微","principal1Phone":"","principal2":"","principal2Phone":""},"dealTime":"","dealUser":null,"ifDealAlarm":0},{"alarmTime":"2016-12-13 09:41:50","alarmType":2,"camera":{"addrcameraNameess":"微微057","areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西540号","cameraId":"2726057","cameraPwd":"123","latitude":"23.131768","longitude":"113.350206","placeType":"其它店","principal1":"微微","principal1Phone":"","principal2":"","principal2Phone":""},"dealTime":"","dealUser":null,"ifDealAlarm":0},{"alarmTime":"2016-12-13 09:38:35","alarmType":2,"camera":{"addrcameraNameess":"微微057","areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西540号","cameraId":"2726057","cameraPwd":"123","latitude":"23.131768","longitude":"113.350206","placeType":"其它店","principal1":"微微","principal1Phone":"","principal2":"","principal2Phone":""},"dealTime":"","dealUser":null,"ifDealAlarm":0},{"alarmTime":"2016-12-13 09:23:14","alarmType":2,"camera":{"addrcameraNameess":"微微057","areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西540号","cameraId":"2726057","cameraPwd":"123","latitude":"23.131768","longitude":"113.350206","placeType":"其它店","principal1":"微微","principal1Phone":"","principal2":"","principal2Phone":""},"dealTime":"","dealUser":null,"ifDealAlarm":0},{"alarmTime":"2016-12-13 09:21:18","alarmType":2,"camera":{"addrcameraNameess":"微微057","areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西540号","cameraId":"2726057","cameraPwd":"123","latitude":"23.131768","longitude":"113.350206","placeType":"其它店","principal1":"微微","principal1Phone":"","principal2":"","principal2Phone":""},"dealTime":"","dealUser":null,"ifDealAlarm":0},{"alarmTime":"2016-12-13 09:14:45","alarmType":2,"camera":{"addrcameraNameess":"微微057","areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西540号","cameraId":"2726057","cameraPwd":"123","latitude":"23.131768","longitude":"113.350206","placeType":"其它店","principal1":"微微","principal1Phone":"","principal2":"","principal2Phone":""},"dealTime":"","dealUser":null,"ifDealAlarm":0},{"alarmTime":"2016-12-13 09:14:39","alarmType":2,"camera":{"addrcameraNameess":"微微057","areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西540号","cameraId":"2726057","cameraPwd":"123","latitude":"23.131768","longitude":"113.350206","placeType":"其它店","principal1":"微微","principal1Phone":"","principal2":"","principal2Phone":""},"dealTime":"","dealUser":null,"ifDealAlarm":0},{"alarmTime":"2016-12-13 09:14:23","alarmType":2,"camera":{"addrcameraNameess":"微微057","areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西540号","cameraId":"2726057","cameraPwd":"123","latitude":"23.131768","longitude":"113.350206","placeType":"其它店","principal1":"微微","principal1Phone":"","principal2":"","principal2Phone":""},"dealTime":"","dealUser":null,"ifDealAlarm":0},{"alarmTime":"2016-12-13 09:07:30","alarmType":2,"camera":{"addrcameraNameess":"微微057","areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西540号","cameraId":"2726057","cameraPwd":"123","latitude":"23.131768","longitude":"113.350206","placeType":"其它店","principal1":"微微","principal1Phone":"","principal2":"","principal2Phone":""},"dealTime":"","dealUser":null,"ifDealAlarm":0},{"alarmTime":"2016-12-13 09:06:25","alarmType":2,"camera":{"addrcameraNameess":"微微057","areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西540号","cameraId":"2726057","cameraPwd":"123","latitude":"23.131768","longitude":"113.350206","placeType":"其它店","principal1":"微微","principal1Phone":"","principal2":"","principal2Phone":""},"dealTime":"","dealUser":null,"ifDealAlarm":0},{"alarmTime":"2016-12-13 09:06:18","alarmType":2,"camera":{"addrcameraNameess":"微微057","areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西540号","cameraId":"2726057","cameraPwd":"123","latitude":"23.131768","longitude":"113.350206","placeType":"其它店","principal1":"微微","principal1Phone":"","principal2":"","principal2Phone":""},"dealTime":"","dealUser":null,"ifDealAlarm":0},{"alarmTime":"2016-12-13 09:05:41","alarmType":2,"camera":{"addrcameraNameess":"微微057","areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西540号","cameraId":"2726057","cameraPwd":"123","latitude":"23.131768","longitude":"113.350206","placeType":"其它店","principal1":"微微","principal1Phone":"","principal2":"","principal2Phone":""},"dealTime":"","dealUser":null,"ifDealAlarm":0},{"alarmTime":"2016-12-13 09:05:23","alarmType":1,"camera":{"addrcameraNameess":"微微057","areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西540号","cameraId":"2726057","cameraPwd":"123","latitude":"23.131768","longitude":"113.350206","placeType":"其它店","principal1":"微微","principal1Phone":"","principal2":"","principal2Phone":""},"dealTime":"","dealUser":null,"ifDealAlarm":0},{"alarmTime":"2016-12-13 09:04:41","alarmType":1,"camera":{"addrcameraNameess":"微微057","areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西540号","cameraId":"2726057","cameraPwd":"123","latitude":"23.131768","longitude":"113.350206","placeType":"其它店","principal1":"微微","principal1Phone":"","principal2":"","principal2Phone":""},"dealTime":"","dealUser":null,"ifDealAlarm":0},{"alarmTime":"2016-12-13 09:04:38","alarmType":1,"camera":{"addrcameraNameess":"微微057","areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西540号","cameraId":"2726057","cameraPwd":"123","latitude":"23.131768","longitude":"113.350206","placeType":"其它店","principal1":"微微","principal1Phone":"","principal2":"","principal2Phone":""},"dealTime":"","dealUser":null,"ifDealAlarm":0},{"alarmTime":"2016-12-13 09:03:40","alarmType":2,"camera":{"addrcameraNameess":"微微057","areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西540号","cameraId":"2726057","cameraPwd":"123","latitude":"23.131768","longitude":"113.350206","placeType":"其它店","principal1":"微微","principal1Phone":"","principal2":"","principal2Phone":""},"dealTime":"","dealUser":null,"ifDealAlarm":0}]
     * error : 获取报警消息成功）
     * errorCode : 0
     */

    private String error;
    private int errorCode;
    /**
     * alarmTime : 4
     * alarmType : 127
     * camera : {"addrcameraNameess":"君","areaName":"测试区","cameraAddress":"中国广东省广州市天河区黄埔大道西536号","cameraId":"3121638","cameraPwd":"123","latitude":"23.131779","longitude":"113.350168","placeType":"其它店","principal1":"break","principal1Phone":"123456789","principal2":"","principal2Phone":""}
     * dealTime : 2016-12-09 10:36:23
     * dealUser : {"email":"fsyhrsst@126.com","phone":"","userId":"05501386","userName":"iOS"}
     * ifDealAlarm : 0
     */

    private List<AlarmBean> Alarm;

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

    public List<AlarmBean> getAlarm() {
        return Alarm;
    }

    public void setAlarm(List<AlarmBean> Alarm) {
        this.Alarm = Alarm;
    }

    public static class AlarmBean {
        private String alarmTime;
        private int alarmType;
        /**
         * addrcameraNameess : 君
         * areaName : 测试区
         * cameraAddress : 中国广东省广州市天河区黄埔大道西536号
         * cameraId : 3121638
         * cameraPwd : 123
         * latitude : 23.131779
         * longitude : 113.350168
         * placeType : 其它店
         * principal1 : break
         * principal1Phone : 123456789
         * principal2 :
         * principal2Phone :
         */

        private CameraBean camera;
        private String dealTime;
        /**
         * email : fsyhrsst@126.com
         * phone :
         * userId : 05501386
         * userName : iOS
         */

        private DealUserBean dealUser;
        private int ifDealAlarm;

        public String getAlarmTime() {
            return alarmTime;
        }

        public void setAlarmTime(String alarmTime) {
            this.alarmTime = alarmTime;
        }

        public int getAlarmType() {
            return alarmType;
        }

        public void setAlarmType(int alarmType) {
            this.alarmType = alarmType;
        }

        public CameraBean getCamera() {
            return camera;
        }

        public void setCamera(CameraBean camera) {
            this.camera = camera;
        }

        public String getDealTime() {
            return dealTime;
        }

        public void setDealTime(String dealTime) {
            this.dealTime = dealTime;
        }

        public DealUserBean getDealUser() {
            return dealUser;
        }

        public void setDealUser(DealUserBean dealUser) {
            this.dealUser = dealUser;
        }

        public int getIfDealAlarm() {
            return ifDealAlarm;
        }

        public void setIfDealAlarm(int ifDealAlarm) {
            this.ifDealAlarm = ifDealAlarm;
        }

        public static class CameraBean {
            private String addrcameraNameess;
            private String areaName;
            private String cameraAddress;
            private String cameraId;
            private String cameraPwd;
            private String latitude;
            private String longitude;
            private String placeType;
            private String principal1;
            private String principal1Phone;
            private String principal2;
            private String principal2Phone;

            public String getAddrcameraNameess() {
                return addrcameraNameess;
            }

            public void setAddrcameraNameess(String addrcameraNameess) {
                this.addrcameraNameess = addrcameraNameess;
            }

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

            public String getCameraPwd() {
                return cameraPwd;
            }

            public void setCameraPwd(String cameraPwd) {
                this.cameraPwd = cameraPwd;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
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

        public static class DealUserBean {
            private String email;
            private String phone;
            private String userId;
            private String userName;

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }
        }
    }
}
