package com.suntown.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/8.
 */
public class GNameBean {

    /**
     * ROWS : 7
     * RECORD : [{"BARCODE":"6901285991530","GCODE":"209232","GNAME":"怡宝纯净水4.5升","SPEC":"4.5升","UNIT":"支","GCLASS":"合格","ORIGIN":"见商品包装","BRAND":"怡宝","PKNUMBER":"1","KIND":"141101","PROVIDER":"","WEIGHT":"","SID":"571002002","SNAME":"施家桥店","MID":"1","UPTPRICE":"9","ORIPRICE":"9","MEMPRICE":"","PRICETYPE":"0","RID":"1","DELIVERYMODE":"0","BEGDATE":"","ENDDATE":"","IMGPATH":"www.suntowngis.com:8080/tempimg/OneFloor/K02G-1-14-1-018/IMG_2600.jpg"},{"BARCODE":"6901285991479","GCODE":"1658811","GNAME":"怡宝纯净水350ml*12","SPEC":"350ml*12","UNIT":"组","GCLASS":"合格","ORIGIN":"见商品包装","BRAND":"怡宝","PKNUMBER":"1","KIND":"141101","PROVIDER":"","WEIGHT":"","SID":"571002002","SNAME":"施家桥店","MID":"1","UPTPRICE":"15.3","ORIPRICE":"15.3","MEMPRICE":"","PRICETYPE":"0","RID":"2","DELIVERYMODE":"0","BEGDATE":"","ENDDATE":"","IMGPATH":"www.suntowngis.com:8080/tempimg/OneFloor/K02G-1-14-1-017/IMG_2582.jpg"},{"BARCODE":"6901285991486","GCODE":"1150590","GNAME":"怡宝纯净水量贩装555ml*12","SPEC":"555ml*12","UNIT":"件","GCLASS":"合格","ORIGIN":"多产地，详见包装","BRAND":"怡宝","PKNUMBER":"1","KIND":"141101","PROVIDER":"","WEIGHT":"","SID":"571002002","SNAME":"施家桥店","MID":"1","UPTPRICE":"16.2","ORIPRICE":"16.2","MEMPRICE":"","PRICETYPE":"0","RID":"3","DELIVERYMODE":"0","BEGDATE":"","ENDDATE":"","IMGPATH":"www.suntowngis.com:8080/tempimg/OneFloor/K02G-1-14-1-017/IMG_2572.jpg"},{"BARCODE":"6901285991486","GCODE":"1150590","GNAME":"怡宝饮用纯净水量贩装555ml*12","SPEC":"555ml*12","UNIT":"件","GCLASS":"合格","ORIGIN":"见商品包装","BRAND":"怡宝","PKNUMBER":"1","KIND":"141101","PROVIDER":"","WEIGHT":"","SID":"571002002","SNAME":"施家桥店","MID":"1","UPTPRICE":"16.2","ORIPRICE":"16.2","MEMPRICE":"","PRICETYPE":"0","RID":"4","DELIVERYMODE":"0","BEGDATE":"","ENDDATE":"","IMGPATH":"www.suntowngis.com:8080/tempimg/OneFloor/K02G-1-14-1-017/IMG_2572.jpg"},{"BARCODE":"6901285991240","GCODE":"384305","GNAME":"怡宝纯净水350ml","SPEC":"350ml","UNIT":"瓶","GCLASS":"合格","ORIGIN":"多产地，详见包装","BRAND":"怡宝","PKNUMBER":"1","KIND":"141101","PROVIDER":"","WEIGHT":"","SID":"571002002","SNAME":"施家桥店","MID":"1","UPTPRICE":"1.3","ORIPRICE":"1.3","MEMPRICE":"","PRICETYPE":"0","RID":"5","DELIVERYMODE":"0","BEGDATE":"","ENDDATE":"","IMGPATH":"www.suntowngis.com:8080/tempimg/OneFloor/K02G-1-14-1-017/IMG_2587.jpg"},{"BARCODE":"6901285991240","GCODE":"384305","GNAME":"怡宝纯净水350ml","SPEC":"350ml","UNIT":"瓶","GCLASS":"合格","ORIGIN":"见商品包装","BRAND":"怡宝","PKNUMBER":"1","KIND":"141101","PROVIDER":"","WEIGHT":"","SID":"571002002","SNAME":"施家桥店","MID":"1","UPTPRICE":"1.3","ORIPRICE":"1.3","MEMPRICE":"","PRICETYPE":"0","RID":"6","DELIVERYMODE":"0","BEGDATE":"","ENDDATE":"","IMGPATH":"www.suntowngis.com:8080/tempimg/OneFloor/K02G-1-14-1-017/IMG_2587.jpg"},{"BARCODE":"6920458826370","GCODE":"656834","GNAME":"怡宝魔力氨基酸500ml","SPEC":"500ml","UNIT":"瓶","GCLASS":"合格","ORIGIN":"上海闵行","BRAND":"怡宝","PKNUMBER":"1","KIND":"141102","PROVIDER":"","WEIGHT":"","SID":"571002002","SNAME":"施家桥店","MID":"1","UPTPRICE":"3","ORIPRICE":"3","MEMPRICE":"","PRICETYPE":"0","RID":"7","DELIVERYMODE":"0","BEGDATE":"","ENDDATE":"","IMGPATH":"www.suntowngis.com:8080/tempimg/OneFloor/K02G-1-14-1-019/IMG_2633.jpg"}]
     */

    private int ROWS;
    /**
     * BARCODE : 6901285991530
     * GCODE : 209232
     * GNAME : 怡宝纯净水4.5升
     * SPEC : 4.5升
     * UNIT : 支
     * GCLASS : 合格
     * ORIGIN : 见商品包装
     * BRAND : 怡宝
     * PKNUMBER : 1
     * KIND : 141101
     * PROVIDER :
     * WEIGHT :
     * SID : 571002002
     * SNAME : 施家桥店
     * MID : 1
     * UPTPRICE : 9
     * ORIPRICE : 9
     * MEMPRICE :
     * PRICETYPE : 0
     * RID : 1
     * DELIVERYMODE : 0
     * BEGDATE :
     * ENDDATE :
     * IMGPATH : www.suntowngis.com:8080/tempimg/OneFloor/K02G-1-14-1-018/IMG_2600.jpg
     */

    private List<RECORDBean> RECORD;

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
        private String BARCODE;
        private String GCODE;
        private String GNAME;
        private String SPEC;
        private String UNIT;
        private String GCLASS;
        private String ORIGIN;
        private String BRAND;
        private String PKNUMBER;
        private String KIND;
        private String PROVIDER;
        private String WEIGHT;
        private String SID;
        private String SNAME;
        private String MID;
        private String UPTPRICE;
        private String ORIPRICE;
        private String MEMPRICE;
        private String PRICETYPE;
        private String RID;
        private String DELIVERYMODE;
        private String BEGDATE;
        private String ENDDATE;
        private String IMGPATH;

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

        public String getSPEC() {
            return SPEC;
        }

        public void setSPEC(String SPEC) {
            this.SPEC = SPEC;
        }

        public String getUNIT() {
            return UNIT;
        }

        public void setUNIT(String UNIT) {
            this.UNIT = UNIT;
        }

        public String getGCLASS() {
            return GCLASS;
        }

        public void setGCLASS(String GCLASS) {
            this.GCLASS = GCLASS;
        }

        public String getORIGIN() {
            return ORIGIN;
        }

        public void setORIGIN(String ORIGIN) {
            this.ORIGIN = ORIGIN;
        }

        public String getBRAND() {
            return BRAND;
        }

        public void setBRAND(String BRAND) {
            this.BRAND = BRAND;
        }

        public String getPKNUMBER() {
            return PKNUMBER;
        }

        public void setPKNUMBER(String PKNUMBER) {
            this.PKNUMBER = PKNUMBER;
        }

        public String getKIND() {
            return KIND;
        }

        public void setKIND(String KIND) {
            this.KIND = KIND;
        }

        public String getPROVIDER() {
            return PROVIDER;
        }

        public void setPROVIDER(String PROVIDER) {
            this.PROVIDER = PROVIDER;
        }

        public String getWEIGHT() {
            return WEIGHT;
        }

        public void setWEIGHT(String WEIGHT) {
            this.WEIGHT = WEIGHT;
        }

        public String getSID() {
            return SID;
        }

        public void setSID(String SID) {
            this.SID = SID;
        }

        public String getSNAME() {
            return SNAME;
        }

        public void setSNAME(String SNAME) {
            this.SNAME = SNAME;
        }

        public String getMID() {
            return MID;
        }

        public void setMID(String MID) {
            this.MID = MID;
        }

        public String getUPTPRICE() {
            return UPTPRICE;
        }

        public void setUPTPRICE(String UPTPRICE) {
            this.UPTPRICE = UPTPRICE;
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

        public String getPRICETYPE() {
            return PRICETYPE;
        }

        public void setPRICETYPE(String PRICETYPE) {
            this.PRICETYPE = PRICETYPE;
        }

        public String getRID() {
            return RID;
        }

        public void setRID(String RID) {
            this.RID = RID;
        }

        public String getDELIVERYMODE() {
            return DELIVERYMODE;
        }

        public void setDELIVERYMODE(String DELIVERYMODE) {
            this.DELIVERYMODE = DELIVERYMODE;
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

        public String getIMGPATH() {
            return IMGPATH;
        }

        public void setIMGPATH(String IMGPATH) {
            this.IMGPATH = IMGPATH;
        }
    }
}
