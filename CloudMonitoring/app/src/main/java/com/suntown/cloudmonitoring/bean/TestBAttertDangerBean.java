package com.suntown.cloudmonitoring.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */

public class TestBAttertDangerBean {

    /**
     * NAME : 
     * USERID : admin
     * RECORD : [{"FALLRANGE":"95-30","ROWS":1,"RECORD":[{"ACODE":"571","ACTIVITYDATE":"2016-10-26 10:37:24","AID":"K02G-1-14-1-016-01-03","ANAME":"杭州","APADDR":"1","APIP":"192.168.0.87","BARCODE":"6902827110068","BATTERY":3008,"GCODE":"1141902","GNAME":"百事可乐百事轻怡可乐330ml","HWTYPE":0,"LASTRETDATE":"2016-10-26 10:37:23","LQI":-67,"PCODE":"-1","PORT":"57345","SID":"571002002","SNAME":"施家桥店","SWVERSION":"","TINYIP":"0.0.84.203"}]},{"FALLRANGE":"95-10","ROWS":1,"RECORD":[{"ACODE":"571","ACTIVITYDATE":"","AID":"K02G-1-14-1-022-01-04","ANAME":"杭州","APADDR":"","APIP":"","BARCODE":"6920459905548","BATTERY":5,"GCODE":"663359","GNAME":"康师傅每日C鲜果橙饮品1.5升","HWTYPE":0,"LASTRETDATE":"2014-03-09 15:19:01","LQI":31,"PCODE":"-1","PORT":"","SID":"571002002","SNAME":"施家桥店","SWVERSION":"","TINYIP":"192.57.3.101"}]},{"FALLRANGE":"80-30","ROWS":1,"RECORD":[{"ACODE":"571","ACTIVITYDATE":"","AID":"K02G-1-14-1-022-02-02","ANAME":"杭州","APADDR":"","APIP":"","BARCODE":"6920459989982","BATTERY":4,"GCODE":"1298757","GNAME":"康师傅酸梅汤饮品900ml","HWTYPE":0,"LASTRETDATE":"2014-03-09 15:19:01","LQI":7,"PCODE":"-1","PORT":"","SID":"571002002","SNAME":"施家桥店","SWVERSION":"","TINYIP":"192.57.3.172"}]},{"FALLRANGE":"80-10","ROWS":1,"RECORD":[{"ACODE":"571","ACTIVITYDATE":"","AID":"K02G-1-14-1-022-02-03","ANAME":"杭州","APADDR":"","APIP":"","BARCODE":"6920459989968","BATTERY":4,"GCODE":"1298758","GNAME":"康师傅酸枣汁饮品900毫升","HWTYPE":0,"LASTRETDATE":"2014-03-09 15:19:01","LQI":28,"PCODE":"-1","PORT":"","SID":"571002002","SNAME":"施家桥店","SWVERSION":"","TINYIP":"192.57.3.155"}]},{"FALLRANGE":"50-10","ROWS":1,"RECORD":[{"ACODE":"571","ACTIVITYDATE":"","AID":"K02G-1-14-1-022-02-04","ANAME":"杭州","APADDR":"","APIP":"","BARCODE":"6920459990179","BATTERY":5,"GCODE":"1229296","GNAME":"康师傅每日C水晶葡萄饮品900ml","HWTYPE":0,"LASTRETDATE":"2014-03-09 15:19:01","LQI":21,"PCODE":"-1","PORT":"","SID":"571002002","SNAME":"施家桥店","SWVERSION":"","TINYIP":"192.57.3.137"}]}]
     */

    public String NAME;
    public String USERID;
    /**
     * FALLRANGE : 95-30
     * ROWS : 1
     * RECORD : [{"ACODE":"571","ACTIVITYDATE":"2016-10-26 10:37:24","AID":"K02G-1-14-1-016-01-03","ANAME":"杭州","APADDR":"1","APIP":"192.168.0.87","BARCODE":"6902827110068","BATTERY":3008,"GCODE":"1141902","GNAME":"百事可乐百事轻怡可乐330ml","HWTYPE":0,"LASTRETDATE":"2016-10-26 10:37:23","LQI":-67,"PCODE":"-1","PORT":"57345","SID":"571002002","SNAME":"施家桥店","SWVERSION":"","TINYIP":"0.0.84.203"}]
     */

    public List<RECORDBean> RECORD;

    public static class RECORDBean {
        public String FALLRANGE;
        public int ROWS;
        /**
         * ACODE : 571
         * ACTIVITYDATE : 2016-10-26 10:37:24
         * AID : K02G-1-14-1-016-01-03
         * ANAME : 杭州
         * APADDR : 1
         * APIP : 192.168.0.87
         * BARCODE : 6902827110068
         * BATTERY : 3008
         * GCODE : 1141902
         * GNAME : 百事可乐百事轻怡可乐330ml
         * HWTYPE : 0
         * LASTRETDATE : 2016-10-26 10:37:23
         * LQI : -67
         * PCODE : -1
         * PORT : 57345
         * SID : 571002002
         * SNAME : 施家桥店
         * SWVERSION : 
         * TINYIP : 0.0.84.203
         */

        public List<RECORDBean.Bean> RECORD;

        @Override
        public String toString() {
            return "RECORDBean{" +
                    "FALLRANGE='" + FALLRANGE + '\'' +
                    ", ROWS=" + ROWS +
                    ", RECORD=" + RECORD +
                    '}';
        }


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
            public int HWTYPE;
            public String LASTRETDATE;
            public int LQI;
            public String PCODE;
            public String PORT;
            public String SID;
            public String SNAME;
            public String SWVERSION;
            public String TINYIP;

            @Override
            public String toString() {
                return "Bean{" +
                        "ACODE='" + ACODE + '\'' +
                        ", ACTIVITYDATE='" + ACTIVITYDATE + '\'' +
                        ", AID='" + AID + '\'' +
                        ", ANAME='" + ANAME + '\'' +
                        ", APADDR='" + APADDR + '\'' +
                        ", APIP='" + APIP + '\'' +
                        ", BARCODE='" + BARCODE + '\'' +
                        ", BATTERY=" + BATTERY +
                        ", GCODE='" + GCODE + '\'' +
                        ", GNAME='" + GNAME + '\'' +
                        ", HWTYPE=" + HWTYPE +
                        ", LASTRETDATE='" + LASTRETDATE + '\'' +
                        ", LQI=" + LQI +
                        ", PCODE='" + PCODE + '\'' +
                        ", PORT='" + PORT + '\'' +
                        ", SID='" + SID + '\'' +
                        ", SNAME='" + SNAME + '\'' +
                        ", SWVERSION='" + SWVERSION + '\'' +
                        ", TINYIP='" + TINYIP + '\'' +
                        '}';
            }
        }

    }
}
