package com.hrsst.housekeeper.entity;

/**
 * Created by Administrator on 2016/11/23.
 */
public class LoginServer {

    /**
     * error : 登录成功）
     * errorCode : 0
     * name : lllppp
     * privilege : 1
     */

    private String error;
    private int errorCode;
    private String name;
    private int privilege;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }
}
