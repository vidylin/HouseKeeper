package com.hrsst.housekeeper.entity;

/**
 * Created by Administrator on 2016/11/23.
 */
public class PostResult {

    /**
     * error : 参数错误
     * errorCode : 1
     */

    private String error;
    private int errorCode;

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
}
