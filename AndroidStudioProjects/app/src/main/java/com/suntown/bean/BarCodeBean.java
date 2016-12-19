package com.suntown.bean;

/**
 * Created by Administrator on 2016/8/16.
 */
public class BarCodeBean {
    String barcode;
    String gname;

    public BarCodeBean(String barcode, String gname) {
        this.barcode = barcode;
        this.gname = gname;
    }

    public BarCodeBean() {
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    @Override
    public String toString() {
        return "BarCodeBean{" +
                "barcode='" + barcode + '\'' +
                ", gname='" + gname + '\'' +
                '}';
    }
}
