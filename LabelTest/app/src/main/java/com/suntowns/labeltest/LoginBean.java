package com.suntowns.labeltest;

/**
 * Created by Administrator on 2016/8/2.
 */
public class LoginBean {

    /**
     * RESULT : 0
     * USERINFO : {"NAME":"suger","LOGINNAME":"","MEMID":"1070","CARDNO":"","SEX":"0","AGE":"","TEL":"18037106850","ADDRESS":"","REGDATE":"2016-03-14 14:54:46.0","ENDDATE":"","MEMNO":"","REGKIND":"1","MENMARK":"0","EMAIL":"","KEY1KIND":"","LOGINTOKEN":"315287499482703","AVATAR":"www.suntowngis.com:8080/tempimg/avatar/1070.jpg","NICKNAME":"suger","AVATARTYPE":"1","WIFIID":"Albert","PUSHID":"171976fa8a81c27e962","PUSHNUM":"1"}
     */

    private String RESULT;
    /**
     * NAME : suger
     * LOGINNAME :
     * MEMID : 1070
     * CARDNO :
     * SEX : 0
     * AGE :
     * TEL : 18037106850
     * ADDRESS :
     * REGDATE : 2016-03-14 14:54:46.0
     * ENDDATE :
     * MEMNO :
     * REGKIND : 1
     * MENMARK : 0
     * EMAIL :
     * KEY1KIND :
     * LOGINTOKEN : 315287499482703
     * AVATAR : www.suntowngis.com:8080/tempimg/avatar/1070.jpg
     * NICKNAME : suger
     * AVATARTYPE : 1
     * WIFIID : Albert
     * PUSHID : 171976fa8a81c27e962
     * PUSHNUM : 1
     */

    private USERINFOBean USERINFO;

    public String getRESULT() {
        return RESULT;
    }

    public void setRESULT(String RESULT) {
        this.RESULT = RESULT;
    }

    public USERINFOBean getUSERINFO() {
        return USERINFO;
    }

    public void setUSERINFO(USERINFOBean USERINFO) {
        this.USERINFO = USERINFO;
    }

    public static class USERINFOBean {
        private String NAME;
        private String LOGINNAME;
        private String MEMID;
        private String CARDNO;
        private String SEX;
        private String AGE;
        private String TEL;
        private String ADDRESS;
        private String REGDATE;
        private String ENDDATE;
        private String MEMNO;
        private String REGKIND;
        private String MENMARK;
        private String EMAIL;
        private String KEY1KIND;
        private String LOGINTOKEN;
        private String AVATAR;
        private String NICKNAME;
        private String AVATARTYPE;
        private String WIFIID;
        private String PUSHID;
        private String PUSHNUM;

        public String getNAME() {
            return NAME;
        }

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }

        public String getLOGINNAME() {
            return LOGINNAME;
        }

        public void setLOGINNAME(String LOGINNAME) {
            this.LOGINNAME = LOGINNAME;
        }

        public String getMEMID() {
            return MEMID;
        }

        public void setMEMID(String MEMID) {
            this.MEMID = MEMID;
        }

        public String getCARDNO() {
            return CARDNO;
        }

        public void setCARDNO(String CARDNO) {
            this.CARDNO = CARDNO;
        }

        public String getSEX() {
            return SEX;
        }

        public void setSEX(String SEX) {
            this.SEX = SEX;
        }

        public String getAGE() {
            return AGE;
        }

        public void setAGE(String AGE) {
            this.AGE = AGE;
        }

        public String getTEL() {
            return TEL;
        }

        public void setTEL(String TEL) {
            this.TEL = TEL;
        }

        public String getADDRESS() {
            return ADDRESS;
        }

        public void setADDRESS(String ADDRESS) {
            this.ADDRESS = ADDRESS;
        }

        public String getREGDATE() {
            return REGDATE;
        }

        public void setREGDATE(String REGDATE) {
            this.REGDATE = REGDATE;
        }

        public String getENDDATE() {
            return ENDDATE;
        }

        public void setENDDATE(String ENDDATE) {
            this.ENDDATE = ENDDATE;
        }

        public String getMEMNO() {
            return MEMNO;
        }

        public void setMEMNO(String MEMNO) {
            this.MEMNO = MEMNO;
        }

        public String getREGKIND() {
            return REGKIND;
        }

        public void setREGKIND(String REGKIND) {
            this.REGKIND = REGKIND;
        }

        public String getMENMARK() {
            return MENMARK;
        }

        public void setMENMARK(String MENMARK) {
            this.MENMARK = MENMARK;
        }

        public String getEMAIL() {
            return EMAIL;
        }

        public void setEMAIL(String EMAIL) {
            this.EMAIL = EMAIL;
        }

        public String getKEY1KIND() {
            return KEY1KIND;
        }

        public void setKEY1KIND(String KEY1KIND) {
            this.KEY1KIND = KEY1KIND;
        }

        public String getLOGINTOKEN() {
            return LOGINTOKEN;
        }

        public void setLOGINTOKEN(String LOGINTOKEN) {
            this.LOGINTOKEN = LOGINTOKEN;
        }

        public String getAVATAR() {
            return AVATAR;
        }

        public void setAVATAR(String AVATAR) {
            this.AVATAR = AVATAR;
        }

        public String getNICKNAME() {
            return NICKNAME;
        }

        public void setNICKNAME(String NICKNAME) {
            this.NICKNAME = NICKNAME;
        }

        public String getAVATARTYPE() {
            return AVATARTYPE;
        }

        public void setAVATARTYPE(String AVATARTYPE) {
            this.AVATARTYPE = AVATARTYPE;
        }

        public String getWIFIID() {
            return WIFIID;
        }

        public void setWIFIID(String WIFIID) {
            this.WIFIID = WIFIID;
        }

        public String getPUSHID() {
            return PUSHID;
        }

        public void setPUSHID(String PUSHID) {
            this.PUSHID = PUSHID;
        }

        public String getPUSHNUM() {
            return PUSHNUM;
        }

        public void setPUSHNUM(String PUSHNUM) {
            this.PUSHNUM = PUSHNUM;
        }
    }
}
