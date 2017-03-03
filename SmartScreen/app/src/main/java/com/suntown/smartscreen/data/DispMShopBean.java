package com.suntown.smartscreen.data;

import java.util.List;

/**
 * Created by Administrator on 2017/2/13.
 */

public class DispMShopBean {
    /**
     * ROWS : 1
     * RECORD : [{"SID":"571002002","SNAME":"施家桥店","ISACTIVE":"1"}]
     */

    public int ROWS;
    public List<RECORDBean> RECORD;

    @Override
    public String toString() {
        return "DispMShopBean{" +
                "ROWS=" + ROWS +
                ", RECORD=" + RECORD +
                '}';
    }

    public static class RECORDBean {
        /**
         * SID : 571002002
         * SNAME : 施家桥店
         * ISACTIVE : 1
         */

        public String SID;
        public String SNAME;
        public String ISACTIVE;


        @Override
        public String toString() {
            return "RECORDBean{" +
                    "SID='" + SID + '\'' +
                    ", SNAME='" + SNAME + '\'' +
                    ", ISACTIVE='" + ISACTIVE + '\'' +
                    '}';
        }
    }
}
