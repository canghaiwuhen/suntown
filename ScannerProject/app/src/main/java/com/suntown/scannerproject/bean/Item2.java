package com.suntown.scannerproject.bean;

/**
 * Created by Shinelon on 2016/9/18.
 */
public class Item2 {
    //    String aname = recordBean.ANAME;
//    String sid = recordBean.SID;
    public String Barcode;
    public String GName;
    public String tinyip;


    @Override
    public String toString() {
        return "Item2{" +
                "Barcode='" + Barcode + '\'' +
                ", GName='" + GName + '\'' +
                ", tinyip='" + tinyip + '\'' +
                '}';
    }
}
