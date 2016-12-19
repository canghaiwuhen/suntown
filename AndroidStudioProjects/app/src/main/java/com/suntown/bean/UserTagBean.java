package com.suntown.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class UserTagBean {

    /**
     * MEMID : 1375
     * ROWS : 1
     * RECORD : [{"SWVERSION":"14a1","BATVALUE":"11197","SERVERDATE":"","TINYIP":"0.0.0.7","BARCODE":"6942404240061","GCODE":"312989","GNAME":"激浪果味汽水饮料500ml","KIND":"140707","UNIT":"瓶","SPEC":"500ml","GCLASS":"合格","PROVIDER":"","BRAND":"激浪","ORIGIN":"多产地，详见包装","IMGPATH":"\\OneFloor\\K02G-1-14-1-014\\IMG_0949.jpg","ORIPRICE":"2.7","MEMPRICE":"","UPTPRICE":"2.7","BEGDATE":"","ENDDATE":""}]
     */

    private String MEMID;
    private int ROWS;
    /**
     * SWVERSION : 14a1
     * BATVALUE : 11197
     * SERVERDATE :
     * TINYIP : 0.0.0.7
     * BARCODE : 6942404240061
     * GCODE : 312989
     * GNAME : 激浪果味汽水饮料500ml
     * KIND : 140707
     * UNIT : 瓶
     * SPEC : 500ml
     * GCLASS : 合格
     * PROVIDER :
     * BRAND : 激浪
     * ORIGIN : 多产地，详见包装
     * IMGPATH : \OneFloor\K02G-1-14-1-014\IMG_0949.jpg
     * ORIPRICE : 2.7
     * MEMPRICE :
     * UPTPRICE : 2.7
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

    public static class RECORDBean {
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
                    ", IMGPATH='" + IMGPATH + '\'' +
                    ", ORIPRICE='" + ORIPRICE + '\'' +
                    ", MEMPRICE='" + MEMPRICE + '\'' +
                    ", UPTPRICE='" + UPTPRICE + '\'' +
                    ", BEGDATE='" + BEGDATE + '\'' +
                    ", ENDDATE='" + ENDDATE + '\'' +
                    '}';
        }
    }
}
