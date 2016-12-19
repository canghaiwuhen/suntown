package com.suntown.scannerproject.scanner.bean;

/**
 * Created by Administrator on 2016/10/18.
 */

public class SCANDATA {
    public  String TINYIP;
    public String BARCODE;

    @Override
    public String toString() {
        return "SCANDATA{" +
                "TINYIP='" + TINYIP + '\'' +
                ", BARCODE='" + BARCODE + '\'' +
                '}';
    }
}
