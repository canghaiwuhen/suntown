package com.suntown.scannerproject.scanner.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by Administrator on 2016/11/30.
 */
public class Shelf_Allocation {
    @XStreamAlias("AID")
    public String AID;
    @XStreamAlias("Tinyip")
    public String Tinyip;
    @XStreamAlias("Barcode")
    public String Barcode;
    @XStreamAlias("GName")
    public String GName;
    @XStreamAlias("RowNumber")
    public String RowNumber;
    @XStreamAlias("ColNumber")
    public String ColNumber;


    @Override
    public String toString() {
        return "Shelf_Allocation{" +
                "AID='" + AID + '\'' +
                ", Tinyip='" + Tinyip + '\'' +
                ", Barcode='" + Barcode + '\'' +
                ", GName='" + GName + '\'' +
                ", RowNumber='" + RowNumber + '\'' +
                ", ColNumber='" + ColNumber + '\'' +
                '}';
    }

}
