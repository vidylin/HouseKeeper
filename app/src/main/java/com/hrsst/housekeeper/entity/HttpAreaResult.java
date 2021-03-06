package com.hrsst.housekeeper.entity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/22.
 */
public class HttpAreaResult<T> {

    /**
     * error : 获取区域id成功
     * errorCode : 0
     */

    private String error;
    private int errorCode;
    private ArrayList<Area> area;

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

    public ArrayList<Area> getSmoke() {
        return area;
    }

    public void setSmoke(ArrayList<Area> area) {
        this.area = area;
    }
}
