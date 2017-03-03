package com.suntown.smartscreen.maintain.detial;

import java.util.List;

/**
 * Created by Administrator on 2017/2/13.
 */

public class DmDetialBean {
    /**
     * ROWS : 1
     * RECORD : [{"TYPENAME":"陈列模板","DMSPECID":"121","ANGLE":"0","DMNAME":"23232","ISSPLASH":"0","LIGHTPERIOD":"300","LIGHTCODENAME":"黄灯","DESCRIBE":"1","DMIMG":"Qk1qAAAAAAAAAD4AAAAoAAAACwAAAAsAAAABAAEAAAAAACwAAAAAAAAAAAAAAAAAAAACAAAAAAAA\n////////4AAA/+AAAP/gAAD/4AAA/2AAAHyAAAD/4AAAP+AAAD/gAACH4AAAP+AAAA==","ISSPLASHNAME":"无闪屏","DMCODE":"347","SPECNAME":"11","ISACTIVE":"0","TYPE":"1","LIGHTCODE":"3"}]
     */

    public int ROWS;
    public List<RECORDBean> RECORD;


    public static class RECORDBean {
        /**
         * TYPENAME : 陈列模板
         * DMSPECID : 121
         * ANGLE : 0
         * DMNAME : 23232
         * ISSPLASH : 0
         * LIGHTPERIOD : 300
         * LIGHTCODENAME : 黄灯
         * DESCRIBE : 1
         * DMIMG : Qk1qAAAAAAAAAD4AAAAoAAAACwAAAAsAAAABAAEAAAAAACwAAAAAAAAAAAAAAAAAAAACAAAAAAAA
         ////////4AAA/+AAAP/gAAD/4AAA/2AAAHyAAAD/4AAAP+AAAD/gAACH4AAAP+AAAA==
         * ISSPLASHNAME : 无闪屏
         * DMCODE : 347
         * SPECNAME : 11
         * ISACTIVE : 0
         * TYPE : 1
         * LIGHTCODE : 3
         */

        public String TYPENAME;
        public String DMSPECID;
        public String ANGLE;
        public String DMNAME;
        public String ISSPLASH;
        public String LIGHTPERIOD;
        public String LIGHTCODENAME;
        public String DESCRIBE;
        public String DMIMG;
        public String ISSPLASHNAME;
        public String DMCODE;
        public String SPECNAME;
        public String ISACTIVE;
        public String TYPE;
        public String LIGHTCODE;

    }
}
