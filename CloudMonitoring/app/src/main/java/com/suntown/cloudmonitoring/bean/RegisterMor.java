package com.suntown.cloudmonitoring.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */

public class RegisterMor implements Parcelable {
    /**
     * NAME : 
     * ROWS : 51
     * USERID : admin
     * RECORD :[]
     */

    public String NAME;
    public int ROWS;
    public String USERID;
    public List<RECORDBean> RECORD;
    /**
     * ACODE : 571
     * ACTIVITYDATE : 2016-09-27 15:05:53
     * AID : K02G-1-14-1-012-1-1
     * ANAME : 杭州
     * APADDR : 119
     * APIP : 
     * BARCODE : 0000000513760
     * BATTERY : 3008
     * GCODE : 437535
     * GNAME : M&M'Sm&ms迷你筒装巧克力30.6g30.6g
     * LASTRETDATE : 2016-09-21 15:30:32
     * LQI : -31
     * PCODE : 8601
     * PORT : 57345
     * SID : 571002001
     * SNAME : 濮家店
     * TINYIP : 0.0.75.150
     */

    public static class RECORDBean implements Parcelable {
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

        @Override
        public String toString() {
            return "RECORDBean{" +
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
                    ", LASTRETDATE='" + LASTRETDATE + '\'' +
                    ", LQI=" + LQI +
                    ", PCODE='" + PCODE + '\'' +
                    ", PORT='" + PORT + '\'' +
                    ", SID='" + SID + '\'' +
                    ", SNAME='" + SNAME + '\'' +
                    ", TINYIP='" + TINYIP + '\'' +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.ACODE);
            dest.writeString(this.ACTIVITYDATE);
            dest.writeString(this.AID);
            dest.writeString(this.ANAME);
            dest.writeString(this.APADDR);
            dest.writeString(this.APIP);
            dest.writeString(this.BARCODE);
            dest.writeInt(this.BATTERY);
            dest.writeString(this.GCODE);
            dest.writeString(this.GNAME);
            dest.writeString(this.LASTRETDATE);
            dest.writeInt(this.LQI);
            dest.writeString(this.PCODE);
            dest.writeString(this.PORT);
            dest.writeString(this.SID);
            dest.writeString(this.SNAME);
            dest.writeString(this.TINYIP);
        }

        public RECORDBean() {
        }

        protected RECORDBean(Parcel in) {
            this.ACODE = in.readString();
            this.ACTIVITYDATE = in.readString();
            this.AID = in.readString();
            this.ANAME = in.readString();
            this.APADDR = in.readString();
            this.APIP = in.readString();
            this.BARCODE = in.readString();
            this.BATTERY = in.readInt();
            this.GCODE = in.readString();
            this.GNAME = in.readString();
            this.LASTRETDATE = in.readString();
            this.LQI = in.readInt();
            this.PCODE = in.readString();
            this.PORT = in.readString();
            this.SID = in.readString();
            this.SNAME = in.readString();
            this.TINYIP = in.readString();
        }

        public static final Parcelable.Creator<RECORDBean> CREATOR = new Parcelable.Creator<RECORDBean>() {
            @Override
            public RECORDBean createFromParcel(Parcel source) {
                return new RECORDBean(source);
            }

            @Override
            public RECORDBean[] newArray(int size) {
                return new RECORDBean[size];
            }
        };
    }
//    RECORD : [{"ACODE":"571","ACTIVITYDATE":"2016-09-27 15:05:53","AID":"K02G-1-14-1-012-1-1","ANAME":"杭州","APADDR":"119","APIP":"","BARCODE":"0000000513760","BATTERY":3008,"GCODE":"437535","GNAME":"M&M'Sm&ms迷你筒装巧克力30.6g30.6g","LASTRETDATE":"2016-09-21 15:30:32","LQI":-31,"PCODE":"8601","PORT":"57345","SID":"571002001","SNAME":"濮家店","TINYIP":"0.0.75.150"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-09-27 11:15:45","AID":"543210000000000-01-06","ANAME":"杭州","APADDR":"108","APIP":"","BARCODE":"6909409012802","BATTERY":3104,"GCODE":"549031","GNAME":"上好佳草莓栗米条40克","LASTRETDATE":"2016-09-22 09:08:02","LQI":-53,"PCODE":"8601","PORT":"57345","SID":"571002005","SNAME":"乐购新塘景芳店","TINYIP":"0.0.59.72"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-09-27 11:35:50","AID":"543210000000000-01-03","ANAME":"杭州","APADDR":"108","APIP":"","BARCODE":"6917878005508","BATTERY":3232,"GCODE":"571095","GNAME":"雀巢鹰唛炼奶巧克力味支装185g","LASTRETDATE":"2016-09-22 09:08:22","LQI":-55,"PCODE":"8601","PORT":"57345","SID":"571002005","SNAME":"乐购新塘景芳店","TINYIP":"0.0.59.190"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-09-27 11:18:05","AID":"543210000000000-01-05","ANAME":"杭州","APADDR":"108","APIP":"","BARCODE":"6917878005508","BATTERY":3040,"GCODE":"571095","GNAME":"雀巢鹰唛炼奶巧克力味支装185g","LASTRETDATE":"2016-09-22 09:08:32","LQI":-61,"PCODE":"8601","PORT":"57345","SID":"571002005","SNAME":"乐购新塘景芳店","TINYIP":"0.0.32.102"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-09-27 13:03:00","AID":"543210000000000-01-01","ANAME":"杭州","APADDR":"108","APIP":"","BARCODE":"6909409012802","BATTERY":3008,"GCODE":"549031","GNAME":"上好佳草莓栗米条40克","LASTRETDATE":"2016-09-22 09:08:37","LQI":-55,"PCODE":"8601","PORT":"57345","SID":"571002005","SNAME":"乐购新塘景芳店","TINYIP":"0.0.59.198"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:23","AID":"LMJHZ0001-1-002-02-02","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518814115","BATTERY":2912,"GCODE":"T14518814115","GNAME":"C.Smart 智能面板（三路）","LASTRETDATE":"2016-01-04 23:42:19","LQI":-66,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.69"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:12","AID":"LMJHZ0001-1-002-03-05","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518846555","BATTERY":3008,"GCODE":"T14518846555","GNAME":"暴风魔镜4代（黄金版）-送价值100元魔豆3个月延保专用立体声耳机","LASTRETDATE":"2016-01-04 23:42:20","LQI":-64,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.30"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:04","AID":"LMJHZ0001-1-002-01-01","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518851168","BATTERY":2912,"GCODE":"T14518851168","GNAME":"华为手表 黑色尖尾真皮皮带42mm","LASTRETDATE":"2016-01-04 23:42:28","LQI":-70,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.50.254"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:17","AID":"LMJHZ0001-1-002-01-02","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14417865569","BATTERY":2976,"GCODE":"T14417865569","GNAME":"华为B2手环 商务版","LASTRETDATE":"2016-01-04 23:42:52","LQI":-73,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.50"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:26","AID":"LMJHZ0001-1-002-01-03","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518855195","BATTERY":3008,"GCODE":"T14518855195","GNAME":"Galaxy ZEGA 银河战甲 智能对战坦克充电遥控车","LASTRETDATE":"2016-01-04 23:42:54","LQI":-59,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.80"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:19:51","AID":"LMJHZ0001-1-002-02-03","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518815343","BATTERY":3040,"GCODE":"T14518815343","GNAME":"C.Smart 智能插座","LASTRETDATE":"2016-01-04 23:42:58","LQI":-59,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.50.212"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:23","AID":"LMJHZ0001-1-002-01-05","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518858802","BATTERY":2976,"GCODE":"T14518858802","GNAME":"mifa M8无线蓝牙音箱","LASTRETDATE":"2016-01-04 23:43:04","LQI":-60,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.68"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:12","AID":"LMJHZ0001-1-002-02-05","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518819821","BATTERY":2976,"GCODE":"T14518819821","GNAME":"C.Smart 智能门锁中控套餐","LASTRETDATE":"2016-01-04 23:43:09","LQI":-65,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.28"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:27","AID":"LMJHZ0001-1-002-02-01","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518812701","BATTERY":3040,"GCODE":"T14518812701","GNAME":"C.smart 智能中控主机","LASTRETDATE":"2016-01-04 23:43:14","LQI":-65,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.86"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:05","AID":"LMJHZ0001-1-002-02-04","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518816677","BATTERY":2944,"GCODE":"T14518816677","GNAME":"C.Smart 智能红外收发器","LASTRETDATE":"2016-01-04 23:43:21","LQI":-76,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.1"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:07","AID":"LMJHZ0001-1-002-03-01","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518820724","BATTERY":2976,"GCODE":"T14518820724","GNAME":"C.Smart 智能窗帘开关","LASTRETDATE":"2016-01-04 23:43:23","LQI":-64,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.12"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:09","AID":"LMJHZ0001-1-002-01-04","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518857102","BATTERY":3008,"GCODE":"T14518857102","GNAME":"Gamesir小鸡游戏手柄G3增强套装版","LASTRETDATE":"2016-01-04 23:43:25","LQI":-70,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.18"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:19","AID":"LMJHZ0001-1-002-03-03","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14302735661","BATTERY":3072,"GCODE":"T14302735661","GNAME":"手机自拍杆 一体式折叠自拍神器","LASTRETDATE":"2016-01-04 23:43:37","LQI":-72,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.57"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:16","AID":"LMJHZ0001-1-002-03-02","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518824147","BATTERY":2944,"GCODE":"T14518824147","GNAME":"C.Smart DWC-102无线双向门磁探测器","LASTRETDATE":"2016-01-04 23:43:39","LQI":-70,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.45"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:24","AID":"LMJHZ0001-1-002-03-04","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518829622","BATTERY":2976,"GCODE":"T14518829622","GNAME":"网易青果摄像头","LASTRETDATE":"2016-01-04 23:43:50","LQI":-74,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.74"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:18","AID":"LMJHZ0001-1-003-02-04","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518816677","BATTERY":3008,"GCODE":"T14518816677","GNAME":"C.Smart 智能红外收发器","LASTRETDATE":"2016-01-05 11:56:21","LQI":-58,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.54"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:04","AID":"LMJHZ0001-1-003-02-01","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518812701","BATTERY":3040,"GCODE":"T14518812701","GNAME":"C.smart 智能中控主机","LASTRETDATE":"2016-01-05 11:56:34","LQI":-63,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.50.253"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:02","AID":"LMJHZ0001-1-003-01-05","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518858802","BATTERY":2976,"GCODE":"T14518858802","GNAME":"mifa M8无线蓝牙音箱","LASTRETDATE":"2016-01-05 11:56:43","LQI":-74,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.50.248"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:05","AID":"LMJHZ0001-1-003-02-05","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518819821","BATTERY":3008,"GCODE":"T14518819821","GNAME":"C.Smart 智能门锁中控套餐","LASTRETDATE":"2016-01-05 11:56:44","LQI":-63,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.4"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:13","AID":"LMJHZ0001-1-003-01-04","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518857102","BATTERY":2976,"GCODE":"T14518857102","GNAME":"Gamesir小鸡游戏手柄G3增强套装版","LASTRETDATE":"2016-01-05 11:56:46","LQI":-58,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.33"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:14","AID":"LMJHZ0001-1-003-02-02","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518814115","BATTERY":3008,"GCODE":"T14518814115","GNAME":"C.Smart 智能面板（三路）","LASTRETDATE":"2016-01-05 11:56:52","LQI":-55,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.36"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:23","AID":"LMJHZ0001-1-003-03-01","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518820724","BATTERY":3040,"GCODE":"T14518820724","GNAME":"C.Smart 智能窗帘开关","LASTRETDATE":"2016-01-05 11:56:52","LQI":-65,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.70"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:09","AID":"LMJHZ0001-1-003-01-02","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14417865569","BATTERY":3072,"GCODE":"T14417865569","GNAME":"华为B2手环 商务版","LASTRETDATE":"2016-01-05 11:56:56","LQI":-60,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.20"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:19","AID":"LMJHZ0001-1-003-03-05","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518846555","BATTERY":2976,"GCODE":"T14518846555","GNAME":"暴风魔镜4代（黄金版）-送价值100元魔豆3个月延保专用立体声耳机","LASTRETDATE":"2016-01-05 11:56:56","LQI":-62,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.55"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:08","AID":"LMJHZ0001-1-003-03-02","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518824147","BATTERY":3008,"GCODE":"T14518824147","GNAME":"C.Smart DWC-102无线双向门磁探测器","LASTRETDATE":"2016-01-05 11:56:58","LQI":-58,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.16"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:29","AID":"LMJHZ0001-1-003-02-03","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518815343","BATTERY":2912,"GCODE":"T14518815343","GNAME":"C.Smart 智能插座","LASTRETDATE":"2016-01-05 11:56:58","LQI":-54,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.92"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:08","AID":"LMJHZ0001-1-003-03-04","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518829622","BATTERY":2976,"GCODE":"T14518829622","GNAME":"网易青果摄像头","LASTRETDATE":"2016-01-05 11:56:59","LQI":-62,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.13"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:15","AID":"LMJHZ0001-1-003-01-01","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518851168","BATTERY":3008,"GCODE":"T14518851168","GNAME":"华为手表 黑色尖尾真皮皮带42mm","LASTRETDATE":"2016-01-05 11:57:06","LQI":-63,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.41"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:31","AID":"LMJHZ0001-1-003-01-03","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518855195","BATTERY":3008,"GCODE":"T14518855195","GNAME":"Galaxy ZEGA 银河战甲 智能对战坦克充电遥控车","LASTRETDATE":"2016-01-05 11:57:10","LQI":-65,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.98"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:20:00","AID":"LMJHZ0001-1-003-03-03","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14302735661","BATTERY":2976,"GCODE":"T14302735661","GNAME":"手机自拍杆 一体式折叠自拍神器","LASTRETDATE":"2016-01-05 11:57:29","LQI":-75,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.50.243"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-05-10 16:46:37","AID":"LMJHZ0001-1-001-01-79","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14297688604","BATTERY":3104,"GCODE":"T14297688604","GNAME":"Easycare易事关怀  多用途随身空气净化伴侣","LASTRETDATE":"2016-01-06 13:06:30","LQI":-58,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.52.23"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-03-11 11:21:21","AID":"LMJHZ0001-1-004-02-02","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14496281593","BATTERY":2912,"GCODE":"T14496281593","GNAME":"品佳pincare 智能蓝牙双向防丢自拍器","LASTRETDATE":"2016-01-26 12:21:45","LQI":-80,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.37.19"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-04-12 19:40:26","AID":"LMJHZ0001-1-004-01-05","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518858802","BATTERY":2976,"GCODE":"T14518858802","GNAME":"mifa M8无线蓝牙音箱","LASTRETDATE":"2016-01-30 11:43:05","LQI":-36,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.24"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-04-12 11:56:21","AID":"LMJHZ0001-1-004-01-03","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"T14518855195","BATTERY":2912,"GCODE":"T14518855195","GNAME":"Galaxy ZEGA 银河战甲 智能对战坦克充电遥控车","LASTRETDATE":"2016-01-30 11:43:11","LQI":-28,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.50.236"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-09-22 08:51:18","AID":"LMJHZ0001-1-004-01-04","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"571002002001","BATTERY":2976,"GCODE":"571002002001","GNAME":"段X云","LASTRETDATE":"2016-09-21 15:50:13","LQI":-88,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.51.38"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-09-27 13:03:08","AID":"LMJHZ0001-1-002-03-07","ANAME":"杭州","APADDR":"120","APIP":"","BARCODE":"571002002001","BATTERY":3104,"GCODE":"571002002001","GNAME":"段X云","LASTRETDATE":"2016-09-21 15:50:30","LQI":-51,"PCODE":"8601","PORT":"57346","SID":"571002007","SNAME":"乐木几杭州武林广场店","TINYIP":"0.0.158.128"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-09-27 15:10:14","AID":"111111111111111-03-02","ANAME":"杭州","APADDR":"94","APIP":"","BARCODE":"0000023172","BATTERY":3136,"GCODE":"0000023172","GNAME":"中心静脉测压尺","LASTRETDATE":"2016-07-26 10:56:50","LQI":-29,"PCODE":"8601","PORT":"57347","SID":"571002032","SNAME":"图特","TINYIP":"0.0.60.247"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-09-27 13:45:25","AID":"111111111111111-03-04","ANAME":"杭州","APADDR":"94","APIP":"","BARCODE":"0000023172","BATTERY":3104,"GCODE":"0000023172","GNAME":"中心静脉测压尺","LASTRETDATE":"2016-07-26 10:56:59","LQI":-36,"PCODE":"8601","PORT":"57347","SID":"571002032","SNAME":"图特","TINYIP":"0.0.157.236"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-09-27 15:10:14","AID":"111111111111111-03-02","ANAME":"杭州","APADDR":"103","APIP":"","BARCODE":"0000023172","BATTERY":3136,"GCODE":"0000023172","GNAME":"中心静脉测压尺","LASTRETDATE":"2016-07-26 10:56:50","LQI":-29,"PCODE":"8601","PORT":"57345","SID":"571002032","SNAME":"图特","TINYIP":"0.0.60.247"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-09-27 13:45:25","AID":"111111111111111-03-04","ANAME":"杭州","APADDR":"103","APIP":"","BARCODE":"0000023172","BATTERY":3104,"GCODE":"0000023172","GNAME":"中心静脉测压尺","LASTRETDATE":"2016-07-26 10:56:59","LQI":-36,"PCODE":"8601","PORT":"57345","SID":"571002032","SNAME":"图特","TINYIP":"0.0.157.236"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-09-27 14:52:30","AID":"","ANAME":"杭州","APADDR":"122","APIP":"","BARCODE":"","BATTERY":3008,"GCODE":"","GNAME":"","LASTRETDATE":"2016-09-27 11:30:27","LQI":-60,"PCODE":"8601","PORT":"57345","SID":"571003051","SNAME":"升腾支付宝","TINYIP":"0.0.82.204"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-09-27 14:27:35","AID":"","ANAME":"杭州","APADDR":"122","APIP":"","BARCODE":"","BATTERY":3008,"GCODE":"","GNAME":"","LASTRETDATE":"2016-09-27 11:30:34","LQI":-54,"PCODE":"8601","PORT":"57345","SID":"571003051","SNAME":"升腾支付宝","TINYIP":"0.0.82.215"},{"$ref":"$[101]"},{"ACODE":"577","ACTIVITYDATE":"2016-09-27 15:03:04","AID":"577000003000001-01-01","ANAME":"温州","APADDR":"123","APIP":"","BARCODE":"T123456789","BATTERY":3008,"GCODE":"T123456789","GNAME":"suntown test good","LASTRETDATE":"2016-09-20 10:42:31","LQI":-54,"PCODE":"8601","PORT":"57345","SID":"577000003","SNAME":"温州由你秀","TINYIP":"0.0.62.183"},{"$ref":"$[101]"},{"ACODE":"577","ACTIVITYDATE":"2016-09-27 14:55:31","AID":"577000003000001-01-02","ANAME":"温州","APADDR":"123","APIP":"","BARCODE":"2008208820","BATTERY":2976,"GCODE":"2008208820","GNAME":"买买买","LASTRETDATE":"2016-09-27 08:00:33","LQI":-93,"PCODE":"8601","PORT":"57345","SID":"577000003","SNAME":"温州由你秀","TINYIP":"0.0.82.116"},{"$ref":"$[101]"},{"ACODE":"577","ACTIVITYDATE":"2016-09-27 14:56:46","AID":"577000003000001-01-03","ANAME":"温州","APADDR":"123","APIP":"","BARCODE":"T123456789","BATTERY":2944,"GCODE":"T123456789","GNAME":"suntown test good","LASTRETDATE":"2016-09-27 08:00:42","LQI":-99,"PCODE":"8601","PORT":"57345","SID":"577000003","SNAME":"温州由你秀","TINYIP":"0.0.82.202"},{"$ref":"$[101]"},{"ACODE":"571","ACTIVITYDATE":"2016-09-27 10:45:43","AID":"","ANAME":"杭州","APADDR":"118","APIP":"","BARCODE":"","BATTERY":2976,"GCODE":"","GNAME":"","LASTRETDATE":"2016-09-18 14:38:44","LQI":-37,"PCODE":"8601","PORT":"57426","SID":"999990100","SNAME":"测试门店100","TINYIP":"0.0.59.49"},{"$ref":"$[101]"}]

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.NAME);
        dest.writeInt(this.ROWS);
        dest.writeString(this.USERID);
        dest.writeTypedList(this.RECORD);
    }

    public RegisterMor() {
    }

    protected RegisterMor(Parcel in) {
        this.NAME = in.readString();
        this.ROWS = in.readInt();
        this.USERID = in.readString();
        this.RECORD = in.createTypedArrayList(RECORDBean.CREATOR);
    }

    public static final Parcelable.Creator<RegisterMor> CREATOR = new Parcelable.Creator<RegisterMor>() {
        @Override
        public RegisterMor createFromParcel(Parcel source) {
            return new RegisterMor(source);
        }

        @Override
        public RegisterMor[] newArray(int size) {
            return new RegisterMor[size];
        }
    };
}
