package com.suntown.scannerproject.input.bean;

/**
 * Created by Administrator on 2016/11/3.
 */

public class PDDetailBean {
//    <?xml version="1.0" encoding="gbk"?>
//    <SHOPGOODSPDLIST>
//    <PDID>21061</PDID>
//    <SPDID>200668724</SPDID>
//    <PDNO>20160815174038</PDNO>
//    <BARCODE>6918976333388</BARCODE>
//    <GOODSNAME>雀巢优活饮用水330毫升</GOODSNAME>
//    <D4>25</D4>
//    <D7>110</D7>
//    </SHOPGOODSPDLIST>
    public String PDID;
    public String SPDID;
    public String PDNO;
    public String BARCODE;
    public String GOODSNAME;
    public String D4;
    public String D7;


    public PDDetailBean() {
    }

    public PDDetailBean(String PDID, String SPDID, String PDNO, String BARCODE, String GOODSNAME, String d4, String d7) {
        this.PDID = PDID;
        this.SPDID = SPDID;
        this.PDNO = PDNO;
        this.BARCODE = BARCODE;
        this.GOODSNAME = GOODSNAME;
        D4 = d4;
        D7 = d7;
    }

    @Override
    public String toString() {
        return "PDDetailBean{" +
                "PDID='" + PDID + '\'' +
                ", SPDID='" + SPDID + '\'' +
                ", PDNO='" + PDNO + '\'' +
                ", BARCODE='" + BARCODE + '\'' +
                ", GOODSNAME='" + GOODSNAME + '\'' +
                ", D4='" + D4 + '\'' +
                ", D7='" + D7 + '\'' +
                '}';
    }
}
