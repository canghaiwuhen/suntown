package com.suntown.cloudmonitoring.bean;

/**
 * Created by Administrator on 2016/11/3.
 */

public class PDBean {
    public String PDID;
    public String SPDID;
    public String PDNO;
    public String BARCODE;
    public String GOODSNAME;
    public String D4;
    public String D7;
    //卖场 仓库
    public String martNum;
    public String storeNum;

    public PDBean() {
    }

    public PDBean(String PDID, String SPDID, String PDNO, String BARCODE, String GOODSNAME, String d4, String d7, String martNum, String storeNum) {
        this.PDID = PDID;
        this.SPDID = SPDID;
        this.PDNO = PDNO;
        this.BARCODE = BARCODE;
        this.GOODSNAME = GOODSNAME;
        D4 = d4;
        D7 = d7;
        this.martNum = martNum;
        this.storeNum = storeNum;
    }

    @Override
    public String toString() {
        return "PDBean{" +
                "PDID='" + PDID + '\'' +
                ", SPDID='" + SPDID + '\'' +
                ", PDNO='" + PDNO + '\'' +
                ", BARCODE='" + BARCODE + '\'' +
                ", GOODSNAME='" + GOODSNAME + '\'' +
                ", D4='" + D4 + '\'' +
                ", D7='" + D7 + '\'' +
                ", martNum='" + martNum + '\'' +
                ", storeNum='" + storeNum + '\'' +
                '}';
    }
}
