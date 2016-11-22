package com.hrsst.housekeeper.entity;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by Administrator on 2016/11/11.
 * <versionCode>1</versionCode>
 <versionName>1.0.6</versionName>
 <url>http://182.254.234.243/download/firelink.apk</url>
 <message>有最新的软件包哦，亲快更新吧!</message>
 */
@Root(name = "info",strict = false)
public class UpdateXml {
    @Attribute
    public String versionCode;
    @Attribute
    public String versionName;
    @Attribute
    public String url;
    @Attribute
    public String msg;

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
