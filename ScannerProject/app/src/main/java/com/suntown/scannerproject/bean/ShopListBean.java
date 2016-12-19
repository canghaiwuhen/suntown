package com.suntown.scannerproject.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by Administrator on 2016/11/29.
 */

public class ShopListBean {

//    SID>571002003</SID>
//    <SName>朝晖店</SName>
    @XStreamAlias("SID")
    public String SID;
    @XStreamAlias("SName")
    public String SName;

    public ShopListBean() {
    }

    public ShopListBean(String SID, String SName) {
        this.SID = SID;
        this.SName = SName;
    }

    @Override
    public String toString() {
        return "ShopListBean{" +
                "SID='" + SID + '\'' +
                ", SName='" + SName + '\'' +
                '}';
    }
}
