package com.suntown.cloudmonitoring.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class BatteryDangerBean {

    /**
     * NAME :
     * USERID : admin
     * RECORD : [{"FALLRANGE":"95-30","ROWS":0,"RECORD":[{"ACODE":"571","ACTIVITYDATE":"2016-09-27 13:57:19","AID":"K02G-1-14-1-012-1-1","ANAME":"杭州","APADDR":"119","APIP":"192.168.0.195","BARCODE":"0000000513760","BATTERY":3008,"GCODE":"","GNAME":"M&M'Sm&ms迷你筒装巧克力30.6g30.6g","LASTRETDATE":"2016-09-21 15:30:32","LQI":-31,"PCODE":"8601","PORT":"57345","SID":"571002001","SNAME":"濮家店","TINYIP":"0.0.75.150"}]},{"FALLRANGE":"95-10","ROWS":0,"RECORD":[]},{"FALLRANGE":"80-30","ROWS":0,"RECORD":[]},{"FALLRANGE":"80-10","ROWS":0,"RECORD":[]},{"FALLRANGE":"50-10","ROWS":0,"RECORD":[]}]
     */

    public String NAME;
    public String USERID;
    public List<RECORDBean> RECORDList;
    /**
     * FALLRANGE : 95-30
     * ROWS : 0
     * RECORD : [{"ACODE":"571","ACTIVITYDATE":"2016-09-27 13:57:19","AID":"K02G-1-14-1-012-1-1","ANAME":"杭州","APADDR":"119","APIP":"192.168.0.195","BARCODE":"0000000513760","BATTERY":3008,"GCODE":"","GNAME":"M&M'Sm&ms迷你筒装巧克力30.6g30.6g","LASTRETDATE":"2016-09-21 15:30:32","LQI":-31,"PCODE":"8601","PORT":"57345","SID":"571002001","SNAME":"濮家店","TINYIP":"0.0.75.150"}]
     */


    public static class RECORDBean {
        public String FALLRANGE;
        public int ROWS;
        /**
         * ACODE : 571
         * ACTIVITYDATE : 2016-09-27 13:57:19
         * AID : K02G-1-14-1-012-1-1
         * ANAME : 杭州
         * APADDR : 119
         * APIP : 192.168.0.195
         * BARCODE : 0000000513760
         * BATTERY : 3008
         * GCODE :
         * GNAME : M&M'Sm&ms迷你筒装巧克力30.6g30.6g
         * LASTRETDATE : 2016-09-21 15:30:32
         * LQI : -31
         * PCODE : 8601
         * PORT : 57345
         * SID : 571002001
         * SNAME : 濮家店
         * TINYIP : 0.0.75.150
         */

        public List<RECORDBean.Bean> RECORD;

        public static class Bean {
            public String ACODE;
            public String ACTIVITYDATE;
            public String AID;
            public String ANAME;
            public String APADDR;
            public String APIP;
            public String BARCODE;
            public int BATTERY;
            public String GCODE;
            public String GNAME;
            public String LASTRETDATE;
            public int LQI;
            public String PCODE;
            public String PORT;
            public String SID;
            public String SNAME;
            public String TINYIP;
        }
    }
}
