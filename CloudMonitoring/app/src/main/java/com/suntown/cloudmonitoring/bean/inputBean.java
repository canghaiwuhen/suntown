package com.suntown.cloudmonitoring.bean;

/**
 * Created by Administrator on 2016/11/1.
 */

public class inputBean {
    public String Barcode;
    public String Gname;
    public String boxNum;
    public String num;
    public String Date;

    public inputBean() {
    }


    public inputBean(String barcode, String gname, String boxNum, String num, String date) {
        this.Barcode = barcode;
        this.Gname = gname;
        this.boxNum = boxNum;
        this.num = num;
        this.Date = date;
    }

    @Override
    public String toString() {
        return "inputBean{" +
                "Barcode='" + Barcode + '\'' +
                ", Gname='" + Gname + '\'' +
                ", boxNum='" + boxNum + '\'' +
                ", num='" + num + '\'' +
                ", Date='" + Date + '\'' +
                '}';
    }
}
