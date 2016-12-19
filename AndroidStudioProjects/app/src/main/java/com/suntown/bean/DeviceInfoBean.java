package com.suntown.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2016/8/12.
 */
public class DeviceInfoBean implements Parcelable {

    /**
     * MEMID : 1070
     * ROWS : 5
     * RECORD : [{"SWVERSION":"12A1","BATVALUE":"2846","SERVERDATE":"","TINYIP":"0.0.0.3","BARCODE":"6901845040456","GCODE":"686459","GNAME":"格力高牛奶奇蒂70克","KIND":"220104","UNIT":"盒","SPEC":"70克","GCLASS":"合格","PROVIDER":"","BRAND":"格力高","ORIGIN":"多产地，详见包装","IMGPATH":"\\TwoFloor\\K02G-2-22-1-026\\IMG_1501.JPG","ORIPRICE":"5.2","MEMPRICE":"","UPTPRICE":"5.2","BEGDATE":"","ENDDATE":""},{"SWVERSION":"14A1","BATVALUE":"2571","SERVERDATE":"","TINYIP":"0.0.0.7","BARCODE":"6902083896591","GCODE":"1373613","GNAME":"娃哈哈启力维生素饮料250毫升","KIND":"141001","UNIT":"罐","SPEC":"250毫升","GCLASS":"合格","PROVIDER":"","BRAND":"娃哈哈","ORIGIN":"浙江嘉兴","IMGPATH":"\\OneFloor\\K02G-1-14-1-019\\IMG_2650.jpg","ORIPRICE":"5.8","MEMPRICE":"","UPTPRICE":"5.8","BEGDATE":"","ENDDATE":""},{"SWVERSION":"","BATVALUE":"","SERVERDATE":"","TINYIP":"0.0.0.2","BARCODE":"6923644265960","GCODE":"801674","GNAME":"蒙牛未来星骨力型儿童牛奶190ml","KIND":"140401","UNIT":"盒","SPEC":"190ml","GCLASS":"合格","PROVIDER":"","BRAND":"蒙牛","ORIGIN":"多产地，详见包装","IMGPATH":"\\OneFloor\\K02G-1-14-1-006\\IMG_0684.jpg","ORIPRICE":"3.6","MEMPRICE":"","UPTPRICE":"3.6","BEGDATE":"","ENDDATE":""},{"SWVERSION":"-24300","BATVALUE":"23562","SERVERDATE":"","TINYIP":"0.0.0.5","BARCODE":"6924745072310","GCODE":"552678","GNAME":"润之家儿童鸡蛋面300g","KIND":"150702","UNIT":"袋","SPEC":"300g","GCLASS":"合格","PROVIDER":"","BRAND":"润之家","ORIGIN":"河南驻马店","IMGPATH":"\\OneFloor\\K02G-1-15-1-001\\IMG_1999.jpg","ORIPRICE":"6.5","MEMPRICE":"","UPTPRICE":"6.5","BEGDATE":"","ENDDATE":""},{"SWVERSION":"-24300","BATVALUE":"24331","SERVERDATE":"","TINYIP":"0.0.0.4","BARCODE":"6933833900695","GCODE":"66817","GNAME":"福事多牛奶加钙核桃粉礼盒840克","KIND":"190702","UNIT":"盒","SPEC":"840克","GCLASS":"合格","PROVIDER":"","BRAND":"福事多","ORIGIN":"浙江金华","IMGPATH":"\\TwoFloor\\K02G-2-19-1-002\\IMG_20150212_132056.jpg","ORIPRICE":"88","MEMPRICE":"","UPTPRICE":"88","BEGDATE":"","ENDDATE":""}]
     */

    private String MEMID;
    private int ROWS;
    /**
     * SERVERDATE :
     * SWVERSION : 12A1
     * BATVALUE : 2846
     * TINYIP : 0.0.0.3
     * BARCODE : 6901845040456
     * GCODE : 686459
     * GNAME : 格力高牛奶奇蒂70克
     * KIND : 220104
     * UNIT : 盒
     * SPEC : 70克
     * GCLASS : 合格
     * PROVIDER :
     * BRAND : 格力高
     * ORIPRICE : 5.2
     * UPTPRICE : 5.2
     * ORIGIN : 多产地，详见包装
     * IMGPATH : \TwoFloor\K02G-2-22-1-026\IMG_1501.JPG
     * MEMPRICE :
     * BEGDATE :
     * ENDDATE :
     */

    private List<RECORDBean> RECORD;

    public String getMEMID() {
        return MEMID;
    }

    public void setMEMID(String MEMID) {
        this.MEMID = MEMID;
    }

    public int getROWS() {
        return ROWS;
    }

    public void setROWS(int ROWS) {
        this.ROWS = ROWS;
    }

    public List<RECORDBean> getRECORD() {
        return RECORD;
    }

    public void setRECORD(List<RECORDBean> RECORD) {
        this.RECORD = RECORD;
    }

    public static class RECORDBean implements Parcelable {
        private String SWVERSION;
        private String BATVALUE;
        private String SERVERDATE;
        private String TINYIP;
        private String BARCODE;
        private String GCODE;
        private String GNAME;
        private String KIND;
        private String UNIT;
        private String SPEC;
        private String GCLASS;
        private String PROVIDER;
        private String BRAND;
        private String ORIGIN;
        private String IMGPATH;
        private String ORIPRICE;
        private String MEMPRICE;
        private String UPTPRICE;
        private String BEGDATE;
        private String ENDDATE;

        public String getSWVERSION() {
            return SWVERSION;
        }

        public void setSWVERSION(String SWVERSION) {
            this.SWVERSION = SWVERSION;
        }

        public String getBATVALUE() {
            return BATVALUE;
        }

        public void setBATVALUE(String BATVALUE) {
            this.BATVALUE = BATVALUE;
        }

        public String getSERVERDATE() {
            return SERVERDATE;
        }

        public void setSERVERDATE(String SERVERDATE) {
            this.SERVERDATE = SERVERDATE;
        }

        public String getTINYIP() {
            return TINYIP;
        }

        public void setTINYIP(String TINYIP) {
            this.TINYIP = TINYIP;
        }

        public String getBARCODE() {
            return BARCODE;
        }

        public void setBARCODE(String BARCODE) {
            this.BARCODE = BARCODE;
        }

        public String getGCODE() {
            return GCODE;
        }

        public void setGCODE(String GCODE) {
            this.GCODE = GCODE;
        }

        public String getGNAME() {
            return GNAME;
        }

        public void setGNAME(String GNAME) {
            this.GNAME = GNAME;
        }

        public String getKIND() {
            return KIND;
        }

        public void setKIND(String KIND) {
            this.KIND = KIND;
        }

        public String getUNIT() {
            return UNIT;
        }

        public void setUNIT(String UNIT) {
            this.UNIT = UNIT;
        }

        public String getSPEC() {
            return SPEC;
        }

        public void setSPEC(String SPEC) {
            this.SPEC = SPEC;
        }

        public String getGCLASS() {
            return GCLASS;
        }

        public void setGCLASS(String GCLASS) {
            this.GCLASS = GCLASS;
        }

        public String getPROVIDER() {
            return PROVIDER;
        }

        public void setPROVIDER(String PROVIDER) {
            this.PROVIDER = PROVIDER;
        }

        public String getBRAND() {
            return BRAND;
        }

        public void setBRAND(String BRAND) {
            this.BRAND = BRAND;
        }

        public String getORIGIN() {
            return ORIGIN;
        }

        public void setORIGIN(String ORIGIN) {
            this.ORIGIN = ORIGIN;
        }

        public String getIMGPATH() {
            return IMGPATH;
        }

        public void setIMGPATH(String IMGPATH) {
            this.IMGPATH = IMGPATH;
        }

        public String getORIPRICE() {
            return ORIPRICE;
        }

        public void setORIPRICE(String ORIPRICE) {
            this.ORIPRICE = ORIPRICE;
        }

        public String getMEMPRICE() {
            return MEMPRICE;
        }

        public void setMEMPRICE(String MEMPRICE) {
            this.MEMPRICE = MEMPRICE;
        }

        public String getUPTPRICE() {
            return UPTPRICE;
        }

        public void setUPTPRICE(String UPTPRICE) {
            this.UPTPRICE = UPTPRICE;
        }

        public String getBEGDATE() {
            return BEGDATE;
        }

        public void setBEGDATE(String BEGDATE) {
            this.BEGDATE = BEGDATE;
        }

        public String getENDDATE() {
            return ENDDATE;
        }

        public void setENDDATE(String ENDDATE) {
            this.ENDDATE = ENDDATE;
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
            this.IMGPATH = in.readString();
            this.ORIPRICE = in.readString();
            this.MEMPRICE = in.readString();
            this.UPTPRICE = in.readString();
            this.BEGDATE = in.readString();
            this.ENDDATE = in.readString();
        }

        public static final Creator<RECORDBean> CREATOR = new Creator<RECORDBean>() {
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
    public String toString() {
        return "DeviceInfoBean{" +
                "MEMID='" + MEMID + '\'' +
                ", ROWS=" + ROWS +
                ", RECORD=" + RECORD +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.MEMID);
        dest.writeInt(this.ROWS);
        dest.writeList(this.RECORD);
    }

    public DeviceInfoBean() {
    }

    protected DeviceInfoBean(Parcel in) {
        this.MEMID = in.readString();
        this.ROWS = in.readInt();
        this.RECORD = new ArrayList<RECORDBean>();
        in.readList(this.RECORD, RECORDBean.class.getClassLoader());
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
    public static class DeviceInfoAndMemidBean implements Parcelable, Comparable<DeviceInfoAndMemidBean> {
        private String MEMID;
        private String SWVERSION;
        private String BATVALUE;
        private String SERVERDATE;
        private String TINYIP;
        private String BARCODE;
        private String GCODE;
        private String GNAME;
        private String KIND;
        private String UNIT;
        private String SPEC;
        private String GCLASS;
        private String PROVIDER;
        private String BRAND;
        private String ORIGIN;
        private String IMGPATH;
        private String ORIPRICE;
        private String MEMPRICE;
        private String UPTPRICE;

        @Override
        public String toString() {
            return "DeviceInfoAndMemidBean{" +
                    "MEMID='" + MEMID + '\'' +
                    ", SWVERSION='" + SWVERSION + '\'' +
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
                    ", IMGPATH='" + IMGPATH + '\'' +
                    ", ORIPRICE='" + ORIPRICE + '\'' +
                    ", MEMPRICE='" + MEMPRICE + '\'' +
                    ", UPTPRICE='" + UPTPRICE + '\'' +
                    ", BEGDATE='" + BEGDATE + '\'' +
                    ", ENDDATE='" + ENDDATE + '\'' +
                    '}';
        }

        private String BEGDATE;
        private String ENDDATE;
                /*
                * SWVERSION : 12A1
                * BATVALUE : 2846
                * TINYIP : 0.0.0.3
                * BARCODE : 6901845040456
                * GCODE : 686459
                * GNAME : 格力高牛奶奇蒂70克
                * KIND : 220104
                * UNIT : 盒
                * SPEC : 70克
                * GCLASS : 合格
                * PROVIDER :
                * BRAND : 格力高
                * ORIPRICE : 5.2
                * UPTPRICE : 5.2
                * ORIGIN : 多产地，详见包装
                */

        public DeviceInfoAndMemidBean(String MEMID, String SWVERSION, String BATVALUE, String TINYIP, String BARCODE, String GCODE, String GNAME, String KIND, String UNIT, String SPEC, String GCLASS, String BRAND, String ORIGIN, String UPTPRICE,String ORIPRICE) {
            this.MEMID = MEMID;
            this.SWVERSION = SWVERSION;
            this.BATVALUE = BATVALUE;
            this.TINYIP = TINYIP;
            this.BARCODE = BARCODE;
            this.GCODE = GCODE;
            this.GNAME = GNAME;
            this.KIND = KIND;
            this.UNIT = UNIT;
            this.SPEC = SPEC;
            this.GCLASS = GCLASS;
            this.BRAND = BRAND;
            this.ORIGIN = ORIGIN;
            this.UPTPRICE = UPTPRICE;
            this.ORIPRICE = ORIPRICE;
        }

        public String getMEMID() {
            return MEMID;
        }

        public void setMEMID(String MEMID) {
            this.MEMID = MEMID;
        }

        public String getSWVERSION() {
            return SWVERSION;
        }

        public void setSWVERSION(String SWVERSION) {
            this.SWVERSION = SWVERSION;
        }

        public String getBATVALUE() {
            return BATVALUE;
        }

        public void setBATVALUE(String BATVALUE) {
            this.BATVALUE = BATVALUE;
        }

        public String getSERVERDATE() {
            return SERVERDATE;
        }

        public void setSERVERDATE(String SERVERDATE) {
            this.SERVERDATE = SERVERDATE;
        }

        public String getTINYIP() {
            return TINYIP;
        }

        public void setTINYIP(String TINYIP) {
            this.TINYIP = TINYIP;
        }

        public String getBARCODE() {
            return BARCODE;
        }

        public void setBARCODE(String BARCODE) {
            this.BARCODE = BARCODE;
        }

        public String getGCODE() {
            return GCODE;
        }

        public void setGCODE(String GCODE) {
            this.GCODE = GCODE;
        }

        public String getGNAME() {
            return GNAME;
        }

        public void setGNAME(String GNAME) {
            this.GNAME = GNAME;
        }

        public String getKIND() {
            return KIND;
        }

        public void setKIND(String KIND) {
            this.KIND = KIND;
        }

        public String getUNIT() {
            return UNIT;
        }

        public void setUNIT(String UNIT) {
            this.UNIT = UNIT;
        }

        public String getSPEC() {
            return SPEC;
        }

        public void setSPEC(String SPEC) {
            this.SPEC = SPEC;
        }

        public String getGCLASS() {
            return GCLASS;
        }

        public void setGCLASS(String GCLASS) {
            this.GCLASS = GCLASS;
        }

        public String getPROVIDER() {
            return PROVIDER;
        }

        public void setPROVIDER(String PROVIDER) {
            this.PROVIDER = PROVIDER;
        }

        public String getBRAND() {
            return BRAND;
        }

        public void setBRAND(String BRAND) {
            this.BRAND = BRAND;
        }

        public String getORIGIN() {
            return ORIGIN;
        }

        public void setORIGIN(String ORIGIN) {
            this.ORIGIN = ORIGIN;
        }

        public String getIMGPATH() {
            return IMGPATH;
        }

        public void setIMGPATH(String IMGPATH) {
            this.IMGPATH = IMGPATH;
        }

        public String getORIPRICE() {
            return ORIPRICE;
        }

        public void setORIPRICE(String ORIPRICE) {
            this.ORIPRICE = ORIPRICE;
        }

        public String getMEMPRICE() {
            return MEMPRICE;
        }

        public void setMEMPRICE(String MEMPRICE) {
            this.MEMPRICE = MEMPRICE;
        }

        public String getUPTPRICE() {
            return UPTPRICE;
        }

        public void setUPTPRICE(String UPTPRICE) {
            this.UPTPRICE = UPTPRICE;
        }

        public String getBEGDATE() {
            return BEGDATE;
        }

        public void setBEGDATE(String BEGDATE) {
            this.BEGDATE = BEGDATE;
        }

        public String getENDDATE() {
            return ENDDATE;
        }

        public void setENDDATE(String ENDDATE) {
            this.ENDDATE = ENDDATE;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.MEMID);
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
            dest.writeString(this.IMGPATH);
            dest.writeString(this.ORIPRICE);
            dest.writeString(this.MEMPRICE);
            dest.writeString(this.UPTPRICE);
            dest.writeString(this.BEGDATE);
            dest.writeString(this.ENDDATE);
        }

        public DeviceInfoAndMemidBean() {
        }

        protected DeviceInfoAndMemidBean(Parcel in) {
            this.MEMID = in.readString();
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
            this.IMGPATH = in.readString();
            this.ORIPRICE = in.readString();
            this.MEMPRICE = in.readString();
            this.UPTPRICE = in.readString();
            this.BEGDATE = in.readString();
            this.ENDDATE = in.readString();
        }

        public static final Creator<DeviceInfoAndMemidBean> CREATOR = new Creator<DeviceInfoAndMemidBean>() {
            @Override
            public DeviceInfoAndMemidBean createFromParcel(Parcel source) {
                return new DeviceInfoAndMemidBean(source);
            }

            @Override
            public DeviceInfoAndMemidBean[] newArray(int size) {
                return new DeviceInfoAndMemidBean[size];
            }
        };

        @Override
        public int compareTo(DeviceInfoAndMemidBean another) {
            return this.getTINYIP().lastIndexOf(getTINYIP().length())-another.getTINYIP().lastIndexOf(getTINYIP().length());
        }
    }
    }
