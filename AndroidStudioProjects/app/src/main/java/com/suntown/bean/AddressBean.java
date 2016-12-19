package com.suntown.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/22.
 */
public class AddressBean {

    /**
     * RESULT : 0
     * RECORD : [{"ID":"761","ADDDATE":"2016-08-22 13:48:14.0","MEMID":"1352","ADDRESS":"杭州西湖","ISDEFAULT":"0","PHONE":"15537450621","RECEIVER":"汤振"},{"ID":"762","ADDDATE":"2016-08-22 14:00:34.0","MEMID":"1352","ADDRESS":"浙江省杭州市西湖区振华路西港新界西区，A憧16楼","ISDEFAULT":"1","PHONE":"12345678977","RECEIVER":"汤振"}]
     */

    private String RESULT;
    /**
     * ID : 761
     * ADDDATE : 2016-08-22 13:48:14.0
     * MEMID : 1352
     * ADDRESS : 杭州西湖
     * ISDEFAULT : 0
     * PHONE : 15537450621
     * RECEIVER : 汤振
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

    public static class RECORDBean {
        private String ID;
        private String ADDDATE;
        private String MEMID;
        private String ADDRESS;
        private String ISDEFAULT;
        private String PHONE;
        private String RECEIVER;

        public RECORDBean(String ID, String ADDDATE, String MEMID, String ADDRESS, String ISDEFAULT, String PHONE, String RECEIVER) {
            this.ID = ID;
            this.ADDDATE = ADDDATE;
            this.MEMID = MEMID;
            this.ADDRESS = ADDRESS;
            this.ISDEFAULT = ISDEFAULT;
            this.PHONE = PHONE;
            this.RECEIVER = RECEIVER;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getADDDATE() {
            return ADDDATE;
        }

        public void setADDDATE(String ADDDATE) {
            this.ADDDATE = ADDDATE;
        }

        public String getMEMID() {
            return MEMID;
        }

        public void setMEMID(String MEMID) {
            this.MEMID = MEMID;
        }

        public String getADDRESS() {
            return ADDRESS;
        }

        public void setADDRESS(String ADDRESS) {
            this.ADDRESS = ADDRESS;
        }

        public String getISDEFAULT() {
            return ISDEFAULT;
        }

        public void setISDEFAULT(String ISDEFAULT) {
            this.ISDEFAULT = ISDEFAULT;
        }

        public String getPHONE() {
            return PHONE;
        }

        public void setPHONE(String PHONE) {
            this.PHONE = PHONE;
        }

        public String getRECEIVER() {
            return RECEIVER;
        }

        public void setRECEIVER(String RECEIVER) {
            this.RECEIVER = RECEIVER;
        }
    }
}
