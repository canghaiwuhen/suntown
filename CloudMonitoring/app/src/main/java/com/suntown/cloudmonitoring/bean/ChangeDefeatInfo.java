package com.suntown.cloudmonitoring.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */

public class ChangeDefeatInfo {

    /**
     * NAME : 
     * ROWS : 2
     * USERID : admin
     *[{"ACODE":"571","ACTIVITYDATE":"2016-09-26 15:44:53","AID":"571003051000001-01-01","ANAME":"杭州","APADDR":"122","APIP":"192.168.0.179","BARCODE":"6901845040951","BATTERY":2880,"GCODE":"","GNAME":"格力高百醇提拉米苏味-48g","LASTRETDATE":"2016-09-26 12:49:08","LQI":-71,"PCODE":"8601","PORT":"57346","SID":"571003051","SNAME":"升腾支付宝","TINYIP":"0.0.36.236"},{"ACODE":"571","ACTIVITYDATE":"2016-09-26 15:18:36","AID":"K02G-1-14-1-012-1-1","ANAME":"杭州","APADDR":"119","APIP":"192.168.0.195","BARCODE":"0000000513760","BATTERY":3008,"GCODE":"","GNAME":"M&M'Sm&ms迷你筒装巧克力30.6g30.6g","LASTRETDATE":"2016-09-21 15:30:32","LQI":-31,"PCODE":"8601","PORT":"57345","SID":"571002001","SNAME":"濮家店","TINYIP":"0.0.75.150"}]
    */
    public String NAME;
    public int ROWS;
    public String USERID;
    public List<RECORDBean> RECORD;
    /**
     * ACODE : 571
     * ACTIVITYDATE : 2016-09-26 15:11:14
     * AID : 571003051000001-01-01
     * ANAME : 杭州
     * APADDR : 122
     * APIP : 192.168.0.179
     * BARCODE : 6901845040951
     * BATTERY : 2880
     * GCODE :
     * GNAME : 格力高百醇提拉米苏味-48g"
     * LASTRETDATE : 2016-09-26 12:49:08
     * LQI : -71
     * PCODE : 8601
     * PORT : 57346
     * SID : 571003051
     * SNAME : 升腾支付宝
     * TINYIP : 0.0.36.236
     */


    public static class RECORDBean {
        public String ACODE;
        public String ACTIVITYDATE;
        public String AID;
        public String ANAME;
        public String APADDR;
        public String APIP;
        public String BARCODE;
        public int BATTERY;
        public int GCODE;
        public String GNAME;
        public String LASTRETDATE;
        public int LQI;
        public String PCODE;
        public String PORT;
        public String SID;
        public String SNAME;
        public String TINYIP;

        public RECORDBean(String ACODE, String ACTIVITYDATE, String AID, String ANAME, String APADDR, String APIP, String BARCODE, int BATTERY, int GCODE, String GNAME, String LASTRETDATE, int LQI, String PCODE, String PORT, String SID, String SNAME, String TINYIP) {
            this.ACODE = ACODE;
            this.ACTIVITYDATE = ACTIVITYDATE;
            this.AID = AID;
            this.ANAME = ANAME;
            this.APADDR = APADDR;
            this.APIP = APIP;
            this.BARCODE = BARCODE;
            this.BATTERY = BATTERY;
            this.GCODE = GCODE;
            this.GNAME = GNAME;
            this.LASTRETDATE = LASTRETDATE;
            this.LQI = LQI;
            this.PCODE = PCODE;
            this.PORT = PORT;
            this.SID = SID;
            this.SNAME = SNAME;
            this.TINYIP = TINYIP;
        }
    }
}
