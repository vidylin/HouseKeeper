package com.hrsst.housekeeper.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */
public class Defence implements Serializable{
    private int area;
    private int channel;
    /**
     * defence : [{"defenceId":"1","defenceName":"dn1","sensorId":"1"},{"defenceId":"2","defenceName":"ndn","sensorId":"12"}]
     * error : 获取防区成功
     * errorCode : 0
     */

    private String error;
    private int errorCode;
    /**
     * defenceId : 1
     * defenceName : dn1
     * sensorId : 1
     */

    private List<DefenceBean> defence;

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
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

    public List<DefenceBean> getDefence() {
        return defence;
    }

    public void setDefence(List<DefenceBean> defence) {
        this.defence = defence;
    }


    public static class DefenceBean {
        private String defenceId;
        private String defenceName;
        private String sensorId;
        private int pos;

        public String getDefenceId() {
            return defenceId;
        }

        public void setDefenceId(String defenceId) {
            this.defenceId = defenceId;
        }

        public String getDefenceName() {
            return defenceName;
        }

        public void setDefenceName(String defenceName) {
            this.defenceName = defenceName;
        }

        public String getSensorId() {
            return sensorId;
        }

        public void setSensorId(String sensorId) {
            this.sensorId = sensorId;
        }

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }
    }
}
