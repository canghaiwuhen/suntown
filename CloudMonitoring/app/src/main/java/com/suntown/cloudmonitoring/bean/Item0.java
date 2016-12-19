package com.suntown.cloudmonitoring.bean;

/**
 * Created by Shinelon on 2016/9/18.
 */
public class Item0 {
    //    String aname = recordBean.ANAME;
//    String sid = recordBean.SID;
    public String sname;
    public String sid;

    public Item0(String sname, String sid) {
        this.sname = sname;
        this.sid = sid;
    }

    public Item0() {
    }

    @Override
    public String toString() {
        return "ExceptionLevel0Item{" +
                "sname='" + sname + '\'' +
                ", sid='" + sid + '\'' +
                '}';
    }
}
