package com.suntown.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/8/12.
 */
public class DeviceInfoBean implements Parcelable {

    /**
     * MEMID : 1352
     * ROWS : 3
     * RECORD : [{"SWVERSION":"20162","BATVALUE":"11121","SERVERDATE":"","TINYIP":"0.0.1.21","BARCODE":"026102865880","GCODE":"1310521","GNAME":"乐美雅可叠沙拉碗C12181","KIND":"401701","UNIT":"只","SPEC":"1","GCLASS":"合格","PROVIDER":"","BRAND":"乐美雅","ORIGIN":"江苏南京","WIFIID":"","IMGPATH":"http://192.168.0.12:8081/TempImages/TwoFloor/K02G-2-40-1-030/IMG_0170.JPG","ORIPRICE":"29.9","MEMPRICE":"","UPTPRICE":"29.9","BEGDATE":"","ENDDATE":""},{"SWVERSION":"14a1","BATVALUE":"1137","SERVERDATE":"","TINYIP":"0.0.0.7","BARCODE":"6924745054187","GCODE":"290566","GNAME":"润之家香辣鸭脖120g","KIND":"230403","UNIT":"袋","SPEC":"120g","GCLASS":"合格","PROVIDER":"","BRAND":"润之家","ORIGIN":"福建","WIFIID":"","IMGPATH":"http://192.168.0.12:8081/TempImages/TwoFloor/K02G-2-23-1-010/IMG_0891.jpg","ORIPRICE":"12.99","MEMPRICE":"","UPTPRICE":"12.99","BEGDATE":"","ENDDATE":""},{"SWVERSION":"14A1","BATVALUE":"2848","SERVERDATE":"","TINYIP":"0.0.1.20","BARCODE":"6924745083293","GCODE":"373152","GNAME":"润之家鸡蛋面900克","KIND":"150701","UNIT":"包","SPEC":"900克","GCLASS":"合格","PROVIDER":"","BRAND":"润之家","ORIGIN":"河南驻马店","WIFIID":"","IMGPATH":"http://192.168.0.12:8081/TempImages/OneFloor/K02G-1-15-1-001/IMG_1997.jpg","ORIPRICE":"7.9","MEMPRICE":"0","UPTPRICE":"7.9","BEGDATE":"","ENDDATE":""}]
     */

    public String MEMID;
    public int ROWS;
    public List<RECORDBean> RECORD;

    public DeviceInfoBean(String MEMID, int ROWS, List<RECORDBean> RECORD) {
        this.MEMID = MEMID;
        this.ROWS = ROWS;
        this.RECORD = RECORD;
    }

    @Override
    public String toString() {
        return "DeviceInfoBean{" +
                "MEMID='" + MEMID + '\'' +
                ", ROWS=" + ROWS +
                ", RECORD=" + RECORD +
                '}';
    }


    public static class RECORDBean implements Parcelable {
        /**
         * SWVERSION : 20162
         * BATVALUE : 11121
         * SERVERDATE : 
         * TINYIP : 0.0.1.21
         * BARCODE : 026102865880
         * GCODE : 1310521
         * GNAME : 乐美雅可叠沙拉碗C12181
         * KIND : 401701
         * UNIT : 只
         * SPEC : 1
         * GCLASS : 合格
         * PROVIDER : 
         * BRAND : 乐美雅
         * ORIGIN : 江苏南京
         * WIFIID : 
         * IMGPATH : http://192.168.0.12:8081/TempImages/TwoFloor/K02G-2-40-1-030/IMG_0170.JPG
         * ORIPRICE : 29.9
         * MEMPRICE : 
         * UPTPRICE : 29.9
         * BEGDATE : 
         * ENDDATE : 
         */

        public String SWVERSION;
        public String BATVALUE;
        public String SERVERDATE;
        public String TINYIP;
        public String BARCODE;
        public String GCODE;
        public String GNAME;
        public String KIND;
        public String UNIT;
        public String SPEC;
        public String GCLASS;
        public String PROVIDER;
        public String BRAND;
        public String ORIGIN;
        public String WIFIID;
        public String IMGPATH;
        public String ORIPRICE;
        public String MEMPRICE;
        public String UPTPRICE;
        public String BEGDATE;
        public String ENDDATE;

        public RECORDBean(String SWVERSION, String BATVALUE, String SERVERDATE, String TINYIP, String BARCODE, String GCODE, String GNAME, String KIND, String UNIT, String SPEC, String GCLASS, String PROVIDER, String BRAND, String ORIGIN, String WIFIID, String IMGPATH, String ORIPRICE, String MEMPRICE, String UPTPRICE, String BEGDATE, String ENDDATE) {
            this.SWVERSION = SWVERSION;
            this.BATVALUE = BATVALUE;
            this.SERVERDATE = SERVERDATE;
            this.TINYIP = TINYIP;
            this.BARCODE = BARCODE;
            this.GCODE = GCODE;
            this.GNAME = GNAME;
            this.KIND = KIND;
            this.UNIT = UNIT;
            this.SPEC = SPEC;
            this.GCLASS = GCLASS;
            this.PROVIDER = PROVIDER;
            this.BRAND = BRAND;
            this.ORIGIN = ORIGIN;
            this.WIFIID = WIFIID;
            this.IMGPATH = IMGPATH;
            this.ORIPRICE = ORIPRICE;
            this.MEMPRICE = MEMPRICE;
            this.UPTPRICE = UPTPRICE;
            this.BEGDATE = BEGDATE;
            this.ENDDATE = ENDDATE;
        }

        @Override
        public String toString() {
            return "RECORDBean{" +
                    "SWVERSION='" + SWVERSION + '\'' +
                    ", BATVALUE='" + BATVALUE + '\'' +
                    ", SERVERDATE='" + SERVERDATE + '\'' +
                    ", TINYIP='" + TINYIP + '\'' +
                    ", BARCODE='" + BARCODE + '\'' +
                    ", GCODE='" + GCODE + '\'' +
                    ", GNAME='" + GNAME + '\'' +
                    ", KIND='" + KIND + '\'' +
                    ", UNIT='" + UNIT + '\'' +
                    ", SPEC='" + SPEC + '\'' +
                    ", GCLASS='" + GCLASS + '\'' +
                    ", PROVIDER='" + PROVIDER + '\'' +
                    ", BRAND='" + BRAND + '\'' +
                    ", ORIGIN='" + ORIGIN + '\'' +
                    ", WIFIID='" + WIFIID + '\'' +
                    ", IMGPATH='" + IMGPATH + '\'' +
                    ", ORIPRICE='" + ORIPRICE + '\'' +
                    ", MEMPRICE='" + MEMPRICE + '\'' +
                    ", UPTPRICE='" + UPTPRICE + '\'' +
                    ", BEGDATE='" + BEGDATE + '\'' +
                    ", ENDDATE='" + ENDDATE + '\'' +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.SWVERSION);
            dest.writeString(this.BATVALUE);
            dest.writeString(this.SERVERDATE);
            dest.writeString(this.TINYIP);
            dest.writeString(this.BARCODE);
            dest.writeString(this.GCODE);
            dest.writeString(this.GNAME);
            dest.writeString(this.KIND);
            dest.writeString(this.UNIT);
            dest.writeString(this.SPEC);
            dest.writeString(this.GCLASS);
            dest.writeString(this.PROVIDER);
            dest.writeString(this.BRAND);
            dest.writeString(this.ORIGIN);
            dest.writeString(this.WIFIID);
            dest.writeString(this.IMGPATH);
            dest.writeString(this.ORIPRICE);
            dest.writeString(this.MEMPRICE);
            dest.writeString(this.UPTPRICE);
            dest.writeString(this.BEGDATE);
            dest.writeString(this.ENDDATE);
        }

        public RECORDBean() {
        }

        protected RECORDBean(Parcel in) {
            this.SWVERSION = in.readString();
            this.BATVALUE = in.readString();
            this.SERVERDATE = in.readString();
            this.TINYIP = in.readString();
            this.BARCODE = in.readString();
            this.GCODE = in.readString();
            this.GNAME = in.readString();
            this.KIND = in.readString();
            this.UNIT = in.readString();
            this.SPEC = in.readString();
            this.GCLASS = in.readString();
            this.PROVIDER = in.readString();
            this.BRAND = in.readString();
            this.ORIGIN = in.readString();
            this.WIFIID = in.readString();
            this.IMGPATH = in.readString();
            this.ORIPRICE = in.readString();
            this.MEMPRICE = in.readString();
            this.UPTPRICE = in.readString();
            this.BEGDATE = in.readString();
            this.ENDDATE = in.readString();
        }

        public static final Parcelable.Creator<RECORDBean> CREATOR = new Parcelable.Creator<RECORDBean>() {
            @Override
            public RECORDBean createFromParcel(Parcel source) {
                return new RECORDBean(source);
            }

            @Override
            public RECORDBean[] newArray(int size) {
                return new RECORDBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.MEMID);
        dest.writeInt(this.ROWS);
        dest.writeTypedList(this.RECORD);
    }

    public DeviceInfoBean() {
    }

    protected DeviceInfoBean(Parcel in) {
        this.MEMID = in.readString();
        this.ROWS = in.readInt();
        this.RECORD = in.createTypedArrayList(RECORDBean.CREATOR);
    }

    public static final Parcelable.Creator<DeviceInfoBean> CREATOR = new Parcelable.Creator<DeviceInfoBean>() {
        @Override
        public DeviceInfoBean createFromParcel(Parcel source) {
            return new DeviceInfoBean(source);
        }

        @Override
        public DeviceInfoBean[] newArray(int size) {
            return new DeviceInfoBean[size];
        }
    };
}
