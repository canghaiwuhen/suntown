package com.suntown.scannerproject.bean;

/**
 * Created by Administrator on 2016/12/27.
 */

public class UpdateBean {
//    <pdaUpdateFile>
//    <fswid>836</fswid>
//    <swid>184</swid>
//    <swversion>1.0.0.3</swversion>
//    <filename>SalesPoint.Device.dll</filename>
//    <version>2.0.7.6</version>
//    <ext>dll</ext>
//    </pdaUpdateFile>
    public String fswid;
    public String swid;
    public String swversion;
    public String filename;
    public String version;
    public String ext;

    @Override
    public String toString() {
        return "UpdateBean{" +
                "fswid='" + fswid + '\'' +
                ", swid='" + swid + '\'' +
                ", swversion='" + swversion + '\'' +
                ", filename='" + filename + '\'' +
                ", version='" + version + '\'' +
                ", ext='" + ext + '\'' +
                '}';
    }
}
