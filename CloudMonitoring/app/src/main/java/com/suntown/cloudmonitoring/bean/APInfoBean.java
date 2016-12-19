package com.suntown.cloudmonitoring.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class APInfoBean implements Parcelable {


    /**
     * NAME :
     * ROWS : 24
     * USERID : admin
     * RECORD : [{"ACODE":"571","ANAME":"杭州","APADDR":"1","APIP":"192.168.0.87","EDCOUNT":81,"HWTYPE":0,"HWVERSION":"0","LASTDATE":"2016-10-19 17:22:37","PCODE":"-1","PORT":"57345","SID":"571002002","SN":"","SNAME":"施家桥店","STATUS":1,"SWVERSION":"52"},{"ACODE":"571","ANAME":"杭州","APADDR":"3","APIP":"192.168.0.119","EDCOUNT":22,"HWTYPE":0,"HWVERSION":"0","LASTDATE":"2016-08-02 11:32:19","PCODE":"-1","PORT":"57345","SID":"571002002","SN":"","SNAME":"施家桥店","STATUS":0,"SWVERSION":"28"},{"ACODE":"571","ANAME":"杭州","APADDR":"4","APIP":"192.168.0.186","EDCOUNT":3,"HWTYPE":0,"HWVERSION":"0","LASTDATE":"2016-07-18 12:41:27","PCODE":"-1","PORT":"57345","SID":"571002002","SN":"","SNAME":"施家桥店","STATUS":0,"SWVERSION":"27"},{"ACODE":"571","ANAME":"杭州","APADDR":"84","APIP":"192.168.0.193","EDCOUNT":0,"HWTYPE":0,"HWVERSION":"","LASTDATE":"","PCODE":"-1","PORT":"1000","SID":"571002002","SN":"","SNAME":"施家桥店","STATUS":1,"SWVERSION":""},{"ACODE":"571","ANAME":"杭州","APADDR":"89","APIP":"192.168.0.188","EDCOUNT":0,"HWTYPE":0,"HWVERSION":"0","LASTDATE":"2016-07-21 15:27:28","PCODE":"-1","PORT":"57345","SID":"571002002","SN":"","SNAME":"施家桥店","STATUS":0,"SWVERSION":"13"},{"ACODE":"571","ANAME":"杭州","APADDR":"95","APIP":"192.168.0.173","EDCOUNT":0,"HWTYPE":0,"HWVERSION":"0","LASTDATE":"2016-10-18 17:30:55","PCODE":"-1","PORT":"57345","SID":"571003046","SN":"","SNAME":"长龙加油站","STATUS":0,"SWVERSION":"27"},{"ACODE":"571","ANAME":"杭州","APADDR":"94","APIP":"192.168.0.93","EDCOUNT":0,"HWTYPE":0,"HWVERSION":"0","LASTDATE":"2016-06-29 08:51:24","PCODE":"-1","PORT":"57347","SID":"571002032","SN":"","SNAME":"图特","STATUS":0,"SWVERSION":"28"},{"ACODE":"571","ANAME":"杭州","APADDR":"97","APIP":"192.168.0.156","EDCOUNT":0,"HWTYPE":0,"HWVERSION":"0","LASTDATE":"2016-07-21 17:29:13","PCODE":"-1","PORT":"57345","SID":"571002002","SN":"","SNAME":"施家桥店","STATUS":0,"SWVERSION":"27"},{"ACODE":"571","ANAME":"杭州","APADDR":"98","APIP":"192.168.0.185","EDCOUNT":0,"HWTYPE":0,"HWVERSION":"","LASTDATE":"","PCODE":"-1","PORT":"1000","SID":"571002002","SN":"","SNAME":"施家桥店","STATUS":1,"SWVERSION":""},{"ACODE":"571","ANAME":"杭州","APADDR":"103","APIP":"192.168.0.189","EDCOUNT":2,"HWTYPE":0,"HWVERSION":"0","LASTDATE":"2016-09-18 16:30:45","PCODE":"-1","PORT":"57345","SID":"571002032","SN":"","SNAME":"图特","STATUS":0,"SWVERSION":"27"},{"ACODE":"571","ANAME":"杭州","APADDR":"105","APIP":"192.168.0.186","EDCOUNT":0,"HWTYPE":0,"HWVERSION":"0","LASTDATE":"2016-08-01 15:29:45","PCODE":"-1","PORT":"57345","SID":"571002002","SN":"","SNAME":"施家桥店","STATUS":0,"SWVERSION":"28"},{"ACODE":"571","ANAME":"杭州","APADDR":"104","APIP":"192.168.0.119","EDCOUNT":4,"HWTYPE":0,"HWVERSION":"0","LASTDATE":"2016-08-02 10:55:32","PCODE":"-1","PORT":"57345","SID":"571002002","SN":"","SNAME":"施家桥店","STATUS":0,"SWVERSION":"28"},{"ACODE":"571","ANAME":"杭州","APADDR":"108","APIP":"192.168.0.186","EDCOUNT":6,"HWTYPE":0,"HWVERSION":"0","LASTDATE":"2016-09-22 09:12:35","PCODE":"-1","PORT":"57345","SID":"571002005","SN":"","SNAME":"乐购新塘景芳店","STATUS":0,"SWVERSION":"28"},{"ACODE":"571","ANAME":"杭州","APADDR":"109","APIP":"192.168.0.127","EDCOUNT":0,"HWTYPE":0,"HWVERSION":"","LASTDATE":"","PCODE":"-1","PORT":"1000","SID":"571002002","SN":"","SNAME":"施家桥店","STATUS":1,"SWVERSION":""},{"ACODE":"571","ANAME":"杭州","APADDR":"120","APIP":"192.168.0.172","EDCOUNT":43,"HWTYPE":0,"HWVERSION":"0","LASTDATE":"2016-10-19 15:08:11","PCODE":"-1","PORT":"57345","SID":"571002007","SN":"","SNAME":"乐木几杭州武林广场店","STATUS":0,"SWVERSION":"27"},{"ACODE":"571","ANAME":"杭州","APADDR":"111","APIP":"192.168.0.184","EDCOUNT":0,"HWTYPE":0,"HWVERSION":"0","LASTDATE":"2016-08-28 16:03:28","PCODE":"-1","PORT":"57345","SID":"999990067","SN":"","SNAME":"测试门店67","STATUS":0,"SWVERSION":"28"},{"ACODE":"571","ANAME":"杭州","APADDR":"114","APIP":"192.168.0.184","EDCOUNT":0,"HWTYPE":0,"HWVERSION":"0","LASTDATE":"2016-09-13 14:43:56","PCODE":"-1","PORT":"57345","SID":"571003001","SN":"","SNAME":"支付宝","STATUS":0,"SWVERSION":"28"},{"ACODE":"571","ANAME":"杭州","APADDR":"115","APIP":"192.168.0.186","EDCOUNT":0,"HWTYPE":0,"HWVERSION":"0","LASTDATE":"2016-08-29 13:41:19","PCODE":"-1","PORT":"57348","SID":"999990067","SN":"","SNAME":"测试门店67","STATUS":0,"SWVERSION":"27"},{"ACODE":"571","ANAME":"杭州","APADDR":"116","APIP":"192.168.0.154","EDCOUNT":1,"HWTYPE":0,"HWVERSION":"0","LASTDATE":"2016-09-27 13:03:14","PCODE":"-1","PORT":"57345","SID":"571003050","SN":"","SNAME":"北田商贸文二西路店","STATUS":0,"SWVERSION":"13"},{"ACODE":"571","ANAME":"杭州","APADDR":"118","APIP":"192.168.0.70","EDCOUNT":1,"HWTYPE":0,"HWVERSION":"0","LASTDATE":"2016-09-18 14:41:51","PCODE":"-1","PORT":"57426","SID":"999990100","SN":"","SNAME":"测试门店100","STATUS":0,"SWVERSION":"27"},{"ACODE":"571","ANAME":"杭州","APADDR":"119","APIP":"192.168.0.195","EDCOUNT":1,"HWTYPE":0,"HWVERSION":"0","LASTDATE":"2016-09-21 15:31:35","PCODE":"-1","PORT":"57345","SID":"571002001","SN":"","SNAME":"濮家店","STATUS":0,"SWVERSION":"27"},{"ACODE":"571","ANAME":"杭州","APADDR":"121","APIP":"192.168.0.157","EDCOUNT":3,"HWTYPE":0,"HWVERSION":"0","LASTDATE":"2016-09-26 10:12:19","PCODE":"-1","PORT":"57345","SID":"571002002","SN":"","SNAME":"施家桥店","STATUS":0,"SWVERSION":"27"},{"ACODE":"571","ANAME":"杭州","APADDR":"122","APIP":"192.168.0.179","EDCOUNT":3,"HWTYPE":0,"HWVERSION":"0","LASTDATE":"2016-09-27 11:30:35","PCODE":"-1","PORT":"57345","SID":"571003051","SN":"","SNAME":"升腾支付宝","STATUS":0,"SWVERSION":"27"},{"ACODE":"577","ANAME":"温州","APADDR":"123","APIP":"192.168.0.179","EDCOUNT":2,"HWTYPE":0,"HWVERSION":"0","LASTDATE":"2016-09-27 08:55:59","PCODE":"-1","PORT":"57345","SID":"577000003","SN":"","SNAME":"温州由你秀","STATUS":0,"SWVERSION":"27"}]
     */

    public String NAME;
    public int ROWS;
    public String USERID;
    /**
     * ACODE : 571
     * ANAME : 杭州
     * APADDR : 1
     * APIP : 192.168.0.87
     * EDCOUNT : 81
     * HWTYPE : 0
     * HWVERSION : 0
     * LASTDATE : 2016-10-19 17:22:37
     * PCODE : -1
     * PORT : 57345
     * SID : 571002002
     * SN :
     * SNAME : 施家桥店
     * STATUS : 1
     * SWVERSION : 52
     */

    public List<RECORDBean> RECORD;

    @Override
    public String toString() {
        return "APInfoBean{" +
                "NAME='" + NAME + '\'' +
                ", ROWS=" + ROWS +
                ", USERID='" + USERID + '\'' +
                ", RECORD=" + RECORD +
                '}';
    }


    public static class RECORDBean implements Parcelable {
        public String ACODE;
        public String ANAME;
        public String APADDR;
        public String APIP;
        public int EDCOUNT;
        public int HWTYPE;
        public String HWVERSION;
        public String LASTDATE;
        public String PCODE;
        public String PORT;
        public String SID;
        public String SN;
        public String SNAME;
        public int STATUS;
        public String SWVERSION;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.ACODE);
            dest.writeString(this.ANAME);
            dest.writeString(this.APADDR);
            dest.writeString(this.APIP);
            dest.writeInt(this.EDCOUNT);
            dest.writeInt(this.HWTYPE);
            dest.writeString(this.HWVERSION);
            dest.writeString(this.LASTDATE);
            dest.writeString(this.PCODE);
            dest.writeString(this.PORT);
            dest.writeString(this.SID);
            dest.writeString(this.SN);
            dest.writeString(this.SNAME);
            dest.writeInt(this.STATUS);
            dest.writeString(this.SWVERSION);
        }

        public RECORDBean() {
        }

        protected RECORDBean(Parcel in) {
            this.ACODE = in.readString();
            this.ANAME = in.readString();
            this.APADDR = in.readString();
            this.APIP = in.readString();
            this.EDCOUNT = in.readInt();
            this.HWTYPE = in.readInt();
            this.HWVERSION = in.readString();
            this.LASTDATE = in.readString();
            this.PCODE = in.readString();
            this.PORT = in.readString();
            this.SID = in.readString();
            this.SN = in.readString();
            this.SNAME = in.readString();
            this.STATUS = in.readInt();
            this.SWVERSION = in.readString();
        }

        public static final Creator<RECORDBean> CREATOR = new Creator<RECORDBean>() {
            @Override
            public RECORDBean createFromParcel(Parcel source) {
                return new RECORDBean(source);
            }

            @Override
            public RECORDBean[] newArray(int size) {
                return new RECORDBean[size];
            }
        };

        @Override
        public String toString() {
            return "RECORDBean{" +
                    "ACODE='" + ACODE + '\'' +
                    ", ANAME='" + ANAME + '\'' +
                    ", APADDR='" + APADDR + '\'' +
                    ", APIP='" + APIP + '\'' +
                    ", EDCOUNT=" + EDCOUNT +
                    ", HWTYPE=" + HWTYPE +
                    ", HWVERSION='" + HWVERSION + '\'' +
                    ", LASTDATE='" + LASTDATE + '\'' +
                    ", PCODE='" + PCODE + '\'' +
                    ", PORT='" + PORT + '\'' +
                    ", SID='" + SID + '\'' +
                    ", SN='" + SN + '\'' +
                    ", SNAME='" + SNAME + '\'' +
                    ", STATUS=" + STATUS +
                    ", SWVERSION='" + SWVERSION + '\'' +
                    '}';
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.NAME);
        dest.writeInt(this.ROWS);
        dest.writeString(this.USERID);
        dest.writeList(this.RECORD);
    }

    public APInfoBean() {
    }

    protected APInfoBean(Parcel in) {
        this.NAME = in.readString();
        this.ROWS = in.readInt();
        this.USERID = in.readString();
        this.RECORD = new ArrayList<RECORDBean>();
        in.readList(this.RECORD, RECORDBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<APInfoBean> CREATOR = new Parcelable.Creator<APInfoBean>() {
        @Override
        public APInfoBean createFromParcel(Parcel source) {
            return new APInfoBean(source);
        }

        @Override
        public APInfoBean[] newArray(int size) {
            return new APInfoBean[size];
        }
    };
}
