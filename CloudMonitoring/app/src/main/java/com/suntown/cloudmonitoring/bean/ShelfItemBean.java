package com.suntown.cloudmonitoring.bean;

/**
 * Created by Administrator on 2016/10/25.
 */
public class ShelfItemBean {
    public String tag;
    public String barcode;
    public String gname;
    public boolean isClick;

    public ShelfItemBean(String tag, String barcode, String gname) {
        this.tag = tag;
        this.barcode = barcode;
        this.gname = gname;
    }

    public ShelfItemBean() {
    }


    @Override
    public String toString() {
        return "ShelfItemBean{" +
                "tag='" + tag + '\'' +
                ", barcode='" + barcode + '\'' +
                ", gname='" + gname + '\'' +
                ", isClick=" + isClick +
                '}';
    }
}
