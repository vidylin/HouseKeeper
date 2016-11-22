package com.hrsst.housekeeper.entity;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Administrator on 2016/11/11.
 */
@Root(name = "data",strict = false)
public class VersionXml {
    @ElementList(required = true,inline = true,entry = "info")

    public List<UpdateXml> list;

    public List<UpdateXml> getList() {
        return list;
    }

    public void setList(List<UpdateXml> list) {
        this.list = list;
    }
}
