package com.suntown.smartscreen.data;

import java.util.List;

/**
 * Created by Administrator on 2017/2/8.
 */

public class ServerBean {
    /**
     * ROWS : 4
     * RECORD : [{"MODID":"21","UNAME":"suntown","MODUSERID":"suntown","MODNAME":"test","MODURL":"http://www.smartesl.com.cn","USERID":"suntown"},{"MODID":"41","UNAME":"suntown","MODUSERID":"suntown","MODNAME":"测试云","MODURL":"http://www.smartesl.com.cn","USERID":"suntown"},{"MODID":"61","UNAME":"suntown","MODUSERID":"suntown","MODNAME":"181","MODURL":"http://192.168.0.181:8080","USERID":"suntown"},{"MODID":"62","UNAME":"suntown","MODUSERID":"suntown","MODNAME":"143","MODURL":"http://192.168.0.143:8080","USERID":"suntown"}]
     */

    public int ROWS;
    public List<RECORDBean> RECORD;

    @Override
    public String toString() {
        return "ServerBean{" +
                "ROWS=" + ROWS +
                ", RECORD=" + RECORD +
                '}';
    }

    public static class RECORDBean {
        /**
         * MODID : 21
         * UNAME : suntown
         * MODUSERID : suntown
         * MODNAME : test
         * MODURL : http://www.smartesl.com.cn
         * USERID : suntown
         */

        public String MODID;
        public String UNAME;
        public String MODUSERID;
        public String MODNAME;
        public String MODURL;
        public String USERID;

        @Override
        public String toString() {
            return "RECORDBean{" +
                    "MODID='" + MODID + '\'' +
                    ", UNAME='" + UNAME + '\'' +
                    ", MODUSERID='" + MODUSERID + '\'' +
                    ", MODNAME='" + MODNAME + '\'' +
                    ", MODURL='" + MODURL + '\'' +
                    ", USERID='" + USERID + '\'' +
                    '}';
        }
    }
}
