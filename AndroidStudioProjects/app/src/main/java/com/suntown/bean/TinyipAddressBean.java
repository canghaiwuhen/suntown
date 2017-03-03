package com.suntown.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/4.
 */

public class TinyipAddressBean {
    /**
     * ROWS : 1
     * RECORD : [{"ID":"744","ADDDATE":"2016-05-25 10:34:52.0","MEMID":"1070","ADDRESS":"辽宁省大连市沙河口区Asddffggjj1231","ISDEFAULT":"1","PHONE":"15789045367","RECEIVER":"Tyre","TINYIP":"0.0.0.7"}]
     */

    public int ROWS;
    public List<RECORDBean> RECORD;


    public static class RECORDBean {
        /**
         * ID : 744
         * ADDDATE : 2016-05-25 10:34:52.0
         * MEMID : 1070
         * ADDRESS : 辽宁省大连市沙河口区Asddffggjj1231
         * ISDEFAULT : 1
         * PHONE : 15789045367
         * RECEIVER : Tyre
         * TINYIP : 0.0.0.7
         */

        public String ID;
        public String ADDDATE;
        public String MEMID;
        public String ADDRESS;
        public String ISDEFAULT;
        public String PHONE;
        public String RECEIVER;
        public String TINYIP;


    }
}
