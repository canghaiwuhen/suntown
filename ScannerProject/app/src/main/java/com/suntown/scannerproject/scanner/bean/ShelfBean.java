package com.suntown.scannerproject.scanner.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by Administrator on 2016/11/30.
 */

public class ShelfBean {
    @XStreamAlias("SFID")
    public String SFID;
    @XStreamAlias("RowCount")
    public String RowCount;
    @XStreamAlias("ACount")
    public String ACount;
    @XStreamAlias("Exist")
    public String Exist;

    @Override
    public String toString() {
        return "ShelfBean{" +
                "SFID='" + SFID + '\'' +
                ", RowCount='" + RowCount + '\'' +
                ", ACount='" + ACount + '\'' +
                ", Exist='" + Exist + '\'' +
                '}';
    }
}
