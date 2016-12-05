package com.hrsst.housekeeper.entity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/21.
 */
public class HttpError<T> {

    /**
     * error : 获取烟感成功）
     * errorCode : 0
     */

    private String error;
    private int errorCode;
    private ArrayList<ShopType> placeType;
    private  String state;

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


    public ArrayList<ShopType> getPlaceType() {
        return placeType;
    }

    public void setPlaceType(ArrayList<ShopType> placeType) {
        this.placeType = placeType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
