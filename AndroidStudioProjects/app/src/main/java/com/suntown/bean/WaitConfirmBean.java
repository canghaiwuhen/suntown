package com.suntown.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/15.
 */
public class WaitConfirmBean {

    /**
     * RESULT : 0
     * RECORD : [{"SID":"","ADDDATE":"2016-04-27 17:37:11.0","FORMNO":"108746399618254","MONEY":"12.9","PAYSTARTDATE":"","PAYKIND":"","PAYDATE":"","MEMID":"1070","PAYSTATUS":"0","FORMSTATUS":"0","TSTATUS":"0","EVASTATUS":"0","ADDRID":"663","ADDRESS":"陈＊＊/18037106523/河北省邯郸市临漳县1098号","TEL":"18037106850","SHIPTSTATUS":"0","APPID":"一键下单","APPTYPE":"0","MSGID":"","MSGSENDFLAG":"0","FNAME":"","ORDERINFO":[{"BARCODE":"6920912352360","DTYPE":"1","TINYIP":"0.0.0.4","GNAME":"盼盼面包(玉米味)440g","ORIGIN":"福建龙岩","IMGPATH":"www.suntowngis.com:8080/tempimg/TwoFloor/K02G-2-22-1-011/IMG_0128.JPG"}]},{"SID":"","ADDDATE":"2016-04-27 17:30:03.0","FORMNO":"305149781679482","MONEY":"12.9","PAYSTARTDATE":"","PAYKIND":"","PAYDATE":"","MEMID":"1070","PAYSTATUS":"0","FORMSTATUS":"0","TSTATUS":"0","EVASTATUS":"0","ADDRID":"663","ADDRESS":"Tyre/15789045367/辽宁省大连市沙河口区Asddffggjj","TEL":"18037106850","SHIPTSTATUS":"0","APPID":"一键下单","APPTYPE":"0","MSGID":"","MSGSENDFLAG":"0","FNAME":"","ORDERINFO":[{"BARCODE":"6920912352360","DTYPE":"1","TINYIP":"0.0.0.4","GNAME":"盼盼面包(玉米味)440g","ORIGIN":"福建龙岩","IMGPATH":"www.suntowngis.com:8080/tempimg/TwoFloor/K02G-2-22-1-011/IMG_0128.JPG"}]}]
     */

    private String RESULT;
    /**
     * SID :
     * ADDDATE : 2016-04-27 17:37:11.0
     * FORMNO : 108746399618254
     * MONEY : 12.9
     * PAYSTARTDATE :
     * PAYKIND :
     * PAYDATE :
     * MEMID : 1070
     * PAYSTATUS : 0
     * FORMSTATUS : 0
     * TSTATUS : 0
     * EVASTATUS : 0
     * ADDRID : 663
     * ADDRESS : 陈＊＊/18037106523/河北省邯郸市临漳县1098号
     * TEL : 18037106850
     * SHIPTSTATUS : 0
     * APPID : 一键下单
     * APPTYPE : 0
     * MSGID :
     * MSGSENDFLAG : 0
     * FNAME :
     * ORDERINFO : [{"BARCODE":"6920912352360","DTYPE":"1","TINYIP":"0.0.0.4","GNAME":"盼盼面包(玉米味)440g","ORIGIN":"福建龙岩","IMGPATH":"www.suntowngis.com:8080/tempimg/TwoFloor/K02G-2-22-1-011/IMG_0128.JPG"}]
     */

    private List<RECORDBean> RECORD;

    public String getRESULT() {
        return RESULT;
    }

    public void setRESULT(String RESULT) {
        this.RESULT = RESULT;
    }

    public List<RECORDBean> getRECORD() {
        return RECORD;
    }

    public void setRECORD(List<RECORDBean> RECORD) {
        this.RECORD = RECORD;
    }

    public static class RECORDBean implements Parcelable {
        private String SID;
        private String ADDDATE;
        private String FORMNO;
        private String MONEY;
        private String PAYSTARTDATE;
        private String PAYKIND;
        private String PAYDATE;
        private String MEMID;
        private String PAYSTATUS;
        private String FORMSTATUS;
        private String TSTATUS;
        private String EVASTATUS;
        private String ADDRID;
        private String ADDRESS;
        private String TEL;
        private String SHIPTSTATUS;
        private String APPID;
        private String APPTYPE;
        private String MSGID;
        private String MSGSENDFLAG;
        private String FNAME;
        /**
         * BARCODE : 6920912352360
         * DTYPE : 1
         * TINYIP : 0.0.0.4
         * GNAME : 盼盼面包(玉米味)440g
         * ORIGIN : 福建龙岩
         * IMGPATH : www.suntowngis.com:8080/tempimg/TwoFloor/K02G-2-22-1-011/IMG_0128.JPG
         */

        private List<ORDERINFOBean> ORDERINFO;

        public String getSID() {
            return SID;
        }

        public void setSID(String SID) {
            this.SID = SID;
        }

        public String getADDDATE() {
            return ADDDATE;
        }

        public void setADDDATE(String ADDDATE) {
            this.ADDDATE = ADDDATE;
        }

        public String getFORMNO() {
            return FORMNO;
        }

        public void setFORMNO(String FORMNO) {
            this.FORMNO = FORMNO;
        }

        public String getMONEY() {
            return MONEY;
        }

        public void setMONEY(String MONEY) {
            this.MONEY = MONEY;
        }

        public String getPAYSTARTDATE() {
            return PAYSTARTDATE;
        }

        public void setPAYSTARTDATE(String PAYSTARTDATE) {
            this.PAYSTARTDATE = PAYSTARTDATE;
        }

        public String getPAYKIND() {
            return PAYKIND;
        }

        public void setPAYKIND(String PAYKIND) {
            this.PAYKIND = PAYKIND;
        }

        public String getPAYDATE() {
            return PAYDATE;
        }

        public void setPAYDATE(String PAYDATE) {
            this.PAYDATE = PAYDATE;
        }

        public String getMEMID() {
            return MEMID;
        }

        public void setMEMID(String MEMID) {
            this.MEMID = MEMID;
        }

        public String getPAYSTATUS() {
            return PAYSTATUS;
        }

        public void setPAYSTATUS(String PAYSTATUS) {
            this.PAYSTATUS = PAYSTATUS;
        }

        public String getFORMSTATUS() {
            return FORMSTATUS;
        }

        public void setFORMSTATUS(String FORMSTATUS) {
            this.FORMSTATUS = FORMSTATUS;
        }

        public String getTSTATUS() {
            return TSTATUS;
        }

        public void setTSTATUS(String TSTATUS) {
            this.TSTATUS = TSTATUS;
        }

        public String getEVASTATUS() {
            return EVASTATUS;
        }

        public void setEVASTATUS(String EVASTATUS) {
            this.EVASTATUS = EVASTATUS;
        }

        public String getADDRID() {
            return ADDRID;
        }

        public void setADDRID(String ADDRID) {
            this.ADDRID = ADDRID;
        }

        public String getADDRESS() {
            return ADDRESS;
        }

        public void setADDRESS(String ADDRESS) {
            this.ADDRESS = ADDRESS;
        }

        public String getTEL() {
            return TEL;
        }

        public void setTEL(String TEL) {
            this.TEL = TEL;
        }

        public String getSHIPTSTATUS() {
            return SHIPTSTATUS;
        }

        public void setSHIPTSTATUS(String SHIPTSTATUS) {
            this.SHIPTSTATUS = SHIPTSTATUS;
        }

        public String getAPPID() {
            return APPID;
        }

        public void setAPPID(String APPID) {
            this.APPID = APPID;
        }

        public String getAPPTYPE() {
            return APPTYPE;
        }

        public void setAPPTYPE(String APPTYPE) {
            this.APPTYPE = APPTYPE;
        }

        public String getMSGID() {
            return MSGID;
        }

        public void setMSGID(String MSGID) {
            this.MSGID = MSGID;
        }

        public String getMSGSENDFLAG() {
            return MSGSENDFLAG;
        }

        public void setMSGSENDFLAG(String MSGSENDFLAG) {
            this.MSGSENDFLAG = MSGSENDFLAG;
        }

        public String getFNAME() {
            return FNAME;
        }

        public void setFNAME(String FNAME) {
            this.FNAME = FNAME;
        }

        public List<ORDERINFOBean> getORDERINFO() {
            return ORDERINFO;
        }

        public void setORDERINFO(List<ORDERINFOBean> ORDERINFO) {
            this.ORDERINFO = ORDERINFO;
        }

        @Override
        public String toString() {
            return "RECORDBean{" +
                    "SID='" + SID + '\'' +
                    ", ADDDATE='" + ADDDATE + '\'' +
                    ", FORMNO='" + FORMNO + '\'' +
                    ", MONEY='" + MONEY + '\'' +
                    ", PAYSTARTDATE='" + PAYSTARTDATE + '\'' +
                    ", PAYKIND='" + PAYKIND + '\'' +
                    ", PAYDATE='" + PAYDATE + '\'' +
                    ", MEMID='" + MEMID + '\'' +
                    ", PAYSTATUS='" + PAYSTATUS + '\'' +
                    ", FORMSTATUS='" + FORMSTATUS + '\'' +
                    ", TSTATUS='" + TSTATUS + '\'' +
                    ", EVASTATUS='" + EVASTATUS + '\'' +
                    ", ADDRID='" + ADDRID + '\'' +
                    ", ADDRESS='" + ADDRESS + '\'' +
                    ", TEL='" + TEL + '\'' +
                    ", SHIPTSTATUS='" + SHIPTSTATUS + '\'' +
                    ", APPID='" + APPID + '\'' +
                    ", APPTYPE='" + APPTYPE + '\'' +
                    ", MSGID='" + MSGID + '\'' +
                    ", MSGSENDFLAG='" + MSGSENDFLAG + '\'' +
                    ", FNAME='" + FNAME + '\'' +
                    ", ORDERINFO=" + ORDERINFO +
                    '}';
        }

        public static class ORDERINFOBean implements Parcelable {
            private String BARCODE;
            private String NUM;
            private String DTYPE;
            private String TINYIP;
            private String GNAME;
            private String ORIGIN;
            private String IMGPATH;


            public String getBARCODE() {
                return BARCODE;
            }

            public void setBARCODE(String BARCODE) {
                this.BARCODE = BARCODE;
            }

            public String getNUM() {
                return NUM;
            }

            public void setNUM(String NUM) {
                this.NUM = NUM;
            }

            public String getDTYPE() {
                return DTYPE;
            }

            public void setDTYPE(String DTYPE) {
                this.DTYPE = DTYPE;
            }

            public String getTINYIP() {
                return TINYIP;
            }

            public void setTINYIP(String TINYIP) {
                this.TINYIP = TINYIP;
            }

            public String getGNAME() {
                return GNAME;
            }

            public void setGNAME(String GNAME) {
                this.GNAME = GNAME;
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

            @Override
            public String toString() {
                return "ORDERINFOBean{" +
                        "BARCODE='" + BARCODE + '\'' +
                        ", NUM='" + NUM + '\'' +
                        ", DTYPE='" + DTYPE + '\'' +
                        ", TINYIP='" + TINYIP + '\'' +
                        ", GNAME='" + GNAME + '\'' +
                        ", ORIGIN='" + ORIGIN + '\'' +
                        ", IMGPATH='" + IMGPATH + '\'' +
                        '}';
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.BARCODE);
                dest.writeString(this.NUM);
                dest.writeString(this.DTYPE);
                dest.writeString(this.TINYIP);
                dest.writeString(this.GNAME);
                dest.writeString(this.ORIGIN);
                dest.writeString(this.IMGPATH);
            }

            public ORDERINFOBean() {
            }

            protected ORDERINFOBean(Parcel in) {
                this.BARCODE = in.readString();
                this.NUM = in.readString();
                this.DTYPE = in.readString();
                this.TINYIP = in.readString();
                this.GNAME = in.readString();
                this.ORIGIN = in.readString();
                this.IMGPATH = in.readString();
            }

            public static final Creator<ORDERINFOBean> CREATOR = new Creator<ORDERINFOBean>() {
                @Override
                public ORDERINFOBean createFromParcel(Parcel source) {
                    return new ORDERINFOBean(source);
                }

                @Override
                public ORDERINFOBean[] newArray(int size) {
                    return new ORDERINFOBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.SID);
            dest.writeString(this.ADDDATE);
            dest.writeString(this.FORMNO);
            dest.writeString(this.MONEY);
            dest.writeString(this.PAYSTARTDATE);
            dest.writeString(this.PAYKIND);
            dest.writeString(this.PAYDATE);
            dest.writeString(this.MEMID);
            dest.writeString(this.PAYSTATUS);
            dest.writeString(this.FORMSTATUS);
            dest.writeString(this.TSTATUS);
            dest.writeString(this.EVASTATUS);
            dest.writeString(this.ADDRID);
            dest.writeString(this.ADDRESS);
            dest.writeString(this.TEL);
            dest.writeString(this.SHIPTSTATUS);
            dest.writeString(this.APPID);
            dest.writeString(this.APPTYPE);
            dest.writeString(this.MSGID);
            dest.writeString(this.MSGSENDFLAG);
            dest.writeString(this.FNAME);
            dest.writeTypedList(this.ORDERINFO);
        }

        public RECORDBean() {
        }

        protected RECORDBean(Parcel in) {
            this.SID = in.readString();
            this.ADDDATE = in.readString();
            this.FORMNO = in.readString();
            this.MONEY = in.readString();
            this.PAYSTARTDATE = in.readString();
            this.PAYKIND = in.readString();
            this.PAYDATE = in.readString();
            this.MEMID = in.readString();
            this.PAYSTATUS = in.readString();
            this.FORMSTATUS = in.readString();
            this.TSTATUS = in.readString();
            this.EVASTATUS = in.readString();
            this.ADDRID = in.readString();
            this.ADDRESS = in.readString();
            this.TEL = in.readString();
            this.SHIPTSTATUS = in.readString();
            this.APPID = in.readString();
            this.APPTYPE = in.readString();
            this.MSGID = in.readString();
            this.MSGSENDFLAG = in.readString();
            this.FNAME = in.readString();
            this.ORDERINFO = in.createTypedArrayList(ORDERINFOBean.CREATOR);
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
}
