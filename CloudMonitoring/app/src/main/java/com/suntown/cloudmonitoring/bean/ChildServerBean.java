package com.suntown.cloudmonitoring.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class ChildServerBean {

    /**
     * USERID : admin
     * UNAME : admin
     * ROWS : 5
     * RECORD : [{"modid":"1","modname":"143","modurl":"http://192.168.0.143:8080","moduserid":"admin"},{"modid":"2","modname":"浜戞湇鍔�","modurl":"http://192.168.0.12:8080","moduserid":"admin"},{"modid":"41","modname":"240","modurl":"http://192.168.0.240","moduserid":"manager"},{"modid":"61","modname":"181","modurl":"http://192.168.0.181:8080","moduserid":"admin"},{"modid":"81","modname":"152","modurl":"http://192.168.0.152:8090","moduserid":"admin"}]
     */

    private String USERID;
    private String UNAME;
    private int ROWS;
    /**
     * modid : 1
     * modname : 143
     * modurl : http://192.168.0.143:8080
     * moduserid : admin
     */

    private List<RECORDBean> RECORD;

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getUNAME() {
        return UNAME;
    }

    public void setUNAME(String UNAME) {
        this.UNAME = UNAME;
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
        private String modid;
        private String modname;
        private String modurl;
        private String moduserid;

        public String getModid() {
            return modid;
        }

        public void setModid(String modid) {
            this.modid = modid;
        }

        public String getModname() {
            return modname;
        }

        public void setModname(String modname) {
            this.modname = modname;
        }

        public String getModurl() {
            return modurl;
        }

        public void setModurl(String modurl) {
            this.modurl = modurl;
        }

        public String getModuserid() {
            return moduserid;
        }

        public void setModuserid(String moduserid) {
            this.moduserid = moduserid;
        }

        @Override
        public String toString() {
            return "RECORDBean{" +
                    "modid='" + modid + '\'' +
                    ", modname='" + modname + '\'' +
                    ", modurl='" + modurl + '\'' +
                    ", moduserid='" + moduserid + '\'' +
                    '}';
        }
    }
}
